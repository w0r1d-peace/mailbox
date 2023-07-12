package com.ruoyi.mailbox.service.handler.server;


import com.ruoyi.common.exception.mailbox.MailPlusException;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.WebProxy;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MyExchangeService implements IMailService {

    public MyExchangeService() {
    }

    public UniversalMail parseEmail(MailItem mailItem, String localSavePath) throws MailPlusException {
        return MailItemParser.parseMail(mailItem, localSavePath);
    }

    public List<MailItem> listAll(MailConn mailConn, String dateFormat, List<String> existUids) throws MailPlusException {
        try {
            FindItemsResults items = getItems(mailConn);
            ArrayList<Item> itemList = items.getItems();
            Iterator var17 = itemList.iterator();
            List<MailItem> mList = Collections.synchronizedList(new ArrayList<>());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            // 获取前五天的日期
            Date date = sdf.parse(dateFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 5);
            Date time = calendar.getTime();
            Integer fiveDaysBefore = Integer.parseInt(sdf.format(time));

            ExecutorService executor = Executors.newFixedThreadPool(5, new DefaultThreadFactory("exchange-pull-"));
            AtomicInteger numAi = new AtomicInteger(0);
            while(var17.hasNext()) {
                Item item = (Item)var17.next();
                if (item instanceof EmailMessage) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            /*// 跟临时表中所存在的uid进行匹配
                            if (existUids != null && !existUids.isEmpty()) {
                                if (existUids.contains(item.getId().getUniqueId())) continue;
                            }*/
                            try {
                                boolean b = false;
                                int num = numAi.get();
                                EmailMessage message = (EmailMessage)item;
                                Date receivedDate = message.getDateTimeSent();
                                String receivedDateFormat = sdf.format(receivedDate);
                                // 该条件主要是判断邮件列表它的时间不会存在一直有序的，所以有可能前一天会排在今天前面，所以这里我直接获取到前五天的时间。
                                // 其次就是避免在去获取时间太久前的邮件
                                if (fiveDaysBefore > Integer.parseInt(receivedDateFormat)) {
                                    executor.shutdown();
                                    return;
                                };

                                // 比较是否是当天的时间
                                if (dateFormat.equals(receivedDateFormat)) {
                                    if (!b) b = true;
                                    mList.add(MailItem.builder().exchangeMessage(message).build());
                                } else {
                                    if (num > 30) {
                                        executor.shutdown();
                                        return;
                                    }
                                    if (b) numAi.incrementAndGet();
                                }
                            } catch (Exception e) {
                                log.error("exchang - 获取邮件异常，异常原因：" +
                                        "\t" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
                executor.shutdown();
                while (true) {
                    if (executor.isTerminated()) {
                        break;
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return mList;
        } catch (Exception var21) {
            var21.printStackTrace();
            throw new MailPlusException(var21.getMessage());
        }
    }


    private FindItemsResults getItems(MailConn mailConn) throws Exception {
        ExchangeService exchangeService = mailConn.getExchangeService();
        Folder msgFolderRoot = Folder.bind(exchangeService, WellKnownFolderName.MsgFolderRoot);
        int childFolderCount = msgFolderRoot.getChildFolderCount();
        FolderView folderView = new FolderView(childFolderCount);
        FindFoldersResults folders = msgFolderRoot.findFolders(folderView);
        ArrayList<Folder> folderList = folders.getFolders();

        Folder folder = null;
        for(int i = 0; i < folderList.size(); i++) {
            folder = (Folder) folderList.get(i);
            String displayName = folder.getDisplayName();
            if (displayName.equals("Inbox")) break;
        }

        ItemView itemView = new ItemView(folder.getTotalCount());
        itemView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
        FindItemsResults items = exchangeService.findItems(folder.getId(), itemView);
        return items;
    }


    public MailConn createConn(MailConnCfg mailConnCfg, boolean proxy) throws MailPlusException {
        ExchangeService service = new ExchangeService();
        if (proxy) {
            WebProxy webProxy = new WebProxy(mailConnCfg.getProxyHost(), mailConnCfg.getProxyPort().intValue());
            service.setWebProxy(webProxy);
        }

        service.setCredentials(new WebCredentials(mailConnCfg.getEmail(), mailConnCfg.getPassword()));
        service.setTimeout(600000);

        try {
            if (StringUtils.isNotBlank(mailConnCfg.getHost()) && !mailConnCfg.getHost().equals("-")) {
                URI uri = new URI(mailConnCfg.getHost());
                service.setUrl(uri);
            } else {
                service.autodiscoverUrl(mailConnCfg.getEmail(), (redirectionUrl) -> {
                    return redirectionUrl.toLowerCase().startsWith("https://");
                });
            }
            mailConnCfg.setHost(service.getUrl().getHost());

            getItems(MailConn.builder().exchangeService(service).build());
            return MailConn.builder().exchangeService(service).build();
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new MailPlusException(var5.getMessage());
        }
    }
}

package com.ruoyi.mailbox.service.handler.server;

import com.ruoyi.common.enums.ProxyTypeEnum;
import com.ruoyi.common.exception.mailbox.MailPlusException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ImapService implements IMailService {

    public ImapService() {
    }

    public UniversalMail parseEmail(MailItem mailItem, String localSavePath) throws MailPlusException {
        return MailItemParser.parseMail(mailItem, localSavePath);
    }

    public List<MailItem> listAll(MailConn mailConn, String dateFormat, List<String> existUids) throws MailPlusException {
        IMAPStore imapStore = mailConn.getImapStore();

        try {
            Folder defaultFolder = imapStore.getDefaultFolder();
            List<MailItem> mList = Collections.synchronizedList(new ArrayList<>());
            Folder[] list = defaultFolder.list();
            boolean flag = false;

            for(int i = 0; i < list.length; ++i) {
                IMAPFolder imapFolder = (IMAPFolder)list[i];
                if (imapFolder.getName().equalsIgnoreCase("[gmail]"))
                    flag = this.listGmailMessageFolder(mList, imapFolder, dateFormat, existUids);
                 else
                    flag = this.listFolderMessage(mList, imapFolder, dateFormat, existUids);

                if (flag) break;
            }

            return mList;
        } catch (MessagingException var10) {
            String content = String.format("【IMAP服务】打开文件夹/获取邮件列表失败，错误信息【{%s}】", var10.getMessage());
            log.error("var10-------" + content + "----------");
            var10.printStackTrace();
            throw new MailPlusException(content);
        }
    }

    private boolean listGmailMessageFolder(List<MailItem> mList, IMAPFolder imapFolder, String dateFormat, List<String> existUids) throws MessagingException {
        Folder[] list = imapFolder.list();
        boolean flag = false;
        Folder[] var6 = list;
        int var7 = list.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Folder folder = var6[var8];
            flag = this.listFolderMessage(mList, (IMAPFolder)folder, dateFormat, existUids);
            if (flag) break;
        }

        return flag;
    }

    private boolean listFolderMessage(List<MailItem> mList, IMAPFolder imapFolder, String dateFormat, List<String> existUids) throws MessagingException {
        boolean flag = false;
        imapFolder.open(Folder.READ_ONLY);
        Message[] messages = imapFolder.getMessages();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // 获取前五天的日期
        Integer fiveDaysBefore = null;
        try {
            Date date = sdf.parse(dateFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 5);
            Date time = calendar.getTime();
            fiveDaysBefore = Integer.parseInt(sdf.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        ExecutorService executor = Executors.newFixedThreadPool(5, new DefaultThreadFactory("imap-pull-"));
        AtomicInteger indexAi = new AtomicInteger(messages.length);
        AtomicInteger numAi = new AtomicInteger(0);
        Integer finalFiveDaysBefore = fiveDaysBefore;
        while (true) {
            if (indexAi.get() == 0) break;

            executor.execute(() -> {
                try {
                    int i = indexAi.decrementAndGet();
                    if (i < 0) {
                        executor.shutdown();
                        return;
                    }
                    int num = numAi.get();
                    boolean b = false;
                    // 跟临时表中所存在的uid进行匹配
                    if (existUids != null && !existUids.isEmpty()) {
                        String uid = imapFolder.getFullName() + imapFolder.getUID(messages[i]);
                        if (existUids.contains(uid) && i == indexAi.get()) {
                            executor.shutdown();
                            return;
                        }
                    }
                    // Message message = messages[i];
                    IMAPMessage message = (IMAPMessage)messages[i];
                    Date receivedDate = message.getSentDate();
                    if (receivedDate == null) return;

                    String receivedDateFormat = sdf.format(receivedDate);

                    // 该条件主要是判断邮件列表它的时间不会存在一直有序的，所以有可能前一天会排在今天前面，所以这里我直接获取到前五天的时间。
                    // 其次就是避免在去获取时间太久前的邮件
                    if (finalFiveDaysBefore > Integer.parseInt(receivedDateFormat)) {
                        executor.shutdown();
                        return;
                    }

                    // 比较是否是当天的时间
                    if (dateFormat.equals(receivedDateFormat)) {
                        if (!b) b = true;
                        mList.add(MailItem.builder().imapMessage((IMAPMessage) messages[i]).build());
                    } else {
                        if (num > 30) {
                            executor.shutdown();
                            return;
                        }
                        if (b) numAi.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.error("imap - 获取邮件异常，异常原因：" +
                            "\t" + e.getMessage());
                    e.printStackTrace();
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
        return flag;
    }

    public MailConn createConn(MailConnCfg mailConnCfg, boolean proxy) throws MailPlusException {
        Properties properties = new Properties();
        properties.put("mail.imap.host", mailConnCfg.getHost());
        properties.put("mail.imap.port", mailConnCfg.getPort());
        properties.put("mail.imap.ssl.enable", mailConnCfg.isSsl());
        properties.put("mail.imap.partialfetch", false);
        properties.put("mail.imap.starttls.enable", false);
        properties.put("mail.imap.auth", true);
        if (proxy && mailConnCfg.getProxyType() != null) {
            ProxyTypeEnum proxyType = mailConnCfg.getProxyType();
            if (proxyType.equals(ProxyTypeEnum.SOCKS)) {
                properties.put("mail.imap.socks.host", mailConnCfg.getSocksProxyHost());
                properties.put("mail.imap.socks.port", mailConnCfg.getSocksProxyPort());
            } else if (proxyType.equals(ProxyTypeEnum.HTTP)) {
                properties.put("mail.imap.proxy.host", mailConnCfg.getProxyHost());
                properties.put("mail.imap.proxy.port", mailConnCfg.getProxyPort());
            }
        }

        Session session = Session.getInstance(properties);

        try {
            Store store = session.getStore("imap");
            store.connect(mailConnCfg.getEmail(), mailConnCfg.getPassword());
            return MailConn.builder().imapStore((IMAPStore)store).build();
        } catch (NoSuchProviderException var6) {
            log.error("var6-------" + var6.getMessage() + "----------");
            var6.printStackTrace();
            throw new MailPlusException(var6.getMessage());
        } catch (MessagingException var7) {
            log.error("var7-------" + var7.getMessage() + "----------");
            var7.printStackTrace();
            throw new MailPlusException(var7.getMessage());
        }
    }
}

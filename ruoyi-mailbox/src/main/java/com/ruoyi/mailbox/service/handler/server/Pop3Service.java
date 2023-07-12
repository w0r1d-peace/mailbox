package com.ruoyi.mailbox.service.handler.server;

import com.ruoyi.common.enums.ProxyTypeEnum;
import com.ruoyi.common.exception.mailbox.MailPlusException;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Message;
import com.sun.mail.pop3.POP3Store;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Pop3Service implements IMailService {

    public Pop3Service() {
    }

    public UniversalMail parseEmail(MailItem mailItem, String localSavePath) throws MailPlusException {
        return MailItemParser.parseMail(mailItem, localSavePath);
    }

    public List<MailItem> listAll(MailConn mailConn, String dateFormat, List<String> existUids) throws MailPlusException {
        POP3Store pop3Store = mailConn.getPop3Store();
        try {
            POP3Folder folder = (POP3Folder)pop3Store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            List<MailItem> mList = Collections.synchronizedList(new ArrayList());
            for (int i = 0; i < messages.length; i++) {
                try {
                    POP3Message message = (POP3Message)messages[i];
                    mList.add(MailItem.builder().pop3Message(message).build());
                } catch (Exception e) {
                    log.error("pop3 - 获取邮件异常，异常原因：" +
                            "\t" + e.getMessage());
                    e.printStackTrace();
                }
            }
            return mList;
        } catch (MessagingException var10) {
            var10.printStackTrace();
            throw new MailPlusException(String.format("【POP3服务】打开文件夹/获取邮件列表失败，错误信息【{}】"));
        }
    }

    public MailConn createConn(MailConnCfg mailConnCfg, boolean proxy) throws MailPlusException {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", mailConnCfg.getHost());
        properties.put("mail.pop3.port", mailConnCfg.getPort());
        properties.put("mail.pop3.ssl.enable", mailConnCfg.isSsl());
        properties.put("mail.pop3.auth", true);
        if (proxy && mailConnCfg.getProxyType() != null) {
            ProxyTypeEnum proxyType = mailConnCfg.getProxyType();
            if (proxyType.equals(ProxyTypeEnum.HTTP)) {
                properties.put("mail.pop3.proxy.host", mailConnCfg.getProxyHost());
                properties.put("mail.pop3.proxy.port", mailConnCfg.getProxyPort());
            } else if (proxyType.equals(ProxyTypeEnum.SOCKS)) {
                properties.put("mail.pop3.socks.host", mailConnCfg.getSocksProxyHost());
                properties.put("mail.pop3.socks.port", mailConnCfg.getSocksProxyPort());
            }
        }

        Session session = Session.getInstance(properties);

        try {
            Store store = session.getStore("pop3");
            store.connect(mailConnCfg.getEmail(), mailConnCfg.getPassword());
            return MailConn.builder().pop3Store((POP3Store)store).build();
        } catch (NoSuchProviderException var6) {
            var6.printStackTrace();
            throw new MailPlusException(var6.getMessage());
        } catch (MessagingException var7) {
            var7.printStackTrace();
            throw new MailPlusException(var7.getMessage());
        }
    }
}

package com.ruoyi.mailbox.service.handler.server;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.exception.mailbox.MailPlusException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Message;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.property.complex.*;
import microsoft.exchange.webservices.data.property.definition.PropertyDefinitionBase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class MailItemParser {

    public MailItemParser() {
    }

    public static UniversalMail parseMail(MailItem mailItem, String targetDir) throws MailPlusException {
        UniversalMail universalMail = null;
        String emlPath;
        if (mailItem.getPop3Message() != null) {
            POP3Message pop3Message = mailItem.getPop3Message();
            universalMail = parseMimeMessage(pop3Message);
            emlPath = saveMimiMessageAsLocalEml(pop3Message, targetDir);
            universalMail.setEmlPath(emlPath);
        } else if (mailItem.getImapMessage() != null) {
            IMAPMessage imapMessage = mailItem.getImapMessage();
            universalMail = parseMimeMessage(imapMessage);
            emlPath = saveMimiMessageAsLocalEml(imapMessage, targetDir);
            universalMail.setEmlPath(emlPath);
        } else if (mailItem.getExchangeMessage() != null) {
            universalMail = parseExchangeMail(mailItem.getExchangeMessage());
            emlPath = saveExchangeMailAsLocalEml(mailItem.getExchangeMessage(), targetDir);
            universalMail.setEmlPath(emlPath);
        }

        return universalMail;
    }

    public static Object isFileExits(String path, int index) {
        String temp = path;
        if(index != 1) temp = String.format(path + "（%s）", index);
        File file = new File(temp);
        if (!file.exists()) return temp;
        return isFileExits(path, ++index);
    }

    public static String saveMimiMessageAsLocalEml(MimeMessage mimeMessage, String targetDir) throws MailPlusException {
        try {
            String subject = mimeMessage.getSubject();
            subject = StringUtils.isEmpty(subject) ? "无主题" + System.currentTimeMillis() : subject;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (mimeMessage.getSentDate() == null) {
                return null;
            }

            String sendDate = sdf.format(mimeMessage.getSentDate());
            // 过滤主题中特殊的字符
            subject = filterSpecialStrings(subject);
            if (subject.length() > 100)
                subject = subject.substring(0, 100) + "...";
            Object fileExits = isFileExits(subject + "-" + sendDate, 1);
            File file = new File(targetDir.concat("/").concat(fileExits.toString()).concat(".eml"));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                mimeMessage.writeTo(new FileOutputStream(file));
            }

            return file.getAbsolutePath();
        } catch (MessagingException var4) {
            var4.printStackTrace();
            throw new MailPlusException(var4.getMessage());
        } catch (IOException var5) {
            var5.printStackTrace();
            throw new MailPlusException(var5.getMessage());
        }
    }

    public static List<UniversalAttachment> parseAttachment(MimeMessage mimeMessage, String targetDir) throws MailPlusException {
        try {
            MimeMessageParser parser = (new MimeMessageParser(mimeMessage)).parse();
            List<UniversalAttachment> list = new ArrayList();
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            List<DataSource> attachmentList = parser.getAttachmentList();
            Collection<String> contentIds = parser.getContentIds();
            Set<String> cidFile = new HashSet();
            Iterator var8 = contentIds.iterator();

            while(var8.hasNext()) {
                String cid = (String)var8.next();
                DataSource attachmentByCid = parser.findAttachmentByCid(cid);
                File file = new File(targetDir + "/" + attachmentByCid.getName());
                if (!file.exists()) {
                    file.createNewFile();
                    FileUtils.copyInputStreamToFile(attachmentByCid.getInputStream(), file);
                }

                UniversalAttachment universalAttachment = UniversalAttachment.builder().cid(cid).path(file.getAbsolutePath()).name(attachmentByCid.getName()).contentType(attachmentByCid.getContentType()).build();
                list.add(universalAttachment);
                cidFile.add(attachmentByCid.getName());
            }

            var8 = attachmentList.iterator();

            while(var8.hasNext()) {
                DataSource dataSource = (DataSource)var8.next();
                if (!cidFile.contains(dataSource.getName())) {
                    File file = new File(targetDir + "/" + dataSource.getName());
                    if (!file.exists()) {
                        file.createNewFile();
                        FileUtils.copyInputStreamToFile(dataSource.getInputStream(), file);
                    }

                    UniversalAttachment universalAttachment = UniversalAttachment.builder().cid((String)null).path(file.getAbsolutePath()).name(dataSource.getName()).contentType(dataSource.getContentType()).build();
                    list.add(universalAttachment);
                }
            }

            return list;
        } catch (Exception var13) {
            var13.printStackTrace();
            throw new MailPlusException(var13.getMessage());
        }
    }

    public static UniversalMail parseMimeMessage(MimeMessage mimeMessage) throws MailPlusException {
        try {
            Folder folder = mimeMessage.getFolder();
            if(!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }
            MimeMessageParser parser = (new MimeMessageParser(mimeMessage)).parse();
            /*String uid = "";
            if (mimeMessage instanceof IMAPMessage) {
                uid = folder.getName() + ((IMAPFolder)folder).getUID(mimeMessage);
            } else if (mimeMessage instanceof POP3Message) {
                uid = ((POP3Folder)folder).getUID(mimeMessage);
            } else {
                uid = UUID.randomUUID().toString();
            }

            if (StringUtils.isEmpty(uid)) {
                throw new MailPlusException("【MIME邮件解析】解析uid失败");
            } else {
                String subject = parser.getSubject();
                String body = parser.hasHtmlContent() ? parser.getHtmlContent() : parser.getPlainContent();
                UniversalMail universalMail = UniversalMail.builder().content(StringUtils.isEmpty(body) ? "" : EmojiParser.parseToAliases(body)).uid(uid).receiver(getMimeMessageAddressJson(parser.getTo())).title(StringUtils.isEmpty(subject) ? "<无主题>" : EmojiParser.parseToAliases(subject)).sendDate(mimeMessage.getSentDate()).hasRead(mimeMessage.getFlags().equals(Flag.SEEN)).hasAttachment(parser.hasAttachments()).fromer(parser.getFrom()).folder(folder != null ? folder.getName() : "手动导入").cc(getMimeMessageAddressJson(parser.getCc())).bcc(getMimeMessageAddressJson(parser.getBcc())).build();
                return universalMail;
            }*/

            String subject = parser.getSubject();
            String body = parser.hasHtmlContent() ? parser.getHtmlContent() : parser.getPlainContent();
            UniversalMail universalMail = UniversalMail.builder().content(StringUtils.isEmpty(body) ? "" : EmojiParser.parseToAliases(body)).receiver(getMimeMessageAddressJson(parser.getTo())).title(StringUtils.isEmpty(subject) ? "<无主题>" : EmojiParser.parseToAliases(subject)).sendDate(mimeMessage.getSentDate()).hasRead(mimeMessage.getFlags().equals(Flag.SEEN)).hasAttachment(parser.hasAttachments()).fromer(parser.getFrom()).folder(folder != null ? folder.getName() : "手动导入").cc(getMimeMessageAddressJson(parser.getCc())).bcc(getMimeMessageAddressJson(parser.getBcc())).build();
            return universalMail;
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new MailPlusException(var8.getMessage());
        }
    }

    public static String getMimeMessageAddressJson(List<Address> address) {
        List<UniversalRecipient> recipients = new ArrayList();

        for(int i = 0; i < address.size(); ++i) {
            InternetAddress internetAddress = (InternetAddress)address.get(i);
            UniversalRecipient build = UniversalRecipient.builder().name(StringUtils.isNotEmpty(internetAddress.getPersonal()) ? EmojiParser.parseToAliases(internetAddress.getPersonal()) : internetAddress.getAddress()).email(internetAddress.getAddress()).build();
            recipients.add(build);
        }

        return JSON.toJSONString(recipients);
    }

    public static String saveExchangeMailAsLocalEml(EmailMessage exchangeMessage, String targetDir) throws MailPlusException {
        try {
            File dir = new File(targetDir);
            if (dir.exists()) {
                dir.mkdirs();
            }

            exchangeMessage.load();
            String subject = exchangeMessage.getSubject();
            subject = StringUtils.isEmpty(subject) ? "<无主题>" + System.currentTimeMillis() : subject;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sendDate = sdf.format(exchangeMessage.getDateTimeSent());
            // 过滤主题中特殊的字符
            subject = filterSpecialStrings(subject);
            if (subject.length() > 100)
                subject = subject.substring(0, 100) + "...";
            Object fileExits = isFileExits(subject + "-" + sendDate, 1);
            File eml = new File(targetDir.concat("/").concat(fileExits.toString()).concat(".eml"));
            exchangeMessage.load(new PropertySet(new PropertyDefinitionBase[]{EmailMessageSchema.MimeContent, EmailMessageSchema.AllowedResponseActions}));
            if (!eml.exists()) {
                File parentFile = eml.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                eml.createNewFile();
                byte[] content = exchangeMessage.getMimeContent().getContent();
                FileUtils.writeByteArrayToFile(eml, content);
            }

            return eml.getAbsolutePath();
        } catch (Exception var7) {
            log.error("var7---------" + var7.getMessage() + "---------------");
            var7.printStackTrace();
            throw new MailPlusException(var7.getMessage());
        }
    }

    public static String filterSpecialStrings(String str) {
        String regEx = "[`~!^*+=|:;\\\\[\\\\]<>/?~……——+|'\"]";
        String rs = Pattern.compile(regEx).matcher(str).replaceAll("").trim();
        return rs;
    }

    public static List<UniversalAttachment> parseAttachment(AttachmentCollection attachments, String targetDir) {
        List<UniversalAttachment> universalAttachments = new ArrayList();
        List<Attachment> items = attachments.getItems();
        File dir = new File(targetDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Iterator var5 = items.iterator();

        while(var5.hasNext()) {
            Attachment attachment = (Attachment)var5.next();

            try {
                String path = targetDir + "/" + attachment.getName();
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                    if (attachment instanceof FileAttachment) {
                        ((FileAttachment)attachment).load(file.getAbsolutePath());
                    } else if (attachment instanceof ItemAttachment) {
                        ItemAttachment itemAttachment = (ItemAttachment)attachment;
                        itemAttachment.load(new PropertyDefinitionBase[]{ItemSchema.MimeContent});
                        Item item = itemAttachment.getItem();
                        FileUtils.writeByteArrayToFile(file, item.getMimeContent().getContent());
                    }
                }

                universalAttachments.add(UniversalAttachment.builder().cid(attachment.getContentId()).contentType(attachment.getContentType()).name(attachment.getName()).path(path).build());
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        }

        return universalAttachments;
    }

    public static UniversalMail parseExchangeMail(EmailMessage message) throws MailPlusException {
        try {
            message.load();
            String subject = message.getSubject();
            EmailAddress from = message.getFrom();
            String body = message.getBody().toString();
            Date dateTimeSent = null;

            try {
                dateTimeSent = message.getDateTimeSent();
            } catch (ServiceLocalException var7) {
                var7.printStackTrace();
            }

            UniversalMail.UniversalMailBuilder builder = UniversalMail.builder().bcc(getExchangeAddressJson(message.getBccRecipients())).cc(getExchangeAddressJson(message.getCcRecipients())).folder(microsoft.exchange.webservices.data.core.service.folder.Folder.bind(message.getService(), message.getParentFolderId()).getDisplayName()).fromer(from == null ? "<无发件人>" : from.getAddress()).hasAttachment(message.getHasAttachments()).hasRead(message.getIsRead()).sendDate(dateTimeSent).title(StringUtils.isAnyEmpty(new CharSequence[]{message.getSubject()}) ? "<无主题>" : EmojiParser.parseToAliases(subject)).receiver(getExchangeAddressJson(message.getToRecipients())).uid(message.getId().getUniqueId()).content(StringUtils.isEmpty(body) ? "" : EmojiParser.parseToAliases(body));
            UniversalMail universalMail = builder.build();
            return universalMail;
        } catch (ServiceLocalException var8) {
            log.error("var8---------" + var8.getMessage() + "---------------");
            var8.printStackTrace();
            throw new MailPlusException(var8.getMessage());
        } catch (Exception var9) {
            log.error("var9---------" + var9.getMessage() + "---------------");
            var9.printStackTrace();
            throw new MailPlusException(var9.getMessage());
        }
    }

    public static String getExchangeAddressJson(EmailAddressCollection recipients) {
        List<EmailAddress> items = recipients.getItems();
        List<UniversalRecipient> list = new ArrayList();
        Iterator var3 = items.iterator();

        while(var3.hasNext()) {
            EmailAddress emailAddress = (EmailAddress)var3.next();
            list.add(UniversalRecipient.builder().email(emailAddress.getAddress()).name(StringUtils.isNotEmpty(emailAddress.getName()) ? EmojiParser.parseToAliases(emailAddress.getName()) : emailAddress.getAddress()).build());
        }

        return JSON.toJSONString(list);
    }
}

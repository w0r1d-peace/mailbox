package com.ruoyi.mailbox.service.handler.server;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Message;
import lombok.Builder;
import lombok.Data;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;

/**
 * @author tangJM.
 * @date 2021/10/13
 * @description
 */
@Data
@Builder
public class MailItem {
    private IMAPMessage imapMessage;
    private POP3Message pop3Message;
    private EmailMessage exchangeMessage;
}


package com.ruoyi.mailbox.service.handler.server;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.pop3.POP3Store;
import lombok.Builder;
import lombok.Data;
import microsoft.exchange.webservices.data.core.ExchangeService;

/**
 * @author tangJM.
 * @date 2021/10/14
 * @description
 */
@Data
@Builder
public class MailConn {
    private POP3Store pop3Store;
    private IMAPStore imapStore;
    private ExchangeService exchangeService;
}

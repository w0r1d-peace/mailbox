package com.ruoyi.mailbox.service.handler.server;

import com.ruoyi.common.exception.mailbox.MailPlusException;

import java.util.List;

/**
 * @author tangJM.
 * @date 2021/10/13
 * @description
 */
public interface IMailService {
    UniversalMail parseEmail(MailItem var1, String var2) throws MailPlusException;

    List<MailItem> listAll(MailConn var1, String var2, List<String> var3) throws MailPlusException;

    MailConn createConn(MailConnCfg var1, boolean var2) throws MailPlusException;
}


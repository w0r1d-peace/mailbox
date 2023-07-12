package com.ruoyi.common.exception.mailbox;

/**
 * @author tangJM.
 * @date 2021/10/14
 * @description
 */
public class MailPlusException extends Exception {
    private static final long serialVersionUID = -8014572218613182580L;

    public MailPlusException(String message) {
        super(message);
    }

    public MailPlusException() {
    }
}

package com.ruoyi.mailbox.domain.dto;

import lombok.Data;

/**
 * 邮箱发送DTO
 */
@Data
public class MailSendDTO {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 收件人
     */
    private String receiver;

    /**
     * 主题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
}

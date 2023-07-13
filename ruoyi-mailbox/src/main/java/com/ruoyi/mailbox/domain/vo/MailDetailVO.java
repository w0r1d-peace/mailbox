package com.ruoyi.mailbox.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MailDetailVO {

    /** 主键 */
    private Long id;

    /** 任务ID */
    private Long taskId;

    /** 邮件唯一id，相对于账户的唯一id */
    private String uid;

    /** 发件人 */
    private String fromer;

    /** 收件人JSON，按name=>value,email=>value的键值对形式存入 */
    private String receiver;

    /** 密送人JSON，格式和收件人一样 */
    private String bcc;

    /** 抄送人JSON，格式和收件人一样 */
    private String cc;

    /** 所属文件夹 */
    private String folder;

    /** 是否已读 1 表示是，0 表示否 */
    private Integer hasRead;

    /** 是否包含附件 1 表示是，0 表示否 */
    private Integer hasAttachment;

    /** 发送日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;

    /** 所属邮箱 */
    private String email;

    /** 邮箱标题 */
    private String title;

    /** 邮件内容 */
    private String content;
}

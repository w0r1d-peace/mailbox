package com.ruoyi.mailbox.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 邮件头对象 mailbox_task_email_header
 *
 * @author tangJM.
 * @date 2023-07-13
 */
public class TaskEmailHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 邮件唯一id，相对于账户的唯一id */
    @Excel(name = "邮件唯一id，相对于账户的唯一id")
    private String uid;

    /** 发件人 */
    @Excel(name = "发件人")
    private String fromer;

    /** 收件人JSON，按name=>value,email=>value的键值对形式存入 */
    @Excel(name = "收件人JSON，按name=>value,email=>value的键值对形式存入")
    private String receiver;

    /** 密送人JSON，格式和收件人一样 */
    @Excel(name = "密送人JSON，格式和收件人一样")
    private String bcc;

    /** 抄送人JSON，格式和收件人一样 */
    @Excel(name = "抄送人JSON，格式和收件人一样")
    private String cc;

    /** 所属文件夹 */
    @Excel(name = "所属文件夹")
    private String folder;

    /** 是否已读 1 表示是，0 表示否 */
    @Excel(name = "是否已读 1 表示是，0 表示否")
    private Integer hasRead;

    /** 是否包含附件 1 表示是，0 表示否 */
    @Excel(name = "是否包含附件 1 表示是，0 表示否")
    private Integer hasAttachment;

    /** 发送日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendDate;

    /** 所属邮箱 */
    @Excel(name = "所属邮箱")
    private String email;

    /** 邮箱标题 */
    @Excel(name = "邮箱标题")
    private String title;

    /** 原始邮件存储路径 */
    @Excel(name = "原始邮件存储路径")
    private String emlPath;

    /** 是否为置顶文件 1表示是，0表示否 */
    @Excel(name = "是否为置顶文件 1表示是，0表示否")
    private Integer hasTop;

    /** 修改时间，冗余字段 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间，冗余字段", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modified;

    /** 所属用户 */
    @Excel(name = "所属用户")
    private Long userId;

    /** 所属用户名，冗余字段，提高查询性能 */
    @Excel(name = "所属用户名，冗余字段，提高查询性能")
    private String creator;

    /** 逻辑删除 1-是 0-否 */
    @Excel(name = "逻辑删除 1-是 0-否")
    private Integer removed;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Long getTaskId()
    {
        return taskId;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }
    public void setFromer(String fromer)
    {
        this.fromer = fromer;
    }

    public String getFromer()
    {
        return fromer;
    }
    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getReceiver()
    {
        return receiver;
    }
    public void setBcc(String bcc)
    {
        this.bcc = bcc;
    }

    public String getBcc()
    {
        return bcc;
    }
    public void setCc(String cc)
    {
        this.cc = cc;
    }

    public String getCc()
    {
        return cc;
    }
    public void setFolder(String folder)
    {
        this.folder = folder;
    }

    public String getFolder()
    {
        return folder;
    }
    public void setHasRead(Integer hasRead)
    {
        this.hasRead = hasRead;
    }

    public Integer getHasRead()
    {
        return hasRead;
    }
    public void setHasAttachment(Integer hasAttachment)
    {
        this.hasAttachment = hasAttachment;
    }

    public Integer getHasAttachment()
    {
        return hasAttachment;
    }
    public void setSendDate(Date sendDate)
    {
        this.sendDate = sendDate;
    }

    public Date getSendDate()
    {
        return sendDate;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setEmlPath(String emlPath)
    {
        this.emlPath = emlPath;
    }

    public String getEmlPath()
    {
        return emlPath;
    }
    public void setHasTop(Integer hasTop)
    {
        this.hasTop = hasTop;
    }

    public Integer getHasTop()
    {
        return hasTop;
    }
    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    public Date getModified()
    {
        return modified;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getCreator()
    {
        return creator;
    }
    public void setRemoved(Integer removed)
    {
        this.removed = removed;
    }

    public Integer getRemoved()
    {
        return removed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskId", getTaskId())
                .append("uid", getUid())
                .append("fromer", getFromer())
                .append("receiver", getReceiver())
                .append("bcc", getBcc())
                .append("cc", getCc())
                .append("folder", getFolder())
                .append("hasRead", getHasRead())
                .append("hasAttachment", getHasAttachment())
                .append("sendDate", getSendDate())
                .append("email", getEmail())
                .append("title", getTitle())
                .append("emlPath", getEmlPath())
                .append("hasTop", getHasTop())
                .append("createTime", getCreateTime())
                .append("modified", getModified())
                .append("userId", getUserId())
                .append("creator", getCreator())
                .append("removed", getRemoved())
                .toString();
    }
}

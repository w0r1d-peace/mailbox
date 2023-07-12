package com.ruoyi.mailbox.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 邮件内容对象 mailbox_task_email_content
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
public class TaskEmailContent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 信息内容 */
    @Excel(name = "信息内容")
    private String content;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modified;

    /** 逻辑删除 0-否 1-是 */
    @Excel(name = "逻辑删除 0-否 1-是")
    private Integer removed;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 创建人 */
    @Excel(name = "创建人")
    private String creator;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setModified(Date modified) 
    {
        this.modified = modified;
    }

    public Date getModified() 
    {
        return modified;
    }
    public void setRemoved(Integer removed) 
    {
        this.removed = removed;
    }

    public Integer getRemoved() 
    {
        return removed;
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

    @Override
    public String toString() {
        return "TaskEmailContent{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", content='" + content + '\'' +
                ", modified=" + modified +
                ", removed=" + removed +
                ", userId=" + userId +
                ", creator='" + creator + '\'' +
                '}';
    }
}

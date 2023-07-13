package com.ruoyi.mailbox.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 邮件附件对象 mailbox_task_email_attachment
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
public class TaskEmailAttachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 邮件ID */
    @Excel(name = "邮件ID")
    private Long headerId;

    /** 路径 */
    @Excel(name = "路径")
    private String path;

    /** cid */
    @Excel(name = "cid")
    private String cid;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 连接类型 */
    @Excel(name = "连接类型")
    private String contentType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPath() 
    {
        return path;
    }
    public void setCid(String cid) 
    {
        this.cid = cid;
    }

    public String getCid() 
    {
        return cid;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setContentType(String contentType) 
    {
        this.contentType = contentType;
    }

    public String getContentType() 
    {
        return contentType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("headerId", getHeaderId())
            .append("path", getPath())
            .append("cid", getCid())
            .append("name", getName())
            .append("contentType", getContentType())
            .toString();
    }
}

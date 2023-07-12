package com.ruoyi.mailbox.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 邮箱任务对象 mailbox_task
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
public class Task extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 邮箱帐号 */
    @Excel(name = "邮箱帐号")
    private String email;

    /** 邮箱密码 */
    @Excel(name = "邮箱密码")
    private String password;

    /** 连接状态 0-无状态 1-连接中 2-连接异常 */
    @Excel(name = "连接状态 0-无状态 1-连接中 2-连接异常")
    private Integer connStatus;

    /** 连接异常原因 */
    @Excel(name = "连接异常原因")
    private String connExceptionReason;

    /** 服务器端口 */
    @Excel(name = "服务器端口")
    private Long port;

    /** 服务器地址 */
    @Excel(name = "服务器地址")
    private String host;

    /** 是否支持SSL 0-否 1-是 */
    @Excel(name = "是否支持SSL 0-否 1-是")
    private Boolean hasSsl;

    /** 协议类型 0-爬虫 1-imap 2-pop3 3-exchange */
    @Excel(name = "协议类型 0-爬虫 1-imap 2-pop3 3-exchange")
    private Integer protocolType;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 创建人ID */
    @Excel(name = "创建人ID")
    private Long createId;

    /** 创建人名称 */
    @Excel(name = "创建人名称")
    private String createName;

    /** 修改人ID */
    @Excel(name = "修改人ID")
    private Long updateId;

    /** 修改人名称 */
    @Excel(name = "修改人名称")
    private String updateName;

    /** 逻辑删除 0-否 1-是 */
    @Excel(name = "逻辑删除 0-否 1-是")
    private Integer removed;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setConnStatus(Integer connStatus) 
    {
        this.connStatus = connStatus;
    }

    public Integer getConnStatus() 
    {
        return connStatus;
    }
    public void setConnExceptionReason(String connExceptionReason) 
    {
        this.connExceptionReason = connExceptionReason;
    }

    public String getConnExceptionReason() 
    {
        return connExceptionReason;
    }
    public void setPort(Long port) 
    {
        this.port = port;
    }

    public Long getPort() 
    {
        return port;
    }
    public void setHost(String host) 
    {
        this.host = host;
    }

    public String getHost() 
    {
        return host;
    }
    public void setHasSsl(Boolean hasSsl)
    {
        this.hasSsl = hasSsl;
    }

    public Boolean getHasSsl()
    {
        return hasSsl;
    }
    public void setProtocolType(Integer protocolType) 
    {
        this.protocolType = protocolType;
    }

    public Integer getProtocolType() 
    {
        return protocolType;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setCreateId(Long createId) 
    {
        this.createId = createId;
    }

    public Long getCreateId() 
    {
        return createId;
    }
    public void setCreateName(String createName) 
    {
        this.createName = createName;
    }

    public String getCreateName() 
    {
        return createName;
    }
    public void setUpdateId(Long updateId) 
    {
        this.updateId = updateId;
    }

    public Long getUpdateId() 
    {
        return updateId;
    }
    public void setUpdateName(String updateName) 
    {
        this.updateName = updateName;
    }

    public String getUpdateName() 
    {
        return updateName;
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
            .append("email", getEmail())
            .append("password", getPassword())
            .append("connStatus", getConnStatus())
            .append("connExceptionReason", getConnExceptionReason())
            .append("port", getPort())
            .append("host", getHost())
            .append("hasSsl", getHasSsl())
            .append("protocolType", getProtocolType())
            .append("description", getDescription())
            .append("createId", getCreateId())
            .append("createName", getCreateName())
            .append("createTime", getCreateTime())
            .append("updateId", getUpdateId())
            .append("updateName", getUpdateName())
            .append("updateTime", getUpdateTime())
            .append("removed", getRemoved())
            .toString();
    }
}

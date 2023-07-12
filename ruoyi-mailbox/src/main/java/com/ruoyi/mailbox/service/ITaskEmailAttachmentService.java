package com.ruoyi.mailbox.service;

import java.util.List;
import com.ruoyi.mailbox.domain.TaskEmailAttachment;

/**
 * 邮件附件Service接口
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
public interface ITaskEmailAttachmentService 
{
    /**
     * 查询邮件附件
     * 
     * @param id 邮件附件主键
     * @return 邮件附件
     */
    public TaskEmailAttachment selectTaskEmailAttachmentById(Long id);

    /**
     * 查询邮件附件列表
     * 
     * @param taskEmailAttachment 邮件附件
     * @return 邮件附件集合
     */
    public List<TaskEmailAttachment> selectTaskEmailAttachmentList(TaskEmailAttachment taskEmailAttachment);

    /**
     * 新增邮件附件
     * 
     * @param taskEmailAttachment 邮件附件
     * @return 结果
     */
    public int insertTaskEmailAttachment(TaskEmailAttachment taskEmailAttachment);

    /**
     * 修改邮件附件
     * 
     * @param taskEmailAttachment 邮件附件
     * @return 结果
     */
    public int updateTaskEmailAttachment(TaskEmailAttachment taskEmailAttachment);

    /**
     * 批量删除邮件附件
     * 
     * @param ids 需要删除的邮件附件主键集合
     * @return 结果
     */
    public int deleteTaskEmailAttachmentByIds(Long[] ids);

    /**
     * 删除邮件附件信息
     * 
     * @param id 邮件附件主键
     * @return 结果
     */
    public int deleteTaskEmailAttachmentById(Long id);
}

package com.ruoyi.mailbox.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mailbox.mapper.TaskEmailAttachmentMapper;
import com.ruoyi.mailbox.domain.TaskEmailAttachment;
import com.ruoyi.mailbox.service.ITaskEmailAttachmentService;

/**
 * 邮件附件Service业务层处理
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
@Service
public class TaskEmailAttachmentServiceImpl implements ITaskEmailAttachmentService 
{
    @Autowired
    private TaskEmailAttachmentMapper taskEmailAttachmentMapper;

    /**
     * 查询邮件附件
     * 
     * @param id 邮件附件主键
     * @return 邮件附件
     */
    @Override
    public TaskEmailAttachment selectTaskEmailAttachmentById(Long id)
    {
        return taskEmailAttachmentMapper.selectTaskEmailAttachmentById(id);
    }

    /**
     * 查询邮件附件列表
     * 
     * @param taskEmailAttachment 邮件附件
     * @return 邮件附件
     */
    @Override
    public List<TaskEmailAttachment> selectTaskEmailAttachmentList(TaskEmailAttachment taskEmailAttachment)
    {
        return taskEmailAttachmentMapper.selectTaskEmailAttachmentList(taskEmailAttachment);
    }

    /**
     * 新增邮件附件
     * 
     * @param taskEmailAttachment 邮件附件
     * @return 结果
     */
    @Override
    public int insertTaskEmailAttachment(TaskEmailAttachment taskEmailAttachment)
    {
        return taskEmailAttachmentMapper.insertTaskEmailAttachment(taskEmailAttachment);
    }

    /**
     * 修改邮件附件
     * 
     * @param taskEmailAttachment 邮件附件
     * @return 结果
     */
    @Override
    public int updateTaskEmailAttachment(TaskEmailAttachment taskEmailAttachment)
    {
        return taskEmailAttachmentMapper.updateTaskEmailAttachment(taskEmailAttachment);
    }

    /**
     * 批量删除邮件附件
     * 
     * @param ids 需要删除的邮件附件主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmailAttachmentByIds(Long[] ids)
    {
        return taskEmailAttachmentMapper.deleteTaskEmailAttachmentByIds(ids);
    }

    /**
     * 删除邮件附件信息
     * 
     * @param id 邮件附件主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmailAttachmentById(Long id)
    {
        return taskEmailAttachmentMapper.deleteTaskEmailAttachmentById(id);
    }
}

package com.ruoyi.mailbox.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mailbox.mapper.TaskEmailContentMapper;
import com.ruoyi.mailbox.domain.TaskEmailContent;
import com.ruoyi.mailbox.service.ITaskEmailContentService;

/**
 * 邮件内容Service业务层处理
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
@Service
public class TaskEmailContentServiceImpl implements ITaskEmailContentService 
{
    @Autowired
    private TaskEmailContentMapper taskEmailContentMapper;

    /**
     * 查询邮件内容
     * 
     * @param id 邮件内容主键
     * @return 邮件内容
     */
    @Override
    public TaskEmailContent selectTaskEmailContentById(Long id)
    {
        return taskEmailContentMapper.selectTaskEmailContentById(id);
    }

    /**
     * 查询邮件内容列表
     * 
     * @param taskEmailContent 邮件内容
     * @return 邮件内容
     */
    @Override
    public List<TaskEmailContent> selectTaskEmailContentList(TaskEmailContent taskEmailContent)
    {
        return taskEmailContentMapper.selectTaskEmailContentList(taskEmailContent);
    }

    /**
     * 新增邮件内容
     * 
     * @param taskEmailContent 邮件内容
     * @return 结果
     */
    @Override
    public int insertTaskEmailContent(TaskEmailContent taskEmailContent)
    {
        taskEmailContent.setCreateTime(DateUtils.getNowDate());
        return taskEmailContentMapper.insertTaskEmailContent(taskEmailContent);
    }

    /**
     * 修改邮件内容
     * 
     * @param taskEmailContent 邮件内容
     * @return 结果
     */
    @Override
    public int updateTaskEmailContent(TaskEmailContent taskEmailContent)
    {
        return taskEmailContentMapper.updateTaskEmailContent(taskEmailContent);
    }

    /**
     * 批量删除邮件内容
     * 
     * @param ids 需要删除的邮件内容主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmailContentByIds(Long[] ids)
    {
        return taskEmailContentMapper.deleteTaskEmailContentByIds(ids);
    }

    /**
     * 删除邮件内容信息
     * 
     * @param id 邮件内容主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmailContentById(Long id)
    {
        return taskEmailContentMapper.deleteTaskEmailContentById(id);
    }
}

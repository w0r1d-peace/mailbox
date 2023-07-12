package com.ruoyi.mailbox.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mailbox.mapper.TaskEmailHeaderMapper;
import com.ruoyi.mailbox.domain.TaskEmailHeader;
import com.ruoyi.mailbox.service.ITaskEmailHeaderService;

/**
 * 邮件头Service业务层处理
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
@Service
public class TaskEmailHeaderServiceImpl implements ITaskEmailHeaderService 
{
    @Autowired
    private TaskEmailHeaderMapper taskEmailHeaderMapper;

    /**
     * 查询邮件头
     * 
     * @param emailId 邮件头主键
     * @return 邮件头
     */
    @Override
    public TaskEmailHeader selectTaskEmailHeaderByEmailId(Long emailId)
    {
        return taskEmailHeaderMapper.selectTaskEmailHeaderByEmailId(emailId);
    }

    /**
     * 查询邮件头列表
     * 
     * @param taskEmailHeader 邮件头
     * @return 邮件头
     */
    @Override
    public List<TaskEmailHeader> selectTaskEmailHeaderList(TaskEmailHeader taskEmailHeader)
    {
        return taskEmailHeaderMapper.selectTaskEmailHeaderList(taskEmailHeader);
    }

    /**
     * 新增邮件头
     * 
     * @param taskEmailHeader 邮件头
     * @return 结果
     */
    @Override
    public int insertTaskEmailHeader(TaskEmailHeader taskEmailHeader)
    {
        taskEmailHeader.setCreateTime(DateUtils.getNowDate());
        return taskEmailHeaderMapper.insertTaskEmailHeader(taskEmailHeader);
    }

    /**
     * 修改邮件头
     * 
     * @param taskEmailHeader 邮件头
     * @return 结果
     */
    @Override
    public int updateTaskEmailHeader(TaskEmailHeader taskEmailHeader)
    {
        return taskEmailHeaderMapper.updateTaskEmailHeader(taskEmailHeader);
    }

    /**
     * 批量删除邮件头
     * 
     * @param emailIds 需要删除的邮件头主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmailHeaderByEmailIds(Long[] emailIds)
    {
        return taskEmailHeaderMapper.deleteTaskEmailHeaderByEmailIds(emailIds);
    }

    /**
     * 删除邮件头信息
     * 
     * @param emailId 邮件头主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmailHeaderByEmailId(Long emailId)
    {
        return taskEmailHeaderMapper.deleteTaskEmailHeaderByEmailId(emailId);
    }
}

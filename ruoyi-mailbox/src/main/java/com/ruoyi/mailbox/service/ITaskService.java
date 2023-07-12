package com.ruoyi.mailbox.service;

import java.util.List;
import com.ruoyi.mailbox.domain.Task;

/**
 * 邮箱任务Service接口
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
public interface ITaskService 
{
    /**
     * 查询邮箱任务
     *
     * @param id 邮箱任务主键
     * @return 邮箱任务
     */
    public Task selectTaskById(Long id);

    /**
     * 查询邮箱任务列表
     * 
     * @param task 邮箱任务
     * @return 邮箱任务集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增邮箱任务
     *
     * @param task 邮箱任务
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改邮箱任务
     *
     * @param task 邮箱任务
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 批量删除邮箱任务
     *
     * @param ids 需要删除的邮箱任务主键集合
     * @return 结果
     */
    public int deleteTaskByIds(Long[] ids);

    /**
     * 删除邮箱任务信息
     *
     * @param id 邮箱任务主键
     * @return 结果
     */
    public int deleteTaskById(Long id);

    /**
     * 拉取邮件
     * @param taskId
     * @return
     */
    int pull(Long taskId);
}

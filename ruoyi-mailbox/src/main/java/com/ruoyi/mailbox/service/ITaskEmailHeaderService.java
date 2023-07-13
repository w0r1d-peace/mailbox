package com.ruoyi.mailbox.service;

import java.util.List;
import com.ruoyi.mailbox.domain.TaskEmailHeader;

/**
 * 邮件头Service接口
 *
 * @author tangJM.
 * @date 2023-07-13
 */
public interface ITaskEmailHeaderService
{
    /**
     * 查询邮件头
     *
     * @param id 邮件头主键
     * @return 邮件头
     */
    public TaskEmailHeader selectTaskEmailHeaderById(Long id);

    /**
     * 查询邮件头列表
     *
     * @param taskEmailHeader 邮件头
     * @return 邮件头集合
     */
    public List<TaskEmailHeader> selectTaskEmailHeaderList(TaskEmailHeader taskEmailHeader);

    /**
     * 新增邮件头
     *
     * @param taskEmailHeader 邮件头
     * @return 结果
     */
    public int insertTaskEmailHeader(TaskEmailHeader taskEmailHeader);

    /**
     * 修改邮件头
     *
     * @param taskEmailHeader 邮件头
     * @return 结果
     */
    public int updateTaskEmailHeader(TaskEmailHeader taskEmailHeader);

    /**
     * 批量删除邮件头
     *
     * @param ids 需要删除的邮件头主键集合
     * @return 结果
     */
    public int deleteTaskEmailHeaderByIds(Long[] ids);

    /**
     * 删除邮件头信息
     *
     * @param id 邮件头主键
     * @return 结果
     */
    public int deleteTaskEmailHeaderById(Long id);
}

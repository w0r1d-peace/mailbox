package com.ruoyi.mailbox.mapper;

import java.util.List;
import com.ruoyi.mailbox.domain.TaskEmailHeader;

/**
 * 邮件头Mapper接口
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
public interface TaskEmailHeaderMapper 
{
    /**
     * 查询邮件头
     * 
     * @param emailId 邮件头主键
     * @return 邮件头
     */
    public TaskEmailHeader selectTaskEmailHeaderByEmailId(Long emailId);

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
     * 删除邮件头
     * 
     * @param emailId 邮件头主键
     * @return 结果
     */
    public int deleteTaskEmailHeaderByEmailId(Long emailId);

    /**
     * 批量删除邮件头
     * 
     * @param emailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskEmailHeaderByEmailIds(Long[] emailIds);
}

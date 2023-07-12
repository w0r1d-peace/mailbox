package com.ruoyi.mailbox.service;

import java.util.List;
import com.ruoyi.mailbox.domain.TaskEmailContent;

/**
 * 邮件内容Service接口
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
public interface ITaskEmailContentService 
{
    /**
     * 查询邮件内容
     * 
     * @param id 邮件内容主键
     * @return 邮件内容
     */
    public TaskEmailContent selectTaskEmailContentById(Long id);

    /**
     * 查询邮件内容列表
     * 
     * @param taskEmailContent 邮件内容
     * @return 邮件内容集合
     */
    public List<TaskEmailContent> selectTaskEmailContentList(TaskEmailContent taskEmailContent);

    /**
     * 新增邮件内容
     * 
     * @param taskEmailContent 邮件内容
     * @return 结果
     */
    public int insertTaskEmailContent(TaskEmailContent taskEmailContent);

    /**
     * 修改邮件内容
     * 
     * @param taskEmailContent 邮件内容
     * @return 结果
     */
    public int updateTaskEmailContent(TaskEmailContent taskEmailContent);

    /**
     * 批量删除邮件内容
     * 
     * @param ids 需要删除的邮件内容主键集合
     * @return 结果
     */
    public int deleteTaskEmailContentByIds(Long[] ids);

    /**
     * 删除邮件内容信息
     * 
     * @param id 邮件内容主键
     * @return 结果
     */
    public int deleteTaskEmailContentById(Long id);
}

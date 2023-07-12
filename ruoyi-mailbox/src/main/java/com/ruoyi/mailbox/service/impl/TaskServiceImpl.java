package com.ruoyi.mailbox.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.mailbox.MailPlusException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.mailbox.domain.TaskEmailAttachment;
import com.ruoyi.mailbox.domain.TaskEmailContent;
import com.ruoyi.mailbox.domain.TaskEmailHeader;
import com.ruoyi.mailbox.mapper.TaskEmailAttachmentMapper;
import com.ruoyi.mailbox.mapper.TaskEmailContentMapper;
import com.ruoyi.mailbox.mapper.TaskEmailHeaderMapper;
import com.ruoyi.mailbox.service.handler.server.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.mailbox.mapper.TaskMapper;
import com.ruoyi.mailbox.domain.Task;
import com.ruoyi.mailbox.service.ITaskService;

import javax.annotation.Resource;

/**
 * 邮箱任务Service业务层处理
 * 
 * @author tangJM.
 * @date 2023-07-12
 */
@Slf4j
@Service
public class TaskServiceImpl implements ITaskService 
{
    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TaskEmailHeaderMapper taskEmailHeaderMapper;

    @Resource
    private TaskEmailContentMapper taskEmailContentMapper;

    @Resource
    private TaskEmailAttachmentMapper taskEmailAttachmentMapper;

    @Value("${email.path}")
    private String emailPath;

    /**
     * 查询邮箱任务
     *
     * @param id 邮箱任务主键
     * @return 邮箱任务
     */
    @Override
    public Task selectTaskById(Long id)
    {
        return taskMapper.selectTaskById(id);
    }

    /**
     * 查询邮箱任务列表
     * 
     * @param task 邮箱任务
     * @return 邮箱任务
     */
    @Override
    public List<Task> selectTaskList(Task task)
    {
        return taskMapper.selectTaskList(task);
    }

    /**
     * 新增邮箱任务
     * 
     * @param task 邮箱任务
     * @return 结果
     */
    @Override
    public int insertTask(Task task)
    {
        // 是否存在邮箱
        boolean exist = existEmail(task.getEmail());
        if (exist) {
            throw new ServiceException("邮箱已存在");
        }

        // 邮箱是否能连接
        boolean isConnect = connectEmail(task.getEmail(), task.getPassword(), task.getHost(), task.getHasSsl(), task.getPort());
        if (!isConnect) {
            throw new ServiceException("邮箱连接失败");
        }

        task.setCreateTime(DateUtils.getNowDate());
        return taskMapper.insertTask(task);
    }


    private boolean connectEmail(String email, String password, String host, Boolean hasSsl, Long port) {
        MailConnCfg mailConnCfg = new MailConnCfg();
        mailConnCfg.setEmail(email);
        mailConnCfg.setPassword(password);
        mailConnCfg.setHost(host);
        mailConnCfg.setPort(port.intValue());
        mailConnCfg.setSsl(Optional.of(hasSsl).orElse(false));
        IMailService mailService = getMailService(host);
        MailConn mailConn = getMailConn(mailConnCfg, mailService);
        return mailConn != null ? true : false;
    }

    private IMailService getMailService(String host) {
        if (host.contains("pop")) {
            return new Pop3Service();
        } else if (host.contains("imap")) {
            return new ImapService();
        } else if (host.contains("exchange")) {
            return new MyExchangeService();
        } else {
            return null;
        }
    }


    /**
     * 获取
     * @param mailConnCfg
     */
    private MailConn getMailConn(MailConnCfg mailConnCfg, IMailService mailService) {
        try {
            MailConn mailConn = mailService.createConn(mailConnCfg, false);
            return mailConn;
        } catch (Exception e) {
            String connExceptionReason = String.format("邮箱连接失败，原始错误信息为【%s】", e.getMessage());
            log.error(connExceptionReason);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 账号是否存在
     * @param email
     * @return
     */
    private boolean existEmail(String email) {
        int count = taskMapper.countByEmail(email);
        return count > 0 ? true : false;
    }

    /**
     * 修改邮箱任务
     *
     * @param task 邮箱任务
     * @return 结果
     */
    @Override
    public int updateTask(Task task)
    {
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    /**
     * 批量删除邮箱任务
     *
     * @param ids 需要删除的邮箱任务主键
     * @return 结果
     */
    @Override
    public int deleteTaskByIds(Long[] ids)
    {
        return taskMapper.deleteTaskByIds(ids);
    }

    /**
     * 删除邮箱任务信息
     *
     * @param id 邮箱任务主键
     * @return 结果
     */
    @Override
    public int deleteTaskById(Long id)
    {
        return taskMapper.deleteTaskById(id);
    }

    @Override
    public boolean pull(Long taskId) {
        List<Task> tasks = taskMapper.selectTaskList(new Task());
        tasks.forEach(task -> {
            pullEmail(task);
        });

        return true;
    }

    /**
     * 拉去邮件
     * @param task
     */
    private void pullEmail(Task task) {
        Long id = task.getId();
        MailConnCfg mailConnCfg = new MailConnCfg();
        mailConnCfg.setEmail(task.getEmail());
        mailConnCfg.setPassword(task.getPassword());
        mailConnCfg.setHost(task.getHost());
        mailConnCfg.setPort(task.getPort().intValue());
        mailConnCfg.setSsl(Optional.of(task.getHasSsl()).orElse(false));
        IMailService mailService = getMailService(task.getHost());
        MailConn mailConn = getMailConn(mailConnCfg, mailService);

        if (mailConn == null) {
            throw new ServiceException("邮箱连接失败");
        }

        try {
            List<MailItem> mailItems = mailService.listAll(mailConn, "", null);
            if (mailItems == null || mailItems.size() == 0) {
                return;
            }

            for (MailItem mailItem : mailItems) {
                UniversalMail universalMail = mailService.parseEmail(mailItem, emailPath);
                TaskEmailHeader emailHeader = new TaskEmailHeader();
                // 邮件头
                BeanUtils.copyProperties(universalMail, emailHeader);
                emailHeader.setTaskId(id);
                emailHeader.setCreateTime(DateUtils.getNowDate());
                taskEmailHeaderMapper.insertTaskEmailHeader(emailHeader);

                // 邮件内容
                TaskEmailContent emailContent = new TaskEmailContent();
                emailContent.setTaskId(id);
                emailContent.setContent(universalMail.getContent());
                emailContent.setCreateTime(DateUtils.getNowDate());
                taskEmailContentMapper.insertTaskEmailContent(emailContent);

                //邮件附件
                List<TaskEmailAttachment> emailAttachments = new ArrayList<>();
                List<UniversalAttachment> attachments = universalMail.getAttachments();
                if (attachments != null) {
                    for (UniversalAttachment attachment : attachments) {
                        TaskEmailAttachment emailAttachment = new TaskEmailAttachment();
                        BeanUtils.copyProperties(attachment, emailAttachment);
                        emailAttachment.setTaskId(id);
                        emailAttachment.setCreateTime(DateUtils.getNowDate());
                        emailAttachments.add(emailAttachment);
                    }
                    taskEmailAttachmentMapper.batchInsertTaskEmailAttachment(emailAttachments);
                }
            }

        } catch (MailPlusException e) {
            log.error("邮件拉取失败，原始错误信息为【{}】", e);
        }
    }
}

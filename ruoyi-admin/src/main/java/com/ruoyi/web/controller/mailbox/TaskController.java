package com.ruoyi.web.controller.mailbox;

import java.util.List;

import com.ruoyi.mailbox.domain.TaskEmailContent;
import com.ruoyi.mailbox.domain.TaskEmailHeader;
import com.ruoyi.mailbox.domain.dto.MailSendDTO;
import com.ruoyi.mailbox.domain.vo.MailDetailVO;
import com.ruoyi.mailbox.service.ITaskEmailContentService;
import com.ruoyi.mailbox.service.ITaskEmailHeaderService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.mailbox.domain.Task;
import com.ruoyi.mailbox.service.ITaskService;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.annotation.Resource;

/**
 * 邮箱任务Controller
 *
 * @author tangJM.
 * @date 2023-07-12
 */
@RestController
@RequestMapping("/mailbox/task")
public class TaskController extends BaseController
{
    @Resource
    private ITaskService taskService;

    @Resource
    private ITaskEmailHeaderService taskEmailHeaderService;

    @Resource
    private ITaskEmailContentService taskEmailContentService;

    /**
     * 查询邮箱任务列表
     */
    @PreAuthorize("@ss.hasPermi('mailbox:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(Task task)
    {
        List<Task> list = taskService.selectTaskList(task);
        return getDataTable(list);
    }

    /**
     * 新增邮箱任务
     */
    @PreAuthorize("@ss.hasPermi('mailbox:task:add')")
    @Log(title = "邮箱任务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Task task)
    {
        return toAjax(taskService.insertTask(task));
    }


    /**
     * 拉取邮件
     */
    @PreAuthorize("@ss.hasPermi('mailbox:task:pull')")
    @Log(title = "拉取邮件", businessType = BusinessType.INSERT)
    @PostMapping("/pull")
    public AjaxResult pull(Long taskId)
    {
        return toAjax(taskService.pull(taskId));
    }

    /**
     * 根据任务ID查询邮件列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('mailbox:mail:list')")
    @GetMapping("/header/list")
    public TableDataInfo headerList(Long taskId)
    {
        TaskEmailHeader taskEmailHeader = new TaskEmailHeader();
        taskEmailHeader.setTaskId(taskId);
        List<TaskEmailHeader> list = taskEmailHeaderService.selectTaskEmailHeaderList(taskEmailHeader);
        if (list.isEmpty() && list.size() > 50) {
            return getDataTable(list.subList(0, 50));
        }

        return getDataTable(list);
    }

    /**
     * 根据邮件ID获取邮件详情
     */
    @PreAuthorize("@ss.hasPermi('mailbox:header:detail')")
    @GetMapping("/header/detail/{headerId}")
    public AjaxResult headerDetail(@PathVariable("headerId") Long headerId)
    {
        // 查询邮件头
        TaskEmailHeader taskEmailHeader = taskEmailHeaderService.selectTaskEmailHeaderById(headerId);
        // 查询邮件内容
        MailDetailVO mailDetailVO = new MailDetailVO();
        BeanUtils.copyProperties(taskEmailHeader, mailDetailVO);
        
        // 查询邮件内容
        TaskEmailContent taskEmailContentParam = new TaskEmailContent();
        taskEmailContentParam.setHeaderId(headerId);
        List<TaskEmailContent> taskEmailContentList = taskEmailContentService.selectTaskEmailContentList(taskEmailContentParam);

        if (taskEmailContentList != null && !taskEmailContentList.isEmpty()) {
            TaskEmailContent taskEmailContent = taskEmailContentList.get(0);
            mailDetailVO.setContent(taskEmailContent.getContent());
        }

        return AjaxResult.success(mailDetailVO);
    }

    /**
     * 发送邮件
     */
    @PreAuthorize("@ss.hasPermi('mailbox:mail:send')")
    @Log(title = "发送邮件", businessType = BusinessType.INSERT)
    @PostMapping("/mail/send")
    public AjaxResult send(@RequestBody MailSendDTO mailSendDTO)
    {
        return toAjax(taskService.send(mailSendDTO));
    }
}

package com.ruoyi.web.controller.mailbox;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.mailbox.domain.Task;
import com.ruoyi.mailbox.service.ITaskService;
import com.ruoyi.common.core.page.TableDataInfo;

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
    @Autowired
    private ITaskService taskService;

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
}

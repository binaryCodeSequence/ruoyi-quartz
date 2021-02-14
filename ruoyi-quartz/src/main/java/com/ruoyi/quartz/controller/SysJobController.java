package com.ruoyi.quartz.controller;

import java.util.List;

import com.ruoyi.common.constant.ScheduleConstants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.service.ISysJobService;
import com.ruoyi.quartz.util.CronUtils;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController {
    private String prefix = "monitor/job";

    @Autowired
    private ISysJobService jobService;

    @GetMapping()
    public String job() {
        return prefix + "/job";
    }

    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysJob job) {
        startPage();
        List<SysJob> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    @GetMapping("/stopAll")
    @ResponseBody
    public AjaxResult stopAll() throws TaskException, SchedulerException {
        String msg = "停止所有任务: ";
        List<SysJob> sysJobs = jobService.selectJobList(new SysJob());
        for (SysJob sysJob: sysJobs) {
            msg += "任务名:" + sysJob.getJobName() + ", 任务组:" + sysJob.getJobGroup();
            msg += "; ";

            sysJob.setStatus(ScheduleConstants.Status.PAUSE.getValue());
            jobService.changeStatus(sysJob);
        }
        return success(msg);
    }

    @GetMapping("/startAll")
    @ResponseBody
    public AjaxResult startAll() throws TaskException, SchedulerException {
        String msg = "启动所有任务: ";
        List<SysJob> sysJobs = jobService.selectJobList(new SysJob());
        for (SysJob sysJob: sysJobs) {
            msg += "任务名:" + sysJob.getJobName() + ", 任务组:" + sysJob.getJobGroup();
            msg += "; ";

            sysJob.setStatus(ScheduleConstants.Status.NORMAL.getValue());
            jobService.changeStatus(sysJob);
        }
        return success(msg);
    }
}

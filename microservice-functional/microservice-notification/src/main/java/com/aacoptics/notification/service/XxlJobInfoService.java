package com.aacoptics.notification.service;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.form.TriggerJobForm;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


public interface XxlJobInfoService extends IService<XxlJobInfo> {

    boolean add(XxlJobInfo xxlJobInfo);

    boolean update(XxlJobInfo xxlJobInfo);

    boolean delete(Integer id);

    IPage<XxlJobInfo> query(Page page, XxlJobInfo xxlJobInfo);

    IPage<XxlJobInfo> listNotificationTask(Page page, XxlJobInfo xxlJobInfo);

    Result triggerJob(TriggerJobForm triggerJobForm);
}
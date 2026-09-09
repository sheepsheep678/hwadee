package com.cdut.service;

import com.cdut.dto.FollowUpPlanQueryDTO;
import com.cdut.dto.FollowUpPlanSaveDTO;
import com.cdut.dto.FollowUpRecordDTO;
import com.cdut.pojo.ElderTag;
import com.cdut.pojo.FollowUpPlan;
import com.cdut.pojo.FollowUpRecord;
import com.cdut.pojo.PageResult;

import java.util.List;

/**
 * 重点人群与随访服务
 */
public interface FocusService {

    /** 标签列表 */
    List<ElderTag> tags();

    /** 取消老人标签 */
    void unbindTag(Long elderId, Long tagId);

    /** 随访计划分页查询 */
    PageResult<FollowUpPlan> planPage(FollowUpPlanQueryDTO query);

    /** 新增随访计划 */
    void savePlan(FollowUpPlanSaveDTO dto);

    /** 修改随访计划 */
    void updatePlan(Long id, FollowUpPlanSaveDTO dto);

    /** 删除随访计划 */
    void deletePlan(Long id);

    /** 新增随访记录 */
    void addRecord(FollowUpRecordDTO dto);

    /** 随访记录分页查询 */
    PageResult<FollowUpRecord> recordPage(FollowUpRecordDTO query);

}

package com.cdut.service;

import com.cdut.common.PageInfo;
import com.cdut.dto.AssessmentReportQueryDTO;
import com.cdut.dto.AssessmentSubmitDTO;
import com.cdut.entity.AssessmentReport;
import com.cdut.entity.AssessmentTemplate;

import java.util.List;

/**
 * 评估报告服务
 */
public interface AssessmentService {

    /** 获取评估模板列表 */
    List<AssessmentTemplate> templates(Integer assessType);

    /** 提交评估结果 */
    AssessmentReport submit(AssessmentSubmitDTO dto);

    /** 评估报告分页查询 */
    PageInfo<AssessmentReport> page(AssessmentReportQueryDTO query);

    /** 评估报告详情 */
    AssessmentReport detail(Long id);

    /** 删除评估报告 */
    void delete(Long id);

}

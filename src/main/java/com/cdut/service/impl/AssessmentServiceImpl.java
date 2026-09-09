package com.cdut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdut.common.BusinessException;
import com.cdut.common.PageInfo;
import com.cdut.dto.AnswerItem;
import com.cdut.dto.AssessmentReportQueryDTO;
import com.cdut.dto.AssessmentSubmitDTO;
import com.cdut.entity.AssessmentAnswer;
import com.cdut.entity.AssessmentReport;
import com.cdut.entity.AssessmentTemplate;
import com.cdut.mapper.AssessmentAnswerMapper;
import com.cdut.mapper.AssessmentReportMapper;
import com.cdut.mapper.AssessmentTemplateMapper;
import com.cdut.mapper.ElderProfileMapper;
import com.cdut.service.AssessmentService;
import com.cdut.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评估报告服务实现。
 */
@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentTemplateMapper templateMapper;
    private final AssessmentReportMapper reportMapper;
    private final AssessmentAnswerMapper answerMapper;
    private final ElderProfileMapper elderProfileMapper;

    @Override
    public List<AssessmentTemplate> templates(Integer assessType) {
        return templateMapper.selectList(new LambdaQueryWrapper<AssessmentTemplate>()
                .eq(assessType != null, AssessmentTemplate::getAssessType, assessType)
                .eq(AssessmentTemplate::getStatus, 1)
                .orderByDesc(AssessmentTemplate::getId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssessmentReport submit(AssessmentSubmitDTO dto) {
        if (elderProfileMapper.selectById(dto.getElderId()) == null) {
            throw new BusinessException("老人档案不存在");
        }
        AssessmentReport report = new AssessmentReport();
        report.setElderId(dto.getElderId());
        report.setTemplateId(dto.getTemplateId());
        report.setAssessType(dto.getAssessType());
        report.setAssessDate(dto.getAssessDate());
        report.setDoctorId(UserContext.requireDoctorId());
        BigDecimal totalScore = BigDecimal.ZERO;
        for (AnswerItem item : dto.getAnswers()) {
            if (item.getScore() != null) {
                totalScore = totalScore.add(item.getScore());
            }
        }
        report.setTotalScore(totalScore);
        report.setLevel(evaluateLevel(totalScore));
        report.setConclusion("评估完成，总分 " + totalScore);
        report.setCreateTime(LocalDateTime.now());
        reportMapper.insert(report);

        for (AnswerItem item : dto.getAnswers()) {
            AssessmentAnswer answer = new AssessmentAnswer();
            answer.setReportId(report.getId());
            answer.setItemId(item.getItemId());
            answer.setAnswer(item.getAnswer());
            answer.setScore(item.getScore());
            answer.setCreateTime(LocalDateTime.now());
            answerMapper.insert(answer);
        }
        return report;
    }

    @Override
    public PageInfo<AssessmentReport> page(AssessmentReportQueryDTO query) {
        LambdaQueryWrapper<AssessmentReport> wrapper = new LambdaQueryWrapper<AssessmentReport>()
                .eq(query.getElderId() != null, AssessmentReport::getElderId, query.getElderId())
                .eq(query.getAssessType() != null, AssessmentReport::getAssessType, query.getAssessType())
                .eq(query.getDoctorId() != null, AssessmentReport::getDoctorId, query.getDoctorId())
                .ge(query.getStartDate() != null, AssessmentReport::getAssessDate, query.getStartDate())
                .le(query.getEndDate() != null, AssessmentReport::getAssessDate, query.getEndDate())
                .orderByDesc(AssessmentReport::getCreateTime);
        Page<AssessmentReport> page = new Page<>(query.safePageNum(), query.safePageSize());
        return PageInfo.of(reportMapper.selectPage(page, wrapper));
    }

    @Override
    public AssessmentReport detail(Long id) {
        AssessmentReport report = reportMapper.selectById(id);
        if (report == null) {
            throw new BusinessException("评估报告不存在");
        }
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (reportMapper.selectById(id) == null) {
            throw new BusinessException("评估报告不存在");
        }
        reportMapper.deleteById(id);
        answerMapper.delete(new LambdaQueryWrapper<AssessmentAnswer>().eq(AssessmentAnswer::getReportId, id));
    }

    private String evaluateLevel(BigDecimal totalScore) {
        if (totalScore == null) {
            return "未知";
        }
        if (totalScore.compareTo(new BigDecimal("90")) >= 0) {
            return "优秀";
        } else if (totalScore.compareTo(new BigDecimal("75")) >= 0) {
            return "良好";
        } else if (totalScore.compareTo(new BigDecimal("60")) >= 0) {
            return "一般";
        }
        return "需重点关注";
    }

}

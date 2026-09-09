package com.cdut.service.impl;

import com.cdut.dto.AnswerItem;
import com.cdut.dto.AssessmentReportQueryDTO;
import com.cdut.dto.AssessmentSubmitDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.AssessmentReportMapper;
import com.cdut.mapper.AssessmentTemplateMapper;
import com.cdut.mapper.ElderProfileMapper;
import com.cdut.pojo.AssessmentReport;
import com.cdut.pojo.AssessmentTemplate;
import com.cdut.pojo.PageResult;
import com.cdut.service.AssessmentService;
import com.cdut.utils.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 评估报告服务实现。
 */
@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentTemplateMapper templateMapper;
    private final AssessmentReportMapper reportMapper;
    private final ElderProfileMapper elderProfileMapper;

    @Override
    public List<AssessmentTemplate> templates(Integer assessType) {
        return templateMapper.selectByTypeAndStatus(assessType, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssessmentReport submit(AssessmentSubmitDTO dto) {
        if (elderProfileMapper.selectById(dto.getElderId()) == null) {
            throw new BizException("老人档案不存在");
        }
        AssessmentReport report = new AssessmentReport();
        report.setElderId(dto.getElderId());
        report.setTemplateId(dto.getTemplateId());
        report.setAssessType(dto.getAssessType());
        report.setAssessDate(dto.getAssessDate());
        report.setAssessorId(UserContext.getUserId());

        BigDecimal totalScore = BigDecimal.ZERO;
        for (AnswerItem item : dto.getAnswers()) {
            if (item.getScore() != null) {
                totalScore = totalScore.add(item.getScore());
            }
        }
        report.setScore(totalScore);
        report.setGrade(evaluateLevel(totalScore));
        report.setConclusion("评估完成，总分 " + totalScore);
        report.setReportNo(generateReportNo());
        report.setResultJson(serializeAnswers(dto.getAnswers()));
        reportMapper.insert(report);
        return report;
    }

    @Override
    public PageResult<AssessmentReport> page(AssessmentReportQueryDTO query) {
        PageHelper.startPage(query.safePageNum(), query.safePageSize());
        List<AssessmentReport> list = reportMapper.selectPage(
                query.getElderId(), query.getAssessType(), query.getAssessorId(),
                query.getStartDate(), query.getEndDate());
        PageInfo<AssessmentReport> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), query.safePageNum(), query.safePageSize());
    }

    @Override
    public AssessmentReport detail(Long id) {
        AssessmentReport report = reportMapper.selectById(id);
        if (report == null) {
            throw new BizException("评估报告不存在");
        }
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (reportMapper.selectById(id) == null) {
            throw new BizException("评估报告不存在");
        }
        reportMapper.deleteById(id);
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

    private String serializeAnswers(List<AnswerItem> answers) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < answers.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            AnswerItem item = answers.get(i);
            sb.append("{\"itemId\":").append(item.getItemId() == null ? "null" : item.getItemId())
              .append(",\"answer\":").append(jsonStr(item.getAnswer()))
              .append(",\"score\":").append(item.getScore() == null ? "null" : item.getScore())
              .append('}');
        }
        return sb.append(']').toString();
    }

    private String jsonStr(String s) {
        if (s == null) {
            return "null";
        }
        return "\"" + s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }

    private String generateReportNo() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "RPT" + date + String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
    }

}

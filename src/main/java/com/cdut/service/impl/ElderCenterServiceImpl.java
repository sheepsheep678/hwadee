package com.cdut.service.impl;

import com.cdut.dto.ElderAccountVO;
import com.cdut.dto.ElderAssessmentReportVO;
import com.cdut.dto.MessageQueryDTO;
import com.cdut.dto.PasswordChangeDTO;
import com.cdut.exception.BizException;
import com.cdut.mapper.AssessmentReportMapper;
import com.cdut.mapper.ElderAccountMapper;
import com.cdut.mapper.MessageMapper;
import com.cdut.mapper.ProfileMapper;
import com.cdut.pojo.AssessmentReport;
import com.cdut.pojo.ElderAccount;
import com.cdut.pojo.ElderProfile;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.SysMessage;
import com.cdut.service.ElderCenterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElderCenterServiceImpl implements ElderCenterService {

    @Autowired
    private ElderAccountMapper elderAccountMapper;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private AssessmentReportMapper assessmentReportMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ElderAccountVO getAccountInfo(Long accountId) {
        ElderAccount account = elderAccountMapper.selectById(accountId);
        if (account == null) {
            throw new BizException("账户不存在");
        }

        ElderAccountVO vo = new ElderAccountVO();
        vo.setId(account.getId());
        vo.setAccountNo(account.getAccountNo());
        vo.setBindPhone(account.getBindPhone());
        vo.setAuthStatus(account.getAuthStatus());
        vo.setAccountStatus(account.getAccountStatus());
        vo.setRegisterChannel(account.getRegisterChannel());
        vo.setCreateTime(account.getCreateTime());

        ElderProfile profile = profileMapper.selectByAccountId(accountId);
        if (profile != null) {
            vo.setElderId(profile.getId());
            vo.setElderNo(profile.getElderNo());
            vo.setName(profile.getName());
            vo.setIdCard(profile.getIdCard());
            vo.setPhone(profile.getPhone());
            vo.setPhotoUrl(profile.getPhotoUrl());
        }
        return vo;
    }

    @Override
    public void changePassword(Long accountId, PasswordChangeDTO dto) {
        if (dto.getOldPassword().equals(dto.getNewPassword())) {
            throw new BizException("新密码不能与原密码相同");
        }

        ElderAccount account = elderAccountMapper.selectById(accountId);
        if (account == null) {
            throw new BizException("账户不存在");
        }
        if (account.getPassword() == null
                || !passwordEncoder.matches(dto.getOldPassword(), account.getPassword())) {
            throw new BizException("原密码错误");
        }

        elderAccountMapper.updatePassword(accountId, passwordEncoder.encode(dto.getNewPassword()));
    }

    @Override
    public PageResult<SysMessage> listMessages(Long accountId, MessageQueryDTO query) {
        int pageNum = query.safePageNum();
        int pageSize = query.safePageSize();
        int offset = (pageNum - 1) * pageSize;

        long total = messageMapper.selectCount(accountId, query.getMsgType(), query.getIsRead());
        if (total == 0) {
            return PageResult.empty(pageNum, pageSize);
        }

        List<SysMessage> list = messageMapper.selectPage(accountId, query.getMsgType(), query.getIsRead(),
                offset, pageSize);
        return PageResult.of(total, pageNum, pageSize, list);
    }

    @Override
    public void markRead(Long accountId, Long messageId) {
        // SQL 带 user_id 条件，改不到别人的消息
        int rows = messageMapper.markRead(messageId, accountId);
        if (rows == 0) {
            throw new BizException("消息不存在");
        }
    }

    @Override
    public long countUnread(Long accountId) {
        return messageMapper.countUnread(accountId);
    }

    @Override
    public void markAllRead(Long accountId) {
        // 幂等：没有未读消息时也直接返回成功，与单条已读的"0 行即不存在"语义区分
        messageMapper.markAllRead(accountId);
    }

    @Override
    public PageResult<ElderAssessmentReportVO> listAssessmentReports(Long accountId, int pageNum,
                                                                     int pageSize, Integer assessType) {
        int safePageNum = pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize < 1 ? 10 : Math.min(pageSize, 100);

        // 只使用当前登录账户换算出的档案ID，不接受前端传入 elderId
        Long elderId = profileMapper.selectElderIdByAccountId(accountId);
        if (elderId == null) {
            return PageResult.empty(safePageNum, safePageSize);
        }

        PageHelper.startPage(safePageNum, safePageSize);
        List<AssessmentReport> reports = assessmentReportMapper.selectPage(elderId, assessType, null, null, null);
        PageInfo<AssessmentReport> pageInfo = new PageInfo<>(reports);

        List<ElderAssessmentReportVO> voList = new ArrayList<>();
        for (AssessmentReport report : pageInfo.getList()) {
            voList.add(toAssessmentReportVO(report));
        }
        return PageResult.of(pageInfo.getTotal(), safePageNum, safePageSize, voList);
    }

    @Override
    public ElderAssessmentReportVO getAssessmentReportDetail(Long accountId, Long reportId) {
        Long elderId = profileMapper.selectElderIdByAccountId(accountId);
        if (elderId == null) {
            throw new BizException("未查询到老人档案");
        }

        // 先按 id 查询，再校验归属：不属于当前老人的报告统一按“不存在”处理，防止越权
        AssessmentReport report = assessmentReportMapper.selectById(reportId);
        if (report == null || !elderId.equals(report.getElderId())) {
            throw new BizException("评估报告不存在");
        }

        return toAssessmentReportVO(report);
    }

    /** 实体转老人端 VO，屏蔽 assessorId、resultJson 等老人端不需要的字段 */
    private ElderAssessmentReportVO toAssessmentReportVO(AssessmentReport report) {
        ElderAssessmentReportVO vo = new ElderAssessmentReportVO();
        vo.setId(report.getId());
        vo.setReportNo(report.getReportNo());
        vo.setAssessType(report.getAssessType());
        vo.setScore(report.getScore());
        vo.setGrade(report.getGrade());
        vo.setAssessDate(report.getAssessDate());
        vo.setConclusion(report.getConclusion());
        return vo;
    }
}

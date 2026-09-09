package com.cdut.service;

import com.cdut.dto.ElderAccountVO;
import com.cdut.dto.MessageQueryDTO;
import com.cdut.dto.PasswordChangeDTO;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.SysMessage;

/**
 * 老人端个人中心：账户信息 / 修改密码 / 我的消息
 */
public interface ElderCenterService {

    /** 我的账户信息（不含密码） */
    ElderAccountVO getAccountInfo(Long accountId);

    /** 修改密码 */
    void changePassword(Long accountId, PasswordChangeDTO dto);

    /** 我的消息（分页） */
    PageResult<SysMessage> listMessages(Long accountId, MessageQueryDTO query);

    /** 单条消息标记已读 */
    void markRead(Long accountId, Long messageId);

    /** 未读消息数量 */
    long countUnread(Long accountId);

    /** 全部消息标记已读（幂等：没有未读也返回成功） */
    void markAllRead(Long accountId);
}

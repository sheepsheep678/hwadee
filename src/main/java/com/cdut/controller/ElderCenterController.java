package com.cdut.controller;

import com.cdut.dto.ElderAccountVO;
import com.cdut.dto.MessageQueryDTO;
import com.cdut.dto.PasswordChangeDTO;
import com.cdut.pojo.PageResult;
import com.cdut.pojo.Result;
import com.cdut.pojo.SysMessage;
import com.cdut.service.ElderCenterService;
import com.cdut.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 老人端个人中心
 *
 * <p>路径：/api/elder/center —— 与档案模块 /api/elder/profile 分开，避免路径冲突。
 * <p>当前用户一律从 UserContext 取，不从 request 也不从前端传。
 */
@RestController
@RequestMapping("/api/elder/center")
@Validated
public class ElderCenterController {

    @Autowired
    private ElderCenterService elderCenterService;

    /** 我的账户信息 */
    @GetMapping("/account")
    public Result<ElderAccountVO> account() {
        return Result.success(elderCenterService.getAccountInfo(UserContext.getUserId()));
    }

    /** 修改密码 */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody @Validated PasswordChangeDTO dto) {
        elderCenterService.changePassword(UserContext.getUserId(), dto);
        return Result.success("修改成功，请重新登录", null);
    }

    /** 我的消息（分页） */
    @GetMapping("/messages")
    public Result<PageResult<SysMessage>> messages(MessageQueryDTO query) {
        return Result.success(elderCenterService.listMessages(UserContext.getUserId(), query));
    }

    /** 未读消息数量 */
    @GetMapping("/messages/unread-count")
    public Result<Long> unreadCount() {
        return Result.success(elderCenterService.countUnread(UserContext.getUserId()));
    }

    /** 单条消息标记已读 */
    @PutMapping("/messages/{id}/read")
    public Result<Void> markRead(@PathVariable Long id) {
        elderCenterService.markRead(UserContext.getUserId(), id);
        return Result.success("已读", null);
    }

    /** 全部消息标记已读 */
    @PutMapping("/messages/read-all")
    public Result<Void> markAllRead() {
        elderCenterService.markAllRead(UserContext.getUserId());
        return Result.success("全部已读", null);
    }
}

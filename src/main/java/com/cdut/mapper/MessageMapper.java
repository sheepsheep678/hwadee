package com.cdut.mapper;

import com.cdut.pojo.SysMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 系统消息 Mapper（SQL 写在 resources/mapper/MessageMapper.xml）
 *
 * <p>约定：老人端 userId = elder_account.id。
 * <b>所有更新/删除必须带 user_id 条件</b>，防止越权改别人的消息。
 */
@Mapper
public interface MessageMapper {

    List<SysMessage> selectPage(@Param("userId") Long userId,
                                @Param("msgType") Integer msgType,
                                @Param("isRead") Integer isRead,
                                @Param("offset") int offset,
                                @Param("pageSize") int pageSize);

    long selectCount(@Param("userId") Long userId,
                     @Param("msgType") Integer msgType,
                     @Param("isRead") Integer isRead);

    @Update("UPDATE sys_message SET is_read = 1 WHERE id = #{id} AND user_id = #{userId} AND is_deleted = 0")
    int markRead(@Param("id") Long id, @Param("userId") Long userId);

    @Update("UPDATE sys_message SET is_read = 1 WHERE user_id = #{userId} AND is_read = 0 AND is_deleted = 0")
    int markAllRead(@Param("userId") Long userId);

    long countUnread(@Param("userId") Long userId);
}

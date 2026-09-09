package com.cdut.mapper;

import com.cdut.pojo.ElderAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ElderAccountMapper {

    /**
     * 登录查账户：支持账号 / 手机号 / 身份证号三种方式
     *
     * <p>身份证号存在 elder_profile 上，需要左连档案表反查。
     */
    @Select("SELECT a.* FROM elder_account a " +
            "LEFT JOIN elder_profile p ON p.account_id = a.id AND p.is_deleted = 0 " +
            "WHERE a.is_deleted = 0 " +
            "  AND (a.account_no = #{account} OR a.bind_phone = #{account} OR p.id_card = #{account}) " +
            "LIMIT 1")
    ElderAccount selectByAccount(@Param("account") String account);

    @Select("SELECT * FROM elder_account WHERE id = #{id} AND is_deleted = 0")
    ElderAccount selectById(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM elder_account WHERE bind_phone = #{phone} AND is_deleted = 0")
    int countByPhone(@Param("phone") String phone);

    @Select("SELECT COUNT(*) FROM elder_account WHERE account_no = #{accountNo}")
    int countByAccountNo(@Param("accountNo") String accountNo);

    @Select("SELECT COUNT(*) FROM elder_account a " +
            "INNER JOIN elder_profile p ON p.account_id = a.id AND p.is_deleted = 0 " +
            "WHERE p.id_card = #{idCard} AND a.is_deleted = 0")
    int countByIdCard(@Param("idCard") String idCard);

    @Insert("INSERT INTO elder_account (account_no, password, bind_phone, auth_status, account_status, register_channel) " +
            "VALUES (#{accountNo}, #{password}, #{bindPhone}, #{authStatus}, #{accountStatus}, #{registerChannel})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ElderAccount account);

    @Update("UPDATE elder_account SET password = #{password} WHERE id = #{id} AND is_deleted = 0")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}

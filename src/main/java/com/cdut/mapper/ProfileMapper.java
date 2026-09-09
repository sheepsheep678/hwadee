package com.cdut.mapper;

import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileUpdateDTO;
import com.cdut.pojo.ElderProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfileMapper {
    /**
     * 按档案主键查询
     *
     * <p>修正：原 SQL 为 where elder_no = #{elderId}（把主键当老人编号用了），且参数类型为 Integer。
     */
    @Select("SELECT * FROM elder_profile WHERE id = #{id} AND is_deleted = 0")
    ElderProfile selectElderProfileById(@Param("id") Long id);



    @Select("select * from elder_profile where elder_no=#{elderId}")
    public List<ElderProfile> selectAllElderProfileById(Integer elderId);

    @Update("update elder_profile set phone=#{phone},address=#{address},photo_url=#{photoUrl} where elder_no=#{elderNo}")
    public void updateElderProfile(ElderProfileUpdateDTO elderProfileUpdateDTO);

    /** 账户ID -> 档案ID（老人端所有业务表查询都要先做这一步换算） */
    @Select("SELECT id FROM elder_profile WHERE account_id = #{accountId} AND is_deleted = 0 LIMIT 1")
    Long selectElderIdByAccountId(@Param("accountId") Long accountId);

    /** 按账户ID查档案（个人中心带出姓名/身份证/头像用） */
    @Select("SELECT * FROM elder_profile WHERE account_id = #{accountId} AND is_deleted = 0 LIMIT 1")
    ElderProfile selectByAccountId(@Param("accountId") Long accountId);

    /** 注册时同步建档，状态默认 1-在管 */
    @Insert("INSERT INTO elder_profile (elder_no, account_id, name, id_card, phone, status) " +
            "VALUES (#{elderNo}, #{accountId}, #{name}, #{idCard}, #{phone}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertForRegister(ElderProfile profile);

}

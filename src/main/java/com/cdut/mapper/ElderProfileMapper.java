package com.cdut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.entity.ElderProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ElderProfileMapper extends BaseMapper<ElderProfile> {

    /**
     * 分页查询老人档案，支持按姓名/身份证/电话/居住类型/标签ID 过滤。
     */
    IPage<ElderProfile> selectProfilePage(Page<ElderProfile> page, @Param("query") ElderProfileQueryDTO query);

}

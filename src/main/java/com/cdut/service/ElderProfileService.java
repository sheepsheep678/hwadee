package com.cdut.service;

import com.cdut.common.PageInfo;
import com.cdut.dto.ElderProfileDetailDTO;
import com.cdut.dto.ElderProfileQueryDTO;
import com.cdut.dto.ElderProfileSaveDTO;
import com.cdut.entity.ElderProfile;

/**
 * 老人档案管理服务
 */
public interface ElderProfileService {

    /** 分页查询老人档案 */
    PageInfo<ElderProfile> page(ElderProfileQueryDTO query);

    /** 查询老人档案详情（含健康档案、家属联系人、标签） */
    ElderProfileDetailDTO detail(Long id);

    /** 新增老人档案，返回新档案ID */
    Long save(ElderProfileSaveDTO dto);

    /** 修改老人档案 */
    void update(Long id, ElderProfileSaveDTO dto);

    /** 删除老人档案 */
    void delete(Long id);



    /** 导出档案，返回文件访问地址 */
    String exportExcel(ElderProfileQueryDTO query);

}

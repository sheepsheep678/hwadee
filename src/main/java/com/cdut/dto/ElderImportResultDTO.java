package com.cdut.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 档案批量导入结果
 */
@Data
public class ElderImportResultDTO implements Serializable {

    /** 总条数 */
    private int total;

    /** 成功条数 */
    private int successCount;

    /** 失败条数 */
    private int failCount;

    /** 失败明细（如：第3行-身份证号重复） */
    private List<String> failList = new ArrayList<>();

    public void addFail(String message) {
        this.failList.add(message);
    }

}

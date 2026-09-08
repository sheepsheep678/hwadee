package com.cdut.dto;

import com.cdut.pojo.ElderProfile;
import com.cdut.pojo.FamilyContact;
import com.cdut.pojo.HealthRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElderProfileUpdateDTO{

    private Integer elderNo;

    // 联系电话 /

    private String phone;


    // 居住地址 /

    private String address;


    // 照片URL /

    private String photoUrl;

}

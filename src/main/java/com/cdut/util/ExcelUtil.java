package com.cdut.util;

import com.cdut.dto.ElderProfileSaveDTO;
import com.cdut.entity.ElderProfile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 老人档案 Excel 导入/导出工具（Apache POI）。
 */
public final class ExcelUtil {

    private static final String[] HEADERS = {"姓名", "身份证号", "联系电话", "性别(1男2女)", "年龄", "居住类型(1居家2社区3机构4独居)", "居住地址"};

    private ExcelUtil() {
    }

    /** 导出老人档案为 xlsx 字节流 */
    public static byte[] exportElderProfiles(List<ElderProfile> list) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("老人档案");
            Row header = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                header.createCell(i).setCellValue(HEADERS[i]);
            }
            int rowIndex = 1;
            for (ElderProfile p : list) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(p.getName() == null ? "" : p.getName());
                row.createCell(1).setCellValue(p.getIdCard() == null ? "" : p.getIdCard());
                row.createCell(2).setCellValue(p.getPhone() == null ? "" : p.getPhone());
                row.createCell(3).setCellValue(p.getGender() == null ? "" : String.valueOf(p.getGender()));
                row.createCell(4).setCellValue(p.getAge() == null ? "" : String.valueOf(p.getAge()));
                row.createCell(5).setCellValue(p.getLivingType() == null ? "" : String.valueOf(p.getLivingType()));
                row.createCell(6).setCellValue(p.getAddress() == null ? "" : p.getAddress());
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("导出 Excel 失败", e);
        }
    }

    /** 解析老人档案 Excel，返回导入数据列表（跳过表头） */
    public static List<ElderProfileSaveDTO> parseElderImport(InputStream inputStream) {
        List<ElderProfileSaveDTO> result = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String name = cellString(row.getCell(0), formatter);
                String idCard = cellString(row.getCell(1), formatter);
                if (isBlank(name) && isBlank(idCard)) {
                    continue;
                }
                ElderProfileSaveDTO dto = new ElderProfileSaveDTO();
                dto.setName(name);
                dto.setIdCard(idCard);
                dto.setPhone(cellString(row.getCell(2), formatter));
                dto.setGender(parseInt(cellString(row.getCell(3), formatter)));
                dto.setAge(parseInt(cellString(row.getCell(4), formatter)));
                dto.setLivingType(parseInt(cellString(row.getCell(5), formatter)));
                dto.setAddress(cellString(row.getCell(6), formatter));
                result.add(dto);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("解析 Excel 失败", e);
        }
        return result;
    }

    private static String cellString(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        return formatter.formatCellValue(cell).trim();
    }

    private static Integer parseInt(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

}

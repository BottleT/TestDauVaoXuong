package com.example.demo.controller;

import com.example.demo.entity.Staff;
import com.example.demo.service.StaffService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/excel")
public class ExcelController {

    private final StaffService staffService;

    @Autowired
    public ExcelController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/download-staff-template")
    public ResponseEntity<byte[]> downloadStaffTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Danh sách nhân viên");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);

        Row headerRow = sheet.createRow(0);

        String[] columns = {"STT", "Mã nhân viên", "Họ tên", "Email FPT (@fpt.edu.vn)", "Email FE (@fe.edu.nv)", "Trạng thái (1: Hoạt động, 0: Không hoạt động)"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);

        for (int i = 1; i <= 5; i++) {
            Row row = sheet.createRow(i);

            Cell sttCell = row.createCell(0);
            sttCell.setCellValue(i);
            sttCell.setCellStyle(dataStyle);

            for (int j = 1; j < columns.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(dataStyle);

                if (i == 1) {
                    switch(j) {
                        case 1:
                            cell.setCellValue("NV001");
                            break;
                        case 2:
                            cell.setCellValue("Nguyễn Văn A");
                            break;
                        case 3:
                            cell.setCellValue("anv@fpt.edu.vn");
                            break;
                        case 4:
                            cell.setCellValue("anv@fe.edu.nv");
                            break;
                        case 5:
                            cell.setCellValue(1);
                            break;
                    }
                }
            }
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "template_nhan_vien.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    @GetMapping("/export-staff-data")
    public ResponseEntity<byte[]> exportStaffData() throws IOException {
        List<Staff> staffList = staffService.getAllStaff();

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Danh sách nhân viên");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);

        Row headerRow = sheet.createRow(0);

        String[] columns = {"STT", "Mã nhân viên", "Họ tên", "Email FPT", "Email FE", "Trạng thái"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);

        int rowNum = 1;
        for (Staff staff : staffList) {
            Row row = sheet.createRow(rowNum++);

            Cell sttCell = row.createCell(0);
            sttCell.setCellValue(rowNum - 1);
            sttCell.setCellStyle(dataStyle);

            Cell staffCodeCell = row.createCell(1);
            staffCodeCell.setCellValue(staff.getStaffCode());
            staffCodeCell.setCellStyle(dataStyle);

            Cell nameCell = row.createCell(2);
            nameCell.setCellValue(staff.getName());
            nameCell.setCellStyle(dataStyle);

            Cell emailFptCell = row.createCell(3);
            emailFptCell.setCellValue(staff.getAccountFpt());
            emailFptCell.setCellStyle(dataStyle);

            Cell emailFeCell = row.createCell(4);
            emailFeCell.setCellValue(staff.getAccountFe());
            emailFeCell.setCellStyle(dataStyle);

            Cell statusCell = row.createCell(5);
            statusCell.setCellValue(staff.getStatus() == 1 ? "Hoạt động" : "Không hoạt động");
            statusCell.setCellStyle(dataStyle);
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "danh_sach_nhan_vien_" + dateFormat.format(new Date()) + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}
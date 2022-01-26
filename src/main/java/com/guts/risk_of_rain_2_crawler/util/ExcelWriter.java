package com.guts.risk_of_rain_2_crawler.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Created by yitian.luo on 2022/1/17.
 */
public class ExcelWriter {
    private final XSSFWorkbook workbook;

    private final XSSFSheet sheet;

    private final String path;


    public ExcelWriter(String filename) {
        this.path = filename;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
    }

    public void writeHead(List<String> head) {
        writeLine(0, head);
    }

    public void writeHead(String ...head) {
        writeLine(0, head);
    }

    public void writeLine(int rowIndex, List<String> line) {
        XSSFRow row = sheet.createRow(rowIndex);
        for (int i = 0; i < line.size(); i++) {
            row.createCell(i).setCellValue(line.get(i));
        }
    }

    public void writeLine(int rowIndex, String[] line) {
        XSSFRow row = sheet.createRow(rowIndex);
        for (int i = 0; i < line.length; i++) {
            row.createCell(i).setCellValue(line[i]);
        }
    }

    public void writeLine(String[] line) {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < line.length; i++) {
            row.createCell(i).setCellValue(line[i]);
        }
    }

    public void writeContent(List<List<String>> content) {
        for (int i = 0; i < content.size(); i++) {
            writeLine(i + 1, content.get(i));
        }
    }

    public void writeLine(List<String> line) {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < line.size(); i++) {
            row.createCell(i).setCellValue(line.get(i));
        }
    }

    public void outputWorkbook() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
    }

    public void close() throws IOException {
        workbook.close();
    }
}

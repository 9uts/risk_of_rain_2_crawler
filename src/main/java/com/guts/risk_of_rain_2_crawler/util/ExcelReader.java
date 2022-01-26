package com.guts.risk_of_rain_2_crawler.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Created by yitian.luo on 2022/1/17.
 */
public class ExcelReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

    private final XSSFSheet sheet;
    private final XSSFWorkbook workbook;

    private final int maxCol;
    private final int maxRow;

    public ExcelReader(String filename) throws IOException {
        String path = Objects.requireNonNull(ExcelReader.class.getClassLoader().getResource("excel/")).getPath() + filename;
        workbook = new XSSFWorkbook(path);
        sheet = workbook.getSheetAt(0);
        if (sheet.getLastRowNum() == 0) {
            LOGGER.info("Empty excel file");
            maxCol = 0;
            maxRow = 0;
        } else {
            maxRow = sheet.getLastRowNum();
            maxCol = sheet.getRow(0).getLastCellNum();
        }

    }

    public List<List<String>> forEachRow(ExcelReaderHelper helper) {
        List<List<String>> content = new LinkedList<>();
        for (int i = 1; i <= maxRow; i++) {
            List<String> row = new LinkedList<>();
            for (int j = 0; j < maxCol; j++) {
                XSSFCell cell = sheet.getRow(i).getCell(j);
                if (cell == null) {
                    row.add("");
                } else {
                    cell.setCellType(CellType.STRING);
                    row.add(cell.getStringCellValue());
                }
            }
            List<String> rowAfterProcess = helper.process(row);
            content.add(rowAfterProcess);
        }
        return content;
    }

    public interface ExcelReaderHelper {
        List<String> process(List<String> row);
    }

    public void close() throws IOException {
        workbook.close();
    }
}

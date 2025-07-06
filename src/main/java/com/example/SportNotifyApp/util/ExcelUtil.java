package com.example.SportNotifyApp.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

     public static <T> void writeToExcel(List<T> dataList, List<String> headers, List<String> fieldNames,
               String filePath) {
          if (dataList == null || dataList.isEmpty()) {
               log.warn("⚠️ No data to write.");
               return;
          }

          try (Workbook workbook = new XSSFWorkbook()) {
               Sheet sheet = workbook.createSheet("Sheet1");

               // Ghi header
               Row headerRow = sheet.createRow(0);
               for (int i = 0; i < headers.size(); i++) {
                    headerRow.createCell(i).setCellValue(headers.get(i));
               }

               // Ghi dữ liệu
               for (int rowIdx = 0; rowIdx < dataList.size(); rowIdx++) {
                    T item = dataList.get(rowIdx);
                    Row row = sheet.createRow(rowIdx + 1);

                    for (int colIdx = 0; colIdx < fieldNames.size(); colIdx++) {
                         String fieldName = fieldNames.get(colIdx);
                         Object value = getFieldValue(item, fieldName);
                         row.createCell(colIdx).setCellValue(value != null ? value.toString() : "");
                    }
               }

               // Lưu file
               try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                    log.info("📄 Excel file created: " + filePath);
               }

          } catch (IOException e) {
               log.error("❌ Failed to write Excel file: " + e.getMessage());
          }
     }

     private static Object getFieldValue(Object obj, String fieldName) {
          try {
               Field field = obj.getClass().getDeclaredField(fieldName);
               field.setAccessible(true);
               return field.get(obj);
          } catch (Exception e) {
               return null;
          }
     }
}

package ru.khairullin.ar.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;


public class CreateExcel {
    private static void writeHeaderLineOne(XSSFSheet sheet, List<String> headers) {
        Row headerRow = sheet.createRow(0);
        int len = headers.size();
        for (int i = 0; i < len; ++i) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers.get(i));
        }
    }

    public static void writeDataLinesOne(ResultSet result, List<String> headers, XSSFSheet sheet) throws SQLException, SQLException {
        int rowNum = 1;
        result.beforeFirst();

        while (result.next()) {
            Row row = sheet.createRow(rowNum++);
            int columnNum = 0;
            for (String header : headers) {
                String table_field = result.getString(header);
                Cell cell = row.createCell(columnNum++);
                cell.setCellValue(table_field);
            }
        }
    }

    public static void generateExcel(ResultSet result, String filename) throws SQLException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(filename);
        sheet.createFreezePane(0, 1);

        List<String> headers = new ArrayList<>();

        result.beforeFirst();
        result.next();
        ResultSetMetaData rsmd = result.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            String columnValue =  rsmd.getColumnName(i);
            headers.add(columnValue);
        }

        writeHeaderLineOne(sheet, headers);

        writeDataLinesOne(result, headers, sheet);

        FileOutputStream outputStream = new FileOutputStream(filename);
        workbook.write(outputStream);
        workbook.close();
    }
}

package com.legion.utils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertExcelToCsv {

    public static void excelToCsv(String filePath, String savePath) {
        String buffer = "";
        String cellContent = "";
        try {
            File file = new File(filePath);
            //set code for reading file
            WorkbookSettings setEncode = new WorkbookSettings();
            setEncode.setEncoding("GB2312");
            //get excel workbook object
            Workbook wb = Workbook.getWorkbook(file, setEncode);
            //get sheet0 (default: there is only one sheet)
            Sheet sheet = wb.getSheet(0);
            //test: print excel content
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    cellContent = sheet.getCell(j, i).getContents();
                    System.out.println(cellContent);
                    buffer += cellContent.replaceAll("\n", "") + ",";
                }
                buffer = buffer.substring(0, buffer.lastIndexOf(",")).toString();
                buffer += "\n";
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write string into the file
        File saveCSV = new File(savePath);
        try {
            if (saveCSV.exists()) {
                saveCSV.delete();
            }
            saveCSV.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
            writer.write(buffer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "src/test/resources/uploadFile/TimeOffRequestImport4059.xls";
        String savePath = "src/test/resources/uploadFile/PTO4059_Auto1.csv";
        excelToCsv(filePath, savePath);
    }


}

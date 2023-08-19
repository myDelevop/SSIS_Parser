package it.regione.rer.output.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.regione.rer.parsers.util.Costanti;
import it.regione.rer.parsers.util.Query;

public class ExcelWriter {
	
	private static String[] columns = {Costanti.COLUMN_UTENTE, Costanti.COLUMN_VISTA, Costanti.AMBITO, Costanti.NOTE, Costanti.QUERY};
	
	
	public static void writeQueriesOnExcel(final List<Query> queries, final String outputPath) {
		
		FileOutputStream fileOut = null;
		Workbook workbook = null;
		try { 
			
			
	
			
			workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
	//
	        CreationHelper createHelper = workbook.getCreationHelper();
	        Sheet sheet = workbook.createSheet("Viste SAP_BW");
	//
	        // Create a Font for styling header cells
//	        Font headerFont = workbook.createFont();
//	        headerFont.setBold(true);
//	        headerFont.setFontHeightInPoints((short) 14);
//	        headerFont.setColor(IndexedColors.RED.getIndex());
	//
	        // Create a CellStyle with the font
//	        CellStyle headerCellStyle = workbook.createCellStyle();
//	        headerCellStyle.setFont(headerFont);
	//
//	        // Create a Row
	        Row headerRow = sheet.createRow(0);
	
	        // Create cells
	        for(int i = 0; i < columns.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns[i]);
//	            cell.setCellStyle(headerCellStyle);
	        }
	
	        // Create Other rows and cells with employees data
	        int rowNum = 1;
	        for(Query query: queries) {
	            Row row = sheet.createRow(rowNum++);
	
	            row.createCell(0)
	                    .setCellValue(query.getQueryUser());
	
	            row.createCell(1)
	            .setCellValue(query.getQueryTable());
	            
	            row.createCell(2)
	            .setCellValue(query.getAmbito());
	
	            row.createCell(3)
	            .setCellValue(query.getNotes());
	
	            row.createCell(4)
	            .setCellValue(query.getQuery());
	        }
	
			// Resize all columns to fit the content size
	        for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	        }
	
	        // Write the output to a file
	        
	        fileOut = new FileOutputStream(outputPath + "/" 
	        		+ "outputFile" + System.currentTimeMillis() + ".xlsx");
	        workbook.write(fileOut);
	        
	
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			if(workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	


}

package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReporteDependencia {

	private static final String company = "PETRÓLEOS DEL PERÚ - PETROPERÚ S.A.";
	private static final String title = "REPORTE DEPENDENCIA - UNIDAD MATRICIAL";
	private static final String supplierLabel = "CORRESPONDENCIAS";
	private static final String userCreateLabel = "Generado por:";
	private static final String fechaHoraLabel = "Fecha y hora:";
	
	private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private Map<String, String> columns;
	private List<Map<String, Object>> rows;
	private String autor;
	private String title_aux;
	Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	
	public ExcelReporteDependencia(Map<String, String> columns, List<Map<String, Object>> rows, String autor) {
		super();
		this.columns = columns;
		this.rows = rows;
		this.autor = autor;
	}
	
	public ExcelReporteDependencia(Map<String, String> columns, List<Map<String, Object>> rows, String autor, String title) {
		super();
		this.columns = columns;
		this.rows = rows;
		this.autor = autor;
		this.title_aux = title;
	}
	
	
	public ByteArrayInputStream  buildReport() throws Exception {
		this.LOGGER.info("[INICIO] buildReport");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Workbook book = new XSSFWorkbook(); 
		try {
			CreationHelper createHelper = book.getCreationHelper();
			Sheet sheet = book.createSheet("PETROPERÚ");
			
			Font titleFont = book.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleFont.setColor(IndexedColors.BLACK.getIndex());
	        
			Font labelFont = book.createFont();
			labelFont.setBold(true);
			labelFont.setFontHeightInPoints((short) 12);
			labelFont.setColor(IndexedColors.BLACK.getIndex());
			
			Font valueFont = book.createFont();
			valueFont.setBold(false);
			valueFont.setFontHeightInPoints((short) 12);
			valueFont.setColor(IndexedColors.BLACK.getIndex());
			
			CellStyle titleStyle = book.createCellStyle();
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			
			CellStyle labelStyle = book.createCellStyle();
			labelStyle.setFont(labelFont);
			labelStyle.setAlignment(HorizontalAlignment.RIGHT);
			
			CellStyle simpleStyle = book.createCellStyle();
			simpleStyle.setFont(valueFont);
			simpleStyle.setAlignment(HorizontalAlignment.LEFT);
			
			CellStyle headerTableStyle = book.createCellStyle();
			headerTableStyle.setFont(labelFont);
			headerTableStyle.setAlignment(HorizontalAlignment.CENTER);
			headerTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyle.setBorderBottom(BorderStyle.THIN);
			headerTableStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyle.setBorderTop(BorderStyle.THIN);
			headerTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyle.setBorderLeft(BorderStyle.THIN);
			headerTableStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyle.setBorderRight(BorderStyle.THIN);
			headerTableStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerTableStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			CellStyle headerTableStyleNumber = book.createCellStyle();
			headerTableStyleNumber.setFont(valueFont);
			headerTableStyleNumber.setAlignment(HorizontalAlignment.RIGHT);
			headerTableStyleNumber.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyleNumber.setBorderBottom(BorderStyle.THIN);
			headerTableStyleNumber.setTopBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyleNumber.setBorderTop(BorderStyle.THIN);
			headerTableStyleNumber.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyleNumber.setBorderLeft(BorderStyle.THIN);
			headerTableStyleNumber.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerTableStyleNumber.setBorderRight(BorderStyle.THIN);
			
			CellStyle valueStringTableStyle = book.createCellStyle();
			valueStringTableStyle.setFont(valueFont);
			valueStringTableStyle.setAlignment(HorizontalAlignment.LEFT);
			valueStringTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			valueStringTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			valueStringTableStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			valueStringTableStyle.setBorderBottom(BorderStyle.THIN);
			valueStringTableStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			valueStringTableStyle.setBorderTop(BorderStyle.THIN);
			valueStringTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			valueStringTableStyle.setBorderLeft(BorderStyle.THIN);
			valueStringTableStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			valueStringTableStyle.setBorderRight(BorderStyle.THIN);
			valueStringTableStyle.setWrapText(true);
			
			CellStyle valueNumberTableStyle = book.createCellStyle();
			valueNumberTableStyle.setFont(valueFont);
			valueNumberTableStyle.setAlignment(HorizontalAlignment.RIGHT);
			valueNumberTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			valueNumberTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			valueNumberTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			valueNumberTableStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			valueNumberTableStyle.setBorderBottom(BorderStyle.THIN);
			valueNumberTableStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			valueNumberTableStyle.setBorderTop(BorderStyle.THIN);
			valueNumberTableStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			valueNumberTableStyle.setBorderLeft(BorderStyle.THIN);
			valueNumberTableStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			valueNumberTableStyle.setBorderRight(BorderStyle.THIN);
			
			int rowNum = 1;
			Row row = sheet.createRow(rowNum);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, columns.size() + 3));
			Cell ctitle = row.createCell(0);
			ctitle.setCellValue(company);
			ctitle.setCellStyle(titleStyle);
			
			
			rowNum ++;
			row = sheet.createRow(rowNum);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, columns.size() + 3));
			Cell subtitle = row.createCell(0);
			if(this.title_aux == null || "".equalsIgnoreCase(this.title_aux)){
				subtitle.setCellValue(title);
			}else{
				subtitle.setCellValue(this.title_aux);
			}
			//subtitle.setCellValue(title);
			subtitle.setCellStyle(titleStyle);
			
			rowNum ++;
			rowNum ++;
			
			rowNum ++;
			row = sheet.createRow(rowNum);
			Cell cuserCreated = row.createCell(1);
			cuserCreated.setCellValue(userCreateLabel);
			cuserCreated.setCellStyle(labelStyle);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 5));
			Cell cuserCreatedVal = row.createCell(2);
			cuserCreatedVal.setCellValue(autor);
			cuserCreatedVal.setCellStyle(simpleStyle);
			
			Cell cdatehourLabel = row.createCell(6);
			cdatehourLabel.setCellValue(fechaHoraLabel);
			cdatehourLabel.setCellStyle(labelStyle);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 7, 8));
			Cell cdateHourValue = row.createCell(7);
			cdateHourValue.setCellValue(formatterDate.format(new Date()));
			cdateHourValue.setCellStyle(simpleStyle);
			
			rowNum ++;
			rowNum ++;
			row = sheet.createRow(rowNum);
			Iterator<String> it = columns.keySet().iterator();
	        int numCol = 1;
			while(it.hasNext()) {
				String key = it.next();	
				Cell cell = row.createCell(numCol);
				cell.setCellValue(columns.get(key));
				cell.setCellStyle(headerTableStyle);
				numCol++;
			}
			rowNum ++;
			numCol = 1;
			it = columns.keySet().iterator();
			for (Map<String, Object>  _row : rows) {
				row = sheet.createRow(rowNum);
				it = columns.keySet().iterator();
				while(it.hasNext()) {
					String key = it.next();
					Cell cell = row.createCell(numCol);
					if(_row.get(key) instanceof Integer  ) {
						cell.setCellValue((Integer)_row.get(key));
						cell.setCellStyle(valueNumberTableStyle);
					} else if(_row.get(key) instanceof Double){
						cell.setCellValue((Double)_row.get(key));
						cell.setCellStyle(valueNumberTableStyle);
					}else {
						//System.out.println(_row.get(key));
						//cell.setCellValue((String)_row.get(key).toString());
						cell.setCellValue(_row.get(key)==null ? "" : String.valueOf(_row.get(key)));
						cell.setCellStyle(valueStringTableStyle);
					}
					numCol++;
				}
				rowNum++;
				numCol = 1;
			}
			
			for(int i = 0; i < 25; i++) {
	            sheet.autoSizeColumn(i);
	        }
			
			book.write(out);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error en generar reporte excel");
		}
		this.LOGGER.info("[FIN] buildReport");
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	
}

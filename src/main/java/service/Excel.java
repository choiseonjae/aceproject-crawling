package service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import model.ExcelRow;

public class Excel {

	public static void write(String path, List<ExcelRow> players){

		// workbook, sheet생성
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("시트명");

		// 엑셀의 행
		HSSFRow row = null;

		// 엑셀의 셀
		HSSFCell cell = null;
		
		// 전체 DB
		for(int rowIdx = 0; rowIdx < players.size(); rowIdx++) {
			// 튜플 생성
			List<String> tuple = players.get(rowIdx).getRow();
			row = sheet.createRow(rowIdx);
			
			for(int cellIdx = 0; cellIdx < tuple.size(); cellIdx++) {
				// row의 특정 컬럼(열)을 생성 후 데이터 입력
				cell = row.createCell(cellIdx);
				cell.setCellValue(tuple.get(cellIdx).toString());
			}		
		}
		
		System.out.println(path);

		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(path);
			// 파일을 쓴다
			workbook.write(fileoutputstream);
			// 필수로 닫아주어야함
			fileoutputstream.close();
			workbook.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		System.out.println("File 생성");
	}
}

package com.gb.fineos.util;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Class Name : XlsReader
 * Description : Used to perform actions on Excel files
 */
public class XlsReader {
	private static final Logger LOG = Logger.getLogger(XlsReader.class);

	private String path;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;
    private XSSFCell cell = null;

    /**
	 * Method Name : Xls_Reader()
     * Description : Parameterized Constructor Used to identify
     * the Location of EXCEL files
     */
    public XlsReader(final String path) {
        this.path = path;

        try (final FileInputStream fis = new FileInputStream(path)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
        	LOG.error("Unable to open XLS : " + path, e);
        }
    }

    /**
	 * Method Name : getRowCount()
     * Description : Used to returns the row count in a sheet
     */
    public int getRowCount(final String sheetName) {
        final int index = workbook.getSheetIndex(sheetName);

        if (index == -1) {
			return 0;
		} else {
            sheet = workbook.getSheetAt(index);
            return sheet.getLastRowNum() + 1;
        }
    }

    /**
	 * Method Name : getCellData() based on SheetName, ColumnName, RowNumber
     * Description : Used to returns the data from a cell
     */
    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            if (rowNum <= 0)
                return "";

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                    colNum = i;
            }

            if (colNum == -1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                return "";
            cell = row.getCell(colNum);

            if (cell == null)
                return "";

            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                String cellText = String.valueOf(cell.getNumericCellValue());

                if (DateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    final Calendar cal = Calendar.getInstance();
                    cal.setTime(DateUtil.getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;
                }

                return cellText;
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return "";
			} else {
				return String.valueOf(cell.getBooleanCellValue());
			}
        } catch (Exception e) {
			LOG.error("Unable read cell data from sheet: " + sheetName + " colName: " + colName + " rowNum: " + rowNum, e);
            return "row " + rowNum + " or column " + colName + " does not exist in xls";
        }
    }

    /**
     * Method Name : getCellData() based on SheetName, ColumnNumber, RowNumber
     * Description : Used to returns the data from a cell
     */
    public String getCellData(String sheetName, int colNum, int rowNum) {
        try {
            if (rowNum <= 0)
                return "";

            int index = workbook.getSheetIndex(sheetName);

            if (index == -1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                return "";
            cell = row.getCell(colNum);
            if (cell == null)
                return "";

            if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                return cell.getStringCellValue();
            else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (DateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(DateUtil.getJavaDate(d));
                    cellText =
                            (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH) + 1 + "/" +
                            cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            cellText;
                }
                return cellText;
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return "";
			} else {
				return String.valueOf(cell.getBooleanCellValue());
			}
        } catch (Exception e) {
			LOG.error("Unable read cell data from sheet: " + sheetName + " colNum: " + colNum + " rowNum: " + rowNum, e);
            return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
        }
    }

    /* Method Name : setCellData() based on SheetName, ColumnName, RowHeaderNumber, RowNumber, Data
     * Description : Used to returns true if data is set successfully else false
     */

    public boolean setCellData(String sheetName, String colName, int rowHeaderNumber, int rowNum, String data) {
        try (final FileInputStream fis = new FileInputStream(path)) {
            workbook = new XSSFWorkbook(fis);

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;

            sheet = workbook.getSheetAt(index);
            rowHeaderNumber = rowHeaderNumber - 1;
            row = sheet.getRow(rowHeaderNumber);

            if (rowNum <= 0) {
                return false;
            }

            if (index == -1) {
                return false;
            }

            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
                    colNum = i;
                    break;
                }
            }
            if (colNum == -1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);

            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);
            final FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);

            fileOut.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}

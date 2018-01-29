package util;

import java.io.File;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LeerExcel {

	private static String[][] matriz = null;
	private static String ruta_xlsx;

	@SuppressWarnings("deprecation")
	public static String[][] retornaDatosExcel(String modulo, String caso) throws IOException {
		String ruta_xlsx = "C:\\desarrollos\\Ejecucion_Automatizada\\Datos_Temporales\\" + caso + ".xlsx";
		try {

			FileInputStream file = new FileInputStream(new File(ruta_xlsx));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();
			Row row;

			String valorString = "";
			int valorInt = -1;
			int contadorFilas = 0, contadorCol = 0;

			matriz = new String[countRow(sheet) + 1][countCol(sheet)];

			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell celda;

				while (cellIterator.hasNext()) {

					celda = cellIterator.next();

					switch (celda.getCellType()) {

					case Cell.CELL_TYPE_NUMERIC:
						valorInt = (int) Math.round(celda.getNumericCellValue());
						break;

					case Cell.CELL_TYPE_STRING:
						valorString = celda.getStringCellValue();
						break;
					}

					if (!valorString.equals("")) {
						matriz[contadorFilas][contadorCol] = valorString;
						valorString = "";
					} else if (valorInt != -1) {
						matriz[contadorFilas][contadorCol] = Integer.toString(valorInt);
						valorInt = -1;
					}

					if (contadorCol != countCol(sheet) - 1) {
						contadorCol++;
					} else {
						contadorCol = 0;
					}
				}
				contadorFilas++;

			}

			if (matriz.length < 2) {
				System.out.println("El archivo excel: '" + caso + ".xlsx' no contiene datos");
				workbook.close();
			}

			workbook.close();
		} catch (Exception e) {
			System.out.println("El archivo excel: '" + caso + ".xlsx' no existe");
			// System.exit(0);
		}

		return matriz;
	}

	public static int countCol(XSSFSheet sheet) {
		int cantidad = 0;
		Iterator<Row> rowIterator = sheet.rowIterator();
		if (rowIterator.hasNext()) {
			Row headerRow = (Row) rowIterator.next();
			cantidad = headerRow.getPhysicalNumberOfCells();
		}
		return cantidad;
	}

	public static int countRow(XSSFSheet sheet) {
		int cantidad = 0;
		cantidad = sheet.getLastRowNum();
		return cantidad;
	}

	public String valorCol(String nomCol, String[][] matriz) {

		String valor = "";
		boolean flag = false;

		try {
			for (int j = 0; j < matriz[0].length; j++) {
				if (matriz[0][j].toString().equals(nomCol)) {
					valor = matriz[1][j].toString();
					flag = true;
					break;
				}
			}

			if (!flag) {
				System.out.println("La columna '" + nomCol + "' no se encuentra en el archivo excel");
				//valor = "La columna '" + nomCol + "' no se encuentra en el archivo excel";
				//System.exit(0);
			}

		} catch (Exception e) {
			System.out.println("Error al retornar valor de matriz de datos: " + nomCol);
		}
		return valor;
	}

	public static void setTextRow(String columna, String valor, String caso) {

		try {
			
			ruta_xlsx = "C:\\desarrollos\\Ejecucion_Automatizada\\Datos_Temporales\\" + caso + ".xlsx";
			FileInputStream file = new FileInputStream(new File(ruta_xlsx));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Cell cell = null;

			XSSFRow sheetrow = sheet.getRow(1);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row;
			int countCol = 0;
			boolean flag = false;
			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					cell = cellIterator.next();

					if (cell.getStringCellValue().equals(columna)) {
						if (sheetrow == null) {
							sheetrow = sheet.createRow(1);
						}
						cell = sheetrow.getCell(countCol);
						cell.setCellValue(valor);
						flag = true;
						break;
					}
					// cell = cellIterator.next();
					countCol++;
				}

				if (flag)
					break;
			}

			FileOutputStream fos = new FileOutputStream(new File(ruta_xlsx));
			workbook.write(fos);
			workbook.close();

		} catch (Exception e) {

		}
	}
}
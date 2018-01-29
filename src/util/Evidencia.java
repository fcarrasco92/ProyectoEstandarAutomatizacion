package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Evidencia {

	private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL,
			BaseColor.BLACK);
	private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL);
	private static final Font categoryFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);

	public void createPDF(ArrayList<String> arrEvidencia, String caso, String path, boolean stateCase)
			throws MalformedURLException, IOException {

		String estadoCaso;

		if (stateCase) {
			estadoCaso = "APROBADO";
		} else {
			estadoCaso = "RECHAZADO";
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		path = path.replace(" ", "_");
		File folder = new File("C:\\desarrollos\\Ejecucion_Automatizada\\" + path + "PDF");
		
		if (!folder.exists())
			folder.mkdirs();

		File pdfNewFile = new File("C:\\desarrollos\\Ejecucion_Automatizada\\" + path + "PDF\\" + caso + ".pdf");

		try {

			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));

			document.open();

			Anchor anchor = new Anchor("		Evidencia Test Case: ", categoryFont);
			Anchor nombreCaso = new Anchor("		" + caso, paragraphFont);
			Anchor fechaEjecucion = new Anchor("		Fecha: " + dateFormat.format(date), chapterFont);
			Anchor estado = new Anchor("		Estado: " + estadoCaso, chapterFont);

			Image image;
			Integer numColumns = 1;
			Integer numRows = arrEvidencia.size();

			document.add(anchor);
			document.add(Chunk.NEWLINE);
			document.add(nombreCaso);
			document.add(Chunk.NEWLINE);
			document.add(fechaEjecucion);
			document.add(Chunk.NEWLINE);
			document.add(estado);
			document.add(Chunk.NEWLINE);

			PdfPTable table = new PdfPTable(numColumns);
			table.setTotalWidth(500);
			table.setLockedWidth(true);
			int cantTablas = returnTables(numRows).size();
			int cantRow = 2;
			int numStep = 0;

			for (int i = 0; i < cantTablas; i++) {
				table = returnTables(numRows).get(i);
				for (int e = 0; e < numColumns; e++) {

					if (arrEvidencia.size() % 2 != 0 && i == (cantTablas - 1)) {
						cantRow = 1;
					}

					for (int j = 0; j < cantRow; j++) {
						if (arrEvidencia.get(numStep).contains("-")) {
							String[] arrEv = arrEvidencia.get(numStep).split("-");
							image = Image.getInstance(arrEv[1]);
							image.setAbsolutePosition(2, 300);

							table.addCell("Step N°: " + (numStep + 1) + "  >>  Name Step: " + arrEv[0] + "  >>  State: "
									+ arrEv[2].toString());
							if (arrEv.length > 3) {
								table.addCell("Error >> " + arrEv[3].toString());
							}

						} else {
							image = Image.getInstance(arrEvidencia.get(numStep));
							image.setAbsolutePosition(2, 300);
						}
						table.addCell(image);
						numStep++;
					}
				}
				document.add(table);
				document.newPage();
			}

			document.close();
			System.out.println("¡Se ha generado tu hoja PDF!");

		} catch (DocumentException documentException) {
			System.out.println("Se ha producido un error al generar un documento: " + documentException);
		}
	}

	public List<PdfPTable> returnTables(double filas) {
		ArrayList<PdfPTable> tables = new ArrayList<PdfPTable>();
		try {
			double cantTablas = 0;
			cantTablas = filas / 2;
			PdfPTable table = new PdfPTable(1);

			for (int i = 0; i < Math.round(cantTablas); i++) {
				tables.add(table);
			}
		} catch (Exception e) {
			System.out.println("ww ");
		}
		return tables;
	}
}

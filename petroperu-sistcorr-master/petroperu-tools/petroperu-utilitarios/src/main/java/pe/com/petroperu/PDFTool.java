package pe.com.petroperu;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFTool {

	private String pathSource;
	private String pathTo;
	private String text;
	private String url;
	private String clave;
	private String nroDocumento;
	private String tipoCorrespondencia;
	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public PDFTool(String pathSource, String pathTo, String text) {
		super();
		this.pathSource = pathSource;
		this.pathTo = pathTo;
		this.text = text;
	}

	/*
	 * public PDFTool(String pathSource, String pathTo, String url, String
	 * clave, String nroDocumento) { super(); this.pathSource = pathSource;
	 * this.pathTo = pathTo; this.url = url; this.clave = clave;
	 * this.nroDocumento = nroDocumento; }
	 */

	public PDFTool(String pathSource, String pathTo, String url, String clave, String nroDocumento,
			String tipoCorrespondencia) {
		super();
		this.pathSource = pathSource;
		this.pathTo = pathTo;
		this.url = url;
		this.clave = clave;
		this.nroDocumento = nroDocumento;
		this.tipoCorrespondencia = tipoCorrespondencia;
	}

	public void writePDF() {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] writePDF");
		try {
			PdfReader pdfReader = new PdfReader(this.pathSource);

			PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(this.pathTo));

			for (int i = 0; i < pdfReader.getNumberOfPages(); i++) {
				// TICKET 9000003992
				this.LOGGER.info("[INFO] writePDF " + " This is info  : " + i);
//				System.out.println(i);
				PdfContentByte content = pdfStamper.getOverContent(i + 1);
				addText(content, this.text);
			}
			pdfStamper.close();
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] writePDF " + " This is error  : " + e);
			// e.printStackTrace();
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] writePDF");
	}

	private void addText(PdfContentByte content, String text) throws DocumentException, IOException {
		String texto = "Si ésta es una reproducción impresa, es una copia auténtica de un documento electrónico firmado digitalmente y ";
		String texto2 = "archivado en Petróleos del Perú - PETROPERÚ S.A., aplicando la Tercera Disposición Complementaria Final del ";
		String texto3 = "D.S. 026-2016-PCM. Su autenticidad e integridad pueden ser contrastadas a través de la siguiente dirección web:";
		// String texto4 = "";
		/*
		 * PdfPTable table = new PdfPTable(3);
		 * table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
		 * table.setTotalWidth(300); PdfPCell cell = new PdfPCell(new
		 * Paragraph(texto)); cell.setColspan(3); cell.setNoWrap(false);
		 * cell.setRotation(270); table.addCell(cell); PdfPCell cell_1 = new
		 * PdfPCell(new Paragraph("Url: ")); cell_1.setRotation(270); PdfPCell
		 * cell_2 = new PdfPCell(new Paragraph("Clave: "));
		 * cell_2.setRotation(270); PdfPCell cell_3 = new PdfPCell(new
		 * Paragraph("Nro. Documento: ")); cell_3.setRotation(270);
		 * table.addCell(cell_1); table.addCell(cell_2); table.addCell(cell_3);
		 * table.writeSelectedRows(100, 100, 110, 150, content);
		 */

		BaseFont bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		content.beginText();
		content.setFontAndSize(bf_times, 8f);
		// content.setTextMatrix(210, 55);
		// content.setTextMatrix(10, 55);
		// content.showText(texto);
		content.showTextAligned(0, texto, 16, 10 + 80, 90);
		content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		content.setRGBColorFill(0, 0, 0);
		content.endText();

		bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		content.beginText();
		content.setFontAndSize(bf_times, 8f);
		// content.setTextMatrix(210, 45);
		// content.setTextMatrix(20, 45);
		// content.showText(texto2);
		content.showTextAligned(0, texto2, 26, 10 + 80, 90);
		content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		content.setRGBColorFill(0, 0, 0);
		content.endText();

		bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		content.beginText();
		content.setFontAndSize(bf_times, 8f);
		// content.setTextMatrix(210, 35);
		// content.setTextMatrix(10, 35);
		// content.showText(texto3);
		content.showTextAligned(0, texto3, 36, 10 + 80, 90);
		content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		content.setRGBColorFill(0, 0, 0);
		content.endText();

		/*
		 * bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		 * content.beginText(); content.setFontAndSize(bf_times, 8f);
		 * //content.setTextMatrix(210, 25); //content.setTextMatrix(10, 25);
		 * //content.showText(texto4); content.showTextAligned(0, texto4, 46,
		 * 10, 90); content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		 * content.setRGBColorFill(0, 0, 0); content.endText();
		 */

		content.setFontAndSize(bf_times, 12);
		/*
		 * int y1 = 3; int y2 = 64; int x1 = 7; //207; int x2 = 410; //385;
		 * //585;
		 */
		int y1 = 7 + 80;
		int y2 = 417 + 80;
		int x1 = 7; // 3;
		int x2 = 58; // 64;
		content.moveTo(x1, y1);
		content.lineTo(x2, y1);
		content.moveTo(x1, y2);
		content.lineTo(x2, y2);
		content.moveTo(x1, y1);
		content.lineTo(x1, y2);
		content.moveTo(x2, y1);
		content.lineTo(x2, y2);
		content.stroke();

		/*
		 * bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252",
		 * false); content.beginText(); content.setFontAndSize(bf_times, 8f);
		 * content.setTextMatrix(210, 15); //content.showText("Url: " +
		 * this.url); content.showText("Url: " +
		 * "http://localhost:8080/ConsultaCorrespondencia");
		 * content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		 * content.setRGBColorFill(0, 0, 0); content.endText();
		 * 
		 * bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252",
		 * false); content.beginText(); content.setFontAndSize(bf_times, 8f);
		 * //content.setTextMatrix(365, 15); content.setTextMatrix(380, 15);
		 * //content.showText("Clave: " + this.clave);
		 * content.showText("Clave: " + "0ZEssJKcm6"); content.setColorFill(new
		 * BaseColor(0x00, 0x00, 0x00)); content.setRGBColorFill(0, 0, 0);
		 * content.endText();
		 * 
		 * bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252",
		 * false); content.beginText(); content.setFontAndSize(bf_times, 8f);
		 * content.setTextMatrix(450, 15); //content.showText("Nro. Documento: "
		 * + this.nroDocumento); content.showText("Nro. Documento: " +
		 * "GISE-0031-2020"); content.setColorFill(new BaseColor(0x00, 0x00,
		 * 0x00)); content.setRGBColorFill(0, 0, 0); content.endText();
		 */

		bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		content.beginText();
		content.setFontAndSize(bf_times, 8f);
		// content.setTextMatrix(210, 15);
		// content.setTextMatrix(10, 15);
		// content.showText("Url: " + this.url);
		// content.showTextAligned(0, "Url: " + this.url, 56, 10, 90);
		content.showTextAligned(0, "Url: " + this.url, 46, 10 + 80, 90);
		content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		content.setRGBColorFill(0, 0, 0);
		content.endText();

		bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		content.beginText();
		content.setFontAndSize(bf_times, 8f);
		// content.setTextMatrix(210, 5);
		// content.setTextMatrix(10, 5);
		// content.showText("Clave: " + this.clave);
		// content.showTextAligned(0, "Clave: " + this.clave, 66, 10, 90);
		content.showTextAligned(0, "Clave: " + this.clave, 56, 10 + 80, 90);
		// content.showTextAligned(0, "Clave: " + this.clave, 46, 300, 90);
		content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		content.setRGBColorFill(0, 0, 0);
		content.endText();

		bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		content.beginText();
		content.setFontAndSize(bf_times, 8f);
		// content.setTextMatrix(350, 5);
		// content.setTextMatrix(150, 5);
		// content.showText("Nro. Documento: " + this.nroDocumento);
		// content.showTextAligned(0, "Nro. Documento: " + this.nroDocumento,
		// 66, 150, 90);
		content.showTextAligned(0, "Nro. Documento: " + this.nroDocumento, 56, 150 + 80, 90);
		// content.showTextAligned(0, "Nro. Documento: " + this.nroDocumento,
		// 56, 10, 90);
		content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		content.setRGBColorFill(0, 0, 0);
		content.endText();

		/*
		 * bf_times = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		 * content.beginText(); content.setFontAndSize(bf_times, 8f);
		 * content.showTextAligned(0, "Tipo: " + this.tipoCorrespondencia, 56,
		 * 175, 90); content.setColorFill(new BaseColor(0x00, 0x00, 0x00));
		 * content.setRGBColorFill(0, 0, 0); content.endText();
		 */

	}

	public static void main() {
		PDFTool pdfTool = new PDFTool("/var/www/html/sistcorr/adjuntos/8502533695477851.pdf",
				"/var/www/html/sistcorr/adjuntos/demooo.pdf", "Esto es una demo");
		pdfTool.writePDF();
	}

	public static void main(String[] args) {
		// PDFTool pdfTool = new
		// PDFTool("/var/www/html/sistcorr/adjuntos/8502533695477851.pdf",
		// "/var/www/html/sistcorr/adjuntos/demooo.pdf", "Esto es una demo");
		PDFTool pdfTool = new PDFTool("/var/www/html/sistcorr/prueba/ABC.pdf", "/var/www/html/sistcorr/prueba/_abc.pdf",
				"Prueba de escritura de texto");
		pdfTool.writePDF();
	}

}

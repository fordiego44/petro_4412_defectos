package pe.com.petroperu.filenet.dao;

import java.util.Locale;

import pe.com.petroperu.model.emision.ArchivoAdjunto;

public interface ProcesoFirmaDigitalPdf {
	
	ArchivoAdjunto obtenerPrincipal(Long idCorrespondcia, Locale locale);
	
	Object[] procesarPDFFirmados(String nombrePDF, byte[] dataZip  ,Locale locale);

}

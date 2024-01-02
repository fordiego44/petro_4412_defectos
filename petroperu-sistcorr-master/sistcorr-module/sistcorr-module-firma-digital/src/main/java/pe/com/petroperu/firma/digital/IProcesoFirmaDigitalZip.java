package pe.com.petroperu.firma.digital;

import java.util.List;
import java.util.Locale;

import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correspondencia;

public interface IProcesoFirmaDigitalZip {
	
	List<ArchivoAdjunto> obtenerArchivosAFirmar(Long idCorrespondcia, Locale locale);
	
	String comprimirArchivos(List<ArchivoAdjunto> archivos, Long idCorrespondencia, Locale locale);

	byte[] dataZipeado(Long idCorrespondencia);
	
	Object[] procesarDocumentosFirmados(String nombreZip, byte[] dataZip  ,Locale locale);
	
	boolean borrarArchivosComprimir(Long idCorrespondencia);
	
	List<ArchivoAdjunto> obtenerArchivosAFirmarGrupal(String correspondencias, Locale locale);
	
	String comprimirArchivosGrupal(List<ArchivoAdjunto> archivos, String nameZip, Locale locale);
	
	Object[] procesarDocumentosFirmadosGrupal(String nombreZip, byte[] dataZip, Locale locale);
	
	boolean borrarArchivosComprimirGrupal(String correlativos, String username);

}

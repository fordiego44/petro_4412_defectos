package pe.com.petroperu.filenet.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;


public class ProcesoFirmaDigitalPdfImpl  implements ProcesoFirmaDigitalPdf {
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());
  
  @Value("${sistcorr.directorio}")
  private String DIRECCTORIO_BASE;
  
  private final String DIRECTORIO_COMPRIMIDOS = "comprimidos";
  
  private final String DIRECTORIO_COMPRIMIDOS_FIRMADOS = "comprimidos_firmados";
  
  @Autowired
  private IArchivoAdjuntoDAO archivoAdjuntoDAO;

  
  public ArchivoAdjunto obtenerPrincipal(Long idCorrespondcia, Locale locale) {
    List<ArchivoAdjunto> archivos = this.archivoAdjuntoDAO.obtenerArchivosAFirmar(idCorrespondcia);
    archivos = (archivos == null) ? new ArrayList<>() : archivos;
    return archivos.get(0);
  }



  
  public Object[] procesarPDFFirmados(String nombrePDF, byte[] dataZip, Locale locale) { return null; }
}

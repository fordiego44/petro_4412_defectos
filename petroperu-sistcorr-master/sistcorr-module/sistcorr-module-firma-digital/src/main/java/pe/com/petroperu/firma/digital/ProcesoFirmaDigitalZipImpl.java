package pe.com.petroperu.firma.digital;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correlativo;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrelativoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;

@Service
@PropertySource({ "classpath:application.properties" })
public class ProcesoFirmaDigitalZipImpl implements IProcesoFirmaDigitalZip {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${sistcorr.directorio}")
	private String DIRECCTORIO_BASE;

	private final String DIRECTORIO_COMPRIMIDOS = "comprimidos";

	private final String DIRECTORIO_COMPRIMIDOS_FIRMADOS = "comprimidos_firmados";

	@Autowired
	private IArchivoAdjuntoDAO archivoAdjuntoDAO;
	
	@Autowired
	private ICorrelativoDAO correlativoDAO;
	
	@Autowired
	private ICorrespondenciaDAO correspondenciaDAO;

	public List<ArchivoAdjunto> obtenerArchivosAFirmar(Long idCorrespondencia, Locale locale) {
		List<ArchivoAdjunto> archivos = this.archivoAdjuntoDAO.obtenerArchivosAFirmar(idCorrespondencia);
		archivos = (archivos == null) ? new ArrayList<>() : archivos;
		return archivos;
	}

	public String comprimirArchivos(List<ArchivoAdjunto> archivos, Long idCorrespondencia, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] comprimirArchivos");
		String zipeado = null;
		// TICKET 9000003992
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;

		try {
			String directorioComprimido = this.DIRECCTORIO_BASE + "comprimidos" + "/" + idCorrespondencia + ".zip";
			fos = new FileOutputStream(directorioComprimido);
			zipOut = new ZipOutputStream(fos);
			for (ArchivoAdjunto archivoAdjunto : archivos) {
				File fileToZip = new File(archivoAdjunto.getUbicacion());
				FileInputStream fis = new FileInputStream(fileToZip);
				ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
				zipOut.putNextEntry(zipEntry);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
				fis.close();
			}
			zipOut.close();
			fos.close();
			zipeado = directorioComprimido;
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] comprimirArchivos" + " This is error : " + e);
		}
		// TICKET 9000003992
		finally {
			if (fos != null) {
				try {
					fos.close();
					this.LOGGER.info("[INFO] comprimirArchivos " + " This is info: se libero el arhivo fos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] comprimirArchivos" + " This is error : " + e);
				}
			}
			if (zipOut != null) {
				try {
					zipOut.close();
					this.LOGGER.info("[INFO] comprimirArchivos " + " This is info: se libero el arhivo zipOut");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] comprimirArchivos" + " This is error : " + e);
				}
			}
		}

		// TICKET 9000003992
		this.LOGGER.info("[FIN] comprimirArchivos");
		return zipeado;
	}

	public byte[] dataZipeado(Long idCorrespondencia) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] dataZipeado");
		File zip = new File(this.DIRECCTORIO_BASE + "comprimidos" + "/" + idCorrespondencia + ".zip");
		if (zip.exists()) {
			// TICKET 9000003992
			// System.out.println(zip.getAbsolutePath());
			LOGGER.info("[INFO] " + " This is info : " + zip.getAbsolutePath());
			FileInputStream fis = null;
			byte[] bArray = new byte[(int) zip.length()];
			try {
				fis = new FileInputStream(zip);
				fis.read(bArray);
				fis.close();
				return bArray;
			} catch (Exception e) {
				// TICKET 9000003992
				LOGGER.error("[ERROR] " + " This is error : " + e);
				// e.printStackTrace();
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] dataZipeado");
		return null;
	}

	// TICKET 9000003992
	public boolean borrarArchivosComprimir(Long idCorrespondencia) {
		this.LOGGER.info("[INICIO] borrarArchivosComprimir");
		boolean borrado = false;

		File fileZip = null;

		try {
			String directorioComprimido = this.DIRECCTORIO_BASE + "comprimidos" + "/" + idCorrespondencia + ".zip";
			fileZip = new File(directorioComprimido);
			if (fileZip.delete()) {
				borrado = true;
			}

		} catch (Exception e) {
			this.LOGGER.error("[ERROR] borrarArchivosComprimir" + " This is error : " + e);
		}

		this.LOGGER.info("[FIN] borrarArchivosComprimir");
		return borrado;
	}
	
	// TICKET 9000004651
	public boolean borrarArchivosComprimirGrupal(String correlativos, String username) {
		this.LOGGER.info("[INICIO] borrarArchivosComprimirGrupal " + correlativos);
		boolean borrado = false;

		File fileZip = null;

		try {
			String[] corr = correlativos.split(",");
			Long temp = 0L;
			for(String codigo : corr){
				//Correlativo objCorrelativo = correlativoDAO.findOneByCodigo(codigo);
				Correspondencia objCorrespondencia = correspondenciaDAO.findOne(Long.valueOf(codigo));
				temp = temp + objCorrespondencia.getId();
			}
			String directorioComprimido = this.DIRECCTORIO_BASE + "comprimidos" + "/" + username + "-" + temp + ".zip";
			fileZip = new File(directorioComprimido);
			if (fileZip.delete()) {
				borrado = true;
			}else{
				this.LOGGER.info("No se pudo borrar el archivo: " + directorioComprimido);
			}

		} catch (Exception e) {
			this.LOGGER.error("[ERROR] borrarArchivosComprimirGrupal" + " This is error : ", e);
		}

		this.LOGGER.info("[FIN] borrarArchivosComprimirGrupal");
		return borrado;
	}
	// FIN TICKET

	public Object[] procesarDocumentosFirmados(String nombreZip, byte[] dataZip, Locale locale) {
		this.LOGGER.info("[INICIO] procesarDocumentosFirmados");
		Object[] respuesta = { null, null };
		try {
			long startTime = Instant.now().toEpochMilli();
			String directorio = this.DIRECCTORIO_BASE + "comprimidos_firmados" + "/" + startTime + ".zip";
			File _zip = new File(directorio);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(_zip));
			stream.write(dataZip);
			stream.close();

			String nombreArchivo = "";
			String directorioDescomprimido = this.DIRECCTORIO_BASE + "comprimidos_firmados" + "/" + startTime;
			File directorioDestino = new File(directorioDescomprimido);
			directorioDestino.mkdirs();
			byte[] buffer = new byte[2048];
			ZipInputStream zis = new ZipInputStream(new FileInputStream(directorio));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(directorioDestino, zipEntry);
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				nombreArchivo = zipEntry.getName();
				this.LOGGER.info("Descomprimidos: " + nombreArchivo);
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			if (!nombreArchivo.equals("")) {
				ArchivoAdjunto archivoAdjunto = this.archivoAdjuntoDAO.findOneByNombreServidor(nombreArchivo);
				if (archivoAdjunto != null) {
					respuesta[0] = archivoAdjunto.getCorrespondencia();
					respuesta[1] = Long.valueOf(startTime);
				}else{
					// TICKET 9000004981
					String[] partesNombre = nombreArchivo.split("-signaturefailed");
					String nombreCorrecto = partesNombre[0] + partesNombre[1];
					ArchivoAdjunto archivoAdjuntoCorrecto = this.archivoAdjuntoDAO.findOneByNombreServidor(nombreCorrecto);
					borrarArchivosComprimir(archivoAdjuntoCorrecto.getCorrespondencia().getId());
					// FIN TICKET
				}
			}
			// TICKET 9000004981
			respuesta[1] = Long.valueOf(startTime);
			// FIN TICKET
			_zip.delete();
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] procesarDocumentosFirmados", e);
		}
		this.LOGGER.info("[FIN] procesarDocumentosFirmados");
		return respuesta;
	}
	
	// TICKET 9000004651
	public Object[] procesarDocumentosFirmadosGrupal(String nombreZip, byte[] dataZip, Locale locale) {
		this.LOGGER.info("[INICIO] procesarDocumentosFirmados");
		Object[] respuesta = { null, null };
		long start = 0;
		try {
			long startTime = Instant.now().toEpochMilli();
			start = startTime;
			String directorio = this.DIRECCTORIO_BASE + "comprimidos_firmados" + "/" + startTime + ".zip";
			File _zip = new File(directorio);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(_zip));
			stream.write(dataZip);
			stream.close();

			List<String> nombresArchivos = new ArrayList<>();
			String nombreArchivo = "";
			String directorioDescomprimido = this.DIRECCTORIO_BASE + "comprimidos_firmados" + "/" + startTime;
			File directorioDestino = new File(directorioDescomprimido);
			directorioDestino.mkdirs();
			byte[] buffer = new byte[2048];
			ZipInputStream zis = new ZipInputStream(new FileInputStream(directorio));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(directorioDestino, zipEntry);
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				nombreArchivo = zipEntry.getName();
				this.LOGGER.info("Descomprimidos: " + nombreArchivo);
				nombresArchivos.add(nombreArchivo);
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			/*if (!nombreArchivo.equals("")) {
				ArchivoAdjunto archivoAdjunto = this.archivoAdjuntoDAO.findOneByNombreServidor(nombreArchivo);
				if (archivoAdjunto != null) {
					respuesta[0] = archivoAdjunto.getCorrespondencia();
					respuesta[1] = Long.valueOf(startTime);
				}
			}*/
			if(nombresArchivos.size() > 0){
				List<ArchivoAdjunto> archivosAdjuntos = archivoAdjuntoDAO.obtenerArchivosPorNombreServidor(nombresArchivos);
				List<Correspondencia> correspondencias = new ArrayList<>();
				
				for(int i = 0; i < archivosAdjuntos.size(); i++){
					ArchivoAdjunto arcAdj = archivosAdjuntos.get(i);
					boolean existe = false;
					for(Correspondencia corr : correspondencias){
						if(arcAdj.getCorrespondencia().getId().compareTo(corr.getId()) == 0){
							existe = true;
						}
					}
					if(!existe){
						correspondencias.add(arcAdj.getCorrespondencia());
					}
				}
				
				String[] correlativos = new String[correspondencias.size()];
				for(int i = 0;i < correspondencias.size(); i++){
					correlativos[i] = String.valueOf(correspondencias.get(i).getId());
				}
				if(correlativos.length > 0){
					String correlativosJuntos = String.join(",", correlativos);
					respuesta[0] = correlativosJuntos;
					respuesta[1] = Long.valueOf(startTime);
				}else{
					List<String> corr = new ArrayList<>();
					for(String archivo : nombresArchivos){
						List<String> nomb = new ArrayList<>();
						nomb.add(archivo);
						List<ArchivoAdjunto> archivos = archivoAdjuntoDAO.obtenerArchivosPorNombreServidor(nomb);
						if(archivos.size()>0){
							boolean existe = false;
							for(String cor : corr){
								if(archivos.get(0).getCorrespondencia().getId().compareTo(Long.valueOf(cor)) == 0){
									existe = true;
								}
							}
							if(!existe){
								corr.add(String.valueOf(archivos.get(0).getCorrespondencia().getId()));
							}
						}else{
							String[] partNom = archivo.split("-signaturefailed");
							String nombreCorregido = partNom[0] + partNom[1];
							nomb.clear();
							nomb.add(nombreCorregido);
							List<ArchivoAdjunto> archivosCorregido = archivoAdjuntoDAO.obtenerArchivosPorNombreServidor(nomb);
							if(archivosCorregido.size()>0){
								boolean existe = false;
								for(String cor : corr){
									if(archivosCorregido.get(0).getCorrespondencia().getId().compareTo(Long.valueOf(cor)) == 0){
										existe = true;
									}
								}
								if(!existe){
									corr.add(String.valueOf(archivosCorregido.get(0).getCorrespondencia().getId()));
								}
							}
						}
						
					}
					//UsuarioPetroperu usuario = obtenerUsuario();
					respuesta[1] = String.join(",", corr) + ";" + Long.valueOf(startTime);
					LOGGER.info("Respuesta 1: " + respuesta[1]);
				}
			}
			// TICKET 9000004981
			//respuesta[1] = Long.valueOf(startTime);
			// FIN TICKET
			_zip.delete();
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] procesarDocumentosFirmados", e);
			// TICKET 9000004981
			respuesta[1] = Long.valueOf(start);
			// FIN TICKET
		}
		this.LOGGER.info("[FIN] procesarDocumentosFirmados");
		return respuesta;
	}
	// FIN TICKET

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
	
	// TICKET 9000004651
	public List<ArchivoAdjunto> obtenerArchivosAFirmarGrupal(String correspondencias, Locale locale) {
		String[] ids = correspondencias.split(",");
		List<Long> idsCorrespondencias = new ArrayList<>();
		for(int i = 0; i < ids.length; i++){
			idsCorrespondencias.add(Long.valueOf(ids[i]));
		}
		List<ArchivoAdjunto> archivos = this.archivoAdjuntoDAO.obtenerArchivosAFirmarGrupal(idsCorrespondencias);
		archivos = (archivos == null) ? new ArrayList<>() : archivos;
		return archivos;
	}
	
	public String comprimirArchivosGrupal(List<ArchivoAdjunto> archivos, String nameZip, Locale locale) {
		this.LOGGER.info("[INICIO] comprimirArchivosGrupal");
		String zipeado = null;
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;

		try {
			String directorioComprimido = this.DIRECCTORIO_BASE + "comprimidos" + "/" + nameZip + ".zip";
			fos = new FileOutputStream(directorioComprimido);
			zipOut = new ZipOutputStream(fos);
			for (ArchivoAdjunto archivoAdjunto : archivos) {
				File fileToZip = new File(archivoAdjunto.getUbicacion());
				FileInputStream fis = new FileInputStream(fileToZip);
				ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
				zipOut.putNextEntry(zipEntry);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
				fis.close();
			}
			zipOut.close();
			fos.close();
			zipeado = directorioComprimido;
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] comprimirArchivosGrupal" + " This is error : " + e);
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
					this.LOGGER.info("[INFO] comprimirArchivosGrupal " + " This is info: se libero el arhivo fos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] comprimirArchivosGrupal" + " This is error : " + e);
				}
			}
			if (zipOut != null) {
				try {
					zipOut.close();
					this.LOGGER.info("[INFO] comprimirArchivosGrupal " + " This is info: se libero el arhivo zipOut");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] comprimirArchivosGrupal" + " This is error : " + e);
				}
			}
		}
		this.LOGGER.info("[FIN] comprimirArchivosGrupal");
		return zipeado;
	}
	// FIN TICKET

	private UsuarioPetroperu obtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		return null;
	}
	
}

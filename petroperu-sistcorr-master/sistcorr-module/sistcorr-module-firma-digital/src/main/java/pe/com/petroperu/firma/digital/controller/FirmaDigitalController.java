package pe.com.petroperu.firma.digital.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.firma.digital.IProcesoFirmaDigitalZip;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correlativo;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrelativoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;
import pe.com.petroperu.sistcorr.dao.IFirmarteDAO;
import pe.com.petroperu.util.Constante;

@Controller
@PropertySource({ "classpath:application.properties" })
public class FirmaDigitalController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${sistcorr.directorio}")
	private String DIRECTORIO_BASE;

	private final String DIRECTORIO_COMPRIMIDOS = "comprimidos";
	
	private final String DIRECTORIO_COMPRIMIDOS_FIRMADOS = "comprimidos_firmados";

	private final String DIRECTORIO_ADJUNTOS = "adjuntos";

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private IProcesoFirmaDigitalZip procesoFirmaDigital;

	@Autowired
	private IArchivoAdjuntoDAO archivoAdjuntoDAO;
	
	@Autowired
	private IFilenetDAO filenetDAO;

	@Autowired
	private ICorrespondenciaDAO correspondenciaDAO;
	
	@Autowired
	private IFirmarteDAO firmanteDAO;
	
	@Autowired
	private ICorrelativoDAO correlativoDAO;

	@Value("${sistcorr.simular.firma}")
	private boolean simularFirma;
	
	Long idCorrespondenciaTemporal;

	@GetMapping({ "/app/firma-digital/zipear/{idCorrespondencia}" })
	public ResponseEntity<String> generarZipeado(@PathVariable("idCorrespondencia") Long idCorrespondencia,
			Locale locale, HttpServletRequest request) {
		this.procesoFirmaDigital.comprimirArchivos(
				this.procesoFirmaDigital.obtenerArchivosAFirmar(idCorrespondencia, locale), idCorrespondencia, locale);
		String url = this.servletContext.getContextPath() + "/comprimidos/" + idCorrespondencia + ".zip";
		return new ResponseEntity(url, HttpStatus.OK);
	}

	@GetMapping({ "/app/firma-digital/{idCorrespondencia}" })
	public ModelAndView formularioFirmaDigital(@PathVariable("idCorrespondencia") Long idCorrespondencia,
			Locale locale) {
		LOGGER.info("[INICIO] formularioFirmaDigital ");
		this.idCorrespondenciaTemporal = idCorrespondencia;
		ModelAndView view = new ModelAndView("extension/firma_digital");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null) {
			return new ModelAndView("redirect:/403.html");
		}
		if (this.simularFirma) {
			return new ModelAndView("redirect:/app/emision-firma/respuesta?res=true&extra1=" + idCorrespondencia
					+ "&extra2=simulacion");
		}
		Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
		ItemFilenet listaUsuario = this.filenetDAO.obtenerFirmante(correspondencia.getCodDependencia());
		if(listaUsuario!=null){
			correspondencia.setCodRemitente(listaUsuario.getCodigo());
			correspondencia.setRemitente(listaUsuario.getDescripcion());
			LOGGER.info("Actualizacion de jefe dependencia: " + listaUsuario.getCodigo() + "||" + listaUsuario.getDescripcion());
		}
		correspondencia = this.correspondenciaDAO.save(correspondencia);
		LOGGER.info("Correspondencia actualizada: " + correspondencia.getCodRemitente() + "||" + correspondencia.getRemitente());
		Boolean autor = false;
		Boolean vb = false;
		
		if (correspondencia.getCodRemitente().equals(usuario.getUsername())) {
			view.addObject("ultimo_firmante", Boolean.valueOf(true));
			//inicio ticket 9000004765
			if(correspondencia.getCodTipoCorrespondencia() != null && correspondencia.getCodTipoCorrespondencia().trim().equalsIgnoreCase(Constante.TIPO_CORRESPONDENCIA_DOC_PAGAR_ID)) {
				view.addObject("textoUltimaFirma", "Aprobado para pago");
			}else {
				view.addObject("textoUltimaFirma", "Aprobado");
			}
			//fin ticket 9000004765
		} else {
			view.addObject("ultimo_firmante", Boolean.valueOf(false));
			if(correspondencia.isPrimerFirmante() && correspondencia.getUsuarioCrea().equals(usuario.getUsername())){
				autor = true;
			}else{
				List<Firmante> firmantes = firmanteDAO.obtenerFirmantes(idCorrespondencia);
				Boolean primeraFirma = true;
				for(int i=0;i<firmantes.size();i++){
					Firmante f = firmantes.get(i);
					if(f.getEstado().getId()==Constante.CORRESPONDENCIA_FIRMADA && f.getNroFlujo()==correspondencia.getNroFlujo()){
						primeraFirma = false;
					}
				}
				if(primeraFirma && correspondencia.getUsuarioCrea().equals(usuario.getUsername())){
					autor = true;
				}else{
					vb = true;
				}
			}
		}
		LOGGER.info("Autor:" + autor);
		LOGGER.info("VB:" + vb);
		view.addObject("autor", autor);
		view.addObject("vb", vb);
		
		view.addObject("titulo", "Confirmación de Firma");
		view.addObject("URL_ZIP", this.servletContext.getContextPath() + "/comprimidos/" + idCorrespondencia + ".zip");
		view.addObject("NOMBRE_ZIP", idCorrespondencia + ".zip");
		view.addObject("NOMBRE_ZIP", idCorrespondencia + ".zip");
		view.addObject("ID_ZIP", idCorrespondencia + ".zip");
		view.addObject("ID_CORRESPONDENCIA", idCorrespondencia);
		return view;
	}
	
	// TICKET 9000004651
	@GetMapping({ "/app/firma-digital-grupal/{nombreZip}" })
	public ModelAndView formularioFirmaDigitalGrupal(@PathVariable("nombreZip") String nombreZip,
			Locale locale) {
		LOGGER.info("[INICIO] formularioFirmaDigitalGrupal ");
		//this.idCorrespondenciaTemporal = idCorrespondencia;
		try{
			ModelAndView view = new ModelAndView("extension/firma_digital_grupal");
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null) {
				return new ModelAndView("redirect:/403.html");
			}
			if (this.simularFirma) {
				return new ModelAndView("redirect:/app/emision-firma/respuesta?res=true&extra1=" + nombreZip
						+ "&extra2=simulacion");
			}
			List<String> archivos = new ArrayList<String>();
			ZipFile zip = new ZipFile(DIRECTORIO_BASE + "/" + DIRECTORIO_COMPRIMIDOS + "/" + nombreZip + ".zip");
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				archivos.add(entry.getName());
			}
			List<ArchivoAdjunto> archivosAdjuntos = this.archivoAdjuntoDAO.obtenerArchivosPorNombreServidor(archivos);
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
				correlativos[i] = correspondencias.get(i).getCorrelativo().getCodigo();
			}
			String correlativosJuntos = String.join(",", correlativos);
			
			Long tipoFirma = this.validarNivelFirma(correlativosJuntos, locale);
			Boolean autor = false;
			Boolean vb = false;
			Boolean ultimoFirmante = false;
			if(tipoFirma.compareTo(Constante.SOY_AUTOR) == 0){
				autor = true;
			} else if(tipoFirma.compareTo(Constante.VISTO_BUENO) == 0){
				vb = true;
			} else if(tipoFirma.compareTo(Constante.ULTIMO_FIRMANTE) == 0){
				ultimoFirmante = true;
			}
			/*Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			ItemFilenet listaUsuario = this.filenetDAO.obtenerFirmante(correspondencia.getCodDependencia());
			if(listaUsuario!=null){
				correspondencia.setCodRemitente(listaUsuario.getCodigo());
				correspondencia.setRemitente(listaUsuario.getDescripcion());
				LOGGER.info("Actualizacion de jefe dependencia: " + listaUsuario.getCodigo() + "||" + listaUsuario.getDescripcion());
			}
			correspondencia = this.correspondenciaDAO.save(correspondencia);
			LOGGER.info("Correspondencia actualizada: " + correspondencia.getCodRemitente() + "||" + correspondencia.getRemitente());*/
			/*if (correspondencia.getCodRemitente().equals(usuario.getUsername())) {
				view.addObject("ultimo_firmante", Boolean.valueOf(true));
			} else {
				view.addObject("ultimo_firmante", Boolean.valueOf(false));
				if(correspondencia.isPrimerFirmante() && correspondencia.getUsuarioCrea().equals(usuario.getUsername())){
					autor = true;
				}else{
					List<Firmante> firmantes = firmanteDAO.obtenerFirmantes(idCorrespondencia);
					Boolean primeraFirma = true;
					for(int i=0;i<firmantes.size();i++){
						Firmante f = firmantes.get(i);
						if(f.getEstado().getId()==Constante.CORRESPONDENCIA_FIRMADA && f.getNroFlujo()==correspondencia.getNroFlujo()){
							primeraFirma = false;
						}
					}
					if(primeraFirma && correspondencia.getUsuarioCrea().equals(usuario.getUsername())){
						autor = true;
					}else{
						vb = true;
					}
				}
			}*/
			LOGGER.info("Autor:" + autor);
			LOGGER.info("VB:" + vb);
			view.addObject("autor", autor);
			view.addObject("vb", vb);
			view.addObject("ultimo_firmante", ultimoFirmante);
			view.addObject("titulo", "Confirmación de Firma Grupal");
			view.addObject("URL_ZIP", this.servletContext.getContextPath() + "/comprimidos/" + nombreZip + ".zip");
			view.addObject("NOMBRE_ZIP", nombreZip + ".zip");
			view.addObject("ID_ZIP", nombreZip + ".zip");
			//view.addObject("ID_CORRESPONDENCIA", idCorrespondencia);
			return view;
		} catch(Exception e) {
			LOGGER.error("[ERROR] ", e);
			return new ModelAndView("redirect:/403.html");
		}
	}
	// FIN TICKET

	@RequestMapping(value = { "/signOk" }, method = { RequestMethod.POST }, consumes = { "multipart/form-data" })
	public String signOk(HttpServletRequest request) throws IOException {
		this.LOGGER.info("[INICIO] signOk");
		this.LOGGER.info("[INICIO] idCorrespondenciaTemporal "+this.idCorrespondenciaTemporal);
		
		String pathServlet = request.getServletPath();
		String fullPathServlet = request.getRequestURL().toString();

		Object[] procesarDocumentoFirmados = null;

		byte[] dataZip = null;
		String nombreZip = "";
		try {
			this.LOGGER.info("[SIZE REQUEST] " + request.getContentLengthLong());
			MultipartHttpServletRequest multiPart = (MultipartHttpServletRequest) request;
			Set set = multiPart.getFileMap().entrySet();
			Iterator<Map.Entry> it = set.iterator();
			String pathAdjunto = "";
			while (it.hasNext()) {
				Map.Entry me = it.next();
				String fileName_ = (String) me.getKey();
				MultipartFile cmpArchivo = (MultipartFile) me.getValue();
				String[] nombreCompleto = cmpArchivo.getOriginalFilename().split("\\.");
				String finalName = cmpArchivo.getOriginalFilename().split("\\.")[0];
				for (int i = 0; i < nombreCompleto.length - 1; i++) {
					finalName = finalName + nombreCompleto[i];
				}
				String extensionFile = nombreCompleto[nombreCompleto.length - 1];
				this.LOGGER.info("ZIP response: " + finalName + "." + extensionFile);
				dataZip = cmpArchivo.getBytes();
				nombreZip = finalName + "." + extensionFile;
			}
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] signOk", e);
			return "redirect:/app/emision-firma/respuesta?res=false";
		}
		
		LOGGER.info("[INFO] nombreZip "+nombreZip);
		LOGGER.info("[INFO] dataZip "+dataZip);
		procesarDocumentoFirmados = this.procesoFirmaDigital.procesarDocumentosFirmados(nombreZip, dataZip, null);

		if (procesarDocumentoFirmados != null && procesarDocumentoFirmados[0] != null
				&& procesarDocumentoFirmados[1] != null) {
			
			// TICKET 7000003566
			//this.procesoFirmaDigital.borrarArchivosComprimir(this.idCorrespondenciaTemporal);
			
			Correspondencia correspondencia = (Correspondencia) procesarDocumentoFirmados[0];
			// TICKET 7000003566
			this.procesoFirmaDigital.borrarArchivosComprimir(correspondencia.getId());
			// FIN TICKET
			long directorio = ((Long) procesarDocumentoFirmados[1]).longValue();
			this.LOGGER.info("[FIN - OK] signOk");
			return "redirect:/app/emision-firma/respuesta?res=true&extra1=" + correspondencia.getId() + "&extra2="
					+ directorio;
		}

		this.LOGGER.info("[FIN - FALSE] signOk");
		// TICKET 9000004981
		File carpeta = new File(this.DIRECTORIO_BASE + DIRECTORIO_COMPRIMIDOS_FIRMADOS + "/" + procesarDocumentoFirmados[1]);
		this.LOGGER.info("Ruta carpeta error: " + this.DIRECTORIO_BASE + DIRECTORIO_COMPRIMIDOS_FIRMADOS + "/" + procesarDocumentoFirmados[1]);
		try{
			if(carpeta.exists()){
				carpeta.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		// FIN TICKET
		return "redirect:/app/emision-firma/respuesta?res=false&extra1=0&extra2="+procesarDocumentoFirmados[1];
	}
	
	@RequestMapping(value = { "/signOkGrupal" }, method = { RequestMethod.POST }, consumes = { "multipart/form-data" })
	public String signOkGrupal(HttpServletRequest request) throws IOException {
		this.LOGGER.info("[INICIO] signOkGrupal");
		
		String pathServlet = request.getServletPath();
		String fullPathServlet = request.getRequestURL().toString();
		String namePath = String.valueOf(request.getAttribute("fileName"));
		LOGGER.info("namePath: " + namePath);

		Object[] procesarDocumentoFirmados = null;

		byte[] dataZip = null;
		String nombreZip = "";
		try {
			this.LOGGER.info("[SIZE REQUEST] " + request.getContentLengthLong());
			MultipartHttpServletRequest multiPart = (MultipartHttpServletRequest) request;
			Set set = multiPart.getFileMap().entrySet();
			Iterator<Map.Entry> it = set.iterator();
			String pathAdjunto = "";
			while (it.hasNext()) {
				Map.Entry me = it.next();
				String fileName_ = (String) me.getKey();
				MultipartFile cmpArchivo = (MultipartFile) me.getValue();
				String[] nombreCompleto = cmpArchivo.getOriginalFilename().split("\\.");
				String finalName = cmpArchivo.getOriginalFilename().split("\\.")[0];
				for (int i = 0; i < nombreCompleto.length - 1; i++) {
					finalName = finalName + nombreCompleto[i];
				}
				String extensionFile = nombreCompleto[nombreCompleto.length - 1];
				this.LOGGER.info("ZIP response: " + finalName + "." + extensionFile);
				dataZip = cmpArchivo.getBytes();
				nombreZip = finalName + "." + extensionFile;
			}
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] signOkGrupal", e);
			return "redirect:/app/emision-firma/respuesta-grupal?res=false";
		}
		
		LOGGER.info("[INFO] nombreZip "+nombreZip);
		LOGGER.info("[INFO] dataZip "+dataZip);
		procesarDocumentoFirmados = this.procesoFirmaDigital.procesarDocumentosFirmadosGrupal(nombreZip, dataZip, null);

		if (procesarDocumentoFirmados != null && procesarDocumentoFirmados[0] != null
				&& procesarDocumentoFirmados[1] != null) {
			
//			UsuarioPetroperu usuario = obtenerUsuario();
//			this.procesoFirmaDigital.borrarArchivosComprimirGrupal(procesarDocumentoFirmados[0].toString(), usuario.getUsername());
			long directorio = ((Long) procesarDocumentoFirmados[1]).longValue();
			this.LOGGER.info("[FIN - OK] signOk");
			return "redirect:/app/emision-firma/respuesta-grupal?res=true&extra1=" + procesarDocumentoFirmados[0] + "&extra2="
					+ directorio;
		}

		this.LOGGER.info("[FIN - FALSE] signOkGrupal");
		return "redirect:/app/emision-firma/respuesta-grupal?res=false&extra1=0&extra2="+procesarDocumentoFirmados[1];
	}
	
	// TICKET 9000004651
	@GetMapping({ "/app/firma-digital/zipear-grupal/{correspondencias}" })
	public ResponseEntity<Respuesta<String>> generarZipeadoGrupal(@PathVariable("correspondencias") String correspondencias,
			Locale locale, HttpServletRequest request) {
		LOGGER.info("[INICIO] generarZipeadoGrupal " + correspondencias);
		Respuesta<String> respuesta = new Respuesta<>();
		try{
			List<ArchivoAdjunto> archivos = this.procesoFirmaDigital.obtenerArchivosAFirmarGrupal(correspondencias, locale);
			UsuarioPetroperu usuario = obtenerUsuario();
			Long temp = 0L;
			List<Correspondencia> correspondenciasArc = new ArrayList<>();
			for(int i = 0; i < archivos.size(); i++){
				ArchivoAdjunto arcAdj = archivos.get(i);
				boolean existe = false;
				for(Correspondencia corr : correspondenciasArc){
					if(arcAdj.getCorrespondencia().getId().compareTo(corr.getId()) == 0){
						existe = true;
					}
				}
				if(!existe){
					correspondenciasArc.add(arcAdj.getCorrespondencia());
				}
			}
			for(Correspondencia corresp : correspondenciasArc){
				temp = temp + corresp.getId();
			}
			String nombreZip = usuario.getUsername() + "-" + temp;
			this.procesoFirmaDigital.comprimirArchivosGrupal(archivos, nombreZip, locale);
			respuesta.estado = true;
			respuesta.mensaje = "Archivos comprimidos correctamente";
			respuesta.datos.add(nombreZip);
		}catch(Exception e){
			LOGGER.error("[ERROR]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al comprimir los archivos";
		}
		LOGGER.info("[FIN] generarZipeadoGrupal");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET
	
	// TICKET 9000004651
	public Long validarNivelFirma(String correlativos, Locale locale){
		Long tipoFirma = 0L;
		try{
			String[] corrs = correlativos.split(",");
			for(int i=0;i<corrs.length;i++){
				Correlativo correlativo = correlativoDAO.findOneByCodigo(corrs[i]);
				Correspondencia correspondencia = correspondenciaDAO.findOneByCorrelativo(correlativo);
				Long tFirma = this.obtenerTipoFirma(correspondencia);
				if(tipoFirma.compareTo(0L) == 0){
					if(tFirma.compareTo(0L) == 0){
						break;
					}else{
						tipoFirma = tFirma;
					}
				}else{
					if(tipoFirma.compareTo(tFirma) != 0){
						tipoFirma = 0L;
						break;
					}
				}
			}
		} catch(Exception e) {
			LOGGER.error("[ERROR] validarNivelFirma", e);
			tipoFirma = 0L;
		}
		return tipoFirma;
	}
	
	public Long obtenerTipoFirma(Correspondencia correspondencia){
		Long tipoFirma = 0L;
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			if(correspondencia.getCodRemitente().toUpperCase().equalsIgnoreCase(usuario.getUsername().toUpperCase())){
				tipoFirma = Constante.ULTIMO_FIRMANTE;
			}else{
				List<Firmante> firmantes = firmanteDAO.obtenerFirmantes(correspondencia.getId());
				Boolean primeraFirma = true;
				for(int i=0;i<firmantes.size();i++){
					Firmante f = firmantes.get(i);
					if(f.getEstado().getId()==Constante.CORRESPONDENCIA_FIRMADA && f.getNroFlujo()==correspondencia.getNroFlujo()){
						primeraFirma = false;
					}
				}
				if(primeraFirma && correspondencia.getUsuarioCrea().equals(usuario.getUsername())){
					tipoFirma = Constante.SOY_AUTOR;
				}else{
					tipoFirma = Constante.VISTO_BUENO;
				}
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] obtenerTipoFirma", e);
			tipoFirma = 0L;
		}
		return tipoFirma;
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

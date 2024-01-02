package pe.com.petroperu.service.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.cliente.model.Bandeja;
import pe.com.petroperu.cliente.model.ListaFiltroCorrespondencia;
import pe.com.petroperu.cliente.model.emision.FiltroConsultaHistorial;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.filenet.dao.FilenetDAOImpl;
import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.filenet.model.ItemTipoCorrespondencia;
import pe.com.petroperu.model.CorrespondenciaSimple;
import pe.com.petroperu.model.CorrespondenciaTareaPaginado;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.UsuarioRemplazo;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.DatosFirmante;
import pe.com.petroperu.model.emision.DestinatarioCopia;
import pe.com.petroperu.model.emision.DestinatarioExterno;
import pe.com.petroperu.model.emision.DestinatarioInterno;
import pe.com.petroperu.model.emision.DestinatarioRespuesta;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.Parametro;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaDTO;
import pe.com.petroperu.model.emision.dto.DatosFirmanteDTO;
import pe.com.petroperu.model.util.SistcorrEstado;
import pe.com.petroperu.service.ICorrespondenciaEmisionService;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.IEmisionService;
import pe.com.petroperu.service.IFilenetService;
import pe.com.petroperu.service.IFirmanteService;
import pe.com.petroperu.service.dto.ArchivoAdjuntoDTO;
import pe.com.petroperu.service.dto.FiltroBandeja;
import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.service.impl.EmisionServiceImpl;
import pe.com.petroperu.service.util.IReport;
import pe.com.petroperu.service.util.ReportExcel;
import pe.com.petroperu.service.util.ReportExcelCABandejaEntrada;
import pe.com.petroperu.service.util.ReportExcelCBandejaSalida;
import pe.com.petroperu.service.util.ReporteExcelHistorial;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;
import pe.com.petroperu.sistcorr.dao.IDatosFirmanteDAO;
import pe.com.petroperu.sistcorr.dao.IFirmarteDAO;
import pe.com.petroperu.sistcorr.dao.IHistorialArchivoDAO;
import pe.com.petroperu.sistcorr.dao.IParametroDAO;
import pe.com.petroperu.sistcorr.dao.IRutaAprobacionDAO;
import pe.com.petroperu.util.Constante;

//adicion 9-3874
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

@Service
@PropertySource({ "classpath:application.properties" })
public class EmisionServiceImpl implements IEmisionService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ICorrespondenciaEmisionService correspondenciaService;
	
	@Autowired
	private ICorrespondenciaService correspondenciaServiceAdj;

	@Autowired
	private IFilenetService filenetService;

	@Value("${sistcorr.directorio}")
	private String DIRECCTORIO_BASE;

	@Value("${sistcorr.simular.firma}")
	private boolean simularFirma;

	@Autowired
	private IFirmarteDAO firmanteDAO;
	
	@Autowired
	private IDatosFirmanteDAO datosFirmanteDAO;//ticket 90004714

	private final String DIRECTORIO_ADJUNTOS = "adjuntos";

	private final String DIRECTORIO_COMPRIMIDOS_FIRMADOS = "comprimidos_firmados";

	private final String DIRECTORIO_HISTORICOS = "historico";

	@Autowired
	private MessageSource messageSource;
	
	// TICKET 9000003791
	@Autowired
	private IHistorialArchivoDAO historialArchivoDAO;
	
	@Autowired
	private ICorrespondenciaDAO correspondenciaDAO; // adicion 9-3874
	
	@Autowired
	private IRutaAprobacionDAO rutaAprobacionDAO;
	
	@Autowired
	private ISistcorrCliente sistcorrCliente;
	
	@Autowired
	private IParametroDAO parametroDAO;

	@Autowired
	private IFirmanteService firmanteService;
	
	public Respuesta<Correspondencia> registrar(@RequestBody Correspondencia correspondencia,
			List<ArchivoAdjuntoDTO> adjuntos, String usuarioCrea, String nombreCompletoUsuarioCrea, List<RutaAprobacion> aprobadores, Locale locale) {
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {
			List<ArchivoAdjunto> _adjuntos = new ArrayList<>();
			for (ArchivoAdjuntoDTO adjunto : adjuntos) {
				long startTime = System.nanoTime();
				String[] nombreCompleto = adjunto.getFile().getOriginalFilename().split("\\.");
				String extension = nombreCompleto[nombreCompleto.length - 1];
				if(copiarArchivo(DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS, startTime + "." + extension, adjunto.getFile().getBytes())) {
					String pathFile = DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS + "/" + startTime + "." + extension;
					ArchivoAdjunto archivo = new ArchivoAdjunto();
					archivo.setExtension(extension);
					archivo.setNombre(adjunto.getFile().getOriginalFilename());
					archivo.setNombreServidor(startTime + "." + extension);
					archivo.setPrincipal(adjunto.isPrincipal());
					archivo.setUbicacion(pathFile);
					archivo.setContentType(adjunto.getFile().getContentType());
					archivo.setTamanio(Double.valueOf(adjunto.getFile().getSize() / Math.pow(1024.0D, 2.0D)));
					// TICKET 9000004510
					archivo.setIndicadorRemoto(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO);
					// FIN TICKET
					_adjuntos.add(archivo);
					continue;
				}
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrar_archivo_adjunto.error", null, locale));
			}
			
			// Validacion para requerir destinatario y copia  adicion 9-3874 
			
			List<ItemTipoCorrespondencia> listaTip = new ArrayList();
	
			//listaTip = (List<ItemTipoCorrespondencia>)filenetDAO.listarTiposCorresponciaEmision("Requiere");
			listaTip = (List<ItemTipoCorrespondencia>)filenetService.listarTiposCorresponciaEmision("Requiere"); 
		
			Boolean reqCopia =  false;
			String reqDestin = "";
			String idTipoCorr = "";
			for (ItemTipoCorrespondencia row : listaTip) {
				
				idTipoCorr = ((row.getCodigo() == null) ? "" : row.getCodigo().toString());
	
				if (idTipoCorr.toString().equals(correspondencia.getCodTipoCorrespondencia()) ){
					reqDestin = ((row.getDestinatario()==null) ? "" : row.getDestinatario().toString());
					reqCopia =  row.isCopia(); 
					break;
				}
			}
			
			Boolean tdest = false;
			Boolean tcop = false;
			/*for (int i = 0; i < correspondencia.getDetalleInterno().size(); i++) {
				tdest = true;
			}

			for (int i = 0; i < correspondencia.getDetalleExterno().size(); i++) {
				tdest = false;
			}		*/	

			for (int i = 0; i < correspondencia.getDetalleCopia().size(); i++) {
				tcop = true;
			}			
			
			/*if (reqDestin.equals("1") && !tdest) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.requiere_destinatario", null, locale));				
			}*/
			
			if ((reqDestin.equals("0") || reqDestin.equals(""))  && tdest) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.rechaza_destinatario", null, locale));				
			}	
			
			if ( !reqCopia && tcop) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.rechaza_copia", null, locale));				
			}				
			
			/*if ( reqCopia && !tcop) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.requiere_copia", null, locale));				
			}*/			
			
			//fin adicion 9-3874

			correspondencia.setAdjuntos(_adjuntos);
			respuesta = this.correspondenciaService.registrar(correspondencia, usuarioCrea, nombreCompletoUsuarioCrea, aprobadores,  locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] registrar", e);
		}
		return respuesta;
	}
	

	public Respuesta<Correspondencia> modificar(@RequestBody Correspondencia correspondencia,
			List<ArchivoAdjuntoDTO> adjuntos, String usuarioCrea, List<RutaAprobacion> aprobadores, Locale locale) {
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		Boolean fPrinc = false; //adicion 9-3874
		try {
			List<ArchivoAdjunto> _adjuntos = new ArrayList<>();
			for (ArchivoAdjuntoDTO adjunto : adjuntos) {
				long startTime = System.nanoTime();
				String[] nombreCompleto = adjunto.getFile().getOriginalFilename().split("\\.");
				String extension = nombreCompleto[nombreCompleto.length - 1];
				if(copiarArchivo(DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS, startTime + "." + extension, adjunto.getFile().getBytes())) {
					String pathFile = DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS + "/" + startTime + "." + extension;
					ArchivoAdjunto archivo = new ArchivoAdjunto();
					archivo.setExtension(extension);
					archivo.setNombre(adjunto.getFile().getOriginalFilename());
					archivo.setNombreServidor(startTime + "." + extension);
					archivo.setPrincipal(adjunto.isPrincipal());
					archivo.setUbicacion(pathFile);
					archivo.setContentType(adjunto.getFile().getContentType());
					archivo.setTamanio(Double.valueOf(adjunto.getFile().getSize() * 1.0E-6D));
					archivo.setId(Long.valueOf(0L));
					// TICKET 9000004510
					archivo.setIndicadorRemoto(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO);
					// FIN TICKET
					_adjuntos.add(archivo);
					
					if (adjunto.isPrincipal()){
						fPrinc = true;   //adicion 9-3874
					}
					continue;
				}
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrar_archivo_adjunto.error", null, locale));
			}
			
			//adicion 9-3874
     		Correspondencia correspondenciaAnterior = (Correspondencia) this.correspondenciaDAO.findOne(correspondencia.getId());
     		List<ArchivoAdjunto> adjuntosPreviosPre = correspondenciaAnterior.getAdjuntos();
     		List<ArchivoAdjunto> arcPrevPre = new ArrayList<>();
     		for(ArchivoAdjunto adjPrevio : adjuntosPreviosPre){
				//LOGGER.info("Archivo previo pre:" + adjPrevio.getNombre() + "||" + adjPrevio.getId());
				ArchivoAdjunto _aa_ = new ArchivoAdjunto();
				_aa_.setId(adjPrevio.getId());
				_aa_.setNombreServidor(adjPrevio.getNombreServidor());
				_aa_.setIndicadorRemoto(adjPrevio.getIndicadorRemoto());
				arcPrevPre.add(_aa_);
     		}
			if (correspondenciaAnterior.isFirmaDigital() != correspondencia.isFirmaDigital() ) {
				if (correspondencia.isFirmaDigital()) {
					if (correspondencia.getEstado().getId() != Constante.CORRESPONDENCIA_SIN_ASIGNAR &&
					correspondencia.getEstado().getId() != Constante.CORRESPONDENCIA_SIN_FIRMA_MANUAL &&
					correspondencia.getEstado().getId() != Constante.CORRESPONDENCIA_SIN_DOCUMENTOS ) {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.modificar_correspondencia.digital", null, locale));
					} 
						
				} else {
					if ((correspondencia.getEstado().getId() != Constante.CORRESPONDENCIA_SIN_ASIGNAR 
							&&	correspondencia.getEstado().getId() != Constante.CORRESPONDENCIA_SIN_FIRMA_MANUAL 
							&& correspondencia.getEstado().getId() != Constante.CORRESPONDENCIA_SIN_DOCUMENTOS	)						
							|| fPrinc ) {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.modificar_correspondencia.manual", null, locale));
					} 					
				}
			}

			
			// Validacion para requerir destinatario y copia  adicion 9-3874 
			
			List<ItemTipoCorrespondencia> listaTip = new ArrayList();
	
			//listaTip = (List<ItemTipoCorrespondencia>)filenetDAO.listarTiposCorresponciaEmision("Requiere");
			listaTip = (List<ItemTipoCorrespondencia>)filenetService.listarTiposCorresponciaEmision("Requiere"); 
		
			Boolean reqCopia =  false;
			String reqDestin = "";
			String idTipoCorr = "";
			for (ItemTipoCorrespondencia row : listaTip) {
				
				idTipoCorr = ((row.getCodigo() == null) ? "" : row.getCodigo().toString());
	
				if (idTipoCorr.toString().equals(correspondencia.getCodTipoCorrespondencia()) ){
					reqDestin = ((row.getDestinatario()==null) ? "" : row.getDestinatario().toString());
					reqCopia =  row.isCopia(); 
					break;
				}
			}
			
			Boolean tdest = false;
			Boolean tcop = false;
			/*for (int i = 0; i < correspondencia.getDetalleInterno().size(); i++) {
				tdest = true;
			}

			for (int i = 0; i < correspondencia.getDetalleExterno().size(); i++) {
				tdest = false;
			}			

			for (int i = 0; i < correspondencia.getDetalleCopia().size(); i++) {
				tcop = true;
			}	*/		
			
			/*if (reqDestin.equals("1") && !tdest) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.requiere_destinatario", null, locale));				
			}
			
			if ((reqDestin.equals("0") || reqDestin.equals(""))  && tdest) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.rechaza_destinatario", null, locale));				
			}	
			
			if ( !reqCopia && tcop) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.rechaza_copia", null, locale));				
			}	
			
			if ( reqCopia && !tcop) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.requiere_copia", null, locale));				
			}*/
			
			
			//fin adicion 9-3874
			// Ticket 9000004409
			List<UsuarioRemplazo> usuarioRemplazo = this.filenetService.obtenerUsuarioRemplazo(usuarioCrea.toString());		
			// FIN TICKET
			correspondencia.getAdjuntos().addAll(_adjuntos);
			respuesta = this.correspondenciaService.modificar(correspondencia, usuarioCrea, aprobadores, locale, usuarioRemplazo);
			List<ArchivoAdjunto> adjuntosPrevios = correspondenciaAnterior.getAdjuntos();
			List<ArchivoAdjunto> adjuntosActuales = respuesta.datos.get(0).getAdjuntos();
			for(ArchivoAdjunto adjPrevio : arcPrevPre){
				//LOGGER.info("Archivo previo:" + adjPrevio.getId());
				boolean encontrado = false;
				for(ArchivoAdjunto adjActual : adjuntosActuales){
					//LOGGER.info("Archivo actual:" + adjActual.getId());
					if(adjPrevio.getId().compareTo(adjActual.getId()) == 0){
						encontrado = true;
					}
				}
				if(!encontrado){
					LOGGER.info("Eliminando:" + adjPrevio.getId() + "||" + adjPrevio.getIndicadorRemoto());
					if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(adjPrevio.getIndicadorRemoto())){
						String urlBase = DIRECCTORIO_BASE;
						String urlCarpetaArchivos = "adjuntos";
						String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + adjPrevio.getNombreServidor();
						File temp = new File(nuevaUrl);
						temp.delete();
					}
					if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(adjPrevio.getIndicadorRemoto())){
						UsuarioPetroperu usuario = obtenerUsuario();
						sistcorrCliente.eliminarDocumentoServidor(usuario.getToken(), adjPrevio.getNombreServidor());
					}
				}
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] registrar", e);
		}
		return respuesta;
	}


	public Respuesta<ArchivoAdjunto> registar(Long idCorrespondencia, ArchivoAdjuntoDTO adjunto, String usuarioCrea,
			Locale locale) {
		Respuesta<ArchivoAdjunto> respuesta = new Respuesta<>();;
		try {
			long startTime = System.nanoTime();
			Correspondencia corr = new Correspondencia();
			corr.setId(idCorrespondencia);
			String[] nombreCompleto = adjunto.getFile().getOriginalFilename().split("\\.");
			String extension = nombreCompleto[nombreCompleto.length - 1];
			if(copiarArchivo(DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS, startTime + "." + extension, adjunto.getFile().getBytes())) {
				String pathFile = DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS + "/" + startTime + "." + extension;
				ArchivoAdjunto archivo = new ArchivoAdjunto();
				archivo.setExtension(extension);
				archivo.setNombre(adjunto.getFile().getOriginalFilename());
				archivo.setNombreServidor(startTime + "." + extension);
				archivo.setPrincipal(adjunto.isPrincipal());
				archivo.setUbicacion(pathFile);
				archivo.setContentType(adjunto.getFile().getContentType());
				archivo.setTamanio(Double.valueOf(adjunto.getFile().getSize() * 1.0E-6D));
				archivo.setCorrespondencia(corr);
				respuesta = this.correspondenciaService.registrar(archivo, usuarioCrea, locale);
			} else {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrar_archivo_adjunto.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] registrar", e);
		}
		return respuesta;
	}
	
	public Respuesta<RespuestaCargaAdjunto> registrarArchivoAdjunto(pe.com.petroperu.model.Correspondencia correspondencia, ArchivoAdjuntoDTO adjunto, UsuarioPetroperu usuario,
			Locale locale) {
		Respuesta<RespuestaCargaAdjunto> respuesta = new Respuesta<>();;
		// TICKET 9000004024 - POST
		String rutaDelete = "";
		// FIN TICKET
		try {
			long startTime = System.nanoTime();
			String[] nombreCompleto = adjunto.getFile().getOriginalFilename().split("\\.");
			String extension = nombreCompleto[nombreCompleto.length - 1];
			if(copiarArchivo(DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS, startTime + "." + extension, adjunto.getFile().getBytes())) {
				String pathFile = DIRECCTORIO_BASE + DIRECTORIO_ADJUNTOS + "/" + startTime + "." + extension;
				ArchivoAdjunto archivo = new ArchivoAdjunto();
				archivo.setExtension(extension);
				archivo.setNombre(adjunto.getFile().getOriginalFilename());
				archivo.setNombreServidor(startTime + "." + extension);
				archivo.setPrincipal(adjunto.isPrincipal());
				archivo.setUbicacion(pathFile);
				archivo.setContentType(adjunto.getFile().getContentType());
				archivo.setTamanio(Double.valueOf(adjunto.getFile().getSize() * 1.0E-6D));
				//respuesta  = this.sist
				// TICKET 9000004024
				rutaDelete = pathFile;
				// FIN TICKET
				//respuesta = this.correspondenciaService.registrar(archivo, usuarioCrea, locale);
				respuesta = this.correspondenciaServiceAdj.cargarArchivoAdjunto(usuario.getToken(), usuario.getUsername(), correspondencia, correspondencia.getCorrelativo(), locale, archivo);
			} else {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrar_archivo_adjunto.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] registrar", e);
		}
		// TICKET 9000004024 - POST
		try{
			LOGGER.info("Ruta Delete:" + rutaDelete);
			if(!rutaDelete.equals("")){
				File archivoDelete = new File(rutaDelete);
				if (archivoDelete.exists()) {
					LOGGER.info("Eliminando archivo");
					archivoDelete.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		// FIN TICKET
		return respuesta;
	}

	public Respuesta<Correspondencia> buscar(Long idCorrespondencia, Locale locale) {
		Respuesta<Correspondencia> respuesta = new Respuesta<>();;
		respuesta = this.correspondenciaService.buscarCorrespondencia(idCorrespondencia, locale);
		return respuesta;
	}

	public Respuesta<CorrespondenciaDTO> pendientes(FiltroBandeja filtro, Locale locale) {
		Respuesta<CorrespondenciaDTO> respuesta = new Respuesta<>();;
		respuesta = this.correspondenciaService.correspondenciasPendientes("pendiente", filtro.getUsuario(),
				filtro.getCorrelativo(), filtro.getAsunto(), false, filtro.isDeclinados(), filtro.getFechaInicio(),
				filtro.getFechaFin(), filtro.getDependenciaOriginadora(), filtro.getTipoCorrespondencia() ,locale);//ticket 9000003866 add dorig, tcorre
		return respuesta;
	}

	public Respuesta<CorrespondenciaDTO> firmados(FiltroBandeja filtro, Locale locale) {
		Respuesta<CorrespondenciaDTO> respuesta = new Respuesta<>();;
		/*respuesta = this.correspondenciaService.correspondencias("firmado", filtro.getUsuario(),
				filtro.getCorrelativo(), filtro.getAsunto(), filtro.isRechazados(), filtro.isDeclinados(),
				filtro.getFechaInicio(), filtro.getFechaFin(), locale);*/
		
	   	// TICKET 9000004409
		UsuarioPetroperu usuario = obtenerUsuario();
		List<UsuarioRemplazo> usuarioRemplazo = new  ArrayList<>();
		
		if (usuario.getUsername().equals(filtro.getUsuario())){
			String vRemplazo = this.filenetService.validarRemplazoVigenteUsuario(filtro.getUsuario());
			
			if(vRemplazo.equals("SI")) {
				respuesta.estado = false;
				//respuesta.mensaje = this.messageSource.getMessage("sistcorr.bandeja.error", null, locale);
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.emision_reemplazo_activo", null, locale);
				return respuesta;
			}
			 usuarioRemplazo = this.filenetService.obtenerUsuarioRemplazo(filtro.getUsuario()); 
		}
	   	// FIN TICKE
		
		respuesta = this.correspondenciaService.correspondencias("firmado", filtro.getUsuario(),
				filtro.getCorrelativo(), filtro.getAsunto(), filtro.isRechazados(), filtro.isMisPendientes(), 
				filtro.isDeclinados(), filtro.getFechaInicio(), filtro.getFechaFin(), usuarioRemplazo, locale);
		return respuesta;
	}

	public Respuesta<CorrespondenciaDTO> enviados(FiltroBandeja filtro, Locale locale) {
		Respuesta<CorrespondenciaDTO> respuesta = new Respuesta<>();;
		respuesta = this.correspondenciaService.correspondencias("enviado", filtro.getUsuario(),
				filtro.getCorrelativo(), filtro.getAsunto(), false, false, filtro.getFechaInicio(),
				filtro.getFechaFin(), locale);
		return respuesta;
	}

	public Object[] obtenerArchivAdjunto(Long idArchivoAdjunto, Locale locale) {
		Object[] data = null;
		Respuesta<ArchivoAdjunto> respuesta = this.correspondenciaService.buscarArchivoAdjunto(idArchivoAdjunto,
				locale);
		if (respuesta.estado == true) {
			data = new Object[3];
			File archivo = new File(((ArchivoAdjunto) respuesta.datos.get(0)).getUbicacion());
			FileInputStream fis = null;
			byte[] bArray = new byte[(int) archivo.length()];
			try {
				fis = new FileInputStream(archivo);
				fis.read(bArray);
				fis.close();
			} catch (Exception e) {
				// TICKET 9000003992
				this.LOGGER.error("[ERROR] obtenerArchivAdjunto" + " This is error : " + e);
			}
			// TICKET 9000003992
			finally {
				if (fis != null) {
					try {
						fis.close();
						this.LOGGER.info("[INFO] obtenerArchivAdjunto " + " This is info: se libero el arhivo fis");
					} catch (IOException e) {
						this.LOGGER.error("[ERROR] obtenerArchivAdjunto" + " This is error : " + e);
					}
				}
			}
			data[0] = bArray;
			data[1] = ((ArchivoAdjunto) respuesta.datos.get(0)).getContentType();
			data[2] = ((ArchivoAdjunto) respuesta.datos.get(0)).getNombre();
		} else {
			data = new Object[1];
			data[0] = respuesta.mensaje;
		}
		return data;
	}

	private boolean copiarArchivo(String directorioBase, String nombreArchivo, byte[] data) {
		File directorio = new File(directorioBase);
		if (!directorio.exists())
			return false;
		String pathFile = directorioBase + "/" + nombreArchivo;
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(pathFile));
			stream.write(data);
			stream.close();
			// TICKET 9000003791
			
			// TICKET 9000003791
			return true;
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] copiarArchivo", e);
			return false;
		}
	}

	public Respuesta<Firmante> asignarFirmanteAutomatico(Long idCorrespondencia, String usuarioCrea, Locale locale) {
		Respuesta<Firmante> respuesta = new Respuesta<>();;
		try {
			Respuesta<Correspondencia> resCorrespondencia = this.correspondenciaService
					.buscarCorrespondencia(idCorrespondencia, locale);
			if (resCorrespondencia.estado == true) {
				Correspondencia correspondencia = resCorrespondencia.datos.get(0);
				// TICKET 9000003908
				respuesta = this.correspondenciaService.agregarFirmante(correspondencia.getId(), usuarioCrea,
						correspondencia.getCodRemitente(), correspondencia.getRemitente(),
						correspondencia.getCodDependencia(), correspondencia.getDependencia(),
						correspondencia.getCodLugarTrabajo(), correspondencia.getLugarTrabajo(), usuarioCrea, correspondencia.getNroFlujo(), locale);
			} else {

				throw new Exception(this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	public Respuesta<Firmante> asignarFirmanteRutaAprobacion(Long idCorrespondencia, String usuarioCrea, Locale locale) {
		Respuesta<Firmante> respuesta = new Respuesta<>();;
		try {
			Respuesta<Correspondencia> resCorrespondencia = this.correspondenciaService
					.buscarCorrespondencia(idCorrespondencia, locale);
			List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(resCorrespondencia.datos.get(0)).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
			int indice = -1;
			boolean encontrado = false;
			for(int i=0;i<aprobadores.size();i++){
				if(aprobadores.get(i).getFirmante() == null && encontrado == false){
					indice = i;
					encontrado = true;
				}
			}
			if (resCorrespondencia.estado == true && aprobadores.size()>0 && indice >= 0) {
				Correspondencia correspondencia = resCorrespondencia.datos.get(0);
				RutaAprobacion aprobadorBase = aprobadores.get(indice);
				String codDep = aprobadores.get(indice).getCodDependencia();
				String nomDep = aprobadores.get(indice).getDependenciaNombre();
				if("1".equalsIgnoreCase(aprobadorBase.getTipoFirmante())){
					ItemFilenet funcionario = this.filenetService.obtenerFirmante(aprobadorBase.getCodDependencia());
					if(funcionario!=null){
						//aprobadorBase.setUsuario(funcionario.getCodigo());
						//aprobadorBase.setUsuarioNombre(funcionario.getDescripcion());
					}
				}
				if("2".equalsIgnoreCase(aprobadorBase.getTipoFirmante())){
					List<ItemFilenet> lista = this.filenetService.obtenerDependenciaPorUsuario(aprobadorBase.getUsuario(), "");
					if(lista.size()>0){
						codDep = lista.get(0).getCodigo();
						nomDep = lista.get(0).getDescripcion();
					}
				}
				ItemFilenet lugar = filenetService.obtenerLugarPorDependencia(codDep);
				if("".equalsIgnoreCase(aprobadorBase.getUsuario())){
					throw new Exception(this.messageSource.getMessage("sistcorr.usuario_ruta_aprobacion.error", null, locale));
				}else{
					respuesta = this.correspondenciaService.agregarFirmante(correspondencia.getId(), usuarioCrea,
							aprobadores.get(indice).getUsuario(), aprobadores.get(indice).getUsuarioNombre(),
							codDep, nomDep,
							lugar.getCodigo(), lugar.getDescripcion(), usuarioCrea, correspondencia.getNroFlujo(), locale);
					aprobadores.get(indice).setFirmante(respuesta.datos.get(0));
					aprobadores.get(indice).setUsuario(aprobadores.get(indice).getUsuario().toLowerCase());
					LOGGER.info("EmisionServiceImpl Linea 669");
					rutaAprobacionDAO.save(aprobadores.get(indice));
				}
			} else {
				throw new Exception(this.messageSource.getMessage("sistcorr.asignar_ruta_aprobacion.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	public Respuesta<Firmante> asignarFirmante(Long idCorrespondencia, Firmante firmante, String usuarioCrea,
			Locale locale) {
		Respuesta<Firmante> respuesta = new Respuesta<>();;
		try {
			Respuesta<Correspondencia> resCorrespondencia = this.correspondenciaService.buscarCorrespondencia(idCorrespondencia, locale);
			if (resCorrespondencia.estado == true) {
				Correspondencia correspondencia = resCorrespondencia.datos.get(0);
				this.LOGGER.info("[asignarFirmante]" + correspondencia.getCorrelativo().getCodigo());
				// TICKET 9000003908 - Agregar campo nro flujo
				if(resCorrespondencia.datos.get(0).isRutaAprobacion()){
					respuesta = this.correspondenciaService.agregarFirmanteVistosPrevios(correspondencia.getId(), usuarioCrea,
							firmante.getCodFirmante(), firmante.getNombreFirmante(), firmante.getCodDependenciaFirmante(),
							firmante.getDependenciaFirmante(), firmante.getCodLugarTrabajoFirmante(),
							firmante.getLugarTrabajoFirmante(), usuarioCrea, firmante.getNroFlujo(), locale);
				}else{
					respuesta = this.correspondenciaService.agregarFirmante(correspondencia.getId(), usuarioCrea,
							firmante.getCodFirmante(), firmante.getNombreFirmante(), firmante.getCodDependenciaFirmante(),
							firmante.getDependenciaFirmante(), firmante.getCodLugarTrabajoFirmante(),
							firmante.getLugarTrabajoFirmante(), usuarioCrea, firmante.getNroFlujo(), locale);
				}
			} else {

				throw new Exception(this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] asignarFirmante", e);
		}
		return respuesta;
	}

	public Respuesta<Firmante> obtenerFirmantes(Long idCorrespondencia, Locale locale) {
		Respuesta<Firmante> respuesta = new Respuesta<>();;
		respuesta = this.correspondenciaService.obtenerFirmantes(idCorrespondencia, locale);
		return respuesta;
	}
	
	// TICKET 9000003997
	public Respuesta<DestinatarioRespuesta> obtenerDestinatarioRespuesta(Long idCorrespondencia, int nroEnvio, Locale locale) {
		Respuesta<DestinatarioRespuesta> respuesta = new Respuesta<>();;
		respuesta = this.correspondenciaService.obtenerDestinatarioRespuesta(idCorrespondencia, nroEnvio, locale);
		return respuesta;
	}
	// FIN TICKET

	public Respuesta validarFirmante(Long idCorrespondencia, Firmante firmante, String usuarioCrea, Locale locale) {
		Respuesta<Firmante> respuesta = new Respuesta<>();;
		try {
			Respuesta<Correspondencia> res = this.correspondenciaService.buscarCorrespondencia(idCorrespondencia,
					locale);
			if (res.estado == true) {
				Correspondencia correspondencia = res.datos.get(0);
				if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ASIGNADA)) {
					throw new Exception(this.messageSource.getMessage("sistcorr.asignar_firma.asignado", null, locale));
				}
			}
			respuesta = this.correspondenciaService.obtenerFirmantes(idCorrespondencia, locale);
			boolean valido = true;
			for (Firmante f : respuesta.datos) {
				if (f.getCodFirmante().equals(firmante.getCodFirmante()) 
						&& f.getEstado().getId().equals(Constante.CORRESPONDENCIA_FIRMADA)
						&& (f.getNroFlujo() == firmante.getNroFlujo())) {
					valido = false;
				}
			}
			if (!valido) {
				Object[] parametros = { firmante.getNombreFirmante() };
				String mensaje = MessageFormat.format(
						this.messageSource.getMessage("sistcorr.asignar_firma.asignado_antes", null, locale),
						parametros);
				throw new Exception(mensaje);
			}
			Object[] parametros = { firmante.getNombreFirmante() };
			respuesta.mensaje = MessageFormat
					.format(this.messageSource.getMessage("sistcorr.asignar_firma.pregunta", null, locale), parametros);
			respuesta.estado = true;
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	public Respuesta<Firmante> rechazarSolicitudFirma(Long idCorrespondencia, Firmante firmante, String usuarioMod, String token,
			Locale locale) {
		this.LOGGER.info("[INICIO] rechazarSolicitudFirma");
		Respuesta<Firmante> respuesta = new Respuesta<>();;
		try {
			// Ticket 9000004409
			List<UsuarioRemplazo> usuarioRemplazo = this.filenetService.obtenerUsuarioRemplazo(usuarioMod);		
			// FIN TICKET
			
			respuesta = this.correspondenciaService.rechazarFirma(idCorrespondencia, usuarioMod,
					firmante.getMotivoRechazo().getId(), firmante.getDescripcionMotivoRechazo(), token, locale, usuarioRemplazo);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] rechazarSolicitudFirma", e);
		}
		this.LOGGER.info("[FIN] rechazarSolicitudFirma");
		return respuesta;
	}

	public Respuesta<Firmante> firmarCorrespondencia(Long idCorrespondencia, String usuarioFirmante,
			String directorioFirmados, Locale locale) {
		this.LOGGER.info("[INICIO] firmarCorrespondencia");
		Respuesta<Firmante> respuesta = new Respuesta<>();;
		try {
			Respuesta<Correspondencia> res = this.correspondenciaService.buscarCorrespondencia(idCorrespondencia,
					locale);
			if (res.estado == true) {
				Correspondencia correspondencia = res.datos.get(0);
				// Ticket 9000004409
				List<UsuarioRemplazo> usuarioRemplazo = this.filenetService.obtenerUsuarioRemplazo(usuarioFirmante);		
				// FIN TICKET
				
				//if (!correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ASIGNADA)) {
				if (!correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ASIGNADA) && !correspondencia.isRutaAprobacion()) {
					LOGGER.info("Correspondencia sin ruta de aprobación, diferente a estado asignada");
					List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(idCorrespondencia);
					if (firmantes == null)
						throw new Exception(
								this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
					Firmante firmante = firmantes.get(firmantes.size() - 1);
					if (firmante.getCodFirmante().equals(usuarioFirmante)) {
						respuesta.estado = true;
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.emitir_firma.exito", null, locale);
						respuesta.datos.add(firmante);
					} else {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.emitir_firma.firmado_antes", null, locale));
					}
					return respuesta;
				}
				// TCKT 9000003997 - DEFECTO
				if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ASIGNADA) && correspondencia.isRutaAprobacion()) {
					LOGGER.info("Correspondencia con ruta de aprobación, estado asignada");
					List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(idCorrespondencia);
					if (firmantes == null)
						throw new Exception(
								this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
					Firmante firmante = firmantes.get(firmantes.size() - 1);
					// Ticket 9000004409
				    boolean bolRemplazo = false;
					for (UsuarioRemplazo user : usuarioRemplazo) {
						LOGGER.info("Usuario: "  + user.getUsername());
	                    if (firmante.getCodFirmante().toLowerCase().equals(user.getUsername().toLowerCase())){
	                    	bolRemplazo = true;
	                    	break;
	                    }
					}
					// FIN Ticket 9000004409
					if (firmante.getCodFirmante().toUpperCase().equals(usuarioFirmante.toUpperCase()) || bolRemplazo) {
						
					}else{
						Firmante firmante2 = firmantes.get(firmantes.size() - 2);
						if (firmante2.getCodFirmante().equals(usuarioFirmante)) {
							respuesta.estado = true;
							// TCKT 9000003997 - DEFECTO
							respuesta.mensaje = this.messageSource.getMessage("sistcorr.asignar_ruta_aprobacion.exito", null, locale) + " " + firmante.getNombreFirmante();
							// FIN TCKT 
							respuesta.datos = null;
						} else {
							throw new Exception(
									this.messageSource.getMessage("sistcorr.emitir_firma.firmado_antes", null, locale));
						}
						return respuesta;
					}
				}
				if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_FIRMADA) && correspondencia.isRutaAprobacion()) {
					LOGGER.info("Correspondencia con ruta de aprobación, estado firmada");
					List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(idCorrespondencia);
					if (firmantes == null)
						throw new Exception(
								this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
					Firmante firmante = firmantes.get(firmantes.size() - 1);
					if (firmante.getCodFirmante().equals(usuarioFirmante)) {
						respuesta.estado = true;
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.siguiente_asignacion_ruta_aprobacion.error", null, locale);
						respuesta.datos = null;
					}
					return respuesta;
				}
				if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_APROBADA) && correspondencia.isRutaAprobacion()) {
					LOGGER.info("Correspondencia con ruta de aprobación, estado aprobada");
					List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(idCorrespondencia);
					if (firmantes == null)
						throw new Exception(
								this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
					Firmante firmante = firmantes.get(firmantes.size() - 1);
					if (firmante.getCodFirmante().equals(usuarioFirmante)) {
						respuesta.estado = true;
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.emitir_firma.exito", null, locale);
						respuesta.datos = null;
					} else {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.emitir_firma.firmado_antes", null, locale));
					}
					return respuesta;
				}
				// FIN TCKT
				if (this.simularFirma == true) {
					respuesta = this.correspondenciaService.emitirFirma(correspondencia.getId(), usuarioFirmante,
							locale, usuarioRemplazo);
				} else if (moverDocumentosAHistorico(correspondencia)) {
					if (moverDocumentosFirmadosAAdjuntos(correspondencia, directorioFirmados)) {
						respuesta = this.correspondenciaService.emitirFirma(correspondencia.getId(), usuarioFirmante,
								locale, usuarioRemplazo);
						if (!respuesta.estado) {
							restaurarDocumentos(correspondencia);
						}
					} else {
						restaurarDocumentos(correspondencia);
						eliminarDocumentosErroneos(correspondencia, directorioFirmados);
						throw new Exception(
								this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
					}
					//elimino documentos historicos
					eliminarDocumentoHistorico(correspondencia);

					
				} else {
					throw new Exception(
							this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
				}

			}

		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] firmarCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] firmarCorrespondencia");
		return respuesta;
	}

	public Respuesta<Correspondencia> enviarCorrespondencia(Long idCorrespondencia, String usuarioModifica,
			String token, Locale locale) {
		this.LOGGER.info("[INICIO] enviarCorrespondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {
			
			
		Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			
			// TICKET 9000004496
			long minPermimitido = 0;
			List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("VALOR_PROCESO_ENVIO", "ENVIO_CORRESPONDENCIA");
			if(listParametro != null && listParametro.size() > 0) 
				minPermimitido = listParametro.get(0).getValor().intValue();
			
			if (correspondencia.getFlgEnvio() == null || correspondencia.getFlgEnvio().equals("F") || correspondencia.getFlgEnvio().equals("E")){
				correspondencia.setFlgEnvio("I");
				correspondencia.setFlgEnvioInicio(new Date());
				this.correspondenciaDAO.save(correspondencia);
				//this.correspondenciaDAO.actualizaEstadoInicioEnvioCorrespondencia(idCorrespondencia,correspondencia.getFlgEnvio(),correspondencia.getFlgEnvioInicio());
			}else{
				Date dateinicio = correspondencia.getFlgEnvioInicio();
				Date dateActual = new Date();
				long mintEspera = obtenerTotalMinutos(dateinicio, dateActual);  
				if (correspondencia.getFlgEnvio().equals("I") && (mintEspera < minPermimitido)){
					respuesta.estado = false;
					respuesta.mensaje = messageSource.getMessage("sistcorr.correspondencia.error.envio.en.proceso", null, locale);
					return respuesta;
				}
			}
			// fin TICKET 9000004496
			
			// Ticket 9000004409
			List<UsuarioRemplazo> usuarioRemplazo = filenetService.obtenerUsuarioRemplazo(usuarioModifica);
			// FIN Ticket 9000004409
			
			respuesta = this.correspondenciaService.enviarCorrespondencia(idCorrespondencia, usuarioModifica, token,
					locale, usuarioRemplazo);
			// TICKET 9000004496
			if(respuesta.estado == false){
			  actualizaEstadoErrorEnvioCorespondencia(idCorrespondencia, correspondencia);				
			}
			// TICKET 9000004496
			
		} catch (Exception e) {	
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			// TICKET 9000004496
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			actualizaEstadoErrorEnvioCorespondencia(idCorrespondencia, correspondencia);	
			// TICKET 9000004496
			this.LOGGER.error("[ERROR] enviarCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] enviarCorrespondencia");
		return respuesta;
	}

	// TICKET 9000004496
		private void actualizaEstadoErrorEnvioCorespondencia(Long idCorrespondencia, Correspondencia correspondencia) {
			correspondencia.setFlgEnvio("E");
			correspondencia.setFlgEnvioFin(new Date());
			this.correspondenciaDAO.actualizaEstadoErrorEnvioCorrespondencia(idCorrespondencia,correspondencia.getFlgEnvio(),correspondencia.getFlgEnvioFin());
		}

		public static long obtenerTotalMinutos(Date dateInicio, Date dateFin) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String dateStart = format.format(dateInicio); 
			String dateStop = format.format(dateFin);
			Date d1 = null;
	        Date d2 = null;
	        try {
	            d1 = format.parse(dateStart);
	            d2 = format.parse(dateStop);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }    
	        long diff = d2.getTime() - d1.getTime();

	        long days = TimeUnit.MILLISECONDS.toDays(diff);
	        long remainingHoursInMillis = diff - TimeUnit.DAYS.toMillis(days);
	        long hours = TimeUnit.MILLISECONDS.toHours(remainingHoursInMillis);
	        long remainingMinutesInMillis = remainingHoursInMillis - TimeUnit.HOURS.toMillis(hours);
	        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMinutesInMillis);
	        
	        long totalMinutos = days* 1440 + hours * 60 + minutes ;
	       
		    return totalMinutos;
		}
		// FIN TICKET 9000004496
	
	public Respuesta<Correspondencia> reasignarCorrespondencia(Long idCorrespondencia, String nuevoResponsable,
			String usuarioModifica, Locale locale) {
		this.LOGGER.info("[INICIO] reasignarCorrespondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		// Ticket 9000004409
		List<UsuarioRemplazo> usuarioRemplazo = filenetService.obtenerUsuarioRemplazo(usuarioModifica);
		// FIN Ticket 9000004409
		respuesta = this.correspondenciaService.reasignarCorrespondencia(idCorrespondencia, nuevoResponsable,
				usuarioModifica, locale , usuarioRemplazo);
		this.LOGGER.info("[FIN] reasignarCorrespondencia");
		return respuesta;
	}

	public Respuesta<Correspondencia> declinarCorrespondencia(Long idCorrespondencia, String usuarioModifica, String token,
			Locale locale) {
		// Ticket 9000004409
		List<UsuarioRemplazo> usuarioRemplazo = filenetService.obtenerUsuarioRemplazo(usuarioModifica);
		// FIN Ticket 9000004409
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		respuesta = this.correspondenciaService.declinarCorrespondencia(idCorrespondencia, usuarioModifica, token, locale, usuarioRemplazo);
		return respuesta;
	}

	public Respuesta<ItemFilenet> buscarFuncionariosReemplazantes(Long idCorrespondencia, String text, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();;
		try {
			List<ItemFilenet> filtrados = new ArrayList<>();
			Respuesta<Correspondencia> res = this.correspondenciaService.buscarCorrespondencia(idCorrespondencia,
					locale);
			respuesta.datos.addAll(this.filenetService
					.listarFuncionariosPorDependencia(((Correspondencia) res.datos.get(0)).getCodDependenciaOriginadora(), text));
			respuesta.estado = true;
			respuesta.mensaje = "OK";
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	private boolean moverDocumentosAHistorico(Correspondencia correspondencia) {
		this.LOGGER.info("[INICIO] moverDocumentosAHistorico");
		boolean resultado = false;
		String pathDirectorioHistorico = this.DIRECCTORIO_BASE + "historico";
		File directorio = new File(pathDirectorioHistorico);
		if (!directorio.exists()) {
			directorio.mkdirs();
		}
		for (ArchivoAdjunto archivo : correspondencia.getAdjuntos()) {
			if (archivo.isPrincipal()) {
				try {
					File historico = new File(this.DIRECCTORIO_BASE + "historico" + "/" + archivo.getNombreServidor());
					if (historico.exists()) {
						historico.delete();
					}
					Path temp = Files.move(Paths.get(archivo.getUbicacion(), new String[0]),
							Paths.get(this.DIRECCTORIO_BASE + "historico" + "/" + archivo.getNombreServidor(),
									new String[0]),
							new java.nio.file.CopyOption[0]);
					resultado = true;
				} catch (Exception e) {
					resultado = false;
					this.LOGGER.error("[ERROR] moverDocumentosAHistorico", e);

					break;
				}
			}
		}
		this.LOGGER.info("[FIN] moverDocumentosAHistorico");
		return resultado;
	}
	
	// TICKET 9000003992
	private boolean eliminarDocumentoHistorico(Correspondencia correspondencia) {
		this.LOGGER.info("[INICIO] eliminarDocumentoHistorico");
		boolean resultado = false;
		String pathDirectorioHistorico = this.DIRECCTORIO_BASE + "historico";
		File directorio = new File(pathDirectorioHistorico);
		if (!directorio.exists()) {
			directorio.mkdirs();
		}
		for (ArchivoAdjunto archivo : correspondencia.getAdjuntos()) {
			if (archivo.isPrincipal()) {
				try {
					File historico = new File(this.DIRECCTORIO_BASE + "historico" + "/" + archivo.getNombreServidor());
					if (historico.exists()) {
						historico.delete();
					}
					resultado = true;
				} catch (Exception e) {
					resultado = false;
					this.LOGGER.error("[ERROR] eliminaDocumentosHistorico", e);

					break;
				}
			}
		}
		
		this.LOGGER.info("[FIN] eliminarDocumentoHistorico");
		return resultado;
	}

	private boolean moverDocumentosFirmadosAAdjuntos(Correspondencia correspondencia, String nombreCarpeta) {
		this.LOGGER.info("[INICIO] moverDocumentosComprimidosAAdjuntos");
		boolean resultado = false;
		String directorioDescromprimido = this.DIRECCTORIO_BASE + "comprimidos_firmados" + "/" + nombreCarpeta;
		String directorioComprimido = this.DIRECCTORIO_BASE + "comprimidos_firmados" + "/" + nombreCarpeta + ".zip";
		File directorio = new File(directorioDescromprimido);
		if (!directorio.exists()) {
			return false;
		}
		for (ArchivoAdjunto archivo : correspondencia.getAdjuntos()) {
			if (archivo.isPrincipal()) {
				try {
					Path temp = Files.move(Paths.get(directorio + "/" + archivo.getNombreServidor(), new String[0]),
							Paths.get(archivo.getUbicacion(), new String[0]), new java.nio.file.CopyOption[0]);
					resultado = true;
				} catch (Exception e) {
					resultado = false;
					this.LOGGER.error("[ERROR] moverDocumentosComprimidosAAdjuntos", e);

					break;
				}
			}
		}
		try {
			File zip = new File(directorioComprimido);
			if (zip.exists()) {
				zip.delete();
			}
			directorio.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.LOGGER.info("[FIN] moverDocumentosComprimidosAAdjuntos");
		return resultado;
	}

	private boolean restaurarDocumentos(Correspondencia correspondencia) {
		this.LOGGER.info("[INICIO] restaurarDocumentos");
		boolean resultado = false;
		for (ArchivoAdjunto archivo : correspondencia.getAdjuntos()) {
			if (archivo.isPrincipal()) {
				try {
					Path temp = Files.move(
							Paths.get(this.DIRECCTORIO_BASE + "historico" + "/" + archivo.getNombreServidor(),
									new String[0]),
							Paths.get(archivo.getUbicacion(), new String[0]), new java.nio.file.CopyOption[0]);
					resultado = true;
				} catch (Exception e) {
					resultado = false;
					this.LOGGER.error("[ERROR] restaurarDocumentos", e);
					break;
				}
			}
		}
		this.LOGGER.info("[FIN] restaurarDocumentos");
		return resultado;
	}
	
	// TICKET 9000004981
	private boolean eliminarDocumentosErroneos(Correspondencia correspondencia, String directorio) {
		this.LOGGER.info("[INICIO] eliminarDocumentosErroneos");
		boolean resultado = false;
		// ELIMINANDO ARCHIVOS
		for(ArchivoAdjunto archivoAdjunto : correspondencia.getAdjuntos()){
			if(archivoAdjunto.isPrincipal()){
				try{
					String nombre = archivoAdjunto.getNombreServidor();
					int tamanio = nombre.length();
					String nombreErroneo = nombre.substring(0, tamanio-4) + "-signaturefailed.pdf";
					File firmado = new File(this.DIRECCTORIO_BASE + DIRECTORIO_COMPRIMIDOS_FIRMADOS + "/" + directorio + "/" + nombre);
					File firmadoErroneo = new File(this.DIRECCTORIO_BASE + DIRECTORIO_COMPRIMIDOS_FIRMADOS + "/" + directorio + "/" + nombreErroneo);
					if (firmado.exists()) {
						firmado.delete();
					}
					if (firmadoErroneo.exists()) {
						firmadoErroneo.delete();
					}
					resultado = true;
				}catch(Exception e){
					resultado = false;
					this.LOGGER.error("[ERROR] eliminarDocumentosErroneos ", e);
					return resultado;
				}
			}
		}
		// ELIMINANDO DIRECTORIO
		File carpeta = new File(this.DIRECCTORIO_BASE + DIRECTORIO_COMPRIMIDOS_FIRMADOS + "/" + directorio);
		if(carpeta.exists()){
			carpeta.delete();
		}
		this.LOGGER.info("[FIN] eliminarDocumentosErroneos");
		return resultado;
	}
	
	public boolean eliminarCarpetaComprimidosFirmados(String directorio){
		this.LOGGER.info("[INICIO] eliminarCarpetaComprimidosFirmados " + directorio);
		boolean resultado = false;
		try{
			File carpeta = new File(this.DIRECCTORIO_BASE + DIRECTORIO_COMPRIMIDOS_FIRMADOS + "/" + directorio);
			if(carpeta.exists()){
				if(carpeta.listFiles().length < 50){
					for(File archivo : carpeta.listFiles()){
						archivo.delete();
					}
					carpeta.delete();
				}
			}
		}catch(Exception e){
			resultado = false;
			this.LOGGER.error("[ERROR] eliminarCarpetaComprimidosFirmados ", e);
			return resultado;
		}
		this.LOGGER.info("[FIN] eliminarCarpetaComprimidosFirmados");
		return resultado;
	}
	// FIN TICKET

	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondencuas(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, 
			Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondencuas " + filtro.toString());
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			respuesta = correspondenciaService.consultarCorrespondencias(
					filtro.isConsiderarOriginadora() ? 1 : 0,
					filtro.getCodDependenciaOriginadora(), 
					filtro.getCodDependenciaRemitente(), 
					filtro.getCorrelativo(), 
					filtro.getAsunto().toLowerCase(), 
					filtro.getEstado(), 
					filtro.isMasFiltros() ? 1 : 0,
					filtro.getCodNombreOriginador(),
					filtro.getFechaDesdeText(),
					filtro.getFechaHastaText(), 
					filtro.getTipoCorrespondencia(),
					filtro.getTipoEmision(), 
					filtro.getFirmaDigital(),
					filtro.getConfidencial(),
					filtro.getUrgente(), 
					filtro.getDespachoFisico(), 
					filtro.getCodDestinatario(),
					filtro.getNombreDestinatario().toLowerCase(),
					filtro.getCodCopia().toLowerCase(), 
					usuario, 
					esJefe,
					filtro.isTodos() ? 1 : 0,
					locale);
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondencuas", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	@Override
	public Respuesta<ByteArrayInputStream> consultarCorrespondenciasExcel(FiltroConsultaCorrespondenciaDTO filtro, String usuarioCreador, String username, int esJefe, Locale locale){
		LOGGER.info("[INICIO] consultarCorrespondenciasExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			//Respuesta<CorrespondenciaConsultaDTO> consulta = consultarCorrespondencuas(filtro, username, esJefe, locale);
			Respuesta<CorrespondenciaConsultaDTO> consulta = consultarCorrespondenciasPaginado(filtro, username, esJefe, 0, 100, Constante.COLUMNAS_CONSULTA_GENERAL[1], "DESC", "SI", locale);
			LOGGER.info("Resultados del excel:" + consulta.datos.size());
			IReport<ByteArrayInputStream> report;
			if(consulta.estado) {
				report = new ReportExcel(consulta.datos, usuarioCreador);
			} else {
				report = new ReportExcel(new ArrayList<>(), usuarioCreador);
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	// TICKET 9000003791
	@Override
	public Respuesta<ByteArrayInputStream> consultarHistorialExcel(Long idCorrespondencia, FiltroConsultaHistorial filtro, String usuarioCreador, Correspondencia corr, Locale locale){
		LOGGER.info("[INICIO] consultarCorrespondenciasExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			// COMENTAR SI SE DESEAN INCLUIR LOS PARAMETROS DE BUSQUEDA
			filtro.setFechaDesde("");
			filtro.setFechaHasta("");
			filtro.setValorBuscar("");
			// FIN COMENTAR
			List<Object[]> datos = historialArchivoDAO.obtenerHistorialCompartidoCorrespondencia(idCorrespondencia, filtro.getFechaDesde(), filtro.getFechaHasta(), filtro.getValorBuscar()); 
			IReport<ByteArrayInputStream> report;
			if(datos != null) {
				report = new ReporteExcelHistorial(datos, usuarioCreador, corr.getCorrelativo().getCodigo());
			} else {
				report = new ReporteExcelHistorial(new ArrayList<>(), usuarioCreador, corr.getCorrelativo().getCodigo());
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	// FIN TICKET 9000003791

	// INICIO TICKET 9000004408
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestor(
			FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondenciasJefeGestor " + filtro.toString());
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			respuesta = correspondenciaService.consultarCorrespondenciasJefeGestor(
					filtro.getCodDependenciaRemitente(), 
					filtro.getCorrelativo(), 
					filtro.getAsunto().toLowerCase(), 
					filtro.getEstado(), 
					filtro.getFechaDesdeText(),
					filtro.getFechaHastaText(),
					locale);
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondenciasJefeGestor", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	// FIN TICKET 9000004408
	
	// TICKET 9000004494
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasPaginado(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, int start, int length, String columna, String order, String excel, 
			Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondenciasPaginado " + filtro.toString());
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			respuesta = correspondenciaService.consultarCorrespondenciasPaginado(
					filtro.isConsiderarOriginadora() ? 1 : 0,
					filtro.getCodDependenciaOriginadora(), 
					filtro.getCodDependenciaRemitente(), 
					filtro.getCorrelativo(), 
					filtro.getAsunto().toLowerCase(), 
					filtro.getEstado(), 
					filtro.isMasFiltros() ? 1 : 0,
					filtro.getCodNombreOriginador(),
					filtro.getFechaDesdeText(),
					filtro.getFechaHastaText(), 
					filtro.getTipoCorrespondencia(),
					filtro.getTipoEmision(), 
					filtro.getFirmaDigital(),
					filtro.getConfidencial(),
					filtro.getUrgente(), 
					filtro.getDespachoFisico(), 
					filtro.getCodDestinatario(),
					filtro.getNombreDestinatario().toLowerCase(),
					filtro.getCodCopia().toLowerCase(), 
					usuario, 
					esJefe,
					filtro.isTodos() ? 1 : 0, 
					start, length, columna, order, excel, 
					filtro.getFechaModificaDesdeText(),
					filtro.getFechaModificaHastaText(), 
					locale);
			if (respuesta.datos.size() > 0){
				respuesta.total = respuesta.datos.get(0).getTotal();
			}
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondenciasPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestorPaginado(
			FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, String token, int length, int start, String columna, String order, String excel, Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondenciasJefeGestor " + filtro.toString());
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			respuesta = correspondenciaService.consultarCorrespondenciasJefeGestorPaginado(
					filtro.getCodDependenciaRemitente(), 
					filtro.getCorrelativo(), 
					filtro.getAsunto().toLowerCase(), 
					filtro.getEstado(), 
					filtro.getFechaDesdeText(),
					filtro.getFechaHastaText(),
					token, length, start, columna, order, excel,
					filtro.getFechaModificaDesdeText(),
					filtro.getFechaModificaHastaText(),
					locale);
			if (respuesta.datos.size() > 0){
				respuesta.total = respuesta.datos.get(0).getTotal();
			}
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondenciasJefeGestor", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	// FIN TICKET


	@Override
	public Respuesta<ByteArrayInputStream> consultarCorrespondenciasExcelJefeGestor(
			FiltroConsultaCorrespondenciaDTO filtro, String usuarioCreador, String usuario, int esJefe, Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondenciasExcelJefeGestor " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			Respuesta<CorrespondenciaConsultaDTO> consulta = consultarCorrespondenciasJefeGestor(filtro, usuario, esJefe, locale); 
			IReport<ByteArrayInputStream> report;
			if(consulta.estado) {
				report = new ReportExcel(consulta.datos, usuarioCreador);
			} else {
				report = new ReportExcel(new ArrayList<>(), usuarioCreador);
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	// FIN TICKET 9000004408
	// TICKET 9000004409
	@Override
	public void actualizarRutaAprobacionJefeActual(UsuarioPetroperu usuario, int esJefe, Locale locale) {
			try {
				String vRemplazo = this.filenetService.validarRemplazoVigenteUsuario(usuario.getUsername());
				
				if(vRemplazo.equals("NO")) {
					List<Integer> rolesInt = new ArrayList<>();
					rolesInt.add(esJefe);

					List<ItemFilenet> listDependenciaJefe = this.filenetService.listarTodosDependenciasJefeGestor(usuario.getUsername(), "", rolesInt);
					String  dependenciasJefe="";
					String firman="";
					String rutaApro="";
					if (listDependenciaJefe.size() > 0){
						
						for (ItemFilenet dep : listDependenciaJefe) {
							dependenciasJefe = dependenciasJefe +  dep.getCodigo() + ",";
						}
						
						dependenciasJefe = dependenciasJefe.substring(0, dependenciasJefe.length()-1);
					
						List<Object[]> firmantes = this.firmanteDAO.obtenerIdFirmaIdCorrelativo(dependenciasJefe, usuario.getUsername());
						
						if (firmantes.size() > 0){
							
							for (Object[] rol : firmantes) {
									firman = firman +  rol[2].toString() + ",";
									rutaApro = rutaApro + rol[3].toString() + ",";
							}
							
						
							firman = firman.substring(0, firman.length()-1);
							rutaApro = rutaApro.substring(0, rutaApro.length()-1);
							LOGGER.info("Se actualizó los siguientes ids: " + firman +  " en la tabla firmante con con el jefe actual: " + usuario.getUsername());
							LOGGER.info("Se actualizó los siguientes ids: " + rutaApro +  " en la tabla ruta_aprobacion con con el jefe actual: " + usuario.getUsername());
							this.firmanteDAO.actualizaUsuarioReemplazoFirmante(firman, usuario.getUsername(),usuario.getNombreCompleto(), new Date());	
							this.firmanteDAO.actualizaUsuarioRutaAprobacion(rutaApro, usuario.getUsername(),usuario.getNombreCompleto(), new Date());
							LOGGER.info("--- Fin actualización---");
						}
						
					}
					
				}		
	
			} catch (Exception e) {
				LOGGER.error("[ERROR] actualizarRutaAprobacionJefeActual", e);
			}
		}
	    
		// FIN TICKET 9000004409
	
	private UsuarioPetroperu obtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		return null;
	}
	
	// inicio ticket 9000004714
	@Override
	public Respuesta<DatosFirmante> registrarDatosFirmante(Long idCorrespondencia, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] registrarDatosFirmante ");
		Respuesta<DatosFirmanteDTO> respuesta_ = new Respuesta<>();
		Respuesta<DatosFirmante> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			respuesta_ = this.correspondenciaService.obtenerDatosFirmanteYFirmantePrevio(idCorrespondencia, locale,
					usuario.getUsername());
			DatosFirmante entidad = null;

			entidad = new DatosFirmante();
			entidad.setId(Long.valueOf(0L));
			entidad.setUsuarioCrea(usuario.getUsername());
			entidad.setFechaCrea(new Date());

			if (respuesta_ != null && (respuesta_.datos.size() == 1 || respuesta_.datos.size() == 2)) {

				entidad.setCodFirmante(respuesta_.datos.get(0).getCodFirmante());
				entidad.setSolicitante(respuesta_.datos.get(0).getSolicitante());
				entidad.setMotivoRechazo(respuesta_.datos.get(0).getMotivoRechazo());

				if (respuesta_.datos.size() == 2)
					entidad.setCodFirmantePrevio(respuesta_.datos.get(1).getCodFirmantePrevio());
			}

			respuesta.estado = (respuesta_ != null
					&& (respuesta_.datos.size() >= 1 && respuesta_.datos.get(0).getMensaje() != null
							&& respuesta_.datos.get(0).getMensaje().equalsIgnoreCase("OK"))) ? (true) : (false);
			respuesta.datos.add(entidad);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	// fin ticket 9000004714
	
	/*INI Ticket 9*4807*/
	@Override
	public Respuesta<ByteArrayInputStream> exportarExcelCorrespondenciasBS(
			FiltroBandeja filtro, String usuarioCreador, String usuario, String bandeja, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] exportarExcelCorrespondenciasBS " + filtro.toString());
		
		Respuesta<CorrespondenciaDTO> respuestaData = new Respuesta<>();
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			
			if ("pendiente".equals(bandeja)) {
				respuestaData = this.pendientes(filtro, locale);
			} else if ("firmado".equals(bandeja)) {
				respuestaData = this.firmados(filtro, locale);
			} else if ("enviado".equals(bandeja)) {
				respuestaData = this.enviados(filtro, locale);
			} else {
				respuestaData.estado = false;
				respuestaData.mensaje = "No existe la bandeja " + bandeja;
			}
			
			if(respuestaData.datos == null){
				respuestaData.datos = new ArrayList<CorrespondenciaDTO>();
			}
			if (respuestaData.estado) {
				respuestaData.mensaje = this.messageSource.getMessage("sistcorr.buscarCorrespondenciaExito", null, locale);
			}
			
			LOGGER.info("Resultados del excel:" + respuesta.datos.size());
			IReport<ByteArrayInputStream> report;
			if(respuestaData.estado) {
				report = new ReportExcelCBandejaSalida(respuestaData.datos, usuarioCreador, bandeja);
			} else {
				report = new ReportExcelCBandejaSalida(new ArrayList<>(), usuarioCreador, bandeja);
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	/*FIN Ticket 9*4807*/
	
	// TICKET 9000004462
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarAuditoriaCorrespondenciasPaginado(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, int start, int length, String columna, String order, String excel, 
			Locale locale) {
		LOGGER.info("[INICIO] consultarAuditoriaCorrespondenciasPaginado " + filtro.toString());
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			respuesta = correspondenciaService.consultarAuditoriaCorrespondenciasPaginado(
					//filtro.isConsiderarOriginadora() ? 1 : 0,
					filtro.getCodDependenciaOriginadora(), 
					filtro.getCodDependenciaRemitente(), 
					filtro.getCorrelativo(), 
					filtro.getAsunto().toLowerCase(), 
					filtro.getEstado(), 
					filtro.isMasFiltros() ? 1 : 0,
					filtro.getCodNombreOriginador(),
					filtro.getFechaDesdeText(),
					filtro.getFechaHastaText(), 
					filtro.getTipoCorrespondencia(),
					filtro.getTipoEmision(), 
					filtro.getFirmaDigital(),
					filtro.getConfidencial(),
					filtro.getUrgente(), 
					filtro.getDespachoFisico(), 
					filtro.getCodDestinatario(),
					filtro.getNombreDestinatario().toLowerCase(),
					filtro.getCodCopia().toLowerCase(), 
					//usuario, 
					//esJefe,
					filtro.isTodos() ? 1 : 0, 
					start, length, columna, order, excel, 
					filtro.getFechaModificaDesdeText(),
					filtro.getFechaModificaHastaText(), 
					locale);
			if (respuesta.datos.size() > 0){
				respuesta.total = respuesta.datos.get(0).getTotal();
			}
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarAuditoriaCorrespondenciasPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	@Override
	public Respuesta<ByteArrayInputStream> consultarAuditoriaCorrespondenciasExcel(FiltroConsultaCorrespondenciaDTO filtro, String usuarioCreador, String username, int esJefe, Locale locale){
		LOGGER.info("[INICIO] consultarAuditoriaCorrespondenciasExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			//Respuesta<CorrespondenciaConsultaDTO> consulta = consultarCorrespondencuas(filtro, username, esJefe, locale);
			Respuesta<CorrespondenciaConsultaDTO> consulta = consultarAuditoriaCorrespondenciasPaginado(filtro, username, esJefe, 0, 100, Constante.COLUMNAS_CONSULTA_GENERAL[1], "DESC", "SI", locale);
			LOGGER.info("Resultados del excel:" + consulta.datos.size());
			IReport<ByteArrayInputStream> report;
			if(consulta.estado) {
				report = new ReportExcel(consulta.datos, usuarioCreador);
			} else {
				report = new ReportExcel(new ArrayList<>(), usuarioCreador);
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	//FIN 
}



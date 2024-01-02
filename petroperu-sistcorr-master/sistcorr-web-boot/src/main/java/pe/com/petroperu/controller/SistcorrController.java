package pe.com.petroperu.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.hibernate.boot.archive.scan.spi.AbstractScannerImpl.ArchiveContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import com.itextpdf.text.pdf.codec.Base64.InputStream;

import pe.com.petroperu.PDFTool;
import pe.com.petroperu.Respuesta;
import pe.com.petroperu.Utilitario;
import pe.com.petroperu.ZipTool;
import pe.com.petroperu.ad.util.RolAD;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.cliente.model.Bandeja;
import pe.com.petroperu.cliente.model.CorrespondenciaMerge;
import pe.com.petroperu.cliente.model.DatosCompartirCorrespondencia;
import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.cliente.model.FiltroConsultaAuditoria;
import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.cliente.model.FiltroConsultaDependencia;
import pe.com.petroperu.cliente.model.FiltroCorrespondencia;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.ListaFiltroCorrespondencia;
import pe.com.petroperu.cliente.model.RegistrarObservacion;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.cliente.model.emision.FiltroConsultaHistorial;
import pe.com.petroperu.cliente.util.SistcorrBandeja;
import pe.com.petroperu.dto.Data;
import pe.com.petroperu.controller.SistcorrController;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.filenet.model.ItemTipoCorrespondencia;
import pe.com.petroperu.firma.digital.IProcesoFirmaDigitalZip;
import pe.com.petroperu.model.Accion;
import pe.com.petroperu.model.Asignacion;
import pe.com.petroperu.model.AsignacionConsulta;
import pe.com.petroperu.model.BaseVendida;
import pe.com.petroperu.model.ConsultaBase;
import pe.com.petroperu.model.Contratacion;
import pe.com.petroperu.model.CopiaCorrespondencia;
import pe.com.petroperu.model.Correspondencia;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaSimple;
import pe.com.petroperu.model.CorrespondenciaTareaPaginado;
import pe.com.petroperu.model.DependenciaUnidadMatricial;
import pe.com.petroperu.model.Devolucion;
import pe.com.petroperu.model.Estado;
import pe.com.petroperu.model.Expediente;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.Impugnacion;
import pe.com.petroperu.model.InformacionAsignacion;
import pe.com.petroperu.model.Integrante;
import pe.com.petroperu.model.Menu;
import pe.com.petroperu.model.Observaciones;
import pe.com.petroperu.model.OrdenServicio;
import pe.com.petroperu.model.Propuesta;
import pe.com.petroperu.model.Rol;
import pe.com.petroperu.model.Tracking;
import pe.com.petroperu.model.TrackingFisico;
import pe.com.petroperu.model.Traza;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.Valija;
import pe.com.petroperu.model.VentaBases;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.ArchivoCompartido;
import pe.com.petroperu.model.emision.Correlativo;
import pe.com.petroperu.model.emision.CorrespondenciaCompartida;
import pe.com.petroperu.model.emision.DestinatarioInterno;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.HistorialArchivo;
import pe.com.petroperu.model.emision.MotivoRechazo;
import pe.com.petroperu.model.emision.Parametro;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.model.emision.TipoEmision;
import pe.com.petroperu.model.emision.dto.ComprobanteConsultaDTO;
import pe.com.petroperu.model.emision.dto.ContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaCompartidaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaEnlaceDTO;
import pe.com.petroperu.model.emision.dto.EstDigContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;
import pe.com.petroperu.model.emision.dto.TrackingConsultaDTO;
import pe.com.petroperu.model.util.SistcorrEstado;
import pe.com.petroperu.notificacion.NotificacionService;
import pe.com.petroperu.service.IComprobanteService;
import pe.com.petroperu.service.IContratacionService;
import pe.com.petroperu.service.ICorrespondenciaEmisionService;
import pe.com.petroperu.service.ICorrespondenciaEstadoService;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.IEmisionService;
import pe.com.petroperu.service.IFilenetService;
import pe.com.petroperu.service.IFirmanteService;
import pe.com.petroperu.service.IMotivoRechazoService;
import pe.com.petroperu.service.IRolService;
import pe.com.petroperu.service.IRutaAprobacionService;
import pe.com.petroperu.service.ITipoEmisionService;
import pe.com.petroperu.service.dto.Flujo;
import pe.com.petroperu.service.dto.FiltroConsultaComprobanteDTO;
import pe.com.petroperu.service.dto.FiltroConsultaContratacionDTO;
import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.service.util.ReportExcelConsultaAsignacion;
import pe.com.petroperu.service.util.ReportExcelConsultaAuditoria;
import pe.com.petroperu.service.util.ReportExcelConsultaCorrespondencia;
import pe.com.petroperu.service.util.ReporteExcelHistorial;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;
import pe.com.petroperu.sistcorr.dao.IArchivoCompartidoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrelativoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaCompartidaDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;
import pe.com.petroperu.sistcorr.dao.IFirmarteDAO;
import pe.com.petroperu.sistcorr.dao.IHistorialArchivoDAO;
import pe.com.petroperu.sistcorr.dao.IParametroDAO;
import pe.com.petroperu.sistcorr.dao.IRolDAO;
import pe.com.petroperu.sistcorr.dao.IRutaAprobacionDAO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.Tutoriales;
import pe.com.petroperu.util.datatable.DataTableResults;
import pe.com.petroperu.util.datatable.entity.DataTableRequestConsultaCorrespondencia;
import pe.com.petroperu.util.datatable.entity.DataTableRequestReemplazoConsulta;
import pe.com.petroperu.util.datatable.entity.DataTableRequestTracking;
import pe.com.petroperu.util.datatable.entity.DatatableRequestConsultaComprobante;
import pe.com.petroperu.util.datatable.entity.DatatableRequestConsultaContratacion;
import pe.com.petroperu.util.datatable.entity.DatatableRequestConsultaEstDigContratacion;

@Controller
@RequestMapping({ "/app/" })
@PropertySource({ "classpath:application.properties" })
public class SistcorrController {
	// TICKET 9000003992
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ICorrespondenciaService correspondeciaService;

	@Autowired
	private IFirmanteService firmanteService;
	
	@Value("${sistcorr.bandeja.entrada.version.despues4273}")
	private Integer versionBandEntradaCorresAsignadas;//TICKET 9000004273

	@Autowired
	private IFilenetService filenetService;

	@Value("${sistcorr.archivo.tamanio}")
	private Integer tamanioMaxArchivo;

	@Value("${sistcorr.nueva_correspondencia}")
	private String nuevaCorrespondencia;
	
	@Value("${sistcorr.por_aceptar}")
	private String porAceptar;
	
	@Value("${sistcorr.correspondencia.mpv}")
	private String correspondenciaMPV;//TICKET 9000005111

	@Value("${sistcorr.archivo.max_size}")
	private Integer tamanioMaxArchivoUpload;
	
	@Value("${sistcorr.archivo.correspondencia.sin_firma.max_size}")
	private Integer tamanioMaxArchivoUploadSinFirmaDigital;

	@Autowired
	private ICorrespondenciaEmisionService correspondenciaEmisionService;

	@Autowired
	private IEmisionService emisionService;

	@Autowired
	private IRolService rolService;
	
	@Autowired
	private ICorrespondenciaEstadoService correspondenciaEstadoService;
	
	@Autowired
	private ISistcorrCliente sistcorrCliente;
	
	
	private final String PATH_ASIGNACIONES = "asig";
	private final String PATH_COORRESPONDENCIAS = "corr";
	private final String NUEVA_CORRESPONDENCIA = "NUEVA_CORRESPONDENCIA";
	private final String POR_ACEPTAR = "POR_ACEPTAR";
	// TICKET 9000004494
	private final String TAMANIO_BAND_ENTR = "TAMANIO_BAND_ENTR";
	// FIN TICKET

	@Autowired
	private ITipoEmisionService tipoEmisionService;
	@Autowired
	private IMotivoRechazoService motivoService;

	// TICKET 9000003791
	@Value("${sistcorr.api.url_compartir}")
	private String apiUrlCompartirCorrespondencia;
	@Autowired
	private IArchivoCompartidoDAO archivoCompartidoDAO;
	@Autowired
	private ICorrespondenciaDAO correspondenciaDAO;
	@Autowired
	private ICorrespondenciaCompartidaDAO correspondenciaCompartidaDAO;
	@Autowired
	private IHistorialArchivoDAO historialArchivoDAO;
	@Autowired
	private IArchivoAdjuntoDAO archivoAdjuntoDAO;
	@Autowired
	private NotificacionService notificacionService;
	@Autowired
	private IFirmarteDAO firmanteDAO;
	@Value("${sistcorr.directorio}")
	private String urlBase;
	private final String DIRECTORIO_ADJUNTOS = "adjuntos";
	private final String DIRECTORIO_ADJUNTOS_ZIP = "zip";

	// TICKET 9000003906
	@Value("${sistcorr.directorio.tutoriales}")
	private String directorioTutoriales;

	@Autowired
	private IRolDAO rolDAO;

	// TICKET 9000003943
	@Autowired
	private IRutaAprobacionService rutaAprobacionService;
	  
	@Autowired
	private IRutaAprobacionDAO rutaAprobacionDAO;
	
	@Autowired
	private IParametroDAO parametroDAO;
	
	// TICKET 9000004044
	private ICorrelativoDAO correlativoDAO;
	
	// TICKET 9000004494
	@Value("${sistcorr.paginado.band_entrada}")
	private String tamanioBandEntrada;
	
	@Autowired
	private IProcesoFirmaDigitalZip procesoFirmaDigital;
	
	/*9000004276 - INICIO*/
	@Autowired
	private IComprobanteService comprobanteService;	

	@Autowired
	private IContratacionService contratacionService;	
	/*9000004276 - FIN*/
	  
	@GetMapping({ "/inicio" })
	public ModelAndView inicio(Locale locale){
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] inicio");
		boolean esGestor = false;
		boolean noEsGestorNiEjecutor = true;
		boolean esResponsabe=false; // Ticket 9*4413
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		for (Object[] rol : roles) {
			LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			if("ROLE_GESTOR".equals(rol[3].toString())){
				LOGGER.info("GESTOR:" + rol[3].toString());
				esGestor = true;
			}
			if(!"ROLE_GESTOR".equals(rol[3].toString()) && !"ROLE_EJECUTOR".equals(rol[3].toString()) && !"ROLE_USUARIO".equals(rol[3].toString())){
				LOGGER.info("NO GESTO NO EJECUTOR:" + rol[3].toString());
				noEsGestorNiEjecutor = false;
			}
			/*INI Ticket 9*4413*/
			if("ROLE_RECEPTOR".equals(rol[3].toString())){
				LOGGER.info("RECEPTOR:" + rol[3].toString());
				esResponsabe = true;
			}
			/*FIN Ticket 9*4413*/
		}
	    if(esGestor&&noEsGestorNiEjecutor){
	    	LOGGER.info("DEL GESTOR");
	    	return new ModelAndView("redirect:" + "/app/delgestor");
	    }/*INI Ticket 9*4413*/
		else if (esResponsabe){
			LOGGER.info("DEL RESPONSABLE");
			return new ModelAndView("redirect:" + "/app/delreceptor");
		}
		/*FIN Ticket 9*4413*/
		else{
	    	LOGGER.info("PENDIENTES");
	    	return new ModelAndView("redirect:" + "/app/pendientes");
	    }
	}

	// TICKET 9000004497

	 @GetMapping({ "/{bandeja}/asignacionGrupal/{id:.+}" })
	public ModelAndView asignacionCorrespondenciaMultiple(@PathVariable("bandeja") String bandeja,
			@PathVariable("id") String id, @RequestParam(value = "workflow", required = true) String workflow,
			Locale locale) {
	 

		UsuarioPetroperu usuario = obtenerUsuario();
		if (!tieneRol(usuario, RolAD.JEFE)) {
			return new ModelAndView("redirect:/403.html");
		}
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("correspondencia_asignacion_grupal");
		view.addObject("tituloGrupal",
				this.messageSource.getMessage("sistcorr.correspondencia.asignar.grupal.titulo", null, locale));
		view.addObject("bandeja", bandeja);
		CorrespondenciaMerge correspondencia = new CorrespondenciaMerge();
		Integer idAsignacion = null;
		String correlativo = null;
		try {
			idAsignacion = Integer.valueOf(Integer.parseInt(id));
		} catch (Exception e) {
			correlativo = id;
		}
		
		if (idAsignacion != null) {
			Respuesta<InformacionAsignacion> respuesta = this.correspondeciaService
					.recuperarInformacionAsignacion(usuario.getToken(), idAsignacion, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(false);
			} else {
				correspondencia = new CorrespondenciaMerge(
						((InformacionAsignacion) respuesta.datos.get(0)).getCorrespondencia(),
						((InformacionAsignacion) respuesta.datos.get(0)).getAsignacion());
			}
		}

		if (correlativo != null) {
			Respuesta<Correspondencia> respuesta = this.correspondeciaService
					.recuperarCorrespondencia(usuario.getToken(), correlativo, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(true);
			} else {
				correspondencia = new CorrespondenciaMerge(respuesta.datos.get(0));
			}
		}

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("tarea", correspondencia);
		view.addObject("correlativo", correspondencia.getCorrelativo());
		view.addObject("workflow", workflow);
		view.addObject("errores", errores);
		view.addObject("NUEVA_CORRESPONDENCIA", this.nuevaCorrespondencia);
		view.addObject(POR_ACEPTAR, this.porAceptar);
		return view;
	}

	// FIN TICKET
			
	
	@GetMapping({ "/{bandeja}" })
	public ModelAndView correspondencia(@PathVariable("bandeja") String bandeja, Locale locale) {
		LOGGER.info("BANDEJA");
		String str;
		/*INI Ticket 9*4413*/
		ModelAndView view = null;
		if (bandeja.equalsIgnoreCase("delreceptor")){
			 view = new ModelAndView("correspondencia_receptor");
		}else{
		/*FIN Ticket 9*4413*/
			view = new ModelAndView("correspondencia");
		}//Ticket 9*4413
		UsuarioPetroperu usuario = obtenerUsuario();
		List<String> errores = new ArrayList<>();
		switch (bandeja) {
		case "pendientes":
			view.addObject("titulo",
					messageSource.getMessage("sistcorr.correspondencia.pendientes.titulo", null, locale));
			view.addObject("tipoCorrespondencia", SistcorrBandeja.PENDIENTE.TD_BANDEJA);
			view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.pendientes.url", null, locale));
			break;
		case "atencion":
			view.addObject("titulo",
					messageSource.getMessage("sistcorr.correspondencia.atencion.titulo", null, locale));
			view.addObject("tipoCorrespondencia", SistcorrBandeja.EN_ATENCION.TD_BANDEJA);
			view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.atencion.url", null, locale));
			break;
		case "completadas":
			view.addObject("titulo",
					messageSource.getMessage("sistcorr.correspondencia.completados.titulo", null, locale));
			view.addObject("tipoCorrespondencia", SistcorrBandeja.COMPLETADA.TD_BANDEJA);
			view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.completados.url", null, locale));
			break;
		// TICKET 9000003862
		case "delgestor":
			view.addObject("titulo",
					messageSource.getMessage("sistcorr.correspondencia.delgestor.titulo", null, locale));
			view.addObject("tipoCorrespondencia", SistcorrBandeja.DE_GESTOR.TD_BANDEJA);
			view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.delgestor.url", null, locale));
			break;
		// FIN TICKET
			/*INI Ticket 9*4413*/
		case "delreceptor" :
			view.addObject("titulo",
					messageSource.getMessage("sistcorr.correspondencia.delreceptor.titulo",null,locale));
			view.addObject("tipoCorrespondencia", SistcorrBandeja.DE_RECEPTOR.TD_BANDEJA);
			view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.delreceptor.url", null, locale));
		break;
		/*FIN Ticket 9*4413*/
		default:
			view = new ModelAndView("redirect:/404.html");
			break;
		}
		view.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 7000003969
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();
		// FIN TICKET
		// TICKET 7000003969
		List<Menu> listaMenu = new ArrayList<>();
		Object menuSession = session.getAttribute("menus");
		if(menuSession == null){
			List<Menu> menus = correspondeciaService.obtenerMenuSistcorr(usuario, locale);
			session.setAttribute("menus", menus);
			listaMenu = menus;
		}else{
			listaMenu = (List<Menu>) menuSession;
		}
		//view.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("listaMenu", listaMenu);
		// FIN TICKET
		view.addObject("usuario", usuario);
		view.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
		// TICKET 7000003969
		Respuesta<Accion> rptaAcciones = new Respuesta<>();
		Object accionesSession = session.getAttribute("acciones");
		if(accionesSession == null){
			Respuesta<Accion> acciones = correspondeciaService.obtenerListaAcciones(usuario.getToken(), locale);
			session.setAttribute("acciones", acciones);
			rptaAcciones = acciones;
		}else{
			rptaAcciones = (Respuesta<Accion>) accionesSession;
		}
		//Respuesta<Accion> respuestaAcciones = correspondeciaService.obtenerListaAcciones(usuario.getToken(), locale);
		Respuesta<Accion> respuestaAcciones = rptaAcciones;
		// FIN TICKET
		view.addObject("acciones", respuestaAcciones.datos);
		// TICKET 9000003862
		// TICKET 7000003969 - INCIDENTE
		List<ItemFilenet> listaDependenciasDestino = new ArrayList<>();
		Object dependenciaSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS);
		if(dependenciaSession == null){
			List<ItemFilenet> dependenciasDestino = filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), "");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS, dependenciasDestino);
			listaDependenciasDestino = dependenciasDestino;
		}else{
			listaDependenciasDestino = (List<ItemFilenet>) dependenciaSession;
		}
		//List<ItemFilenet> listaDependenciasDestino = filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), "");
		// FIN TICKET - INCIDENTE
		view.addObject("dependenciasDestino", listaDependenciasDestino);
		// FIN TICKET
		// TICKET 7000003969
		List<Estado> listaEstadosCorrespondencia = new ArrayList<>();
		Object estadoSession = session.getAttribute("estados");
		if(estadoSession == null){
			List<Estado> estadosCorrespondencia = correspondeciaService.listarEstadosFile("TD_CR");
			session.setAttribute("estados", estadosCorrespondencia);
			listaEstadosCorrespondencia = estadosCorrespondencia;
		}else{
			listaEstadosCorrespondencia = (List<Estado>) estadoSession;
		}
		//List<Estado> listaEstadosCorrespondencia = correspondeciaService.listarEstadosFile("TD_CR");
		// FIN TICKET 
		//inicio tikcet 9000003866
		List<ItemFilenet> listaDependenciasBandES = new ArrayList<>();
		Object dependenciaBandESSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_BES);
		if(dependenciaBandESSession == null){
			List<ItemFilenet> dependenciasBandES = filenetService.obtenerDependenciasBandES(usuario.getUsername());
			session.setAttribute(Constante.SESSION_DEPENDENCIAS_BES, dependenciasBandES);
			listaDependenciasBandES = dependenciasBandES;
		}else{
			listaDependenciasBandES = (List<ItemFilenet>) dependenciaBandESSession;
		}
		view.addObject("dependenciasDestinoBPAC", listaDependenciasBandES);//ticket 9000003866
		//fin tikcet 9000003866
		view.addObject("estadosCorr", listaEstadosCorrespondencia);
		view.addObject("tamanioMaxArchivo", tamanioMaxArchivo);
		// TICKET 7000003969
		//view.addObject("esJefe", tieneRol(usuario, RolAD.JEFE));
		//view.addObject("esGestor", tieneRol(usuario, RolAD.GESTOR));
		List<Rol> roles = new ArrayList<>();
		Object rolesSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if(rolesSession == null){
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			roles = rolesBD;
		}else{
			roles = (List<Rol>) rolesSession;
		}
		
		//inicio ticket 9000004807
		List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("FLAG_ADVERTENCIA_BE", "VALOR_LIM_MOSTRAR_MSJ_EXPORT_BE");
		view.addObject("limit_export_be_advert", 1000);
		if(listParametro != null && listParametro.size() > 0) 
			view.addObject("limit_export_be_advert", listParametro.get(0).getValor().intValue());
		//fin ticket 9000004807
		
		view.addObject("esJefe", tieneRolSession(roles, RolAD.JEFE));
		view.addObject("esGestor", tieneRolSession(roles, RolAD.GESTOR));
		// FIN TICKET
		view.addObject("errores", errores);
		view.addObject("versionBandEntradaCorresAsignadas", versionBandEntradaCorresAsignadas);//TICKET 9000004273
		
		view.addObject(NUEVA_CORRESPONDENCIA, nuevaCorrespondencia);
		view.addObject(POR_ACEPTAR, porAceptar);
		view.addObject("CORRESPONDENCIA_MPV", correspondenciaMPV);//TICKET 9000005111
		// TICKET 9000004494
		view.addObject(TAMANIO_BAND_ENTR, tamanioBandEntrada);
		// FIN TICKET
		return view;
	}

	@GetMapping({ "/{bandeja}/informacion/{tipo_correspondencia}/{id:.+}" })
	public ModelAndView detalleCorrespondencia(@PathVariable("bandeja") String bandeja,
			@PathVariable("tipo_correspondencia") String tipoCorrespondencia, @PathVariable("id") String id,
			@RequestParam(value = "workflow", required = true) String workflow, Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		/*INI Ticket 9*4413*/
		ModelAndView view = new ModelAndView();
		if (bandeja.equalsIgnoreCase("delreceptor")){
			 view = new ModelAndView("correspondencia_detalle_receptor");
		}else{
		/*FIN Ticket 9*4413*/
			view = new ModelAndView("correspondencia_detalle");
		}//Ticket 9*4413
		List<String> errores = new ArrayList<>();
		// TICKET 7000004004
		String bandejaFilenet = "";
		// FIN TICKET
		switch (bandeja) {
		case "pendientes":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.PENDIENTE.TD_BANDEJA);
			// TICKET 7000004004
			bandejaFilenet = pe.com.petroperu.cliente.util.Constante.BANDEJA_PENDIENTES;
			// FIN TICKET
			break;
		case "atencion":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.EN_ATENCION.TD_BANDEJA);
			// TICKET 7000004004
			bandejaFilenet = pe.com.petroperu.cliente.util.Constante.BANDEJA_ATENCION;
			// FIN TICKET
			break;
		case "completadas":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.COMPLETADA.TD_BANDEJA);
			// TICKET 7000004004
			bandejaFilenet = pe.com.petroperu.cliente.util.Constante.BANDEJA_COMPLETADAS;
			// FIN TICKET
			break;
		case "consulta-asignaciones":
			view.addObject("tipoCorrespondencia", "consulta-asignaciones");
			break;
		case "consulta-correspondencia":
			view.addObject("tipoCorrespondencia", "consulta-correspondencia");
			break;
		case "delgestor":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.DE_GESTOR.TD_BANDEJA);
			// TICKET 7000004004
			bandejaFilenet = pe.com.petroperu.cliente.util.Constante.BANDEJA_DELGESTOR;
			// FIN TICKET
			break;
		// TICKET 9000004961
		case "consulta-auditoria":
			view.addObject("tipoCorrespondencia", "consulta-auditoria");
			break;
		// FIN TICKET
		/*INI Ticket 9*4413*/
		case "delreceptor" :
			view.addObject("tipoCorrespondencia",SistcorrBandeja.DE_RECEPTOR.TD_BANDEJA);
			bandejaFilenet = pe.com.petroperu.cliente.util.Constante.BANDEJA_DELRECEPTOR;
			break;
		/*FIN Ticket 9*4413*/
		default:
			view = new ModelAndView("redirect:/404.html");
			break;
		}
		CorrespondenciaMerge correspondencia = new CorrespondenciaMerge();
		CorrespondenciaMerge correspondenciaMPV = new CorrespondenciaMerge();//ticket 9*4413
		if (PATH_COORRESPONDENCIAS.equals(tipoCorrespondencia)) {
			LOGGER.info("CORRESPONDENCIA");
			Respuesta<Correspondencia> respuesta = this.correspondeciaService
					.recuperarCorrespondencia(usuario.getToken(), id, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(true);
			} else {
				correspondencia = new CorrespondenciaMerge(respuesta.datos.get(0));
				// TICKET 9000003780
				if(bandeja.equalsIgnoreCase("consulta-correspondencia") || bandeja.equalsIgnoreCase("consulta-auditoria")){
					FiltroConsultaCorrespondencia filtro = new FiltroConsultaCorrespondencia();
					filtro.setProcedencia("");
					filtro.setNumeroDocumentoInterno("");
					filtro.setNombreDependenciaExterna("");
					filtro.setCorrelativo(id);
					Respuesta<CorrespondenciaConsulta> respuestaConsulta = this.correspondeciaService.consultarCorrespondencia(usuario.getToken(), filtro, 10, 1, Constante.COLUMNAS_CONSULTA_CORRESPONDENCIA[2], "SI", "NO", locale);
					LOGGER.info("DATOS EXTRAIDOS:" + respuestaConsulta.datos.size());
					if(respuestaConsulta.estado && respuestaConsulta.datos != null && respuestaConsulta.datos.size() > 0){
						if(respuestaConsulta.datos.get(0).getCorrelativo().equals(correspondencia.getCorrelativo())){
							if(correspondencia.getEsAsignado() == null){
								correspondencia.setEsAsignado(respuestaConsulta.datos.get(0).getEsAsignado());
							}
						}
					}
				}else{
					ListaFiltroCorrespondencia filtro = new ListaFiltroCorrespondencia();
					FiltroCorrespondencia criterio = new FiltroCorrespondencia();
					criterio.setFieldId("TD_sCorrelativo");
					criterio.setOperator("=");
					criterio.setValue(id);
					List<FiltroCorrespondencia> filtros = new ArrayList<>();
					filtros.add(criterio);
					filtro.setSearchCriteria(filtros);
					Respuesta<CorrespondenciaTareaPaginado> respuestaBandeja = this.correspondeciaService.buscarCorrespondencia(usuario.getToken(), bandejaFilenet, filtro, tamanioBandEntrada, locale);
					LOGGER.info("DATOS EXTRAIDOS:" + respuestaBandeja.datos.size());
					if(respuestaBandeja.estado && respuestaBandeja.datos != null && respuestaBandeja.datos.size() > 0 && 
							respuestaBandeja.datos.get(0).getDetalleCorrespondencias()!= null && 
							respuestaBandeja.datos.get(0).getDetalleCorrespondencias().size() > 0){
						if(respuestaBandeja.datos.get(0).getDetalleCorrespondencias().get(0).equals(correspondencia.getCorrelativo())){
							if(correspondencia.getEsAsignado() == null){
								CorrespondenciaTareaPaginado band = respuestaBandeja.datos.get(0);
								CorrespondenciaSimple corrSimple = band.getDetalleCorrespondencias().get(0);
								correspondencia.setEsAsignado(corrSimple.getEsAsignado());
							}
						}
					}
				}
				// FIN TICKET
			}
			/*INI Ticket 9*4413*/
			Respuesta<Correspondencia> respuestaMVP = this.correspondeciaService
					.recuperarCorrespondenciaMPV(usuario.getToken(), id, locale);
			if (!respuestaMVP.estado) {
				errores.add(respuestaMVP.mensaje);
				correspondenciaMPV = new CorrespondenciaMerge();
				correspondenciaMPV.setNuevaCorrespondencia(true);
			}else{
				correspondenciaMPV = new CorrespondenciaMerge(respuestaMVP.datos.get(0));
				view.addObject("correspondenciaMPV",respuestaMVP.datos.get(0));//Ticket 9*4413
			}
			/*FIN Ticket 9*4413*/
		} else if (PATH_ASIGNACIONES.equals(tipoCorrespondencia)) {
			LOGGER.info("ASIGNACION");
			Integer idAsignacion = Integer.valueOf(id.trim());
			Respuesta<InformacionAsignacion> respuesta = this.correspondeciaService
					.recuperarInformacionAsignacion(usuario.getToken(), idAsignacion, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(false);
			} else {
				LOGGER.info("Fecha pre" + respuesta.datos.get(0).getCorrespondencia().getFechaDocumento());
				correspondencia = new CorrespondenciaMerge(
						((InformacionAsignacion) respuesta.datos.get(0)).getCorrespondencia(),
						((InformacionAsignacion) respuesta.datos.get(0)).getAsignacion());
				LOGGER.info("Fecha post" + correspondencia.getFechaDocumento());
				// correspondencia.setFechaDocumento("ABCDEFG");
			}
		} else {
			view = new ModelAndView("redirect:/404.html");
			return view;
		}
		
		// TICKET 9000004044
		LOGGER.info("Verificando enlace con documento");
		if(correspondencia.getNumeroDocumentoEnlace() != null && !"".equalsIgnoreCase(correspondencia.getNumeroDocumentoEnlace())){
			List<pe.com.petroperu.model.emision.Correspondencia> correspondenciaSalida = correspondenciaEmisionService.buscarCorrespondenciaPorNumeroDocumento(correspondencia.getNumeroDocumentoEnlace());
			if(correspondenciaSalida != null && correspondenciaSalida.size() > 0){
				//correspondencia.setNumeroDocumentoEnlace(correspondenciaSalida.get(0).getCorrelativo().getCodigo());
				correspondencia.setIdDocumentoEnlace(correspondenciaSalida.get(0).getId());
				LOGGER.info("Correlativo " + correspondencia.getCorrelativo() + " enlazado con documento " + correspondencia.getNumeroDocumentoEnlace() + " con id " + correspondencia.getIdDocumentoEnlace());
			}
		}
		// FIN TICKET

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("bandeja", bandeja);
		view.addObject("correlativo", correspondencia.getCorrelativo());
		view.addObject("workflow", workflow);
		view.addObject("tamanioMaxArchivo", this.tamanioMaxArchivo);
		view.addObject("titulo", correspondencia.getAsunto());
		view.addObject("tarea", correspondencia);
		view.addObject("esJefe", tieneRol(usuario, RolAD.JEFE));
		view.addObject("esGestor", tieneRol(usuario, RolAD.GESTOR));
		view.addObject("versionCorrespBandEntrada", versionBandEntradaCorresAsignadas);

		LOGGER.info("esAsignado:" + correspondencia.getEsAsignado());
		LOGGER.info("nuevaCorrespondencia:" + correspondencia.isNuevaCorrespondencia());

		Respuesta<Asignacion> respuestaHitorico = this.correspondeciaService
				.obtenerHistorialAsignaciones(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if (!respuestaHitorico.estado) {
			errores.add(respuestaHitorico.mensaje);
			view.addObject("historicoAsignaciones", new ArrayList<>());
		} else {
			// TICKET 9000003973
			List<Asignacion> asignaciones = respuestaHitorico.datos;
			for (Asignacion a : asignaciones) {
				//this.LOGGER.info("Asignado Por:" + a.getNombreApellidoAsignador());
				String[] asignadoPor = a.getNombreApellidoAsignador().split("<br>");
				List<String> asigPor = new ArrayList<String>();
				for (String ap : asignadoPor) {
					if (!ap.trim().equalsIgnoreCase("")) {
						asigPor.add(ap);
					}
				}
				String asignado = "";
				for (int i = 0; i < asigPor.size(); i++) {
					asignado = asignado + asigPor.get(i);
					if (i < asigPor.size() - 1) {
						asignado = asignado + "<br>";
					}
				}
				a.setNombreApellidoAsignador(asignado);
				//this.LOGGER.info("Asignado Por:" + a.getNombreApellidoAsignador());
				//this.LOGGER.info("Asignado a:" + a.getNombreApellidoAsignado());
				String[] asignadoA = a.getNombreApellidoAsignado().split("<br>");
				List<String> asigA = new ArrayList<String>();
				for (String aa : asignadoA) {
					if (!aa.trim().equalsIgnoreCase("")) {
						asigA.add(aa);
					}
				}
				String asignadoa = "";
				for (int i = 0; i < asigA.size(); i++) {
					asignadoa = asignadoa + asigA.get(i);
					if (i < asigA.size() - 1) {
						asignadoa = asignadoa + "<br>";
					}
				}
				a.setNombreApellidoAsignado(asignadoa);
				//this.LOGGER.info("Asignado Por:" + a.getNombreApellidoAsignado());
				//this.LOGGER.info(a.toString());
				// TICKET 9000004044
				if(a.getNumeroDocumentoEnlace()!=null && !"".equalsIgnoreCase(a.getNumeroDocumentoEnlace())){
					LOGGER.info("Buscando relación de asignación con documento respuesta " + a.getNumeroDocumentoEnlace());
					List<pe.com.petroperu.model.emision.Correspondencia> corrSalida = correspondenciaEmisionService.buscarCorrespondenciaPorNumeroDocumento(a.getNumeroDocumentoEnlace());
					if(corrSalida != null && corrSalida.size() > 0){
						//a.setNumeroDocumentoEnlace(corrSalida.get(0).getCorrelativo().getCodigo());
						a.setIdDocumentoEnlace(corrSalida.get(0).getId());
					}
				}
				//a.setNumeroDocumentoEnlace("GISE-0027-2021");
				//a.setIdDocumentoEnlace(1003L);
				// FIN TICKET
			}
			/*
			 * for (Asignacion a : respuestaHitorico.datos) {
			 * this.LOGGER.info(a.toString()); }
			 */
			// FIN TICKET
			view.addObject("historicoAsignaciones", respuestaHitorico.datos);
		}
		
		// TICKET 9000003514
		Respuesta<Traza> respuestaTraza = this.correspondeciaService.obtenerListaTrazas(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if(!respuestaTraza.estado){
			errores.add(respuestaTraza.mensaje);
			view.addObject("eventos", new ArrayList<>());
		}else{
			for (Traza t : respuestaTraza.datos){
				if(t.getTraza().length() > 100){
					t.setNumCaract(false);
				}else{
					t.setNumCaract(true);
				}
			}
			// TICKET 9000003973
			List<Traza> trazas = respuestaTraza.datos;
			for (Traza t : trazas) {
				//this.LOGGER.info("Traza:" + t.getNomApeUsuario());
				String[] trazaPor = t.getNomApeUsuario().split("<br>");
				List<String> trPor = new ArrayList<String>();
				for(String tr : trazaPor){
					if(!tr.trim().equalsIgnoreCase("")){
						trPor.add(tr);
					}
				}
				String traz = "";
				for(int i = 0; i < trPor.size(); i++){
					traz = traz+ trPor.get(i);
					if(i < trPor.size()-1){
						traz = traz + "<br>";
					}
				}
				t.setNomApeUsuario(traz);
				//this.LOGGER.info("Traza:" + t.getNomApeUsuario());
			}
			/*for (Asignacion a : respuestaHitorico.datos) {
				this.LOGGER.info(a.toString());
			}*/
			// FIN TICKET
			view.addObject("eventos", respuestaTraza.datos);
		}
		
		Respuesta<Observaciones> respuestaObservaciones = this.correspondeciaService.obtenerListaObservaciones(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if(!respuestaObservaciones.estado){
			errores.add(respuestaObservaciones.mensaje);
			view.addObject("observaciones", new ArrayList<>());
		}else{
			for (Observaciones o : respuestaObservaciones.datos){
				this.LOGGER.info(o.toString());
				if(o.getObservacion().length() > 100){
					o.setNumCaract(false);
				}else{
					o.setNumCaract(true);
				}
			}
			view.addObject("observaciones", respuestaObservaciones.datos);
		}
		
		Respuesta<TrackingFisico> respuestaTrackingFisico = this.correspondeciaService.obtenerTrackingFisico(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if(!respuestaTrackingFisico.estado){
			errores.add(respuestaTrackingFisico.mensaje);
			view.addObject("tracking", new ArrayList<>());
		}else{
			for (TrackingFisico t : respuestaTrackingFisico.datos){
				this.LOGGER.info(t.toString());
			}
			view.addObject("tracking", respuestaTrackingFisico.datos);
		}
		// FIN TICKET

		Respuesta<InformacionDocumento> respuestaAdjuntos = this.correspondeciaService
				.obtenerDocumentosAdjuntos(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if (!respuestaAdjuntos.estado) {
			errores.add(respuestaAdjuntos.mensaje);
			view.addObject("documentos", new ArrayList<>());
		} else {
			List<InformacionDocumento> adjuntos = respuestaAdjuntos.datos;
			view.addObject("documentos", adjuntos);
		}

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("formatoFecha", "dd/MM/yyyy HH:mm");
		view.addObject("esJefe", Boolean.valueOf(tieneRol(usuario, RolAD.JEFE)));
		view.addObject("esGestor", Boolean.valueOf(tieneRol(usuario, RolAD.GESTOR)));
		view.addObject("errores", errores);
		view.addObject("NUEVA_CORRESPONDENCIA", this.nuevaCorrespondencia);
		view.addObject(POR_ACEPTAR, this.porAceptar);
		view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
		// TICKET 9000003514
		//view.addObject("eventos",  new ArrayList<>());
		//view.addObject("observaciones",  new ArrayList<>());
		//view.addObject("tracking",  new ArrayList<>());
		// FIN TICKET
		return view;
	}
	
	@GetMapping({ "/{bandeja}/copia/{tipo_correspondencia}/{id:.+}" })
	public ModelAndView enviarCopia(@PathVariable("bandeja") String bandeja,
			@PathVariable("tipo_correspondencia") String tipoCorrespondencia, @PathVariable("id") String id,
			@RequestParam(value = "workflow", required = true) String workflow, Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		ModelAndView view = new ModelAndView("copia_correspondencia");
		List<String> errores = new ArrayList<>();
		switch (bandeja) {
		case "pendientes":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.PENDIENTE.TD_BANDEJA);
			break;
		case "atencion":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.EN_ATENCION.TD_BANDEJA);
			break;
		case "completadas":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.COMPLETADA.TD_BANDEJA);
			break;
		case "delgestor":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.DE_GESTOR.TD_BANDEJA);
			break;
		case "consulta-correspondencia":
			view.addObject("tipoCorrespondencia", "consulta-correspondencia");
			break;
		case "consulta-asignaciones":
			view.addObject("tipoCorrespondencia", "consulta-asignaciones");
			break;
		/*INI Ticket 9*4413*/
		case "delreceptor":
			view.addObject("tipoCorrespondencia", SistcorrBandeja.DE_RECEPTOR.TD_BANDEJA);
			break;	
		/*FIN Ticket 9*4413*/
		default:
			view = new ModelAndView("redirect:/404.html");
			break;
		}
		CorrespondenciaMerge correspondencia = new CorrespondenciaMerge();
		if (PATH_COORRESPONDENCIAS.equals(tipoCorrespondencia)) {
			Respuesta<Correspondencia> respuesta = this.correspondeciaService
					.recuperarCorrespondencia(usuario.getToken(), id, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(true);
			} else {
				correspondencia = new CorrespondenciaMerge(respuesta.datos.get(0));
			}

		} else if (PATH_ASIGNACIONES.equals(tipoCorrespondencia)) {
			Integer idAsignacion = Integer.valueOf(id.trim());
			Respuesta<InformacionAsignacion> respuesta = this.correspondeciaService
					.recuperarInformacionAsignacion(usuario.getToken(), idAsignacion, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(false);
			} else {
				correspondencia = new CorrespondenciaMerge(
						((InformacionAsignacion) respuesta.datos.get(0)).getCorrespondencia(),
						((InformacionAsignacion) respuesta.datos.get(0)).getAsignacion());
			}
		} else {
			view = new ModelAndView("redirect:/404.html");
			return view;
		}

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("bandeja", bandeja);
		view.addObject("correlativo", correspondencia.getCorrelativo());
		view.addObject("workflow", workflow);
		view.addObject("tamanioMaxArchivo", this.tamanioMaxArchivo);
		view.addObject("titulo",
				this.messageSource.getMessage("sistcorr.correspondencia.copia.titulo", null, locale));
		
		view.addObject("tarea", correspondencia);

		Respuesta<Asignacion> respuestaHitorico = this.correspondeciaService
				.obtenerHistorialAsignaciones(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if (!respuestaHitorico.estado) {
			errores.add(respuestaHitorico.mensaje);
			view.addObject("historicoAsignaciones", new ArrayList<>());
		} else {
			for (Asignacion a : respuestaHitorico.datos) {
				this.LOGGER.info(a.toString());
			}
			view.addObject("historicoAsignaciones", respuestaHitorico.datos);
		}
		
		// TICKET 9000003514
		Respuesta<CopiaCorrespondencia> respuestaCopiaCorrespondencia = this.correspondeciaService.obtenerCopiaCorrespondencia(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if(!respuestaCopiaCorrespondencia.estado){
			LOGGER.info("Información copia correspondencia estado false: " + respuestaCopiaCorrespondencia.mensaje);
			errores.add(respuestaCopiaCorrespondencia.mensaje);
			view.addObject("destinatarios", new ArrayList());
		}else{
			LOGGER.info("Información copia correspondencia estado true: " + respuestaCopiaCorrespondencia.datos.size());
			view.addObject("destinatarios", respuestaCopiaCorrespondencia.datos);
		}
		
		Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionarios(usuario.getToken(), "0", "", "SI", locale);
		if(!respuestaFuncionario.estado){
			LOGGER.info("Información funcionario estado false: " + respuestaFuncionario.mensaje);
			errores.add(respuestaFuncionario.mensaje);
			view.addObject("funcionarios", new ArrayList());
		}else{
			LOGGER.info("Información funcionario estado true: " + respuestaFuncionario.datos.size());
			view.addObject("funcionarios", respuestaFuncionario.datos);
		}
		// FIN TICKET

		Respuesta<InformacionDocumento> respuestaAdjuntos = this.correspondeciaService
				.obtenerDocumentosAdjuntos(usuario.getToken(), correspondencia.getCorrelativo(), locale);
		if (!respuestaAdjuntos.estado) {
			errores.add(respuestaAdjuntos.mensaje);
			view.addObject("documentos", new ArrayList<>());
		} else {
			List<InformacionDocumento> adjuntos = respuestaAdjuntos.datos;
			view.addObject("documentos", adjuntos);
		}

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("formatoFecha", "dd/MM/yyyy HH:mm");
		view.addObject("esJefe", Boolean.valueOf(tieneRol(usuario, RolAD.JEFE)));
		view.addObject("esGestor", Boolean.valueOf(tieneRol(usuario, RolAD.GESTOR)));
		view.addObject("errores", errores);
		view.addObject("NUEVA_CORRESPONDENCIA", this.nuevaCorrespondencia);
		view.addObject(POR_ACEPTAR, this.porAceptar);
		view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
		/*9000004276 - INICIO*/
		view.addObject("tipoCopia1", "correspondencia");
		view.addObject("tipoCopia2", " la correspondencia");
		/*9000004276 - FIN*/
		return view;
	}
	
	// TICKET 9000003943
	@GetMapping(value = "/buscar/personas-todas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<ItemFilenet>> listarDependenciasTodas(@RequestParam("codDependencia") String codDependencia,  @RequestParam(value = "q", defaultValue = "") String texto, Locale locale){
		UsuarioPetroperu usuario = obtenerUsuario();
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionariosRuta(usuario.getToken(), "0", "", "SI", texto, locale);
		LOGGER.info("Cantidad de Personas:" + respuestaFuncionario.datos.size());
		List<ItemFilenet> datosFuncionarios = new ArrayList<>();
		for(Funcionario f : respuestaFuncionario.datos){
			ItemFilenet i = new ItemFilenet();
			i.setCodigo(f.getUsuario());
			i.setDescripcion(f.getNombreApellidoUsuario());
			datosFuncionarios.add(i);
		}
		respuesta.datos.addAll(datosFuncionarios);
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET

	@GetMapping({ "/{bandeja}/asignacion/{id:.+}" })
	public ModelAndView asignacionCorrespondencia(@PathVariable("bandeja") String bandeja,
			@PathVariable("id") String id, @RequestParam(value = "workflow", required = true) String workflow,
			Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (!tieneRol(usuario, RolAD.JEFE)) {
			return new ModelAndView("redirect:/403.html");
		}
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("correspondencia_asignacion");
		view.addObject("titulo",
				this.messageSource.getMessage("sistcorr.correspondencia.asignar.titulo", null, locale));
		view.addObject("bandeja", bandeja);

		CorrespondenciaMerge correspondencia = new CorrespondenciaMerge();
		Integer idAsignacion = null;
		String correlativo = null;
		try {
			idAsignacion = Integer.valueOf(Integer.parseInt(id));
		} catch (Exception e) {
			correlativo = id;
		}

		if (idAsignacion != null) {
			Respuesta<InformacionAsignacion> respuesta = this.correspondeciaService
					.recuperarInformacionAsignacion(usuario.getToken(), idAsignacion, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(false);
			} else {
				correspondencia = new CorrespondenciaMerge(
						((InformacionAsignacion) respuesta.datos.get(0)).getCorrespondencia(),
						((InformacionAsignacion) respuesta.datos.get(0)).getAsignacion());
			}
		}

		if (correlativo != null) {
			Respuesta<Correspondencia> respuesta = this.correspondeciaService
					.recuperarCorrespondencia(usuario.getToken(), correlativo, locale);
			if (!respuesta.estado) {
				errores.add(respuesta.mensaje);
				correspondencia = new CorrespondenciaMerge();
				correspondencia.setNuevaCorrespondencia(true);
			} else {
				correspondencia = new CorrespondenciaMerge(respuesta.datos.get(0));
			}
		}

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("tarea", correspondencia);
		view.addObject("correlativo", correspondencia.getCorrelativo());
		view.addObject("workflow", workflow);
		view.addObject("errores", errores);
		view.addObject("NUEVA_CORRESPONDENCIA", this.nuevaCorrespondencia);
		view.addObject(POR_ACEPTAR, this.porAceptar);
		return view;
	}

	@GetMapping({ "/registro" })
	public ModelAndView registroCorrespondencia(Locale locale) {
		
		List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
		if(listParametro != null && listParametro.size() > 0) 
			this.tamanioMaxArchivoUploadSinFirmaDigital = listParametro.get(0).getValor().intValue();
		UsuarioPetroperu usuario = obtenerUsuario();
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("correspondencia_edicion");
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.registro", null, locale));
		
		view.addObject("usuario", usuario);
		view.addObject("username", usuario.getUsername());
		view.addObject("nombreUsuario", usuario.getNombreCompleto());
		view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision(""));
		view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
		view.addObject("dependenciasUsuario",
				this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		view.addObject("edicion", Boolean.valueOf(false));
		view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
		view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
		view.addObject("correlativo", "");
		view.addObject("errores", errores);
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003908
		view.addObject("firmantes", new ArrayList());
		return view;
	}

	@GetMapping({ "/edicion/{idCorrespondencia}" })
	public ModelAndView edicionCorrespondencia(@PathVariable("idCorrespondencia") Long idCorrespondencia,
			Locale locale) {
		
		List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
		if(listParametro != null && listParametro.size() > 0) 
			this.tamanioMaxArchivoUploadSinFirmaDigital = listParametro.get(0).getValor().intValue();
		
		UsuarioPetroperu usuario = obtenerUsuario();
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("correspondencia_edicion");
		// TICKET 9000003943
		Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuestaPrevia = this.correspondenciaEmisionService
				.buscarCorrespondencia(idCorrespondencia, locale);
		if (respuestaPrevia.datos != null && respuestaPrevia.datos.size() > 0) {
			pe.com.petroperu.model.emision.Correspondencia correspondenciaPrevia = respuestaPrevia.datos.get(0);
			if(correspondenciaPrevia.isRutaAprobacion()){
				List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(correspondenciaPrevia)
						.stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden))
						.collect(Collectors.toList());
				if(aprobadores.size()>0){
					RutaAprobacion ultimoAprobador = aprobadores.get(aprobadores.size()-1);
					if (ultimoAprobador.getFirmante() != null 
							&& correspondenciaPrevia.getEstado().getId() == Constante.CORRESPONDENCIA_POR_CORREGIR) {
						ultimoAprobador.setFirmante(null);
						LOGGER.info("SistcorrController Linea 1173");
						rutaAprobacionDAO.save(ultimoAprobador);
					}
				} 
			}
		}
		// FIN TICKET
		Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuesta = this.correspondenciaEmisionService
				.buscarCorrespondencia(idCorrespondencia, locale);
		if (!respuesta.estado) {
			return new ModelAndView("redirect:/404.html");
		}
		// TICKET 9000003908
		List<Firmante> firmantes = this.firmanteService.obtenerFirmantes(idCorrespondencia);
		Integer nroFlujo = 0;
		for(Firmante _f : firmantes){
			if(_f.getNroFlujo() > nroFlujo){
				nroFlujo = _f.getNroFlujo();
			}
		}
		List<Firmante> ultimosFirmantes = new ArrayList();
		for(Firmante _f : firmantes){
			if(_f.getNroFlujo() == nroFlujo){
				ultimosFirmantes.add(_f);
			}
		}
		// FIN TICKET
		view.addObject("correspondencia", idCorrespondencia);

		view.addObject("dependenciasUsuario",
				this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.edicion", null, locale));
		view.addObject("usuario", usuario);
		view.addObject("username", usuario.getUsername());
		view.addObject("nombreUsuario", usuario.getNombreCompleto());
		view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision(""));
		view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
		view.addObject("edicion", Boolean.valueOf(true));
		view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
		view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
		view.addObject("correlativo", respuesta.datos.get(0).getCorrelativo().getCodigo());
		view.addObject("errores", errores);
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("firmantes", ultimosFirmantes);
		// TICKET 9000003943
		pe.com.petroperu.model.emision.Correspondencia c = correspondenciaDAO.findOne(idCorrespondencia);
		List<RutaAprobacion> aprobadores = rutaAprobacionService.obtenerRutaAprobacion(c).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
		view.addObject("rutaAprobacion", aprobadores);
		// FIN TICKET
		return view;
	}

	// INICIO TICKET 9000003996
	// @RequestBody DatosCompartirCorrespondencia datos
	@PostMapping(value = { "/actualizaflujo" }, produces = { "application/json" })
	public @ResponseBody List<Firmante> actualizaFlujo(@RequestBody Data data) {
		LOGGER.info("actualizaFlujo");
		LOGGER.info("flujoNombre " + data.getFlujoNombre());
		LOGGER.info("idCorrespondencia " + data.getIdCorrespondencia());

		List<Firmante> firmantes = this.firmanteService.obtenerFirmantesFlujo(Long.valueOf(data.getIdCorrespondencia()),
				Long.valueOf(data.getFlujoNombre()));
		LOGGER.info("firmantes " + firmantes.size());

		return firmantes;

	}
	// FIN TICKET 9000003996

	@GetMapping({ "/ver-detalle/{idCorrespondencia}" })
	public ModelAndView verDetalle(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		
		// TICKET 9000004962
		boolean esFiscalizador = false;
		String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		for (Object[] rol : roles) {
			LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			if("ROLE_FISCALIZADOR".equals(rol[3].toString())){
				LOGGER.info("GESTOR:" + rol[3].toString());
				esFiscalizador = true;
			}
		}
		// FIN 9000004962
		
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("correspondencia_detalle_firma");
		// TICKET 9000003780
		boolean permisoArchivos = false;
		List<Flujo> nombreFlujo = null;
		// FIN TICKET
		// TICKET 9000003943
		/*Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuestaPrevia = this.correspondenciaEmisionService.buscarCorrespondencia(idCorrespondencia, locale);
		if(respuestaPrevia.datos != null && respuestaPrevia.datos.size() > 0){
			pe.com.petroperu.model.emision.Correspondencia correspondenciaPrevia = respuestaPrevia.datos.get(0);
			if(correspondenciaPrevia.isRutaAprobacion()){
				List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(correspondenciaPrevia).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
				if(aprobadores.size()>0){
					RutaAprobacion ultimoAprobador = aprobadores.get(aprobadores.size()-1);
					if(ultimoAprobador.getFirmante()!=null && correspondenciaPrevia.getEstado().getId()==Constante.CORRESPONDENCIA_POR_CORREGIR){
						ultimoAprobador.setFirmante(null);
						rutaAprobacionDAO.save(ultimoAprobador);
					}
				} 
			}
		}*/
		// FIN TICKET
		Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuesta = this.correspondenciaEmisionService.buscarCorrespondencia(idCorrespondencia, locale);
		if (!respuesta.estado) {
			return new ModelAndView("redirect:/404.html");
		}
		// TICKET 9000003908
		List<Firmante> firmantes = this.firmanteService.obtenerFirmantes(idCorrespondencia);
		Integer nroFlujo = 0;
		for(Firmante _f : firmantes){
			if(_f.getNroFlujo() > nroFlujo){
				nroFlujo = _f.getNroFlujo();
			}
		}
		List<Firmante> ultimosFirmantes = new ArrayList();
		for(Firmante _f : firmantes){
			if(_f.getNroFlujo() == nroFlujo){
				ultimosFirmantes.add(_f);
				// TICKET 9000003780
				if(_f.getCodFirmante().toUpperCase().equalsIgnoreCase(usuario.getUsername().toUpperCase())){
					permisoArchivos = true;
				}
				// FIN TICKET 9000003780
			}
		}
		// FIN TICKET
		// TICKET 9000003780
		if(respuesta.datos.get(0).getResponsable().toUpperCase().equalsIgnoreCase(usuario.getUsername().toUpperCase())){
			permisoArchivos = true;
			view.addObject("esUserResponsable", "S");
		}
		// FIN TICKET

		// INICIO TICKET 9000003996
		nombreFlujo = correspondenciaEmisionService.nombreNumeroFlujos(idCorrespondencia);

		view.addObject("nombreFlujo", nombreFlujo);
		view.addObject("correspondencia", idCorrespondencia);
		// FIN TICKET 9000003996
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.ver_detalle", null, locale));
		view.addObject("usuario", usuario);
		view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision(""));
		view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
		view.addObject("firmantes", ultimosFirmantes);
		view.addObject("errores", errores);
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003791
		LOGGER.info("Correspondencia:" + respuesta.datos.size());
		if(respuesta.datos.size() > 0){
			LOGGER.info("Correspondencia:" + respuesta.datos.get(0).isConfidencial());
			LOGGER.info("Correspondencia:" + respuesta.datos.get(0).getEstado().getId());
			LOGGER.info("Correspondencia:" + respuesta.datos.get(0).getEstado().getDescripcionEstado());
			LOGGER.info("Correspondencia:" + respuesta.datos.get(0).getEstado().getEstado());
			LOGGER.info("Correspondencia:" + respuesta.datos.get(0).getEstado().getBandeja());
		}
		// TICKET 9000003780
		if(!respuesta.datos.get(0).isConfidencial()){
			permisoArchivos = true;
		}
		ItemFilenet funcionario = this.filenetService.obtenerFirmante(respuesta.datos.get(0).getCodDependencia());
		if(usuario.getUsername().toUpperCase().equalsIgnoreCase(funcionario.getCodigo())){
			permisoArchivos = true;
		}
		// FIN TICKET 9000003780
		
		// TICKET 9000004962
		if (esFiscalizador){
			permisoArchivos = true;
		}
		// FIN TICKET 9000004962
		
		boolean compartir = false;
		compartir = esCompartirCorrespondencia(respuesta.datos.get(0), usuario);
		view.addObject("permiso", permisoArchivos?"1":"0");
		view.addObject("compartir", compartir);
		// FIN TICKET 9000003791
		// TICKET 9000003943
		List<RutaAprobacion> aprobadores = new ArrayList<RutaAprobacion>();
		LOGGER.info("Correspondencia:" + respuesta.datos.get(0).getId());
		aprobadores = rutaAprobacionService.obtenerRutaAprobacion(respuesta.datos.get(0)).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
		view.addObject("rutaAprobacion", aprobadores);
		// FIN TICKET
		// TICKET 900003997
		List<ItemFilenet> nroEnvios = new ArrayList<>();
		pe.com.petroperu.model.emision.Correspondencia corr = respuesta.datos.get(0);
		for(int i=1;i<=corr.getNroEnvio();i++){
			ItemFilenet item = new ItemFilenet();
			item.setCodigo(String.valueOf(i));
			item.setDescripcion(String.valueOf(i));
			nroEnvios.add(item);
		}
		LOGGER.info("Nro Envios:" + nroEnvios.size());
		view.addObject("nroEnvios", nroEnvios);
		// FIN TICKET
		// TICKET 9000004272
		boolean permitidoFirma = false;
		if(corr.getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA){
			Integer nroFlujo2 = 0;
			for(Firmante _f : firmantes){
				if(_f.getNroFlujo() > nroFlujo2){
					nroFlujo2 = _f.getNroFlujo();
				}
			}
			List<Firmante> ultimosFirmantes2 = new ArrayList();
			for(Firmante _f : firmantes){
				if(_f.getNroFlujo() == nroFlujo){
					ultimosFirmantes2.add(_f);
					// TICKET 9000003780
					if(_f.getCodFirmante().toUpperCase().equalsIgnoreCase(usuario.getUsername().toUpperCase()) && _f.getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA){
						permitidoFirma = true;
					}
					// FIN TICKET 9000003780
				}
			}
		}
		view.addObject("permitidoFirma", permitidoFirma);
		// FIN TICKET
		return view;
	}
	
	// TICKET 9000003791
	@GetMapping({ "/compartir/{idCorrespondencia}/{ids}/{modo}" })
	public ModelAndView compartirCorrespondencia(@PathVariable("idCorrespondencia") Long idCorrespondencia, @PathVariable("ids") String ids, @PathVariable("modo") String modo, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] compartirCorrespondencia");
		UsuarioPetroperu usuario = obtenerUsuario();
		List<String> errores = new ArrayList<>();
		String[] idArchivosAdjuntos = ids.split(",");
		List<ArchivoAdjunto> archivos = new ArrayList<>();
		for(int i=0;i<idArchivosAdjuntos.length;i++){
			ArchivoAdjunto adj = correspondenciaEmisionService.buscarArchivoAdjunto(Long.valueOf(idArchivosAdjuntos[i]), locale).datos.get(0);
			archivos.add(adj);
		}
		
		ModelAndView view = new ModelAndView("compartir_correspondencia");
		Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuesta = this.correspondenciaEmisionService.buscarCorrespondencia(idCorrespondencia, locale);
		if (!respuesta.estado) {
			return new ModelAndView("redirect:/404.html");
		}
		
		String modoTexto = "";
		if(modo.equalsIgnoreCase("DIR")){
			modoTexto = "Dirección";
		}else if(modo.equalsIgnoreCase("ADJ")){
			modoTexto = "Adjunto";
		}
		
		Respuesta<ArchivoAdjunto> respuestaArchivos = new Respuesta<ArchivoAdjunto>();
		respuestaArchivos.estado = true;
		respuestaArchivos.mensaje = "ABC";
		respuestaArchivos.datos = archivos;
		
		view.addObject("correspondencia", idCorrespondencia);
		view.addObject("tarea", respuesta);

		view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.compartir", null, locale));
		view.addObject("archivos", archivos);
		view.addObject("respuestaArchivos", respuestaArchivos);
		view.addObject("modo", modo);
		view.addObject("modoTexto", modoTexto);
		view.addObject("idCorrespondencia", idCorrespondencia);
		view.addObject("correlativo", respuesta.datos.get(0).getCorrelativo().getCodigo());
		view.addObject("ids", ids);
		
		view.addObject("usuario", usuario);
		view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision(""));
		view.addObject("firmantes", this.firmanteService.obtenerFirmantes(idCorrespondencia));
		view.addObject("errores", errores);
		view.addObject("urlCompartir", apiUrlCompartirCorrespondencia);
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003992
		this.LOGGER.info("[FIN] compartirCorrespondencia");
		return view;
	}
	
	@GetMapping(value = { "/obtener-archivos/{ids}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ArchivoAdjunto>> clavesPorCorrespondencia(@PathVariable("ids") String ids,
			Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] clavesPorCorrespondencia");
		Respuesta<ArchivoAdjunto> respuesta = new Respuesta<>();
		try {
			List<ArchivoAdjunto> lista = new ArrayList<>();
			String[] _ids = ids.split(",");
			for(String _id : _ids){
				ArchivoAdjunto adj = correspondenciaEmisionService.buscarArchivoAdjunto(Long.valueOf(_id), locale).datos.get(0);
				ArchivoCompartido arc = archivoCompartidoDAO.findOneByArchivo(adj);
				if(arc != null){
					adj.setKey(arc.getClave());
				}else{
					//adj.setKey(generateKey());
				}
				LOGGER.info("Key generado:" + adj.getKey());
				lista.add(adj);
			}
			respuesta.estado = true;
			respuesta.mensaje = "Archivos recuperados correctamente";
			respuesta.datos = lista;
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			respuesta.datos = new ArrayList<>();
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] clavesPorCorrespondencia");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping(value = { "/compartir-correspondencia" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<String>> compartirCorrespondencia(@RequestBody DatosCompartirCorrespondencia datos, Locale locale){
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] compartirCorrespondencia");
		Respuesta<String> respuesta = new Respuesta<>();
		try{
			List<HistorialArchivo> historial = new ArrayList<>();
			UsuarioPetroperu usuario = obtenerUsuario();
			LOGGER.info("Correspondencia:" + datos.getCorrespondencia());
			LOGGER.info("Archivos:" + datos.getArchivos().size());
			pe.com.petroperu.model.emision.Correspondencia cor = new pe.com.petroperu.model.emision.Correspondencia();
			cor = correspondenciaDAO.findOne(datos.getCorrespondencia().getIdCorrespondencia());
			CorrespondenciaCompartida corresComp = new CorrespondenciaCompartida();
			corresComp.setAsunto(datos.getCorrespondencia().getAsunto());
			corresComp.setContenido(datos.getCorrespondencia().getContenido());
			corresComp.setDestinatarios(datos.getCorrespondencia().getDestinatarios());
			corresComp.setCopias(datos.getCorrespondencia().getCopias());
			corresComp.setModoCompartido(datos.getCorrespondencia().getModoCompartido());
			corresComp.setCorrespondencia(cor);
			corresComp.setUsuarioCrea(usuario.getUsername());
			corresComp.setFechaCrea(new Date());
			corresComp = (CorrespondenciaCompartida) correspondenciaCompartidaDAO.save(corresComp);
			LOGGER.info("Id Correspondencia Compartida:" + corresComp.getId());
			for(ArchivoAdjunto adj : datos.getArchivos()){
				LOGGER.info("Id Arc Adj:" + adj.getId());
				ArchivoAdjunto arcAdj = archivoAdjuntoDAO.findOne(adj.getId());
				ArchivoCompartido arc = archivoCompartidoDAO.findOneByArchivo(arcAdj);
				/*if(arc == null){
					ArchivoCompartido arcCom = new ArchivoCompartido();
					arcCom.setClave(adj.getKey());
					arcCom.setUsuarioCrea(usuario.getUsername());
					arcCom.setFechaCrea(new Date());
					arcCom.setArchivo(arcAdj);
					arc = (ArchivoCompartido) archivoCompartidoDAO.save(arcCom);
					String pathTo = urlBase + DIRECTORIO_COMPARTIDOS + "/" + adj.getNombreServidor();
					PDFTool pdf = new PDFTool(arcAdj.getUbicacion(), pathTo, apiUrlCompartirCorrespondencia, adj.getKey(), arcAdj.getCorrespondencia().getCorrelativo().getCodigo());
					pdf.writePDF();
				}*/
				if(!arc.isCompartido()){
					arc.setCompartido(true);
					archivoCompartidoDAO.save(arc);
				}
				HistorialArchivo hist = new HistorialArchivo();
				hist.setCorrespondenciaCompartida(corresComp);
				hist.setArchivoCompartido(arc);
				hist.setUsuarioCrea(usuario.getUsername());
				hist.setFechaCrea(new Date());
				hist = (HistorialArchivo) historialArchivoDAO.save(hist);
				historial.add(hist);
				// TICKET 9000004510
				if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(adj.getIndicadorRemoto())){
					String urlCarpetaArchivos = "adjuntos";
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + adj.getNombreServidor();
					ResponseEntity<byte[]> archivo = sistcorrCliente.descargarDocumentoServidor(usuario.getToken(), adj.getNombreServidor());
					FileUtils.writeByteArrayToFile(new File(nuevaUrl), archivo.getBody());
				}
				// FIN TICKET
			}
			boolean estado = notificacionService.notificarHistorialCorrespondencia(historial, usuario, locale);
			if(estado){
				respuesta.estado = true;
				respuesta.mensaje = "Correspondencia compartida correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = "Ocurrió un error al compartir correspondencia";
			}
			// TICKET 9000004510
			for(ArchivoAdjunto adj : datos.getArchivos()){
				if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(adj.getIndicadorRemoto())){
					String urlCarpetaArchivos = "adjuntos";
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + adj.getNombreServidor();
					File temp = new File(nuevaUrl);
					temp.delete();
				}
			}
			// FIN TICKET
		}catch(Exception e){
			e.printStackTrace();
			respuesta.estado = false;
			respuesta.mensaje = "No se pudo compartir la correspondencia";
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] compartirCorrespondencia");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping({ "/historial-compartido/{idCorrespondencia}" })
	public ModelAndView historialCompartido(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
		//UsuarioPetroperu usuario = obtenerUsuario();
		ModelAndView view = new ModelAndView("historial_compartido");
		pe.com.petroperu.model.emision.Correspondencia corr = (pe.com.petroperu.model.emision.Correspondencia) correspondenciaDAO.findOne(idCorrespondencia);
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.historial", null, locale));
		view.addObject("correlativo", corr.getCorrelativo().getCodigo());
		view.addObject("idCorrespondencia", idCorrespondencia);
		return view;
	}
	
	@PostMapping(value = { "/obtener-historial/{id}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<CorrespondenciaCompartidaDTO>> obtenerHistorialCorrespondencia(@PathVariable("id") Long id, @RequestBody FiltroConsultaHistorial filtro,
			Locale locale) {
		Respuesta<CorrespondenciaCompartidaDTO> respuesta = new Respuesta<>();
		try {
			List<CorrespondenciaCompartidaDTO> lista = new ArrayList<>();
			List<Object[]> historial = historialArchivoDAO.obtenerHistorialCompartidoCorrespondencia(id, filtro.getFechaDesde(), filtro.getFechaHasta(), filtro.getValorBuscar());
			for(Object[] comp : historial){
				CorrespondenciaCompartidaDTO corr = new CorrespondenciaCompartidaDTO();
				corr.setFecha(String.valueOf(comp[0]));
				corr.setHora(String.valueOf(comp[1]));
				corr.setModoCompartido(String.valueOf(comp[2]));
				corr.setCompartidoPor(String.valueOf(comp[3]));
				corr.setDestinatarios(String.valueOf(comp[4]));
				corr.setCopias(String.valueOf(comp[5]));
				corr.setAsunto(String.valueOf(comp[6]));
				corr.setContenido(String.valueOf(comp[7]).replaceAll("\\n", "<br>"));
				corr.setArchivos(String.valueOf(comp[8]));
				lista.add(corr);
			}
			respuesta.estado = true;
			respuesta.mensaje = "Consulta realizada correctamente";
			respuesta.datos = lista;
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			respuesta.datos = new ArrayList<>();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/obtener-historial-excel/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelHistorial(@PathVariable("id") Long id,
			@RequestBody FiltroConsultaHistorial filtro, Locale locale) {
		LOGGER.info("[INICIO] exportExcelHistorial");
		UsuarioPetroperu usuario = obtenerUsuario();
		pe.com.petroperu.model.emision.Correspondencia corr = (pe.com.petroperu.model.emision.Correspondencia) correspondenciaDAO
				.findOne(id);
		Respuesta<ByteArrayInputStream> respuesta = emisionService.consultarHistorialExcel(id, filtro,
				usuario.getNombreCompleto(), corr, locale);
		if(respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=historial.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	@GetMapping(value="/descargar-archivos-correspondencia/{ids}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> obtenerCorrespondenciaCompartidaImpresion(@PathVariable("ids") String ids, Locale locale){
		UsuarioPetroperu usuario = obtenerUsuario();
		Respuesta<ByteArrayOutputStream> respuesta = new Respuesta<>();
		try{
			String[] _ids = ids.split(",");

			LOGGER.info("descargar-archivos-correspondencia");

			pe.com.petroperu.model.emision.Correspondencia cor = new pe.com.petroperu.model.emision.Correspondencia();
			ArchivoAdjunto aa_ = archivoAdjuntoDAO.findOne(Long.valueOf(_ids[0]));
			cor = correspondenciaDAO.findOne(aa_.getCorrespondencia().getId());
			CorrespondenciaCompartida corresComp = new CorrespondenciaCompartida();
			corresComp.setAsunto("");
			corresComp.setContenido("");
			corresComp.setDestinatarios("");
			corresComp.setCopias("");
			corresComp.setModoCompartido("Impresión");
			corresComp.setCorrespondencia(cor);
			corresComp.setUsuarioCrea(usuario.getUsername());
			corresComp.setFechaCrea(new Date());
			corresComp = (CorrespondenciaCompartida) correspondenciaCompartidaDAO.save(corresComp);
			
			// TICKET 9000004510
			List<HistorialArchivo> historial = new ArrayList<>();
			List<String> nombresFilenet = new ArrayList<>();
			List<String> nombresArchivos = new ArrayList<>();
			List<String> indicadoresRemoto = new ArrayList<>();
			int contLocal = 0;
			// FIN TICKET
			for(int i=0;i<_ids.length;i++){
				String id = _ids[i];
				ArchivoAdjunto aa = archivoAdjuntoDAO.findOne(Long.valueOf(id));
				ArchivoCompartido ac = archivoCompartidoDAO.findOneByArchivo(aa);
				/*if(ac==null){
					ArchivoCompartido arcCom = new ArchivoCompartido();
					arcCom.setClave(generateKey());
					arcCom.setUsuarioCrea(usuario.getUsername());
					arcCom.setFechaCrea(new Date());
					arcCom.setArchivo(aa);
					ac = (ArchivoCompartido) archivoCompartidoDAO.save(arcCom);
					String pathTo = urlBase + DIRECTORIO_COMPARTIDOS + "/" + aa.getNombreServidor();
					PDFTool pdf = new PDFTool(aa.getUbicacion(), pathTo, apiUrlCompartirCorrespondencia, arcCom.getClave(), aa.getCorrespondencia().getCorrelativo().getCodigo());
					pdf.writePDF();
				}*/
				if(!ac.isCompartido()){
					ac.setCompartido(true);
					archivoCompartidoDAO.save(ac);
				}
				
				HistorialArchivo hist = new HistorialArchivo();
				hist.setCorrespondenciaCompartida(corresComp);
				hist.setArchivoCompartido(ac);
				hist.setUsuarioCrea(usuario.getUsername());
				hist.setFechaCrea(new Date());
				hist = (HistorialArchivo) historialArchivoDAO.save(hist);
				historial.add(hist);
				// TICKET 9000004510
				nombresFilenet.add(aa.getNombreServidor());
				nombresArchivos.add(aa.getNombre());
				indicadoresRemoto.add(aa.getIndicadorRemoto());
				if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
					contLocal++;
				}
				// FIN TICKET
			}
			
			String[] pathSource = new String[contLocal];
			String[] nameSource = new String[contLocal];
			// TICKET 9000004510
			List<byte[]> archivos = new ArrayList<>();
			// FIN TICKET
			for(int i=0;i<historial.size();i++){
				//pathSource[i] = historial.get(i).getArchivoCompartido().getArchivo().getUbicacion();
				/*File source = new File(urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(i).getArchivoCompartido().getArchivo().getNombreServidor());
				File dest = new File(urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(i).getArchivoCompartido().getArchivo().getNombre());
				FileInputStream fis = new FileInputStream(source);
				FileOutputStream fos = new FileOutputStream(dest);
				byte[] buffer = new byte[1024];
				int length;
				while((length = fis.read(buffer)) > 0){
					fos.write(buffer, 0, length);
				}*/
				// TICKET 9000004510
				//pathSource[i] = urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(i).getArchivoCompartido().getArchivo().getNombreServidor();
				//nameSource[i] = historial.get(i).getArchivoCompartido().getArchivo().getNombre();
				if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(indicadoresRemoto.get(i))){
					pathSource[i] = urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(i).getArchivoCompartido().getArchivo().getNombreServidor();
					nameSource[i] = historial.get(i).getArchivoCompartido().getArchivo().getNombre();
				}else{
					ResponseEntity<byte[]> archivo = sistcorrCliente.descargarDocumentoServidor(usuario.getToken(), nombresFilenet.get(i));
					archivos.add(archivo.getBody());
				}
				// FIN TICKET
				//pathSource[i] = urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(i).getArchivoCompartido().getArchivo().getNombre();
			}
			String pathTo = urlBase + DIRECTORIO_ADJUNTOS + "/" + DIRECTORIO_ADJUNTOS_ZIP + "/" + historial.get(0).getCorrespondenciaCompartida().getCorrespondencia().getCorrelativo().getCodigo() + ".zip";
			// TICKET 9000004510
			//ZipTool zip = new ZipTool(nameSource, pathSource, pathTo);
			//zip.zip2();
			ZipTool zip = new ZipTool(nameSource, pathSource, pathTo, nombresArchivos, archivos);
			zip.zip3();
			// FIN TICKET
			/*for(int i=0;i<historial.size();i++){
				File temp = new File(urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(i).getArchivoCompartido().getArchivo().getNombre());
				LOGGER.info("Eliminado:" + temp.delete() + "||" + urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(i).getArchivoCompartido().getArchivo().getNombre());
			}*/
			
			HttpHeaders headers = new HttpHeaders();
			if(historial.size()==1){
				//pathTo = urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(0).getArchivoCompartido().getArchivo().getNombreServidor();
				if(contLocal == 1){
					headers.add("Content-Disposition", "inline; filename=\"" + historial.get(0).getArchivoCompartido().getArchivo().getNombre() + "\"");
					pathTo = urlBase + DIRECTORIO_ADJUNTOS + "/" + historial.get(0).getArchivoCompartido().getArchivo().getNombreServidor();
					File file = new File(pathTo);
					File file2 = new File(pathTo);
					FileInputStream fis = new FileInputStream(file);
					FileInputStream fis2 = new FileInputStream(file2);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int length;
					byte[] buffer = new byte[4096];
					while((length = fis.read(buffer)) > 0){
						bos.write(buffer, 0, length);
					}
					fis.close();
					bos.flush();
					bos.close();
					respuesta.datos.add(bos);
					return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
							.body(new InputStreamResource(fis2));
				}else{
					//Inicio ticket 9000004944
				 	Utilitario utilitario = new Utilitario();
			    	        String nombreArchivo = utilitario.nombreArchivoDescarga(historial.get(0).getArchivoCompartido().getArchivo().getNombre());
					headers.add("Content-Disposition", "inline; filename=\"" + nombreArchivo + "\"");
					//fin 9000004944
					java.io.InputStream res = new ByteArrayInputStream(archivos.get(0));
					return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
							.body(new InputStreamResource(res));
				}
			}else{
				headers.add("Content-Disposition", "inline; filename=" + historial.get(0).getCorrespondenciaCompartida().getCorrespondencia().getCorrelativo().getCodigo() + ".zip");
				File file = new File(pathTo);
				File file2 = new File(pathTo);
				FileInputStream fis = new FileInputStream(file);
				FileInputStream fis2 = new FileInputStream(file2);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int length;
				byte[] buffer = new byte[4096];
				while((length = fis.read(buffer)) > 0){
					bos.write(buffer, 0, length);
				}
				fis.close();
				bos.flush();
				bos.close();
				respuesta.datos.add(bos);
				return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
						.body(new InputStreamResource(fis2));
			}
			
			//return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(respuesta.datos.get(0)));
			/*File file = new File(pathTo);
			File file2 = new File(pathTo);
			FileInputStream fis = new FileInputStream(file);
			FileInputStream fis2 = new FileInputStream(file2);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int length;
			byte[] buffer = new byte[4096];
			while((length = fis.read(buffer)) > 0){
				bos.write(buffer, 0, length);
			}
			fis.close();
			bos.flush();
			bos.close();
			respuesta.datos.add(bos);
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new InputStreamResource(fis2));*/
			//return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean esCompartirCorrespondencia(pe.com.petroperu.model.emision.Correspondencia correspondencia, UsuarioPetroperu usuario){
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] esCompartirCorrespondencia");
		boolean compartir = false;
		//if((correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_COMPLETADA) ||
		if((correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_APROBADA) || 
			correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ENVIADA)) && 
			correspondencia.isFirmaDigital()){
			if(!correspondencia.isConfidencial()){
				compartir = true;
			}else{
				ItemFilenet funcionario = this.filenetService.obtenerFirmante(correspondencia.getCodDependencia());
				if(usuario.getUsername().toUpperCase().equalsIgnoreCase(funcionario.getCodigo())){
					compartir = true;
				}
				//if(correspondencia.getResponsable().equals(usuario.getUsername()) || 
				//	correspondencia.getRemitente().equals(usuario.getUsername())){
				if(correspondencia.getResponsable().equals(usuario.getUsername())){
					compartir = true;
				}else{
					List<Firmante> lista = firmanteDAO.obtenerFirmantes(correspondencia.getId());
					int nroFlujo = 0;
					for(int i=0;i<lista.size();i++){
						if(lista.get(i).getNroFlujo()>nroFlujo){
							nroFlujo = lista.get(i).getNroFlujo();
						}
					}
					for(int i=0;i<lista.size();i++){
						if(usuario.getUsername().equals(lista.get(i).getCodFirmante()) && nroFlujo==lista.get(i).getNroFlujo()){
							compartir = true;
						}
					}
				}
			}
		}
		if(compartir){
			LOGGER.info("Correspondencia: " + correspondencia.getCorrelativo().getCodigo());
			for(ArchivoAdjunto aa : correspondencia.getAdjuntos()){
				LOGGER.info("Archivo Adjunto: " + aa.getNombre() + "||" + aa.isPrincipal());
				if(aa.isPrincipal()){
					ArchivoCompartido ac = archivoCompartidoDAO.findOneByArchivo(aa);
					if(ac==null){
						compartir = false;
					}
				}
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] esCompartirCorrespondencia");
		return compartir;
	}
	// FIN TICKET 9000003791

	@GetMapping({ "/asignacion-firma/{idCorrespondencia}" })
	public ModelAndView asignacionFirma(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] asignacionFirma");
		UsuarioPetroperu usuario = obtenerUsuario();
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("correspondencia_asignacion_firma");
		Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuesta = this.correspondenciaEmisionService
				.buscarCorrespondencia(idCorrespondencia, locale);
		if (!respuesta.estado) {
			return new ModelAndView("redirect:/404.html");
		}
		pe.com.petroperu.model.emision.Correspondencia corr = respuesta.datos.get(0);
		if(!correspondenciaEmisionService.validarSiPuedeAsignarFirma(corr, usuario.getUsername()))
			return new ModelAndView("redirect:/401");
		corr.setDetalleCopia(new ArrayList<>());
		corr.setDetalleExterno(new ArrayList<>());
		corr.setDetalleInterno(new ArrayList<>());
		view.addObject("correspondencia", idCorrespondencia);
		view.addObject("correlativo", corr.getCorrelativo().getCodigo());
		view.addObject("asunto", corr.getAsunto());

		view.addObject("titulo",
				this.messageSource.getMessage("sistcorr.correspondencia.asignacion_firma", null, locale));
		view.addObject("errores", errores);
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003992
		this.LOGGER.info("[FIN] asignacionFirma");
		return view;
	}

	@GetMapping({ "/lista-documentos/{asignacion}" })
	public ModelAndView firma(@PathVariable("asignacion") String asignacion,
			@RequestParam(required = false) String correspondencia,
			@RequestParam(required = false, defaultValue = "false") boolean declinados,
			@RequestParam(required = false, defaultValue = "false") boolean rechazados,
			@RequestParam(required = false, defaultValue = "true") boolean misPendientes,  Locale locale) {
		this.LOGGER.info("[INICIO] firma");
		if(asignacion.equals("pendientes")){
			misPendientes = false;
		}
		UsuarioPetroperu usuario = obtenerUsuario();
		// TICKET 7000003969
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();
		List<Menu> listaMenu = new ArrayList<>();
		List<MotivoRechazo> listaMotivos = new ArrayList<>();
		List<MotivoRechazo> listaMotivosResponsable = new ArrayList<>();
		List<MotivoRechazo> listaMotivosFirmado = new ArrayList<>();
		List<ItemFilenet> listaDependenciasGestor = new ArrayList<>();
		List<ItemFilenet> listaDependenciasBandES = new ArrayList<>();
		List<Rol> listaRoles = new ArrayList<>();
		Object menuSession = session.getAttribute(Constante.SESSION_MENU);
		if(menuSession == null){
			List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
			session.setAttribute(Constante.SESSION_MENU, menu);
			listaMenu = menu;
		}else{
			listaMenu = (List<Menu>) menuSession;
		}
		Object motivosSession = session.getAttribute(Constante.SESSION_MOTIVOS_RECHAZO);
		if(motivosSession == null){
			List<MotivoRechazo> motivos = this.motivoService.listarTodos();
			session.setAttribute(Constante.SESSION_MOTIVOS_RECHAZO, motivos);
			listaMotivos = motivos;
		}else{
			listaMotivos = (List<MotivoRechazo>) motivosSession;
		}
		Object motivosResponsableSession = session.getAttribute(Constante.SESSION_MOTIVOS_RECHAZO_RESPONSABLE);
		if(motivosResponsableSession == null){
			List<MotivoRechazo> motivosResponsable = this.motivoService.listarRechazoResponsable();
			session.setAttribute(Constante.SESSION_MOTIVOS_RECHAZO_RESPONSABLE, motivosResponsable);
			listaMotivosResponsable = motivosResponsable;
		}else{
			listaMotivosResponsable = (List<MotivoRechazo>) motivosResponsableSession;
		}
		Object motivosFirmadoSession = session.getAttribute(Constante.SESSION_MOTIVOS_RECHAZO_FIRMADO);
		if(motivosFirmadoSession == null){
			List<MotivoRechazo> motivosFirmado = this.motivoService.listarRechazoFirmado();
			session.setAttribute(Constante.SESSION_MOTIVOS_RECHAZO_FIRMADO, motivosFirmado);
			listaMotivosFirmado = motivosFirmado;
		}else{
			listaMotivosFirmado = (List<MotivoRechazo>) motivosFirmadoSession;
		}
		Object dependenciaSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_GESTOR);
		if(dependenciaSession == null){
			List<ItemFilenet> dependenciasGestor = filenetService.obtenerDependenciasGestor(usuario.getUsername());
			session.setAttribute(Constante.SESSION_DEPENDENCIAS_GESTOR, dependenciasGestor);
			listaDependenciasGestor = dependenciasGestor;
		}else{
			listaDependenciasGestor = (List<ItemFilenet>) dependenciaSession;
		}
		//inicio tikcet 9000003866
		Object dependenciaBandESSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_BES);
		if(dependenciaBandESSession == null){
			List<ItemFilenet> dependenciasBandES = filenetService.obtenerDependenciasBandES(usuario.getUsername());
			session.setAttribute(Constante.SESSION_DEPENDENCIAS_BES, dependenciasBandES);
			listaDependenciasBandES = dependenciasBandES;
		}else{
			listaDependenciasBandES = (List<ItemFilenet>) dependenciaBandESSession;
		}
		//fin tikcet 9000003866
		Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if(rolSession == null){
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			listaRoles = rolesBD;
		}else{
			listaRoles = (List<Rol>) rolSession;
		}
		// FIN TICKET
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("lista_documentos");
		view.addObject("usuario", usuario);
		//view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("listaMenu", listaMenu);
		view.addObject("errores", errores);
		view.addObject("solicitante", usuario.getUsername());
		//view.addObject("motivos_rechazo", this.motivoService.listarTodos());
		view.addObject("motivos_rechazo", listaMotivos);
		//view.addObject("motivos_rechazo_responsable", this.motivoService.listarRechazoResponsable());
		view.addObject("motivos_rechazo_responsable", listaMotivosResponsable);
		//view.addObject("motivos_rechazo_firmado", this.motivoService.listarRechazoFirmado());
		view.addObject("motivos_rechazo_firmado", listaMotivosFirmado);
		view.addObject("crearCorrespondencia", Boolean.valueOf(true));
		view.addObject("filtro_correlativo", (correspondencia == null) ? "" : correspondencia);
		view.addObject("declinados", Boolean.valueOf(declinados));
		view.addObject("misPendientes", Boolean.valueOf(misPendientes));
		view.addObject("rechazados", Boolean.valueOf(rechazados));
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003994
		//view.addObject("dependencias", filenetService.obtenerDependenciasGestor(usuario.getUsername()));
		view.addObject("dependencias", listaDependenciasGestor);
		view.addObject("dependenciasBandES", listaDependenciasBandES);//ticket 9000003866
		//view.addObject("esJefe", tieneRol(usuario, RolAD.JEFE));
		//view.addObject("esGestor", tieneRol(usuario, RolAD.GESTOR));
		view.addObject("esJefe", tieneRolSession(listaRoles, RolAD.JEFE));
		view.addObject("esGestor", tieneRolSession(listaRoles, RolAD.GESTOR));
		// FIN TICKET
		view.addObject("tiposCorrespondencia", new ArrayList<ItemTipoCorrespondencia>());//ticket 9000003866
		
		switch (asignacion) {
		case "pendientes":
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.firmas.pendientes.titulo", null, locale));
			view.addObject("url", SistcorrEstado.SIN_ASIGNACION.URL);
			view.addObject("bandeja", "pendiente");
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision(""));//ticket 9000003866
			view.addObject("nuevo_documento", Boolean.valueOf(true));
			break;
		case "enviados":
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.firmas.enviados.titulo", null, locale));
			view.addObject("url", SistcorrEstado.ENVIADO.URL);
			view.addObject("bandeja", "enviado");
			view.addObject("nuevo_documento", Boolean.valueOf(false));
			break;
		case "firmados":
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.firmas.firmados.titulo", null, locale));
			view.addObject("url", SistcorrEstado.FIRMADO.URL);
			view.addObject("bandeja", "firmado");
			view.addObject("nuevo_documento", Boolean.valueOf(false));
			break;
		default:
			view = new ModelAndView("redirect:/404.html");
			break;
		}
		this.LOGGER.info("[FIN] firma");
		return view;
	}

	@GetMapping({ "/emision-firma/respuesta" })
	public ModelAndView resultadoFirma(@RequestParam(value = "res", required = true) boolean respuesta,
			@RequestParam(value = "extra1", required = false, defaultValue = "0") String idCorrespondencia,
			@RequestParam(value = "extra2", required = false, defaultValue = "0") String directorio, Locale locale) {
		this.LOGGER.info("[INICIO] resultadoFirma " + respuesta);
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null) {
			return new ModelAndView("redirect:/403.html");
		}
		ModelAndView view = new ModelAndView("respuesta_firma");
		view.addObject("titulo", "Resultado de firma");
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		if (respuesta) {
			Long correspondencia = Long.valueOf(Long.parseLong(idCorrespondencia));
			pe.com.petroperu.model.emision.Correspondencia corr = correspondenciaDAO.findOne(correspondencia);
			int cantidad = corr.getDetalleInterno().size() + corr.getDetalleExterno().size();
			int cantidadInternos = corr.getDetalleInterno().size();
			LOGGER.info("CANTIDAD DE DESTINATARIOS:" + cantidad);
			Respuesta<Firmante> respuestaFirmante = this.emisionService.firmarCorrespondencia(correspondencia,
					usuario.getUsername(), directorio, locale);
			if (!respuestaFirmante.estado) {
				view.addObject("error", Boolean.valueOf(true));
			} else {
				view.addObject("error", Boolean.valueOf(false));
				if(respuestaFirmante.datos!=null){
					view.addObject("firmante", respuestaFirmante.datos.get(0));
					//view.addObject("estado", ((Firmante) respuestaFirmante.datos.get(0)).getEstado().getId());
				}
				view.addObject("estado", corr.getEstado().getId());
				view.addObject("rutaAprobacion", (corr.isRutaAprobacion())?1:0);
				view.addObject("cantidad", cantidad);
				view.addObject("cantidadInternos", cantidadInternos);
				view.addObject("idCorrespondencia", idCorrespondencia);
			}
			view.addObject("mensaje", respuestaFirmante.mensaje);
			// TICKET 9000003943
			if(corr.isRutaAprobacion() && respuestaFirmante.estado && respuestaFirmante.datos != null){
				List<RutaAprobacion> aprobadores = rutaAprobacionService.obtenerRutaAprobacion(corr).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
				Firmante f = new Firmante();
				boolean encontrado = false;
				boolean registrado = false;
				String sigFirmante = "";
				for(RutaAprobacion ra : aprobadores){
					if(ra.getFirmante()==null){
						if(!encontrado){
							LOGGER.info("Ruta Aprobacion:" + ra.getId());
							f.setNroFlujo(corr.getNroFlujo());
							ItemFilenet lugar = new ItemFilenet();
							if("1".equalsIgnoreCase(ra.getTipoFirmante())){
								ItemFilenet funcionario = this.filenetService.obtenerFirmante(ra.getCodDependencia());
								if(funcionario!=null){
									ra.setUsuario(funcionario.getCodigo().toLowerCase());
									ra.setUsuarioNombre(funcionario.getDescripcion());
								}
								lugar = this.filenetService.obtenerLugarPorDependencia(ra.getCodDependencia());
							}
							if("2".equalsIgnoreCase(ra.getTipoFirmante())){
								ra.setUsuario(ra.getUsuario().toLowerCase());
								List<ItemFilenet> listaDependenciasDestino = filenetService.obtenerDependenciaPorUsuario(ra.getUsuario(), "");
								//ra.setCodDependencia(listaDependenciasDestino.get(0).getCodigo());
								//ra.setDependenciaNombre(listaDependenciasDestino.get(0).getDescripcion());
								lugar = this.filenetService.obtenerLugarPorDependencia(listaDependenciasDestino.get(0).getCodigo());
							}
							f.setCodFirmante(ra.getUsuario());
							f.setNombreFirmante(ra.getUsuarioNombre());
							f.setCodDependenciaFirmante(ra.getCodDependencia());
							f.setDependenciaFirmante(ra.getDependenciaNombre());
							f.setCodLugarTrabajoFirmante(lugar.getCodigo());
							f.setLugarTrabajoFirmante(lugar.getDescripcion());
							if("".equalsIgnoreCase(f.getCodFirmante())){
								//view.addObject("error", Boolean.valueOf(false));
								view.addObject("mensaje",
										this.messageSource.getMessage("sistcorr.siguiente_asignacion_ruta_aprobacion.error", null, locale));
							}else{
								Respuesta<Firmante> respuestaFirmanteRuta = this.emisionService.asignarFirmante(corr.getId(), f, usuario.getUsername(), locale);
								ra.setFirmante(respuestaFirmanteRuta.datos.get(0));
								ra.setFechaModifica(new Date());
								ra.setUsuarioModifica(usuario.getUsername());
								LOGGER.info("SistcorrController Linea 2128");
								rutaAprobacionDAO.save(ra);
								registrado = true;
								// TCKT 9000003997 - DEFECTO
								//sigFirmante = ra.getUsuario();
								sigFirmante = ra.getUsuarioNombre();
								// FIN TCKT
								//INICIO TICKET 9000004714
							    if (respuestaFirmanteRuta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA) {
							  	  this.emisionService.registrarDatosFirmante(corr.getId(), locale);
								}
							    //FIN TICKET 9000004714
								LOGGER.info("Asignado a:" + sigFirmante);
							}
							encontrado = true;
						}
					}
				}
				if(registrado){
					view.addObject("mensaje",
							this.messageSource.getMessage("sistcorr.asignar_ruta_aprobacion.exito", null, locale) + sigFirmante + ".");
					
				}
			}
			// FIN TICKET
		} else {
			// TICKET 9000004981
			if(directorio != null && !"".equals(directorio.trim())){
				this.emisionService.eliminarCarpetaComprimidosFirmados(directorio);
			}
			// FIN TICKET
			view.addObject("error", Boolean.valueOf(true));
			view.addObject("mensaje",
					this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
		}
		this.LOGGER.info("[FIN] resultadoFirma");
		return view;
	}
	
	// TICKET 9000004651
	@GetMapping({ "/emision-firma/respuesta-grupal" })
	public ModelAndView resultadoFirmaGrupal(@RequestParam(value = "res", required = true) boolean respuesta,
			@RequestParam(value = "extra1", required = false, defaultValue = "0") String correlativos,
			@RequestParam(value = "extra2", required = false, defaultValue = "0") String directorio, Locale locale) {
		this.LOGGER.info("[INICIO] resultadoFirmaGrupal " + respuesta);
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null) {
			return new ModelAndView("redirect:/403.html");
		}
		ModelAndView view = new ModelAndView("respuesta_firma_grupal");
		view.addObject("titulo", "Resultado de firma");
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		if (respuesta) {
			LOGGER.info("Correlativos: " + correlativos);
			String[] correl = correlativos.split(",");
			boolean todoOk = true;
			List<String> idsCorrespondenciasEnvio = new ArrayList<>();
			List<String> idsCorrespondenciasProcesar = new ArrayList<>();
			List<String> idsCorrespondenciasErrorFirma = new ArrayList<>();
			List<String> mensajesCorrespondenciasErrorFirma = new ArrayList<>();
			this.procesoFirmaDigital.borrarArchivosComprimirGrupal(correlativos, usuario.getUsername().toLowerCase());
			for(String correlat : correl){
				Long correspondencia = Long.valueOf(correlat);
				LOGGER.info("Correspondencia:" + correspondencia);
				pe.com.petroperu.model.emision.Correspondencia corr = correspondenciaDAO.findOne(correspondencia);
				// INICIO DE FLUJO NORMAL DE FIRMA DE CORRESPONDENCIA
				int cantidad = corr.getDetalleInterno().size() + corr.getDetalleExterno().size();
				int cantidadInternos = corr.getDetalleInterno().size();
				LOGGER.info("CANTIDAD DE DESTINATARIOS:" + cantidad);
				Respuesta<Firmante> respuestaFirmante = this.emisionService.firmarCorrespondencia(correspondencia,
						usuario.getUsername(), directorio, locale);
				if (!respuestaFirmante.estado) {
					idsCorrespondenciasErrorFirma.add(correlat);
					mensajesCorrespondenciasErrorFirma.add(this.messageSource.getMessage("sistcorr.emitir_firma.error", null, locale) + " para la correspondencia " + corr.getCorrelativo().getCodigo() + ".");
					//view.addObject("error", Boolean.valueOf(false));
				} else {
					//view.addObject("error", Boolean.valueOf(false));
					if(respuestaFirmante.datos!=null){
						view.addObject("firmante", respuestaFirmante.datos.get(0));
						//view.addObject("estado", ((Firmante) respuestaFirmante.datos.get(0)).getEstado().getId());
					}
					view.addObject("estado", corr.getEstado().getId());
					view.addObject("rutaAprobacion", (corr.isRutaAprobacion())?1:0);
					view.addObject("cantidad", cantidad);
					view.addObject("cantidadInternos", cantidadInternos);
					view.addObject("idCorrespondencia", correlativos);
					if(cantidad > 0){
						//idsCorrespondenciasEnvio.add(String.valueOf(correlat));
						if(corr.getEstado().getId().compareTo(Constante.CORRESPONDENCIA_COMPLETADA)==0 || corr.getEstado().getId().compareTo(Constante.CORRESPONDENCIA_APROBADA)==0){
							idsCorrespondenciasEnvio.add(String.valueOf(correlat));
						}

					}
					if(cantidadInternos <= 0){
						if(corr.getEstado().getId().compareTo(Constante.CORRESPONDENCIA_COMPLETADA)==0 || corr.getEstado().getId().compareTo(Constante.CORRESPONDENCIA_APROBADA)==0){
							idsCorrespondenciasProcesar.add(String.valueOf(correlat));
						}
					}
				}
				view.addObject("mensaje", respuestaFirmante.mensaje);
				// TICKET 9000003943
				if(corr.isRutaAprobacion() && respuestaFirmante.estado && respuestaFirmante.datos != null){
					List<RutaAprobacion> aprobadores = rutaAprobacionService.obtenerRutaAprobacion(corr).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
					Firmante f = new Firmante();
					boolean encontrado = false;
					boolean registrado = false;
					String sigFirmante = "";
					for(RutaAprobacion ra : aprobadores){
						if(ra.getFirmante()==null){
							if(!encontrado){
								LOGGER.info("Ruta Aprobacion:" + ra.getId());
								f.setNroFlujo(corr.getNroFlujo());
								ItemFilenet lugar = new ItemFilenet();
								if("1".equalsIgnoreCase(ra.getTipoFirmante())){
									ItemFilenet funcionario = this.filenetService.obtenerFirmante(ra.getCodDependencia());
									if(funcionario!=null){
										ra.setUsuario(funcionario.getCodigo().toLowerCase());
										ra.setUsuarioNombre(funcionario.getDescripcion());
									}
									lugar = this.filenetService.obtenerLugarPorDependencia(ra.getCodDependencia());
								}
								if("2".equalsIgnoreCase(ra.getTipoFirmante())){
									ra.setUsuario(ra.getUsuario().toLowerCase());
									List<ItemFilenet> listaDependenciasDestino = filenetService.obtenerDependenciaPorUsuario(ra.getUsuario(), "");
									lugar = this.filenetService.obtenerLugarPorDependencia(listaDependenciasDestino.get(0).getCodigo());
								}
								f.setCodFirmante(ra.getUsuario());
								f.setNombreFirmante(ra.getUsuarioNombre());
								f.setCodDependenciaFirmante(ra.getCodDependencia());
								f.setDependenciaFirmante(ra.getDependenciaNombre());
								f.setCodLugarTrabajoFirmante(lugar.getCodigo());
								f.setLugarTrabajoFirmante(lugar.getDescripcion());
								if("".equalsIgnoreCase(f.getCodFirmante())){
									view.addObject("mensaje",
											this.messageSource.getMessage("sistcorr.siguiente_asignacion_ruta_aprobacion.error", null, locale));
								}else{
									Respuesta<Firmante> respuestaFirmanteRuta = this.emisionService.asignarFirmante(corr.getId(), f, usuario.getUsername(), locale);
									ra.setFirmante(respuestaFirmanteRuta.datos.get(0));
									ra.setFechaModifica(new Date());
									ra.setUsuarioModifica(usuario.getUsername());
									LOGGER.info("SistcorrController Linea 2268");
									rutaAprobacionDAO.save(ra);
									registrado = true;
									sigFirmante = ra.getUsuarioNombre();
									//INICIO TICKET 9000004714
								    if (respuestaFirmanteRuta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA) {
								  	  this.emisionService.registrarDatosFirmante(corr.getId(), locale);
									}
								    //FIN TICKET 9000004714
									LOGGER.info("Asignado a: " + sigFirmante);
								}
								encontrado = true;
							}
						}
					}
					if(registrado){
						//view.addObject("mensaje",
						//		this.messageSource.getMessage("sistcorr.asignar_ruta_aprobacion.exito", null, locale) + sigFirmante + ".");
						
					}else{
						todoOk = false;
					}
				}
				// FIN DE FLUJO NORMAL DE CORRESPONDENCIA
			}
			if(idsCorrespondenciasErrorFirma.size() == correl.length){
				view.addObject("error", Boolean.valueOf(true));
				view.addObject("mensaje", this.messageSource.getMessage("sistcorr.emitir_firma.error", null, locale));
			}else{
				view.addObject("error", Boolean.valueOf(false));
				view.addObject("mensaje", this.messageSource.getMessage("sistcorr.emitir_firma.exito", null, locale));
			}
			view.addObject("corr_prc", String.join(",", idsCorrespondenciasProcesar));
			view.addObject("corr_env", String.join(",", idsCorrespondenciasEnvio));
			view.addObject("corr_err", String.join(",", idsCorrespondenciasErrorFirma));
			view.addObject("msj_error", String.join(" ", mensajesCorrespondenciasErrorFirma));
			if(todoOk){
				//view.addObject("mensaje", this.messageSource.getMessage("sistcorr.emitir_firma.exito", null, locale));
			}
		} else {
			// TICKET 9000004981
			String idsCorr = "";
			String direc = "";
			String[] partes = directorio.split(";");
			idsCorr = partes[0];
			direc = partes[1];
			if(directorio != null && !"".equals(directorio.trim())){
				this.emisionService.eliminarCarpetaComprimidosFirmados(direc);
				this.procesoFirmaDigital.borrarArchivosComprimirGrupal(idsCorr, usuario.getUsername().toLowerCase());
			}
			// FIN TICKET
			view.addObject("error", Boolean.valueOf(true));
			view.addObject("mensaje",
					this.messageSource.getMessage("sistcorr.emitir_firma.proceso.error", null, locale));
		}
		this.LOGGER.info("[FIN] resultadoFirmaGrupal");
		return view;
	}
	// FIN TICKET

	@GetMapping({ "/documentos-firmados/{idCorrespondenica}" })
	public ModelAndView documentosFirmados(@PathVariable("idCorrespondenica") Long id, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] documentosFirmados");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("firmados");
		try {
			Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuesta = this.emisionService.buscar(id, locale);
			if (!respuesta.estado)
				return new ModelAndView("redirect:/404.html");
			Respuesta<Firmante> rFirmante = this.emisionService.obtenerFirmantes(id, locale);
			boolean esFirmante = false;
			for (Firmante f : rFirmante.datos) {
				if (f.getCodFirmante().equals(usuario.getUsername())) {
					esFirmante = true;
				}
			}
			boolean esResponsable = false;
			if (usuario.getUsername().equals(respuesta.datos.get(0).getResponsable()))
				esResponsable = true;
			boolean esExterno = false;

			if (tieneRol(usuario, RolAD.EXTERNO)) {
				if ( respuesta.datos.get(0).getDetalleExterno().isEmpty()) {
					esExterno = false;
				} else if (respuesta.datos.get(0).getDetalleExterno().get(0).getCodDependenciaNacional().equals(usuario.getNombres())
						&&  respuesta.datos.get(0).getEstado().getId().equals(Constante.CORRESPONDENCIA_ENVIADA)) {
					esExterno = true;
				}
			}

			this.LOGGER.info("Responsable: " + esResponsable + " Externo: " + esExterno + " esFirmante:" + esFirmante);
			if (esExterno || esResponsable || esFirmante) {
				page.addObject("manual", Boolean.valueOf(!respuesta.datos.get(0).isFirmaDigital()));
				page.addObject("documentos",  respuesta.datos.get(0).getAdjuntos());
				page.addObject("usuario_externo", Boolean.valueOf(esExterno));
				page.addObject("asunto",  respuesta.datos.get(0).getAsunto());
				page.addObject("correlativoInt", respuesta.datos.get(0).getCorrelativo().getCodigo());
				page.addObject("correlativoExt", respuesta.datos.get(0).getFileNetCorrelativo());
				page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			} else {
				return new ModelAndView("redirect:/404.html");
			}

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}
	
	@GetMapping(value = "/consulta-bandeja-salida")
	public ModelAndView consultaBandejaSalida(Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] consultaBandejaSalida");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		// TICKET 7000003969
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();
		
		List<ItemTipoCorrespondencia> listaTipoCorrEmision = new ArrayList<>();
		List<ItemFilenet> listaDependencias = new ArrayList<>();
		List<Rol> listaRoles = new ArrayList<>();
		List<Menu> listaMenu = new ArrayList<>();
		List<TipoEmision> listaTipoEmision = new ArrayList<>();
		Object tipoCorrEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_CORR_EMISION);
		if(tipoCorrEmisionSession == null){
			List<ItemTipoCorrespondencia> tiposCorr = filenetService.listarTiposCorresponciaEmision("");
			session.setAttribute(Constante.SESSION_TIPOS_CORR_EMISION, tiposCorr);
			listaTipoCorrEmision = tiposCorr;
		}else{
			listaTipoCorrEmision = (List<ItemTipoCorrespondencia>) tipoCorrEmisionSession;
		}
		Object dependenciaSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS);
		if(dependenciaSession == null){
			List<ItemFilenet> dependenciasDestino = filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), "");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS, dependenciasDestino);
			listaDependencias = dependenciasDestino;
		}else{
			listaDependencias = (List<ItemFilenet>) dependenciaSession;
		}
		Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if(rolSession == null){
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			listaRoles = rolesBD;
		}else{
			listaRoles = (List<Rol>) rolSession;
		}
		Object menuSession = session.getAttribute(Constante.SESSION_MENU);
		if(menuSession == null){
			List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
			session.setAttribute(Constante.SESSION_MENU, menu);
			listaMenu = menu;
		}else{
			listaMenu = (List<Menu>) menuSession;
		}
		Object tipoEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_EMISION);
		if(tipoEmisionSession == null){
			List<TipoEmision> tipoEmision = this.tipoEmisionService.listarTodos();
			session.setAttribute(Constante.SESSION_TIPOS_EMISION, tipoEmision);
			listaTipoEmision = tipoEmision;
		}else{
			listaTipoEmision = (List<TipoEmision>) tipoEmisionSession;
		}
		// FIN TICKET
		//Ticket 4808
		Date fechaHoraHoy = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fechaActual = sdf.format(fechaHoraHoy);		
		Calendar c = Calendar.getInstance();
		c.setTime(fechaHoraHoy);
		c.add(Calendar.DATE, -7);
		Date fechadesd = c.getTime();
		String fechaDesde = sdf.format(fechadesd);
		// fin Ticket 4808
		
		
		ModelAndView page = new ModelAndView("consulta_bandeja_salida");
		page.addObject("fechaActual",fechaActual);
		page.addObject("fechaDesde",fechaDesde);
		page.addObject("titulo", "CONSULTA DE BANDEJA DE SALIDA");
		page.addObject("usuario", usuario);
		//page.addObject("dependenciasUsuario", filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		page.addObject("dependenciasUsuario", listaDependencias);
		//page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("listaMenu", listaMenu);
		page.addObject("errores", new ArrayList<>());
		//page.addObject("tiposCorrespondencia", filenetService.listarTiposCorresponciaEmision(""));
		page.addObject("tiposCorrespondencia", listaTipoCorrEmision);
		List<String> estadosCorrespondencia = new ArrayList<>();
		estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Por firmar");
		estadosCorrespondencia.add("Firmado");
		estadosCorrespondencia.add("Rechazado");
		estadosCorrespondencia.add("Aprobado");
		estadosCorrespondencia.add("Enviado");
		// COMENTAR ESTA LINEA PORQUE EL ESTADO SE ENCUENTRA REPETIDO
		//estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Declinada");
		boolean esJefeGestor = false;
		//if(tieneRol(usuario, RolAD.JEFE) || tieneRol(usuario, RolAD.GESTOR)){
		if(tieneRolSession(listaRoles, RolAD.JEFE) || tieneRolSession(listaRoles, RolAD.GESTOR)){
			esJefeGestor = true;
		}
		page.addObject("esJefe", esJefeGestor);
		page.addObject("estados_correspondencia", estadosCorrespondencia);
		//page.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		page.addObject("listaTipoEmision", listaTipoEmision);
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003992
		this.LOGGER.info("[FIN] consultaBandejaSalida");
		return page;
	}
	
	//Inicio Ticket 4408
	@GetMapping(value = "/consulta-jefe-gestor")
	public ModelAndView consultaJefeGestor(Locale locale) {
		
		this.LOGGER.info("[INICIO] consultaJefeGestor");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("consulta_jefe_gestor");
		page.addObject("titulo", "CONSULTA DE JEFE / GESTOR");
		page.addObject("usuario", usuario);
		page.addObject("dependenciasUsuario", filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", filenetService.listarTiposCorresponciaEmision(""));
		List<String> estadosCorrespondencia = new ArrayList<>();
		estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Por firmar");
		estadosCorrespondencia.add("Firmado");
		estadosCorrespondencia.add("Rechazado");
		estadosCorrespondencia.add("Aprobado");
		estadosCorrespondencia.add("Enviado");
		//Ticket 4808
		Date fechaHoraHoy = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fechaActual = sdf.format(fechaHoraHoy);
		
		 Calendar c = Calendar.getInstance();
		 c.setTime(fechaHoraHoy);
		 c.add(Calendar.DATE, -7);
		 Date fechadesd = c.getTime();
		 String fechaDesde = sdf.format(fechadesd);
		 // fin Ticket 4808
		// COMENTAR ESTA LINEA PORQUE EL ESTADO SE ENCUENTRA REPETIDO
		//estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Declinada");
		boolean esJefeGestor = false;
		if(tieneRol(usuario, RolAD.JEFE) || tieneRol(usuario, RolAD.GESTOR)){
			esJefeGestor = true;
		}
		page.addObject("fechaActual",fechaActual);
		page.addObject("fechaDesde",fechaDesde);
		page.addObject("esJefe", esJefeGestor);
		page.addObject("estados_correspondencia", estadosCorrespondencia);
		page.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		
		this.LOGGER.info("[FIN] consultaJefeGestor");
		return page;
	}
	//Fin Ticket 4408

	/* 9000004276 - INICIO */
	@GetMapping(value = "/consulta-comprobantes")
	public ModelAndView consultaComprobantes(Locale locale) {
		this.LOGGER.info("[INICIO] consultaComprobantes");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");

		ModelAndView page = new ModelAndView("consulta_comprobantes");
		page.addObject("titulo", "CONSULTA DE COMPROBANTES");
		page.addObject("usuario", usuario);
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("dependencias", filenetService.listarDependencias("", ""));
		page.addObject("estadosComprobante", correspondeciaService.listarEstadosFile("TD_COM"));
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] consultaComprobantes");
		return page;
	}

	@GetMapping(value = "/consultar-comprobantes-paginado", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<ComprobanteConsultaDTO>>> consultarComprobantesPaginado(HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ComprobanteConsultaDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();

			DatatableRequestConsultaComprobante dataTableRequest = new DatatableRequestConsultaComprobante(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<ComprobanteConsultaDTO> respuestaConsultaComprobantes = comprobanteService.consultarComprobantesPaginado(username, dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<ComprobanteConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaComprobantes.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaComprobantes.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaComprobantes.total.toString());
			respuesta.datos.add(dataTableResults);

		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultar-comprobantes-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelConsultaComprobantes(@RequestBody FiltroConsultaComprobanteDTO filtro, Locale locale) {
		LOGGER.info("[INICIO] exportExcelConsultaComprobantes");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();

		Respuesta<ByteArrayInputStream> respuesta = comprobanteService.consultarComprobantesExcel(username, filtro, locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(respuesta.datos.get(0)));
	}

	@GetMapping({ "/ver-detalle-comprobante/{correlativo:.+}" })
	public ModelAndView detalleComprobante(@PathVariable("correlativo") String correlativo, Locale locale) {
		ModelAndView view = new ModelAndView("comprobante_detalle");
		List<String> errores = new ArrayList<>();

		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();

		Respuesta<ComprobanteConsultaDTO> respuestaDetalleComprobante = comprobanteService.consultarDatosComprobante(username, correlativo, locale);
		if (respuestaDetalleComprobante != null && respuestaDetalleComprobante.estado && respuestaDetalleComprobante.datos != null && respuestaDetalleComprobante.datos.size() > 0) {
			ComprobanteConsultaDTO comp = respuestaDetalleComprobante.datos.get(0);
			view.addObject("comprobante", comp);
		} else {
			if (respuestaDetalleComprobante != null && respuestaDetalleComprobante.mensaje != null)
				errores.add(respuestaDetalleComprobante.mensaje);
			else
				errores.add(messageSource.getMessage("sistcorr.consulta.comprobante.error", null, locale));
			view.addObject("comprobante", new ComprobanteConsultaDTO());
		}

		Respuesta<TrackingFisico> respuestaTrackingFisico = this.correspondeciaService.obtenerTrackingFisico(usuario.getToken(), correlativo, locale);
		if (!respuestaTrackingFisico.estado) {
			errores.add(respuestaTrackingFisico.mensaje);
			view.addObject("tracking", new ArrayList<>());
		} else {
			for (TrackingFisico t : respuestaTrackingFisico.datos) {
				this.LOGGER.info(t.toString());
			}
			view.addObject("tracking", respuestaTrackingFisico.datos);
		}

		Respuesta<InformacionDocumento> respuestaAdjuntos = this.comprobanteService.obtenerDocumentosAdjuntos(usuario.getToken(), correlativo, locale);
		if (!respuestaAdjuntos.estado) {
			errores.add(respuestaAdjuntos.mensaje);
			view.addObject("documentos", new ArrayList<>());
		} else {
			List<InformacionDocumento> adjuntos = respuestaAdjuntos.datos;
			view.addObject("documentos", adjuntos);
		}

		Respuesta<Traza> respuestaTraza = this.correspondeciaService.obtenerListaTrazas(usuario.getToken(), correlativo, locale);
		if (!respuestaTraza.estado) {
			errores.add(respuestaTraza.mensaje);
			view.addObject("eventos", new ArrayList<>());
		} else {
			for (Traza t : respuestaTraza.datos) {
				if (t.getTraza().length() > 100) {
					t.setNumCaract(false);
				} else {
					t.setNumCaract(true);
				}
			}
			List<Traza> trazas = respuestaTraza.datos;
			for (Traza t : trazas) {
				String[] trazaPor = t.getNomApeUsuario().split("<br>");
				List<String> trPor = new ArrayList<String>();
				for (String tr : trazaPor) {
					if (!tr.trim().equalsIgnoreCase("")) {
						trPor.add(tr);
					}
				}
				String traz = "";
				for (int i = 0; i < trPor.size(); i++) {
					traz = traz + trPor.get(i);
					if (i < trPor.size() - 1) {
						traz = traz + "<br>";
					}
				}
				t.setNomApeUsuario(traz);
			}
			view.addObject("eventos", respuestaTraza.datos);
		}

		Respuesta<Observaciones> respuestaObservaciones = this.comprobanteService.obtenerListaObservaciones(usuario.getToken(), correlativo, locale);
		if (!respuestaObservaciones.estado) {
			errores.add(respuestaObservaciones.mensaje);
			view.addObject("observaciones", new ArrayList<>());
		} else {
			for (Observaciones o : respuestaObservaciones.datos) {
				this.LOGGER.info(o.toString());
				if (o.getObservacion().length() > 100) {
					o.setNumCaract(false);
				} else {
					o.setNumCaract(true);
				}
			}
			view.addObject("observaciones", respuestaObservaciones.datos);
		}

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("correlativo", correlativo);
		view.addObject("titulo", correlativo);
		view.addObject("formatoFecha", "dd/MM/yyyy HH:mm");
		view.addObject("errores", errores);

		return view;
	}

	@PutMapping(value = { "/registrar/observacion-comprobante/{correlativo:.+}" }, consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> registrarObservacion(@PathVariable("correlativo") String correlativo, @RequestBody RegistrarObservacion observacion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.comprobanteService.registrarObservacion(usuario.getUsername(), correlativo, observacion.getObservacion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping({ "/enviar/comprobante/copia/{correlativo:.+}" })
	public ModelAndView enviarCopiaComprobante(@PathVariable("correlativo") String correlativo, Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		ModelAndView view = new ModelAndView("copia_correspondencia");
		List<String> errores = new ArrayList<>();

		view.addObject("errores", errores);
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("bandeja", "consulta-comprobantes");
		view.addObject("correlativo", correlativo);	
		view.addObject("tamanioMaxArchivo", this.tamanioMaxArchivo);
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.comprobante.copia.titulo", null, locale));
		view.addObject("tarea", null);
		view.addObject("tipoCopia1", "comprobante");
		view.addObject("tipoCopia2", "l comprobante");

		Respuesta<CopiaCorrespondencia> respuestaCopiaCorrespondencia = this.correspondeciaService.obtenerCopiaCorrespondencia(usuario.getToken(), correlativo, locale);
		if (!respuestaCopiaCorrespondencia.estado) {
			LOGGER.info("Información copia correspondencia estado false: " + respuestaCopiaCorrespondencia.mensaje);
			errores.add(respuestaCopiaCorrespondencia.mensaje);
			view.addObject("destinatarios", new ArrayList());
		} else {
			LOGGER.info("Información copia correspondencia estado true: " + respuestaCopiaCorrespondencia.datos.size());
			view.addObject("destinatarios", respuestaCopiaCorrespondencia.datos);
		}

		Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionarios(usuario.getToken(), "0", "", "SI", locale);
		if (!respuestaFuncionario.estado) {
			LOGGER.info("Información funcionario estado false: " + respuestaFuncionario.mensaje);
			errores.add(respuestaFuncionario.mensaje);
			view.addObject("funcionarios", new ArrayList());
		} else {
			LOGGER.info("Información funcionario estado true: " + respuestaFuncionario.datos.size());
			view.addObject("funcionarios", respuestaFuncionario.datos);
		}

		return view;
	}
	
	// -------------

	@GetMapping(value = "/consulta-contrataciones")
	public ModelAndView consultaContrataciones(Locale locale) {
		this.LOGGER.info("[INICIO] consultaContrataciones");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");

		ModelAndView page = new ModelAndView("consulta_contrataciones");
		page.addObject("titulo", "CONSULTA DE CONTRATACIONES");
		page.addObject("usuario", usuario);
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("dependencias", filenetService.listarDependenciasCEE(usuario.getUsername(), "", ""));
		page.addObject("tiposProceso", filenetService.listarTiposProceso());
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] consultaContrataciones");
		return page;
	}

	@GetMapping(value = "/consultar-contrataciones-paginado", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<ContratacionConsultaDTO>>> consultarContratacionesPaginado(HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ContratacionConsultaDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();

			DatatableRequestConsultaContratacion dataTableRequest = new DatatableRequestConsultaContratacion(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<ContratacionConsultaDTO> respuestaConsultaComprobantes = contratacionService.consultarContratacionesPaginado(username, dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<ContratacionConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaComprobantes.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaComprobantes.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaComprobantes.total.toString());
			respuesta.datos.add(dataTableResults);

		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultar-contrataciones-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelConsultaContrataciones(@RequestBody FiltroConsultaContratacionDTO filtro, Locale locale) {
		LOGGER.info("[INICIO] exportExcelConsultaContrataciones");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();

		Respuesta<ByteArrayInputStream> respuesta = contratacionService.consultarContratacionesExcel(username, filtro, locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(respuesta.datos.get(0)));
	}

	@GetMapping({ "/ver-detalle-contratacion/{nroProceso:.+}" })
	public ModelAndView detalleContratacion(@PathVariable("nroProceso") String nroProceso, Locale locale) {
		ModelAndView view = new ModelAndView("contratacion_detalle");
		List<String> errores = new ArrayList<>();

		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();

		// DATOS
		Respuesta<Contratacion> respuestaDetalleContratacion = contratacionService.consultarDatosContratacion(username, nroProceso, locale);
		if (respuestaDetalleContratacion != null && respuestaDetalleContratacion.estado && respuestaDetalleContratacion.datos != null && respuestaDetalleContratacion.datos.size() > 0) {
			Contratacion comp = respuestaDetalleContratacion.datos.get(0);
			view.addObject("contratacion", comp);
		} else {
			if (respuestaDetalleContratacion != null && respuestaDetalleContratacion.mensaje != null)
				errores.add(respuestaDetalleContratacion.mensaje);
			else
				errores.add(messageSource.getMessage("sistcorr.consulta.contratacion.error", null, locale));
			view.addObject("contratacion", new Contratacion());
		}

		// BASES VENDIDAS
		Respuesta<BaseVendida> respuestaBasesVendidas = contratacionService.consultarBasesVendidas(username, nroProceso, locale);
		if (!respuestaBasesVendidas.estado) {
			errores.add(respuestaBasesVendidas.mensaje);
			view.addObject("basesVendidas", new ArrayList<>());
		} else {
			List<BaseVendida> basesVendidas = respuestaBasesVendidas.datos;
			view.addObject("basesVendidas", basesVendidas);
		}

		// CONSULTAS
		Respuesta<ConsultaBase> respuestaConsultaBases = contratacionService.consultarConsultaBases(username, nroProceso, locale);
		if (!respuestaConsultaBases.estado) {
			errores.add(respuestaConsultaBases.mensaje);
			view.addObject("consultaBases", new ArrayList<>());
		} else {
			List<ConsultaBase> consultaBases = respuestaConsultaBases.datos;
			view.addObject("consultaBases", consultaBases);
		}

		// PROPUESTAS
		Respuesta<Propuesta> respuestaPropuestas = contratacionService.consultarPropuestas(username, nroProceso, locale);
		if (!respuestaPropuestas.estado) {
			errores.add(respuestaPropuestas.mensaje);
			view.addObject("propuestas", new ArrayList<>());
		} else {
			List<Propuesta> propuestas = respuestaPropuestas.datos;
			view.addObject("propuestas", propuestas);
		}

		// IMPUGNACIONES
		Respuesta<Impugnacion> respuestaImpugnaciones = contratacionService.consultarImpugnaciones(username, nroProceso, locale);
		if (!respuestaImpugnaciones.estado) {
			errores.add(respuestaImpugnaciones.mensaje);
			view.addObject("impugnaciones", new ArrayList<>());
		} else {
			List<Impugnacion> propuestas = respuestaImpugnaciones.datos;
			view.addObject("impugnaciones", propuestas);
		}

		// DOCUMENTOS
		Respuesta<InformacionDocumento> respuestaAdjuntos = this.contratacionService.obtenerDocumentosAdjuntos(usuario.getToken(), nroProceso, locale);
		if (!respuestaAdjuntos.estado) {
			errores.add(respuestaAdjuntos.mensaje);
			view.addObject("documentos", new ArrayList<>());
		} else {
			List<InformacionDocumento> adjuntos = respuestaAdjuntos.datos;
			view.addObject("documentos", adjuntos);
		}

		// TRAZA
		Respuesta<Traza> respuestaTraza = this.correspondeciaService.obtenerListaTrazas(usuario.getToken(), nroProceso, locale);
		if (!respuestaTraza.estado) {
			errores.add(respuestaTraza.mensaje);
			view.addObject("eventos", new ArrayList<>());
		} else {
			for (Traza t : respuestaTraza.datos) {
				if (t.getTraza().length() > 100) {
					t.setNumCaract(false);
				} else {
					t.setNumCaract(true);
				}
			}
			List<Traza> trazas = respuestaTraza.datos;
			for (Traza t : trazas) {
				String[] trazaPor = t.getNomApeUsuario().split("<br>");
				List<String> trPor = new ArrayList<String>();
				for (String tr : trazaPor) {
					if (!tr.trim().equalsIgnoreCase("")) {
						trPor.add(tr);
					}
				}
				String traz = "";
				for (int i = 0; i < trPor.size(); i++) {
					traz = traz + trPor.get(i);
					if (i < trPor.size() - 1) {
						traz = traz + "<br>";
					}
				}
				t.setNomApeUsuario(traz);
			}
			view.addObject("eventos", respuestaTraza.datos);
		}

		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("nroProceso", nroProceso);
		view.addObject("titulo", nroProceso);
		view.addObject("formatoFecha", "dd/MM/yyyy HH:mm");
		view.addObject("errores", errores);

		return view;
	}	
	/* 9000004276 - FIN */

	// TICKET 9000003906
	public int compare(Object o1, Object o2) {
	    return o1.toString().compareToIgnoreCase(o2.toString());
	}
	
	@GetMapping(value = "/tutoriales")
	public ModelAndView tutoriales(Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] tutoriales");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("tutoriales");
		page.addObject("titulo", "Tutoriales");
		page.addObject("usuario", usuario);
		page.addObject("dependenciasUsuario", filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		
		File folderNormativa = new File(urlBase + directorioTutoriales + Tutoriales.NORMATIVA.TD_Directorio);
		List<String> archivosNormativa = new ArrayList<String>();
		File[] archivos;
		if(folderNormativa.isDirectory()){
			archivos = folderNormativa.listFiles();
			for(File f : archivos){
				if(f.isFile()){
					archivosNormativa.add(f.getName());
				}
			}
			Collections.sort(archivosNormativa, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
		}
		
		File folderGuia = new File(urlBase + directorioTutoriales + Tutoriales.GUIA.TD_Directorio);
		List<String> archivosGuia = new ArrayList<String>();
		if(folderGuia.isDirectory()){
			archivos = folderGuia.listFiles();
			for(File f : archivos){
				if(f.isFile()){
					archivosGuia.add(f.getName());
				}
			}
			Collections.sort(archivosGuia, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
		}
		
		File folderVideo = new File(urlBase + directorioTutoriales + Tutoriales.VIDEO.TD_Directorio);
		List<String> archivosVideo = new ArrayList<String>();
		if(folderVideo.isDirectory()){
			archivos = folderVideo.listFiles();
			for(File f : archivos){
				if(f.isFile()){
					archivosVideo.add(f.getName());
				}
			}
			Collections.sort(archivosVideo, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
		}
		
		File folderManual = new File(urlBase + directorioTutoriales + Tutoriales.MANUAL.TD_Directorio);
		List<String> archivosManual = new ArrayList<String>();
		if(folderManual.isDirectory()){
			archivos = folderManual.listFiles();
			for(File f : archivos){
				if(f.isFile()){
					archivosManual.add(f.getName());
				}
			}
			Collections.sort(archivosManual, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
		}
		
		File folderPregunta = new File(urlBase + directorioTutoriales + Tutoriales.PREGUNTAS.TD_Directorio);
		List<String> archivosPregunta = new ArrayList<String>();
		if(folderPregunta.isDirectory()){
			archivos = folderPregunta.listFiles();
			for(File f : archivos){
				if(f.isFile()){
					archivosPregunta.add(f.getName());
				}
			}
			Collections.sort(archivosPregunta, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
		}
		page.addObject("normativa", archivosNormativa);
		page.addObject("guia", archivosGuia);
		page.addObject("video", archivosVideo);
		page.addObject("manual", archivosManual);
		page.addObject("pregunta", archivosPregunta);
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003992
		this.LOGGER.info("[FIN] tutoriales");
		return page;
	}
	
	@GetMapping({ "/descargar/tutorial/{tipo}/{archivo:.+}" })
	public ResponseEntity<byte[]> obtenerDocumento(@PathVariable("tipo") String path, @PathVariable("archivo") String archivo, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] obtenerDocumento");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		try{
			String pathFile = urlBase + directorioTutoriales + path + "/" + archivo;
			// TICKET 9000003992
			// System.out.println("Path:" + pathFile);
			this.LOGGER.info("[INFO] " + " This is info : pathFile " + pathFile);
			File fil = new File(pathFile);
			String contentType = URLConnection.guessContentTypeFromName(archivo);
			LOGGER.info("ContentType:" + contentType);
			if(archivo.toLowerCase().indexOf("mp4") >= 0){
				contentType = "video/mp4";
			}
			if(contentType==null){
				contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			}
			byte[] byteFile = Files.readAllBytes(fil.toPath());
			java.io.InputStream targetStream = new ByteArrayInputStream(byteFile);
			InputStreamResource resource = new InputStreamResource(targetStream);
			if (resource != null) {
				return ((ResponseEntity.BodyBuilder) ((ResponseEntity.BodyBuilder) ResponseEntity.ok()
						.header("Content-Disposition", new String[] {
	
								"attachment" })).header("filename", new String[] { archivo }))
						.contentType(MediaType.parseMediaType(contentType)).body(byteFile);
			}
		}catch(Exception e){
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] obtenerDocumento" + " This is error : " + e);
			// e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	
	@GetMapping({ "/consulta-correspondencia" })
	public ModelAndView consultaCorrespondencia(Locale locale) {
		this.LOGGER.info("[INICIO] firma");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("consulta_correspondencia");
		try{
			// TICKET 7000003969
			List<Estado> listaEstados = new ArrayList<>();
			List<Estado> listaTiposCorr = new ArrayList<>();
			List<ItemFilenet> listaDep = new ArrayList<>();
			List<ItemFilenet> listaDepRem = new ArrayList<>();
			List<Rol> listaRoles = new ArrayList<>();
			List<Menu> listaMenu = new ArrayList<>();
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (requestAttributes != null)
					? ((ServletRequestAttributes) requestAttributes).getRequest()
					: null;
			HttpSession session = request.getSession();
			Object estadoSession = session.getAttribute(Constante.SESSION_ESTADOS_FILENET);
			if(estadoSession == null){
				List<Estado> estadosFilenet = correspondeciaService.listarEstadosFile("TD_CR");
				session.setAttribute(Constante.SESSION_ESTADOS_FILENET, estadosFilenet);
				listaEstados = estadosFilenet;
			}else{
				listaEstados = (List<Estado>) estadoSession;
			}
			Object tipoCorrSession = session.getAttribute(Constante.SESSION_TIPOS_CORR);
			if(tipoCorrSession == null){
				List<Estado> tiposCorr = correspondeciaService.listarTiposCorrespondencia();
				session.setAttribute(Constante.SESSION_TIPOS_CORR, tiposCorr);
				listaTiposCorr = tiposCorr;
			}else{
				listaTiposCorr = (List<Estado>) tipoCorrSession;
			}
			Object depSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_FILENET);
			if(depSession == null){
				List<ItemFilenet> dep = filenetService.listarDependencias(null, "%");
				session.setAttribute(Constante.SESSION_DEPENDENCIAS_FILENET, dep);
				listaDep = dep;
			}else{
				listaDep = (List<ItemFilenet>) depSession; 
			}
			Object depRemSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_REMITENTE_FILENET);
			if(depRemSession == null){
				List<ItemFilenet> depRem = filenetService.listarDependenciasRemitente("%");
				session.setAttribute(Constante.SESSION_DEPENDENCIAS_REMITENTE_FILENET, depRem);
				listaDepRem = depRem;
			}else{
				listaDepRem = (List<ItemFilenet>) depRemSession;
			}
			Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
			if(rolSession == null){
				List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
				session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
				listaRoles = rolesBD;
			}else{
				listaRoles = (List<Rol>) rolSession;
			}
			Object menuSession = session.getAttribute(Constante.SESSION_MENU);
			if(menuSession == null){
				List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
				session.setAttribute(Constante.SESSION_MENU, menu);
				listaMenu = menu;
			}else{
				listaMenu = (List<Menu>) menuSession;
			}
			// FIN TICKET
			FiltroConsultaCorrespondencia filtro = new FiltroConsultaCorrespondencia();
			//List<Estado> listaEstadosCorrespondencia = correspondeciaService.listarEstadosFile("TD_CR");
			List<Estado> listaEstadosCorrespondencia = listaEstados;
			//List<Estado> listaTiposCorrespondencia = correspondeciaService.listarTiposCorrespondencia();
			List<Estado> listaTiposCorrespondencia = listaTiposCorr;
			//List<ItemFilenet> listaDependencias = filenetService.listarDependencias(null, "%");
			List<ItemFilenet> listaDependencias = listaDep;
			//List<ItemFilenet> listaDependenciasRemitente = filenetService.listarDependenciasRemitente("%");
			List<ItemFilenet> listaDependenciasRemitente = listaDepRem;
			
			Respuesta<CorrespondenciaConsulta> respuesta = new Respuesta<>();
			respuesta.datos = new ArrayList<>();
			
			page.addObject("estadosCorr", listaEstadosCorrespondencia);
			page.addObject("esJefe", tieneRolSession(listaRoles, RolAD.JEFE));
			LOGGER.info("Consulta correspondencia:" + usuario.getUsername() + "|" + tieneRol(usuario, RolAD.JEFE));
			page.addObject("esGestor", tieneRolSession(listaRoles, RolAD.GESTOR));
			page.addObject("tiposCorrespondencia", listaTiposCorrespondencia);
			page.addObject("dependenciasRemitentes", listaDependenciasRemitente);
			page.addObject("dependenciasDestinatarios", listaDependencias);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			//page.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
			page.addObject("listaMenu", listaMenu);
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource.getMessage("sistcorr.consulta.correspondencia.titulo", null, locale));
			page.addObject("fechaDocumento", "10/12/2019");
			page.addObject("correspondencias", respuesta.datos);
			return page;
		}catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("redirect:/404.html");
		}
	}
	
	// TICKET 9000003995
	@PostMapping(value = "/consultar-correspondencia-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelConsultaCorrespondencia(@RequestBody FiltroConsultaCorrespondencia filtro, Locale locale){
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelConsultaCorrespondencia");
		LOGGER.info("[FILTRO] " + filtro.toString());
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		//Respuesta<CorrespondenciaConsulta> respuestaConsulta = new Respuesta<>();
		// TICKET 9000004494 - ORDENADO POR COLUMNA CORRELATIVO, ORDENAMIENTO DESCENDENTE ACTIVADO
		Respuesta<CorrespondenciaConsulta> respuestaConsulta = this.correspondeciaService.consultarCorrespondencia(usuario.getToken(), filtro, 1, 1, Constante.COLUMNAS_CONSULTA_CORRESPONDENCIA[2], "SI", "SI", locale);
		//respuestaConsulta = this.correspondeciaService.consultarCorrespondencia(usuario.getToken(), filtro, locale);
		ReportExcelConsultaCorrespondencia reporte = new ReportExcelConsultaCorrespondencia(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if(respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}
	 
	@PostMapping(value = "/consultar-asignacion-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelConsultaAsignacion(@RequestBody FiltroConsultaAsignacion filtro, Locale locale){
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelConsultaAsignacion");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		// TICKET 9000004494
		Respuesta<AsignacionConsulta> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.correspondeciaService.consultarAsignaciones(usuario.getToken(), filtro, 1, 1, Constante.COLUMNAS_CONSULTA_ASIGNACION[2], "SI", "SI", locale);
		// FIN TICKET
		ReportExcelConsultaAsignacion reporte = new ReportExcelConsultaAsignacion(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if(respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}
	// FIN TICKET
	
	@GetMapping({ "/consulta-asignaciones" })
	public ModelAndView consultaAsignaciones(Locale locale) {
		this.LOGGER.info("[INICIO] consultaAsignaciones");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("consulta_asignaciones");
		try{
			// TICKET 7000003969
			List<Estado> listaEstados = new ArrayList<>();
			List<ItemFilenet> listaDep = new ArrayList<>();
			Respuesta<Funcionario> respuestaFunc = new Respuesta<>();
			Respuesta<Accion> respuestaAcc = new Respuesta<>();
			List<Rol> listaRoles = new ArrayList<>();
			List<Menu> listaMenu = new ArrayList<>();
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (requestAttributes != null)
					? ((ServletRequestAttributes) requestAttributes).getRequest()
					: null;
			HttpSession session = request.getSession();
			Object estadoSession = session.getAttribute(Constante.SESSION_ESTADOS_ASIGNACION);
			if(estadoSession == null){
				List<Estado> estadosFilenet = correspondeciaService.listarEstadosAsignacion();
				session.setAttribute(Constante.SESSION_ESTADOS_ASIGNACION, estadosFilenet);
				listaEstados = estadosFilenet;
			}else{
				listaEstados = (List<Estado>) estadoSession;
			}
			Object depSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_ASIGNACION);
			if(depSession == null){
				List<ItemFilenet> dep = filenetService.listarDependenciasAsignacion("%");
				session.setAttribute(Constante.SESSION_DEPENDENCIAS_ASIGNACION, dep);
				listaDep = dep;
			}else{
				listaDep = (List<ItemFilenet>) depSession; 
			}
			Object personaAsignadaSession = session.getAttribute(Constante.SESSION_PERSONA_ASIGNADA);
			if(personaAsignadaSession == null){
				respuestaFunc.datos = filenetService.listarPersonaAsignada(null, null, "%", "SI");
				session.setAttribute(Constante.SESSION_PERSONA_ASIGNADA, respuestaFunc);
			}else{
				respuestaFunc = (Respuesta<Funcionario>) personaAsignadaSession;
			}
			Object accSession = session.getAttribute(Constante.SESSION_ACCIONES);
			if(accSession == null){
				respuestaAcc = correspondeciaService.obtenerListaAcciones(usuario.getToken(), locale);
				session.setAttribute(Constante.SESSION_ACCIONES, respuestaAcc);
			}else{
				respuestaAcc = (Respuesta<Accion>) accSession;
			}
			Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
			if(rolSession == null){
				List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
				session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
				listaRoles = rolesBD;
			}else{
				listaRoles = (List<Rol>) rolSession;
			}
			Object menuSession = session.getAttribute(Constante.SESSION_MENU);
			if(menuSession == null){
				List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
				session.setAttribute(Constante.SESSION_MENU, menu);
				listaMenu = menu;
			}else{
				listaMenu = (List<Menu>) menuSession;
			}
			// FIN TICKET
			
			FiltroConsultaAsignacion filtro = new FiltroConsultaAsignacion();
			//Respuesta<AsignacionConsulta> respuesta = this.correspondeciaService.consultarAsignaciones(usuario.getToken(), filtro, locale);
			//List<Estado> listaEstadosAsignacion = correspondeciaService.listarEstadosAsignacion();
			List<Estado> listaEstadosAsignacion = listaEstados;
			//List<ItemFilenet> listaDependencias = filenetService.listarDependenciasAsignacion("%");
			List<ItemFilenet> listaDependencias = listaDep;
			//Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionarios(usuario.getToken(), "0", "", "SI", locale);
			Respuesta<Funcionario> respuestaFuncionario = new Respuesta<>(); 
			//respuestaFuncionario.datos = filenetService.listarPersonaAsignada(null, null, "%", "SI");
			respuestaFuncionario.datos = respuestaFunc.datos;
			List<Funcionario> listaPersonas = respuestaFuncionario.datos;
			Respuesta<Accion> respuestaAcciones = correspondeciaService.obtenerListaAcciones(usuario.getToken(), locale);
			
			Respuesta<AsignacionConsulta> respuesta = new Respuesta<>();
			respuesta.datos = new ArrayList<>();
			
			page.addObject("acciones", respuestaAcciones.datos);
			page.addObject("estadosAsig", listaEstadosAsignacion);
			page.addObject("esJefe", tieneRol(usuario, RolAD.JEFE));
			page.addObject("esGestor", tieneRol(usuario, RolAD.GESTOR));
			page.addObject("dependenciasAsignantes", listaDependencias);
			page.addObject("personasAsignadas", listaPersonas);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource.getMessage("sistcorr.consulta.asignaciones.titulo", null, locale));
			page.addObject("fechaDocumento", "10/12/2019");
			page.addObject("asignaciones", respuesta.datos);
			return page;
		}catch(Exception e){
			return new ModelAndView("redirect:/404.html");
		}
	}
	
	@GetMapping(value = { "/eliminar-documento/{idDocumento}/{codigoTraza}/{proceso}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> eliminarDocumento(@PathVariable("idDocumento") String idDocumento, 
			@PathVariable("codigoTraza") String codigoTraza, @PathVariable("proceso") String proceso, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] eliminarDocumento");
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			if(usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			respuesta = this.correspondeciaService.eliminarDocumento(usuario.getToken(), idDocumento, proceso, codigoTraza, locale);
		}catch(Exception e){
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] eliminarDocumento");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	private UsuarioPetroperu obtenerUsuario() {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] obtenerUsuario");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] obtenerUsuario");
		return null;
	}
	
	// TICKET 9000003780
	@GetMapping(value = { "/listar-dependencias-nivel/{cgc}/{nivel}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> listarDependenciasNivel(@PathVariable("cgc") String cgc, 
			@PathVariable("nivel") String nivel, Locale locale) {
		Respuesta<ItemFilenet> respuestaDependencias = new Respuesta<>();
		try{
			LOGGER.info("Nivel Dependencia:" + cgc + "||" + nivel);
			if(cgc==null || cgc.equalsIgnoreCase("null")){
				cgc = "";
			}
			respuestaDependencias.estado = true;
			respuestaDependencias.datos = filenetService.listarDependenciasNivelJerarquia(nivel, cgc);
			LOGGER.info("Dependencias:" + respuestaDependencias.datos.size());
		}catch(Exception e){
			e.printStackTrace();
			respuestaDependencias.estado = false;
			respuestaDependencias.datos = new ArrayList();
		}
		return new ResponseEntity<>(respuestaDependencias, HttpStatus.OK);
	}
	// FIN TICKET
	
	// TICKET 9000003973
		@GetMapping(value = { "/gestionar-correspondencia-entrada/{correlativo}" }, produces = { "application/json" })
		public ResponseEntity<Respuesta<ItemFilenet>> gestionarCorrespondenciaEntrada(@PathVariable("correlativo") String correlativo, 
				Locale locale) {
			Respuesta<ItemFilenet> respuestaDependencias = new Respuesta<>();
			try{
				LOGGER.info("gestionarCorrespondenciaEntrada:" + correlativo);
				/*if(cgc==null || cgc.equalsIgnoreCase("null")){
					cgc = "";
				}
				respuestaDependencias.estado = true;
				respuestaDependencias.datos = filenetService.listarDependenciasNivelJerarquia(nivel, cgc);*/
				LOGGER.info("Dependencias:" + respuestaDependencias.datos.size());
			}catch(Exception e){
				e.printStackTrace();
				respuestaDependencias.estado = false;
				respuestaDependencias.datos = new ArrayList();
			}
			return new ResponseEntity<>(respuestaDependencias, HttpStatus.OK);
		}
		// FIN TICKET
		
	// TICKET 9000004024
	@GetMapping({ "/{bandeja}/{correlativo}" })
	public ModelAndView correspondenciaCorrelativo(@PathVariable("bandeja") String bandeja, @PathVariable("correlativo") String correlativo, Locale locale) {
			LOGGER.info("BANDEJA");
			/*INI Ticket 9*4413*/
			ModelAndView view = new ModelAndView();
			if (bandeja.equalsIgnoreCase("delreceptor")){
				 view = new ModelAndView("correspondencia_receptor");
			}else{
			/*FIN Ticket 9*4413*/
				view = new ModelAndView("correspondencia");
			}//Ticket 9*4413
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			switch (bandeja) {
			case "pendientes":
				view.addObject("titulo", messageSource.getMessage("sistcorr.correspondencia.pendientes.titulo", null, locale));
				view.addObject("tipoCorrespondencia", SistcorrBandeja.PENDIENTE.TD_BANDEJA);
				view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.pendientes.url", null, locale));
				break;
			case "atencion":
				view.addObject("titulo", messageSource.getMessage("sistcorr.correspondencia.atencion.titulo", null, locale));
				view.addObject("tipoCorrespondencia", SistcorrBandeja.EN_ATENCION.TD_BANDEJA);
				view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.atencion.url", null, locale));
				break;
			case "completadas":
				view.addObject("titulo", messageSource.getMessage("sistcorr.correspondencia.completados.titulo", null, locale));
				view.addObject("tipoCorrespondencia", SistcorrBandeja.COMPLETADA.TD_BANDEJA);
				view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.completados.url", null, locale));
				break;
			// TICKET 9000003862
			case "delgestor":
				view.addObject("titulo", messageSource.getMessage("sistcorr.correspondencia.delgestor.titulo", null, locale));
				view.addObject("tipoCorrespondencia", SistcorrBandeja.DE_GESTOR.TD_BANDEJA);
				view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.delgestor.url", null, locale));
				break;
			// FIN TICKET
			/*INI Ticket 9*4413*/
			case "delreceptor":
				view.addObject("titulo", messageSource.getMessage("sistcorr.correspondencia.delreceptor.titulo", null, locale));
				view.addObject("tipoCorrespondencia", SistcorrBandeja.DE_RECEPTOR.TD_BANDEJA);
				view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.delreceptor.url", null, locale));
				break;
			/*FIN Ticket 9*4413*/
			default:
				view = new ModelAndView("redirect:/404.html");
				break;
		}
		view.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
		view.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("usuario", usuario);
		view.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
		Respuesta<Accion> respuestaAcciones = correspondeciaService.obtenerListaAcciones(usuario.getToken(), locale);
		view.addObject("acciones", respuestaAcciones.datos);
		// TICKET 9000003862
		List<ItemFilenet> listaDependenciasDestino = filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), "");
		view.addObject("dependenciasDestino", listaDependenciasDestino);
		// FIN TICKET
		List<Estado> listaEstadosCorrespondencia = correspondeciaService.listarEstadosFile("TD_CR");
		view.addObject("estadosCorr", listaEstadosCorrespondencia);
		view.addObject("tamanioMaxArchivo", tamanioMaxArchivo);
		LOGGER.info("ROL JEFE:" + tieneRol(usuario, RolAD.JEFE));
		LOGGER.info("ROL GESTOR:" + tieneRol(usuario, RolAD.GESTOR));
		view.addObject("esJefe", tieneRol(usuario, RolAD.JEFE));
		view.addObject("esGestor", tieneRol(usuario, RolAD.GESTOR));
		view.addObject("errores", errores);
		view.addObject(NUEVA_CORRESPONDENCIA, nuevaCorrespondencia);
		view.addObject(POR_ACEPTAR, porAceptar);
		view.addObject("correlativo", correlativo);
		return view;
	}
	// FIN TICKET
	
	// TICKET 9000003944
	@GetMapping({ "/adminEquipos" })
	public ModelAndView adminEquipo(Locale locale){
		this.LOGGER.info("[INICIO] adminEquipo");
		ModelAndView view = new ModelAndView("admin_equipos");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		for (Object[] rol : roles) {
			/*LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			if("ROLE_GESTOR".equals(rol[3].toString())){
				LOGGER.info("GESTOR:" + rol[3].toString());
				esGestor = true;
			}
			if(!"ROLE_GESTOR".equals(rol[3].toString()) && !"ROLE_EJECUTOR".equals(rol[3].toString()) && !"ROLE_USUARIO".equals(rol[3].toString())){
				LOGGER.info("NO GESTO NO EJECUTOR:" + rol[3].toString());
				noEsGestorNiEjecutor = false;
			}*/
		}
		Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionariosRuta(usuario.getToken(), "0", "", "SI", "", locale);
		LOGGER.info("Cantidad de Personas:" + respuestaFuncionario.datos.size());
		List<ItemFilenet> datosFuncionarios = new ArrayList<>();
		for(Funcionario f : respuestaFuncionario.datos){
			ItemFilenet i = new ItemFilenet();
			i.setCodigo(f.getUsuario());
			i.setDescripcion(f.getNombreApellidoUsuario());
			datosFuncionarios.add(i);
		}
		view.addObject("titulo",
				messageSource.getMessage("sistcorr.correspondencia.dependencia_equipos.titulo", null, locale));
		view.addObject("usuario", usuario);
		view.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("jefes", datosFuncionarios);
		this.LOGGER.info("[FIN] adminEquipo");
		return view;
	}
	
	@GetMapping({ "/registrarDependencia" })
	public ModelAndView registrarDependencia(Locale locale){
		this.LOGGER.info("[INICIO] registrarDependencia");
		ModelAndView view = new ModelAndView("dependencia_unidad");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionariosRuta(usuario.getToken(), "0", "", "SI", "", locale);
		LOGGER.info("Cantidad de Personas:" + respuestaFuncionario.datos.size());
		List<ItemFilenet> datosFuncionarios = new ArrayList<>();
		for(Funcionario f : respuestaFuncionario.datos){
			ItemFilenet i = new ItemFilenet();
			i.setCodigo(f.getUsuario());
			i.setDescripcion(f.getNombreApellidoUsuario());
			datosFuncionarios.add(i);
		}
		view.addObject("idDependencia", 0);
		view.addObject("usuario", usuario);
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.registrarDependencia.titulo", null, locale));
		view.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("tiposUnidadMatricial", this.filenetService.listarTipoUnidadMatricial());
		view.addObject("jerarquias", this.filenetService.listarJerarquia());
		view.addObject("jefes", datosFuncionarios);
		view.addObject("integrantes", datosFuncionarios);
		view.addObject("lugares", this.filenetService.listarLugares(""));
		view.addObject("dependenciasSuperiores", this.filenetService.listarDependenciasNuevo("", ""));
		this.LOGGER.info("[FIN] registrarDependencia");
		return view;
	}
	
	@GetMapping({ "/modificarDependencia/{id}/{codigo}" })
	public ModelAndView modificarDependencia(@PathVariable("id") Long idDependencia, @PathVariable("codigo") String codigo, Locale locale){
		this.LOGGER.info("[INICIO] regmodificarDependenciaistrarDependencia");
		ModelAndView view = new ModelAndView("dependencia_unidad");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionariosRuta(usuario.getToken(), "0", "", "SI", "", locale);
		LOGGER.info("Cantidad de Personas:" + respuestaFuncionario.datos.size());
		List<ItemFilenet> datosFuncionarios = new ArrayList<>();
		for(Funcionario f : respuestaFuncionario.datos){
			ItemFilenet i = new ItemFilenet();
			i.setCodigo(f.getUsuario());
			i.setDescripcion(f.getNombreApellidoUsuario());
			datosFuncionarios.add(i);
		}
		view.addObject("idDependencia", idDependencia);
		view.addObject("codigo", codigo);
		view.addObject("usuario", usuario);
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.registrarDependencia.titulo", null, locale));
		view.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("tiposUnidadMatricial", this.filenetService.listarTipoUnidadMatricial());
		view.addObject("jerarquias", this.filenetService.listarJerarquia());
		view.addObject("jefes", datosFuncionarios);
		view.addObject("integrantes", datosFuncionarios);
		view.addObject("lugares", this.filenetService.listarLugares(""));
		view.addObject("dependenciasSuperiores", this.filenetService.listarDependenciasNuevo("", ""));
		this.LOGGER.info("[FIN] modificarDependencia");
		return view;
	}
	
	@PostMapping(value = {"/guardarDependencia"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<DependenciaUnidadMatricial>> guardarDependencia(@RequestBody DependenciaUnidadMatricial dependencia, Locale locale){
		Respuesta<DependenciaUnidadMatricial> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		UsuarioPetroperu usuario = obtenerUsuario();
		//System.out.println("Dependencia:" + dependencia.toString());
		String mensajeRespuesta = "";
		if(dependencia.getIdDependenciaUnidadMatricial() == 0L){
			mensajeRespuesta = this.filenetService.registrarDependenciaUnidadMatricialCompleto(dependencia, usuario.getUsername());
			//System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Registro guardado correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
		}else{
			this.filenetService.modificarDependenciaUnidadMatricialCompleto(dependencia, usuario.getUsername());
			if(!dependencia.getMensajeRpt().trim().equalsIgnoreCase("")) {
				respuesta.estado = false;
				respuesta.mensaje = dependencia.getMensajeRpt().trim();
			}else {
				respuesta.estado = true;
				respuesta.mensaje = "Registro guardado correctamente.";
			}
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping(value = {"/buscarDependencias"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<DependenciaUnidadMatricial>> buscarDependencias(@RequestBody FiltroConsultaDependencia filtro, Locale locale){
		LOGGER.info("[INICIO] buscarDependencias");
		LOGGER.info("filtros buscarDependencias:" + filtro.toString());
		UsuarioPetroperu usuario = obtenerUsuario();
		Respuesta<DependenciaUnidadMatricial> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		int codigo = 0;
		try{
			codigo = Integer.valueOf(filtro.getCodigoDependencia());
		}catch(Exception e){
			codigo = 0;
		}
		String nombre = filtro.getNombreDependencia();
		String tipo = filtro.getTipo();
		String jefe = filtro.getJefe();
		try{
			List<DependenciaUnidadMatricial> dependencias = filenetService.buscarDependencias(codigo, nombre, tipo, jefe);
			Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionariosRuta(usuario.getToken(), "0", "", "SI", "", locale);
			List<ItemFilenet> datosFuncionarios = new ArrayList<>();
			for(Funcionario f : respuestaFuncionario.datos){
				ItemFilenet i = new ItemFilenet();
				i.setCodigo(f.getUsuario());
				i.setDescripcion(f.getNombreApellidoUsuario());
				datosFuncionarios.add(i);
			}
			for(int i=0;i<dependencias.size();i++){
				DependenciaUnidadMatricial dep = dependencias.get(i);
				// JEFE
				for(ItemFilenet it : datosFuncionarios){
					if(dep.getJefe().equalsIgnoreCase(it.getCodigo())){
						dependencias.get(i).setJefe(it.getDescripcion());
						break;
					}
				}
				// TIPO
				if("DEPENDENCIA".equalsIgnoreCase(dep.getTipo().toUpperCase())){
					dependencias.get(i).setTipo("DEPENDENCIA");
					dependencias.get(i).setTipoUnidadMatricial("");
				}else{
					dependencias.get(i).setTipo("UNIDAD MATRICIAL");
					dependencias.get(i).setTipoUnidadMatricial(dependencias.get(i).getTipo());
				}
			}
			respuesta.datos = dependencias;
			respuesta.estado = true;
			respuesta.mensaje = "Busqueda realizada correctamente.";
		}catch(Exception e){
			LOGGER.error("[ERROR]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al realizar la bÃºsqueda.";
		}
		LOGGER.info("[FIN] buscarDependencias");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/obtenerDependencia/{codigo}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<DependenciaUnidadMatricial>> obtenerDependencia(@PathVariable("codigo") String codigoDependencia, Locale locale){
		LOGGER.info("[INICIO] obtenerDependencia");
		LOGGER.info("filtros obtenerDependencia:" + codigoDependencia);
		Respuesta<DependenciaUnidadMatricial> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		try{
			//respuesta.datos = filenetService.obtenerDependencias(id);
			int codigo = Integer.valueOf(codigoDependencia);
			respuesta.estado = true;
			respuesta.mensaje = "Datos obtenidos correctamente.";
			List<DependenciaUnidadMatricial> dependencias = filenetService.buscarDependencias(codigo, "", "", ""); 
			if(dependencias != null && dependencias.size() > 0){
				if("DEPENDENCIA".equalsIgnoreCase(dependencias.get(0).getTipo())){
					dependencias.get(0).setTipo("D");
				}else{
					String textoTipoUM = dependencias.get(0).getTipo();
					dependencias.get(0).setTipo("UM");
					List<ItemFilenet> tiposUM = this.filenetService.listarTipoUnidadMatricial();
					for(ItemFilenet it : tiposUM){
						System.out.println(it.getDescripcion() + "-" + textoTipoUM);
						if(it.getDescripcion().equalsIgnoreCase(textoTipoUM)){
							dependencias.get(0).setTipoUnidadMatricial(it.getCodigo());
						}
					}
				}
			}
			respuesta.datos = dependencias;
		}catch(Exception e){
			LOGGER.error("[ERROR]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] obtenerDependencia");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/obtenerIntegrantes/{codigo}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<Integrante>> obtenerIntegrantes(@PathVariable("codigo") String codigoDependencia, Locale locale){
		LOGGER.info("[INICIO] obtenerIntegrantes");
		LOGGER.info("filtros obtenerIntegrantes:" + codigoDependencia);
		Respuesta<Integrante> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		try{
			//respuesta.datos = filenetService.obtenerDependencias(id);
			int codigo = Integer.valueOf(codigoDependencia);
			respuesta.estado = true;
			respuesta.mensaje = "Datos obtenidos correctamente.";
			List<DependenciaUnidadMatricial> dependencias = filenetService.buscarDependencias(codigo, "", "", "");
			if("DEPENDENCIA".equalsIgnoreCase(dependencias.get(0).getTipo())){
				List<Funcionario> funcionarios = filenetService.listarPersonaAsignada(codigoDependencia, "", "", "");
				List<Integrante> integrantes = new ArrayList<>();
				for(Funcionario f : funcionarios){
					Integrante in = new Integrante();
					in.setCodigoIntegrante(f.getId());
					in.setNombreIntegrante(f.getNombreApellido());
					integrantes.add(in);
				}
				respuesta.datos = integrantes;
			}else{
				List<Integrante> integrantes = filenetService.listarIntegrantesUM(codigoDependencia);
				respuesta.datos = integrantes;
			}
		}catch(Exception e){
			LOGGER.error("[ERROR]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] obtenerIntegrantes");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/verificarDatos/{id}/{codigo}/{sigla}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<Integrante>> verificarDatos(@PathVariable("id") String id, @PathVariable("codigo") String codigo, @PathVariable("sigla") String sigla, Locale locale){
		LOGGER.info("[INICIO] verificarDatos" + codigo + "-" + sigla);
		Respuesta<Integrante> respuesta = new Respuesta<>();
		respuesta.estado = true;
		try{
			int codigoDep = Integer.valueOf(codigo);
			Long idDep = Long.valueOf(id);
			// VERIFICANDO CODIGO
			List<DependenciaUnidadMatricial> dependenciasBusq = filenetService.buscarDependencias(codigoDep, "", "", "");
			if(dependenciasBusq.size() > 0){
				for(int i=0;i<dependenciasBusq.size();i++){
					if(idDep.compareTo(dependenciasBusq.get(i).getIdDependenciaUnidadMatricial()) != 0){
						respuesta.estado = false;
						respuesta.mensaje = "COD";
						break;
					}
				}
			}
			// VERIFICANDO SIGLA
			if(respuesta.estado){
				List<DependenciaUnidadMatricial> dependencias = filenetService.buscarDependencias(0, "", "", "");
				boolean encontrado = false;
				for(DependenciaUnidadMatricial dum : dependencias){
					// System.out.println(sigla + "-" + dum.getSiglas() + "||" + id + "-" + dum.getIdDependenciaUnidadMatricial() + "||" + codigo + "-" + dum.getCodigo());
					if(dum.getSiglas() != null && sigla.toUpperCase().equalsIgnoreCase(dum.getSiglas().toUpperCase()) && !codigo.equalsIgnoreCase(String.valueOf(dum.getCodigo()))){
						encontrado = true;
						break;
					}
				}
				if(encontrado){
					respuesta.estado = false;
					respuesta.mensaje = "SIG";
				}else{
	
					respuesta.estado = true;
					respuesta.mensaje = "OK";
				}
			}
		}catch(Exception e){
			LOGGER.error("[ERROR]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] verificarSiglas");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/verificarSiglas/{sigla}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<Integrante>> verificarSiglas(@PathVariable("sigla") String sigla, Locale locale){
		LOGGER.info("[INICIO] verificarSiglas" + sigla);
		Respuesta<Integrante> respuesta = new Respuesta<>();
		
		try{
			List<DependenciaUnidadMatricial> dependencias = filenetService.buscarDependencias(0, "", "", "");
			boolean encontrado = false;
			for(DependenciaUnidadMatricial dum : dependencias){
				if(dum.getSiglas() != null && sigla.toUpperCase().equalsIgnoreCase(dum.getSiglas().toUpperCase())){
					encontrado = true;
					break;
				}
			}
			if(encontrado){
				respuesta.estado = false;
				respuesta.mensaje = "Sigla encontrada";
			}else{

				respuesta.estado = true;
				respuesta.mensaje = "OK";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] verificarSiglas");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/buscarDependenciaSuperior/{jerarquia}/{codLugar}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<ItemFilenet>> buscarDependenciaSuperior(@PathVariable("jerarquia") int jerarquia, @PathVariable("codLugar") String codLugar, Locale locale){
		LOGGER.info("[INICIO] buscarDependenciaSuperior");
		LOGGER.info("filtros buscarDependenciaSuperior: " + jerarquia + " - " + codLugar);
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try{
			respuesta.datos = filenetService.listarDependenciasUnidadMatricial(jerarquia, codLugar);
			respuesta.estado = true;
			respuesta.mensaje = "Datos obtenidos correctamente.";
		}catch(Exception e){
			LOGGER.error("[buscarDependenciaSuperior]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] buscarDependenciaSuperior");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping(value = "/buscarDependenciasExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> buscarDependenciasExcel(@RequestBody FiltroConsultaDependencia filtro, Locale locale){
		LOGGER.info("[INICIO] buscarDependenciasExcel");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Funcionario> respuestaFuncionario = this.correspondeciaService.obtenerFuncionariosRuta(usuario.getToken(), "0", "", "SI", "", locale);
		List<ItemFilenet> datosFuncionarios = new ArrayList<>();
		for(Funcionario f : respuestaFuncionario.datos){
			ItemFilenet i = new ItemFilenet();
			i.setCodigo(f.getUsuario());
			i.setDescripcion(f.getNombreApellidoUsuario());
			datosFuncionarios.add(i);
		}
		List<ItemFilenet> jerarquias = this.filenetService.listarJerarquia();
		List<ItemFilenet> lugares = this.filenetService.listarLugares("");
		List<ItemFilenet> dependencias = this.filenetService.listarDependenciasNuevo("", "");
		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarDependenciasExcel(filtro, usuario.getNombreCompleto(), datosFuncionarios, 
				jerarquias, lugares, dependencias, locale);
		if(respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new InputStreamResource(respuesta.datos.get(0)));
	}
	// FIN TICKET
	
	// TICKET 9000004044
	@GetMapping(value = {"/validarExisteDocumentoRespuestaCorrespondencia/{correlativo}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<ItemFilenet>> validarExisteDocumentoRespuestaCorrespondencia(@PathVariable("correlativo") String correlativo, Locale locale){
		LOGGER.info("[INICIO] validarExisteDocumentoRespuestaCorrespondencia" + correlativo);
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		try{
			List<pe.com.petroperu.model.emision.Correspondencia> correspondencias = correspondenciaEmisionService.listarDocumentoRespuestaCorrespondencia(correlativo);
			boolean existe = false;
			if(correspondencias != null && correspondencias.size() > 0) {
				for(int i=0; i < correspondencias.size(); i++){
					pe.com.petroperu.model.emision.Correspondencia cor = correspondencias.get(i);
					if(cor.getEstadoDocumentoRespuesta() == 1){
						if(cor.getEstado().getId() != Constante.CORRESPONDENCIA_DECLINADA_FIRMADA && 
								cor.getEstado().getId() != Constante.CORRESPONDENCIA_DECLINADA_PENDIENTE && 
								cor.getEstado().getId() != Constante.CORRESPONDENCIA_ANULADA){
							existe = true;
						}
					}
				}
			}
			respuesta.estado = existe;
			respuesta.mensaje = "Datos obtenidos correctamente.";
		}catch(Exception e){
			LOGGER.error("[validarExisteDocumentoRespuestaCorrespondencia]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] validarExisteDocumentoRespuestaCorrespondencia");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/validarExisteDocumentoRespuestaAsignacion/{idAsignacion}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<ItemFilenet>> validarExisteDocumentoRespuestaAsignacion(@PathVariable("idAsignacion") Long idAsignacion, Locale locale){
		LOGGER.info("[INICIO] validarExisteDocumentoRespuestaAsignacion" + idAsignacion);
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		try{
			List<pe.com.petroperu.model.emision.Correspondencia> correspondencias = correspondenciaEmisionService.listarDocumentoRespuestaAsignacion(idAsignacion);
			boolean existe = false;
			if(correspondencias != null && correspondencias.size() > 0){
				for(int i=0; i < correspondencias.size(); i++){
					pe.com.petroperu.model.emision.Correspondencia cor = correspondencias.get(i);
					if(cor.getEstadoDocumentoRespuesta() == Constante.CORRESPONDENCIA_DOCUMENTO_RESPUESTA_ENLAZADO){
						if(cor.getEstado().getId() != Constante.CORRESPONDENCIA_DECLINADA_FIRMADA && 
								cor.getEstado().getId() != Constante.CORRESPONDENCIA_DECLINADA_PENDIENTE && 
								cor.getEstado().getId() != Constante.CORRESPONDENCIA_ANULADA){
							existe = true;
						}
					}
				}
			}
			respuesta.estado = existe;
			respuesta.mensaje = "Datos obtenidos correctamente.";
		}catch(Exception e){
			LOGGER.error("[validarExisteDocumentoRespuestaAsignacion]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] validarExisteDocumentoRespuestaAsignacion");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/verificarEstadoNumeroDocumento/{numeroDocumento}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<pe.com.petroperu.model.emision.Correspondencia>> verificarEstadoNumeroDocumento(@PathVariable("numeroDocumento") String nroDocumento, Locale locale){
		LOGGER.info("[INICIO] verificarEstadoNumeroDocumento " + nroDocumento);
		Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		try{
			List<pe.com.petroperu.model.emision.Correspondencia> correspondencias = correspondenciaEmisionService.buscarCorrespondenciaPorNumeroDocumento(nroDocumento);
			if(correspondencias != null && correspondencias.size() > 0){
				if((correspondencias.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_APROBADA || correspondencias.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ENVIADA)){
					if(!correspondencias.get(0).isEsDocumentoRespuesta()){
						respuesta.datos = correspondencias;
						respuesta.mensaje = "NÃºmero de documento encontrado correctamente.";
					}else{
						respuesta.estado = false;
						respuesta.mensaje = "El nÃºmero de documento ingresado se encuentra asociado a otra correspondencia.";
					}
				}else{
					respuesta.estado = false;
					respuesta.mensaje = "El NÃºmero de documento ingresado tiene un estado distinto a Aprobado o Enviado";
				}
			}else{
				respuesta.estado = false;
				respuesta.mensaje = "El NÃºmero de documento ingresado no existe";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] verificarEstadoNumeroDocumento ", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al hacer la verificación";
		}
		LOGGER.info("[FIN] verificarEstadoNumeroDocumento");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping(value = {"/enlazarCorrespondenciaNumeroDocumento"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<pe.com.petroperu.model.emision.Correspondencia>> enlazarCorrespondenciaNumeroDocumento(@RequestBody CorrespondenciaEnlaceDTO correspondenciaEnlace, Locale locale){
		LOGGER.info("[INICIO] enlazarCorrespondenciaNumeroDocumento " + correspondenciaEnlace.toString());
		Respuesta<pe.com.petroperu.model.emision.Correspondencia> respuesta = new Respuesta<>();
		try{
			List<pe.com.petroperu.model.emision.Correspondencia> correspondencias = correspondenciaEmisionService.buscarCorrespondenciaPorNumeroDocumento(correspondenciaEnlace.getNumeroDocumento());
			if(correspondencias != null && correspondencias.size() > 0){
				pe.com.petroperu.model.emision.Correspondencia correspondencia = correspondencias.get(0);
				correspondencia.setEsDocumentoRespuesta(true);
				correspondencia.setEstadoDocumentoRespuesta(Constante.CORRESPONDENCIA_DOCUMENTO_RESPUESTA_ENLAZADO);
				if(correspondenciaEnlace.getIdAsignacion().compareTo(0L) == 0){
					correspondencia.setCorrelativoEntrada(correspondenciaEnlace.getCorrelativo());
					correspondencia.setRespuesta(correspondenciaEnlace.getRespuesta());
				}else{
					correspondencia.setCorrelativoEntrada(correspondenciaEnlace.getCorrelativo());
					correspondencia.setRespuesta(correspondenciaEnlace.getRespuesta());
					correspondencia.setIdAsignacion(correspondenciaEnlace.getIdAsignacion());
					correspondencia.setTipo(correspondenciaEnlace.getTipoAccion());
				}
				correspondenciaDAO.save(correspondencia);
				respuesta.estado = true;
				respuesta.mensaje = "Enlace establecido correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = "No se pudo establecer el enlace, nÃºmero de documento inexistente.";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] enlazarCorrespondenciaNumeroDocumento ", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error en el sistema";
		}
		
		LOGGER.info("[FIN] enlazarCorrespondenciaNumeroDocumento");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = {"obtenerHistoricoAsignaciones/{correlativo}"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<Asignacion>> obtenerHistoricoAsignaciones(@PathVariable("correlativo") String correlativo, Locale locale){
		LOGGER.info("[INICIO] obtenerHistoricoAsignaciones " + correlativo);
		UsuarioPetroperu usuario = obtenerUsuario();
		Respuesta<Asignacion> respuestaHistorico = this.correspondeciaService
				.obtenerHistorialAsignaciones(usuario.getToken(), correlativo, locale);
		List<Asignacion> asignaciones = respuestaHistorico.datos;
		for (Asignacion a : asignaciones) {
			this.LOGGER.info("Asignado Por:" + a.getNombreApellidoAsignador());
			String[] asignadoPor = a.getNombreApellidoAsignador().split("<br>");
			List<String> asigPor = new ArrayList<String>();
			for (String ap : asignadoPor) {
				if (!ap.trim().equalsIgnoreCase("")) {
					asigPor.add(ap);
				}
			}
			String asignado = "";
			for (int i = 0; i < asigPor.size(); i++) {
				asignado = asignado + asigPor.get(i);
				if (i < asigPor.size() - 1) {
					asignado = asignado + "<br>";
				}
			}
			a.setNombreApellidoAsignador(asignado);
			this.LOGGER.info("Asignado Por:" + a.getNombreApellidoAsignador());
			this.LOGGER.info("Asignado a:" + a.getNombreApellidoAsignado());
			String[] asignadoA = a.getNombreApellidoAsignado().split("<br>");
			List<String> asigA = new ArrayList<String>();
			for (String aa : asignadoA) {
				if (!aa.trim().equalsIgnoreCase("")) {
					asigA.add(aa);
				}
			}
			String asignadoa = "";
			for (int i = 0; i < asigA.size(); i++) {
				asignadoa = asignadoa + asigA.get(i);
				if (i < asigA.size() - 1) {
					asignadoa = asignadoa + "<br>";
				}
			}
			a.setNombreApellidoAsignado(asignadoa);
		}
		return new ResponseEntity<>(respuestaHistorico, HttpStatus.OK);
	}
	// FIN TICKET
	
	// TICKET 9000004494
	@PostMapping(value = { "/consultar-correspondencias/buscar" }, produces = { "application/json" })
	private ResponseEntity<Respuesta<DataTableResults<CorrespondenciaConsulta>>> consultarCorrespondencias(){
		Respuesta<DataTableResults<CorrespondenciaConsulta>> respuesta = new Respuesta<>();
		
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET

	private boolean tieneRol(UsuarioPetroperu usuario, RolAD rol) {
		this.LOGGER.info("[ROL] " + rol.ROL_SEGURIDAD);
		boolean resultado = false;
		List<Rol> roles = this.rolService.listarRolPorUsuario(usuario.getUsername());
		for (Rol _rol : roles) {
			this.LOGGER.info("[  ROL] " + _rol.getRolSpring());
			if (_rol.getRolSpring().equals(rol.ROL_SEGURIDAD)) {
				resultado = true;
				break;
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] tieneRol");
		return resultado;
	}
	
	// TICKET 7000003969
	private boolean tieneRolSession(List<Rol> roles, RolAD rol){
		this.LOGGER.info("[ROL] " + rol.ROL_SEGURIDAD);
		boolean resultado = false;
		if(roles != null){
			for (Rol _rol : roles) {
				this.LOGGER.info("[  ROL] " + _rol.getRolSpring());
				if (_rol.getRolSpring().equals(rol.ROL_SEGURIDAD)) {
					resultado = true;
					break;
				}
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] tieneRol");
		return resultado;
	}
	// FIN TICKET
	
	private String generateKey(){
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] generateKey");
		String key = "";
		SecureRandom random = new SecureRandom();
		String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
		String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	    String NUMBER = "0123456789";
	    String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	    StringBuilder sb = new StringBuilder(10);
	    for(int i=0;i<10;i++){
	    	int indice = random.nextInt(DATA_FOR_RANDOM_STRING.length());
	    	sb.append(DATA_FOR_RANDOM_STRING.charAt(indice));
	    }
	    key = sb.toString();
		// TICKET 9000003992
		this.LOGGER.info("[FIN] generateKey");
		return key;
	}
	
	//INICIO TICKET 9000004275
	@GetMapping({ "/configuracion" })
	public ModelAndView menuConfiguracion(Locale locale) {
		this.LOGGER.info("[INICIO] menuConfiguración");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("menu_configuracion");

		view.addObject("titulo",
				messageSource.getMessage("sistcorr.administracion.configuracion.titulo", null, locale));
		view.addObject("url", messageSource.getMessage("sistcorr.correspondencia.pendientes.url", null, locale));

		view.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();

		List<Menu> listaMenu = new ArrayList<>();
		Object menuSession = session.getAttribute("menus");
		if (menuSession == null) {
			List<Menu> menus = correspondeciaService.obtenerMenuSistcorr(usuario, locale);
			session.setAttribute("menus", menus);
			listaMenu = menus;
		} else {
			listaMenu = (List<Menu>) menuSession;
		}
		view.addObject("listaMenu", listaMenu);
		view.addObject("usuario", usuario);
		view.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);

		return view;
	}

	//FIN TICKET 9000004275
	
	// TICKET 9000004961

	
	@GetMapping({ "/consulta-auditoria" })
	public ModelAndView consultaAuditoria(Locale locale) {
		this.LOGGER.info("[INICIO] consultaAuditoria");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("consulta_auditoria");
		try{
			// TICKET 7000003969
			List<Estado> listaEstados = new ArrayList<>();
			List<Estado> listaTiposCorr = new ArrayList<>();
			List<ItemFilenet> listaDep = new ArrayList<>();
			List<ItemFilenet> listaDepRem = new ArrayList<>();
			List<Rol> listaRoles = new ArrayList<>();
			List<Menu> listaMenu = new ArrayList<>();
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (requestAttributes != null)
					? ((ServletRequestAttributes) requestAttributes).getRequest()
					: null;
			HttpSession session = request.getSession();
			Object estadoSession = session.getAttribute(Constante.SESSION_ESTADOS_FILENET);
			if(estadoSession == null){
				List<Estado> estadosFilenet = correspondeciaService.listarEstadosFile("TD_CR");
				session.setAttribute(Constante.SESSION_ESTADOS_FILENET, estadosFilenet);
				listaEstados = estadosFilenet;
			}else{
				listaEstados = (List<Estado>) estadoSession;
			}
			Object tipoCorrSession = session.getAttribute(Constante.SESSION_TIPOS_CORR);
			if(tipoCorrSession == null){
				List<Estado> tiposCorr = correspondeciaService.listarTiposCorrespondencia();
				session.setAttribute(Constante.SESSION_TIPOS_CORR, tiposCorr);
				listaTiposCorr = tiposCorr;
			}else{
				listaTiposCorr = (List<Estado>) tipoCorrSession;
			}
			Object depSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_FILENET);
			if(depSession == null){
				List<ItemFilenet> dep = filenetService.listarDependencias(null, "%");
				session.setAttribute(Constante.SESSION_DEPENDENCIAS_FILENET, dep);
				listaDep = dep;
			}else{
				listaDep = (List<ItemFilenet>) depSession; 
			}
			Object depRemSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_REMITENTE_FILENET);
			if(depRemSession == null){
				List<ItemFilenet> depRem = filenetService.listarDependenciasRemitente("%");
				session.setAttribute(Constante.SESSION_DEPENDENCIAS_REMITENTE_FILENET, depRem);
				listaDepRem = depRem;
			}else{
				listaDepRem = (List<ItemFilenet>) depRemSession;
			}
			Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
			if(rolSession == null){
				List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
				session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
				listaRoles = rolesBD;
			}else{
				listaRoles = (List<Rol>) rolSession;
			}
			Object menuSession = session.getAttribute(Constante.SESSION_MENU);
			if(menuSession == null){
				List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
				session.setAttribute(Constante.SESSION_MENU, menu);
				listaMenu = menu;
			}else{
				listaMenu = (List<Menu>) menuSession;
			}
			// FIN TICKET
			FiltroConsultaCorrespondencia filtro = new FiltroConsultaCorrespondencia();
			//List<Estado> listaEstadosCorrespondencia = correspondeciaService.listarEstadosFile("TD_CR");
			List<Estado> listaEstadosCorrespondencia = listaEstados;
			//List<Estado> listaTiposCorrespondencia = correspondeciaService.listarTiposCorrespondencia();
			List<Estado> listaTiposCorrespondencia = listaTiposCorr;
			//List<ItemFilenet> listaDependencias = filenetService.listarDependencias(null, "%");
			List<ItemFilenet> listaDependencias = listaDep;
			//List<ItemFilenet> listaDependenciasRemitente = filenetService.listarDependenciasRemitente("%");
			List<ItemFilenet> listaDependenciasRemitente = listaDepRem;
			
			Respuesta<CorrespondenciaConsulta> respuesta = new Respuesta<>();
			respuesta.datos = new ArrayList<>();
			
			page.addObject("estadosCorr", listaEstadosCorrespondencia);
			page.addObject("esJefe", tieneRolSession(listaRoles, RolAD.JEFE));
			LOGGER.info("Consulta correspondencia:" + usuario.getUsername() + "|" + tieneRol(usuario, RolAD.JEFE));
			page.addObject("esGestor", tieneRolSession(listaRoles, RolAD.GESTOR));
			page.addObject("tiposCorrespondencia", listaTiposCorrespondencia);
			page.addObject("dependenciasRemitentes", listaDependenciasRemitente);
			page.addObject("dependenciasDestinatarios", listaDependencias);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			//page.addObject("listaMenu", correspondeciaService.obtenerMenuSistcorr(usuario, locale));
			page.addObject("listaMenu", listaMenu);
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource.getMessage("sistcorr.consulta.auditoria.titulo", null, locale));
			page.addObject("fechaDocumento", "10/12/2019");
			page.addObject("correspondencias", respuesta.datos);
			return page;
		}catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("redirect:/404.html");
		}
	}
	
	@PostMapping(value = "/consultar-correspondencia-auditoria-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelConsultaCorrespondenciaAuditoria(@RequestBody FiltroConsultaAuditoria filtro, Locale locale){
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelConsultaCorrespondencia");
		LOGGER.info("[FILTRO] " + filtro.toString());
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		//Respuesta<CorrespondenciaConsulta> respuestaConsulta = new Respuesta<>();
		// TICKET 9000004494 - ORDENADO POR COLUMNA CORRELATIVO, ORDENAMIENTO DESCENDENTE ACTIVADO
		Respuesta<CorrespondenciaConsulta> respuestaConsulta = this.correspondeciaService.consultarCorrespondenciaAuditoria(usuario.getToken(), filtro, 1, 1, Constante.COLUMNAS_CONSULTA_CORRESPONDENCIA[2], "SI", "SI", locale);
		LOGGER.info("Cantidad: " + respuestaConsulta.datos.size());
		//respuestaConsulta = this.correspondeciaService.consultarCorrespondencia(usuario.getToken(), filtro, locale);
		ReportExcelConsultaAuditoria reporte = new ReportExcelConsultaAuditoria(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if(respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}
	// FIN TICKET
	
	// TICKET FIN 9000004962
	@GetMapping(value = "/consulta-auditoria-salida")
	public ModelAndView consultaAuditoriaSalida(Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] consultaAuditoriaSalida");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		// TICKET 7000003969
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();
		
		List<ItemTipoCorrespondencia> listaTipoCorrEmision = new ArrayList<>();
		List<ItemFilenet> listaDependencias = new ArrayList<>();
		List<Rol> listaRoles = new ArrayList<>();
		List<Menu> listaMenu = new ArrayList<>();
		List<TipoEmision> listaTipoEmision = new ArrayList<>();
		Object tipoCorrEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_CORR_EMISION);
		if(tipoCorrEmisionSession == null){
			List<ItemTipoCorrespondencia> tiposCorr = filenetService.listarTiposCorresponciaEmision("");
			session.setAttribute(Constante.SESSION_TIPOS_CORR_EMISION, tiposCorr);
			listaTipoCorrEmision = tiposCorr;
		}else{
			listaTipoCorrEmision = (List<ItemTipoCorrespondencia>) tipoCorrEmisionSession;
		}
		Object dependenciaSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS);
		if(dependenciaSession == null){
			List<ItemFilenet> dependenciasDestino = filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), "");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS, dependenciasDestino);
			listaDependencias = dependenciasDestino;
		}else{
			listaDependencias = (List<ItemFilenet>) dependenciaSession;
		}
		Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if(rolSession == null){
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			listaRoles = rolesBD;
		}else{
			listaRoles = (List<Rol>) rolSession;
		}
		Object menuSession = session.getAttribute(Constante.SESSION_MENU);
		if(menuSession == null){
			List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
			session.setAttribute(Constante.SESSION_MENU, menu);
			listaMenu = menu;
		}else{
			listaMenu = (List<Menu>) menuSession;
		}
		Object tipoEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_EMISION);
		if(tipoEmisionSession == null){
			List<TipoEmision> tipoEmision = this.tipoEmisionService.listarTodos();
			session.setAttribute(Constante.SESSION_TIPOS_EMISION, tipoEmision);
			listaTipoEmision = tipoEmision;
		}else{
			listaTipoEmision = (List<TipoEmision>) tipoEmisionSession;
		}

		Date fechaHoraHoy = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fechaActual = sdf.format(fechaHoraHoy);		
		Calendar c = Calendar.getInstance();
		c.setTime(fechaHoraHoy);
		c.add(Calendar.DATE, -7);
		Date fechadesd = c.getTime();
		String fechaDesde = sdf.format(fechadesd);
	
		
		ModelAndView page = new ModelAndView("consulta_auditoria_salida");
		page.addObject("fechaActual",fechaActual);
		page.addObject("fechaDesde",fechaDesde);
		page.addObject("titulo", "CONSULTA DE BANDEJA DE SALIDA - AUDITORIA");
		page.addObject("usuario", usuario);
		page.addObject("dependenciasUsuario", listaDependencias);
		page.addObject("listaMenu", listaMenu);
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", listaTipoCorrEmision);
		List<String> estadosCorrespondencia = new ArrayList<>();
		estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Por firmar");
		estadosCorrespondencia.add("Firmado");
		estadosCorrespondencia.add("Rechazado");
		estadosCorrespondencia.add("Aprobado");
		estadosCorrespondencia.add("Enviado");
		estadosCorrespondencia.add("Declinada");
		boolean esJefeGestor = false;

		if(tieneRolSession(listaRoles, RolAD.JEFE) || tieneRolSession(listaRoles, RolAD.GESTOR)){
			esJefeGestor = true;
		}
		page.addObject("esJefe", esJefeGestor);
		page.addObject("estados_correspondencia", estadosCorrespondencia);
		page.addObject("listaTipoEmision", listaTipoEmision);
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		this.LOGGER.info("[FIN] consultaBandejaSalida");
		return page;
	}
	// TICKET FIN 9000004962
	
	/*INI Ticket 9000004412
	@GetMapping({ "/valijas" })
	public ModelAndView valijas(Locale locale) {
		this.LOGGER.info("[INICIO] valijas");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("valijas");
		try{
			List<Menu> listaMenu = new ArrayList<>();
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (requestAttributes != null)
					? ((ServletRequestAttributes) requestAttributes).getRequest()
					: null;
			HttpSession session = request.getSession();
			Object menuSession = session.getAttribute(Constante.SESSION_MENU);
			if(menuSession == null){
				List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
				session.setAttribute(Constante.SESSION_MENU, menu);
				listaMenu = menu;
			}else{
				listaMenu = (List<Menu>) menuSession;
			}
			List<ItemFilenet> centroGestionRemitente=this.filenetService.obtenerCentroGestionRemitente("");
			List<ItemFilenet> listacouriers = this.filenetService.listaCouriers("");
			page.addObject("listaCourier", listacouriers);
			page.addObject("centroGestionRemitente",centroGestionRemitente);
			page.addObject("titulo", "Nueva Valija");
			page.addObject("usuario", usuario);
			page.addObject("listaMenu", listaMenu);
			return page;
		}catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("redirect:/404.html");
		}
	}
	
	FIN Ticket 9000004412
	
	ticket 9000004412d
		@GetMapping({ "/venta-bases" })
	    public ModelAndView ventaBases(Locale locale) {
	          
			LOGGER.info("???vb");
			List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
			if(listParametro != null && listParametro.size() > 0) 
				this.tamanioMaxArchivoUploadSinFirmaDigital = listParametro.get(0).getValor().intValue();
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("venta_bases"); 
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.venta_bases", null, locale));
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale)); 
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision("")); 
			view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
			view.addObject("dependenciasUsuario", this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
			view.addObject("edicion", Boolean.valueOf(false));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
			view.addObject("correlativo", "");
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			view.addObject("proceso",this.filenetService.listarProcesos(""));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			
			// MONEDAS
			 //view.addObject("listaMonedas", obtenerListaTipoMoneda());  
			// CGC USUARIO
			 //String cgc = this.filenetService.obtenerCGCUsuario(usuario.getUsername()); 
			 // LISTA DEPDENDENCIA DESTINO
			 //view.addObject("dependenciaDestinos", this.filenetService.obtenerListaDepDestino(cgc));
			 		
			return view; 
	    }
		
		@GetMapping({ "/consulta-bases" })
	    public ModelAndView consultaBases(Locale locale) {
	          
			LOGGER.info("???vb");
			List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
			if(listParametro != null && listParametro.size() > 0) 
				this.tamanioMaxArchivoUploadSinFirmaDigital = listParametro.get(0).getValor().intValue();
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_bases"); 
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.consulta_bases", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			view.addObject("proceso",this.filenetService.listarProcesos(""));
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale)); 
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision("")); 
			view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
			view.addObject("dependenciasUsuario", this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
			view.addObject("edicion", Boolean.valueOf(false));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
			view.addObject("correlativo", "");
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			 
			 		
			return view; 
	    }
		
		
		@GetMapping({ "/recibir-propuesta" })
	    public ModelAndView recibirPropuesta(Locale locale) {
	          
			LOGGER.info("???rp");
			List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
			if(listParametro != null && listParametro.size() > 0) 
				this.tamanioMaxArchivoUploadSinFirmaDigital = listParametro.get(0).getValor().intValue();
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_bases"); 
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.recibir_propuesta", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale)); 
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision("")); 
			view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
			view.addObject("dependenciasUsuario", this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
			view.addObject("edicion", Boolean.valueOf(false));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
			view.addObject("correlativo", "");
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			 
			 		
			return view; 
	    }
		
		@GetMapping({ "/impugnaciones-subsanaciones" })
	    public ModelAndView impugnacionesSubsanaciones(Locale locale) {
	          
			LOGGER.info("???rp");
			List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
			if(listParametro != null && listParametro.size() > 0) 
				this.tamanioMaxArchivoUploadSinFirmaDigital = listParametro.get(0).getValor().intValue();
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("impugnaciones_subsanaciones"); 
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.impugnaciones_subsanaciones", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale)); 
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision("")); 
			view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
			view.addObject("dependenciasUsuario", this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
			view.addObject("edicion", Boolean.valueOf(false));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
			view.addObject("correlativo", "");
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			 
			 		
			return view; 
	    }
		
		@GetMapping({ "/adjuntar-documentos" })
	    public ModelAndView adjuntarDocumento(Locale locale) {
	          
			LOGGER.info("???ad");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("adjuntar_documento"); 
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.adjuntar_documento", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale)); 
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision("")); 
			view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
			view.addObject("dependenciasUsuario", this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
			view.addObject("edicion", Boolean.valueOf(false));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
			view.addObject("correlativo", "");
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			 
			 		
			return view; 
	    }
		
		@GetMapping({ "/crear-expediente" })
	    public ModelAndView crear_expediente(Locale locale) {
	          
			LOGGER.info("crear_expediente");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("crear_expediente"); 
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.crear_expediente", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale)); 
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision("")); 
			view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
			view.addObject("dependenciasUsuario", this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
			
			List<ItemFilenet> tipoProceso=this.filenetService.listarTiposProceso();
			view.addObject("tipoProceso",tipoProceso);
			view.addObject("dependencias", filenetService.listarDependenciasCEE(usuario.getUsername(), "", ""));
			view.addObject("personaContacto",filenetService.listarPersonaAsignada(null, null, "%", "SI"));
			view.addObject("edicion", Boolean.valueOf(false));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
			view.addObject("correlativo", "");
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			 
			 		
			return view; 
	    }
		
		
		@GetMapping({ "/registrar-ingreso" })
	    public ModelAndView registrar_ingreso(Locale locale) {
	          
			LOGGER.info("registrar_ingreso");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("registrar_ingreso");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.registrar_ingreso", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale)); 
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
			view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision("")); 
			view.addObject("destDocPagar", this.correspondeciaService.recuperarDestinatariosDocPorPagar().datos);//ticket 9000004765
			view.addObject("dependenciasUsuario", this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
			view.addObject("funcionario",filenetService.listarPersonaAsignada(null, null, "%", "SI"));
			view.addObject("edicion", Boolean.valueOf(false));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital);
			view.addObject("correlativo", "");
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			 
			 		
			return view; 
	    }
		
		@GetMapping({ "/registrar-salida" })
	    public ModelAndView registrar_salida(Locale locale) {
	          
			LOGGER.info("registrar-salida");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("registrar_salida");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.registrar_salida", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
			view.addObject("funcionario",filenetService.listarPersonaAsignada(null, null, "%", "SI"));
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			// TICKET 9000003908
			view.addObject("firmantes", new ArrayList());
			 
			 		
			return view; 
	    }
	 
		@GetMapping({ "/estado-digitalizacion-contrataciones" })
	    public ModelAndView estado_digitalización_contrataciones(Locale locale) {
	          
			LOGGER.info("???rs");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("estado_digitalizacion_contrataciones");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.estado_digitalizacion_contrataciones", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			 
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			 
			 
			 		
			return view; 
	    }
		
		@GetMapping({ "/consulta-despacho" })
	    public ModelAndView consulta_despacho(Locale locale) {
	          
			LOGGER.info("???cd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_despacho");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.despacho", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			
			 
			 
			 		
			return view; 
	    }
		
		@GetMapping({ "/consulta-despacho-detalle" })
	    public ModelAndView consulta_despacho_detalle(Locale locale) {
	          
			LOGGER.info("???cd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_despacho_detalle");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.despacho_detalle", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			 
			return view; 
	    }
		
		 
		@GetMapping({ "/consulta-valijas-recibidas" })
	    public ModelAndView consulta_valijas_recibidas(Locale locale) {
	          
			LOGGER.info("???cv");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_valijas_recibidas");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.consulta_valijas_recibidas", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			 
			return view; 
	    }
		
		@GetMapping({ "/consulta-valijas-recibidas-detalle" })
	    public ModelAndView consulta_valijas_recibidas_detalle(Locale locale) {
	          
			LOGGER.info("???cd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_valijas_recibidas_detalle");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.consulta_valijas_recibidas_detalle", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			 
			return view; 
	    } 
		
		@GetMapping({ "/consulta-guias-remision" })
	    public ModelAndView consulta_guias_remision(Locale locale) {
	          
			LOGGER.info("???cd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_guias_remision");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.consulta_guias_remision", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			 
			return view; 
	    }
		
		@GetMapping({ "/consulta-plantilla-guias-remision" })
	    public ModelAndView consulta_remision(Locale locale) {
	          
			LOGGER.info("???cd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_remision");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.consulta_remision", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			 
			return view; 
	    }
		
		@GetMapping({ "/consulta-planilla" })
	    public ModelAndView consulta_planilla(Locale locale) {
	          
			LOGGER.info("???cd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_planilla");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.consulta_planilla", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			 
			return view; 
	    }
		
		@PostMapping(value = {"/registrarValija"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<Valija>> registrarValija(@RequestBody Valija valija, Locale locale){
			this.LOGGER.info("[INICIO] registrarValija");
			Respuesta<Valija> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			//System.out.println("Dependencia:" + dependencia.toString());
			String mensajeRespuesta = "";
			String cgc="";
			cgc = this.filenetService.obtenerCGCUsuario(usuario.getUsername()); 
			mensajeRespuesta = this.filenetService.registrarValijas(valija, usuario.getUsername(), cgc,usuario.getToken(),locale);
			//System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Registro guardado correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
	 
		@PostMapping(value = {"/registrarExpediente"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<Expediente>> registrarExpediente(@RequestBody Expediente expediente, Locale locale){
			this.LOGGER.info("[INICIO] registrarExpediente");
			Respuesta<Expediente> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			//System.out.println("Dependencia:" + dependencia.toString());
			String mensajeRespuesta = "";
			String cgc="";
			mensajeRespuesta = this.filenetService.registrarExpedientes(expediente, usuario.getUsername(),usuario.getToken(),locale);
			//System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Registro guardado correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
		
		@PostMapping(value = {"/validarVentasBases"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<Valija>> validarVentasBases(@RequestBody VentaBases ventaBases, Locale locale){
			this.LOGGER.info("[INICIO] validarVentasBases");
			Respuesta<Valija> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			//System.out.println("Dependencia:" + dependencia.toString());
			String mensajeRespuesta = "";
			String cgc="";
			mensajeRespuesta = this.filenetService.validarVentaBases(ventaBases, usuario.getUsername(),usuario.getToken(),locale);
			//System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Venta Bases validado correctamente, se procede al registro";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
		
		@PostMapping(value = {"/registrarVentasBases"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<VentaBases>> registrarVentasBases(@RequestBody VentaBases ventaBases, Locale locale){
			this.LOGGER.info("[INICIO] validarVentasBases");
			Respuesta<VentaBases> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			//System.out.println("Dependencia:" + dependencia.toString());
			String mensajeRespuesta = "";
			String cgc="";
			mensajeRespuesta = this.filenetService.registrarVentaBases(ventaBases, usuario.getUsername(),usuario.getToken(),locale);
			//System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Ventas Bases registrado correctamente.";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
		
		CONSULTAS VENTAS BASES
		@PostMapping(value = {"/validarConsultaVentasBases"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<Valija>> validarConsultaVentasBases(@RequestBody VentaBases ventaBases, Locale locale){
			this.LOGGER.info("[INICIO] validarConsultaVentasBases");
			Respuesta<Valija> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			//System.out.println("Dependencia:" + dependencia.toString());
			String mensajeRespuesta = "";
			String cgc="";
			mensajeRespuesta = this.filenetService.validarConsultaVentaBases(ventaBases, usuario.getUsername(),usuario.getToken(),locale);
			//System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Venta Bases validado correctamente, se procede al registro";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
		
		
		@PostMapping(value = {"/registrarConsultaVentasBases"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<VentaBases>> registrarConsultaVentasBases(@RequestBody VentaBases ventaBases, Locale locale){
			this.LOGGER.info("[INICIO] registrarConsultaVentasBases");
			Respuesta<VentaBases> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			//System.out.println("Dependencia:" + dependencia.toString());
			String mensajeRespuesta = "";
			String cgc="";
			//mensajeRespuesta = this.filenetService.registrarVentaBases(ventaBases, usuario.getUsername(),usuario.getToken(),locale);
			mensajeRespuesta=this.filenetService.registrarConsultaVentaBases(ventaBases, usuario.getUsername(),usuario.getToken(),locale);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Ventas Bases registrado correctamente.";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
		
		TRACKING REGISTRAR INGRESO
		@PostMapping(value = {"/registrarTrackingIngreso"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<Tracking>> registrarTrackingIngreso(@RequestBody Tracking tracking, Locale locale){
			this.LOGGER.info("[INICIO] registrarTrackingIngreso");
			Respuesta<Tracking> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			String mensajeRespuesta = "";
			String modo="I";
			mensajeRespuesta = this.filenetService.registrarTracking(tracking, usuario.getUsername(),usuario.getToken(),modo,locale);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Registro guardado correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
		
		
		@GetMapping(value = "/busquedaTrackingIngeso", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Respuesta<DataTableResults<TrackingConsultaDTO>>> busquedaTrackingIngeso(
				HttpServletRequest request, Locale locale) {
			Respuesta<DataTableResults<TrackingConsultaDTO>> respuesta = new Respuesta<>();
			try {
				UsuarioPetroperu usuario = obtenerUsuario();
				String username = usuario.getUsername();
				DataTableRequestTracking dataTableRequest = new DataTableRequestTracking(request);
				int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
				Respuesta<TrackingConsultaDTO> respuestaConsultaTracking = filenetService
						.consultaTracking(username, "INGRESO",dataTableRequest.getFiltro(), locale);
				DataTableResults<TrackingConsultaDTO> dataTableResults = new DataTableResults<>();
				dataTableResults.setListOfDataObjects(respuestaConsultaTracking.datos);
				dataTableResults.setDraw(dataTableRequest.getDraw());
				dataTableResults.setRecordsFiltered(respuestaConsultaTracking.total.toString());
				dataTableResults.setRecordsTotal(respuestaConsultaTracking.total.toString());
				respuesta.datos.add(dataTableResults);
			} catch (Exception e) {
				LOGGER.error("[ERROR]", e);
			}
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		}
		
		TRACKING REGISTRAR SALIDA
		@PostMapping(value = {"/registrarTrackingSalida"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<Tracking>> registrarTrackingSalida(@RequestBody Tracking tracking, Locale locale){
			this.LOGGER.info("[INICIO] registrarTrackingSalida");
			Respuesta<Tracking> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			String mensajeRespuesta = "";
			String modo="S";
			mensajeRespuesta = this.filenetService.registrarTracking(tracking, usuario.getUsername(),usuario.getToken(),modo,locale);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Tracking salida guardado correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
		
		
		@GetMapping(value = "/busquedaTrackingSalida", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Respuesta<DataTableResults<TrackingConsultaDTO>>> busquedaTrackingSalida(
				HttpServletRequest request, Locale locale) {
			Respuesta<DataTableResults<TrackingConsultaDTO>> respuesta = new Respuesta<>();
			try {
				UsuarioPetroperu usuario = obtenerUsuario();
				String username = usuario.getUsername();
				DataTableRequestTracking dataTableRequest = new DataTableRequestTracking(request);
				int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
				Respuesta<TrackingConsultaDTO> respuestaConsultaTracking = filenetService
						.consultaTracking(username, "SALIDA",dataTableRequest.getFiltro(), locale);
				DataTableResults<TrackingConsultaDTO> dataTableResults = new DataTableResults<>();
				dataTableResults.setListOfDataObjects(respuestaConsultaTracking.datos);
				dataTableResults.setDraw(dataTableRequest.getDraw());
				dataTableResults.setRecordsFiltered(respuestaConsultaTracking.total.toString());
				dataTableResults.setRecordsTotal(respuestaConsultaTracking.total.toString());
				respuesta.datos.add(dataTableResults);
			} catch (Exception e) {
				LOGGER.error("[ERROR]", e);
			}
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		}
		
		@GetMapping({ "/solicitud-despacho" })
	    public ModelAndView solicitud_despacho(Locale locale) {
	          
			LOGGER.info("???sd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("solicitud_despacho");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.solicitud_despacho", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			  
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos()); 
			
			return view; 
	    }
		
		 
		@GetMapping({ "/incluir-documento-despacho" })
	    public ModelAndView incluir_documento_despacho(Locale locale) {
	          
			LOGGER.info("???sd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("incluir_documento_despacho");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.incluir_documento_despacho", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			  
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos()); 
			
			return view; 
	    }
		
		 
		@GetMapping({ "/generar-guias-remision" })
	    public ModelAndView generar_guias_remision(Locale locale) {
	          
			LOGGER.info("???sd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("generar_guias_remision");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.generar_guias_remision", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
			Listas deObjesplegables
			view.addObject("listaLugarTrabajo",this.filenetService.listaLugares(""));
			//view.addObject("listaAlcance",this.filenetService.)
			//view.addObject("listaCourier",this.filenetService.listaCouriersCGC(""));
			view.addObject("listaCourier",this.filenetService.listaCouriers(""));
			FiN
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);	
			
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			  
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos()); 
			
			return view; 
	    }


		@GetMapping({ "/generar-plantillas-guias-remision" })
	    public ModelAndView generar_plantillas_guias_remision(Locale locale) {
	          
			LOGGER.info("???sd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("generar_plantillas_guias_remision");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.generar_plantillas_guias_remision", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			  
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos()); 
			
			return view; 
	    }
		 
		@GetMapping({ "/generar-planillas" })
	    public ModelAndView generar_planillas(Locale locale) {
	          
			LOGGER.info("???gp");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("generar_planillas");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.generar_planillas", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			  
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos()); 
			
			return view; 
	    }
		
		 
		@GetMapping({ "/registrar-devolucion" })
	    public ModelAndView registrar_devolucion(Locale locale) {
	          
			LOGGER.info("???rd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("registrar_devolucion");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.registrar_devolucion", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			view.addObject("listaMotivos",this.filenetService.listarMotivos(""));
			  
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos()); 
			
			return view; 
	    }
		
		@GetMapping({ "/asociar-orden-servicio" })
	    public ModelAndView asociar_orden_servicio(Locale locale) {
	          
			LOGGER.info("???rd");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("asociar_orden_servicio");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.asociar_orden_servicio", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			view.addObject("nombreUsuario", usuario.getNombreCompleto());
			view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));  
		 
			view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
			view.addObject("sizeFileUploadSinFirmaDigital", this.tamanioMaxArchivoUploadSinFirmaDigital); 
			view.addObject("errores", errores); 
			view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
			  
			view.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos()); 
			
			return view; 
	    }
		
		REGISTRAR DEVOLUCION
		@PostMapping(value = {"/registrarDevolucion"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<Devolucion>> registrarDevolucion(@RequestBody Devolucion devolucion, Locale locale){
			this.LOGGER.info("[INICIO] registrarDevolucion");
			Respuesta<Devolucion> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			String mensajeRespuesta = "";
			String modo="S";
			mensajeRespuesta = this.filenetService.registrarDevolucion(devolucion, usuario.getUsername(),usuario.getToken(),locale);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Registro de devolución guardado correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}
	 
		ASOCIAR ORDEN DE SERVICIO
		
		
		@PostMapping(value = {"/asociarOrdenServicio"}, produces = {"application/json"})
		public ResponseEntity<Respuesta<OrdenServicio>> asociarOrdenServicio(@RequestBody OrdenServicio ordenServicio, Locale locale){
			this.LOGGER.info("[INICIO] asociarOrdenServicio");
			Respuesta<OrdenServicio> respuesta = new Respuesta<>();
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			UsuarioPetroperu usuario = obtenerUsuario();
			String mensajeRespuesta = "";
			String modo="S";
			mensajeRespuesta = this.filenetService.asociarOrdenServicio(ordenServicio, usuario.getUsername(),usuario.getToken(),locale);
			if("".equalsIgnoreCase(mensajeRespuesta)){
				respuesta.estado = true;
				respuesta.mensaje = "Servicio asociado correctamente";
			}else{
				respuesta.estado = false;
				respuesta.mensaje = mensajeRespuesta;
			}
			
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
			
		}

		
		
		FIN Ticket 9000004412
		*/
	
}

package pe.com.petroperu.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.firma.digital.IProcesoFirmaDigitalZip;
import pe.com.petroperu.model.Devolucion;
import pe.com.petroperu.model.Expediente;
import pe.com.petroperu.model.Menu;
import pe.com.petroperu.model.OrdenServicio;
import pe.com.petroperu.model.Tracking;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.Valija;
import pe.com.petroperu.model.VentaBases;
import pe.com.petroperu.model.emision.Parametro;
import pe.com.petroperu.model.emision.dto.ContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.DespachoConsultaDTO;
import pe.com.petroperu.model.emision.dto.EstDigContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.FuncionariosDTO;
import pe.com.petroperu.model.emision.dto.TrackingConsultaDTO;
import pe.com.petroperu.model.emision.dto.ValijasRecibidasDTO;
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
import pe.com.petroperu.service.dto.FiltroConsultaEstDigContratacion;
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
import pe.com.petroperu.util.datatable.DataTableResults;
import pe.com.petroperu.util.datatable.entity.DataTableRequestFuncionariosConsulta;
import pe.com.petroperu.util.datatable.entity.DataTableRequestTracking;
import pe.com.petroperu.util.datatable.entity.DatatableRequestConsultaContratacion;
import pe.com.petroperu.util.datatable.entity.DatatableRequestConsultaDespacho;
import pe.com.petroperu.util.datatable.entity.DatatableRequestConsultaEstDigContratacion;
import pe.com.petroperu.util.datatable.entity.DatatableRequestConsultaValijasRecibidas;

import pe.com.petroperu.model.Planilla;
import pe.com.petroperu.model.PlanillaGuiaRemision;

@Controller
@RequestMapping({ "/app/" })
@PropertySource({ "classpath:application.properties" })
public class TramiteDocumentarioController {

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
	
	/*INI Ticket 9000004412*/

	@PostMapping(value = {"/generarPlanilla"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<Planilla>> generarPlanilla(@RequestBody Planilla data){
		this.LOGGER.info("[INICIO] registrarValija");
		Respuesta<Planilla> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		UsuarioPetroperu usuario = obtenerUsuario(); 
		String mensajeRespuesta = "";
		 System.out.println("usuario:" + usuario.getUsername());
		 System.out.println("alcance:" + data.getAlcance());
		 System.out.println("courier:" + data.getCourier());
		 System.out.println("urgente:" + data.getUrgente());

		mensajeRespuesta = this.filenetService.generarPlanilla(usuario.getUsername(),data.getAlcance(), data.getCourier(), data.getUrgente());
		 System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
		if(mensajeRespuesta != null){
			respuesta.estado = true;
			respuesta.mensaje = mensajeRespuesta;
		} else {
			respuesta.estado = false;
			respuesta.mensaje = mensajeRespuesta;
		}
		
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
		
	}
	
	@PostMapping(value = {"/generarPlanillaGuiaRemision"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<PlanillaGuiaRemision>> generarPlanillaGuiaRemision(@RequestBody PlanillaGuiaRemision data){
		this.LOGGER.info("[INICIO] registrarValija");
		Respuesta<PlanillaGuiaRemision> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		UsuarioPetroperu usuario = obtenerUsuario(); 
		String mensajeRespuesta = "";
		 System.out.println("usuario:" + usuario.getUsername());
		 System.out.println("alcance:" + data.getLugarTrabajo());
		 System.out.println("courier:" + data.getCourier());
 
		mensajeRespuesta = this.filenetService.generarPlanillaGuiaRemision(usuario.getUsername(),data.getLugarTrabajo(), data.getCourier());
		 System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
		if(mensajeRespuesta != null){
			respuesta.estado = true;
			respuesta.mensaje = mensajeRespuesta;
		} else {
			respuesta.estado = false;
			respuesta.mensaje = mensajeRespuesta;
		} 
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
		
	}
	
	@PostMapping(value = {"/generarGuiaRemision"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<Planilla>> generarGuiaRemision(@RequestBody PlanillaGuiaRemision data){
		this.LOGGER.info("[INICIO] registrarValija");
		Respuesta<Planilla> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		UsuarioPetroperu usuario = obtenerUsuario(); 
		String mensajeRespuesta = "";
		 System.out.println("usuario:" + usuario.getUsername());
		 System.out.println("alcance:" + data.getLugarTrabajo());
		 System.out.println("courier:" + data.getCourier()); 

		mensajeRespuesta = this.filenetService.generarPlanillaGuiaRemision(usuario.getUsername(),data.getLugarTrabajo(), data.getCourier());
		 System.out.println("Mensaje Respuesta:" + mensajeRespuesta);
		if(mensajeRespuesta != null){
			respuesta.estado = true;
			respuesta.mensaje = mensajeRespuesta;
		} else {
			respuesta.estado = false;
			respuesta.mensaje = mensajeRespuesta;
		} 
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
		
	}

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
	
	/*FIN Ticket 9000004412*/
	
	/*ticket 9000004412d*/
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
			//INICIO defecto 14
			view.addObject("proceso",this.filenetService.listarProcesos(""));
			//FIN defecto 14
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
			
			//view.addObject("usuarioRemitente",this.filenetService.listarUsuariosRemitentes(null, null, "%"));
			view.addObject("usuarioRemitente",this.filenetService.listarPersonaAsignada(null, "", "", ""));
			view.addObject("dependenciaRemitente",this.filenetService.listarDependenciasRemitente("%"));
			view.addObject("estado",this.filenetService.listarEstados("%"));
			
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
	          
			LOGGER.info("Consultas Valijas Recibidas");
			 
			UsuarioPetroperu usuario = obtenerUsuario();
			List<String> errores = new ArrayList<>();
			ModelAndView view = new ModelAndView("consulta_valijas_recibidas");   
			view.addObject("titulo", this.messageSource.getMessage("sistcorr.correspondencia.consulta_valijas_recibidas", null, locale)); 
			
			view.addObject("usuario", usuario);
			view.addObject("username", usuario.getUsername());
			
			List<ItemFilenet> listaCgc = this.filenetService.listaCGC("");
			List<ItemFilenet> listacouriers = this.filenetService.listaCouriers("");

			view.addObject("listaCgc", listaCgc);
			view.addObject("listaCouriers", listacouriers);
			view.addObject("estado",this.filenetService.listarEstados("%"));
			
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
		
		/*CONSULTAS VENTAS BASES*/
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
		
		/*TRACKING REGISTRAR INGRESO*/
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
		
		/*TRACKING REGISTRAR SALIDA*/
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
			/*Listas deObjesplegables*/
			view.addObject("listaLugarTrabajo",this.filenetService.listaLugares(""));
			//view.addObject("listaAlcance",this.filenetService.)
			//view.addObject("listaCourier",this.filenetService.listaCouriersCGC(""));
			view.addObject("listaCourier",this.filenetService.listaCouriers(""));
			/*FiN*/
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
			view.addObject("listaCourier",this.filenetService.listaCouriers(""));
			view.addObject("listaLugarTrabajo",this.filenetService.listaLugares(""));

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
			view.addObject("listaCourier",this.filenetService.listaCouriers(""));
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
		
		/*REGISTRAR DEVOLUCION*/
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
	 
		/*ASOCIAR ORDEN DE SERVICIO*/
		
		
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
		
		/*CONSULTAS*/
		
		/*ESTADO DIGITALIZACION CONTRATACION*/
		@GetMapping(value = "/consulta-est-dig-contrataciones", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Respuesta<DataTableResults<EstDigContratacionConsultaDTO>>> consultarEstDigContratacionesPaginado(HttpServletRequest request, Locale locale) {
			Respuesta<DataTableResults<EstDigContratacionConsultaDTO>> respuesta = new Respuesta<>();
			try {
				UsuarioPetroperu usuario = obtenerUsuario();
				String username = usuario.getUsername();

				DatatableRequestConsultaEstDigContratacion dataTableRequest = new DatatableRequestConsultaEstDigContratacion(request);
				int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

				Respuesta<EstDigContratacionConsultaDTO> respuestaConsultaEstDigContratacion = contratacionService.consultarEstDigContratacionesPaginado(username, dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

				DataTableResults<EstDigContratacionConsultaDTO> dataTableResults = new DataTableResults<>();
				dataTableResults.setListOfDataObjects(respuestaConsultaEstDigContratacion.datos);
				dataTableResults.setDraw(dataTableRequest.getDraw());
				dataTableResults.setRecordsFiltered(respuestaConsultaEstDigContratacion.total.toString());
				dataTableResults.setRecordsTotal(respuestaConsultaEstDigContratacion.total.toString());
				respuesta.datos.add(dataTableResults);

			} catch (Exception e) {
				LOGGER.error("[ERROR]", e);
			}
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		}	
		
		
		 @PostMapping(value = "/consulta-est-dig-contrataciones-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
		 public ResponseEntity<InputStreamResource> exportExcelConsultaEstDigContrataciones(@RequestBody FiltroConsultaEstDigContratacion filtro, Locale locale){
			  LOGGER.info("[INICIO] exportExcelConsulta");
			  UsuarioPetroperu usuario = obtenerUsuario();
			  String username = usuario.getUsername();
				List<Object[]> roles = rolDAO.listByUsuario(username);
				int esJefe = 0;
				for (Object[] rol : roles) {
					LOGGER.info(rol.toString()); 
					LOGGER.info("ROL:" + rol[3].toString());
					if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
						esJefe = 1; 
					}
				}
			  //Respuesta<ByteArrayInputStream> respuesta = emisionService.consultarCorrespondenciasExcelEventoDocumento(filtro, usuario.getNombreCompleto(), username, esJefe, locale);
			  Respuesta<ByteArrayInputStream> respuesta=contratacionService.consultarEstDigContratacionExcel(filtro,usuario.getNombreCompleto(),username,locale);
			  if(respuesta.estado == false) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
			  }
			  HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "inline; filename=report.xlsx");
				return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
						.body(new InputStreamResource(respuesta.datos.get(0)));
		 }
		
		 /*CONSULTA DESPACHO*/
		 @GetMapping(value = "/consulta-despacho", produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<Respuesta<DataTableResults<DespachoConsultaDTO>>> consultarDespacho(HttpServletRequest request, Locale locale) {
				Respuesta<DataTableResults<DespachoConsultaDTO>> respuesta = new Respuesta<>();
				try {
					UsuarioPetroperu usuario = obtenerUsuario();
					String username = usuario.getUsername();

					DatatableRequestConsultaDespacho dataTableRequest = new DatatableRequestConsultaDespacho(request);
					int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

					Respuesta<DespachoConsultaDTO> respuestaConsultaDespacho = contratacionService.consultaDespachoPaginado(username, dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

					DataTableResults<DespachoConsultaDTO> dataTableResults = new DataTableResults<>();
					dataTableResults.setListOfDataObjects(respuestaConsultaDespacho.datos);
					dataTableResults.setDraw(dataTableRequest.getDraw());
					dataTableResults.setRecordsFiltered(respuestaConsultaDespacho.total.toString());
					dataTableResults.setRecordsTotal(respuestaConsultaDespacho.total.toString());
					respuesta.datos.add(dataTableResults);

				} catch (Exception e) {
					LOGGER.error("[ERROR]", e);
				}
				return new ResponseEntity<>(respuesta, HttpStatus.OK);
			}		
		 
		 
		 @GetMapping(value = "/consulta-valijas-recibidas", produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<Respuesta<DataTableResults<ValijasRecibidasDTO>>> consultarValijasRecibidas(HttpServletRequest request, Locale locale) {
				Respuesta<DataTableResults<ValijasRecibidasDTO>> respuesta = new Respuesta<>();
				try {
					UsuarioPetroperu usuario = obtenerUsuario();
					String username = usuario.getUsername();

					DatatableRequestConsultaValijasRecibidas dataTableRequest = new DatatableRequestConsultaValijasRecibidas(request);
					int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

					Respuesta<ValijasRecibidasDTO> respuestaConsultaDespacho = contratacionService.consultaValijasRecibidasPaginado(username, dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

					DataTableResults<ValijasRecibidasDTO> dataTableResults = new DataTableResults<>();
					dataTableResults.setListOfDataObjects(respuestaConsultaDespacho.datos);
					dataTableResults.setDraw(dataTableRequest.getDraw());
					dataTableResults.setRecordsFiltered(respuestaConsultaDespacho.total.toString());
					dataTableResults.setRecordsTotal(respuestaConsultaDespacho.total.toString());
					respuesta.datos.add(dataTableResults);

				} catch (Exception e) {
					LOGGER.error("[ERROR]", e);
				}
				return new ResponseEntity<>(respuesta, HttpStatus.OK);
			}	
		/*FIN Ticket 9000004412*/
}

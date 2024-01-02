package pe.com.petroperu.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.Utilitario;
import pe.com.petroperu.ad.util.RolAD;
import pe.com.petroperu.cliente.model.AgregarAsignacionMasivaParametro;
import pe.com.petroperu.cliente.model.AgregarAsignacionParametro;
import pe.com.petroperu.cliente.model.AsignacionGrupal;
import pe.com.petroperu.cliente.model.AsignarDependenciaParametro;
import pe.com.petroperu.cliente.model.Bandeja;
import pe.com.petroperu.cliente.model.CerrarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.CompletarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.CorrespondenciaMerge;
import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.ListaFiltroCorrespondencia;
import pe.com.petroperu.cliente.model.RechazarAsignacionCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RechazarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RegistrarObservacion;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.controller.CorrespondenciaController;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.Asignacion;
import pe.com.petroperu.model.AsignacionConsulta;
import pe.com.petroperu.model.CopiaCorrespondencia;
import pe.com.petroperu.model.Correspondencia;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaTareaPaginado;
import pe.com.petroperu.model.FiltroSiguientePagina;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.InformacionAsignacion;
import pe.com.petroperu.model.Menu;
import pe.com.petroperu.model.Rol;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.DestinatarioExterno;
import pe.com.petroperu.service.ICorrespondenciaEmisionService;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.IDestinatarioEntidadExternoService;
import pe.com.petroperu.service.IRolService;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;
import pe.com.petroperu.util.datatable.DataTableResults;
import pe.com.petroperu.util.datatable.entity.DataTableRequestConsultaAsignacion;
import pe.com.petroperu.util.datatable.entity.DataTableRequestConsultaAuditoria;
import pe.com.petroperu.util.datatable.entity.DataTableRequestConsultaCorrespondencia;
import pe.com.petroperu.util.datatable.entity.FiltroPaginadoConsultaCorrespondencia;
import pe.com.petroperu.xml.jaxb.EntidadExternaSunatResponse;

@RestController
@RequestMapping({ "/app/correspondencias" })
@PropertySource({ "classpath:application.properties" })
public class CorrespondenciaController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ICorrespondenciaService correspondeciaService;
	
	@Autowired
	private ICorrespondenciaEmisionService correspondenciaEmisionService;
	
	@Autowired
	private IDestinatarioEntidadExternoService destinatarioEntidadExternoService;

	@Value("${sistcorr.archivo.tamanio}")
	private Integer tamanioMaxArchivo;
	
	// TICKET 9000004494
	@Value("${sistcorr.paginado.band_entrada}")
	private String tamanioBandEntrada;

	@Autowired
	private IRolService rolService;

	@GetMapping(value = { "/{bandeja}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<Bandeja>> correspondenciasPorBandeja(@PathVariable("bandeja") String bandeja,
			Locale locale) {
		Respuesta<Bandeja> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			respuesta = this.correspondeciaService.obtenerColeccion(usuario.getToken(), bandeja, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/{bandeja}/buscar" }, consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<CorrespondenciaTareaPaginado>> buscarCorrespondencia(@PathVariable("bandeja") String bandeja,
			@RequestBody ListaFiltroCorrespondencia filtro, Locale locale) {
		Respuesta<CorrespondenciaTareaPaginado> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			// TICKET 9000004944
			//respuesta = this.correspondeciaService.buscarCorrespondencia(usuario.getToken(), bandeja, filtro, locale);
			respuesta = this.correspondeciaService.buscarCorrespondencia(usuario.getToken(), bandeja, filtro, tamanioBandEntrada, locale);
			// FIN TICKET
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	//INICIO TICKET 9000004807
	@PostMapping(value = "/{bandeja}/exportarExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportarExcelCorrespondenciasAsignacionesBandejaEntrada(
			@PathVariable("bandeja") String bandeja, @RequestBody ListaFiltroCorrespondencia filtro, Locale locale) throws NoSuchMessageException, Exception {

		LOGGER.info("[INICIO] exportarExcelCorrespondenciasAsignacionesBandejaEntrada");
		UsuarioPetroperu usuario = obtenerUsuario();
		
		if (usuario == null)
			throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
		
		Respuesta<ByteArrayInputStream> respuesta = correspondeciaService.exportarExcelCorrespondenciasAsignacionesBandejaEntrada(filtro, usuario.getNombreCompleto(), usuario.getUsername(), bandeja, locale);
		if(respuesta.estado == false) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=report.xlsx");
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new InputStreamResource(respuesta.datos.get(0)));
	}
	//FIN TICKET 9000004807
	
	// TICKET 900004494
	@PostMapping(value = { "/filtrarSiguientePagina" }, consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<CorrespondenciaTareaPaginado>> filtrarCorrespondenciaSiguientePagina(@RequestBody FiltroSiguientePagina filtro, Locale locale) {
		Respuesta<CorrespondenciaTareaPaginado> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			filtro.setTotalPagina(tamanioBandEntrada);
			filtro.setTotalItemxPagina(tamanioBandEntrada);
			respuesta = this.correspondeciaService.filtraCorrespondenciasSiguientePagina(usuario.getToken(), filtro, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET

	@GetMapping(value = { "/informacion-documento-principal/{correlativo:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<InformacionDocumento>> obtenerInformacionDocumentoPrincipal(
			@PathVariable("correlativo") String correlativo, Locale locale) {
		Respuesta<InformacionDocumento> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			respuesta = this.correspondeciaService.obtenerDocumentoPrincipal(usuario.getToken(), correlativo, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping({ "/descargar/documento/{documento:.+}" })
	public ResponseEntity<byte[]> obtenerDocumento(@PathVariable("documento") String documentoId, Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		ResponseEntity<byte[]> response = this.correspondeciaService.descargarDocumentoPrincipal(usuario.getToken(),
				documentoId, locale);
		if (response != null) {
			HttpHeaders headersResponse = response.getHeaders();
			String contenType = headersResponse.get("Content-Type").get(0);
			String contenDisposition = headersResponse.get("Content-Disposition").get(0);
			String header = headersResponse.get("Content-Disposition").get(0);
			String nombreOriginal = header.split("filename=")[1];
			Utilitario utilitario = new Utilitario();
			String nombreArchivo = utilitario.nombreArchivoDescarga(nombreOriginal);
			//contenDisposition = nombreArchivo;
			//contenDisposition = contenDisposition.replaceAll(" ", "_");
			//contenDisposition = contenDisposition.replaceAll(",", " ");
			contenDisposition = "attachment; filename=\"" + nombreArchivo + "\"";
			byte[] byteFile = (byte[]) response.getBody();
			InputStream targetStream = new ByteArrayInputStream(byteFile);
			InputStreamResource resource = new InputStreamResource(targetStream);
			if (resource != null) {
				return ((ResponseEntity.BodyBuilder) ((ResponseEntity.BodyBuilder) ResponseEntity.ok()
						.header("Content-Disposition", new String[] {

								contenDisposition })).header("filename", new String[] { "sistcorr.png" }))
										.contentType(MediaType.parseMediaType(contenType)).body(byteFile);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = { "/funcionarios/{workflow:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<Funcionario>> recuperarFuncionarios(@PathVariable("workflow") String workflow,
			Locale locale) {
		Respuesta<Funcionario> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.recuperarFuncionarios(usuario.getToken(), workflow, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/asignaciones/agregar" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<Asignacion>> agregarAsignacion(@RequestBody AgregarAsignacionParametro asignacion,
			Locale locale) {
		Respuesta<Asignacion> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.agregarAsignacion(usuario.getToken(), asignacion, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/asignaciones/agregar-masivo" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<Asignacion>> agregarAsignacion(
			@RequestBody AgregarAsignacionMasivaParametro asignacion, Locale locale) {
		Respuesta<Asignacion> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.agregarAsignacionMasivo(usuario.getToken(), asignacion, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/asignaciones/enviar/{correlativo:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> enviarAsignaciones(@PathVariable("correlativo") String correlativo,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.enviarAsignaciones(usuario.getToken(), correlativo, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	//TICKET 9000003934
	@PostMapping(value = {"/actualizar/correoDestinatarioExterno"}, consumes ={"application/json"}, produces = {"application/json"})
	public ResponseEntity<Respuesta<DestinatarioExterno>> actualizarCorreoDestinatarioExterno(@RequestParam(value = "correo") String correo, @RequestParam("id") Long idCorrespondencia, @RequestParam("codDE") Long idDE, Locale locale){
		  LOGGER.info("[INICIO] actualizarCorreoDestinatarioExterno");
		 
		  UsuarioPetroperu usuario = obtenerUsuario();
		  Respuesta<DestinatarioExterno> resp = this.correspondenciaEmisionService.actualizarCorreoDestinatarioExterno(idCorrespondencia, usuario.getUsername(), idDE, correo, locale);
		  
		  return new ResponseEntity<>(resp, HttpStatus.OK);
	}
	
	// TICKET 9000003934
	@PostMapping(value = { "/consultar/EntidadesExternaNacionalSunat" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<Object>> consultarEntidadesExternaNacionalSunat(
			@RequestParam(value = "rucRazonSocial") String valor, @RequestParam("tipo") String tipo,
			Locale locale) {
		
		LOGGER.info("[INICIO] consultarEntidadesExternaNacionalSunat");

		//UsuarioPetroperu usuario = obtenerUsuario();
		
		return new ResponseEntity<>(this.destinatarioEntidadExternoService.getEntidadesExternasNacionalesSunat(valor, tipo, locale), HttpStatus.OK);
	}

	@GetMapping(value = { "/asignaciones/temporal/{correlativo:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<Asignacion>> temporalAsignaciones(@PathVariable("correlativo") String correlativo,
			Locale locale) {
		Respuesta<Asignacion> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.recuperarTemporalAsignaciones(usuario.getToken(), correlativo,
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@DeleteMapping(value = { "/asignaciones/eliminar/{idAsignacion}" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> borrarAsignacion(@PathVariable("idAsignacion") Integer idAsignacion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.borrarAsignacion(usuario.getToken(), idAsignacion, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PutMapping(value = { "/asignaciones/completar/{idAsignacion}" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> completarCorrespondencia(
			@PathVariable("idAsignacion") Integer idAsignacion,
			@RequestBody CompletarCorrespondenciaParametro correspondencia, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.completarCorrespondencia(usuario.getToken(), idAsignacion,
					correspondencia, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	//INICIO TICKET 9000004273
	@PutMapping(value = { "/asignaciones/rechazarasignacion/{idAsignacion}" }, consumes = { "application/json" }, produces = {
	"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> rechazarAsignacionCorrespondencia(
		@PathVariable("idAsignacion") Integer idAsignacion,
		@RequestBody RechazarAsignacionCorrespondenciaParametro correspondencia, Locale locale) {
	Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	try {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		respuesta = this.correspondeciaService.rechazarAsignacionCorrespondencia(usuario.getToken(), idAsignacion,
				correspondencia, locale);
	} catch (Exception e) {
		respuesta.estado = false;
		respuesta.mensaje = e.getMessage();
	}
	return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	//FIN TICKET 9000004273
	
	@PutMapping(value = { "/asignaciones/cerrar/{correlativo:.+}" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> cerrarCorrespondencia(
			@PathVariable("correlativo") String correlativo,
			@RequestBody CerrarCorrespondenciaParametro correspondencia, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = "";
			if(correspondencia.getDocumentoRespuesta() != null && !"".equalsIgnoreCase(correspondencia.getDocumentoRespuesta())){
				username = usuario.getUsername();
			}
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			if (!tieneRol(usuario, RolAD.JEFE))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.cerrarCorrespondencia(usuario.getToken(), correlativo,
					correspondencia.getObservacion(), username, correspondencia.getDocumentoRespuesta(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PutMapping(value = { "/asignaciones/aceptar/{correlativo:.+}" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> aceptarCorrespondencia(
			@PathVariable("correlativo") String correlativo,Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			if (!tieneRol(usuario, RolAD.GESTOR) && !tieneRol(usuario, RolAD.JEFE))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.aceptarCorrespondencia(usuario.getToken(), correlativo,
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	// TICKET 9000003997
	@PutMapping(value = { "/asignaciones/procesarAceptar/{nroDocumento}/{correlativo:.+}" }, consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> procesarAceptarCorrespondencia(@PathVariable("nroDocumento") String nroDocumento, @PathVariable("correlativo") String correlativo,Locale locale) {
	Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	respuesta.estado = true;
	respuesta.mensaje = "";
	try {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		if (!tieneRol(usuario, RolAD.GESTOR) && !tieneRol(usuario, RolAD.JEFE))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		respuesta = this.correspondeciaService.procesarAceptarCorrespondencia(usuario.getToken(), usuario.getUsername(), usuario.getNombreCompleto(), nroDocumento, correlativo,
				locale);
		LOGGER.info("Respuesta del service:" + respuesta.mensaje);
	} catch (Exception e) {
		respuesta.estado = false;
		respuesta.mensaje = e.getMessage();
	}
	LOGGER.info("Respuesta del service:" + respuesta.mensaje);
	return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET

	// TICKET 9000003997
	@PutMapping(value = { "/asignaciones/procesarRechazar/{nroDocumento}/{correlativo}" }, consumes = { "application/json" }, produces = {"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> procesarRechazarCorrespondencia(
			@PathVariable("nroDocumento") String nroDocumento,
			@PathVariable("correlativo") String correlativo,
			@RequestBody RechazarCorrespondenciaParametro correspondencia, Locale locale) {
		LOGGER.info("[INICIO] procesarRechazarCorrespondencia");
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			if (!tieneRol(usuario, RolAD.GESTOR) && !tieneRol(usuario, RolAD.JEFE))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.procesarRechazarCorrespondencia(usuario.getToken(), usuario.getUsername(), usuario.getNombreCompleto(), nroDocumento, correlativo,
					correspondencia.getObservacion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET
	
	@PutMapping(value = { "/asignaciones/rechazar/{correlativo:.+}" }, consumes = { "application/json" }, produces = {"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> rechazarCorrespondencia(
		@PathVariable("correlativo") String correlativo,
		@RequestBody RechazarCorrespondenciaParametro correspondencia, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			if (!tieneRol(usuario, RolAD.GESTOR) && !tieneRol(usuario, RolAD.JEFE))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.rechazarCorrespondencia(usuario.getToken(), correlativo,
					correspondencia.getObservacion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping(value = { "/recuperar/{id:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<CorrespondenciaMerge>> obtenerCorrespondencia(@PathVariable("id") String id,
			Locale locale) {
		Respuesta<CorrespondenciaMerge> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			CorrespondenciaMerge correspondenciaMerge = new CorrespondenciaMerge();
			Integer idAsignacion = null;
			String correlativo = null;
			try {
				idAsignacion = Integer.valueOf(Integer.parseInt(id));
			} catch (Exception e) {
				correlativo = id;
			}
			if (idAsignacion != null) {
				Respuesta<InformacionAsignacion> respuestaAsignacion = this.correspondeciaService
						.recuperarInformacionAsignacion(usuario.getToken(), idAsignacion, locale);
				if (!respuestaAsignacion.estado)
					throw new Exception(respuestaAsignacion.mensaje);
				correspondenciaMerge = new CorrespondenciaMerge(
						((InformacionAsignacion) respuestaAsignacion.datos.get(0)).getCorrespondencia(),
						((InformacionAsignacion) respuestaAsignacion.datos.get(0)).getAsignacion());
			}

			if (correlativo != null) {
				Respuesta<Correspondencia> respuestaCorrespondencia = this.correspondeciaService
						.recuperarCorrespondencia(usuario.getToken(), correlativo, locale);
				if (!respuestaCorrespondencia.estado)
					throw new Exception(respuestaCorrespondencia.mensaje);
				correspondenciaMerge = new CorrespondenciaMerge(respuestaCorrespondencia.datos.get(0));
			}
			respuesta.estado = true;
			respuesta.mensaje = "";
			respuesta.datos.add(correspondenciaMerge);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping(value = { "/recuperar/asignacion/{idAsignacion}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<InformacionAsignacion>> obtenerAsignacion(
			@PathVariable("idAsignacion") Integer idAsignacion, Locale locale) {
		Respuesta<InformacionAsignacion> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.recuperarInformacionAsignacion(usuario.getToken(), idAsignacion,
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping(value = { "/recuperar-menu" }, produces = { "application/json" })
	public ResponseEntity<List<Menu>> obtenerMenu(Locale locale) {
		List<Menu> menu = new ArrayList<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, true, locale);
		return new ResponseEntity<>(menu, HttpStatus.OK);
	}
	
	// TICKET 9000003514
	@GetMapping(value = { "/agregarContacto/{correlativo:.+}/{contacto:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<CopiaCorrespondencia>> agregarContacto(@PathVariable("correlativo") String correlativo,
			@PathVariable("contacto") String contacto, Locale locale) {
		Respuesta<CopiaCorrespondencia> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			respuesta = this.correspondeciaService.agregarCopiaCorrespondencia(usuario.getToken(), contacto, correlativo, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping(value = { "/eliminarContacto/{correlativo:.+}/{contacto:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> eliminarContacto(@PathVariable("correlativo") String correlativo,
			@PathVariable("contacto") String contacto, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			respuesta = this.correspondeciaService.eliminarCopiaCorrespondencia(usuario.getToken(), contacto, correlativo, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	// TICKET 7000003426
		@PostMapping(value = { "/enviarCopiaCorrespondencia/{correlativo:.+}" }, produces = { "application/json" })
		public ResponseEntity<Respuesta<RespuestaApi>> enviarCopiaCorrespondencia(@PathVariable("correlativo") String correlativo,
				@RequestBody ItemFilenet textoParametro, Locale locale) {
			Respuesta<RespuestaApi> respuesta = new Respuesta<>();
			String texto = textoParametro.getDescripcion();
			texto = texto.replaceAll(";", ",");
			try {
				UsuarioPetroperu usuario = obtenerUsuario();
				if (usuario == null)
					throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
				/*if("without{}text".equalsIgnoreCase(texto)){
					texto = "";
				}*/
				respuesta = this.correspondeciaService.enviarCopiaCorrespondencia(usuario.getToken(), texto, correlativo, locale);
			} catch (Exception e) {
				respuesta.estado = false;
				respuesta.mensaje = e.getMessage();
			}
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		}
	/*@GetMapping(value = { "/enviarCopiaCorrespondencia/{correlativo:.+}/{texto:.+}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> enviarCopiaCorrespondencia(@PathVariable("correlativo") String correlativo,
			@PathVariable("texto") String texto, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
			if("without{}text".equalsIgnoreCase(texto)){
				texto = "";
			}
			respuesta = this.correspondeciaService.enviarCopiaCorrespondencia(usuario.getToken(), texto, correlativo, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}*/
		// FIN TICKET
	
	@PostMapping(value = { "/correspondencia/buscar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<CorrespondenciaConsulta>> buscarCorrespondencias(@RequestBody FiltroConsultaCorrespondencia filtro, Locale locale){
		Respuesta<CorrespondenciaConsulta> respuesta = new Respuesta<>();
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			/*if(filtro.getNumeroDocumentoInterno().equalsIgnoreCase("")){
				filtro.setNumeroDocumentoInterno("%");
			}
			if(filtro.getNombreDependenciaExterna().equalsIgnoreCase("")){
				filtro.setNombreDependenciaExterna("%");
			}
			if(filtro.getAsunto().equalsIgnoreCase("")){
				filtro.setAsunto("%");
			}*/
			//respuesta = this.correspondeciaService.consultarCorrespondencia(usuario.getToken(), filtro, locale);
			this.LOGGER.info("Correspondencias obtenidas en consulta:" + respuesta.datos.size());
			
			/*ListaFiltroCorrespondencia filtroCorrespondencia = new ListaFiltroCorrespondencia();
			filtroCorrespondencia.setSearchCriteria(new ArrayList<>());
			Respuesta<Bandeja> respuestaPendientes = this.correspondeciaService.buscarCorrespondencia(usuario.getToken(), "Pendientes", filtroCorrespondencia, locale);
			this.LOGGER.info("Correspondencias pendientes obtenidas:" + respuestaPendientes.datos.get(0).getCorrespondencias().size());
			Respuesta<Bandeja> respuestaEnAtencion = this.correspondeciaService.buscarCorrespondencia(usuario.getToken(), "EnAtencion", filtroCorrespondencia, locale);
			this.LOGGER.info("Correspondencias en atencion obtenidas:" + respuestaEnAtencion.datos.get(0).getCorrespondencias().size());
			Respuesta<Bandeja> respuestaCompletadas = this.correspondeciaService.buscarCorrespondencia(usuario.getToken(), "Completadas", filtroCorrespondencia, locale);
			this.LOGGER.info("Correspondencias completadas obtenidas:" + respuestaCompletadas.datos.get(0).getCorrespondencias().size());*/
			
		}catch(Exception e){
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	// TICKET 9000004494
	@GetMapping(value = { "/correspondencia/consultar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<CorrespondenciaConsulta>>> consultaCorrespondencia(HttpServletRequest request, Locale locale){
		Respuesta<DataTableResults<CorrespondenciaConsulta>> respuesta = new Respuesta<>();
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestConsultaCorrespondencia dataTableRequest = new DataTableRequestConsultaCorrespondencia(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1; 
			Respuesta<CorrespondenciaConsulta> respuestaConsultaCorrespondencias = this.correspondeciaService.consultarCorrespondencia(usuario.getToken(), dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);
			LOGGER.info("ASUNTO:" + dataTableRequest.getFiltro().getAsunto());
			LOGGER.info("DRAW:" + dataTableRequest.getDraw());
			DataTableResults<CorrespondenciaConsulta> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaCorrespondencias.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaCorrespondencias.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaCorrespondencias.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch(Exception e){
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		respuesta.mensaje = "PRUEBA DENTRO METODO";
		//LOGGER.info("" + filtro.getFiltro().toString());
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET
	
	@PostMapping(value = { "/asignacion/buscar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<AsignacionConsulta>> buscarAsignacion(@RequestBody FiltroConsultaAsignacion filtro, Locale locale){
		Respuesta<AsignacionConsulta> respuesta = new Respuesta<>();
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			/*if(filtro.getNumeroDocumentoInterno().equalsIgnoreCase("")){
				filtro.setNumeroDocumentoInterno("%");
			}*/
			//respuesta = this.correspondeciaService.consultarAsignaciones(usuario.getToken(), filtro, locale);
		}catch(Exception e){
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET
	
	// TICKET 9000004494
	@GetMapping(value = { "/asignaciones/consultar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<AsignacionConsulta>>> consultaAsignacion(HttpServletRequest request, Locale locale){
		Respuesta<DataTableResults<AsignacionConsulta>> respuesta = new Respuesta<>();
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestConsultaAsignacion dataTableRequest = new DataTableRequestConsultaAsignacion(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1; 
			Respuesta<AsignacionConsulta> respuestaConsultaAsignaciones = this.correspondeciaService.consultarAsignaciones(usuario.getToken(), dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);
			//LOGGER.info("DRAW:" + dataTableRequest.getDraw());
			DataTableResults<AsignacionConsulta> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaAsignaciones.datos);
			/*for(int i=0;i<dataTableResults.getListOfDataObjects().size(); i++){
				LOGGER.info("DATOS:" + dataTableResults.getListOfDataObjects().get(i).toString());
			}*/
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaAsignaciones.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaAsignaciones.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch(Exception e){
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		respuesta.mensaje = "PRUEBA DENTRO METODO";
		//LOGGER.info("" + filtro.getFiltro().toString());
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
		// FIN TICKET
	
	// TICKET 9000004065
	
	@PutMapping(value = { "/registrar/observacion/{correlativo:.+}" }, consumes = { "application/json" }, produces = {"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> registrarObservacion(
		@PathVariable("correlativo") String correlativo,
		@RequestBody RegistrarObservacion observacion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.registrarObservacion(usuario.getToken(), correlativo,
					observacion.getObservacion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET

	// TICKET 9000004497
	@PostMapping(value = { "/asignacionGrupal/agregar" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<Asignacion>> agregarAsignacionGrupal(@RequestBody AgregarAsignacionParametro asignacion,
			Locale locale) {
		Respuesta<Asignacion> respuesta = new Respuesta<>();
		try {
			
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (requestAttributes != null)
					? ((ServletRequestAttributes) requestAttributes).getRequest()
					: null;
			HttpSession session = request.getSession();
			
			Object ObjetoAsignacionGrupal = session.getAttribute(Constante.SESSION_ASIGNACION_GRUPAL);
			if(ObjetoAsignacionGrupal == null){
				respuesta.estado =false;
				respuesta.mensaje = "Ocurrió un inconveniente, retorne a la bandeja y vuelva a intentar.";
			}else{
				AsignacionGrupal asignacionGrupal = new AsignacionGrupal();
				asignacionGrupal = (AsignacionGrupal) ObjetoAsignacionGrupal; 
							
				UsuarioPetroperu usuario = obtenerUsuario();
				if (usuario == null)
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				respuesta = this.correspondeciaService.agregarAsignacionGrupal(usuario.getToken(), asignacion, locale, asignacionGrupal);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@DeleteMapping(value = { "/asignacionGrupal/eliminar/{idAsignacion}" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> borrarAsignacionGrupal(@PathVariable("idAsignacion") Integer idAsignacion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.borrarAsignacion(usuario.getToken(), idAsignacion, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping(value = { "/asignacionGrupal/enviar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> enviarAsignacionGrupal(Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (requestAttributes != null)
					? ((ServletRequestAttributes) requestAttributes).getRequest()
					: null;
			HttpSession session = request.getSession();
			Object ObjetoAsignacionGrupal = session.getAttribute(Constante.SESSION_ASIGNACION_GRUPAL);
			  
			if(ObjetoAsignacionGrupal == null){
				respuesta.estado =false;
				respuesta.mensaje = "Ocurrió un inconveniente, retorne a la bandeja y vuelva a intentar.";
			}else{
				AsignacionGrupal asignacionGrupal = new AsignacionGrupal();
				asignacionGrupal = (AsignacionGrupal) ObjetoAsignacionGrupal;
				
				UsuarioPetroperu usuario = obtenerUsuario();
				if (usuario == null)
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				respuesta = this.correspondeciaService.enviarAsignacionGrupal(usuario.getToken(), asignacionGrupal, locale);
				session.removeAttribute(Constante.SESSION_ASIGNACION_GRUPAL);
			}
	
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	
	@GetMapping(value = { "/asignacionGrupal/temporal" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<Asignacion>> temporalAsignacionGrupal(Locale locale) {
		Respuesta<Asignacion> respuesta = new Respuesta<>();
		try {
			
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (requestAttributes != null)
					? ((ServletRequestAttributes) requestAttributes).getRequest()
					: null;
			HttpSession session = request.getSession();
			
			Object ObjetoAsignacionGrupal = session.getAttribute(Constante.SESSION_ASIGNACION_GRUPAL);
			
			if(ObjetoAsignacionGrupal == null){
				respuesta.estado =false;
				respuesta.mensaje = "Ocurrió un inconveniente, retorne a la bandeja y vuelva a intentar.";
			}else{
				AsignacionGrupal asignacionGrupal = new AsignacionGrupal();
				asignacionGrupal = (AsignacionGrupal) ObjetoAsignacionGrupal; 
				
				List<Asignacion> listAsignacionGrupal = new ArrayList<>();
				listAsignacionGrupal = asignacionGrupal.getListAsignacionGrupal();
				String grupoCorrelativo="";
				for (Asignacion nAsinacion : listAsignacionGrupal) {
					grupoCorrelativo = grupoCorrelativo + nAsinacion.getCorrelativo() + ",";
				}
				grupoCorrelativo = grupoCorrelativo.substring(0, grupoCorrelativo.length()-1);
				
				UsuarioPetroperu usuario = obtenerUsuario();
				if (usuario == null)
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				respuesta = this.correspondeciaService.recuperarTemporalAsignacionGrupal(usuario.getToken(), grupoCorrelativo, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	 
	@PostMapping(value = { "/{bandeja}/validarAsignacionGrupal" }, consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> validarAsignacionGrupal(@PathVariable("bandeja") String bandeja,
			@RequestBody AsignacionGrupal asignacion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
		
			UsuarioPetroperu usuario = obtenerUsuario();
		
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				respuesta = this.correspondeciaService.validarAsignacionGrupal(usuario.getToken(), asignacion, locale);
				if (respuesta.estado){
					RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
					HttpServletRequest request = (requestAttributes != null)
							? ((ServletRequestAttributes) requestAttributes).getRequest()
							: null;
					HttpSession session = request.getSession();
					session.setAttribute(Constante.SESSION_ASIGNACION_GRUPAL, asignacion);
				}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	// FIN TICKET
	
	private UsuarioPetroperu obtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		return null;
	}

	private boolean verificarTamanioArchivo(InformacionDocumento doc) {
		String tamanioArchivo = doc.getTamano();
		tamanioArchivo = tamanioArchivo.replaceAll(" MB", "");
		Double tamanio = Double.valueOf(Double.parseDouble(tamanioArchivo));
		if (tamanio.doubleValue() > this.tamanioMaxArchivo.intValue())
			return false;
		return true;
	}

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
		return resultado;
	}
	
	// TICKET 9000004961
	@GetMapping(value = { "/correspondencia-auditoria/consultar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<CorrespondenciaConsulta>>> consultaCorrespondenciaAuditoria(HttpServletRequest request, Locale locale){
		Respuesta<DataTableResults<CorrespondenciaConsulta>> respuesta = new Respuesta<>();
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestConsultaAuditoria dataTableRequest = new DataTableRequestConsultaAuditoria(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1; 
			Respuesta<CorrespondenciaConsulta> respuestaConsultaCorrespondencias = this.correspondeciaService.consultarCorrespondenciaAuditoria(usuario.getToken(), dataTableRequest.getFiltro(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);
			LOGGER.info("ASUNTO:" + dataTableRequest.getFiltro().getAsunto());
			LOGGER.info("DRAW:" + dataTableRequest.getDraw());
			DataTableResults<CorrespondenciaConsulta> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaCorrespondencias.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaCorrespondencias.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaCorrespondencias.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch(Exception e){
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		respuesta.mensaje = "PRUEBA DENTRO METODO";
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	// FIN TICKET
	
	/*INI Ticket 9*4413*/
	@PutMapping(value = { "/asignaciones/rechazarMPV/{correlativo:.+}" }, consumes = { "application/json" }, produces = {"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> rechazarCorrespondenciaMPV(
		@PathVariable("correlativo") String correlativo,
		@RequestBody RechazarCorrespondenciaParametro correspondencia, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			if (!tieneRol(usuario, RolAD.RECEPTOR))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			respuesta = this.correspondeciaService.rechazarCorrespondenciaMPV(usuario.getToken(), correlativo,
					correspondencia.getObservacion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@PutMapping(value = { "/asignaciones/asignarDependenciaMPV/{correlativo:.+}" }, consumes = { "application/json" }, produces = {"application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> asignarDestinatarioMPV(
			@PathVariable("correlativo") String correlativo,
			@RequestBody AsignarDependenciaParametro correspondencia, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			if (!tieneRol(usuario, RolAD.RECEPTOR))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			
			respuesta=this.correspondeciaService.asignarDependenciaMPV(usuario.getToken(),correlativo,correspondencia.getCgc(),correspondencia.getDependencia(),
									correspondencia.getAccion(),locale);
			
		}catch(Exception e){
			respuesta.estado=false;
			respuesta.mensaje=e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}	
	
	/*FIN Ticket 9*4413*/
}

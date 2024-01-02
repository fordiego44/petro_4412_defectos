package pe.com.petroperu.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.Utilitario;
import pe.com.petroperu.ad.util.RolAD;

import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.filenet.model.administracion.CgcCorrespondencia;
import pe.com.petroperu.filenet.model.administracion.CgcLugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.Ciudad;
import pe.com.petroperu.filenet.model.administracion.Courier;
import pe.com.petroperu.filenet.model.administracion.CourierLugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.Departamentos;
import pe.com.petroperu.filenet.model.administracion.DependenciaExterna;
import pe.com.petroperu.filenet.model.administracion.Distrito;
import pe.com.petroperu.filenet.model.administracion.Estado;
import pe.com.petroperu.filenet.model.administracion.FormaEnvio;
import pe.com.petroperu.filenet.model.administracion.GestorDependencia;
import pe.com.petroperu.filenet.model.administracion.Jerarquia;
import pe.com.petroperu.filenet.model.administracion.LugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.LugarTrabajoRequest;
import pe.com.petroperu.filenet.model.administracion.Moneda;
import pe.com.petroperu.filenet.model.administracion.Motivo;
import pe.com.petroperu.filenet.model.administracion.Numeradores;
import pe.com.petroperu.filenet.model.administracion.Pais;
import pe.com.petroperu.filenet.model.administracion.Provincia;
import pe.com.petroperu.filenet.model.administracion.ProvinciaLugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.TipoAccion;
import pe.com.petroperu.filenet.model.administracion.TipoComprobante;
import pe.com.petroperu.filenet.model.administracion.TipoCorrespondencia;
import pe.com.petroperu.filenet.model.administracion.TipoUnidadMatricial;
import pe.com.petroperu.filenet.model.administracion.TransaccionesCgc;
import pe.com.petroperu.filenet.model.administracion.UsuarioCgc;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.Rol;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.service.IAdministracionService;
import pe.com.petroperu.service.IFilenetService;
import pe.com.petroperu.service.IRolService;
import pe.com.petroperu.service.util.admin.ReportExcelCGC;
import pe.com.petroperu.service.util.admin.ReportExcelCgcLugarTrabajo;
import pe.com.petroperu.service.util.admin.ReportExcelCiudad;
import pe.com.petroperu.service.util.admin.ReportExcelCourier;
import pe.com.petroperu.service.util.admin.ReportExcelCourierLugarTrabajo;
import pe.com.petroperu.service.util.admin.ReportExcelDepartamentosGeograficos;
import pe.com.petroperu.service.util.admin.ReportExcelDependenciaExterna;
import pe.com.petroperu.service.util.admin.ReportExcelDistrito;
import pe.com.petroperu.service.util.admin.ReportExcelEstados;
import pe.com.petroperu.service.util.admin.ReportExcelFormaEnvio;
import pe.com.petroperu.service.util.admin.ReportExcelGestorDependencia;
import pe.com.petroperu.service.util.admin.ReportExcelJerarquia;
import pe.com.petroperu.service.util.admin.ReportExcelLugarTrabajo;
import pe.com.petroperu.service.util.admin.ReportExcelMoneda;
import pe.com.petroperu.service.util.admin.ReportExcelMotivo;
import pe.com.petroperu.service.util.admin.ReportExcelNumeradores;
import pe.com.petroperu.service.util.admin.ReportExcelPais;
import pe.com.petroperu.service.util.admin.ReportExcelProvincia;
import pe.com.petroperu.service.util.admin.ReportExcelProvinciaLugarTrabajo;
import pe.com.petroperu.service.util.admin.ReportExcelTipoAccion;
import pe.com.petroperu.service.util.admin.ReportExcelTipoComprobante;
import pe.com.petroperu.service.util.admin.ReportExcelTipoCorrespondencia;
import pe.com.petroperu.service.util.admin.ReportExcelTipoUM;
import pe.com.petroperu.service.util.admin.ReportExcelTransaccionesCGC;
import pe.com.petroperu.service.util.admin.ReportExcelUsuarioCGC;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableResults;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestCGC;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestCgcLugarTrabajo;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestCiudad;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestCourier;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestCourierLugarTrabajo;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestDepartamento;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestDependenciaExterna;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestDistrito;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestEstado;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestFormaEnvio;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestGestorDependencia;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestJerarquia;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestLugarTrabajo;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestMonedas;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestMotivo;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestNumerador;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestPais;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestProvincia;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestProvinciaLugarTrabajo;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestTipoAccion;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestTipoComprobante;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestTipoCorrespondencia;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestTipoUM;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestTransaccionesCGC;
import pe.com.petroperu.util.datatable.entity.admin.DataTableRequestUsuarioCgc;

@Controller
@RequestMapping({ "/app/configuracion" })
@PropertySource({ "classpath:application.properties" })
public class ConfiguracionController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IAdministracionService administracionService;

	@Autowired
	private IRolService rolService;

	@Autowired
	private IFilenetService fileNetService;

	@GetMapping({ "/departamentoGeografico" })
	public ModelAndView callDepartamentos(Locale locale) {
		this.LOGGER.info("[INICIO] callDepartamentos");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/departamentos");
		try {
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.conf.departamentos.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/buscarDepartamentosGeograficos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Departamentos>>> consultaDepartamentosGeograficos(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<Departamentos>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestDepartamento dataTableRequest = new DataTableRequestDepartamento(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Departamentos> respuestaDepGeofraficos = administracionService.consultarDepartamentosGeograficos(
					dataTableRequest.getFiltro().getDepartamento(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Departamentos> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaDepGeofraficos.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaDepGeofraficos.total.toString());
			dataTableResults.setRecordsTotal(respuestaDepGeofraficos.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarDepaGeograficoExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelDepartamentosGeograficos(@RequestBody Departamentos filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelDepartamentos Geograficos");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Departamentos> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarDepartamentosGeograficos(filtro.getDepartamento(), 1, 1,
				Constante.COLUMNAS_CONSULTA_DEPARTAMENTOS_GEOGRAFICOS[0], "SI", "SI", locale);

		// FIN TICKET
		ReportExcelDepartamentosGeograficos reporte = new ReportExcelDepartamentosGeograficos(respuestaConsulta.datos,
				username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	@PostMapping(value = { "/crudDepartamentosGeograficos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudDepartamentosGeograficos(@RequestBody Departamentos filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudDepartamento(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	// *************Paises****************************

	@GetMapping({ "/paises" })
	public ModelAndView callPaises(Locale locale) {
		this.LOGGER.info("[INICIO] callPaises");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/paises");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.paises.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/buscarPaises" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Pais>>> consultaPaises(HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<Pais>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestPais dataTableRequest = new DataTableRequestPais(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Pais> respuestaDepGeofraficos = administracionService.consultarPaises(
					dataTableRequest.getFiltro().getNombrePais(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Pais> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaDepGeofraficos.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaDepGeofraficos.total.toString());
			dataTableResults.setRecordsTotal(respuestaDepGeofraficos.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudPaises" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudPaises(@RequestBody Pais filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudPaises(filtro, usuario.getUsername(), filtro.getCodigoAccion(),
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarPaisesExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelPaises(@RequestBody Pais filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelPaises");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Pais> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarPaises(filtro.getNombrePais(), 1, 1,
				Constante.COLUMNAS_CONSULTA_PAISES[0], "SI", "SI", locale);

		// FIN TICKET
		ReportExcelPais reporte = new ReportExcelPais(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Jerarquias****************************

	@GetMapping({ "/jerarquias" })
	public ModelAndView callJerarquias(Locale locale) {
		this.LOGGER.info("[INICIO] callJerarquias");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/jerarquias");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.jerarquias.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarJerarquias" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Jerarquia>>> consultaJerarquias(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Jerarquia>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestJerarquia dataTableRequest = new DataTableRequestJerarquia(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Jerarquia> respuestaJerarquia = administracionService.consultarJerarquias(
					dataTableRequest.getFiltro().getNombreJerarquia(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Jerarquia> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaJerarquia.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaJerarquia.total.toString());
			dataTableResults.setRecordsTotal(respuestaJerarquia.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudJerarquias" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudJerarquia(@RequestBody Jerarquia filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudJerarquias(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarJerarquiasExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelJerarquias(@RequestBody Jerarquia filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelJerarquias");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Jerarquia> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarJerarquias(filtro.getNombreJerarquia(), 1, 1,
				Constante.COLUMNAS_CONSULTA_JERARQUIA[0], "SI", "SI", locale);

		// FIN TICKET
		ReportExcelJerarquia reporte = new ReportExcelJerarquia(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Courier****************************

	@GetMapping({ "/couriers" })
	public ModelAndView callCouriers(Locale locale) {
		this.LOGGER.info("[INICIO] callCouriers");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/couriers");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.couriers.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarCouriers" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Courier>>> consultaCouriers(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Courier>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestCourier dataTableRequest = new DataTableRequestCourier(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Courier> response = administracionService.consultarCouriers(
					dataTableRequest.getFiltro().getNombreCourier(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Courier> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudCouriers" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudCouriers(@RequestBody Courier filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudCouriers(filtro, usuario.getUsername(), filtro.getCodigoAccion(),
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarCouriersExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelJerarquias(@RequestBody Courier filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] consultarCouriersExcel");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Courier> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarCouriers(filtro.getNombreCourier(), 1, 1,
				Constante.COLUMNAS_CONSULTA_COURIERS[0], "SI", "SI", locale);

		// FIN TICKET
		ReportExcelCourier reporte = new ReportExcelCourier(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Tipo de Comprobanter****************************

	@GetMapping({ "/tipo-comprobante" })
	public ModelAndView callTipoComprobante(Locale locale) {
		this.LOGGER.info("[INICIO] callTipoComprobante");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/tipo-comprobante");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.tipo.comprobante.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarTipoComprobante" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<TipoComprobante>>> consultaTipoComprobante(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<TipoComprobante>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestTipoComprobante dataTableRequest = new DataTableRequestTipoComprobante(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<TipoComprobante> response = administracionService.consultarTipoComprobate(
					dataTableRequest.getFiltro().getNombreComprobante(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<TipoComprobante> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudTipoComprobante" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudTipoComprobante(@RequestBody TipoComprobante filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudTipoComprobante(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarTipoComprobanteExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelTipoComprobante(@RequestBody TipoComprobante filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelTipoComprobante");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<TipoComprobante> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarTipoComprobate(filtro.getNombreComprobante(), 1, 1,
				Constante.COLUMNAS_CONSULTA_TIPO_COMPROBANTE[0], "SI", "SI", locale);

		ReportExcelTipoComprobante reporte = new ReportExcelTipoComprobante(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Tipo de Moneda****************************

	@GetMapping({ "/monedas" })
	public ModelAndView callMoneda(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/monedas");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.monedas.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarMoneda" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Moneda>>> consultaMoneda(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Moneda>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestMonedas dataTableRequest = new DataTableRequestMonedas(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Moneda> response = administracionService.consultarMoneda(
					dataTableRequest.getFiltro().getDescripcion(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Moneda> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudMonedas" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudMonedas(@RequestBody Moneda filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudMonedas(filtro, usuario.getUsername(), filtro.getCodigoAccion(),
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarMonedaExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelMoneda(@RequestBody Moneda filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelMoneda");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Moneda> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarMoneda(filtro.getDescripcion(), 1, 1,
				Constante.COLUMNAS_CONSULTA_MONEDA[0], "SI", "SI", locale);

		ReportExcelMoneda reporte = new ReportExcelMoneda(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Formas de envio****************************

	@GetMapping({ "/forma-envio" })
	public ModelAndView callFormaEnvio(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/forma-envio");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.formas.envio.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarFormaEnvio" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<FormaEnvio>>> consultaFormaEnvio(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<FormaEnvio>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestFormaEnvio dataTableRequest = new DataTableRequestFormaEnvio(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<FormaEnvio> response = administracionService.consultarFormaEnvio(
					dataTableRequest.getFiltro().getDescripcion(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<FormaEnvio> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudFormaEnvio" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudFormaEnvio(@RequestBody FormaEnvio filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudFormaEnvio(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarFormaEnvioExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelFormaEnvio(@RequestBody FormaEnvio filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelFormaEnvio");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<FormaEnvio> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarFormaEnvio(filtro.getDescripcion(), 1, 1,
				Constante.COLUMNAS_CONSULTA_FORMA_ENVIO[0], "SI", "SI", locale);

		ReportExcelFormaEnvio reporte = new ReportExcelFormaEnvio(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Numeradores****************************

	@GetMapping({ "/numeradores" })
	public ModelAndView callNumeradores(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/numeradores");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.numeradores.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarNumeradores" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Numeradores>>> consultaNumeradores(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Numeradores>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestNumerador dataTableRequest = new DataTableRequestNumerador(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Numeradores> response = administracionService.consultarNumeradores(
					dataTableRequest.getFiltro().getCodigoNumerador(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Numeradores> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudNumeradores" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudNumeradores(@RequestBody Numeradores filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudNumeradores(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarNumeradorExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelNumerador(@RequestBody Numeradores filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Numeradores> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarNumeradores(filtro.getCodigoNumerador(), 1, 1,
				Constante.COLUMNAS_CONSULTA_NUMERADOR[0], "SI", "SI", locale);

		ReportExcelNumeradores reporte = new ReportExcelNumeradores(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Estado****************************

	@GetMapping({ "/estados" })
	public ModelAndView callEstados(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/estados");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.estados.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarEstados" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Estado>>> consultaEstado(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Estado>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestEstado dataTableRequest = new DataTableRequestEstado(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Estado> response = administracionService.consultarEtados(dataTableRequest.getFiltro().getEstado(),
					dataTableRequest.getFiltro().getCodigoProceso(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Estado> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudEstados" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudEstados(@RequestBody Estado filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudEstados(filtro, usuario.getUsername(), filtro.getCodigoAccion(),
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarEstadosExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelEstados(@RequestBody Estado filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelEstados");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Estado> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarEtados(filtro.getEstado(), filtro.getCodigoProceso(), 1,
				1, Constante.COLUMNAS_CONSULTA_ESTADO[0], "SI", "SI", locale);

		ReportExcelEstados reporte = new ReportExcelEstados(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Motivo****************************

	@GetMapping({ "/motivos" })
	public ModelAndView callMotivos(Locale locale) {
		this.LOGGER.info("[INICIO] callMotivos");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/motivos");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.motivo.devolucion.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarMotivos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Motivo>>> consultaMotivos(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Motivo>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestMotivo dataTableRequest = new DataTableRequestMotivo(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Motivo> response = administracionService.consultarMotivos(
					dataTableRequest.getFiltro().getNombreMotivo(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Motivo> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudMotivos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudMotivos(@RequestBody Motivo filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudMotivos(filtro, usuario.getUsername(), filtro.getCodigoAccion(),
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarMotivosExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelMotivos(@RequestBody Motivo filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] consultarMotivosExcel");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Motivo> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarMotivos(filtro.getNombreMotivo(), 1, 1,
				Constante.COLUMNAS_CONSULTA_MOTIVOS[0], "SI", "SI", locale);

		// FIN TICKET
		ReportExcelMotivo reporte = new ReportExcelMotivo(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Tipo Unidad Matricial****************************

	@GetMapping({ "/tipo-unidad-matricial" })
	public ModelAndView callTipoUnidadMatricial(Locale locale) {
		this.LOGGER.info("[INICIO] callTipoUnidadMatricial");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/tipo-unidad-matricial");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.tipo.unidad.matricial.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarTipoUM" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<TipoUnidadMatricial>>> consultarTipoUM(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<TipoUnidadMatricial>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestTipoUM dataTableRequest = new DataTableRequestTipoUM(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<TipoUnidadMatricial> response = administracionService.consultarTipoUnidadMatricial(
					dataTableRequest.getFiltro().getNombre(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<TipoUnidadMatricial> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudTipoUnidadMatricial" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudTipoUnidadMatricial(@RequestBody TipoUnidadMatricial filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudTipoUnidadMatricial(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarTipoUMExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelTipoUM(@RequestBody TipoUnidadMatricial filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelTipoUM");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<TipoUnidadMatricial> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarTipoUnidadMatricial(filtro.getNombre(), 1, 1,
				Constante.COLUMNAS_CONSULTA_TIPO_UM[0], "SI", "SI", locale);

		// FIN TICKET
		ReportExcelTipoUM reporte = new ReportExcelTipoUM(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Provincia****************************

	@GetMapping({ "/provincia" })
	public ModelAndView callProvincia(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/provincia");
		try {

			List<ItemFilenet> lstDepartamento = this.fileNetService.listarDepartamentos("");
			page.addObject("lstDepartamento", lstDepartamento);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.provincia.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarProvincia" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Provincia>>> consultaProvincia(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Provincia>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestProvincia dataTableRequest = new DataTableRequestProvincia(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Provincia> response = administracionService.consultarProvincia(
					dataTableRequest.getFiltro().getCodigoDepartamento(),
					dataTableRequest.getFiltro().getNombreProvincia(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<Provincia> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudProvincia" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudProvincia(@RequestBody Provincia filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudProvincias(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarProvinciaExcel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelProvincia(@RequestBody Provincia filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelProvincia");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Provincia> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarProvincia(filtro.getCodigoDepartamento(),
				filtro.getNombreProvincia(), 1, 1, Constante.COLUMNAS_CONSULTA_PROVINCIA[0], "SI", "SI", locale);

		ReportExcelProvincia reporte = new ReportExcelProvincia(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Ciudad****************************

	@GetMapping({ "/ciudad" })
	public ModelAndView callCiudad(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/ciudad");
		try {

			List<ItemFilenet> lstPaises = this.fileNetService.listarPaises("");
			page.addObject("listaPaises", lstPaises);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.ciudad.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarCiudad" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Ciudad>>> consultaCiudad(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Ciudad>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestCiudad dataTableRequest = new DataTableRequestCiudad(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Ciudad> response = administracionService.consultarCiudad(
					dataTableRequest.getFiltro().getCodigoPais(), dataTableRequest.getFiltro().getNombreCiudad(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<Ciudad> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudCiudad" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudCiudad(@RequestBody Ciudad filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudCiudad(filtro, usuario.getUsername(), filtro.getCodigoAccion(),
					locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelCiudad", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelCiudad(@RequestBody Ciudad filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelCiudad");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Ciudad> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarCiudad(filtro.getCodigoPais(), filtro.getNombreCiudad(),
				1, 1, Constante.COLUMNAS_CONSULTA_CIUDAD[0], "SI", "SI", locale);

		ReportExcelCiudad reporte = new ReportExcelCiudad(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************CGC****************************

	@GetMapping({ "/cgc" })
	public ModelAndView callCGC(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/cgc");
		try {

			page.addObject("usuario", usuario);
			page.addObject("lugares", this.fileNetService.listarLugaresTV(""));
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.cgc.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarCGC" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<CgcCorrespondencia>>> consultarCGCorrespondencia(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<CgcCorrespondencia>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestCGC dataTableRequest = new DataTableRequestCGC(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<CgcCorrespondencia> response = administracionService.consultarCGCorrespondencia(
					dataTableRequest.getFiltro().getCodigoLugar(), dataTableRequest.getFiltro().getNombreCGC(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<CgcCorrespondencia> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudCGC" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudCGCorrespondencia(@RequestBody CgcCorrespondencia filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudCGCorrespondencia(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelCGC", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelCGC(@RequestBody CgcCorrespondencia filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelCGC");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<CgcCorrespondencia> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarCGCorrespondencia(filtro.getCodigoLugar(),
				filtro.getNombreCGC(), 1, 1, Constante.COLUMNAS_CONSULTA_CGC[0], "SI", "SI", locale);

		ReportExcelCGC reporte = new ReportExcelCGC(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Distrito****************************

	@GetMapping({ "/distrito" })
	public ModelAndView callDistrito(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/distritos");
		try {

			List<ItemFilenet> lstDepartamento = this.fileNetService.listarDepartamentos("");
			page.addObject("lstDepartamento", lstDepartamento);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.distritos.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarDistrito" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Distrito>>> consultarDistrito(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<Distrito>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestDistrito dataTableRequest = new DataTableRequestDistrito(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<Distrito> response = administracionService.consultarDistritos(
					dataTableRequest.getFiltro().getCodigoDepartamento(),
					dataTableRequest.getFiltro().getCodigoProvincia(), dataTableRequest.getFiltro().getNombreDistrito(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<Distrito> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudDistritos" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudDistritos(@RequestBody Distrito filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudDistritos(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelDistrito", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelDistrito(@RequestBody Distrito filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelDistrito");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<Distrito> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarDistritos(filtro.getCodigoDepartamento(),
				filtro.getCodigoProvincia(), filtro.getNombreDistrito(), 1, 1, Constante.COLUMNAS_CONSULTA_DISTRITO[0],
				"SI", "SI", locale);

		ReportExcelDistrito reporte = new ReportExcelDistrito(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Usuario CGC****************************

	@GetMapping({ "/usuario-cgc" })
	public ModelAndView callUsuarioCGC(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/usuario-cgc");
		try {

			List<ItemFilenet> listaCgc = this.fileNetService.listaCGC("");
			List<Funcionario> funcionarios = this.fileNetService.listarPersonaAsignada(null, "", "", "");

			page.addObject("listaCgc", listaCgc);
			page.addObject("funcionarios", funcionarios);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.gestor.usuario.cgc.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarUsuarioCGC" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<UsuarioCgc>>> consultarUsuarioCGC(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<UsuarioCgc>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestUsuarioCgc dataTableRequest = new DataTableRequestUsuarioCgc(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<UsuarioCgc> response = administracionService.consultarUsuarioCGC(
					dataTableRequest.getFiltro().getCodigoCGC(), dataTableRequest.getFiltro().getNombreGestor(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<UsuarioCgc> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudUsuarioCGC" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudUsuarioCGC(@RequestBody UsuarioCgc filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudUsuarioCGC(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelUsuarioCGC", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelUsuarioCGC(@RequestBody UsuarioCgc filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelUsuarioCGC");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<UsuarioCgc> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarUsuarioCGC(filtro.getCodigoCGC(),
				filtro.getNombreGestor(), 1, 1, Constante.COLUMNAS_CONSULTA_USUARIO_CGC[0], "SI", "SI", locale);

		ReportExcelUsuarioCGC reporte = new ReportExcelUsuarioCGC(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Gestor Dependencia****************************

	@GetMapping({ "/gestor-dependencia" })
	public ModelAndView callGestorDependencia(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/gestor-dependencia");
		try {

			List<ItemFilenet> dependencias = this.fileNetService.listarDependenciasNuevo("", "");
			List<ItemFilenet> funcionarios = this.fileNetService.listaFuncionariosTodos();

			page.addObject("lstDependencias", dependencias);
			page.addObject("lstfuncionarios", funcionarios);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.gestor.dependencia.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarGestorDependencia" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<GestorDependencia>>> consultarGestorDependencia(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<GestorDependencia>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestGestorDependencia dataTableRequest = new DataTableRequestGestorDependencia(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<GestorDependencia> response = administracionService.consultarGestorDependencia(
					dataTableRequest.getFiltro().getNombreDependencia(), dataTableRequest.getFiltro().getNombreGestor(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<GestorDependencia> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudGestorDependencia" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudGestorDependencia(@RequestBody GestorDependencia filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudGestorDependencia(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelGestorDependencia", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelGestorDependencia(@RequestBody GestorDependencia filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelGestorDependencia");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<GestorDependencia> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarGestorDependencia(filtro.getNombreDependencia(),
				filtro.getNombreGestor(), 1, 1, Constante.COLUMNAS_CONSULTA_GESTOR_DEPENDENCIA[0], "SI", "SI", locale);

		ReportExcelGestorDependencia reporte = new ReportExcelGestorDependencia(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Lugares de Trabajo ****************************

	@GetMapping({ "/lugar-trabajo" })
	public ModelAndView callLugaresDeTrabajo(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/lugar-trabajo");
		try {

			List<ItemFilenet> lstDepartamento = this.fileNetService.listarDepartamentos("");
			page.addObject("lstDepartamento", lstDepartamento);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.gestor.lugar.trabajo.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarLugaresDeTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<LugarTrabajo>>> consultarLugaresDeTrabajo(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<LugarTrabajo>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestLugarTrabajo dataTableRequest = new DataTableRequestLugarTrabajo(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			LugarTrabajoRequest requestLugarTrabajo = new LugarTrabajoRequest();

			// (columnaOrden == null) ? "" : columnaOrden

			requestLugarTrabajo.setCodigoLugar(dataTableRequest.getFiltro().getCodigoLugar());
			requestLugarTrabajo.setNombreLugar(dataTableRequest.getFiltro().getNombreLugar());
			requestLugarTrabajo.setCodigoDepartamento(dataTableRequest.getFiltro().getCodigoDepartamento());
			requestLugarTrabajo.setCodigoProvincia(dataTableRequest.getFiltro().getCodigoProvincia());
			requestLugarTrabajo.setCodigoDistrito(dataTableRequest.getFiltro().getCodigoDistrito());
			requestLugarTrabajo.setItemsPorPagina(dataTableRequest.getLength());
			requestLugarTrabajo.setNumeroPagina(start);
			requestLugarTrabajo.setColumnaOrden(dataTableRequest.getColumna());
			requestLugarTrabajo.setOrden(dataTableRequest.getOrdenDesc());

			Respuesta<LugarTrabajo> response = administracionService.consultarLugaresDeTrabajo(requestLugarTrabajo,
					"NO", locale);

			DataTableResults<LugarTrabajo> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudLugaresDeTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudLugaresDeTrabajo(@RequestBody LugarTrabajo filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudLugaresDeTrabajo(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelLugarTrabajo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelLugarTrabajo(@RequestBody LugarTrabajo filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelLugarTrabajo");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<LugarTrabajo> respuestaConsulta = new Respuesta<>();

		LugarTrabajoRequest requestLugarTrabajo = new LugarTrabajoRequest();

		// (columnaOrden == null) ? "" : columnaOrden

		requestLugarTrabajo.setCodigoLugar(filtro.getCodigoLugar());
		requestLugarTrabajo.setNombreLugar(filtro.getNombreLugar());
		requestLugarTrabajo.setCodigoDepartamento(filtro.getCodigoDepartamento());
		requestLugarTrabajo.setCodigoProvincia(filtro.getCodigoProvincia());
		requestLugarTrabajo.setCodigoDistrito(filtro.getCodigoDistrito());
		requestLugarTrabajo.setItemsPorPagina(1);
		requestLugarTrabajo.setNumeroPagina(1);
		requestLugarTrabajo.setColumnaOrden(Constante.COLUMNAS_CONSULTA_LUGARES_TRABAJO[0]);
		requestLugarTrabajo.setOrden("SI");

		respuestaConsulta = this.administracionService.consultarLugaresDeTrabajo(requestLugarTrabajo, "SI", locale);

		ReportExcelLugarTrabajo reporte = new ReportExcelLugarTrabajo(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************courier_lugar de trabajo****************************

	@GetMapping({ "/courier-lugar-trabajo" })
	public ModelAndView callCourierLugarTrabajo(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/courier-lugar-trabajo");
		try {

			List<ItemFilenet> listaCgc = this.fileNetService.listaCGC("");
			List<ItemFilenet> listacouriers = this.fileNetService.listaCouriers("");
			page.addObject("listaCgc", listaCgc);
			page.addObject("listaCouriers", listacouriers);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource.getMessage(
					"sistcorr.administracion.config.gestor.relacion.courier.lugar.trabajo.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarCourierLugarTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<CourierLugarTrabajo>>> consultarCourierLugarTrabajo(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<CourierLugarTrabajo>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestCourierLugarTrabajo dataTableRequest = new DataTableRequestCourierLugarTrabajo(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<CourierLugarTrabajo> response = administracionService.consultarCourierLugarTrabajo(
					dataTableRequest.getFiltro().getCodigoCGC(), dataTableRequest.getFiltro().getCodigoCourier(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<CourierLugarTrabajo> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudCourierLugarTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudCourierLugarTrabajo(@RequestBody CourierLugarTrabajo filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudCourierLugarTrabajo(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelCourierLugarTrabajo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelCourierLugarTrabajo(@RequestBody CourierLugarTrabajo filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelCourierLugarTrabajo");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<CourierLugarTrabajo> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarCourierLugarTrabajo(filtro.getCodigoCGC(),
				filtro.getCodigoCourier(), 1, 1, Constante.COLUMNAS_CONSULTA_COURIER_LUGAR_TRABAJO[0], "SI", "SI",
				locale);

		ReportExcelCourierLugarTrabajo reporte = new ReportExcelCourierLugarTrabajo(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************consultar Provincia Lugar
	// Trabajo****************************

	@GetMapping({ "/provincia-lugar-trabajo" })
	public ModelAndView callProvinciaLugarTrabajo(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/provincia-lugar-trabajo");
		try {

			List<ItemFilenet> lstDepartamento = this.fileNetService.listarDepartamentos("");
			List<ItemFilenet> lstLugares = this.fileNetService.listaLugares("");

			page.addObject("lstDepartamento", lstDepartamento);
			page.addObject("lstLugares", lstLugares);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource.getMessage(
					"sistcorr.administracion.config.gestor.relacion.provincia.lugar.trabajo.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarProvinciaLugarTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<ProvinciaLugarTrabajo>>> consultarProvinciaLugarTrabajo(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ProvinciaLugarTrabajo>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestProvinciaLugarTrabajo dataTableRequest = new DataTableRequestProvinciaLugarTrabajo(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<ProvinciaLugarTrabajo> response = administracionService.consultarProvinciaLugarTrabajo(
					dataTableRequest.getFiltro().getCodigoDepartamento(),
					dataTableRequest.getFiltro().getCodigoProvincia(), dataTableRequest.getFiltro().getCodigoLugar(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<ProvinciaLugarTrabajo> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudProvinciaLugarTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudProvinciaLugarTrabajo(@RequestBody ProvinciaLugarTrabajo filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudProvinciaLugarTrabajo(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelProvinciaLugarTrabajo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelProvinciaLugarTrabajo(
			@RequestBody ProvinciaLugarTrabajo filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelProvinciaLugarTrabajo");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<ProvinciaLugarTrabajo> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarProvinciaLugarTrabajo(filtro.getCodigoDepartamento(),
				filtro.getCodigoProvincia(), filtro.getCodigoLugar(), 1, 1,
				Constante.COLUMNAS_CONSULTA_PROVINCIA_LUGAR_TRABAJO[0], "SI", "SI", locale);

		ReportExcelProvinciaLugarTrabajo reporte = new ReportExcelProvinciaLugarTrabajo(respuestaConsulta.datos,
				username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Dependencia Externa****************************

	@GetMapping({ "/dependencia-externa" })
	public ModelAndView callDependenciaExterna(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/dependencia-externa");
		try {

			List<ItemFilenet> lstDepartamento = this.fileNetService.listarDepartamentos("");
			List<ItemFilenet> lstPaises = this.fileNetService.listarPaises("");

			page.addObject("lstDepartamento", lstDepartamento);
			page.addObject("listaPaises", lstPaises);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.dependencia.externa.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarDependenciaExterna" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<DependenciaExterna>>> consultarDependenciaExterna(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<DependenciaExterna>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestDependenciaExterna dataTableRequest = new DataTableRequestDependenciaExterna(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<DependenciaExterna> response = administracionService.consultarDependenciaExterna(
					dataTableRequest.getFiltro().getNombreDependencia(), dataTableRequest.getFiltro().getRuc(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<DependenciaExterna> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudDependenciaExterna" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudDependenciaExterna(@RequestBody DependenciaExterna filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudDependenciaExterna(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelDependenciaExterna", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelDependenciaExterna(@RequestBody DependenciaExterna filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelDependenciaExterna");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<DependenciaExterna> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarDependenciaExterna(filtro.getNombreDependencia(),
				filtro.getRuc(), 1, 1, Constante.COLUMNAS_CONSULTA_DEPENDENCIA_EXTERNA[0], "SI", "SI", locale);

		ReportExcelDependenciaExterna reporte = new ReportExcelDependenciaExterna(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Tipo de Acción****************************

	@GetMapping({ "/tipo-accion" })
	public ModelAndView callTipoAccion(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/tipo-accion");
		try {
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo",
					this.messageSource.getMessage("sistcorr.administracion.config.tipo.accion.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarTipoAccion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<TipoAccion>>> consultarTipoAccion(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<TipoAccion>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestTipoAccion dataTableRequest = new DataTableRequestTipoAccion(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<TipoAccion> response = administracionService.consultarTipoAccion(
					dataTableRequest.getFiltro().getNombreAccion(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<TipoAccion> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudTipoAccion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudTipoAccion(@RequestBody TipoAccion filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudTipoAccion(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelTipoAccion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelTipoAccion(@RequestBody TipoAccion filtro, Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelTipoAccion");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<TipoAccion> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarTipoAccion(filtro.getNombreAccion(), 1, 1,
				Constante.COLUMNAS_CONSULTA_TIPO_ACCION[0], "SI", "SI", locale);

		ReportExcelTipoAccion reporte = new ReportExcelTipoAccion(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Tipos de Correspondencias****************************

	@GetMapping({ "/tipo-correspondencia" })
	public ModelAndView callTipoCorrespondencia(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/tipo-correspondencia");
		try {

			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.tipo.correspondencia.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarTipoCorrespondencia" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<TipoCorrespondencia>>> consultarTipoCorrespondencia(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<TipoCorrespondencia>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestTipoCorrespondencia dataTableRequest = new DataTableRequestTipoCorrespondencia(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<TipoCorrespondencia> response = administracionService.consultarTipoCorrespondencia(
					dataTableRequest.getFiltro().getNombreTipoCorr(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<TipoCorrespondencia> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudTipoCorrespondencia" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudTipoCorrespondencia(@RequestBody TipoCorrespondencia filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudTipoCorrespondencia(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelTipoCorrespondencia", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelTipoCorrespondencia(@RequestBody TipoCorrespondencia filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelTipoCorrespondencia");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<TipoCorrespondencia> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarTipoCorrespondencia(filtro.getNombreTipoCorr(), 1, 1,
				Constante.COLUMNAS_CONSULTA_TIPO_CORRESPONDENCIA[0], "SI", "SI", locale);

		ReportExcelTipoCorrespondencia reporte = new ReportExcelTipoCorrespondencia(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************Transacciones por CGC****************************

	@GetMapping({ "/transacciones-cgc" })
	public ModelAndView callTransaccionesCGC(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/transacciones-cgc");
		try {

			List<ItemFilenet> listaCgc = this.fileNetService.listaCGC("");
			List<ItemFilenet> listaNumerador = this.fileNetService.listaNumeradores();

			page.addObject("listaCgc", listaCgc);
			page.addObject("listaNumerador", listaNumerador);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.transacciones.cgc.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarTransaccionesCGC" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<TransaccionesCgc>>> consultarTransaccionesCGC(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<TransaccionesCgc>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestTransaccionesCGC dataTableRequest = new DataTableRequestTransaccionesCGC(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<TransaccionesCgc> response = administracionService.consultarTransaccionesCGC(
					dataTableRequest.getFiltro().getTipoTransaccion(), dataTableRequest.getFiltro().getCgcOrigen(),
					dataTableRequest.getFiltro().getCgcDestino(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);

			DataTableResults<TransaccionesCgc> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudTransaccionesCGC" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudTransaccionesCGC(@RequestBody TransaccionesCgc filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudTransaccionesCGC(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelTransaccionesCGC", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelTransaccionesCGC(@RequestBody TransaccionesCgc filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelTransaccionesCGC");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<TransaccionesCgc> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarTransaccionesCGC(filtro.getTipoTransaccion(),
				filtro.getCgcOrigen(), filtro.getCgcDestino(), 1, 1, Constante.COLUMNAS_CONSULTA_TRANSACCIONES_CGC[0],
				"SI", "SI", locale);

		ReportExcelTransaccionesCGC reporte = new ReportExcelTransaccionesCGC(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// *************CGC Lugares de Trabajo****************************

	@GetMapping({ "/cgc-lugar-trabajo" })
	public ModelAndView callCgcLugarTrabajo(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403.html");
		ModelAndView page = new ModelAndView("administracion/configuracion/cgc-lugar-trabajo");
		try {

			List<ItemFilenet> listaCgc = this.fileNetService.listaCGC("");
			List<ItemFilenet> lstLugares = this.fileNetService.listaLugares("");

			page.addObject("listaCgc", listaCgc);
			page.addObject("listaLugares", lstLugares);
			page.addObject("usuario", usuario);
			page.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
			page.addObject("formatoFecha", Utilitario.FORMATO_FECHA_FRONT);
			page.addObject("titulo", this.messageSource
					.getMessage("sistcorr.administracion.config.cgc.lugar.trabajo.titulo", null, locale));

			return page;
		} catch (Exception e) {
			return new ModelAndView("redirect:/404.html");
		}
	}

	@GetMapping(value = { "/consultarCgcLugarTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<CgcLugarTrabajo>>> consultarCgcLugarTrabajo(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<CgcLugarTrabajo>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			DataTableRequestCgcLugarTrabajo dataTableRequest = new DataTableRequestCgcLugarTrabajo(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;

			Respuesta<CgcLugarTrabajo> response = administracionService.consultarCgcLugarTrabajo(
					dataTableRequest.getFiltro().getCodigoCgc(), dataTableRequest.getFiltro().getCodigoLugar(),
					dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(),
					"NO", locale);

			DataTableResults<CgcLugarTrabajo> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(response.datos);

			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(response.total.toString());
			dataTableResults.setRecordsTotal(response.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		respuesta.estado = true;
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/crudCgcLugarTrabajo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> crudCgcLugarTrabajo(@RequestBody CgcLugarTrabajo filtro,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();

			respuesta = this.administracionService.crudCgcLugarTrabajo(filtro, usuario.getUsername(),
					filtro.getCodigoAccion(), locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultarExcelCgcLugarTrabajo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportExcelCgcLugarTrabajo(@RequestBody CgcLugarTrabajo filtro,
			Locale locale) {
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportExcelCgcLugarTrabajo");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<CgcLugarTrabajo> respuestaConsulta = new Respuesta<>();
		respuestaConsulta = this.administracionService.consultarCgcLugarTrabajo(filtro.getCodigoCgc(),
				filtro.getCodigoLugar(), 1, 1, Constante.COLUMNAS_CONSULTA_CGC_LUGAR_TRABAJO[0], "SI", "SI", locale);

		ReportExcelCgcLugarTrabajo reporte = new ReportExcelCgcLugarTrabajo(respuestaConsulta.datos, username);
		reporte.prepareRequest();
		reporte.process();
		respuesta.estado = true;
		respuesta.datos.add(reporte.getResult());
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	@GetMapping(value = { "/buscar/ciudad" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> select2Ciudad(
			@RequestParam(value = "codPais", defaultValue = "") String codDepartamento,
			@RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		respuesta.datos.addAll(this.fileNetService.listarCiudadesPorPais(codDepartamento, texto));
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	private UsuarioPetroperu obtenerUsuario() {
		this.LOGGER.info("[INICIO] obtenerUsuario");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		this.LOGGER.info("[FIN] obtenerUsuario");
		return null;
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
		this.LOGGER.info("[FIN] tieneRol");
		return resultado;
	}
}

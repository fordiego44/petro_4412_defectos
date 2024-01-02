package pe.com.petroperu.controller;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.ad.util.RolAD;
import pe.com.petroperu.cliente.model.ListaFiltroConductor;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.filenet.model.ItemTipoCorrespondencia;
import pe.com.petroperu.model.Conductor;
import pe.com.petroperu.model.ConductorPaginado;
import pe.com.petroperu.model.ContinuationData;
import pe.com.petroperu.model.Estado;
import pe.com.petroperu.model.FiltroSiguientePagina;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.Menu;
import pe.com.petroperu.model.ReemplazoAdicion;
import pe.com.petroperu.model.Rol;
import pe.com.petroperu.model.Traza;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.TipoEmision;
import pe.com.petroperu.model.emision.dto.FuncionariosDTO;
import pe.com.petroperu.model.emision.dto.LogDTO;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;
import pe.com.petroperu.model.emision.dto.ReemplazoRolesAnterioresConsultaDTO;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.IFilenetService;
import pe.com.petroperu.service.IRolService;
import pe.com.petroperu.service.ITipoEmisionService;
import pe.com.petroperu.service.dto.FiltroConsultaFuncionariosDTO;
import pe.com.petroperu.service.dto.FiltroConsultaLogDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoRolesAnterioresDTO;
import pe.com.petroperu.service.util.admin.ReportExcelConductor;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableResults;
import pe.com.petroperu.util.datatable.entity.DataTableRequestConsultaReemplazoDTO;
import pe.com.petroperu.util.datatable.entity.DataTableRequestFuncionariosConsulta;
import pe.com.petroperu.util.datatable.entity.DataTableRequestLogConsulta;
import pe.com.petroperu.util.datatable.entity.DataTableRequestReemplazoConsulta;

@Controller
@RequestMapping({ "/app/" })
@PropertySource({ "classpath:application.properties" })
public class AdministracionController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IFilenetService filenetService;

	@Autowired
	private ICorrespondenciaService correspondeciaService;

	@Autowired
	private ITipoEmisionService tipoEmisionService;

	@Autowired
	private IRolService rolService;
	
	@Value("${sistcorr.paginado.conductor}")
	private String tamanioBandConfConductor;
	
	@Value("${sistcorr.archivo.max_size}")
	private Integer tamanioMaxArchivoUpload;

	@GetMapping(value = "/conductor")
	public ModelAndView conductor(Locale locale) {

		this.LOGGER.info("[INICIO] conductor");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("administracion/admin_conductor");

		page.addObject("titulo", "CONDUCTOR");
		page.addObject("usuario", usuario);
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", filenetService.listarTiposCorresponciaEmision(""));
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] conductor");
		return page;
	}

	@GetMapping(value = "/detalleConductor/{id}")
	public ModelAndView detalleConductor(Locale locale, @PathVariable("id") String id) {

		this.LOGGER.info("[INICIO] detalleConductor");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("administracion/conductor_detalle");

		if (id != null && !id.trim().equalsIgnoreCase("") && !id.trim().equalsIgnoreCase("0")) {
			Respuesta<Traza> respuestaTraza = this.correspondeciaService.obtenerListaTrazas(usuario.getToken(), id,
					locale);
			if (!respuestaTraza.estado) {
				page.addObject("eventos", new ArrayList<>());
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

				page.addObject("eventos", respuestaTraza.datos);
			}
		} else {
			page.addObject("eventos", new ArrayList<>());
		}

		page.addObject("titulo", "TAREA");
		page.addObject("usuario", usuario);
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", filenetService.listarTiposCorresponciaEmision(""));
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] detalleConductor");
		return page;
	}

	@GetMapping(value = "/consultalog")
	public ModelAndView consultaLog(Locale locale) {
		this.LOGGER.info("[INICIO] consultaLog");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("administracion/consultalog");
		
		/* Acciones */
		List<String> acciones = new ArrayList<>();
		acciones.add("AGREGAR");
		acciones.add("MODIFICAR");
		acciones.add("BORRAR");
		page.addObject("acciones", acciones);

		/* Tablas */
		page.addObject("tablas", filenetService.obtenerTablas());

		page.addObject("titulo", "CONSULTA LOG");
		page.addObject("usuario", usuario);
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] consultaLog");
		return page;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/adminisFuncionarios")
	public ModelAndView administracionFuncionarios(Locale locale) {

		this.LOGGER.info("[INICIO] adminisFuncionarios");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("administracion/admin_funcionarios");
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();
		
		/* Dependencias */
		List<ItemFilenet> listaDependencias = new ArrayList<>();
		Object dependenciaSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_TODAS);
		if (dependenciaSession == null) {
			List<ItemFilenet> dependenciasDestino = filenetService.listarDependenciaApoyo("ADICION", "");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS_TODAS, dependenciasDestino);
			listaDependencias = dependenciasDestino;
		} else {
			listaDependencias = (List<ItemFilenet>) dependenciaSession;
		}
		/* Opciones */
		List<String> opciones = new ArrayList<>();
		opciones.add("SI");
		opciones.add("NO");
		page.addObject("opciones", opciones);

		page.addObject("listaDependencias", listaDependencias);
		page.addObject("titulo", "Administración de Funcionarios");
		page.addObject("usuario", usuario);
		page.addObject("dependenciasUsuario", filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", filenetService.listarTiposCorresponciaEmision(""));
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] consultaJefeGestor");
		return page;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/reemplazosAdicion")
	public ModelAndView reemplazosAdicion(Locale locale) {

		this.LOGGER.info("[INICIO] reemplazosAdicion");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("administracion/reemplazos_adicion");
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();
		List<Rol> listaRoles = new ArrayList<>();
		/* Rol */
		Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if (rolSession == null) {
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			listaRoles = rolesBD;
		} else {
			listaRoles = (List<Rol>) rolSession;
			page.addObject("listaRoles", listaRoles);

		}
		page.addObject("listaRoles", listaRoles);
		page.addObject("titulo", "Reemplazos en Adición");
		page.addObject("usuario", usuario);
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] reemplazosAdicion");
		return page;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/reemplazosTotal")
	public ModelAndView reemplazosTotal(Locale locale) {
		
		this.LOGGER.info("[INICIO] consultaJefeGestor");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("administracion/reemplazos_total");
		List<String> errores = new ArrayList<>();
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		HttpSession session = request.getSession();
		List<Rol> listaRoles = new ArrayList<>();
		/* Usuarios Salientes */
		Respuesta<Funcionario> respuestaFuncionarioSalientes = this.correspondeciaService
				.obtenerFuncionarios(usuario.getToken(), "0", "", "SI", locale);
		if (!respuestaFuncionarioSalientes.estado) {
			LOGGER.info("Información funcionario estado false: " + respuestaFuncionarioSalientes.mensaje);
			errores.add(respuestaFuncionarioSalientes.mensaje);
			page.addObject("usuariosSalientes", new ArrayList<>());
		} else {
			LOGGER.info("Información funcionario estado true: " + respuestaFuncionarioSalientes.datos.size());
			page.addObject("usuariosSalientes", respuestaFuncionarioSalientes.datos);
		}
		/* Usuarios Entrantes */
		Respuesta<Funcionario> respuestaFuncionarioEntrantes = this.correspondeciaService
				.obtenerFuncionarios(usuario.getToken(), "0", "", "SI", locale);
		if (!respuestaFuncionarioEntrantes.estado) {
			LOGGER.info("Información funcionario estado false: " + respuestaFuncionarioEntrantes.mensaje);
			errores.add(respuestaFuncionarioEntrantes.mensaje);
			page.addObject("usuariosEntrantes", new ArrayList<>());
		} else {
			LOGGER.info("Información funcionario estado true: " + respuestaFuncionarioEntrantes.datos.size());
			page.addObject("usuariosEntrantes", respuestaFuncionarioEntrantes.datos);
		}
		/* Dependencias */
		/*
		 * List<ItemFilenet> listaDependencias = new ArrayList<>(); tipo="TOTAL";
		 * List<ItemFilenet> dependenciasDestino =
		 * filenetService.listarDependenciasReempTotal(tipo,""); listaDependencias =
		 * dependenciasDestino;
		 */
		/* Rol */
		Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if (rolSession == null) {
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			listaRoles = rolesBD;
		} else {
			listaRoles = (List<Rol>) rolSession;
			page.addObject("listaRoles", listaRoles);

		}
		page.addObject("listaRoles", listaRoles);
		// page.addObject("listaDependencias",listaDependencias);
		page.addObject("titulo", "Reemplazos Total");
		page.addObject("usuario", usuario);
		page.addObject("dependenciasUsuario", filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		page.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", filenetService.listarTiposCorresponciaEmision(""));
		page.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] consultaJefeGestor");
		return page;
	}

	@GetMapping(value = "/apoyoGGPD")
	public ModelAndView reemplazosGG(Locale locale) {

		this.LOGGER.info("[INICIO] reemplazosGG");
		UsuarioPetroperu usuario = obtenerUsuario();
		if (usuario == null)
			return new ModelAndView("redirect:/403");
		ModelAndView page = new ModelAndView("administracion/reemplazos_GG");
		page.addObject("titulo", "Apoyo a Gerencia General y Presidencia del Directorio");
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
		// COMENTAR ESTA LINEA PORQUE EL ESTADO SE ENCUENTRA REPETIDO
		// estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Declinada");
		boolean esJefeGestor = false;
		if (tieneRol(usuario, RolAD.JEFE) || tieneRol(usuario, RolAD.GESTOR)) {
			esJefeGestor = true;
		}
		page.addObject("esJefe", esJefeGestor);
		page.addObject("estados_correspondencia", estadosCorrespondencia);
		page.addObject("listaTipoEmision", this.tipoEmisionService.listarTodos());
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));

		this.LOGGER.info("[FIN] consultaJefeGestor");
		return page;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping(value = "/reemplazos")
	public ModelAndView consultaReemplazos(Locale locale) {
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
		List<ItemFilenet> listaDep = new ArrayList<>();
		List<ItemFilenet> listaDepRem = new ArrayList<>();
		List<Estado> listaEstados = new ArrayList<>();
		Object tipoEstados = session.getAttribute(Constante.SESSION_ESTADOS_FILENET);
		if (tipoEstados == null) {
			List<Estado> tiposEsta = filenetService.listarEstados("TD_CR");
			session.setAttribute(Constante.SESSION_ESTADOS_FILENET, tiposEsta);
			listaEstados = tiposEsta;
		} else {
			listaEstados = (List<Estado>) tipoEstados;
		}
		Object tipoCorrEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_CORR_EMISION);
		if (tipoCorrEmisionSession == null) {
			List<ItemTipoCorrespondencia> tiposCorr = filenetService.listarTiposCorresponciaEmision("");
			session.setAttribute(Constante.SESSION_TIPOS_CORR_EMISION, tiposCorr);
			listaTipoCorrEmision = tiposCorr;
		} else {
			listaTipoCorrEmision = (List<ItemTipoCorrespondencia>) tipoCorrEmisionSession;
		}
		Object dependenciaSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS);
		if (dependenciaSession == null) {
			List<ItemFilenet> dependenciasDestino = filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(),
					"");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS, dependenciasDestino);
			listaDependencias = dependenciasDestino;
		} else {
			listaDependencias = (List<ItemFilenet>) dependenciaSession;
		}
		Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if (rolSession == null) {
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			listaRoles = rolesBD;
		} else {
			listaRoles = (List<Rol>) rolSession;
		}
		Object menuSession = session.getAttribute(Constante.SESSION_MENU);
		if (menuSession == null) {
			List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
			session.setAttribute(Constante.SESSION_MENU, menu);
			listaMenu = menu;
		} else {
			listaMenu = (List<Menu>) menuSession;
		}
		Object tipoEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_EMISION);
		if (tipoEmisionSession == null) {
			List<TipoEmision> tipoEmision = this.tipoEmisionService.listarTodos();
			session.setAttribute(Constante.SESSION_TIPOS_EMISION, tipoEmision);
			listaTipoEmision = tipoEmision;
		} else {
			listaTipoEmision = (List<TipoEmision>) tipoEmisionSession;
		}
		Object depRemSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_REMITENTE_FILENET);
		if (depRemSession == null) {
			List<ItemFilenet> depRem = filenetService.listarDependenciasRemitente("%");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS_REMITENTE_FILENET, depRem);
			listaDepRem = depRem;
		} else {
			listaDepRem = (List<ItemFilenet>) depRemSession;
		}
		Object depSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS_FILENET);
		if (depSession == null) {
			List<ItemFilenet> dep = filenetService.listarDependencias(null, "%");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS_FILENET, dep);
			listaDep = dep;
		} else {
			listaDep = (List<ItemFilenet>) depSession;
		}

		List<ItemFilenet> listaDependenciasDestinatario = listaDep;
		List<ItemFilenet> listaDependenciasRemitente = listaDepRem;

		ModelAndView page = new ModelAndView("administracion/consulta_reemplazos");
		page.addObject("titulo", "Consulta Reemplazos");
		page.addObject("usuario", usuario);
		page.addObject("dependenciasUsuario", listaDependencias);
		page.addObject("listaMenu", listaMenu);
		page.addObject("dependenciasRemitentes", listaDependenciasRemitente);
		page.addObject("dependenciasDestinatarios", listaDependenciasDestinatario);
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", listaTipoCorrEmision);
		page.addObject("listaEstados", listaEstados);
		List<String> estadosCorrespondencia = new ArrayList<>();
		estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Por firmar");
		estadosCorrespondencia.add("Firmado");
		estadosCorrespondencia.add("Rechazado");
		estadosCorrespondencia.add("Aprobado");
		estadosCorrespondencia.add("Enviado");
		estadosCorrespondencia.add("Declinada");

		List<String> roles = new ArrayList<>();
		roles.add("EJECUTOR");
		roles.add("GESTOR");
		roles.add("JEFE");

		boolean esJefeGestor = false;
		if (tieneRol(usuario, RolAD.JEFE) || tieneRol(usuario, RolAD.GESTOR)) {
			esJefeGestor = true;
		}
		page.addObject("esJefe", esJefeGestor);
		page.addObject("roles", roles);
		page.addObject("estados_correspondencia", estadosCorrespondencia);
		page.addObject("listaTipoEmision", listaTipoEmision);
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		this.LOGGER.info("[FIN] consultaBandejaSalida");
		return page;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping(value = "/rolesAnteriores")
	public ModelAndView consultaRolesAnteriores(Locale locale) {
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
		List<Estado> listaEstados = new ArrayList<>();
		List<TipoEmision> listaTipoEmision = new ArrayList<>();
		Object tipoCorrEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_CORR_EMISION);
		Object tipoEstados = session.getAttribute(Constante.SESSION_ESTADOS_FILENET);
		if (tipoEstados == null) {
			List<Estado> tiposEsta = filenetService.listarEstados("TD_CR");
			session.setAttribute(Constante.SESSION_ESTADOS_FILENET, tiposEsta);
			listaEstados = tiposEsta;
		} else {
			listaEstados = (List<Estado>) tipoEstados;
		}
		if (tipoCorrEmisionSession == null) {
			List<ItemTipoCorrespondencia> tiposCorr = filenetService.listarTiposCorresponciaEmision("");
			session.setAttribute(Constante.SESSION_TIPOS_CORR_EMISION, tiposCorr);
			listaTipoCorrEmision = tiposCorr;
		} else {
			listaTipoCorrEmision = (List<ItemTipoCorrespondencia>) tipoCorrEmisionSession;
		}
		Object dependenciaSession = session.getAttribute(Constante.SESSION_DEPENDENCIAS);
		if (dependenciaSession == null) {
			List<ItemFilenet> dependenciasDestino = filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(),
					"");
			session.setAttribute(Constante.SESSION_DEPENDENCIAS, dependenciasDestino);
			listaDependencias = dependenciasDestino;
		} else {
			listaDependencias = (List<ItemFilenet>) dependenciaSession;
		}
		Object rolSession = session.getAttribute(Constante.SESSION_ROLES_USUARIO);
		if (rolSession == null) {
			List<Rol> rolesBD = this.rolService.listarRolPorUsuario(usuario.getUsername());
			session.setAttribute(Constante.SESSION_ROLES_USUARIO, rolesBD);
			listaRoles = rolesBD;
		} else {
			listaRoles = (List<Rol>) rolSession;
		}
		Object menuSession = session.getAttribute(Constante.SESSION_MENU);
		if (menuSession == null) {
			List<Menu> menu = this.correspondeciaService.obtenerMenuSistcorr(usuario, locale);
			session.setAttribute(Constante.SESSION_MENU, menu);
			listaMenu = menu;
		} else {
			listaMenu = (List<Menu>) menuSession;
		}
		Object tipoEmisionSession = session.getAttribute(Constante.SESSION_TIPOS_EMISION);
		if (tipoEmisionSession == null) {
			List<TipoEmision> tipoEmision = this.tipoEmisionService.listarTodos();
			session.setAttribute(Constante.SESSION_TIPOS_EMISION, tipoEmision);
			listaTipoEmision = tipoEmision;
		} else {
			listaTipoEmision = (List<TipoEmision>) tipoEmisionSession;
		}
		ModelAndView page = new ModelAndView("administracion/consulta_roles_anteriores");
		page.addObject("titulo", "Consulta Roles Anteriores");
		page.addObject("usuario", usuario);
		page.addObject("dependenciasUsuario", listaDependencias);
		page.addObject("listaMenu", listaMenu);
		page.addObject("errores", new ArrayList<>());
		page.addObject("tiposCorrespondencia", listaTipoCorrEmision);
		page.addObject("listaEstados", listaEstados);
		List<String> estadosCorrespondencia = new ArrayList<>();
		estadosCorrespondencia.add("Sin asignar");
		estadosCorrespondencia.add("Por firmar");
		estadosCorrespondencia.add("Firmado");
		estadosCorrespondencia.add("Rechazado");
		estadosCorrespondencia.add("Aprobado");
		estadosCorrespondencia.add("Enviado");
		estadosCorrespondencia.add("Declinada");

		List<String> roles = new ArrayList<>();
		roles.add("EJECUTOR");
		roles.add("GESTOR");
		roles.add("JEFE");

		boolean esJefeGestor = false;
		if (tieneRol(usuario, RolAD.JEFE) || tieneRol(usuario, RolAD.GESTOR)) {
			esJefeGestor = true;
		}
		page.addObject("esJefe", esJefeGestor);
		page.addObject("roles", roles);
		page.addObject("estados_correspondencia", estadosCorrespondencia);
		page.addObject("listaTipoEmision", listaTipoEmision);
		page.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		this.LOGGER.info("[FIN] consultaBandejaSalida");
		return page;
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

	@GetMapping(value = "/consulta-log", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<LogDTO>>> consultaLogPaginado(HttpServletRequest request,
			Locale locale) {
		Respuesta<DataTableResults<LogDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			
			DataTableRequestLogConsulta dataTableRequest = new DataTableRequestLogConsulta(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<LogDTO> respuestaLogPaginado = filenetService.consultaLogPaginado(dataTableRequest.getFiltro(),
					usuario.getToken(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(),
					dataTableRequest.getOrdenDesc(), "NO", locale);
			DataTableResults<LogDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaLogPaginado.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaLogPaginado.total.toString());
			dataTableResults.setRecordsTotal(respuestaLogPaginado.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultar-log-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> consultarLogExcel(@RequestBody FiltroConsultaLogDTO filtro,
			Locale locale) {

		LOGGER.info("[INICIO] consultarLogExcel");
		UsuarioPetroperu usuario = obtenerUsuario();

		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarLogExcel(filtro,
				usuario.getNombreCompleto(), usuario.getUsername(), locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=consulta_log.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	/*****************************
	 * CONSULTA ROLES ANTERIORES
	 *****************************/
	@GetMapping(value = "/consultar-roles-anteriores-paginado", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<ReemplazoRolesAnterioresConsultaDTO>>> consultaRolesAnterioresPaginado(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ReemplazoRolesAnterioresConsultaDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();
			DataTableRequestConsultaReemplazoDTO dataTableRequest = new DataTableRequestConsultaReemplazoDTO(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<ReemplazoRolesAnterioresConsultaDTO> respuestaLogPaginado = filenetService
					.consultaRolesAnterioresPaginado(username, dataTableRequest.getFiltro(),
							dataTableRequest.getLength(), start, dataTableRequest.getColumna(),
							dataTableRequest.getOrdenDesc(), "NO", locale);
			DataTableResults<ReemplazoRolesAnterioresConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaLogPaginado.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaLogPaginado.total.toString());
			dataTableResults.setRecordsTotal(respuestaLogPaginado.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultar-roles-anteriores-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> consultarRolesAnterioresExcel(
			@RequestBody FiltroConsultaReemplazoRolesAnterioresDTO filtro, Locale locale) {

		LOGGER.info("[INICIO] consultarRolesAnterioresExcel");
		UsuarioPetroperu usuario = obtenerUsuario();

		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarRolesAnterioresExcel(filtro,
				usuario.getNombreCompleto(), usuario.getUsername(), locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=roles_anteriores.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	/*****************************
	 * CONSULTA ROLES REEMPLAZO
	 *****************************/
	@GetMapping(value = "/consultar-roles-reemplazo-paginado", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<ReemplazoRolesAnterioresConsultaDTO>>> consultaRolesReemplazoPaginado(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ReemplazoRolesAnterioresConsultaDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();
			DataTableRequestConsultaReemplazoDTO dataTableRequest = new DataTableRequestConsultaReemplazoDTO(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<ReemplazoRolesAnterioresConsultaDTO> respuestaLogPaginado = filenetService
					.consultaRolesReemplazoPaginado(username, dataTableRequest.getFiltro(),
							dataTableRequest.getLength(), start, dataTableRequest.getColumna(),
							dataTableRequest.getOrdenDesc(), "NO", locale);
			DataTableResults<ReemplazoRolesAnterioresConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaLogPaginado.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaLogPaginado.total.toString());
			dataTableResults.setRecordsTotal(respuestaLogPaginado.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultar-roles-reemplazos-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> consultarRolesReemplazosExcel(
			@RequestBody FiltroConsultaReemplazoRolesAnterioresDTO filtro, Locale locale) {

		LOGGER.info("[INICIO] consultarRolesReemplazosExcel");
		UsuarioPetroperu usuario = obtenerUsuario();

		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarRolesReemplazosExcel(filtro,
				usuario.getNombreCompleto(), usuario.getUsername(), locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=roles_anteriores.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	/******************************
	 * ADMINISTRACION FUNCIONARIOS
	 ******************************/
	@GetMapping(value = "/consultar-admin-funcionarios", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<FuncionariosDTO>>> consultaAdminFuncionarios(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<FuncionariosDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();
			DataTableRequestFuncionariosConsulta dataTableRequest = new DataTableRequestFuncionariosConsulta(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<FuncionariosDTO> respuestaConsultaFuncionarios = filenetService.consultaFuncionariosPaginado(
					username, dataTableRequest.getFiltro(), dataTableRequest.getLength(), start,
					dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);
			DataTableResults<FuncionariosDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaFuncionarios.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaFuncionarios.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaFuncionarios.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/consultar-funcionario-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> consultarFuncionariosExcel(
			@RequestBody FiltroConsultaFuncionariosDTO filtro, Locale locale) {

		LOGGER.info("[INICIO] consultarFuncionariosExcel");
		UsuarioPetroperu usuario = obtenerUsuario();
		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarFuncionarioExcel(filtro,
				usuario.getNombreCompleto(), usuario.getUsername(), locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=consultar-funcionarios.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	// guardar-funcioanarios
	@PostMapping(value = { "/guardar-funcioanarios" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> guardarFuncionarios(@RequestBody Funcionario funcionario,
			Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();

		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			respuesta.datos = filenetService.guardarFuncionario(funcionario, usuario.getUsername());
			if (respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "Registro guardado correctamente.";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion().toString();
			}

		} catch (Exception e) {
			LOGGER.error("[buscarDependenciaSuperior]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] guardarFuncionarios");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);

	}

	@PostMapping(value = { "/eliminarFuncionario" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> eliminarFuncionario(@RequestBody Funcionario funcionario,
			Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();

		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			respuesta.datos = filenetService.eliminarFuncionario(funcionario.getIdFuncionario(),
					funcionario.getRegistro(), usuario.getUsername());
			if (respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "Registro eliminado correctamente.";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion().toString();
			}

		} catch (Exception e) {
			LOGGER.error("[eliminarFuncionario]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al realizar el proceso.";
		}
		LOGGER.info("[FIN] buscarDependenciaSuperior");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	/***********************
	 * Apoyo a Gerencia General y Presidencia del Directorio
	 ****************************/
	@GetMapping(value = { "/buscar/filtroDependenciaGG" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> comboDependenciaApoyo(
			@RequestParam(value = "tipoReemplazo", defaultValue = "") String tipoReemplazo, @RequestParam(value = "q", defaultValue = "") String term) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		respuesta.datos.addAll(this.filenetService.listarDependenciaApoyo(tipoReemplazo, term));
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping(value = { "/buscar/funcionarioReemplazado" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> comboJefeDependencia(
			@RequestParam(value = "codDependencia") String codDepen, @RequestParam("rol") String rol, @RequestParam(value = "q", defaultValue = "") String term) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		respuesta.datos.addAll(this.filenetService.listarJefeXDependencia(codDepen, rol, term));
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping(value = { "/buscar/funcionarioReemplazar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> comboFuncionarioApoyo(
			@RequestParam(value = "tipoReemplazo") String tipoReemp, @RequestParam("rol") String rol,
			@RequestParam(value = "codDependencia") String codDepen, @RequestParam(value = "q", defaultValue = "") String term) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		respuesta.datos.addAll(this.filenetService.listarFuncionariosApoyo(tipoReemp, codDepen, rol, term));
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping(value = "/consultar-reemplazo-apoyo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<ReemplazoConsultaDTO>>> consultarReemplazoApoyo(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ReemplazoConsultaDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();
			DataTableRequestReemplazoConsulta dataTableRequest = new DataTableRequestReemplazoConsulta(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<ReemplazoConsultaDTO> respuestaConsultaFuncionarios = filenetService
					.consultaReemplazoApoyoPaginado(username, dataTableRequest.getFiltro(),
							dataTableRequest.getLength(), start, dataTableRequest.getColumna(),
							dataTableRequest.getOrdenDesc(), "NO", "APOYO", locale);
			DataTableResults<ReemplazoConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaFuncionarios.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaFuncionarios.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaFuncionarios.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	/*********************** Reemplazos en Adición ***********************/
	@GetMapping(value = { "/buscar/rolAdicionReemplazo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> comboRolDependenciaReemplazo(
			@RequestParam(value = "codDependencia", defaultValue = "") String codDependencia) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		respuesta.datos.addAll(this.filenetService.comboRolDependenciaReemplazo(codDependencia));
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@GetMapping(value = "/consultar-reemplazo-adicion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<ReemplazoConsultaDTO>>> consultarReemplazoAdicion(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ReemplazoConsultaDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();
			DataTableRequestReemplazoConsulta dataTableRequest = new DataTableRequestReemplazoConsulta(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<ReemplazoConsultaDTO> respuestaConsultaFuncionarios = filenetService
					.consultaReemplazoApoyoPaginado(username, dataTableRequest.getFiltro(),
							dataTableRequest.getLength(), start, dataTableRequest.getColumna(),
							dataTableRequest.getOrdenDesc(), "NO", "ADICION", locale);
			DataTableResults<ReemplazoConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaFuncionarios.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaFuncionarios.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaFuncionarios.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/exportar-reemplazo-adicion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> consultarReemplazoAdicionExcel(
			@RequestBody FiltroConsultaReemplazoDTO filtro, Locale locale) {

		LOGGER.info("[INICIO] consultarReemplazoAdicionExcel");
		UsuarioPetroperu usuario = obtenerUsuario();

		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarReemplazoAdicionExcel(filtro,
				usuario.getNombreCompleto(), usuario.getUsername(), locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reemplazoAdicion.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	/*********************** Reemplazos Total ***********************/

	@GetMapping(value = "/consultar-reemplazo-total", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Respuesta<DataTableResults<ReemplazoConsultaDTO>>> consultarReemplazoTotal(
			HttpServletRequest request, Locale locale) {
		Respuesta<DataTableResults<ReemplazoConsultaDTO>> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			String username = usuario.getUsername();
			DataTableRequestReemplazoConsulta dataTableRequest = new DataTableRequestReemplazoConsulta(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<ReemplazoConsultaDTO> respuestaConsultaFuncionarios = filenetService
					.consultaReemplazoApoyoPaginado(username, dataTableRequest.getFiltro(),
							dataTableRequest.getLength(), start, dataTableRequest.getColumna(),
							dataTableRequest.getOrdenDesc(), "NO", "TOTAL", locale);
			DataTableResults<ReemplazoConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaConsultaFuncionarios.datos);
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaConsultaFuncionarios.total.toString());
			dataTableResults.setRecordsTotal(respuestaConsultaFuncionarios.total.toString());
			respuesta.datos.add(dataTableResults);
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/eliminarReemplazo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> eliminarReemplazo(@RequestBody ReemplazoConsultaDTO reemplazo,
			Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();

		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			respuesta.datos = filenetService.eliminarReemplazo(reemplazo.getId_reemplazo(), usuario.getUsername());
			if (respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "Registro eliminado correctamente.";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion().toString();
			}

		} catch (Exception e) {
			LOGGER.error("[eliminarFuncionario]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al realizar el proceso.";
		}
		LOGGER.info("[FIN] buscarDependenciaSuperior");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/validarReemplazo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> validarReemplazo(@RequestBody ReemplazoConsultaDTO reemplazo,
			Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		Integer valorInicial = 0;
		try {
			
			respuesta.estado = true;
			if (reemplazo != null && reemplazo.getTipoReemplazo() != null
					&& reemplazo.getTipoReemplazo().trim().equalsIgnoreCase("TOTAL") && reemplazo.getAccion() != null
					&& reemplazo.getAccion().equalsIgnoreCase("")) {
				String variableName = "EstadoMsgConfirm";
				
				List<ItemFilenet> respModVar = filenetService.modificarValorVariale(usuario.getUsername(), "EstadoMsgConfirm", "", 0);
				
				if (!respModVar.get(0).getDescripcion().contains("Error:")) {
					List<ItemFilenet> respObtValor = filenetService.obtenerValorVar(usuario.getUsername(), variableName);
					valorInicial = Integer.valueOf(respObtValor.get(0).getCodigo().toString());
				}else {
					respuesta.estado = false;
					respuesta.mensaje = respModVar.get(0).getDescripcion();
				}
			}
			
			if(respuesta.estado) {
				List<ItemFilenet> resp = filenetService.validarReemplazo(reemplazo, reemplazo.getTipoReemplazo(),
						usuario.getUsername());
				if (reemplazo != null && reemplazo.getTipoReemplazo() != null
						&& reemplazo.getTipoReemplazo().trim().equalsIgnoreCase("TOTAL")) {
					if (resp != null && resp.size() > 0) {
						resp.get(0).setCodigoAux(valorInicial + "");
					}
				}
				
				respuesta.datos.addAll(resp);
				respuesta.estado = true;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion().toString();
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		LOGGER.info("[FIN] validarReemplazo");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/grabarReemplazoAdicionSinConfirmar" }, produces = { "application/json" }) // codigoConfirmacion
																										// = 0 y
																										// mensaje=''
	public ResponseEntity<Respuesta<ItemFilenet>> grabarReemplazoAdicionSinConfirmar(
			@RequestBody ReemplazoConsultaDTO reemplazo, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		Respuesta<ReemplazoConsultaDTO> respuestaReemplazo = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			String sCodReemplazo = "";
			reemplazo.setTipoReemplazo("ADICION");
			if (reemplazo.getAccion().equalsIgnoreCase("")) {
				respuesta.datos = filenetService.guardarReemplazoAdicion(reemplazo, usuario.getUsername());// paso 4
				if (!respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")
						&& !respuesta.datos.get(0).getDescripcion().contains("Error:")) {
					sCodReemplazo = respuesta.datos.get(0).getDescripcion().toString();
					respuestaReemplazo = this.correspondeciaService.notificacionReemplazo(usuario.getToken(),
							sCodReemplazo, locale);// paso 5
					if (respuestaReemplazo.estado) {
						Respuesta<ItemFilenet> respuestaRol = new Respuesta<>();
						respuestaRol.datos = filenetService.obtenerRolDepOriginal(reemplazo.getUsuarioEntrante(),
								usuario.getUsername());// paso 6
						if (respuestaRol.datos.get(0).getCodigo().equalsIgnoreCase("EJECUTOR")
								&& (reemplazo.getRol().equalsIgnoreCase("JEFE")
										|| reemplazo.getRol().equalsIgnoreCase("EJECUTOR"))) {
							Object[] parametros = { reemplazo.getRol() };
							respuesta.estado = true;
							respuesta.datos.get(0).setDescripcion(MessageFormat.format(this.messageSource.getMessage(
									"sistcorr.notificar.error_rol_dep_original", null, locale), parametros));
						} else {
							respuesta.estado = true;
							respuesta.datos.get(0).setDescripcion("");
							respuesta.mensaje = this.messageSource
									.getMessage("sistcorr.notificar.registrarReemplazoAdicion", null, locale);
						}
					} else {
						respuesta.estado = false;
						respuesta.mensaje = respuestaReemplazo.mensaje;
					}
				} else {
					respuesta.estado = false;
					respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
				}
			} else {
				respuesta.datos = filenetService.actualizarReemplazoAdicion(reemplazo, usuario.getUsername());
				if (!respuesta.datos.get(0).getDescripcion().contains("Error:")) {
					respuesta.estado = true;
					respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.actualizarReemplazoAdicion",
							null, locale);
				} else {
					respuesta.estado = true;
					respuesta.mensaje = "Error al actualizar el reemplazo en adicion";
				}
			}

		} catch (Exception e) {
			LOGGER.error("[grabarReemplazoAdicionSinConfirmar]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] grabarReemplazoAdicionSinConfirmar");

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/grabarReemplazoAdicionConConfirmacion1" }, produces = { "application/json" }) // codigoConfirmacion
																											// = 1 y
																											// mensaje
																											// <> ''
	public ResponseEntity<Respuesta<ItemFilenet>> grabarReemplazoAdicionConConfirmacion1(
			@RequestBody ReemplazoConsultaDTO reemplazo, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		Respuesta<ReemplazoConsultaDTO> respuestaReemplazo = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			String sCodReemplazo = "";
			// reemplazo.setTipoReemplazo("ADICION");
			respuesta.datos = filenetService.guardarReemplazoAdicion(reemplazo, usuario.getUsername());// paso 4
			if (!respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")
					&& !respuesta.datos.get(0).getDescripcion().contains("Error:")) {
				sCodReemplazo = respuesta.datos.get(0).getDescripcion().toString();
				respuestaReemplazo = this.correspondeciaService.notificacionReemplazo(usuario.getToken(),
						sCodReemplazo, locale);// paso 5
				if (respuestaReemplazo.estado) {
					respuesta.estado = true;
					if (reemplazo.getTipoReemplazo() != null
							&& reemplazo.getTipoReemplazo().trim().equalsIgnoreCase("ADICION"))
						respuesta.mensaje = this.messageSource
								.getMessage("sistcorr.notificar.registrarReemplazoAdicion", null, locale);
					else if (reemplazo.getTipoReemplazo() != null
							&& reemplazo.getTipoReemplazo().trim().equalsIgnoreCase("TOTAL"))
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.registrarReemplazoTotal",
								null, locale);
					else if (reemplazo.getTipoReemplazo() != null
							&& reemplazo.getTipoReemplazo().trim().equalsIgnoreCase("APOYO"))
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.registrarReemplazoGG",
								null, locale);
				} else {
					respuesta.estado = false;
					respuesta.mensaje = respuestaReemplazo.mensaje;
				}
			} else {
				respuesta.estado = false;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
			}

		} catch (Exception e) {
			LOGGER.error("[grabarReemplazoAdicionConConfirmacion1]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] grabarReemplazoAdicionConConfirmacion1");

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/grabarReemplazoAdicionConConfirmacion2" }, produces = { "application/json" }) // codigoConfirmacion
																											// = 2,3 y
																											// mensaje
																											// <> ''
	public ResponseEntity<Respuesta<ItemFilenet>> grabarReemplazoAdicionConConfirmacion2(
			@RequestBody ReemplazoConsultaDTO reemplazo, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		Respuesta<ReemplazoConsultaDTO> respuestaReemplazo = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			String sCodReemplazo = "";
			respuesta.datos = filenetService.eliminarRemplazo(reemplazo, usuario.getUsername());

			if (respuesta.datos != null && respuesta.datos.size() > 0
					&& respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")) {
				// reemplazo.setTipoReemplazo("ADICION");
				respuesta.datos = filenetService.guardarReemplazoAdicion(reemplazo, usuario.getUsername());// paso 4
				if (!respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")
						&& !respuesta.datos.get(0).getDescripcion().contains("Error:")) {
					sCodReemplazo = respuesta.datos.get(0).getDescripcion().toString();
					respuestaReemplazo = this.correspondeciaService.notificacionReemplazo(usuario.getToken(),
							sCodReemplazo, locale);// paso 5
					if (respuestaReemplazo.estado) {
						respuesta.estado = true;
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.eliminacionReemplazo",
								null, locale);
					} else {
						respuesta.estado = false;
						respuesta.mensaje = respuestaReemplazo.mensaje;
					}
				} else {
					respuesta.estado = false;
					respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
				}
			} else {
				respuesta.estado = false;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
			}

		} catch (Exception e) {
			LOGGER.error("[grabarReemplazoAdicionConConfirmacion2]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] grabarReemplazoAdicionConConfirmacion2");

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/eliminarRemplazosYactualizarValorVarConConfirmacion4" }, produces = { "application/json" }) // codigoConfirmacion
																															// =
																															// 4
																															// y
																															// mensaje
																															// <>
																															// ''
	public ResponseEntity<Respuesta<ItemFilenet>> eliminarRemplazosYactualizarValorVarConConfirmacion4(
			@RequestBody ReemplazoConsultaDTO reemplazo, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			respuesta.datos = filenetService.eliminarRemplazo(reemplazo, usuario.getUsername());
			if (respuesta.datos != null && respuesta.datos.size() > 0
					&& respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")) {
				// reemplazo.setTipoReemplazo("ADICION");
				Respuesta<ReemplazoConsultaDTO> respuestaReemplazo = new Respuesta<>();
				respuesta.datos = filenetService.modificarValorVariale(usuario.getUsername(), "EstadoMsgConfirm", "", 1);
				
				if (!respuesta.datos.get(0).getDescripcion().contains("Error:")) {
					
					respuesta.datos = filenetService.guardarReemplazoAdicion(reemplazo, usuario.getUsername());
					if (!respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")
							&& !respuesta.datos.get(0).getDescripcion().contains("Error:")) {
						String sCodReemplazo = "";
						sCodReemplazo = respuesta.datos.get(0).getDescripcion().toString();
						respuestaReemplazo = this.correspondeciaService.notificacionReemplazo(usuario.getToken(),
								sCodReemplazo, locale);
						if (respuestaReemplazo.estado) {
							respuesta.estado = true;
							respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.eliminacionReemplazoAdicionNuevoReempTotal",
									null, locale);
						} else {
							respuesta.estado = false;
							respuesta.mensaje = respuestaReemplazo.mensaje;
						}
					} else {
						respuesta.estado = false;
						respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
					}
					
				} else {
					respuesta.estado = false;
					respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
				}
			} else {
				respuesta.estado = false;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
			}

		} catch (Exception e) {
			LOGGER.error("[eliminarRemplazosYactualizarValorVarConConfirmacion4]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] eliminarRemplazosYactualizarValorVarConConfirmacion4");

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/listarReemplazosAdicion" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ReemplazoAdicion>> listarReemplazosAdicion(@RequestBody ReemplazoConsultaDTO filtro,
			Locale locale) {
		LOGGER.info("[INICIO] listarReemplazosAdicion");
		LOGGER.info("filtros listarReemplazosAdicion:" + filtro.toString());
		UsuarioPetroperu usuario = obtenerUsuario();
		Respuesta<ReemplazoAdicion> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		try {
			respuesta.datos = filenetService.listarReemplazoAdicion(filtro, usuario.getUsername());
			if (respuesta.datos != null && respuesta.datos.size() > 0) {
				respuesta.estado = true;
				respuesta.mensaje = "Busqueda realizada correctamente.";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = "No se encontraron resultados en la busqueda de reemplazos";
			}
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al realizar la búsqueda.";
		}
		LOGGER.info("[FIN] listarReemplazosAdicion");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/grabarReemplazoTotalSinConfirmar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> grabarReemplazoTotalSinConfirmar(
			@RequestBody ReemplazoConsultaDTO reemplazo, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		Respuesta<ReemplazoConsultaDTO> respuestaReemplazo = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			String sCodReemplazo = "";
			reemplazo.setTipoReemplazo("TOTAL");
			if (reemplazo.getAccion().equalsIgnoreCase("")) {
				respuesta.datos = filenetService.guardarReemplazoAdicion(reemplazo, usuario.getUsername());// paso 4
				if (!respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")
						&& !respuesta.datos.get(0).getDescripcion().contains("Error:")) {
					sCodReemplazo = respuesta.datos.get(0).getDescripcion().toString();
					respuestaReemplazo = this.correspondeciaService.notificacionReemplazo(usuario.getToken(),
							sCodReemplazo, locale);// paso 5
					if (respuestaReemplazo.estado) {
						respuesta.estado = true;
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.registrarReemplazoTotal",
								null, locale);
					} else {
						respuesta.estado = false;
						respuesta.mensaje = respuestaReemplazo.mensaje;
					}
				} else {
					respuesta.estado = false;
					respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
				}
			} else {
				respuesta.datos = filenetService.actualizarReemplazoAdicion(reemplazo, usuario.getUsername());
				if (!respuesta.datos.get(0).getDescripcion().contains("Error:")) {
					respuesta.estado = true;
					respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.actualizarReemplazoTotal",
							null, locale);
				} else {
					respuesta.estado = true;
					respuesta.mensaje = "Error al actualizar el reemplazo en adicion";
				}
			}

		} catch (Exception e) {
			LOGGER.error("[grabarReemplazoTotalSinConfirmar]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] grabarReemplazoTotalSinConfirmar");

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/validarSiUsuarioEsDeMejorCargo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> validarSiUsuarioEsDeMejorCargo(
			@RequestBody ReemplazoConsultaDTO reemplazo, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();

		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			Respuesta<ItemFilenet> respuestaRol = new Respuesta<>();

			respuesta.datos = filenetService.modificarValorVariale(usuario.getUsername(), "EstadoMsgConfirm", "", 1);

			if (!respuesta.datos.get(0).getDescripcion().contains("Error:")) {

				respuestaRol.datos = filenetService.obtenerRolDepOriginal(reemplazo.getUsuarioEntrante(),
						usuario.getUsername());// paso 6
				if (respuestaRol.datos.get(0).getCodigo().equalsIgnoreCase("EJECUTOR")
						&& (reemplazo.getRol().equalsIgnoreCase("JEFE")
								|| reemplazo.getRol().equalsIgnoreCase("EJECUTOR"))) {
					Object[] parametros = { reemplazo.getRol() };
					respuesta.estado = true;
					respuesta.datos.get(0).setDescripcion(MessageFormat.format(
							this.messageSource.getMessage("sistcorr.notificar.error_rol_dep_original", null, locale),
							parametros));
				} else {
					respuesta.estado = true;
					respuesta.datos.get(0).setDescripcion("");
					respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.registrarReemplazoTotal",
							null, locale);
				}
			} else {
				respuesta.estado = false;
				respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
			}

		} catch (Exception e) {
			LOGGER.error("[grabarReemplazoAdicionSinConfirmar]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] grabarReemplazoAdicionSinConfirmar");

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/grabarReemplazoApoyoSinConfirmar" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<ItemFilenet>> grabarReemplazoApoyoSinConfirmar(
			@RequestBody ReemplazoConsultaDTO reemplazo, Locale locale) {
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		Respuesta<ReemplazoConsultaDTO> respuestaReemplazo = new Respuesta<>();
		UsuarioPetroperu usuario = obtenerUsuario();
		respuesta.estado = true;
		respuesta.mensaje = "OK";

		try {
			String sCodReemplazo = "";
			reemplazo.setTipoReemplazo("APOYO");
			if (reemplazo.getAccion().equalsIgnoreCase("")) {
				respuesta.datos = filenetService.guardarReemplazoAdicion(reemplazo, usuario.getUsername());// paso 4
				if (!respuesta.datos.get(0).getDescripcion().equalsIgnoreCase("")
						&& !respuesta.datos.get(0).getDescripcion().contains("Error:")) {

					sCodReemplazo = respuesta.datos.get(0).getDescripcion().toString();
					respuestaReemplazo = this.correspondeciaService.notificacionReemplazo(usuario.getToken(),
							sCodReemplazo, locale);// paso 5
					if (respuestaReemplazo.estado) {
						respuesta.estado = true;
						respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.registrarReemplazoGG",
								null, locale);
					} else {
						respuesta.estado = false;
						respuesta.mensaje = respuestaReemplazo.mensaje;
					}
					
				} else {
					respuesta.estado = false;
					respuesta.mensaje = respuesta.datos.get(0).getDescripcion();
				}
			} else {
				respuesta.datos = filenetService.actualizarReemplazoAdicion(reemplazo, usuario.getUsername());
				if (!respuesta.datos.get(0).getDescripcion().contains("Error:")) {
					respuesta.estado = true;
					respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.actualizarReemplazoGG", null,
							locale);
				} else {
					respuesta.estado = true;
					respuesta.mensaje = "Error al actualizar el reemplazo de tipo Apoyo a Gerencia General y Presidencia del Directorio";
				}
			}

		} catch (Exception e) {
			LOGGER.error("[grabarReemplazoApoyoSinConfirmar]", e);
			respuesta.estado = false;
			respuesta.mensaje = "Ocurrió un error al obtener la información.";
		}
		LOGGER.info("[FIN] grabarReemplazoApoyoSinConfirmar");

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/exportar-reemplazo-total", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> consultarReemplazoTotalExcel(
			@RequestBody FiltroConsultaReemplazoDTO filtro, Locale locale) {

		LOGGER.info("[INICIO] consultarReemplazoAdicionExcel");
		UsuarioPetroperu usuario = obtenerUsuario();

		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarReemplazoTotalExcel(filtro,
				usuario.getNombreCompleto(), usuario.getUsername(), locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reemplazoTotal.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	@PostMapping(value = "/exportar-reemplazo-apoyo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> consultarReemplazoApoyoExcel(
			@RequestBody FiltroConsultaReemplazoDTO filtro, Locale locale) {

		LOGGER.info("[INICIO] consultarReemplazoAdicionExcel");
		UsuarioPetroperu usuario = obtenerUsuario();

		Respuesta<ByteArrayInputStream> respuesta = filenetService.consultarReemplazoApoyoExcel(filtro,
				usuario.getNombreCompleto(), usuario.getUsername(), locale);
		if (respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reemplazoAdicion.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
	}

	@PostMapping(value = { "/buscarConductor" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Conductor>>> buscarConductor(HttpServletRequest request,
			@RequestBody ListaFiltroConductor filtro, Locale locale) {
		Respuesta<DataTableResults<Conductor>> respuesta = new Respuesta<>();
		Respuesta<ConductorPaginado> respuestaConductorRest = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));

			String bandeja = "Conductor";
			respuestaConductorRest = this.correspondeciaService.buscarConductor(usuario.getToken(), bandeja, filtro,
					tamanioBandConfConductor, locale);
			String totalRegistros = "";
			if (respuestaConductorRest.datos.size() > 0) {
				request.getSession().setAttribute("continuacionDataPagCond",
						respuestaConductorRest.datos.get(0).getContinuationData());
				request.getSession().setAttribute("totalRegistros",
						respuestaConductorRest.datos.get(0).getTotalTareas().toString());
				totalRegistros = (String) request.getSession().getAttribute("totalRegistros");
			}
			DataTableResults<Conductor> dataTableResults = new DataTableResults<>();

			if (respuestaConductorRest.datos.size() > 0) {
				dataTableResults.setListOfDataObjects(
						(List<Conductor>) respuestaConductorRest.datos.get(0).getDetalleConductor());
				dataTableResults.setRecordsTotal(totalRegistros);
				respuesta.estado = true;
			} else {
				respuesta.estado = false;
			}

			respuesta.datos.add(dataTableResults);
			respuesta.mensaje = respuestaConductorRest.mensaje;
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/buscarConductorSiguientePagina" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<DataTableResults<Conductor>>> buscarConductorSiguientePagina(
			HttpServletRequest request, @RequestBody ListaFiltroConductor filtro, Locale locale) {
		Respuesta<DataTableResults<Conductor>> respuesta = new Respuesta<>();
		Respuesta<ConductorPaginado> respuestaConductorRest = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));

			String totalRegistros = "";
			List<ContinuationData> listContDta = (List<ContinuationData>) request.getSession()
					.getAttribute("continuacionDataPagCond");
			String tamanioT = (String) request.getSession().getAttribute("totalRegistros");

			FiltroSiguientePagina filtroSigPag = new FiltroSiguientePagina();
			filtroSigPag.setTotalPagina(tamanioBandConfConductor);
			filtroSigPag.setTotalItemxPagina(tamanioBandConfConductor);
			if (listContDta != null && listContDta.size() > 0)
				filtroSigPag.setContinuationData(listContDta);

			respuestaConductorRest = this.correspondeciaService.filtraConductorSiguientePagina(usuario.getToken(),
					filtroSigPag, locale);
			if (respuestaConductorRest.datos.size() > 0)
				request.getSession().setAttribute("continuacionDataPagCond",
						respuestaConductorRest.datos.get(0).getContinuationData());

			if (tamanioT != null && !tamanioT.trim().equalsIgnoreCase(""))
				totalRegistros = tamanioT;
			else
				totalRegistros = "0";

			DataTableResults<Conductor> dataTableResults = new DataTableResults<>();

			if (respuestaConductorRest.datos.size() > 0) {
				dataTableResults.setListOfDataObjects(
						(List<Conductor>) respuestaConductorRest.datos.get(0).getDetalleConductor());
				dataTableResults.setRecordsTotal(totalRegistros);
				respuesta.estado = true;
			} else {
				respuesta.estado = false;
			}

			respuesta.datos.add(dataTableResults);
			respuesta.mensaje = respuestaConductorRest.mensaje;
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/conductorReintentarMasivo" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> conductorReintentarMasivo(HttpServletRequest request,
			@RequestBody ListaFiltroConductor filtro, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));

			for (String workflowId : filtro.getListaWorkflowIds()) {
				Respuesta<RespuestaApi> respApi = this.correspondeciaService.reintentarConductor(usuario.getToken(),
						workflowId, locale);

				respuesta.datos.add(respApi.datos.get(0));
			}

			respuesta.estado = true;
			respuesta.mensaje = "Ok";
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = "/exportar-conductor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> exportarConductorExcel(@RequestBody ListaFiltroConductor filtro,
			Locale locale) {

		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		LOGGER.info("[INICIO] exportarConductorExcel");
		UsuarioPetroperu usuario = obtenerUsuario();
		String username = usuario.getUsername();
		Respuesta<ConductorPaginado> respuestaConductorRest = new Respuesta<>();
		String bandeja = "Conductor";
		respuestaConductorRest = this.correspondeciaService.buscarConductor(usuario.getToken(), bandeja, filtro,
				filtro.getTotal() + "", locale);

		ReportExcelConductor reporte = new ReportExcelConductor(
				respuestaConductorRest.datos.get(0).getDetalleConductor(), username);
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

	@GetMapping({ "/administracionConductor" })
	public ModelAndView administracionConductor(Locale locale) {
		UsuarioPetroperu usuario = obtenerUsuario();
		List<String> errores = new ArrayList<>();
		ModelAndView view = new ModelAndView("administracion/conductor_edicion");
		view.addObject("titulo", this.messageSource.getMessage("sistcorr.conductor.titulo", null, locale));
		view.addObject("usuario", usuario);
		view.addObject("username", usuario.getUsername());
		view.addObject("nombreUsuario", usuario.getNombreCompleto());
		view.addObject("listaMenu", this.correspondeciaService.obtenerMenuSistcorr(usuario, locale));
		view.addObject("tiposCorrespondencia", this.filenetService.listarTiposCorresponciaEmision(""));
		view.addObject("dependenciasUsuario",
				this.filenetService.obtenerDependenciaPorUsuario(usuario.getUsername(), ""));
		view.addObject("edicion", Boolean.valueOf(false));
		view.addObject("sizeFileUpload", this.tamanioMaxArchivoUpload);
		view.addObject("correlativo", "");
		view.addObject("errores", errores);
		view.addObject("version", this.messageSource.getMessage("sistcorr.login.version", null, locale));
		// TICKET 9000003908
		view.addObject("firmantes", new ArrayList<>());
		return view;
	}

	@PostMapping(value = { "/reintentar-conductor/{id}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> reintentarConductor(@PathVariable("id") String workflowId,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);

			respuesta = this.correspondeciaService.reintentarConductor(usuario.getToken(), workflowId, locale);
			respuesta.estado = respuesta.estado;
			respuesta.mensaje = respuesta.mensaje;
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/saltar-paso-conductor/{id}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> saltarPasoConductor(@PathVariable("id") String workflowId,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);

			respuesta = this.correspondeciaService.saltarPasoConductor(usuario.getToken(), workflowId, locale);
			respuesta.estado = respuesta.estado;
			respuesta.mensaje = respuesta.mensaje;

		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

	@PostMapping(value = { "/terminar-paso-conductor/{id}" }, produces = { "application/json" })
	public ResponseEntity<Respuesta<RespuestaApi>> terminarPasoConductor(@PathVariable("id") String workflowId,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuario = obtenerUsuario();
			if (usuario == null)
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);

			respuesta = this.correspondeciaService.terminarPasoConductor(usuario.getToken(), workflowId, locale);
			respuesta.estado = respuesta.estado;
			respuesta.mensaje = respuesta.mensaje;
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
}
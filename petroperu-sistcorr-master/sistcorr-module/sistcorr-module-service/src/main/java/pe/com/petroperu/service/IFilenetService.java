package pe.com.petroperu.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.FiltroConsultaDependencia;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.filenet.model.ItemTipoCorrespondencia;
import pe.com.petroperu.model.DependenciaUnidadMatricial;
import pe.com.petroperu.model.Devolucion;
import pe.com.petroperu.model.Estado;
import pe.com.petroperu.model.Expediente;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.Integrante;
import pe.com.petroperu.model.OrdenServicio;
import pe.com.petroperu.model.ReemplazoAdicion;
import pe.com.petroperu.model.Tracking;
import pe.com.petroperu.model.UsuarioRemplazo;
import pe.com.petroperu.model.Valija;
import pe.com.petroperu.model.VentaBases;
import pe.com.petroperu.model.emision.dto.FuncionariosDTO;
import pe.com.petroperu.model.emision.dto.LogDTO;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;
import pe.com.petroperu.model.emision.dto.ReemplazoRolesAnterioresConsultaDTO;
import pe.com.petroperu.model.emision.dto.TrackingConsultaDTO;
import pe.com.petroperu.service.dto.FiltroConsultaFuncionariosDTO;
import pe.com.petroperu.service.dto.FiltroConsultaLogDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoRolesAnterioresDTO;
import pe.com.petroperu.service.dto.FiltroConsultaTrackingDTO;

public interface IFilenetService {
	
	List<ItemFilenet> listarPaises(String texto);
	
	List<ItemFilenet> listarDepartamentos(String texto);
	
	List<ItemFilenet> listarProvincias(String codDepartamento, String texto);
	
	List<ItemFilenet> listarDistritos(String codDepartamento, String codProvincia, String texto);
	
	List<ItemFilenet> listarLugares(String texto);
	
	List<ItemTipoCorrespondencia> listarTiposCorresponciaEmision(String texto);
	
	List<ItemFilenet> listarDependencias(String codLugar, String texto);
	
	List<ItemFilenet> listarDependenciasNuevo(String codLugar, String texto);
	
	List<ItemFilenet> listarDependenciasRemitente(String texto);
	
	List<ItemFilenet> listarDependenciasAsignacion(String texto);
	
	List<ItemFilenet> listarFuncionarios(String codDependencia);

	List<ItemFilenet> listarDependenciasExternas(String texto);
	
	ItemFilenet obtenerFirmante(String codDependencia);
	
	ItemFilenet obtenerFirmanteRutaAprobacion(String codDependencia);
	
	List<ItemFilenet> listarFuncionariosPorDependencia(String codDependencia, String texto);
	
	/**
	 * Generar correlativo temporal
	 * @return
	 */
	String generarCorrelativoTemporal();
	
	/**
	 * Generar Fecha cgc 
	 * @param usuario
	 * @return  [usuario, cgc, fecha]
	 */
	String[] obtenerFechaCGCUsuario(String usuario);
	
	/**
	 * Obtener Dependencia por Usuario
	 * @param usuario
	 * @return
	 */
	List<ItemFilenet> obtenerDependenciaPorUsuario(String usuario, String texto);
	
	/**
	 * Obtiene el lugar de una dependencia
	 * @param codDependencia
	 * @return
	 */
	ItemFilenet obtenerLugarPorDependencia(String codDependencia);
	
	/**
	 * 
	 * @param codDepartamento
	 * @param codProvincia
	 * @param codDistrito
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDependenciasExternas (String codDepartamento, String codProvincia, String codDistrito, String texto);
	
	/**
	 * 
	 * @param tipo
	 * @return
	 */
	List<Estado> listarEstados (String tipo);
	
	/**
	 * 
	 * @return
	 */
	List<Estado> listarEstadosAsignacion ();
	
	/**
	 * 
	 * @return
	 */
	List<Estado> listarTiposCorrespondencia ();
	
	/**
	 * 
	 * @param codDependencia
	 * @param textoFiltro
	 * @return
	 */
	List<ItemFilenet> listarDependenciasSuperiores(String codDependencia, String textoFiltro);
	
	/**
	 * Lista las dependencias subordinadas
	 * @param codDependencia
	 * @return
	 */
	List<ItemFilenet> listarDependenciasSubordinadas(String codDependencia);

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	List<ItemFilenet> listarTodosFuncionarios(String filtro);

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	List<ItemFilenet> listarTodosDependencias(String filtro);
	
	
	/**4408
	 * 
	 * @param filtro
	 * @return
	 */
	List<ItemFilenet> listarTodosDependenciasJefeGestor(String filtro, String parametro, List<Integer> rolesInt);

	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	List<ItemFilenet> listarTodosDependenciasExternas(String filtro);
	
	List<Funcionario> listarPersonaAsignada(String codigoDependencia, String codigoLugar, String Cadena, String MInclNoActivo);
	
	List<ItemFilenet> listarDependenciasNivelJerarquia(String codigo, String cgc);
	
	// TICKET 9000003997
	List<ItemFilenet> obtenerStatusCorrespondencia(String nroDocumento);
	// FIN TICKET
	
	// TICKET 9000003994
	List<ItemFilenet> obtenerDependenciasGestor(String usuario);
	// FIN TICKET
	
	// TICKET 9000003944
	List<DependenciaUnidadMatricial> buscarDependencias(int codigo, String nombre, String tipo, String jefe);
	
	List<ItemFilenet> listarTipoUnidadMatricial();
	
	List<ItemFilenet> listarJerarquia();
	
	Respuesta<ByteArrayInputStream> consultarDependenciasExcel(FiltroConsultaDependencia filtro, String nombreUsuario, 
			List<ItemFilenet> datosFuncionarios, List<ItemFilenet> jerarquias, List<ItemFilenet> lugares,
			List<ItemFilenet> dependencias, Locale locale);
	
	List<ItemFilenet> listarDependenciasUnidadMatricial(int jerarquia, String codLugar);
	
	List<ItemFilenet> registrarDependencia(DependenciaUnidadMatricial dep, String usuario);
	
	List<ItemFilenet> registrarIntegrante(String integrante, int codigoDependencia);
	
	List<ItemFilenet> eliminarIntegrante(String integrante, int codigoDependencia);
	
	List<Funcionario> listaIntegrante(String codigoDependencia, String codigoLugar,String Cadena, String MInclNoActivo);
	
	List<ItemFilenet> modificarDependencia(DependenciaUnidadMatricial dep, String usuario);
	
	String registrarDependenciaUnidadMatricialCompleto(DependenciaUnidadMatricial dependencia, String usuario);
	
	boolean modificarDependenciaUnidadMatricialCompleto(DependenciaUnidadMatricial dependencia, String usuario);
	
	List<ItemFilenet> listarDependenciasInterno(String codLugar, String texto);
	
	List<ItemFilenet> obtenerDependenciaPorUsuarioConsulta(String usuario, String texto);
	
	List<Integrante> listarIntegrantesUM(String codigoDependencia);
	// FIN TICKET
	// TICKET 9000004409
	String validarRemplazoVigenteUsuario(String usuario);
	List<UsuarioRemplazo> obtenerUsuarioRemplazo(String usuario);
	// FIN TICKET

	/* 9000004276 - INICIO */
	List<ItemFilenet> listarTiposProceso();
	List<ItemFilenet> listarDependenciasCEE(String registro, String cadena,String funcionario);
	/* 9000004276 - FIN */
	
	/*INI Ticket 9*4275*/
	List<ItemFilenet> listaCGC(String texto);
	List<ItemFilenet> listaCouriers(String texto);
	List<ItemFilenet> listaLugares(String texto);
	List<ItemFilenet> listarCiudades(String codPais, String texto);
	List<ItemFilenet> listarCiudadesPorPais(String codPais, String texto);
	List<ItemFilenet> listaNumeradores();
	List<ItemFilenet> listaFuncionariosTodos();
	List<ItemFilenet>  obtenerTablas();
	List<ItemFilenet> guardarFuncionario(Funcionario funcionario, String usuario);	
	List<ItemFilenet> eliminarFuncionario(Integer idFuncionario,String registro, String usuario);
	List<ItemFilenet>listarDependenciaApoyo(String tipoReemplazo, String term);
	List<ItemFilenet>listarJefeXDependencia(String codDepend, String rol, String term);
	List<ItemFilenet>listarFuncionariosApoyo(String tipoReemp,String codDepend, String rol, String term);
	List<ItemFilenet>comboRolDependenciaReemplazo(String codDepend);
	List<ItemFilenet> eliminarReemplazo(Integer idReemplazo, String usuario);
	List<ItemFilenet> validarReemplazo(ReemplazoConsultaDTO reemplazo,String tipoReemplazo,String usuario);
	List<ItemFilenet> guardarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String usuario);
	List<ItemFilenet> eliminarRemplazo(ReemplazoConsultaDTO reemplazo, String usuario);
	List<ReemplazoAdicion> listarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String usuario);
	List<ItemFilenet> obtenerRolDepOriginal(String usuarioEntrante,String usuario);
	List<ItemFilenet> actualizarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String usuario);
	List<ItemFilenet> obtenerValorVar(String username,String variableName);
	List<ItemFilenet> modificarValorVariale(String usuario, String variableName, String valorStr, Integer valor);
	List<ItemFilenet> listarLugaresTV(String texto);
	
	Respuesta<LogDTO> consultaLogPaginado(FiltroConsultaLogDTO filtro,String token, Integer length, Integer start, String columna, String order, String excel, Locale locale);
	Respuesta<ByteArrayInputStream> consultarLogExcel(FiltroConsultaLogDTO filtro, String usuarioCreado,String usuario,Locale locale);
	Respuesta<ReemplazoRolesAnterioresConsultaDTO> consultaRolesAnterioresPaginado(String usuario,FiltroConsultaReemplazoRolesAnterioresDTO filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String tipo, Locale locale);
	Respuesta<ByteArrayInputStream> consultarRolesAnterioresExcel(FiltroConsultaReemplazoRolesAnterioresDTO filtro, String usuarioCreado,String usuario,Locale locale);
	Respuesta<ReemplazoRolesAnterioresConsultaDTO> consultaRolesReemplazoPaginado(String usuario,FiltroConsultaReemplazoRolesAnterioresDTO filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String excel, Locale locale);
	Respuesta<ByteArrayInputStream> consultarRolesReemplazosExcel(FiltroConsultaReemplazoRolesAnterioresDTO filtro, String usuarioCreado,String usuario,Locale locale);
	Respuesta<FuncionariosDTO>consultaFuncionariosPaginado(String token,FiltroConsultaFuncionariosDTO filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String excel, Locale locale);
	Respuesta<ByteArrayInputStream> consultarFuncionarioExcel(FiltroConsultaFuncionariosDTO filtro, String usuarioCreado,String usuario,Locale locale);
	Respuesta<ReemplazoConsultaDTO>consultaReemplazoApoyoPaginado(String token,FiltroConsultaReemplazoDTO filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String excel, String tipoReemplazo, Locale locale);
	Respuesta<ByteArrayInputStream> consultarReemplazoAdicionExcel(FiltroConsultaReemplazoDTO filtro, String usuarioCreado,String usuario,Locale locale);
	Respuesta<ByteArrayInputStream> consultarReemplazoTotalExcel(FiltroConsultaReemplazoDTO filtro, String usuarioCreado,String usuario,Locale locale);
	Respuesta<ByteArrayInputStream> consultarReemplazoApoyoExcel(FiltroConsultaReemplazoDTO filtro, String usuarioCreado,String usuario,Locale locale);
	/*FIN Ticket 9*4275*/
	
	// TICKET 9000003866
	List<ItemFilenet> obtenerDependenciasBandES(String usuario);
	// FIN TICKET
	
	/*INI Ticket 9000004412*/
	List<ItemFilenet> obtenerCentroGestionRemitente(String texto);
	List<ItemFilenet> listarProcesos(String texto);
	String obtenerCGCUsuario( String usuario);
	String registrarValijas(Valija valija, String usuario, String cgc, String token, Locale locale);
	String registrarExpedientes(Expediente expediente, String usuario, String token, Locale locale);
	String validarVentaBases(VentaBases ventaBases, String usuario, String token, Locale locale);
	String registrarVentaBases(VentaBases ventaBases, String usuario, String token, Locale locale);
	String validarConsultaVentaBases(VentaBases ventaBases, String usuario, String token, Locale locale);
	String registrarConsultaVentaBases(VentaBases ventaBases, String usuario, String token, Locale locale);
	String registrarTracking(Tracking tracking, String usuario, String token, String modo, Locale locale);
	Respuesta<TrackingConsultaDTO>consultaTracking(String token,String tipo, FiltroConsultaTrackingDTO filtro,  Locale locale);
	List<ItemFilenet> listaCouriersCGC(String texto);
	List<ItemFilenet> listarMotivos(String texto);
	String registrarDevolucion(Devolucion devolucion, String usuario, String token, Locale locale);
	String asociarOrdenServicio(OrdenServicio ordenServicio, String usuario, String token, Locale locale);
	
	String generarPlanilla(String usuario, String alcance, String curier, String urgente);
	String generarPlanillaGuiaRemision(String usuario, String lugarTrabajo, String courier);

	
	/*FIN Ticket 9000004412*/
}

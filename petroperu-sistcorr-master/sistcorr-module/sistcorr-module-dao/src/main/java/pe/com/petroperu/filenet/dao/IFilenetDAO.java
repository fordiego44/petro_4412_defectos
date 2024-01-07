package pe.com.petroperu.filenet.dao;

import java.util.List;
import java.util.Locale;

import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.Contratacion;
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
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;

public interface IFilenetDAO {
	
	/**
	 * Lista de paises
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarPaises(String texto);
	
	/**
	 * Lista de departamento
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDepartamentos(String texto);
	
	/**
	 * Lista de provincias
	 * @param codDepartamento
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarProvincias(String codDepartamento, String texto);
	
	/**
	 * Lista de distritos
	 * @param codDepartamento
	 * @param codProvincia
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDistritos(String codDepartamento, String codProvincia, String texto);
	
	
	/**
	 * Lista de lugares
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarLugares(String texto);
	
	
	/**
	 * Lista de tipos de correspondencia de emision
	 * @param texto
	 * @return
	 */
	List<? extends ItemFilenet> listarTiposCorresponciaEmision(String texto);
	
	/**
	 * Lista de dependencias
	 * @param codLugar
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDependencias(String codLugar, String texto);
	
	/**
	 * Lista de dependencias
	 * @param codLugar
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDependenciasNuevo(String codLugar, String texto);
	
	List<ItemFilenet> listarDependenciasRemitente(String texto);
	
	
	/**
	 * Lista de dependencias Asignacion
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDependenciasAsignacion(String texto);
	
	
	/**
	 * Lista de funcionarios
	 * @param codDependencia
	 * @return
	 */
	List<ItemFilenet> listarFuncionarios(String codDependencia);

	/**
	 * Lista de dependencias externas
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDependenciasExternas(String texto);
	
	
	/**
	 * Obtener informacion de firmante
	 * @param codDependencia
	 * @return
	 */
	ItemFilenet obtenerFirmante(String codDependencia);
	
	
	/**
	 * Obtener informacion de firmante
	 * @param codDependencia
	 * @return
	 */
	ItemFilenet obtenerFirmanteRutaAprobacion(String codDependencia);
	
	
	/**
	 * Generar CGC
	 * @param codLugar
	 * @param codDependencia
	 * @return
	 */
	String[] obtenerCGC(String codLugar, String codDependencia);
	
	/**
	 * Obtener email de funcionario
	 * @param codUsuario
	 * @return
	 */
	String obtenerEmailFuncionario(String codUsuario);
	
	/**
	 * Listar funcionario por dependencia
	 * @param codDependencia
	 * @return
	 */
	List<ItemFilenet> listarFuncionariosPorDependencia(String codDependencia);
	
	/**
	 * Generar corretlativo temporal
	 * @return
	 */
	String generarCorrelativoTemporal();
	/**
	 * 
	 * @param usuario
	 * @return [usuario, cgc, fecha]
	 */
	String[] obtenerFechaCGCUsuario(String usuario);
	
	/**
	 * Obtener ruc y email de dependencia externa
	 * @param codigoDependencia
	 * @return
	 */
	String[] obtenerRUCEmailDependenciaExterna(String codigoDependencia);
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	List<ItemFilenet> obtenerDependenciaPorUsuario(String username);
	
	
	/**
	 * Obtener el lugar de una dependencia
	 * @param codDependencia
	 * @return
	 */
	ItemFilenet obtenerLugarPorDependencia(String codDependencia);
	
	/**
	 * Obtiene las dependencias externas
	 * @param codDepartamento
	 * @param codProvincia
	 * @param codDistrito
	 * @param texto
	 * @return
	 */
	List<ItemFilenet> listarDependenciasExternas(String codDepartamento, String codProvincia, String codDistrito, String texto);
	
	/**
	 * Obtiene los estados
	 * @param tipo
	 * @return
	 */
	List<Estado> listarEstados(String tipo);
	
	/**
	 * Obtiene los estados
	 * @return
	 */
	List<Estado> listarEstadosAsignacion();
	
	/**
	 * Obtiene los tipos de Correspondencia
	 * @return
	 */
	List<Estado> listarTiposCorrespondencia();
	
	List<Funcionario> listarPersonaAsignada(String codigoDependencia, String codigoLugar, String Cadena, String MInclNoActivo);
	
	
	/**
	 * Obtiene la lista de dependencias superiores superiores
	 * @param codDependencia
	 * @return
	 */
	List<ItemFilenet> listarDependenciasSuperiores(String codDependencia);
	
	
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
	List<ItemFilenet> obtenerDependenciasJefe(String usuario);
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	List<ItemFilenet> listarTodosDependenciasExternas(String filtro);
	
	/**
	 * 
	 * @param codigo
	 * @param cgc
	 * @return
	 */
	List<ItemFilenet> listarDependenciasNivelJerarquia(String codigo, String cgc);
	
	List<ItemFilenet> obtenerStatusCorrespondencia(String nroDocumento);
	
	// TICKET 9000003994
	List<ItemFilenet> obtenerDependenciasGestor(String usuario);
	// FIN TICKET
	
	// TICKET 9000003944
	List<DependenciaUnidadMatricial> buscarDependencias(int codigo, String nombre, String tipo, String jefe);
	
	List<ItemFilenet> listarTipoUnidadMatricial();
	
	List<ItemFilenet> listarJerarquia();
	
	List<ItemFilenet> listarDependenciasUnidadMatricial(int jerarquia, String codLugar);
	
	List<ItemFilenet> registrarDependencia(DependenciaUnidadMatricial dep, String usuario);
	
	List<ItemFilenet> registrarIntegrante(String integrante, int codigoDependencia);
	
	List<ItemFilenet> eliminarIntegrante(String integrante, int codigoDependencia);
	
	List<ItemFilenet> modificarDependencia(DependenciaUnidadMatricial dep, String usuario);
	
	List<ItemFilenet> listarDependenciasInterno(String codLugar, String texto);
	
	List<ItemFilenet> obtenerDependenciaPorUsuarioConsulta(String username);
	
	List<Integrante> listarIntegrantesUM(String codigoDependencia);
	// FIN TICKET
	// TICKET 9000004409
	String validarRemplazoVigenteUsuario(String usuario);
	
	List<UsuarioRemplazo> obtenerUsuarioRemplazo(String usuario);
	// FIN TICKET
	
	/* 9000004276 - INICIO */
	List<Object[]> consultar_comprobantes_paginado(String usuario, String radicado, String fechaDesde, String fechaHasta, String nroBatch, String ruc, String nroComprobante, Integer codigoEstado, Integer codDependencia, String razonSocial, Integer itemsPorPagina, Integer numeroPagina, String exportaExcel, String nombreColumna, String desc);
	List<Object[]> consultar_contrataciones_paginado(String usuario, String nroProceso, String tipoProceso, String nroMemo, Integer codDependencia, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc);
	List<ItemFilenet> listarTiposProceso();
	Object[] consultar_detalle_comprobante(String usuario, String correlativo);
	List<ItemFilenet> listarDependenciasCEE(String registro, String cadena, String funcionario);
	Contratacion consultar_detalle_contratacion(String usuario, String correlativo) ;
	List<Object[]> consultar_venta_base(String radicado);
	List<Object[]> consultar_consultas_base(String nroProceso);
	List<Object[]> consultar_propuestas(String radicado);
	List<Object[]> consultar_impugnaciones(String radicado);
	/* 9000004276 - FIN */
	
	/*INI Ticket 9*4275*/
	List<ItemFilenet> listaCGC(String texto);
	List<ItemFilenet> listaCouriers(String texto);
	List<ItemFilenet> listaLugares(String texto);
	List<ItemFilenet> listarCiudades(String codPais, String texto);
	List<ItemFilenet> listarCiudadesPorPais(String codPais, String texto);
	List<ItemFilenet> listaNumeradores();
	List<ItemFilenet> listaFuncionariosTodos();
	List<ItemFilenet> obtenerTablas();
	List<ItemFilenet> guardarFuncionario(Funcionario funcionario,String usuario);
	List<ItemFilenet> eliminarFuncionario(Integer idFuncionario,String registro,String usuario);
	List<ItemFilenet> listarDependenciaApoyo(String tipoReemplazo, String term);
	List<ItemFilenet> listarJefeXDependencia(String codDepend, String rol, String term);
	List<ItemFilenet> listarFuncionariosApoyo(String tipoReemp,String codDepend, String rol, String term);
	List<ItemFilenet> comboRolDependenciaReemplazo(String codDepend);
	List<ItemFilenet> eliminarReemplazo(Integer idReemplazo, String usuario);
	List<ItemFilenet> validarReemplazo(ReemplazoConsultaDTO reemplazo,String tipoReemplazo,String usuario);
	List<ItemFilenet> guardarReemplazoAdicion(ReemplazoConsultaDTO reemplazo,String usuario);
	List<ItemFilenet> obtenerRolDepOriginal(String usuarioEntrante,String usuario);
	List<ItemFilenet> actualizarReemplazoAdicion(ReemplazoConsultaDTO reemplazo,String usuario);
	List<ItemFilenet> obtenerValorVar(String userName,String variableName);
	List<ItemFilenet> eliminarReemplazo(ReemplazoConsultaDTO reemplazo, String usuario);
	List<ReemplazoAdicion> listarReemplazosAdicion(ReemplazoConsultaDTO reemplazo, String usuario);
	List<ItemFilenet> modificarValorVariale(String usuario, String variableName, String valorStr, Integer valor);
	List<ItemFilenet> listarLugaresTV(String texto);
	
	public List<Object[]> consultaLog(String tabla, String usuario, String accion, String fechaDesde, String fechaHasta
			, String token, int length, int start, String columna, String orden, String excel, Locale locale);
	
	public List<Object[]> consultaRolesAnterioresPaginado(String codUsuario,String tipoTransaccion, String tipoReemplazo, String correlativo, String fechaDesde, 
			String fechaHasta,Integer codTipoCorrespondencia,String codigoEstado,String fechaDocumento,Integer codDestino,Integer codDepRemitente,String externa,
			String asunto, String nroDocumento, String rol, Integer itemsPorPagina,Integer numeroPagina, String columnaOrden, String orden, String tipo);
	
	public List<Object[]> consultaReemplazoAdicion(String idUsuario,Integer codDependencia, String rol, String usuarioSaliente, String usuarioEntrante, String fechaDesde, String fechaHasta, Integer estado, String referencia, 
			String tipo,Integer itemsPorPagina, Integer numeroPagina, String excel,String columnaOrden, String orden);
	
	public List<Object[]> consultaAdministracionFuncionarios(String idUsuario,Integer codDependencia, String registro, String nombres, String apellidos, 
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String tipo);
	
	public List<Object[]>obtenerValorVar(String userName,String variableName,String valueSring,Integer valueInt);
	/*FIN Ticket 9*4275*/
	
	// TICKET 9000003866
	List<ItemFilenet> obtenerDependenciasBandES(String usuario);
	// FIN TICKET
	
	/*INI Ticket 9000004412*/
	List<ItemFilenet> obtenerCentroGestionRemitente(String texto);
	List<ItemFilenet> listarProcesos(String texto);
	String obtenerCGCUsuario( String usuario);
	List<ItemFilenet> registrarValija(Valija valija, String usuario, String cgc);
	List<ItemFilenet> registrarVentaBases(VentaBases ventaBases, String usuario);
	List<ItemFilenet> registrarConsultaVentaBases(VentaBases ventaBases, String usuario);
	List<ItemFilenet> registrarExpediente(Expediente expediente, String usuario);
	List<ItemFilenet> validarVentaBases(VentaBases ventaBases, String usuario);
	List<ItemFilenet> validarConsultaVentaBases(VentaBases ventaBases, String usuario);
	List<ItemFilenet> registrarTracking(Tracking tracking, String usuario, String modo);
	public List<Object[]> consultaTracking(String idUsuario,String correlativo);
	List<ItemFilenet> listaCouriersCGC(String texto);
	List<ItemFilenet> listarMotivos(String texto);
	List<ItemFilenet> registrarDevolucion(Devolucion devolucion, String usuario);
	List<ItemFilenet> asociarOrdenServicio(OrdenServicio ordenServicio, String usuario);
	List<Object[]> consultar_estDigContrataciones_paginado(String usuario, String nroProceso, String fechaDesde, String fechaHasta,String ruc, String estado, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc);
	List<Object[]> consultar_despacho_paginado(String usuario,String nroCorrelativo,String codEstado,String fechaDesde,String fechaHasta,String dependenciaRemitente,String usuarioRemitente,String numeroDocumento,String entidadExterna,String asunto,String guiaRemision, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc);
	List<Object[]> consultar_valijas_recibidas_paginado(String usuario,String nroCorrelativo,String codEstado,String fechaDesde,String fechaHasta,String cgcReceptor,String cgcRemitente,String courier, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc);
	String generarPlanilla(String usuario, String alcance, String curier, String urgente);
	String generarPlanillaGuiaRemision(String usuario, String lugarTrabajo, String courier);
	
	/*FIN Ticket 9000004412*/
}

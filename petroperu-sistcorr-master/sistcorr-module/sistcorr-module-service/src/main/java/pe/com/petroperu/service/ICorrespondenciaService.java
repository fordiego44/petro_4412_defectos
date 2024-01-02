package pe.com.petroperu.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;

import org.springframework.http.ResponseEntity;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.AgregarAsignacionMasivaParametro;
import pe.com.petroperu.cliente.model.AgregarAsignacionParametro;
import pe.com.petroperu.cliente.model.AsignacionGrupal;
import pe.com.petroperu.cliente.model.Bandeja;
import pe.com.petroperu.cliente.model.CompletarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.cliente.model.FiltroConsultaAuditoria;
import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.ListaFiltroConductor;
import pe.com.petroperu.cliente.model.ListaFiltroCorrespondencia;
import pe.com.petroperu.cliente.model.RechazarAsignacionCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExternaRespuesta;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInternaRespuesta;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.model.Accion;
import pe.com.petroperu.model.Asignacion;
import pe.com.petroperu.model.AsignacionConsulta;
import pe.com.petroperu.model.ConductorPaginado;
import pe.com.petroperu.model.CopiaCorrespondencia;
import pe.com.petroperu.model.Correspondencia;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaTareaPaginado;
import pe.com.petroperu.model.Estado;
import pe.com.petroperu.model.FiltroSiguientePagina;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.InformacionAsignacion;
import pe.com.petroperu.model.Menu;
import pe.com.petroperu.model.Observaciones;
import pe.com.petroperu.model.TrackingFisico;
import pe.com.petroperu.model.Traza;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;
import pe.com.petroperu.model.emision.DestinatarioDocPagar;
import pe.com.petroperu.service.model.ItemMenu;

public interface ICorrespondenciaService {
	
	
	
	/**
	 * Obtiene el menu con ssus respectivos contadores
	 * @param usuario
	 * @param estado
	 * @param locale
	 * @return
	 */
	List<Menu> obtenerMenuSistcorr(UsuarioPetroperu usuario, boolean estado, Locale locale);
	
	/**
	 * Obtene el menu de la aplicació
	 * @param usuario
	 * @param locale
	 * @return
	 */
	List<Menu> obtenerMenuSistcorr(UsuarioPetroperu usuario, Locale locale);
	
	/**
	 * Obtiene la lista de tareas por bandeja
	 * @param token
	 * @param bandeja
	 * @param locale
	 * @return
	 */
	Respuesta<Bandeja> obtenerColeccion(String token, String bandeja, Locale locale);
	
	/**
	 * Ontiene información del documento principal
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<InformacionDocumento> obtenerDocumentoPrincipal(String token, String correlativo, Locale locale);
	
	
	/**
	 * Descarga el documento principal
	 * @param token
	 * @param idDocumentos
	 * @param locale
	 * @return
	 */
	ResponseEntity<byte[]> descargarDocumentoPrincipal(String token, String idDocumentos, Locale locale);
	
	/**
	 * Recupera la tarea
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> recuperarCorrespondencia(String token, String correlativo, Locale locale);
	
	
	/**
	 * Obtene la lista de documentos adjuntos de la tarea
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<InformacionDocumento> obtenerDocumentosAdjuntos(String token, String correlativo, Locale locale);
	
	
	/**
	 * Obtener el historico de asignacionesde
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<Asignacion> obtenerHistorialAsignaciones(String token, String correlativo, Locale locale);
	
	
	/**
	 * Obtener listado de acciones de la aplicacion
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<Accion> obtenerListaAcciones(String token, Locale locale);
	
	/**
	 * Buscar correspondencia
	 * @param token
	 * @param bandeja
	 * @param filtro
	 * @param tamanio
	 * @param locale
	 * @return
	 */
	//Respuesta<Bandeja> buscarCorrespondencia(String token, String bandeja, ListaFiltroCorrespondencia filtro, Locale locale);
	Respuesta<CorrespondenciaTareaPaginado> buscarCorrespondencia(String token, String bandeja, ListaFiltroCorrespondencia filtro, String tamanio, Locale locale);
	
	/**
	 * Recuperar funcionarios
	 * @param token
	 * @param wobNum
	 * @return
	 */
	Respuesta<Funcionario> recuperarFuncionarios(String token, String wobNum, Locale locale);
	
	/**
	 * Agregar asignacion
	 * @param token
	 * @param asignacion
	 * @param locale
	 * @return
	 */
	Respuesta<Asignacion> agregarAsignacion(String token, AgregarAsignacionParametro asignacion, Locale locale);
	
	/**
	 * Enviar asignaciones
	 * @param token
	 * @param correlativo
	 * @param wobNum
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> enviarAsignaciones(String token, String correlativo, Locale locale);
	
	
	/**
	 * Recuperar temporal de asignaciones
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<Asignacion> recuperarTemporalAsignaciones(String token, String correlativo, Locale locale);
	
	
	/**
	 * Borrar asignacion
	 * @param token
	 * @param idAsignacion
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> borrarAsignacion(String token, Integer idAsignacion, Locale locale);

	/**
	 *  Cerrar correspondencia
	 * @param token
	 * @param correlativo
	 * @param observacion
	 * @param usuarioResponsable
	 * @param documentoRespuesta
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> cerrarCorrespondencia(String token, String correlativo, String observacion, String usuarioResponsable, String documentoRespuesta, Locale locale);

	/**
	 *  Aceptar correspondencia
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> aceptarCorrespondencia(String token, String correlativo, Locale locale);
	
	/**
	 *  Procesar Aceptar correspondencia Bandeja Salida
	 * @param token
	 * @param nroDocumento
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> procesarAceptarCorrespondencia(String token, String usuarioModifica, String nombreUsuario, String nroDocumento, String correlativo, Locale locale);
	
	/**
	 *  Procesar Aceptar correspondencia Bandeja Salida
	 * @param token
	 * @param nroDocumento
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> procesarRechazarCorrespondencia(String token, String usuarioModifica, String usuarioNombreCompleto, String nroDocumento, String correlativo, String observacion, Locale locale);

	/**
	 *  Rechazar correspondencia
	 * @param token
	 * @param correlativo
	 * @param observacion
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> rechazarCorrespondencia(String token, String correlativo, String observacion, Locale locale);

	
	/**
	 * Completar correspondencia
	 * @param token
	 * @param correspondencia
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> completarCorrespondencia(String token, Integer idAsignacion, CompletarCorrespondenciaParametro correspondencia, Locale locale);
	
	/**
	 * Completar correspondencia
	 * @param token
	 * @param correspondencia
	 * @param locale
	 * @return
	 * TICKET 9000004273
	 */
	Respuesta<RespuestaApi> rechazarAsignacionCorrespondencia(String token, Integer idAsignacion, RechazarAsignacionCorrespondenciaParametro correspondencia, Locale locale);

	/**
	 * Recuperar información de asignación
	 * @param token
	 * @param idAsignacion
	 * @param locale
	 * @return
	 */
	Respuesta<InformacionAsignacion> recuperarInformacionAsignacion(String token, Integer idAsignacion, Locale locale);

	/**
	 * Agregar asignación masiva
	 * @param token
	 * @param asignacion
	 * @param locale
	 * @return
	 */
	Respuesta<Asignacion> agregarAsignacionMasivo(String token, AgregarAsignacionMasivaParametro asignacion, Locale locale);
	
	/**
	 * Registrar correspondencia interna
	 * @param token
	 * @param correspondencia
	 * @param locale
	 * @return Adjuntos / Correspondencia
	 */
	 Respuesta<Object> registrarCorrespondencia(String token, pe.com.petroperu.model.emision.Correspondencia correspondencia, CorrespondenciaInterna correspondenciaI, String usuario, Locale locale);

	/**
	 * Registrar correspondencia externa
	 * @param token
	 * @param correspondencia
	 * @param locale
	 * @return Adjuntos / Correspondencia
	 */
	 Respuesta<Object> registrarCorrespondencia(String token, pe.com.petroperu.model.emision.Correspondencia correspondencia, CorrespondenciaExterna correspondenciaE, String usuario, Locale locale);

	
	/**
	 * Cargar archivos adjuntos
	 * @param token
	 * @param usuario
	 * @param correspondencia
	 * @param correlativoTenporal
	 * @param cgcFecha
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaCargaAdjunto> cargarArchivosAdjuntos(String token, String usuario, pe.com.petroperu.model.emision.Correspondencia correspondencia, String correlativoTenporal, String[] cgcFecha, Locale locale);
	
	Respuesta<RespuestaCargaAdjunto> cargarArchivoAdjunto(String token, String usuario, pe.com.petroperu.model.Correspondencia correspondencia, String correlativoTemporal, Locale locale, ArchivoAdjunto archivo);
	
	// TICKET 9000003514
	/**
	 * Obtener la lista de trazas
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<Traza> obtenerListaTrazas(String token, String correlativo, Locale locale);
	
	/**
	 * Obtener la lista de observaciones
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<Observaciones> obtenerListaObservaciones(String token, String correlativo, Locale locale);
	
	/**
	 * Obtener el tracking fisico
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<TrackingFisico> obtenerTrackingFisico(String token, String correlativo, Locale locale);
	
	/**
	 * Obtener el tracking fisico
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaConsulta> consultarCorrespondencia(String token, FiltroConsultaCorrespondencia filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);
	
	/**
	 * Obtener el tracking fisico
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<AsignacionConsulta> consultarAsignaciones(String token, FiltroConsultaAsignacion filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);
	
	List<Estado> listarEstados(String tipo);
	
	List<Estado> listarEstadosAsignacion();
	
	/**
	 * Obtener la lista de copia correspondencia
	 * @param token
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<CopiaCorrespondencia> obtenerCopiaCorrespondencia(String token, String correlativo, Locale locale);
	
	/**
	 * Eliminar una copia de correspondencia
	 * @param token
	 * @param usuario
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> eliminarCopiaCorrespondencia(String token, String usuario, String correlativo, Locale locale);
	
	/**
	 * Agregar una copia de correspondencia
	 * @param token
	 * @param usuario
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<CopiaCorrespondencia> agregarCopiaCorrespondencia(String token, String usuario, String correlativo, Locale locale);
	
	/**
	 * Obtener la lista de funcionarios
	 * @param token
	 * @param codigoDependencia
	 * @param codigoLugar
	 * @param activo
	 * @param locale
	 * @return
	 */
	Respuesta<Funcionario> obtenerFuncionarios(String token, String codigoDependencia, String codigoLugar, String activo, Locale locale);
	
	/**
	 * Obtener la lista de funcionarios
	 * @param token
	 * @param codigoDependencia
	 * @param codigoLugar
	 * @param activo
	 * @param locale
	 * @return
	 */
	Respuesta<Funcionario> obtenerFuncionariosRuta(String token, String codigoDependencia, String codigoLugar, String activo, String texto, Locale locale);
	
	/**
	 * Enviar copia de correspondencia
	 * @param token
	 * @param texto
	 * @param correlativo
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> enviarCopiaCorrespondencia(String token, String observacion, String correlativo, Locale locale);
	
	List<Estado> listarEstadosFile(String tipo);
	
	List<Estado> listarTiposCorrespondencia();
	
	/**
	 * Enviar copia de correspondencia
	 * @param token
	 * @param idDocumento
	 * @param proceso
	 * @param codigoTraza
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> eliminarDocumento(String token, String idDocumento, String proceso, String codigoTraza, Locale locale);
	
	/**
	 * Registrar observacion
	 * @param token
	 * @param correlativo
	 * @param observacion
	 * @param locale
	 * @return
	 */
	Respuesta<RespuestaApi> registrarObservacion(String token, String correlativo, String observacion, Locale locale);
	
	// TICKET 9000004510
	Object[] descargarDocumentoServidor(String token, String idDocumento, Locale locale);
	// FIN TICKET
	
	// TICKET 900004494
	Respuesta<CorrespondenciaTareaPaginado> filtraCorrespondenciasSiguientePagina(String token, FiltroSiguientePagina filtros, Locale locale);
	// FIN TICKET
	
	// TICKET 900004494
	Respuesta<Asignacion> agregarAsignacionGrupal(String token, AgregarAsignacionParametro asignacion, Locale locale,AsignacionGrupal asignacionGrupal);	

	Respuesta<RespuestaApi> validarAsignacionGrupal(String token, AsignacionGrupal asignacioGrupal, Locale locale);
	
	Respuesta<Asignacion> recuperarTemporalAsignacionGrupal(String token, String grupoCorrelativo, Locale locale);
	
	Respuesta<RespuestaApi> enviarAsignacionGrupal(String token, AsignacionGrupal asignacionGrupal, Locale locale);
	
	Respuesta<DestinatarioDocPagar> recuperarDestinatariosDocPorPagar();//ticket 9000004765
	
	// FIN TICKET
	
	/*INI Ticket 9*4275*/
	Respuesta<ReemplazoConsultaDTO> notificacionReemplazo(String token, String sCodReemplazo, Locale locale);
	Respuesta<ConductorPaginado> buscarConductor(String token, String bandeja, ListaFiltroConductor filtro, String tamanio, Locale locale);
	Respuesta<ConductorPaginado> filtraConductorSiguientePagina(String token, FiltroSiguientePagina filtros, Locale locale);
	Respuesta<RespuestaApi> reintentarConductor(String token, String workflowId, Locale locale);
	Respuesta<RespuestaApi> saltarPasoConductor(String token, String workflowId, Locale locale);
	Respuesta<RespuestaApi> terminarPasoConductor(String token, String workflowId, Locale locale);
	/*FIN Ticket 9*4275*/
	
	/*INI Ticket 9*4807*/
	/**
	 * 
	 * @param filtro
	 * @param usuarioCreador
	 * @param usuario
	 * @param bandeja
	 * @param locale
	 * @return
	 */
	Respuesta<ByteArrayInputStream> exportarExcelCorrespondenciasAsignacionesBandejaEntrada(ListaFiltroCorrespondencia filtro, String usuarioCreador, String usuario, String bandeja,
			Locale locale);
	/*FIN Ticket 9*4807*/
	// TICKET 9000004961
	Respuesta<CorrespondenciaConsulta> consultarCorrespondenciaAuditoria(String token, FiltroConsultaAuditoria filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);
	// FIN TICKET
	
	/*INI Ticket 9*4413*/
	Respuesta<Correspondencia> recuperarCorrespondenciaMPV(String token, String correlativo, Locale locale);
	Respuesta<RespuestaApi> rechazarCorrespondenciaMPV(String token, String correlativo, String observacion, Locale locale);
	Respuesta<RespuestaApi> asignarDependenciaMPV(String token, String correlativo, String cgc, String dependencia, String accion, Locale locale);
	/*FIN Ticket 9*4413*/
	
	/*INI Ticket 9000004412*/
	Respuesta<RespuestaApi> crearExpediente(String token,String nroProceso, Locale locale);
	/*FIN Ticket 9000004412*/
	
}

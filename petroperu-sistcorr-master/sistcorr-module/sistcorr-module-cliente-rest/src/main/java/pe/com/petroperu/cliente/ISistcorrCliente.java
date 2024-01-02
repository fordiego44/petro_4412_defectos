package pe.com.petroperu.cliente;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.springframework.http.ResponseEntity;


import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.AgregarAsignacionMasivaParametro;
import pe.com.petroperu.cliente.model.AgregarAsignacionParametro;
import pe.com.petroperu.cliente.model.AsignacionGrupal;
import pe.com.petroperu.cliente.model.AsignarDependenciaParametro;
import pe.com.petroperu.cliente.model.AsignacionGrupalRequest;
import pe.com.petroperu.cliente.model.Bandeja;
import pe.com.petroperu.cliente.model.CerrarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.CompletarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.ContadorBandeja;
import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.ListaFiltroConductor;
import pe.com.petroperu.cliente.model.ListaFiltroCorrespondencia;
import pe.com.petroperu.cliente.model.RechazarAsignacionCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RechazarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RegistrarObservacion;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.cliente.model.emision.AsignarDocumento;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExternaRespuesta;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInternaRespuesta;
import pe.com.petroperu.cliente.model.emision.Folder;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.model.Accion;
import pe.com.petroperu.model.Asignacion;
import pe.com.petroperu.model.AsignacionConsulta;
import pe.com.petroperu.model.ConductorPaginado;
import pe.com.petroperu.model.CopiaCorrespondencia;
import pe.com.petroperu.model.Correspondencia;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaTareaPaginado;
import pe.com.petroperu.model.FiltroSiguientePagina;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.InformacionAsignacion;
import pe.com.petroperu.model.Observaciones;
import pe.com.petroperu.model.TrackingFisico;
import pe.com.petroperu.model.Traza;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;

/**
 * 
 * @author KenyoJoelPechoNaupar
 *
 */
public interface ISistcorrCliente {
	
	
	String getApiUsuarioNombre();

	String getApiUsuarioClave();
	
	/**
	 * Autenticación de usuarios de SISTCORR.
	 * @param nombreUsuario
	 * @param claveUsuario
	 * @return
	 */
	Respuesta<String> recuperarToken(String nombreUsuario, String claveUsuario);
	
	/**
	 * Descarga de adjunto de una correspondencia
	 * @param token
	 * @param documentoId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	ResponseEntity<byte[]> descargarDocumento(String token, String documentoId);

	/**
	 * Obtiene la información del documento principal de una correspondencia de la lista de adjuntos
	 * @param token			Token de sesión.
	 * @param correlativo	Correlativo de la correspondencia.
	 * @return
	 */
	Respuesta<InformacionDocumento> recuperarDocumento(String token, String correlativo);

	
	/**
	 * Retorna la cantidad de tareas especificando la bandeja
	 * @param token
	 * @param bandeja
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	Respuesta<ContadorBandeja> contadorBandeja(String token, String bandeja);
	
	/**
	 * Lista de correspondencias y Asignaciones
	 * @param token		Token de sesión
	 * @param bandeja	Nombre de la bandeja a mostrar las tareas
	 * @return
	 */
	Respuesta<Bandeja> recuperarCorrespondencias(String token, String bandeja);
	
	/**
	 * Obtiene la información de una correspondencia 
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<Correspondencia> recuperarCorrespondencia(String token, String correlativo);
	
	/**
	 * Recupera la información de los adjuntos de la correspondencia.
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<InformacionDocumento> recuperarDocumentos(String token, String correlativo, Locale locale);
	
	/**
	 * Recupear historial de asignaciones
	 * @param token
	 * @param tareaId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	Respuesta<Asignacion> recupearHistorial(String token, String correlativo);
	
	/**
	 * Recupera la lista de acciones para la función asignar acciones
	 * @param token
	 * @return
	 */
	Respuesta<Accion> recuperarAcciones(String token);
	
	
	
	/**
	 * Recupera la lista de funcionarios.
	 * @param token
	 * @param wobNum	WobNum del workflow.
	 * @return
	 */
	Respuesta<Funcionario> recuperarFuncionarios(String token, String wobNum);
	
	/**
	 * Agregar asignacion
	 * @param token
	 * @param parametro
	 * @return
	 */
	Respuesta<Asignacion> agregarAsignacion(String token, AgregarAsignacionParametro parametro);
	
	/**
	 * Borrar asignacion
	 * @param token
	 * @param asignacion
	 * @return
	 */
	Respuesta<RespuestaApi> borrarAsigancion(String token, Integer idAsignacion);

	
	/**
	 * Enviar asignacion
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<RespuestaApi> enviarAsignacion(String token, String correlativo);
	
	
	/**
	 * Completar asignacion
	 * @param token
	 * @param idAsignacion
	 * @param correspondencia
	 * @return
	 */
	Respuesta<RespuestaApi> completarAsignacion(String token, Integer idAsignacion, CompletarCorrespondenciaParametro correspondencia);
	
	/**
	 * Completar asignacion
	 * @param token
	 * @param idAsignacion
	 * @param correspondencia
	 * @return
	 * TICKET 9000004273
	 */
	Respuesta<RespuestaApi> rechazarAsignacionCorrespondencia(String token, Integer idAsignacion, RechazarAsignacionCorrespondenciaParametro correspondencia);
	
	
	
	/**
	 * Lista de correspondencias y asignaciones (Filtro)
	 * @param token
	 * @param filtros
	 * @return
	 */
	Respuesta<Bandeja> filtraCorrespondencias(String token, String bandeja, ListaFiltroCorrespondencia filtros);
	
	/**
	 * Termina el proceso de la correspondencia.
	 * @param token
	 * @param correlativo
	 * @param parametros
	 * @return
	 */
	Respuesta<RespuestaApi> cerrarCorrespondencia(String token, String correlativo, CerrarCorrespondenciaParametro parametros);
	
	/**
	 * Termina el proceso de la correspondencia.
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<RespuestaApi> aceptarCorrespondencia(String token, String correlativo);
	
	/**
	 * Termina el proceso de la correspondencia.
	 * @param token
	 * @param correlativo
	 * @param parametros
	 * @return
	 */
	Respuesta<RespuestaApi> rechazarCorrespondencia(String token, String correlativo, RechazarCorrespondenciaParametro parametros);
	
	
	/**
	 * Recuperar temporal de asignaciones
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<Asignacion> recuperarTemporalAsignaciones(String token, String correlativo);

	/**
	 * Enviar asignacion masivo
	 * @param token
	 * @param parametro
	 * @return
	 */
	Respuesta<Asignacion> agregarAsignacionMasivo(String token, AgregarAsignacionMasivaParametro parametro);

	/**
	 * Obtiene la información de una asignación y de la correspondencia.
	 * @param token
	 * @param idAsignacion
	 * @return
	 */
	Respuesta<InformacionAsignacion> recuperarAsignacion(String token, Integer idAsignacion);
	
	/**
	 * Registra una Correspondencia Emitida Interna (CEI).
	 * @param token
	 * @param correlativo
	 * @param correspondencia
	 * @return
	 */
	Respuesta<CorrespondenciaInternaRespuesta> registrarCorrespondenciaEmitidaInterna(String token, String correlativo, CorrespondenciaInterna correspondencia);
	
	/**
	 * Registra una Correspondencia Emitida Externa (CEE).
	 * @param token
	 * @param correlativo
	 * @param correspondencia
	 * @return
	 */
	Respuesta<CorrespondenciaExternaRespuesta> registrarCorrespondenciaEmitidaExterna(String token, String correlativo, CorrespondenciaExterna correspondencia);
	
	
	/**
	 * Cargar adjunto
	 * @param token
	 * @param parametro
	 * @param folder
	 * @param file
	 * @param fileName
	 * @return
	 */
	Respuesta<RespuestaCargaAdjunto> cargarAdjunto(String token, AsignarDocumento parametro, Folder folder, File file, String fileName, String titulo);
	
	/**
	 * Listar traza
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<Traza> recuperarTraza(String token, String correlativo);
	
	/**
	 * Listar observaciones
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<Observaciones> recuperarObservaciones(String token, String correlativo);
	
	/**
	 * Listar tracking fisico
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<TrackingFisico> recuperarTrackingFisico(String token, String correlativo);
	
	/**
	 * Listar tracking fisico
	 * @param token
	 * @return
	 */
	Respuesta<CorrespondenciaConsulta> consultarCorrespondencia(String token, FiltroConsultaCorrespondencia filtro);
	
	/**
	 * Listar tracking fisico
	 * @param token
	 * @return
	 */
	Respuesta<AsignacionConsulta> consultarAsignaciones(String token, FiltroConsultaAsignacion filtro);
	
	/**
	 * Listar copias correspondencia
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<CopiaCorrespondencia> recuperarCopiaCorrespondencia(String token, String correlativo);
	
	/**
	 * Eliminar copia correspondencia
	 * @param token
	 * @param usuario
	 * @param correlativo
	 * @return
	 */
	Respuesta<RespuestaApi> eliminarCopiaCorrespondencia(String token, String usuario, String correlativo);
	
	/**
	 * Agregar copia correspondencia
	 * @param token
	 * @param usuario
	 * @param correlativo
	 * @return
	 */
	Respuesta<CopiaCorrespondencia> agregarCopiaCorrespondencia(String token, String usuario, String correlativo);
	
	/**
	 * Obtener funcionarios
	 * @param token
	 * @param usuario
	 * @param correlativo
	 * @return
	 */
	Respuesta<Funcionario> obtenerFuncionarios(String token, String codigoDependencia, String codigoLugar, String activo);
	
	/**
	 * Eliminar copia correspondencia
	 * @param token
	 * @param texto
	 * @param correlativo
	 * @return
	 */
	Respuesta<RespuestaApi> enviarCopiaCorrespondencia(String token, String texto, String correlativo);
	
	/**
	 * Eliminar de adjunto de una correspondencia
	 * @param token
	 * @param documentoId
	 * @param proceso
	 * @param codigoTraza
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	Respuesta<RespuestaApi> eliminarDocumento(String token, String documentoId, String proceso, String codigoTraza);
	
	/**
	 * Registrar una observación de la correspondencia.
	 * @param token
	 * @param parametros
	 * @return
	 */
	Respuesta<RespuestaApi> registrarObservacion(String token, RegistrarObservacion parametros);
	
	/**
	 * Completar una asignación automáticamente.
	 * @param token
	 * @param idAsignacion
	 * @return
	 */
	Respuesta<RespuestaApi> completarAsignacionAutomatico(String token, Integer idAsignacion);
	
	// TICKET 9000004510
	ResponseEntity<byte[]> descargarDocumentoServidor(String token, String documentoId);
	
	Respuesta<RespuestaCargaAdjunto> cargarArchivoServidor(String token, AsignarDocumento parametro, File file, String fileName);
	
	Respuesta<RespuestaApi> eliminarDocumentoServidor(String token, String documentoId);
	// FIN TICKET
	
	// TICKET 9000004494
	/**
	 * Lista de correspondencias y asignaciones (Filtro)
	 * @param token
	 * @param filtros
	 * @return
	 */
	Respuesta<CorrespondenciaTareaPaginado> filtraCorrespondenciasPrimeraPagina(String token, String bandeja, ListaFiltroCorrespondencia filtros, String tamanio);
	
	/**
	 * Lista de correspondencias y asignaciones (Filtro)
	 * @param token
	 * @param filtros
	 * @return
	 */
	Respuesta<CorrespondenciaTareaPaginado> filtraCorrespondenciasSiguientePagina(String token, FiltroSiguientePagina filtros);
	// FIN TICKET
	
	// TICKET 9000004494
	/**
	 * Enviar asignacion Grupal
	 * @param token
	 * @param correlativo
	 * @return
	 */
	Respuesta<RespuestaApi> enviarAsignacionGrupal(String token, AsignacionGrupalRequest asignacionGrupal);
	// FIN TICKET

	/* 9000004276 - INICIO */
	Respuesta<InformacionDocumento> recuperarDocumentosGeneral(String token, String clase, String correlativo, Locale locale);
	/* 9000004276 - FIN */
	
	/*INI Ticket 9*4275*/
	Respuesta<ReemplazoConsultaDTO>notificacionReemplazo(String token,String sCodReemplazo);
	Respuesta<ConductorPaginado> filtraConductorPrimeraPagina(String token, String bandeja, ListaFiltroConductor filtros, String tamanio);
	Respuesta<ConductorPaginado> filtraConductorSiguientePagina(String token, FiltroSiguientePagina filtros);
	Respuesta<RespuestaApi> reintentarConductor(String token, String workflowId);
	Respuesta<RespuestaApi> saltarPasoConductor(String token, String workflowId);
	Respuesta<RespuestaApi> terminarPasoConductor(String token, String workflowId);
	/*FIN Ticket 9*4275*/
	
	/*INI Ticket 9*4413*/
	Respuesta<RespuestaApi> rechazarCorrespondenciaMPV(String token, String correlativo, RechazarCorrespondenciaParametro parametros);
	Respuesta<RespuestaApi> asignarDependenciaMPV(String token, String correlativo, AsignarDependenciaParametro parametros);
	/*FIN Ticket 9*4413*/
	
	/*INI Ticket 9000004412*/
	Respuesta<RespuestaApi> crearExpediente(String token,String nroProceso);
	/*FIN Ticket 9000004412*/
}

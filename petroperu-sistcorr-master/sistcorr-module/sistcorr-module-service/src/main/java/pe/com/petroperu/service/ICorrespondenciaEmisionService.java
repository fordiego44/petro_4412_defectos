package pe.com.petroperu.service;

import java.util.List;
import java.util.Locale;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.model.UsuarioRemplazo;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correlativo;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.DestinatarioExterno;
import pe.com.petroperu.model.emision.DestinatarioRespuesta;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaDTO;
import pe.com.petroperu.model.emision.dto.DatosFirmanteDTO;
import pe.com.petroperu.service.dto.Flujo;

/**
 * @author User
 *
 */
public interface ICorrespondenciaEmisionService {
	
	/**
	 * Registra correspondencia
	 	 * @param idCorrespondencia
	 * @param usuarioModifica
	 * @param idDestinatarioExterno
	 * @param correo
	 * @param locale
	 * @return
	 */
	Respuesta<DestinatarioExterno> actualizarCorreoDestinatarioExterno(Long idCorrespondencia, String usuarioModifica, Long idDestinatarioExterno, String correo, Locale locale);
	
	/**
	 * Registra correspondencia
	 * @param correspondencia
	 * @param usuarioCrea
	 * @param nombreCompletoUsuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> registrar(Correspondencia correspondencia, String usuarioCrea, String nombreCompletoUsuarioCrea, List<RutaAprobacion> aprobadores, Locale locale);
	
	/**
	 * Modificar correspondencia
	 * @param correspondencia
	 * @param usuarioMaodifica
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> modificar(Correspondencia correspondencia, String usuarioMaodifica, List<RutaAprobacion> aprobadores, Locale locale, List<UsuarioRemplazo> usuarioRemplazo);
	
	/**
	 * Registrar archivo adjunto
	 * @param archivoAdjunto
	 * @param usuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<ArchivoAdjunto> registrar(ArchivoAdjunto archivoAdjunto, String usuarioCrea, Locale locale);
	
	/**
	 * Obtener correspondencia por ID
	 * @param idCorrespondencia
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> buscarCorrespondencia(Long idCorrespondencia, Locale locale);

	/**
	 * Obtener correlativo interno
	 * @param codLugar
	 * @param codDependencia
	 * @param anio
	 * @param lugar
	 * @param dependencia
	 * @param usuario
	 * @param locale
	 * @return
	 */
	Correlativo generarCorrelativo(String codLugar, String codDependencia, Integer anio, String lugar,	String dependencia, String usuario, Locale locale);
	
	/**
	 * Listar correspondendcias x bandeja y usuario
	 * @param bandeja
	 * @param usuario
	 * @param correlativo
	 * @param asunto
	 * @param rechazados
	 * @param declinados
	 * @param fechaInicio
	 * @param fechaFin
	 * @param dorig
	 * @param tcorre
	 * @param locale
	 * @return
	 */
	//ticket 9000003866 add dorig, tcorre
	Respuesta<CorrespondenciaDTO> correspondenciasPendientes(String bandeja, String usuario, String correlativo, String asunto, boolean rechazados, boolean declinados, String fechaInicio, String fechaFin,  String dorig, String tcorre, Locale locale);

	/**
	 * Listar correspondendcias x bandeja y usuario
	 * @param bandeja
	 * @param usuario
	 * @param correlativo
	 * @param asunto
	 * @param rechazados
	 * @param declinados
	 * @param fechaInicio
	 * @param fechaFin
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaDTO> correspondencias(String bandeja, String usuario, String correlativo, String asunto, boolean rechazados, boolean declinados, String fechaInicio, String fechaFin,  Locale locale);
	
	/**
	 * Listar correspondendcias x bandeja y usuario
	 * @param bandeja
	 * @param usuario
	 * @param correlativo
	 * @param asunto
	 * @param rechazados
	 * @param misPendientes
	 * @param declinados
	 * @param fechaInicio
	 * @param fechaFin
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaDTO> correspondencias(String bandeja, String usuario, String correlativo, String asunto, boolean rechazados, boolean misPendientes, boolean declinados, String fechaInicio, String fechaFin, List<UsuarioRemplazo> userRemplazo, Locale locale);
	
	/**
	 * Obtener archivo adjunto por ID
	 * @param idArchivoAdjunto
	 * @param locale
	 * @return
	 */
	Respuesta<ArchivoAdjunto> buscarArchivoAdjunto(Long idArchivoAdjunto, Locale locale);
	
	/**
	 * Agregar firmante a correspondencia
	 * @param idCorrespondencia
	 * @param usuarioCrea
	 * @param codFirmante
	 * @param nombreFirmante
	 * @param codDependenciaFirmante
	 * @param dependenciaFirmante
	 * @param codLugarTrabajoFirmante
	 * @param lugarTrabajoFirmante
	 * @param solicitante
	 * @param nroFlujo
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> agregarFirmante(Long idCorrespondencia, String usuarioCrea, String codFirmante, String nombreFirmante, String codDependenciaFirmante, String dependenciaFirmante, String codLugarTrabajoFirmante, String lugarTrabajoFirmante, String solicitante, Integer nroFlujo, Locale locale);
	
	/**
	 * Agregar firmante a correspondencia
	 * @param idCorrespondencia
	 * @param usuarioCrea
	 * @param codFirmante
	 * @param nombreFirmante
	 * @param codDependenciaFirmante
	 * @param dependenciaFirmante
	 * @param codLugarTrabajoFirmante
	 * @param lugarTrabajoFirmante
	 * @param solicitante
	 * @param nroFlujo
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> agregarFirmanteVistosPrevios(Long idCorrespondencia, String usuarioCrea, String codFirmante, String nombreFirmante, String codDependenciaFirmante, String dependenciaFirmante, String codLugarTrabajoFirmante, String lugarTrabajoFirmante, String solicitante, Integer nroFlujo, Locale locale);
	
	/**
	 * Rechazar firma
	 * @param idCorrespondencia
	 * @param usuarioModifica
	 * @param motivoRechazo
	 * @param descripcionRechazo
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> rechazarFirma(Long idCorrespondencia, String usuarioModifica, Long motivoRechazo, String descripcionRechazo, String token, Locale locale,List<UsuarioRemplazo> usuarioRemplazo);
	
	/**
	 * Obtener lista de firmantes
	 * @param idCorrespondencia
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> obtenerFirmantes(Long idCorrespondencia, Locale locale);
	
	/**
	 * Obtener lista de firmantes
	 * @param idCorrespondencia
	 * @param nroEnvio
	 * @param locale
	 * @return
	 */
	Respuesta<DestinatarioRespuesta> obtenerDestinatarioRespuesta(Long idCorrespondencia, int nroEnvio, Locale locale);

	/**
	 * Emitir firma
	 * @param idCorrespondencia
	 * @param usuarioModifica
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> emitirFirma(Long idCorrespondencia, String usuarioModifica, Locale locale, List<UsuarioRemplazo> usuarioRemplazo);

	/**
	 * Enviar correspondencia
	 * @param idCorrespondencia
	 * @param usuarioModifica
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> enviarCorrespondencia(Long idCorrespondencia, String usuarioModifica, String token,	Locale locale, List<UsuarioRemplazo> usuarioRemplazo);
	
	
	/**
	 * Reasignar correspondencia
	 * @param idCorrespondencia
	 * @param nuevoResponsable
	 * @param usuarioModifica
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> reasignarCorrespondencia(Long idCorrespondencia, String nuevoResponsable, String usuarioModifica, Locale locale, List<UsuarioRemplazo> usuarioRemplazo);
	
	
	/**
	 * Declinar correspondencia
	 * @param idCorrespondencia
	 * @param usuarioModifica
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> declinarCorrespondencia(Long idCorrespondencia, String usuarioModifica, String token, Locale locale, List<UsuarioRemplazo> usuarioRemplazo);
	
	
	boolean validarSiPuedeAsignarFirma(Correspondencia correspondencia, String usuario);
	
	
	/**
	 * 
	 * @param codDependenciaOriginadora
	 * @param codeDependenciaRemitente
	 * @param correlativo
	 * @param asunto
	 * @param estado
	 * @param masFiltros
	 * @param nombreOriginador
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param tipoCorrespondencia
	 * @param tipoEmision
	 * @param firmaDigital
	 * @param confidencial
	 * @param urgente
	 * @param despachoFisico
	 * @param codDependenciaDestinataria
	 * @param codDependenciaCopia
	 * @param usuario
	 * @param esJefe
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondencias(int considerarOriginadora, String codDependenciaOriginadora, String codeDependenciaRemitente, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, String usuario, int esJefe, int todos, Locale locale);
	
	
	
	//Inicio 4408
	/**
	 * 
	 * @param codDependenciaOriginadora
	 * @param codeDependenciaRemitente
	 * @param correlativo
	 * @param asunto
	 * @param estado
	 * @param masFiltros
	 * @param nombreOriginador
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param tipoCorrespondencia
	 * @param tipoEmision
	 * @param firmaDigital
	 * @param confidencial
	 * @param urgente
	 * @param despachoFisico
	 * @param codDependenciaDestinataria
	 * @param codDependenciaCopia
	 * @param usuario
	 * @param esJefe
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestor(String codeDependenciaRemitente, String correlativo,  String asunto, String estado,
			String fechaDesde, String fechaHasta, Locale locale);
	//Fin 4408
	/**
	 * 
	 * @param idCorrespondecia
	 * * @return
	 */
	List<Flujo> nombreNumeroFlujos(Long idCorrespondecia);
	
	/**
	 * 
	 * @param idCorrespondecia
	 * * @param nroFlujo
	 * * @return
	 */
	List<pe.com.petroperu.model.Correspondencia> listaFlujo(Long correspondecia,Long nroFlujo);
	
	// TICKET 9000004044
	/**
	 * 
	 * @param correlativo
	 * @return
	 */
	List<Correspondencia> listarDocumentoRespuestaCorrespondencia(String correlativo);
	
	/**
	 * 
	 * @param idAsignacion
	 * @return
	 */
	List<Correspondencia> listarDocumentoRespuestaAsignacion(Long idAsignacion);
	
	/**
	 * 
	 * @param numeroDocumento
	 * @return
	 */
	List<Correspondencia> buscarCorrespondenciaPorNumeroDocumento(String numeroDocumento);
	// FIN TICKET
	
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestorPaginado(String codeDependenciaRemitente, String correlativo,  String asunto, String estado,
			String fechaDesde, String fechaHasta, String token, int length, int start, String columna, String orden, String excel,String fechaModificaDesde, String fechaModificaHasta, Locale locale);
	
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasPaginado(int considerarOriginadora, String codDependenciaOriginadora, String codeDependenciaRemitente, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, String usuario, int esJefe, int todos, 
			int start, int length, String columna, String orden, String excel,String fechaModificaDesde, String fechaModificaHasta, Locale locale);
	
	Long validarNivelFirma(String correlativos, Locale locale);
	
	/**
	 * Obtener lista de firmantes
	 * @param idCorrespondencia
	 * @param locale
	 * @return
	 */
	Respuesta<DatosFirmanteDTO> obtenerDatosFirmanteYFirmantePrevio(Long idCorrespondencia, Locale locale, String usuario);
	
	// TICKET 9000004962
	Respuesta<CorrespondenciaConsultaDTO> consultarAuditoriaCorrespondenciasPaginado(String codDependenciaOriginadora, String codeDependenciaRemitente, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, int todos, 
			int start, int length, String columna, String orden, String excel,String fechaModificaDesde, String fechaModificaHasta, Locale locale);
	
}

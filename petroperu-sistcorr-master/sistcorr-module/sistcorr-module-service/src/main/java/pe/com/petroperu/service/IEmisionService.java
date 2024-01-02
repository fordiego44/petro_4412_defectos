package pe.com.petroperu.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.emision.FiltroConsultaHistorial;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.DatosFirmante;
import pe.com.petroperu.model.emision.DestinatarioRespuesta;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaDTO;
import pe.com.petroperu.service.dto.ArchivoAdjuntoDTO;
import pe.com.petroperu.service.dto.FiltroBandeja;
import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;

public interface IEmisionService {
	
	/**
	 * Regitrar correspondencia nueva
	 * @param correspondencia
	 * @param adjuntos
	 * @param usuarioCrea
	 * @param nombreCompletoUsuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> registrar(Correspondencia correspondencia, List<ArchivoAdjuntoDTO> adjuntos, String usuarioCrea, String nombreCompletoUsuarioCrea, List<RutaAprobacion> aprobadores,  Locale locale);
	
	/**
	 * Regitar archivo adjunto
	 * @param idCorrespondencia
	 * @param adjunto
	 * @param usuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<ArchivoAdjunto> registar(Long idCorrespondencia, ArchivoAdjuntoDTO adjunto, String usuarioCrea, Locale locale);
	
	Respuesta<RespuestaCargaAdjunto> registrarArchivoAdjunto(pe.com.petroperu.model.Correspondencia correlativo, ArchivoAdjuntoDTO adjunto, UsuarioPetroperu usuario, Locale locale);
	
	/**
	 * Buscar correspondencia por ID
	 * @param idCorrespondencia
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> buscar(Long idCorrespondencia, Locale locale);
	
	
	/**
	 * Obtener bandeja de pendientes
	 * @param filtro
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaDTO> pendientes(FiltroBandeja filtro, Locale locale);
	
	/**
	 * Obtener bandeja de firmados
	 * @param filtro
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaDTO> firmados(FiltroBandeja filtro, Locale locale);
	
	/**
	 * Obtener vandeja de enviados
	 * @param filtro
	 * @param locale
	 * @return
	 */
	Respuesta<CorrespondenciaDTO> enviados(FiltroBandeja filtro, Locale locale);

	/**
	 * Obtener archivo adjunto 
	 * @param idArchivoAdjunto
	 * @param locale
	 * @return [idCorrespondencia, directorio]
	 */
	Object[] obtenerArchivAdjunto(Long idArchivoAdjunto, Locale locale);
	
	/**
	 * Asignar como firmante a remitente
	 * @param idCorrespondencia
	 * @param usuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> asignarFirmanteAutomatico(Long idCorrespondencia, String usuarioCrea, Locale locale);
	
	/**
	 * Asignar como firmante a remitente
	 * @param idCorrespondencia
	 * @param usuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> asignarFirmanteRutaAprobacion(Long idCorrespondencia, String usuarioCrea, Locale locale);
	
	/**
	 * Asignar firmante
	 * @param idCorrespondencia
	 * @param firmante
	 * @param usuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> asignarFirmante(Long idCorrespondencia, Firmante firmante, String usuarioCrea, Locale locale);
	
	/**
	 * Obtener lista de firmantes
	 * @param idCorrespondencia
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> obtenerFirmantes(Long idCorrespondencia, Locale locale);
	
	/**
	 * Obtener lista de correlativos de destinatario por envio
	 * @param idCorrespondencia
	 * @param nroEnvio
	 * @param locale
	 * @return
	 */
	Respuesta<DestinatarioRespuesta> obtenerDestinatarioRespuesta(Long idCorrespondencia, int nroEnvio, Locale locale);
	
	/**
	 * Validar firmante
	 * @param idCorrespondencia
	 * @param firmante
	 * @param usuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta validarFirmante(Long idCorrespondencia, Firmante firmante, String usuarioCrea, Locale locale);
	
	/**
	 * Rechazar soliciutd de firma
	 * @param idCorrespondencia
	 * @param firmante
	 * @param usuarioMod
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> rechazarSolicitudFirma(Long idCorrespondencia, Firmante firmante, String usuarioMod, String token, Locale locale);

	/**
	 * Modificar correspondencia
	 * @param correspondencia
	 * @param adjuntos
	 * @param usuarioCrea
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> modificar(Correspondencia correspondencia, List<ArchivoAdjuntoDTO> adjuntos, 	String usuarioCrea, List<RutaAprobacion> aprobadores, Locale locale);

	/**
	 * Firmar correspondencia
	 * @param idCorrespondencia
	 * @param usuarioFirmante
	 * @param directorioFirmados
	 * @param locale
	 * @return
	 */
	Respuesta<Firmante> firmarCorrespondencia(Long idCorrespondencia, String usuarioFirmante, String directorioFirmados, Locale locale);
	
	/**
	 * Enviar correspondencia a FILENET
	 * @param idCorrespondencia
	 * @param usuarioModifica
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> enviarCorrespondencia(Long idCorrespondencia, String usuarioModifica, String token, Locale locale);
	
	
	/**
	 * Reasignar correspondencia
	 * @param idCorrespondencia
	 * @param nuevoResponsable
	 * @param usuarioModificar
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> reasignarCorrespondencia(Long idCorrespondencia, String nuevoResponsable, String usuarioModificar, Locale locale);
	
	
	/**
	 * Declinar correspondencia
	 * @param idCorrespondencia
	 * @param usuarioModifica
	 * @param token
	 * @param locale
	 * @return
	 */
	Respuesta<Correspondencia> declinarCorrespondencia(Long idCorrespondencia, String usuarioModifica, String token, Locale locale);

	/**
	 * Lista los posibles usuario reemplazantes
	 * @param idCorrespondencia
	 * @param text
	 * @param locale
	 * @return
	 */
	Respuesta<ItemFilenet> buscarFuncionariosReemplazantes(Long idCorrespondencia, String text, Locale locale);
	
	
	/**
	 * 
	 * @param filtro
	 * @param locale
	 * @param usuario
	 * @param esJefe
	 * @return
	 */
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondencuas(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, Locale locale);

	//Inicio 4408
	/**
	 * 
	 * @param filtro
	 * @param locale
	 * @param usuario
	 * @param esJefe
	 * @return
	 */
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestor(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, Locale locale);
	//Fin 4408
	
	/**
	 * 
	 * @param filtro
	 * @param usuarioCreador
	 * @param usuario
	 * @param esJefe
	 * @param locale
	 * @return
	 */
	Respuesta<ByteArrayInputStream> consultarCorrespondenciasExcel(FiltroConsultaCorrespondenciaDTO filtro, String usuarioCreador, String usuario, int esJefe,
			Locale locale);
	
	/**
	 * 
	 * @param filtro
	 * @param usuarioCreador
	 * @param usuario
	 * @param esJefe
	 * @param locale
	 * @return
	 */
	Respuesta<ByteArrayInputStream> consultarCorrespondenciasExcelJefeGestor(FiltroConsultaCorrespondenciaDTO filtro, String usuarioCreador, String usuario, int esJefe,
			Locale locale);

	/**
	 * 
	 * @param filtro
	 * @param usuarioCreador
	 * @param locale
	 * @return
	 */
	Respuesta<ByteArrayInputStream> consultarHistorialExcel(Long idCorrespondencia, FiltroConsultaHistorial filtro, String usuarioCreador, Correspondencia corr,
			Locale locale);
	
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestorPaginado(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, String token, int length, int start, String columna, String order, String excel, Locale locale);
	
	Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasPaginado(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, int start, int length, String columna, String order, String excel, Locale locale);
	

	void actualizarRutaAprobacionJefeActual(UsuarioPetroperu usuario, int esJefe, Locale locale);

	
	
	/**
	 * registrar datos firmantes
	 * @param idCorrespondencia
	 * @param locale
	 * @return
	 */
	Respuesta<DatosFirmante> registrarDatosFirmante(Long idCorrespondencia, Locale locale);
	
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
	Respuesta<ByteArrayInputStream> exportarExcelCorrespondenciasBS(FiltroBandeja filtro, String usuarioCreador, String usuario, String bandeja,
			Locale locale);
	/*FIN Ticket 9*4807*/
	
	//Inicio 9000004962
	Respuesta<CorrespondenciaConsultaDTO> consultarAuditoriaCorrespondenciasPaginado(FiltroConsultaCorrespondenciaDTO filtro, String usuario, int esJefe, int start, int length, String columna, String order, String excel, Locale locale);
	Respuesta<ByteArrayInputStream> consultarAuditoriaCorrespondenciasExcel(FiltroConsultaCorrespondenciaDTO filtro, String usuarioCreador, String usuario, int esJefe,
			Locale locale);
	/*FIN Ticket 9000004962*/
	
	// TICKET 9000004981
	boolean eliminarCarpetaComprimidosFirmados(String directorio);
	// FIN TICKET
	
}

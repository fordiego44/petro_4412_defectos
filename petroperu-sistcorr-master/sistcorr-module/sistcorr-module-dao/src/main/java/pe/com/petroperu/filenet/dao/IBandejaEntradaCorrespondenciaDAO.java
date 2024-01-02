package pe.com.petroperu.filenet.dao;

import java.util.List;

import pe.com.petroperu.model.Correspondencia;

public interface IBandejaEntradaCorrespondenciaDAO {

	public Object[] obtenerInformacionCorrespondencia(String correlativo);
	public List<Object[]> consultaTrackingFisico(String correlativo);
	public List<Object[]> recuperarListaAcciones(String proceso);
	public String agregarAsignacionTemporal(String codigoAccion, String correlativo, String usuarioAsignado, String detalleSolicitud, String fechaLimite, String idPadre, String idUsuario);
	public List<Object[]> obtenerAsignacionesTemporales(String correlativo, String idUsuario);
	public String agregarAsignacionTemporalMasivo(String codigoAccion, String correlativo, String detalleSolicitud, String fechaLimite, String idPadre, Integer codigoDependencia, String idUsuario);
	public boolean eliminarAsignacionesTemporales(Integer idAsignacion, String idUsuario);
	public List<Object[]> consultarListaFuncionarios(Integer codigoDependencia, String codigoLugar, String cadena, String minclNoActivo);
	public List<Object[]> consultarListaFuncionariosCopiados(String idUsuario, String correlativo);
	public boolean agregarFuncionarioListaCopias(String idUsuario, String usuario, String correlativo);
	public boolean eliminarFuncionarioListaCopias(String idUsuario, String registroCopia, String correlativo);
	public List<Object[]> consultaCorresRecibidaXProce(String idUsuario, String correlativo, String fechaDesde, String fechaHasta, Integer codigoTipoCorrespondencia, Integer codigoEstado
														, String fechaDocumento, Integer codigoDependenciaDestino, Integer codigoDependenciaRemitente, String nombreDependenciaExterna
														, String asunto, String numeroDocumentoInterno, String guiaRemision, String urgente, String procedencia
														, Integer itemsPorPagina, Integer numeroPagina, String columnaOrdern, String orden, String exportarExcel);
	public List<Object[]> consultaAsignacionesv2(String idUsuario, String correlativo, String numeroDocumentoInterno, Integer codigoDependenciaAsignante, String usuarioAsignado, String codigoAccion
														, Integer codigoEstado, String fechaDesde, String fechaHasta, String vencimientoDesde, String vencimientoHasta, String urgente, 
														Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);
	public List<Object[]> consultarListaHistorialAsignaciones(String idUsuario, String correlativo);
	public List<Object[]> recuperarListaTraza(String proceso, String correlativo, String referencia);
	public List<Object[]> recuperarListaObservaciones(String proceso, String correlativo, String referencia);
	public boolean agregarObservacion(String proceso, String idUsuario, String correlativo, String observacion);
	public String agregarAsignacionGrupalTemporal(String codigoAccion, String usuarioAsignado, String detalleSolicitud, String fechaLimite, String idUsuario,String grupoCorrespondencia, String grupoIdPadre);
	public List<Object[]> obtenerAsignacionGrupalTemporales(String idUsuario,String grupoCorrelativo);
	public String validarAsignacionGrupal(String idUsuario,String grupoCorrelativo, String grupoIdPadre);
	// TICKET 9000004961
	public List<Object[]> consultaCorrespondenciaAuditoria(String idUsuario, String correlativo, String fechaDesde, String fechaHasta, Integer codigoTipoCorrespondencia, Integer codigoEstado
			, String fechaDocumento, Integer codigoDependenciaDestino, Integer codigoDependenciaRemitente, String nombreDependenciaExterna
			, String asunto, String numeroDocumentoInterno, String guiaRemision, String urgente, String procedencia
			, Integer itemsPorPagina, Integer numeroPagina, String columnaOrdern, String orden, String exportarExcel);
	// FIN TICKET
	
	public List<Correspondencia> obtenerInformacionCorrespondenciaMPV(String correlativo); // Ticket 9*4413
}

package pe.com.petroperu.sistcorr.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import pe.com.petroperu.model.emision.Correlativo;
import pe.com.petroperu.model.emision.Correspondencia;

public interface ICorrespondenciaDAO extends JpaRepository<Correspondencia, Long>{
	
	//@Query(value = "select * from fn_obtener_bandeja(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
	//List<Object[]> bandeja(String bandeja, String usuario, String correlativo, String asunto, boolean rechazados, boolean declinados, String fechaInicio, String fechaFin);
	
	@Query(value = "exec sp_obtener_bandeja ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8", nativeQuery = true)
	List<Object[]> bandeja(String bandeja, String usuario, String correlativo, String asunto, int rechazados, int declinados, String fechaInicio, String fechaFin);
	
	//inicio ticket 9000004714   
	@Query(value = "exec sp_obtener_firmante_firmantePrevio_correspondencia ?1, ?2", nativeQuery = true)
	List<Object[]> obtenerFirmanteAndFirmantePrevioCorrespondencia(Long correspondecia, String usuario);
	//fin ticket 9000004714
	
	// TICKET 9000003993
	//@Query(value = "exec sp_obtener_bandeja_pendientes ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8", nativeQuery = true)
	@Query(value = "exec sp_obtener_bandeja_pendientes_order_v3 ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10", nativeQuery = true)//TICKET 9000004714 sp_obtener_bandeja_pendientes_order POR sp_obtener_bandeja_pendientes_order_v2
	List<Object[]> bandejaPendientes(String bandeja, String usuario, String correlativo, String asunto, int rechazados, int declinados, String fechaInicio, String fechaFin, String dorig, String tcorre);//ticket 9000003866 add dorig, tcorre
	
	// TICKET 9000003993
	//@Query(value = "exec sp_obtener_bandeja_firmados ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9", nativeQuery = true)
	//@Query(value = "exec sp_obtener_bandeja_firmados_order ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9", nativeQuery = true)
	@Query(value = "exec sp_obtener_bandeja_firmados_v4 ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10", nativeQuery = true)
	List<Object[]> bandeja(String bandeja, String usuario, String correlativo, String asunto, int rechazados, int misPendientes, int declinados, String fechaInicio, String fechaFin, String userRemplazo);
	//@Query(value = "exec sp_obtener_bandeja_firmados_v3 ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9", nativeQuery = true)//TICKET 9000004714 sp_obtener_bandeja_firmados_v2 POR sp_obtener_bandeja_firmados_v3
	//@Query(value = "exec sp_obtener_bandeja_firmados_v3_1 ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9", nativeQuery = true)//TICKET 9000004714 sp_obtener_bandeja_firmados_v2 POR sp_obtener_bandeja_firmados_v3
	//List<Object[]> bandeja(String bandeja, String usuario, String correlativo, String asunto, int rechazados, int misPendientes, int declinados, String fechaInicio, String fechaFin);
	
	@Query(value = "exec sp_consultar_correspondencias ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19", nativeQuery = true)
	List<Object[]> consultar(int considerarOriginadora, String codDependenciaOriginadora, String correspondenciasRemitentes, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia);
	
	@Query(value = "exec sp_consultar_correspondencias_cantidad ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19", nativeQuery = true)
	List<Object[]> consultar_cantidad(int considerarOriginadora, String codDependenciaOriginadora, String correspondenciasRemitentes, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia);
	
	@Query(value = "exec sp_consultar_correspondencias_cantidad_jefe ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21", nativeQuery = true)
	List<Object[]> consultar_cantidad_jefe(int considerarOriginadora, String codDependenciaOriginadora, String correspondenciasRemitentes, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, String usuarioPetroperu, int esJefe);
	
	@Query(value = "exec sp_consultar_correspondencias_cantidad_jefe_act ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21", nativeQuery = true)
	List<Object[]> consultar_cantidad_jefe_act(int considerarOriginadora, String codDependenciaOriginadora, String correspondenciasRemitentes, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, String usuarioPetroperu, int esJefe);
	
	@Query(value = "exec sp_consultar_correspondencias_cantidad_jefe_act_orig ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22", nativeQuery = true)
	List<Object[]> consultar_cantidad_jefe_act_orig(int considerarOriginadora, String codDependenciaOriginadora, String correspondenciasRemitentes, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, String usuarioPetroperu, int esJefe, int todos);
	
	// Inicio 4408
	@Query(value = "exec sp_consultar_correspondencias_cantidad_jefe_act_orig_gestor ?1, ?2, ?3, ?4, ?5, ?6", nativeQuery = true)
	List<Object[]> consultar_cantidad_jefe_act_orig_gestor(String correspondenciasRemitentes, String correlativo,  String asunto, String estado,
			String fechaDesde, String fechaHasta);
	
	//Fin 4408
	Correspondencia findOneByCorrelativo(Correlativo correlativo);
	
//	  inicio ticket 9000003996   
	@Query(value = "select DISTINCT nroFlujo, id_correspondencia from firmante f  where id_correspondencia =?", nativeQuery = true)
	List<String> findNumeroFujos(Long correspondecia);
	
	@Query(value = "select * from firmante f  where id_correspondencia =? and nroFlujo = ?", nativeQuery = true)
	List<pe.com.petroperu.model.Correspondencia> listaFlujo(Long correspondecia,Long nroFlujo);
//	  fin ticket 9000003996   
	
	// TICKET 9000004044
	@Query(value = "select * from correspondencia where idAsignacion is null and estadoDocumentoRespuesta = 1 and correlativoEntrada = ?", nativeQuery = true)
	List<Correspondencia> listarDocumentoRespuestaCorrespondencia(String correlativoEntrada);
	//List<Correspondencia> findByCorrelativoEntrada(String correlativoEntrada);
	
	List<Correspondencia> findByIdAsignacion(Long idAsignacion);
	
	List<Correspondencia> findByCorrelativo(Correlativo correlativo);
	// FIN TICKET
	
	@Query(value = "exec sp_consultar_correspondencias_cantidad_jefe_act_orig_gestor_paginado_v2 ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13", nativeQuery = true)
	List<Object[]> consultar_cantidad_jefe_act_orig_gestor_paginado(String correspondenciasRemitentes, String correlativo,  String asunto, String estado,
			String fechaDesde, String fechaHasta, int start, int length, String columna, String order, String excel,String fechaModificaDesde, String fechaModificaHasta);
	
	@Query(value = "exec sp_consultar_correspondencias_cantidad_jefe_act_orig_paginado_v2 ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22, ?23, ?24, ?25, ?26, ?27, ?28, ?29", nativeQuery = true)
	List<Object[]> consultar_cantidad_jefe_act_orig_paginado(int considerarOriginadora, String codDependenciaOriginadora, String correspondenciasRemitentes, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, String usuarioPetroperu, int esJefe, int todos, 
			int length, int start, String columna, String order, String excel, String fechaModificaDesde, String fechaModificaHasta);

	@Transactional
	@Query(value = "update correspondencia set flgEnvio = ?2 , flgEnvioInicio=?3 where id = ?1" , nativeQuery = true)
	@Modifying
	void actualizaEstadoInicioEnvioCorrespondencia(Long idCorrespondencia, String flgEnvio, Date date );
	
	@Transactional
    @Modifying
	@Query(value = "update correspondencia set flgEnvio = ?2 , flgEnvioFin=?3 where id = ?1" , nativeQuery = true)
	void actualizaEstadoErrorEnvioCorrespondencia(Long idCorrespondencia, String flgEnvio, Date date );
	
	
	@Query(value = "exec sp_consultar_correspondencias_auditoria_paginado ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22, ?23, ?24, ?25, ?26", nativeQuery = true)
	List<Object[]> consultar_auditoria_correspondencia_paginado(String codDependenciaOriginadora, String correspondenciasRemitentes, String correlativo,  String asunto, String estado, int masFiltros, String codNombreOriginador,
			String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision, int firmaDigital, int confidencial, int urgente, int despachoFisico,
			String codDependenciaDestinataria, String nombreDependenciaDestinataria, String codDependenciaCopia, int todos, 
			int length, int start, String columna, String order, String excel, String fechaModificaDesde, String fechaModificaHasta);

	
}

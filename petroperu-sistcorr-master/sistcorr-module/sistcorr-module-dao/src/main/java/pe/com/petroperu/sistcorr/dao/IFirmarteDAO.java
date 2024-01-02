package pe.com.petroperu.sistcorr.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import pe.com.petroperu.model.emision.Firmante;

public interface IFirmarteDAO extends JpaRepository<Firmante, Long> {

	 @Query("from Firmante fi where fi.correspondencia.id =:correspondencia order by fi.id asc")
	List<Firmante> obtenerFirmantes(@Param("correspondencia") Long idCorrespondencia);
	 
	// INICIO TICKET 9000003791
	 @Query(value = "select * from firmante  where id_correspondencia = ? and nroFlujo = ? order by id asc", nativeQuery = true)
	 List<Firmante> obtenerFirmantesFlujo(Long idCorrespondencia,Long nroFlujo);
	// FIN TICKET 9000003791
	 
	@Query("from Firmante fi where fi.correspondencia.id =:correspondencia and fi.estado.id in (:estados) order by fi.fechaCrea desc")
	List<Firmante> obtenerFirmantesByEstado(@Param("correspondencia") Long idCorrespondencia, @Param("estados") List<Long> idEstados);
	
	// INICIO TICKET 9000004409
	//@Query(value = "select f.id_correspondencia, f.id from ruta_aprobacion r inner join firmante f on r.id_firmante = f.id where r.codDependencia in (:dependencia) and  f.id_estado = 2 and f.codFirmante <> :usuario", nativeQuery = true)
	@Query(value = "select r.codDependencia, r.id_correspondencia, f.id as idFirma, r.id as idRuta from ruta_aprobacion r inner join firmante f on r.id_firmante = f.id where r.codDependencia in (SELECT Item COLLATE Latin1_General_CI_AS FROM dbo.SplitString(:dependencia, ',')) and  f.id_estado = 2 and f.codFirmante<>:usuario and r.tipoFirmante = 1", nativeQuery = true)
	List<Object[]> obtenerIdFirmaIdCorrelativo(@Param("dependencia") String dependencia, @Param("usuario") String usuario);
		
	@Transactional
	@Modifying
	@Query(value = "update ruta_aprobacion set usuario=?2, usuarioNombre=?3, fechaModifica=?4 where id in (SELECT Item COLLATE Latin1_General_CI_AS FROM dbo.SplitString(?1, ','))" , nativeQuery = true)
	void actualizaUsuarioRutaAprobacion(String idRuta, String usuario, String nomUsuario, Date date);
		
	@Transactional
	@Modifying
	@Query(value ="update firmante set codFirmante=?2,  nombreFirmante=?3 , fechaModifica=?4 where  id in (SELECT Item COLLATE Latin1_General_CI_AS FROM dbo.SplitString(?1, ','))" , nativeQuery = true)
	void actualizaUsuarioReemplazoFirmante(String idFirmante, String usuario, String nomUsuario, Date date); 
	// FIN TICKET 9000004409

	
}

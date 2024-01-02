package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.DestinatarioRespuesta;

public interface IDestinatarioRespuestaDAO extends JpaRepository<DestinatarioRespuesta, Long>{
	
	List<DestinatarioRespuesta> findAllByCorrespondencia(Correspondencia c);
	
	@Query("from DestinatarioRespuesta dr where dr.correspondencia.id =:correspondencia and dr.nroEnvio =:nroEnvio order by dr.id asc")
	List<DestinatarioRespuesta> obtenerDestinatarioRespuesta(@Param("correspondencia") Long idCorrespondencia, @Param("nroEnvio") int NroEnvio);
	
	@Query("from DestinatarioRespuesta where correlativo = :correlativo")
	List<DestinatarioRespuesta> findAllByCorrelativo(@Param("correlativo") String correlativo);

}

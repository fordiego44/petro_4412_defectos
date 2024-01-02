package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.com.petroperu.model.emision.CorrespondenciaDestDocPagar;

public interface ICorrespondenciaDestDocPagarDAO extends JpaRepository<CorrespondenciaDestDocPagar, Long> {


	 @Query("from CorrespondenciaDestDocPagar cdp where cdp.correspondencia.id =:correspondencia order by cdp.id asc")
	List<CorrespondenciaDestDocPagar> obtenerCorrespDestDocPagar(@Param("correspondencia") Long idCorrespondencia);
}

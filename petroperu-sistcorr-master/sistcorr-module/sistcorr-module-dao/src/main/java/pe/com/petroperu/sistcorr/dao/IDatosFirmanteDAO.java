package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.com.petroperu.model.emision.DatosFirmante;

public interface IDatosFirmanteDAO extends JpaRepository<DatosFirmante, Long> {

	@Query("from DatosFirmante df where df.correspondencia.id =:correspondencia order by df.id asc")
	List<DatosFirmante> obtenerDatosFirmantes(@Param("correspondencia") Long idCorrespondencia);
}

package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.com.petroperu.model.emision.ArchivoAdjunto;

public interface IArchivoAdjuntoDAO extends JpaRepository<ArchivoAdjunto, Long> {
	
	@Query("from ArchivoAdjunto ar where ar.correspondencia.id =:correspondencia and ar.principal = true")
	List<ArchivoAdjunto> obtenerArchivosAFirmar(@Param("correspondencia") Long idCorrespondencia);
	
	@Query("from ArchivoAdjunto ar WHERE ar.correspondencia.id IN :correspondencias and ar.principal = true")
	List<ArchivoAdjunto> obtenerArchivosAFirmarGrupal(@Param("correspondencias") List<Long> correspondencias);
	
	@Query("from ArchivoAdjunto ar WHERE ar.nombreServidor IN :archivos")
	List<ArchivoAdjunto> obtenerArchivosPorNombreServidor(@Param("archivos") List<String> archivos);

	ArchivoAdjunto findOneByNombreServidor(String nombreServidor);

}

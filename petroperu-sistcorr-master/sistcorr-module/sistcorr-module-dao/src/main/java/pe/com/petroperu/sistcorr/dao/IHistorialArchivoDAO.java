package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pe.com.petroperu.model.emision.HistorialArchivo;

public interface IHistorialArchivoDAO extends JpaRepository<HistorialArchivo, Long> {
	
	@Query(value = "exec BDFIRMA.dbo.sp_consultar_historial_correspondencia ?1, ?2, ?3, ?4", nativeQuery = true)
	List<Object[]> obtenerHistorialCompartidoCorrespondencia(Long idCorrespondencia, String fechaDesde, String fechaHasta, String valor);

}

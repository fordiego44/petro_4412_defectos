package pe.com.petroperu.sistcorr.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pe.com.petroperu.model.emision.Correlativo;

public interface ICorrelativoDAO extends JpaRepository<Correlativo, Long> {

	
	//@Query(value = "select * from fn_obtener_ultimo_correlativo(?1, ?2, ?3) ", nativeQuery = true)
	//Long obtenerUltimoCorrelativo(String codLugar, String codDependencia,  Integer anio);
	
	 @Query(value = "exec sp_obtener_ultimo_correlativo ?1, ?2, ?3 ", nativeQuery = true)
	Long obtenerUltimoCorrelativo(String codLugar, String codDependencia,  Integer anio);
	 
	 Correlativo findOneByCodigo(String codigo);
}

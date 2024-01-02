package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.petroperu.model.emision.Parametro;

@Repository
public interface IParametroDAO extends JpaRepository<Parametro, Long> {

	List<Parametro> findByGrupoAndDenominacion(String grupo, String denominacion);
	
	List<Parametro> findByGrupo(String grupo);
}

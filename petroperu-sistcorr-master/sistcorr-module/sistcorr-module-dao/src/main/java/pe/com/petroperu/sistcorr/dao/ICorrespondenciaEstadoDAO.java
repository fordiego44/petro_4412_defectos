package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.petroperu.model.emision.CorrespondenciaEstado;

@Repository
public interface ICorrespondenciaEstadoDAO extends JpaRepository<CorrespondenciaEstado, Long> {
	
	CorrespondenciaEstado findOneByEstado(String estado);
	
	List<CorrespondenciaEstado> findAll();

}

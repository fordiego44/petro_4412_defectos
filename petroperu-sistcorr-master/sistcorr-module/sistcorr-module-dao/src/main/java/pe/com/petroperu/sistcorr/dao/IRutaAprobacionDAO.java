package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.RutaAprobacion;

public interface IRutaAprobacionDAO extends JpaRepository<RutaAprobacion, Long> {
	
	List<RutaAprobacion> findAllByCorrespondencia(Correspondencia c); 

}

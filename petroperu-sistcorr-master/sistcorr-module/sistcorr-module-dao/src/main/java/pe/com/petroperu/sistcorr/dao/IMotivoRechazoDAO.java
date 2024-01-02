package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.petroperu.model.emision.MotivoRechazo;

@Repository
public interface IMotivoRechazoDAO extends JpaRepository<MotivoRechazo, Long>{

	MotivoRechazo findOneByDescripcion(String descripcion);

	List<MotivoRechazo> findAll();

	// inicio ticket 9000003996
	List<MotivoRechazo> findByRechazoFirma(boolean rechazoFirma);
	// fin ticket 9000003996
	
	List<MotivoRechazo> findByRechazoResponsable(boolean rechazoFirma);
}

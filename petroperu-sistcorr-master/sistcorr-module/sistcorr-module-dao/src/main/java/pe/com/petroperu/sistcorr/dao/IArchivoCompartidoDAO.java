package pe.com.petroperu.sistcorr.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.ArchivoCompartido;

public interface IArchivoCompartidoDAO extends JpaRepository<ArchivoCompartido, Long> {
	
	ArchivoCompartido findOneByArchivo(ArchivoAdjunto archivo);

}

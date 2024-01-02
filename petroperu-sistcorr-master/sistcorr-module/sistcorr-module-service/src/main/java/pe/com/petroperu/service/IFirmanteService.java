package pe.com.petroperu.service;

import java.util.List;

import pe.com.petroperu.model.emision.Firmante;

public interface IFirmanteService {

	List<Firmante> obtenerFirmantes(Long idCorrespondencia);

	// INICIO TICKET 9000003791
	List<Firmante> obtenerFirmantesFlujo(Long idCorrespondencia, long nroFlujo);
	// FIN TICKET 9000003791
}

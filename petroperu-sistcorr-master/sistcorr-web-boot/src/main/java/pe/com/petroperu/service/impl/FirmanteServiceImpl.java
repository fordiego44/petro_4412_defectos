package pe.com.petroperu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.service.IFirmanteService;
import pe.com.petroperu.sistcorr.dao.IFirmarteDAO;

@Service
public class FirmanteServiceImpl implements IFirmanteService{
	
	@Autowired
	private IFirmarteDAO firmanteDAO;

	@Override
	public List<Firmante> obtenerFirmantes(Long idCorrespondencia) {
		List<Firmante> firmantes = firmanteDAO.obtenerFirmantes(idCorrespondencia);
		firmantes = firmantes == null ? new ArrayList<>() : firmantes;
		return firmantes;
	}

	// INICIO TICKET 9000003791
	@Override
	public List<Firmante> obtenerFirmantesFlujo(Long idCorrespondencia, long nroFlujo) {
		List<Firmante> firmantes = firmanteDAO.obtenerFirmantesFlujo(idCorrespondencia, nroFlujo);
		firmantes = firmantes == null ? new ArrayList<>() : firmantes;
		return firmantes;
	}
	// FIN TICKET 9000003791

}

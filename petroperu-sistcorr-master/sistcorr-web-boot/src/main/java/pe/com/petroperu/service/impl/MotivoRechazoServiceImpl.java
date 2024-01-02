package pe.com.petroperu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.petroperu.model.emision.MotivoRechazo;
import pe.com.petroperu.service.IMotivoRechazoService;
import pe.com.petroperu.sistcorr.dao.IMotivoRechazoDAO;

@Service
public class MotivoRechazoServiceImpl implements IMotivoRechazoService{
	
	@Autowired
	private IMotivoRechazoDAO motivoRechazoDAO;

	@Override
	public List<MotivoRechazo> listarTodos() {
		return motivoRechazoDAO.findAll();
	}

//	  inicio ticket 9000003996   
	@Override
	public List<MotivoRechazo> listarRechazoFirmado() {
		
		return motivoRechazoDAO.findByRechazoFirma(true);
	}
//	  fin ticket 9000003996 
	
	@Override 
	public List<MotivoRechazo> listarRechazoResponsable() {
		return motivoRechazoDAO.findByRechazoResponsable(true);
	}

}

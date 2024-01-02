package pe.com.petroperu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.petroperu.model.emision.TipoEmision;
import pe.com.petroperu.service.ITipoEmisionService;
import pe.com.petroperu.sistcorr.dao.ITipoEmisionDAO;

@Service
public class TipoEmisionServiceImpl implements ITipoEmisionService {
	
	@Autowired
	private ITipoEmisionDAO tipoEmisionDAO;

	@Override
	public List<TipoEmision> listarTodos() {
		return tipoEmisionDAO.findAll();
	}
	
	

}

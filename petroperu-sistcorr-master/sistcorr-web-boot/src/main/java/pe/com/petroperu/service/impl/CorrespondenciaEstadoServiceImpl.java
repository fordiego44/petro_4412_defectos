package pe.com.petroperu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.petroperu.model.emision.CorrespondenciaEstado;
import pe.com.petroperu.service.ICorrespondenciaEstadoService;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaEstadoDAO;

@Service
public class CorrespondenciaEstadoServiceImpl implements ICorrespondenciaEstadoService{
	
	
	@Autowired
	private ICorrespondenciaEstadoDAO correspondenciaEstadoDAO;

	@Override
	public List<CorrespondenciaEstado> listarTodos() {
		return correspondenciaEstadoDAO.findAll();
	}

}

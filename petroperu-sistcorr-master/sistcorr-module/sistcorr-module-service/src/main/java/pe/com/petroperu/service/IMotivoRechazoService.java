package pe.com.petroperu.service;

import java.util.List;

import pe.com.petroperu.model.emision.MotivoRechazo;

public interface IMotivoRechazoService {

	List<MotivoRechazo> listarTodos();
	
	List<MotivoRechazo> listarRechazoFirmado();
	
	List<MotivoRechazo> listarRechazoResponsable();
	
}

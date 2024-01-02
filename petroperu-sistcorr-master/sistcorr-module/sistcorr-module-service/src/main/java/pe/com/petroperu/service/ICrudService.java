package pe.com.petroperu.service;

import pe.com.petroperu.model.EntidadBase;

public interface ICrudService<T extends EntidadBase> {
	
	T crear(T objeto);
	
	T actualizar(T objeto);
	
	boolean eliminar(Long id);

}

package pe.com.petroperu.service;

import java.util.List;

import pe.com.petroperu.model.Rol;

public interface IRolService extends ICrudService<Rol>{
	
	List<Rol> listarRolPorUsuario(String userName);

}

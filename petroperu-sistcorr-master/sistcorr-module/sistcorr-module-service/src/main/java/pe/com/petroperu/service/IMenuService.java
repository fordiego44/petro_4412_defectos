package pe.com.petroperu.service;

import java.util.List;

import pe.com.petroperu.model.Menu;

public interface IMenuService extends ICrudService<Menu> {

	List<Menu> listarPorUsuario(String userName);
	
	Long obtenerContadorBandeja(String bandeja, String nombreUsuario);
}

package pe.com.petroperu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.petroperu.model.Menu;
import pe.com.petroperu.service.IMenuService;
import pe.com.petroperu.sistcorr.dao.IMenuDAO;
import pe.com.petroperu.sistcorr.dao.SistcorrDAO;

@Service
public class MenuServiceImpl implements IMenuService {
	
	@Autowired
	private IMenuDAO menuDAO;
	
	@Autowired
	private SistcorrDAO sistcorrDAO;

	@Override
	public Menu crear(Menu objeto) {
		return null;
	}

	@Override
	public Menu actualizar(Menu objeto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Menu> listarPorUsuario(String userName) {
		List<Menu> menus = new ArrayList<>();
		menuDAO.listByUsuario(userName).forEach( obj -> {
			Menu m = new Menu();
			m.setIdMenu(Long.parseLong(String.valueOf(obj[0])));
			m.setNombre(String.valueOf(obj[1]));
			m.setEnlace(obj[2] == null ? null : String.valueOf(obj[2]));
			m.setIcono(obj[3] == null ? null : String.valueOf(obj[3]));
			m.setIdMenuSuperior(obj[4] == null ? null : Long.parseLong(String.valueOf(obj[4])));
			m.setOrden(obj[5] == null ? null : Integer.parseInt(String.valueOf(obj[5])));
			m.setCantidad(null);
			m.setSubMenu(new ArrayList<>());
			if(m.getIdMenuSuperior() == null) {
				menus.add(m);
			} else {
				menus.forEach(_m -> {
					if(_m.getIdMenu().equals(m.getIdMenuSuperior())) {
						_m.getSubMenu().add(m);
					}else{
						_m.getSubMenu().forEach(_sm -> {
							if(_sm.getIdMenu().equals(m.getIdMenuSuperior())){
								_sm.getSubMenu().add(m);
							}
						});
					}
				});
			}
				
			
		});
		
		return menus;
	}

	@Override
	public Long obtenerContadorBandeja(String bandeja, String nombreUsuario) {
		return sistcorrDAO.contadorBandeja(bandeja, nombreUsuario);
	}

}

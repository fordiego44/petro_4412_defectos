package pe.com.petroperu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.petroperu.model.Rol;
import pe.com.petroperu.service.IRolService;
import pe.com.petroperu.sistcorr.dao.IRolDAO;

@Service
public class RolServiceImpl implements IRolService {
	
	@Autowired
	private IRolDAO rolDAO;

	@Override
	public Rol crear(Rol objeto) {
		return null;
	}

	@Override
	public Rol actualizar(Rol objeto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Rol> listarRolPorUsuario(String userName) {
		List<Rol> roles = new ArrayList<>();
		rolDAO.listByUsuario(userName).forEach(obj ->{
			Rol rol = new Rol();
			rol.setIdRol(Long.parseLong(String.valueOf(obj[0])));
			rol.setNombre(String.valueOf(obj[1]));
			rol.setDescripcion(String.valueOf(obj[2]));
			rol.setRolSpring(String.valueOf(obj[3]));
			roles.add(rol);
		});
		return roles;
	}

}

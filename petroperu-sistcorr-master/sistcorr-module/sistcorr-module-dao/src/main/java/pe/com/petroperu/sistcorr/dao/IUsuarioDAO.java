package pe.com.petroperu.sistcorr.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.com.petroperu.model.UsuarioPetroperu;

@Repository
public interface IUsuarioDAO  extends CrudRepository<UsuarioPetroperu, Long>{
	
	UsuarioPetroperu findOneByUsername(String username);

}

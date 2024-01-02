package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;

import pe.com.petroperu.model.UsuarioRol;

public interface IUsuarioRolDAO extends CrudRepository<UsuarioRol, Long> {

	 @Query("from UsuarioRol ur where ur.usuario.username =:usuario ")
	List<UsuarioRol> obtenerRoles(@Param("usuario") String usuario);
	
	
}

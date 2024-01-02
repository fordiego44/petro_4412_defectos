package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pe.com.petroperu.model.Rol;

public interface IRolDAO extends CrudRepository<Rol, Long> {

	Rol findOneByRolSpring(String rolSpring);
	
	/**
	 * 
	 * @param nombre
	 * @return [idrol, nombre, descripcion, rolsping]
	 */
	 @Query(value = "select distinct idrol, r.nombre, r.descripcion, r.rolspring from usuario_rol ur inner join rol r on r.idrol = ur.id_rol inner join usuario u on ur.id_usuario = u.idusuario where u.uid = :nombre order by r.idrol asc", nativeQuery = true)
	List<Object[]> listByUsuario(@Param("nombre") String nombre);

}

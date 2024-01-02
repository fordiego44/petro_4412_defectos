package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pe.com.petroperu.model.Menu;

public interface IMenuDAO extends CrudRepository<Menu, Long> {
	
	
	 @Query(value = "select DISTINCT  m.idmenu, m.nombre, m.enlace, m.icono, m.menu_superior, m.orden from menu_rol mr inner join usuario_rol ur on ur.id_rol = mr.id_rol inner join menu m on m.idmenu = mr.id_menu inner join usuario u on ur.id_usuario = u.idusuario where u.uid =:nombre order by  m.orden asc", nativeQuery = true)
	List<Object[]> listByUsuario(@Param("nombre") String nombre);
	
	
	/*@Query(value = "select * from fn_contador_bandeja(?1, ?2) ", nativeQuery = true)
	Long contadorBandeja(String bandeja, String usuario);*/
	
	 @Query(value = "exec dbo.sp_contador_bandeja ?1, ?2 ", nativeQuery = true)
	Long contadorBandeja(String bandeja, String usuario);

}

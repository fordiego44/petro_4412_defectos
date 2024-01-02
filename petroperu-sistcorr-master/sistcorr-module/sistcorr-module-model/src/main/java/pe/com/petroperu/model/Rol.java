package pe.com.petroperu.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Rol extends EntidadBase implements Serializable {
	@Id
	private Long idRol;
	private String nombre;
	private String rolSpring;
	private String descripcion;

	public Long getIdRol() {
		return this.idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRolSpring() {
		return this.rolSpring;
	}

	public void setRolSpring(String rolSpring) {
		this.rolSpring = rolSpring;
	}
}

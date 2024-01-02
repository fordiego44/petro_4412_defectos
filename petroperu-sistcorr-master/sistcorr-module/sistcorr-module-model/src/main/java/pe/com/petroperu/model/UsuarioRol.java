package pe.com.petroperu.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuario_rol")
public class UsuarioRol extends EntidadBase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuarioRol;
	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioPetroperu usuario;
	@ManyToOne
	@JoinColumn(name = "id_rol", nullable = false)
	private Rol rol;

	public Long getIdUsuarioRol() {
		return this.idUsuarioRol;
	}

	public void setIdUsuarioRol(Long idUsuarioRol) {
		this.idUsuarioRol = idUsuarioRol;
	}

	public UsuarioPetroperu getUsuario() {
		return this.usuario;
	}

	public void setUsuario(UsuarioPetroperu usuario) {
		this.usuario = usuario;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
}

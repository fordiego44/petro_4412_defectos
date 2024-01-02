package pe.com.petroperu.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuario_funcion")
public class UsuarioFuncion extends EntidadBase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuarioFuncion;
	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioPetroperu usuario;
	@ManyToOne
	@JoinColumn(name = "id_funcion", nullable = false)
	private Funcion funcion;
	@Column(nullable = false)
	private boolean habilitado;

	public Long getIdUsuarioFuncion() {
		return this.idUsuarioFuncion;
	}

	public void setIdUsuarioFuncion(Long idUsuarioFuncion) {
		this.idUsuarioFuncion = idUsuarioFuncion;
	}

	public UsuarioPetroperu getUsuario() {
		return this.usuario;
	}

	public void setUsuario(UsuarioPetroperu usuario) {
		this.usuario = usuario;
	}

	public Funcion getFuncion() {
		return this.funcion;
	}

	public void setFuncion(Funcion funcion) {
		this.funcion = funcion;
	}

	public boolean isHabilitado() {
		return this.habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
}

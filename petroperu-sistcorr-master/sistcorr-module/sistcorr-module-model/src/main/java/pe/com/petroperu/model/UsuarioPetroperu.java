package pe.com.petroperu.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "usuario")
public class UsuarioPetroperu extends EntidadBase implements Serializable {
	private static final long serialVersionUID = -4632999253204247634L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;
	private long session;
	@Column(name = "uid")
	private String username;
	@Column(name = "contrasenia")
	private String password;
	@Column(name = "habilitado")
	private boolean enabled;
	private String email;
	private String nombres;
	private String apellidos;
	@Column(columnDefinition = "text")
	private String token;
	private String nombreCompleto;
	@Transient
	private boolean bandeja;
	@Transient
	private List<Rol> roles;

	public UsuarioPetroperu() {
	}

	public UsuarioPetroperu(long session, String username, String password, boolean enabled, String email,
			String nombres, String apellidos) {
		this.session = session;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.email = email;
		this.nombres = nombres;
		this.apellidos = apellidos;
	}

	public Long getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public long getSession() {
		return this.session;
	}

	public void setSession(long session) {
		this.session = session;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNombreCompleto() {
		if (this.nombreCompleto == null)
			return this.nombres + " " + this.apellidos;
		if (this.nombreCompleto.equals(this.username)) {
			return this.nombres + " " + this.apellidos;
		}
		return this.nombreCompleto;
	}

	public boolean isBandeja() {
		return bandeja;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public void setBandeja(boolean bandeja) {
		this.bandeja = bandeja;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String toString() {
		return "UsuarioPetroperu [idUsuario=" + this.idUsuario + ", session=" + this.session + ", username="
				+ this.username + ", password=" + this.password + ", enabled=" + this.enabled + ", email=" + this.email
				+ ", nombres=" + this.nombres + ", apellidos=" + this.apellidos + ", token=" + this.token
				+ ", nombreCompleto=" + this.nombreCompleto + "]";
	}
}

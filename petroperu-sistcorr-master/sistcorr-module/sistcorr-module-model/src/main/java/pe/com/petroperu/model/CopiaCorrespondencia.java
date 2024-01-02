package pe.com.petroperu.model;

public class CopiaCorrespondencia {
	
	private String usuarioCopia;
	private String email;
	private String nombreApellidoUsuario;
	
	public String getUsuarioCopia() {
		return usuarioCopia;
	}
	
	public void setUsuarioCopia(String usuarioCopia) {
		this.usuarioCopia = usuarioCopia;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNombreApellidoUsuario() {
		return nombreApellidoUsuario;
	}
	
	public void setNombreApellidoUsuario(String nombreApellidoUsuario) {
		this.nombreApellidoUsuario = nombreApellidoUsuario;
	}

	@Override
	public String toString() {
		return "CopiaCorrespondencia [usuarioCopia=" + usuarioCopia + ", email=" + email + ", nombreApellidoUsuario="
				+ nombreApellidoUsuario + "]";
	}
	
}

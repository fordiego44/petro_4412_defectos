package pe.com.petroperu.ad.model;

import pe.com.petroperu.ad.util.RolAD;

public class Rol {
	private String descripcion;
	private String rol;
	private RolAD rolAD;

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public RolAD getRolAD() {
		return this.rolAD;
	}

	public void setRolAD(RolAD rolAD) {
		this.rolAD = rolAD;
	}

	public void generarRolAD() {
		if (this.rol == null)
			return;
		if (this.rol.equals(""))
			return;
		if (RolAD.JEFE.ROL.equals(this.rol.toUpperCase()))
			this.rolAD = RolAD.JEFE;
		if (RolAD.EJECUTOR.ROL.equals(this.rol.toUpperCase()))
			this.rolAD = RolAD.EJECUTOR;
		if (RolAD.GESTOR.ROL.equals(this.rol.toUpperCase()))
			this.rolAD = RolAD.GESTOR;
		if (RolAD.ADMINISTRADOR.ROL.equals(this.rol.toUpperCase()))
			this.rolAD = RolAD.ADMINISTRADOR;
		if (RolAD.USUARIO.ROL.equals(this.rol.toUpperCase())) {
			this.rolAD = RolAD.USUARIO;
		}
		/*9000004276 - INICIO*/
		if (RolAD.RECEPTOR.ROL.equals(this.rol.toUpperCase()))
			this.rolAD = RolAD.RECEPTOR;
		if (RolAD.P8CEADMIN.ROL.equals(this.rol.toUpperCase()))
			this.rolAD = RolAD.P8CEADMIN;
		if (RolAD.P8PEADMIN.ROL.equals(this.rol.toUpperCase()))
			this.rolAD = RolAD.P8PEADMIN;		
		/*9000004276 - FIN*/
		// TICKET 9000004961
		if(RolAD.FISCALIZADOR.ROL.equals(this.rol.toUpperCase())){
			this.rolAD = RolAD.FISCALIZADOR;
		}
		// FIN TICKET
	}

	public String toString() {
		return "Rol [descripcion=" + this.descripcion + ", rol=" + this.rol + ", rolAD="
				+ ((this.rolAD != null) ? this.rolAD.ROL : "") + " - "
				+ ((this.rolAD != null) ? this.rolAD.ROL_SEGURIDAD : "") + "]";
	}
}

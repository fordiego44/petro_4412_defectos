package pe.com.petroperu.filenet.model.administracion;

public class Jerarquia {

	private Integer id;
	private Integer codigoJerarquia;
	private String  nombreJerarquia;
	private String codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoJerarquia() {
		return codigoJerarquia;
	}
	public void setCodigoJerarquia(Integer codigoJerarquia) {
		this.codigoJerarquia = codigoJerarquia;
	}
	public String getNombreJerarquia() {
		return nombreJerarquia;
	}
	public void setNombreJerarquia(String nombreJerarquia) {
		this.nombreJerarquia = nombreJerarquia;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	
	@Override
	public String toString() {
		return "Jerarquia [id=" + id + ", codigoJerarquia=" + codigoJerarquia + ", nombreJerarquia=" + nombreJerarquia
				+ ", codigoAccion=" + codigoAccion + "]";
	}
}

package pe.com.petroperu.filenet.model.administracion;

public class Moneda {

	private Integer id;
	private String codigoMoneda;
	private String descripcion;
	private String codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigoMoneda() {
		return codigoMoneda;
	}
	public void setCodigoMoneda(String codigoMoneda) {
		this.codigoMoneda = codigoMoneda;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "Moneda [id=" + id + ", codigoMoneda=" + codigoMoneda + ", descripcion=" + descripcion
				+ ", codigoAccion=" + codigoAccion + "]";
	}

}

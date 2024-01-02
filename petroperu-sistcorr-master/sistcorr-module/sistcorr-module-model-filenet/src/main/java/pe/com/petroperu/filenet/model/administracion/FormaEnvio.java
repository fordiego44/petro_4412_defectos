package pe.com.petroperu.filenet.model.administracion;

public class FormaEnvio {

	private Integer id;
	private String codigoFormaEnvio;
	private String descripcion;
	private String codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigoFormaEnvio() {
		return codigoFormaEnvio;
	}
	public void setCodigoFormaEnvio(String codigoFormaEnvio) {
		this.codigoFormaEnvio = codigoFormaEnvio;
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
		return "FormaEnvio [id=" + id + ", codigoFormaEnvio=" + codigoFormaEnvio + ", Descripcion=" + descripcion
				+ ", codigoAccion=" + codigoAccion + "]";
	}
	
	
}

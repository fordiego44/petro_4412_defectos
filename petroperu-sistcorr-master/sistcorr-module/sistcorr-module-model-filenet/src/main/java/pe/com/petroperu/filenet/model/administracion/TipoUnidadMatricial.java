package pe.com.petroperu.filenet.model.administracion;

public class TipoUnidadMatricial {

	private Integer id;
	private String  nombre;
	private String codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "TipoUnidadMatricial [id=" + id + ", nombre=" + nombre + ", codigoAccion=" + codigoAccion + "]";
	}
}

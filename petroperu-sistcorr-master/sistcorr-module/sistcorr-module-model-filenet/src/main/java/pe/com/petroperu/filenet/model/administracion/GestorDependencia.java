package pe.com.petroperu.filenet.model.administracion;

public class GestorDependencia {

	private Integer id;
	private Integer codigoDependencia;
	private String  registro;
	private String nombreGestor;
	private String nombreDependencia;
	private String codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoDependencia() {
		return codigoDependencia;
	}
	public void setCodigoDependencia(Integer codigoDependencia) {
		this.codigoDependencia = codigoDependencia;
	}
	public String getRegistro() {
		return registro;
	}
	public void setRegistro(String registro) {
		this.registro = registro;
	}
	public String getNombreGestor() {
		return nombreGestor;
	}
	public void setNombreGestor(String nombreGestor) {
		this.nombreGestor = nombreGestor;
	}
	public String getNombreDependencia() {
		return nombreDependencia;
	}
	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "GestorDependencia [id=" + id + ", codigoDependencia=" + codigoDependencia + ", registro=" + registro
				+ ", nombreGestor=" + nombreGestor + ", nombreDependencia=" + nombreDependencia + ", codigoAccion="
				+ codigoAccion + "]";
	}
	
}

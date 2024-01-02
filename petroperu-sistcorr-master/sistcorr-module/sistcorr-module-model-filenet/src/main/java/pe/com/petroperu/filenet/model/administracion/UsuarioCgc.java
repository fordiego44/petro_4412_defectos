package pe.com.petroperu.filenet.model.administracion;

public class UsuarioCgc {
	
	private Integer id;
	private String  registro;
	private String nombreGestor;
	private String codigoCGC;
	private String nombreCGC;
	private String codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getCodigoCGC() {
		return codigoCGC;
	}
	public void setCodigoCGC(String codigoCGC) {
		this.codigoCGC = codigoCGC;
	}
	public String getNombreCGC() {
		return nombreCGC;
	}
	public void setNombreCGC(String nombreCGC) {
		this.nombreCGC = nombreCGC;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "UsuarioCGC [id=" + id + ", registro=" + registro + ", nombreGestor=" + nombreGestor + ", codigoCGC="
				+ codigoCGC + ", nombreCGC=" + nombreCGC + ", codigoAccion=" + codigoAccion + "]";
	}

}

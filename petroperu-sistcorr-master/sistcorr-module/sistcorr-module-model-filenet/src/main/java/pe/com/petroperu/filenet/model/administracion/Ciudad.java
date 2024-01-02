package pe.com.petroperu.filenet.model.administracion;

public class Ciudad {

	private Integer id;
	private Integer codigoCiudad;
	private String  nombreCiudad;
	private Integer codigoPais;
	private String  nombrePais;
	private String  codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoCiudad() {
		return codigoCiudad;
	}
	public void setCodigoCiudad(Integer codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}
	public String getNombreCiudad() {
		return nombreCiudad;
	}
	public void setNombreCiudad(String nombreCiudad) {
		this.nombreCiudad = nombreCiudad;
	}
	public Integer getCodigoPais() {
		return codigoPais;
	}
	public void setCodigoPais(Integer codigoPais) {
		this.codigoPais = codigoPais;
	}
	public String getNombrePais() {
		return nombrePais;
	}
	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "Ciudad [id=" + id + ", codigoCiudad=" + codigoCiudad + ", nombreCiudad=" + nombreCiudad
				+ ", codigoPais=" + codigoPais + ", nombrePais=" + nombrePais + ", codigoAccion=" + codigoAccion + "]";
	}
	
}

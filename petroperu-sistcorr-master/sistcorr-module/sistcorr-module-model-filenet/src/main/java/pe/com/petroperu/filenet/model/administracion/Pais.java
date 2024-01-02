package pe.com.petroperu.filenet.model.administracion;

public class Pais {
	
	private Integer id;
	private Integer codigoPais;
	private String  nombrePais;
	private String codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
		return "Pais [id=" + id + ", codigoPais=" + codigoPais + ", nombrePais=" + nombrePais + ", codigoAccion="
				+ codigoAccion + "]";
	}
	
}

package pe.com.petroperu.filenet.model.administracion;

public class Provincia {

	private Integer id;
	private Integer codigoProvincia;
	private String  nombreProvincia;
	private Integer codigoDepartamento;
	private String  nombreDepartamento;
	private String  codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoProvincia() {
		return codigoProvincia;
	}
	public void setCodigoProvincia(Integer codigoProvincia) {
		this.codigoProvincia = codigoProvincia;
	}
	public String getNombreProvincia() {
		return nombreProvincia;
	}
	public void setNombreProvincia(String nombreProvincia) {
		this.nombreProvincia = nombreProvincia;
	}
	public Integer getCodigoDepartamento() {
		return codigoDepartamento;
	}
	public void setCodigoDepartamento(Integer codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}
	public String getNombreDepartamento() {
		return nombreDepartamento;
	}
	public void setNombreDepartamento(String nombreDepartamento) {
		this.nombreDepartamento = nombreDepartamento;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "Provincia [id=" + id + ", codigoProvincia=" + codigoProvincia + ", nombreProvincia=" + nombreProvincia
				+ ", codigoDepartamento=" + codigoDepartamento + ", nombreDepartamento=" + nombreDepartamento
				+ ", codigoAccion=" + codigoAccion + "]";
	}
	
}

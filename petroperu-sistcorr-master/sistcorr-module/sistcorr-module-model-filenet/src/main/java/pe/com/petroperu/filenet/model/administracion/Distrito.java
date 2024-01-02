package pe.com.petroperu.filenet.model.administracion;

public class Distrito {


	private Integer id;
	private Integer codigoDistrito;
	private String  nombreDistrito;	
	private Integer codigoProvincia;
	private String  nombreProvincia;
	private Integer codigoDepartamento;
	private String  nombreDepartamento;
	private String  codigoPostal;
	private String  codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoDistrito() {
		return codigoDistrito;
	}
	public void setCodigoDistrito(Integer codigoDistrito) {
		this.codigoDistrito = codigoDistrito;
	}
	public String getNombreDistrito() {
		return nombreDistrito;
	}
	public void setNombreDistrito(String nombreDistrito) {
		this.nombreDistrito = nombreDistrito;
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
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	@Override
	public String toString() {
		return "Distrito [id=" + id + ", codigoDistrito=" + codigoDistrito + ", nombreDistrito=" + nombreDistrito
				+ ", codigoProvincia=" + codigoProvincia + ", nombreProvincia=" + nombreProvincia
				+ ", codigoDepartamento=" + codigoDepartamento + ", nombreDepartamento=" + nombreDepartamento
				+ ", codigoAccion=" + codigoAccion + ", codigoPostal=" + codigoPostal + "]";
	}
}

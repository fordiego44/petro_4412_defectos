package pe.com.petroperu.filenet.model.administracion;

public class LugarTrabajo {
	
	private Integer id;
	private String  codigoLugar;
	private String  nombreLugar;	
	private String  direccionLugar;
	private Integer codigoDepartamento;
	private String  nombreDepartamento;
	private Integer codigoProvincia;
	private String  nombreProvincia;
	private Integer codigoDistrito;
	private String  nombreDistrito;
	private String  codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigoLugar() {
		return codigoLugar;
	}
	public void setCodigoLugar(String codigoLugar) {
		this.codigoLugar = codigoLugar;
	}
	public String getNombreLugar() {
		return nombreLugar;
	}
	public void setNombreLugar(String nombreLugar) {
		this.nombreLugar = nombreLugar;
	}
	public String getDireccionLugar() {
		return direccionLugar;
	}
	public void setDireccionLugar(String direccionLugar) {
		this.direccionLugar = direccionLugar;
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
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "LugarTrabajo [id=" + id + ", codigoLugar=" + codigoLugar + ", nombreLugar=" + nombreLugar
				+ ", direccionLugar=" + direccionLugar + ", codigoDepartamento=" + codigoDepartamento
				+ ", nombreDepartamento=" + nombreDepartamento + ", codigoProvincia=" + codigoProvincia
				+ ", nombreProvincia=" + nombreProvincia + ", codigoDistrito=" + codigoDistrito + ", nombreDistrito="
				+ nombreDistrito + ", codigoAccion=" + codigoAccion + "]";
	}

}

package pe.com.petroperu.filenet.model.administracion;

public class ProvinciaLugarTrabajo {
	
	private Integer id;
	private String  codigoLugar;
	private String  nombreLugar;	
	private Integer codigoDepartamento;
	private String  nombreDepartamento;
	private Integer codigoProvincia;
	private String  nombreProvincia;
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
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "LugarProvincialLocal [id=" + id + ", codigoLugar=" + codigoLugar + ", nombreLugar=" + nombreLugar
				+ ", codigoDepartamento=" + codigoDepartamento + ", nombreDepartamento=" + nombreDepartamento
				+ ", codigoProvincia=" + codigoProvincia + ", nombreProvincia=" + nombreProvincia + ", codigoAccion="
				+ codigoAccion + "]";
	}

}

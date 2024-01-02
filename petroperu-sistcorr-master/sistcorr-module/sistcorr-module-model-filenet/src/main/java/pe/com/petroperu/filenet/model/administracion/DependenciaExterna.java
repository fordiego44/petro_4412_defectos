package pe.com.petroperu.filenet.model.administracion;

public class DependenciaExterna {

	private Integer id;
	private String  ruc;
	private String  email;	
	private String  direccion;
	private String  nombreDependencia;
	private Integer codigoPais;
	private String  nombrePais;
	private Integer codigoDepartamento;
	private String  nombreDepartamento;
	private Integer codigoProvincia;
	private String  nombreProvincia;
	private Integer codigoDistrito;
	private String  nombreDistrito;
	private Integer codigoCiudad;
	private String  nombreCiudad;	
	private String  codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNombreDependencia() {
		return nombreDependencia;
	}
	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
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
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "DependenciaExterna [id=" + id + ", ruc=" + ruc + ", email=" + email + ", direccion=" + direccion
				+ ", nombreDependencia=" + nombreDependencia + ", codigoPais=" + codigoPais + ", nombrePais="
				+ nombrePais + ", codigoDepartamento=" + codigoDepartamento + ", nombreDepartamento="
				+ nombreDepartamento + ", codigoProvincia=" + codigoProvincia + ", nombreProvincia=" + nombreProvincia
				+ ", codigoDistrito=" + codigoDistrito + ", nombreDistrito=" + nombreDistrito + ", codigoCiudad="
				+ codigoCiudad + ", nombreCiudad=" + nombreCiudad + ", codigoAccion=" + codigoAccion + "]";
	}
	
}

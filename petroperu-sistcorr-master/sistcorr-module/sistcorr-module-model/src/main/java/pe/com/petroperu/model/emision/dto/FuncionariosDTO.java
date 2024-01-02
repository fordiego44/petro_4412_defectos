package pe.com.petroperu.model.emision.dto;

public class FuncionariosDTO {
	
	private Integer id;
	private String registro;
	private String nombreApellidos;
	private String nombre1;
	private String nombre2;
	private String apellido1;
	private String apellido2;
	private String email;
	private Integer codigoDependencia;
	private String nombreDependencia;
	private String ficha;
	private String operaciones;
	private String tipoFuncionario;
	private String notificaciones;
	private String proceso;
	private String activo;
	private String supervisor;
	
	
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
	public String getNombreApellidos() {
		return nombreApellidos;
	}
	public void setNombreApellidos(String nombreApellidos) {
		this.nombreApellidos = nombreApellidos;
	}
	public String getNombre1() {
		return nombre1;
	}
	public void setNombre1(String nombre1) {
		this.nombre1 = nombre1;
	}
	public String getNombre2() {
		return nombre2;
	}
	public void setNombre2(String nombre2) {
		this.nombre2 = nombre2;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getCodigoDependencia() {
		return codigoDependencia;
	}
	public void setCodigoDependencia(Integer codigoDependencia) {
		this.codigoDependencia = codigoDependencia;
	}
	public String getNombreDependencia() {
		return nombreDependencia;
	}
	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
	}
	
	public String getFicha() {
		return ficha;
	}
	public void setFicha(String ficha) {
		this.ficha = ficha;
	}
	public String getOperaciones() {
		return operaciones;
	}
	public void setOperaciones(String operaciones) {
		this.operaciones = operaciones;
	}
	public String getTipoFuncionario() {
		return tipoFuncionario;
	}
	public void setTipoFuncionario(String tipoFuncionario) {
		this.tipoFuncionario = tipoFuncionario;
	}
	public String getNotificaciones() {
		return notificaciones;
	}
	public void setNotificaciones(String notificaciones) {
		this.notificaciones = notificaciones;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	
	public FuncionariosDTO() {}
  
	
	public FuncionariosDTO(Integer id, String registro, String nombreApellidos,
			String nombre1, String nombre2, String apellido1,
			String apellido2,String email,Integer codigoDependencia,String nombreDependencia,
			String ficha,String operaciones,String tipoFuncionario,String notificaciones,String proceso,
			String activo,String supervisor) {
		super();
		this.id = id;
		this.registro = registro;
		this.nombreApellidos = nombreApellidos;
		this.nombre1 = nombre1;
		this.nombre2 = nombre2;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.email = email;
		this.codigoDependencia=codigoDependencia;
		this.nombreDependencia=nombreDependencia;
		this.ficha=ficha;
		this.operaciones=operaciones;
		this.tipoFuncionario=tipoFuncionario;
		this.notificaciones=notificaciones;
		this.proceso=proceso;
		this.activo=activo;
		this.supervisor=supervisor;
	}
	
	@Override
	public String toString() {
		return "FuncionariosDTO [id=" + id + ", registro=" + registro
				+ ", nombreApellidos=" + nombreApellidos + ", nombre1="
				+ nombre1 + ", nombre2=" + nombre2 + ", apellido1=" + apellido1
				+ ", apellido2=" + apellido2 + ", email=" + email 
				+ ", codigoDependencia="+ codigoDependencia +", nombreDependencia="+ nombreDependencia 
				+ ", ficha="+ ficha +", operaciones="+operaciones 
				+ ", tipoFuncionario="+ tipoFuncionario +", notificaciones="+ notificaciones 
				+ ", proceso="+ proceso +", activo="+ activo  +", supervisor="+ supervisor
				+ "]";
	}
	

}

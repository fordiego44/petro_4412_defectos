package pe.com.petroperu.model;

public class Funcionario {
	private String nombreApellido;
	private String nombreUsuario;
	private String text;
	private String id;
	
	// TICKET 9000003514
	private String nombreApellidoUsuario;
	private String usuario;
	private String codDependencia;
	private String nombreDependencia;
	// FIN TICKET
	
	/*INI Ticket 9*4275*/
	private Integer idFuncionario;
	private String registro;
	private String primerNombre;
	private String segundoNombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private Integer dependencia;
	private String email;
	private String ficha;
	private String operacion;
	private String tipoFuncionario;
	private String notificaciones;
	private String activo;
	private String supervisor;
	private String participaProceso;
	private String codOperacion;
	private String accion;
	/*FIN Ticket 9*4275*/

	public String getText() {
		this.text = (this.nombreApellido == null) ? "" : this.nombreApellido;
		return this.text;
	}

	public Integer getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public Integer getDependencia() {
		return dependencia;
	}

	public void setDependencia(Integer dependencia) {
		this.dependencia = dependencia;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFicha() {
		return ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
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

	public String getParticipaProceso() {
		return participaProceso;
	}

	public void setParticipaProceso(String participaProceso) {
		this.participaProceso = participaProceso;
	}

	public String getCodOperacion() {
		return codOperacion;
	}

	public void setCodOperacion(String codOperacion) {
		this.codOperacion = codOperacion;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getNombreApellido() {
		return this.nombreApellido;
	}

	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
	}

	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	// TICKET 9000003514

	public String getNombreApellidoUsuario() {
		return nombreApellidoUsuario;
	}

	public void setNombreApellidoUsuario(String nombreApellidoUsuario) {
		this.nombreApellidoUsuario = nombreApellidoUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCodDependencia() {
		return codDependencia;
	}

	public void setCodDependencia(String codDependencia) {
		this.codDependencia = codDependencia;
	}

	public String getNombreDependencia() {
		return nombreDependencia;
	}

	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
	}
	
	// FIN TICKET
	
}

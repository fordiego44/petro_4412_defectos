package pe.com.petroperu.model;

public class ReemplazoAdicion {

	private Integer id;
	private String usuarioSaliente;
	private String usuarioEntrante;
	private String rol;
	private String dependencia;
	private String fechaInicio;
	private String fechFin;
	private String estado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuarioSaliente() {
		return usuarioSaliente;
	}

	public void setUsuarioSaliente(String usuarioSaliente) {
		this.usuarioSaliente = usuarioSaliente;
	}

	public String getUsuarioEntrante() {
		return usuarioEntrante;
	}

	public void setUsuarioEntrante(String usuarioEntrante) {
		this.usuarioEntrante = usuarioEntrante;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechFin() {
		return fechFin;
	}

	public void setFechFin(String fechFin) {
		this.fechFin = fechFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}

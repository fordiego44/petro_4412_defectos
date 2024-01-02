package pe.com.petroperu.model.emision.dto;

public class ReemplazoConsultaDTO {
	
	private Integer id_reemplazo;
	private String codUsuarioSaliente;
	private String codUsuarioEntrante;
	private String usuarioSaliente;
	private String usuarioEntrante;
	private String rol;
	private Integer codDependencia;
	private String dependencia;
	private String fechaInicio;
	private String fechaTermino;
	private String estado;
	private String referencia;
	private String tipoReemplazo;
	private String accion;
	private Integer total;
	private String codConfirm;

	public String getCodConfirm() {
		return codConfirm;
	}

	public void setCodConfirm(String codConfirm) {
		this.codConfirm = codConfirm;
	}

	public Integer getId_reemplazo() {
		return id_reemplazo;
	}

	public void setId_reemplazo(Integer id_reemplazo) {
		this.id_reemplazo = id_reemplazo;
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

	public String getFechaTermino() {
		return fechaTermino;
	}

	public void setFechaTermino(String fechaTermino) {
		this.fechaTermino = fechaTermino;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
    
    public ReemplazoConsultaDTO() {}
    
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Integer getCodDependencia() {
		return codDependencia;
	}

	public void setCodDependencia(Integer codDependencia) {
		this.codDependencia = codDependencia;
	}
	
	
	public String getCodUsuarioSaliente() {
		return codUsuarioSaliente;
	}

	public void setCodUsuarioSaliente(String codUsuarioSaliente) {
		this.codUsuarioSaliente = codUsuarioSaliente;
	}

	public String getCodUsuarioEntrante() {
		return codUsuarioEntrante;
	}

	public void setCodUsuarioEntrante(String codUsuarioEntrante) {
		this.codUsuarioEntrante = codUsuarioEntrante;
	}
	
	public String getTipoReemplazo() {
		return tipoReemplazo;
	}

	public void setTipoReemplazo(String tipoReemplazo) {
		this.tipoReemplazo = tipoReemplazo;
	}
	
	@Override
	public String toString() {
		return "ReemplazoConsultaDTO [id_reemplazo=" + id_reemplazo + ", codUsuarioSaliente=" + codUsuarioSaliente
				+ ", codUsuarioEntrante=" + codUsuarioEntrante + ", usuarioSaliente=" + usuarioSaliente
				+ ", usuarioEntrante=" + usuarioEntrante + ", rol=" + rol + ", codDependencia=" + codDependencia
				+ ", dependencia=" + dependencia + ", fechaInicio=" + fechaInicio + ", fechaTermino=" + fechaTermino
				+ ", estado=" + estado + ", referencia=" + referencia + ", tipoReemplazo=" + tipoReemplazo + ", accion="
				+ accion + ", total=" + total + "]";
	}
	
	public ReemplazoConsultaDTO(Integer id_reemplazo, String codUsuarioSaliente, String codUsuarioEntrante,
			String usuarioSaliente, String usuarioEntrante, String rol, Integer codDependencia, String dependencia,
			String fechaInicio, String fechaTermino, String estado, String referencia, String tipoReemplazo,
			String accion, Integer total) {
		super();
		this.id_reemplazo = id_reemplazo;
		this.codUsuarioSaliente = codUsuarioSaliente;
		this.codUsuarioEntrante = codUsuarioEntrante;
		this.usuarioSaliente = usuarioSaliente;
		this.usuarioEntrante = usuarioEntrante;
		this.rol = rol;
		this.codDependencia = codDependencia;
		this.dependencia = dependencia;
		this.fechaInicio = fechaInicio;
		this.fechaTermino = fechaTermino;
		this.estado = estado;
		this.referencia = referencia;
		this.tipoReemplazo = tipoReemplazo;
		this.accion = accion;
		this.total = total;
	}

	
}

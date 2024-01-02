package pe.com.petroperu.model;

import java.sql.Date;

public class Reemplazos {
	private Integer idReemplazos;
	
	private String usuarioSaliente;
	private String usuarioEntrante;
	private Date fechaInicio;
	private Date fechaFin;
	private String txtfechaInicio;
	private String txtFechaFin;
	private Integer estado;
	private String tipoReemplazo;
	private Integer codDependencia;
	private String dependencia;
	private String rol;
	private String referencia;
	
	public Integer getIdReemplazos() {
		return idReemplazos;
	}
	public void setIdReemplazos(Integer idReemplazos) {
		this.idReemplazos = idReemplazos;
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
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String getTipoReemplazo() {
		return tipoReemplazo;
	}
	public void setTipoReemplazo(String tipoReemplazo) {
		this.tipoReemplazo = tipoReemplazo;
	}
	public Integer getCodDependencia() {
		return codDependencia;
	}
	public void setCodDependencia(Integer codDependencia) {
		this.codDependencia = codDependencia;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getTxtfechaInicio() {
		return txtfechaInicio;
	}
	public void setTxtfechaInicio(String txtfechaInicio) {
		this.txtfechaInicio = txtfechaInicio;
	}
	public String getTxtFechaFin() {
		return txtFechaFin;
	}
	public void setTxtFechaFin(String txtFechaFin) {
		this.txtFechaFin = txtFechaFin;
	}
	public String getDependencia() {
		return dependencia;
	}
	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}
	
	@Override
	public String toString() {
		return "Reemplazos [idReemplazos=" + idReemplazos + ", usuarioSaliente=" + usuarioSaliente
				+ ", usuarioEntrante=" + usuarioEntrante + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
				+ ", txtfechaInicio=" + txtfechaInicio + ", txtFechaFin=" + txtFechaFin + ", estado=" + estado
				+ ", tipoReemplazo=" + tipoReemplazo + ", codDependencia=" + codDependencia + ", dependencia="
				+ dependencia + ", rol=" + rol + ", referencia=" + referencia + "]";
	}
	
	
	
}

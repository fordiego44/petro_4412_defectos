package pe.com.petroperu.service.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class FiltroConsultaReemplazoRolesAnterioresDTO {
	
	private boolean considerarOriginadora;
	private String codDependenciaOriginadora;
	private String codDependenciaRemitente;
	private String codDependenciaDestinatario;
	private String correlativo;
	private String asunto;
	private String estado;
	private boolean masFiltros = false;
	private String codNombreOriginador;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaDesde;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaHasta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fecha;
	private Integer tipoCorrespondencia;
	private Integer tipoEmision;
	private int firmaDigital;
	private int confidencial;
	private int urgente;
	private int despachoFisico;
	private String codDestinatario;
	private String nombreDestinatario;
	private String codCopia;
	
	private String entidadExterna;
	private String estadoRolesAnteriores;
	private String fechaText;
	private String nroDocumento;
	private String rol;

	private String fechaDesdeText;
	
	private String fechaHastaText;
	
	private boolean todos = false;
	
	
	public String getFechaText() {
		if(fecha == null)
			return "";
		fechaText = new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(fecha);
		return fechaText;
	}
	public void setFechaText(String fechaText) {
		this.fechaText = fechaText;
	}
	public String getCodDependenciaDestinatario() {
		return codDependenciaDestinatario;
	}
	public void setCodDependenciaDestinatario(String codDependenciaDestinatario) {
		this.codDependenciaDestinatario = codDependenciaDestinatario;
	}
	public String getCodDependenciaOriginadora() {
		codDependenciaOriginadora = codDependenciaOriginadora == null ? "" : codDependenciaOriginadora;
		return codDependenciaOriginadora;
	}
	public void setCodDependenciaOriginadora(String codDependenciaOriginadora) {
		this.codDependenciaOriginadora = codDependenciaOriginadora;
	}
	public String getCodDependenciaRemitente() {
		codDependenciaRemitente = codDependenciaRemitente == null ? "" : codDependenciaRemitente;
		return codDependenciaRemitente;
	}
	public void setCodDependenciaRemitente(String codDependenciaRemitente) {
		this.codDependenciaRemitente = codDependenciaRemitente;
	}
	public String getCorrelativo() {
		correlativo = correlativo == null ? "" : correlativo;
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getAsunto() {
		asunto = asunto == null ? "" : asunto;
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	public String getEstado() {
		estado = estado == null ? "" : estado;
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setFechaDesdeText(String fechaDesdeText) {
		this.fechaDesdeText = fechaDesdeText;
	}
	public void setFechaHastaText(String fechaHastaText) {
		this.fechaHastaText = fechaHastaText;
	}
	public boolean isMasFiltros() {
		return masFiltros;
	}
	public void setMasFiltros(boolean masFiltros) {
		this.masFiltros = masFiltros;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public Integer getTipoCorrespondencia() {
		tipoCorrespondencia = tipoCorrespondencia == null ? 0 : tipoCorrespondencia;
		return tipoCorrespondencia;
	}
	public void setTipoCorrespondencia(Integer tipoCorrespondencia) {
		this.tipoCorrespondencia = tipoCorrespondencia;
	}
	public Integer getTipoEmision() {
		tipoEmision = tipoEmision == null ? 0 : tipoEmision;
		return tipoEmision;
	}
	public void setTipoEmision(Integer tipoEmision) {
		this.tipoEmision = tipoEmision;
	}
	
	public int getFirmaDigital() {
		firmaDigital = firmaDigital > 2 ? 2 : firmaDigital;
		return firmaDigital;
	}
	public void setFirmaDigital(int firmaDigital) {
		this.firmaDigital = firmaDigital;
	}
	public int getConfidencial() {
		confidencial = confidencial > 2 ? 2 : confidencial;
		return confidencial;
	}
	public void setConfidencial(int confidencial) {
		this.confidencial = confidencial;
	}
	public int getUrgente() {
		urgente = urgente > 2 ? 2 : urgente;
		return urgente;
	}
	public void setUrgente(int urgente) {
		this.urgente = urgente;
	}
	public int getDespachoFisico() {
		//despachoFisico = despachoFisico > 2 ? 2 : despachoFisico;
		despachoFisico = ((despachoFisico == 0 || despachoFisico == 1 || despachoFisico == 3) ? despachoFisico : 2);
		return despachoFisico;
	}
	public void setDespachoFisico(int despachoFisico) {
		this.despachoFisico = despachoFisico;
	}
	public String getCodDestinatario() {
		codDestinatario = codDestinatario == null ? "" : codDestinatario;
		return codDestinatario;
	}
	public void setCodDestinatario(String codDestinatario) {
		this.codDestinatario = codDestinatario;
	}
	public String getCodCopia() {
		return codCopia = codCopia == null ? "" : codCopia;
	}
	public void setCodCopia(String codCopia) {
		this.codCopia = codCopia;
	}
	
	
	public String getFechaDesdeText() {
		if(fechaDesde == null)
			return "";
		fechaDesdeText = new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(fechaDesde);
		return fechaDesdeText;
	}
	public String getFechaHastaText() {
		if(fechaHasta == null)
			return "";
		fechaHastaText = new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(fechaHasta);
		return fechaHastaText;
	}
	
	public String getCodNombreOriginador() {
		return codNombreOriginador = codNombreOriginador == null ? "" : codNombreOriginador;
	}
	public void setCodNombreOriginador(String codNombreOriginador) {
		this.codNombreOriginador = codNombreOriginador;
	}
	public String getNombreDestinatario() {
		return nombreDestinatario = nombreDestinatario ==  null ? "" : nombreDestinatario;
	}
	public void setNombreDestinatario(String nombreDestinatario) {
		this.nombreDestinatario = nombreDestinatario;
	}
	
	
	public String getEntidadExterna() {
		return entidadExterna;
	}
	public void setEntidadExterna(String entidadExterna) {
		this.entidadExterna = entidadExterna;
	}
	public String getEstadoRolesAnteriores() {
		return estadoRolesAnteriores;
	}
	public void setEstadoRolesAnteriores(String estadoRolesAnteriores) {
		this.estadoRolesAnteriores = estadoRolesAnteriores;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public boolean isConsiderarOriginadora() {
		return considerarOriginadora;
	}
	public void setConsiderarOriginadora(boolean considerarOriginadora) {
		this.considerarOriginadora = considerarOriginadora;
	}
	@Override
	public String toString() {
		return "FiltroConsultaCorrespondenciaDTO [considerarOriginadora=" + considerarOriginadora
				+ ", codDependenciaOriginadora=" + codDependenciaOriginadora + ", codDependenciaRemitente="
				+ codDependenciaRemitente + ", correlativo=" + correlativo + ", asunto=" + asunto + ", estado=" + estado
				+ ", masFiltros=" + masFiltros + ", codNombreOriginador=" + codNombreOriginador + ", fechaDesde="
				+ fechaDesde + ", fechaHasta=" + fechaHasta + ", tipoCorrespondencia=" + tipoCorrespondencia
				+ ", tipoEmision=" + tipoEmision + ", firmaDigital=" + firmaDigital + ", confidencial=" + confidencial
				+ ", urgente=" + urgente + ", despachoFisico=" + despachoFisico + ", codDestinatario=" + codDestinatario
				+ ", nombreDestinatario=" + nombreDestinatario + ", codCopia=" + codCopia + ", fechaDesdeText="
				+ fechaDesdeText + ", fechaHastaText=" + fechaHastaText 
				+ ", entidadExterna=" + entidadExterna
				+", estadoRolesAnteriores=" + estadoRolesAnteriores
				+", fecha=" + fecha
				+", nroDocumento=" + nroDocumento
				+", rol=" + rol
				+ "]";
	}
	public boolean isTodos() {
		return todos;
	}
	public void setTodos(boolean todos) {
		this.todos = todos;
	}
	
	
	
}

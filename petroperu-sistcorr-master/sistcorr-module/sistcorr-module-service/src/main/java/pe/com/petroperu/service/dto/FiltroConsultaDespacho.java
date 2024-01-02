package pe.com.petroperu.service.dto;

public class FiltroConsultaDespacho {
	
	private String nroCorrelativo;
	private String codEstado;
	private String fechaDesde;
	private String fechaHasta;
	private String dependenciaRemitente;
	private String usuarioRemitente;
	private String numeroDocumento;
	private String entidadExterna;
	private String asunto;
	private String guiaRemision;
	public String getNroCorrelativo() {
		return nroCorrelativo;
	}
	public void setNroCorrelativo(String nroCorrelativo) {
		this.nroCorrelativo = nroCorrelativo;
	}
	public String getCodEstado() {
		return codEstado;
	}
	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getDependenciaRemitente() {
		return dependenciaRemitente;
	}
	public void setDependenciaRemitente(String dependenciaRemitente) {
		this.dependenciaRemitente = dependenciaRemitente;
	}
	public String getUsuarioRemitente() {
		return usuarioRemitente;
	}
	public void setUsuarioRemitente(String usuarioRemitente) {
		this.usuarioRemitente = usuarioRemitente;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getEntidadExterna() {
		return entidadExterna;
	}
	public void setEntidadExterna(String entidadExterna) {
		this.entidadExterna = entidadExterna;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getGuiaRemision() {
		return guiaRemision;
	}
	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}
	@Override
	public String toString() {
		return "FiltroConsultaDespacho [nroCorrelativo=" + nroCorrelativo + ", codEstado=" + codEstado + ", fechaDesde="
				+ fechaDesde + ", fechaHasta=" + fechaHasta + ", dependenciaRemitente=" + dependenciaRemitente
				+ ", usuarioRemitente=" + usuarioRemitente + ", numeroDocumento=" + numeroDocumento
				+ ", entidadExterna=" + entidadExterna + ", asunto=" + asunto + ", guiaRemision=" + guiaRemision + "]";
	}
	
	
	
	
}

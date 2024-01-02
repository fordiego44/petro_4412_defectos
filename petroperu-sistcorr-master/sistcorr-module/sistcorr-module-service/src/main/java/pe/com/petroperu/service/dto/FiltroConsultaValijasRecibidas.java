package pe.com.petroperu.service.dto;
/*Ticket 9000004412*/
public class FiltroConsultaValijasRecibidas {
	
	private String nroCorrelativo;
	private String codEstado;
	private String fechaDesde;
	private String fechaHasta;
	private String cgcRemitente;
	private String cgcReceptor;
	private String courier;
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
	public String getCgcRemitente() {
		return cgcRemitente;
	}
	public void setCgcRemitente(String cgcRemitente) {
		this.cgcRemitente = cgcRemitente;
	}
	public String getCgcReceptor() {
		return cgcReceptor;
	}
	public void setCgcReceptor(String cgcReceptor) {
		this.cgcReceptor = cgcReceptor;
	}
	public String getCourier() {
		return courier;
	}
	public void setCourier(String courier) {
		this.courier = courier;
	}
	@Override
	public String toString() {
		return "FiltroValijasRecibidas [nroCorrelativo=" + nroCorrelativo + ", codEstado=" + codEstado + ", fechaDesde="
				+ fechaDesde + ", fechaHasta=" + fechaHasta + ", cgcRemitente=" + cgcRemitente + ", cgcReceptor="
				+ cgcReceptor + ", courier=" + courier + "]";
	}
	
	
	
	

}

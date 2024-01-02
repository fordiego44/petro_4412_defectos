package pe.com.petroperu.model.emision.dto;

public class ValijasRecibidasDTO {

	private int idValijasRecibidas;
	private String correlativo;
	private String fecha;
	private String cgcRecibe;
	private String cgcRemitente;
	private String courier;
	private String guia;
	private String estado;
	private String cantidad;
	public int getIdValijasRecibidas() {
		return idValijasRecibidas;
	}
	public void setIdValijasRecibidas(int idValijasRecibidas) {
		this.idValijasRecibidas = idValijasRecibidas;
	}
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getCgcRecibe() {
		return cgcRecibe;
	}
	public void setCgcRecibe(String cgcRecibe) {
		this.cgcRecibe = cgcRecibe;
	}
	public String getCgcRemitente() {
		return cgcRemitente;
	}
	public void setCgcRemitente(String cgcRemitente) {
		this.cgcRemitente = cgcRemitente;
	}
	public String getCourier() {
		return courier;
	}
	public void setCourier(String courier) {
		this.courier = courier;
	}
	public String getGuia() {
		return guia;
	}
	public void setGuia(String guia) {
		this.guia = guia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	@Override
	public String toString() {
		return "ValijasRecibidasDTO [idValijasRecibidas=" + idValijasRecibidas + ", correlativo=" + correlativo
				+ ", fecha=" + fecha + ", cgcRecibe=" + cgcRecibe + ", cgcRemitente=" + cgcRemitente + ", courier="
				+ courier + ", guia=" + guia + ", estado=" + estado + ", cantidad=" + cantidad + "]";
	}
	public ValijasRecibidasDTO(int idValijasRecibidas, String correlativo, String fecha, String cgcRecibe,
			String cgcRemitente, String courier, String guia, String estado, String cantidad) {
		super();
		this.idValijasRecibidas = idValijasRecibidas;
		this.correlativo = correlativo;
		this.fecha = fecha;
		this.cgcRecibe = cgcRecibe;
		this.cgcRemitente = cgcRemitente;
		this.courier = courier;
		this.guia = guia;
		this.estado = estado;
		this.cantidad = cantidad;
	}
	public ValijasRecibidasDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}

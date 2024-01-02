package pe.com.petroperu.model.emision.dto;

public class CorrespondenciaCompartidaDTO {
	
	private String fecha;
	private String hora;
	private String modoCompartido;
	private String compartidoPor;
	private String destinatarios;
	private String copias;
	private String asunto;
	private String contenido;
	private String archivos;
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getHora() {
		return hora;
	}
	
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	public String getModoCompartido() {
		return modoCompartido;
	}
	
	public void setModoCompartido(String modoCompartido) {
		this.modoCompartido = modoCompartido;
	}
	
	public String getCompartidoPor() {
		return compartidoPor;
	}
	
	public void setCompartidoPor(String compartidoPor) {
		this.compartidoPor = compartidoPor;
	}
	
	public String getDestinatarios() {
		return destinatarios;
	}
	
	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}
	
	public String getCopias() {
		return copias;
	}
	
	public void setCopias(String copias) {
		this.copias = copias;
	}
	
	public String getAsunto() {
		return asunto;
	}
	
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	public String getContenido() {
		return contenido;
	}
	
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	public String getArchivos() {
		return archivos;
	}
	
	public void setArchivos(String archivos) {
		this.archivos = archivos;
	}
	
}

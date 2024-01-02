package pe.com.petroperu.notificacion;

import java.util.HashMap;
import java.util.Map;

public class MailHistorialDireccion {
	
	private String from;
	private String[] to;
	private String[] copias;
	private String url;
	private String subject;
	private Map<String, Object> model = new HashMap<>();
	private String responsable;
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String[] getTo() {
		return to;
	}
	
	public void setTo(String[] to) {
		this.to = to;
	}
	
	public String[] getCopias() {
		return copias;
	}
	
	public void setCopias(String[] copias) {
		this.copias = copias;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public Map<String, Object> getModel() {
		return model;
	}
	
	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
}

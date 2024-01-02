package pe.com.petroperu.notificacion;

import java.util.HashMap;
import java.util.Map;

public class Mail {
	private String from;
	private String to;
	private String[] copies;
	private String subject;
	private String htmlBody;//TICKET 9000004411
	private String archivosDirStr;//TICKET 9000004411
	private String nombreArcStr;//TICKET 9000004411
	
	private Map<String, Object> model = new HashMap<>();

	public String getNombreArcStr() {
		return nombreArcStr;
	}

	public void setNombreArcStr(String nombreArcStr) {
		this.nombreArcStr = nombreArcStr;
	}

	public String getArchivosDirStr() {
		return archivosDirStr;
	}

	public void setArchivosDirStr(String archivosDirStr) {
		this.archivosDirStr = archivosDirStr;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Object> getModel() {
		return this.model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public String[] getCopies() {
		return this.copies;
	}

	public void setCopies(String[] copies) {
		this.copies = copies;
	}
}

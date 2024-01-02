package pe.com.petroperu.cliente.model;


public class CompletarCorrespondenciaParametro
{
  private String respuesta;
  private String usuarioResponsable;
  private String documentoRespuesta;
  
  public CompletarCorrespondenciaParametro() {}
  
  public CompletarCorrespondenciaParametro(String respuesta) { this.respuesta = respuesta; }


  
  public CompletarCorrespondenciaParametro(String respuesta, String usuarioResponsable, String documentoRespuesta) {
	this.respuesta = respuesta;
	this.usuarioResponsable = usuarioResponsable;
	this.documentoRespuesta = documentoRespuesta;
  }

public String getRespuesta() { return this.respuesta; }


  
  public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

	public String getUsuarioResponsable() {
		return usuarioResponsable;
	}
	
	public void setUsuarioResponsable(String usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}
	
	public String getDocumentoRespuesta() {
		return documentoRespuesta;
	}
	
	public void setDocumentoRespuesta(String documentoRespuesta) {
		this.documentoRespuesta = documentoRespuesta;
	}

	@Override
	public String toString() {
		return "CompletarCorrespondenciaParametro [respuesta=" + respuesta + ", usuarioResponsable="
				+ usuarioResponsable + ", documentoRespuesta=" + documentoRespuesta + "]";
	}
  
  
}

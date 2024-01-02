package pe.com.petroperu.cliente.model;

import java.io.Serializable;

public class InformacionDocumento
  implements Serializable
{
  private String id;
  private boolean esPrincipal;
  private String documentTitle;
  private String tamano;
  private String mimeType;
  
  public String getId() { return this.id; }

  
  public void setId(String id) { this.id = id; }

  
  public boolean isEsPrincipal() { return this.esPrincipal; }

  
  public void setEsPrincipal(boolean esPrincipal) { this.esPrincipal = esPrincipal; }

  
  public String getDocumentTitle() { return this.documentTitle; }

  
  public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }

  
  public String getTamano() { return this.tamano; }

  
  public void setTamano(String tamano) { this.tamano = tamano; }

  
  public String getMimeType() { return this.mimeType; }

  
  public void setMimeType(String mimeType) { this.mimeType = mimeType; }
}

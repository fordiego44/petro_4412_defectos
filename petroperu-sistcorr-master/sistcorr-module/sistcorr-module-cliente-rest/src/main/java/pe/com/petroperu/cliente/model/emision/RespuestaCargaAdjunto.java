package pe.com.petroperu.cliente.model.emision;


public class RespuestaCargaAdjunto
{
  private String documentTitle;
  private Integer tamano;
  private String mimeType;
  private String id;
  
  public String getDocumentTitle() { return this.documentTitle; }

  
  public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }

  
  public Integer getTamano() { return this.tamano; }

  
  public void setTamano(Integer tamano) { this.tamano = tamano; }

  
  public String getMimeType() { return this.mimeType; }

  
  public void setMimeType(String mimeType) { this.mimeType = mimeType; }

  
  public String getId() { return this.id; }

  
  public void setId(String id) { this.id = id; }
}

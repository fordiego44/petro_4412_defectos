package pe.com.petroperu.cliente.model.emision;

public class DestinoRespuesta
{
  private String correlativo;
  private Long idDependencia;
  private String dependencia;
  
  public String getCorrelativo() { return this.correlativo; }

  
  public void setCorrelativo(String correlativo) { this.correlativo = correlativo; }

  
  public Long getIdDependencia() { return this.idDependencia; }

  
  public void setIdDependencia(Long idDependencia) { this.idDependencia = idDependencia; }

  
  public String getDependencia() { return this.dependencia; }

  
  public void setDependencia(String dependencia) { this.dependencia = dependencia; }


  
  public String toString() { return "DestinoRespuesta [correlativo=" + this.correlativo + ", idDependencia=" + this.idDependencia + ", dependencia=" + this.dependencia + "]"; }
}

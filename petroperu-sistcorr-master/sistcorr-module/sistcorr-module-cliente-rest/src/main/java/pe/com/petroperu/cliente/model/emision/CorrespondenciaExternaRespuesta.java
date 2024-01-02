package pe.com.petroperu.cliente.model.emision;

import java.util.List;

public class CorrespondenciaExternaRespuesta
{
  private String correlativoCE;
  private String entidadExterna;
  private List<DestinoRespuesta> destinos;
  
  public String getCorrelativoCE() { return this.correlativoCE; }

  
  public void setCorrelativoCE(String correlativoCE) { this.correlativoCE = correlativoCE; }

  
  public String getEntidadExterna() { return this.entidadExterna; }

  
  public void setEntidadExterna(String entidadExterna) { this.entidadExterna = entidadExterna; }

  
  public List<DestinoRespuesta> getDestinos() { return this.destinos; }

  
  public void setDestinos(List<DestinoRespuesta> destinos) { this.destinos = destinos; }


  
  public String toString() { return "CorrespondenciaExternaRespuesta [correlativoCE=" + this.correlativoCE + ", entidadExterna=" + this.entidadExterna + ", destinos=" + this.destinos + "]"; }
}

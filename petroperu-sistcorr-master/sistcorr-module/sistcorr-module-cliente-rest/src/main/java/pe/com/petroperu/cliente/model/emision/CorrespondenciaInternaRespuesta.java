package pe.com.petroperu.cliente.model.emision;

import java.util.List;

public class CorrespondenciaInternaRespuesta
{
  private String correlativoCE;
  private List<DestinoRespuesta> destinos;
  
  public String getCorrelativoCE() { return this.correlativoCE; }

  
  public void setCorrelativoCE(String correlativoCE) { this.correlativoCE = correlativoCE; }

  
  public List<DestinoRespuesta> getDestinos() { return this.destinos; }

  
  public void setDestinos(List<DestinoRespuesta> destinos) { this.destinos = destinos; }



  
  public String toString() { return "CorrespondenciaInternaRespuesta [correlativoCE=" + this.correlativoCE + ", destinos=" + this.destinos + "]"; }
}

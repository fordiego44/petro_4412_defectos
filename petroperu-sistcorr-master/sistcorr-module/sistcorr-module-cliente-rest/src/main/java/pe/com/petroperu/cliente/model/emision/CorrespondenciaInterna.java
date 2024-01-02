package pe.com.petroperu.cliente.model.emision;

import java.util.ArrayList;
import java.util.List;


public class CorrespondenciaInterna
{
  public String tipoCE;
  public Long idDependenciaRemitente;
  public String confidencial;
  public String urgente;
  public String fechaDocumento;
  public String numDocInterno;
  public Long idTipoCorrespondencia;
  public String asunto;
  public String observacion;
  public String tipoCorrespondenciaOtro;
  public List<Destinatario> destinatarios = new ArrayList<>();
  
  public List<ConCopia> conCopia = new ArrayList<>();

  
  public String getTipoCE() { return this.tipoCE; }


  
  public void setTipoCE(String tipoCE) { this.tipoCE = tipoCE; }


  
  public Long getIdDependenciaRemitente() { return this.idDependenciaRemitente; }


  
  public void setIdDependenciaRemitente(Long idDependenciaRemitente) { this.idDependenciaRemitente = idDependenciaRemitente; }


  
  public String getConfidencial() { return this.confidencial; }


  
  public void setConfidencial(String confidencial) { this.confidencial = confidencial; }


  
  public String getUrgente() { return this.urgente; }


  
  public void setUrgente(String urgente) { this.urgente = urgente; }


  
  public String getFechaDocumento() { return this.fechaDocumento; }


  
  public void setFechaDocumento(String fechaDocumento) { this.fechaDocumento = fechaDocumento; }


  
  public String getNumDocInterno() { return this.numDocInterno; }


  
  public void setNumDocInterno(String numDocInterno) { this.numDocInterno = numDocInterno; }


  
  public Long getIdTipoCorrespondencia() { return this.idTipoCorrespondencia; }


  
  public void setIdTipoCorrespondencia(Long idTipoCorrespondencia) { this.idTipoCorrespondencia = idTipoCorrespondencia; }


  
  public String getAsunto() { return this.asunto; }


  
  public void setAsunto(String asunto) { this.asunto = asunto; }


  
  public String getObservacion() { return this.observacion; }


  
  public void setObservacion(String observacion) { this.observacion = observacion; }


  
  public List<Destinatario> getDestinatarios() { return this.destinatarios; }


  
  public void setDestinatarios(List<Destinatario> destinatarios) { this.destinatarios = destinatarios; }


  
  public List<ConCopia> getConCopia() { return this.conCopia; }


  
  public void setConCopia(List<ConCopia> conCopia) { this.conCopia = conCopia; }


  
  public String getTipoCorrespondenciaOtro() { return this.tipoCorrespondenciaOtro; }


  
  public void setTipoCorrespondenciaOtro(String tipoCorrespondenciaOtro) { this.tipoCorrespondenciaOtro = tipoCorrespondenciaOtro; }



  
  public String toString() { return "CorrespondenciaInterna [tipoCE=" + this.tipoCE + ", idDependenciaRemitente=" + this.idDependenciaRemitente + ", confidencial=" + this.confidencial + ", urgente=" + this.urgente + ", fechaDocumento=" + this.fechaDocumento + ", numDocInterno=" + this.numDocInterno + ", idTipoCorrespondencia=" + this.idTipoCorrespondencia + ", asunto=" + this.asunto + ", observacion=" + this.observacion + ", tipoCorrespondenciaOtro=" + this.tipoCorrespondenciaOtro + ", destinatarios=" + this.destinatarios + ", conCopia=" + this.conCopia + "]"; }
}

package pe.com.petroperu.cliente.model.emision;

import java.util.ArrayList;
import java.util.List;


public class CorrespondenciaExterna
{
  public String tipoCE;
  public String entidadExterna;
  public String personaDest;
  public String direccion;
  public String idPais;
  public String ciudad;
  public Long idProvincia;
  public Long idDistrito;
  public Long idDepartamento;
  public Long idDependenciaRemitente;
  public Long idTipoCorrespondencia;
  public String tipoCorrespondenciaOtro;
  public String numDocInterno;
  public String fechaDocumento;
  public String retornoCargo;
  public String urgente;
  public String confidencial;
  public String asunto;
  public String observacion;
  public String despachoFisico;//TICKET 9000003934
  public List<ConCopia> conCopia = new ArrayList<>();
  public List<Destinatario> destinatarios = new ArrayList<>();

  /* INICIO TICKET 9000003934*/
	
	public String getTipoCE() {
		return tipoCE;
	}

	public String getDespachoFisico() {
		return despachoFisico;
	}

	public void setDespachoFisico(String despachoFisico) {
		this.despachoFisico = despachoFisico;
	}

	public void setTipoCE(String tipoCE) {
		this.tipoCE = tipoCE;
	}

	public String getEntidadExterna() {
		return entidadExterna;
	}

	public void setEntidadExterna(String entidadExterna) {
		this.entidadExterna = entidadExterna;
	}

	public String getPersonaDest() {
		return personaDest;
	}

	public void setPersonaDest(String personaDest) {
		this.personaDest = personaDest;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getIdPais() {
		return idPais;
	}

	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Long getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(Long idProvincia) {
		this.idProvincia = idProvincia;
	}

	public Long getIdDistrito() {
		return idDistrito;
	}

	public void setIdDistrito(Long idDistrito) {
		this.idDistrito = idDistrito;
	}

	public Long getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(Long idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public Long getIdDependenciaRemitente() {
		return idDependenciaRemitente;
	}

	public void setIdDependenciaRemitente(Long idDependenciaRemitente) {
		this.idDependenciaRemitente = idDependenciaRemitente;
	}

	public Long getIdTipoCorrespondencia() {
		return idTipoCorrespondencia;
	}

	public void setIdTipoCorrespondencia(Long idTipoCorrespondencia) {
		this.idTipoCorrespondencia = idTipoCorrespondencia;
	}

	public String getTipoCorrespondenciaOtro() {
		return tipoCorrespondenciaOtro;
	}

	public void setTipoCorrespondenciaOtro(String tipoCorrespondenciaOtro) {
		this.tipoCorrespondenciaOtro = tipoCorrespondenciaOtro;
	}

	public String getNumDocInterno() {
		return numDocInterno;
	}

	public void setNumDocInterno(String numDocInterno) {
		this.numDocInterno = numDocInterno;
	}

	public String getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public String getRetornoCargo() {
		return retornoCargo;
	}

	public void setRetornoCargo(String retornoCargo) {
		this.retornoCargo = retornoCargo;
	}

	public String getUrgente() {
		return urgente;
	}

	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}

	public String getConfidencial() {
		return confidencial;
	}

	public void setConfidencial(String confidencial) {
		this.confidencial = confidencial;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<ConCopia> getConCopia() {
		return conCopia;
	}

	public void setConCopia(List<ConCopia> conCopia) {
		this.conCopia = conCopia;
	}

	public List<Destinatario> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(List<Destinatario> destinatarios) {
		this.destinatarios = destinatarios;
	}
	/* FIN TICKET 9000003934*/
  
  public String toString() { return "CorrespondenciaExterna [tipoCE=" + this.tipoCE + ", entidadExterna=" + this.entidadExterna + ", personaDest=" + this.personaDest + ", direccion=" + this.direccion + ", idPais=" + this.idPais + ", ciudad=" + this.ciudad + ", idProvincia=" + this.idProvincia + ", idDistrito=" + this.idDistrito + ", idDepartamento=" + this.idDepartamento + ", idDependenciaRemitente=" + this.idDependenciaRemitente + ", idTipoCorrespondencia=" + this.idTipoCorrespondencia + ", tipoCorrespondenciaOtro=" + this.tipoCorrespondenciaOtro + ", numDocInterno=" + this.numDocInterno + ", fechaDocumento=" + this.fechaDocumento + ", retornoCargo=" + this.retornoCargo + ", urgente=" + this.urgente + ", confidencial=" + this.confidencial + ", asunto=" + this.asunto + ", observacion=" + this.observacion + ", conCopia=" + this.conCopia + ", destinatarios=" + this.destinatarios + "]"; }
}

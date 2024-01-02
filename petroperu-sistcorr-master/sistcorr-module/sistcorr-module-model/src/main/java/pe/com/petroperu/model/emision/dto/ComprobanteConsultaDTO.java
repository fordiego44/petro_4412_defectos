//9000004276
package pe.com.petroperu.model.emision.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class ComprobanteConsultaDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaRecepcion;
	private String correlativo;
	private String nroBatch;
	private String ruc;
	private String razonSocial;
	private String descComprobante;
	private String nroComprobante;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaComprobante;
	private String moneda;
	private String estado;
	private String dependencia;

	@Override
	public String toString() {
		return "ComprobanteConsultaDTO [fechaRecepcion=" + fechaRecepcion + ", correlativo=" + correlativo + ", nroBatch=" + nroBatch + ", ruc=" + ruc + ", razonSocial=" + razonSocial + ", descComprobante=" + descComprobante + ", nroComprobante=" + nroComprobante + ", fechaComprobante=" + fechaComprobante + ", moneda=" + moneda + ", estado=" + estado + ", dependencia=" + dependencia + "]";
	}

	public ComprobanteConsultaDTO() {
	}

	public ComprobanteConsultaDTO(Date fechaRecepcion, String correlativo, String nroBatch, String ruc, String razonSocial, String descComprobante, String nroComprobante, Date fechaComprobante, String moneda, String estado, String dependencia) {
		super();
		this.fechaRecepcion = fechaRecepcion;
		this.correlativo = correlativo;
		this.nroBatch = nroBatch;
		this.ruc = ruc;
		this.razonSocial = razonSocial;
		this.descComprobante = descComprobante;
		this.nroComprobante = nroComprobante;
		this.fechaComprobante = fechaComprobante;
		this.moneda = moneda;
		this.estado = estado;
		this.dependencia = dependencia;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getNroBatch() {
		return nroBatch;
	}

	public void setNroBatch(String nroBatch) {
		this.nroBatch = nroBatch;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDescComprobante() {
		return descComprobante;
	}

	public void setDescComprobante(String descComprobante) {
		this.descComprobante = descComprobante;
	}

	public String getNroComprobante() {
		return nroComprobante;
	}

	public void setNroComprobante(String nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	public Date getFechaComprobante() {
		return fechaComprobante;
	}

	public void setFechaComprobante(Date fechaComprobante) {
		this.fechaComprobante = fechaComprobante;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

}

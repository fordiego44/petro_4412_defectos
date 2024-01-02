//9000004276
package pe.com.petroperu.service.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class FiltroConsultaComprobanteDTO {

	private String correlativo;
	private Integer estado;
	private String nroBatch;
	private Integer codDependencia;
	private String rucProveedor;
	private String numComprobante;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaDesde;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaHasta;
	private String razonSocial;

	private String fechaDesdeText;
	private String fechaHastaText;

	@Override
	public String toString() {
		return "FiltroConsultaComprobante [correlativo=" + correlativo + ", estado=" + estado + ", nroBatch=" + nroBatch
				+ ", codDependencia=" + codDependencia + ", rucProveedor=" + rucProveedor + ", numComprobante="
				+ numComprobante + ", fechaDesde=" + fechaDesde + ", fechaHasta=" + fechaHasta + ", razonSocial="
				+ razonSocial + ", fechaDesdeText=" + fechaDesdeText + ", fechaHastaText=" + fechaHastaText + "]";
	}

	public String getCorrelativo() {
		correlativo = correlativo == null ? "" : correlativo;
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public Integer getEstado() {
		estado = estado == null ? 0 : estado;
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getNroBatch() {
		nroBatch = nroBatch == null ? "" : nroBatch;
		return nroBatch;
	}

	public void setNroBatch(String nroBatch) {
		this.nroBatch = nroBatch;
	}

	public Integer getCodDependencia() {
		codDependencia = codDependencia == null ? 0 : codDependencia;
		return codDependencia;
	}

	public void setCodDependencia(Integer codDependencia) {
		this.codDependencia = codDependencia;
	}

	public String getRucProveedor() {
		rucProveedor = rucProveedor == null ? "" : rucProveedor;
		return rucProveedor;
	}

	public void setRucProveedor(String asunto) {
		this.rucProveedor = asunto;
	}

	public void setFechaDesdeText(String fechaDesdeText) {
		this.fechaDesdeText = fechaDesdeText;
	}

	public void setFechaHastaText(String fechaHastaText) {
		this.fechaHastaText = fechaHastaText;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getNumComprobante() {
		numComprobante = numComprobante == null ? "" : numComprobante;
		return numComprobante;
	}

	public void setNumComprobante(String numComprobante) {
		this.numComprobante = numComprobante;
	}

	public String getRazonSocial() {
		razonSocial = razonSocial == null ? "" : razonSocial;
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getFechaDesdeText() {
		if (fechaDesde == null)
			return "";
		fechaDesdeText = new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(fechaDesde);
		return fechaDesdeText;
	}

	public String getFechaHastaText() {
		if (fechaHasta == null)
			return "";
		fechaHastaText = new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(fechaHasta);
		return fechaHastaText;
	}

}

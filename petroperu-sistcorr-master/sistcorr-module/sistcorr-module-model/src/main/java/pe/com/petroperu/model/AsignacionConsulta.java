package pe.com.petroperu.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import pe.com.petroperu.Utilitario;

public class AsignacionConsulta {
	
	private Integer idAsignacion;
	private String correlativo;
	private String numeroDocumentoInterno;
	private String accion;
	private String asunto;
	private String fechaAsignacion;
	private String asignado;
	private String solicitante;
	private String estado;
	private String fechaPlazoRespuesta;
	private String avance;
	private String textoAsig;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaAsignacionProc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaPlazoRespuestaProc;
	private String tipoIcono;
	private String esConfidencial;
	private String esAsignado;
	// TICKET 9000003995
	private String codigoAccion;
	private String fechaRecepcion;
	private String fechaDocumento;
	private String remitente;
	private String documentoRespuesta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaRecepcionProc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaDocumentoProc;
	// FIN TICKET
	
	
	
	public String getEsConfidencial() {
		return esConfidencial;
	}

	public void setEsConfidencial(String esConfidencial) {
		this.esConfidencial = esConfidencial;
	}

	public String getEsAsignado() {
		return esAsignado;
	}

	public void setEsAsignado(String esAsignado) {
		this.esAsignado = esAsignado;
	}

	public String getTipoIcono() {
		return tipoIcono;
	}

	public void setTipoIcono(String tipoIcono) {
		this.tipoIcono = tipoIcono;
	}

	public Integer getIdAsignacion() {
		return idAsignacion;
	}
	
	public void setIdAsignacion(Integer idAsignacion) {
		this.idAsignacion = idAsignacion;
	}
	
	public String getCorrelativo() {
		return correlativo;
	}
	
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	
	public String getNumeroDocumentoInterno() {
		return numeroDocumentoInterno;
	}
	
	public void setNumeroDocumentoInterno(String numeroDocumentoInterno) {
		this.numeroDocumentoInterno = numeroDocumentoInterno;
	}
	
	public String getAccion() {
		return accion;
	}
	
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	public String getAsunto() {
		return asunto;
	}
	
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	public String getFechaAsignacion() {
		return fechaAsignacion;
	}
	
	public void setFechaAsignacion(String fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}
	
	public String getAsignado() {
		return asignado;
	}
	
	public void setAsignado(String asignado) {
		this.asignado = asignado;
	}
	
	public String getSolicitante() {
		return solicitante;
	}
	
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getFechaPlazoRespuesta() {
		return fechaPlazoRespuesta;
	}
	
	public void setFechaPlazoRespuesta(String fechaPlazoRespuesta) {
		this.fechaPlazoRespuesta = fechaPlazoRespuesta;
	}
	
	public String getAvance() {
		return avance;
	}
	
	public void setAvance(String avance) {
		this.avance = avance;
	}
	
	public String getTextoAsig() {
		return textoAsig;
	}
	
	public void setTextoAsig(String textoAsig) {
		this.textoAsig = textoAsig;
	}
	
	public String getFechaAsignacionProc() {
		if(this.fechaAsignacion!=null && !this.fechaAsignacion.equals("")){
			Date dateFechaAsignacionProc = Utilitario.convertirToDate(this.fechaAsignacion);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.fechaAsignacionProc = sdf.format(dateFechaAsignacionProc);
		}else{
			this.fechaAsignacionProc = "";
		}
		return fechaAsignacionProc;
	}
	
	public void setFechaAsignacionProc(String fechaAsignacionProc) {
		this.fechaAsignacionProc = fechaAsignacionProc;
	}
	
	public String getFechaPlazoRespuestaProc() {
		if(this.fechaPlazoRespuesta!=null && !this.fechaPlazoRespuesta.equals("")){
			Date dateFechaPlazoRespuestaProc = Utilitario.convertirToDate(this.fechaPlazoRespuesta);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.fechaPlazoRespuestaProc = sdf.format(dateFechaPlazoRespuestaProc);
		}else{
			this.fechaPlazoRespuestaProc = "";
		}
		return fechaPlazoRespuestaProc;
	}
	
	public void setFechaPlazoRespuestaProc(String fechaPlazoRespuestaProc) {
		this.fechaPlazoRespuestaProc = fechaPlazoRespuestaProc;
	}
	
	// TICKET 9000003995
	public String getCodigoAccion() {
		return codigoAccion;
	}

	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}

	public String getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public String getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public String getRemitente() {
		return remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	public String getDocumentoRespuesta() {
		return documentoRespuesta;
	}

	public void setDocumentoRespuesta(String documentoRespuesta) {
		this.documentoRespuesta = documentoRespuesta;
	}

	public String getFechaRecepcionProc() {
		if(this.fechaRecepcion!=null && !this.fechaRecepcion.equals("")){
			Date dateFechaRecepcionProc = Utilitario.convertirToDate(this.fechaRecepcion);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.fechaRecepcionProc = sdf.format(dateFechaRecepcionProc);
			
		}else{
			this.fechaRecepcionProc = "";
		}
		return fechaRecepcionProc;
	}

	public void setFechaRecepcionProc(String fechaRecepcionProc) {
		this.fechaRecepcionProc = fechaRecepcionProc;
	}

	public String getFechaDocumentoProc() {
		try{
			if(this.fechaDocumento!=null && !this.fechaDocumento.equals("")){
				Date dateFechaAsignacionProc = Utilitario.convertirToDate(this.fechaDocumento);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				this.fechaDocumentoProc = sdf.format(dateFechaAsignacionProc);
			}else{
				this.fechaDocumentoProc = "";
			}
		}catch(Exception e){
			e.printStackTrace();
			this.fechaDocumentoProc = "";
		}
		return fechaDocumentoProc;
	}

	public void setFechaDocumentoProc(String fechaDocumentoProc) {
		this.fechaDocumentoProc = fechaDocumentoProc;
	}
	// FIN TICKET

	@Override
	public String toString() {
		return "AsignacionConsulta [idAsignacion=" + idAsignacion + ", correlativo=" + correlativo
				+ ", numeroDocumentoInterno=" + numeroDocumentoInterno + ", accion=" + accion + ", asunto=" + asunto
				+ ", fechaAsignacion=" + fechaAsignacion + ", asignado=" + asignado + ", solicitante=" + solicitante
				+ ", estado=" + estado + ", fechaPlazoRespuesta=" + fechaPlazoRespuesta + ", avance=" + avance
				+ ", textoAsig=" + textoAsig + ", fechaAsignacionProc=" + fechaAsignacionProc
				+ ", fechaPlazoRespuestaProc=" + fechaPlazoRespuestaProc + ", tipoIcono=" + tipoIcono
				+ ", esConfidencial=" + esConfidencial + ", esAsignado=" + esAsignado + ", codigoAccion=" + codigoAccion
				+ ", fechaRecepcion=" + fechaRecepcion + ", fechaDocumento=" + fechaDocumento + ", remitente="
				+ remitente + ", documentoRespuesta=" + documentoRespuesta + ", fechaRecepcionProc="
				+ fechaRecepcionProc + ", fechaDocumentoProc=" + fechaDocumentoProc + "]";
	}
	
}

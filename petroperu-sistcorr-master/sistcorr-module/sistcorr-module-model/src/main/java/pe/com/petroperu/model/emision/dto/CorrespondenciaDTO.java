package pe.com.petroperu.model.emision.dto;

import pe.com.petroperu.model.emision.CorrespondenciaEstado;

public class CorrespondenciaDTO {

	private Long id;
	private String usuario;
	private String codigoTipoCorrespondencia;
	private String tipoCorrespondencia;
	private String asunto;
	private Long idCorrelativo;
	private String correlativo;
	private String fechaDocumento;
	private boolean firmaDigital;
	private CorrespondenciaEstado estado;
	private String solicitante;
	private String firmante;
	private String correlativoFilenet;
	private String bandeja;
	private String estadoDescripcion;
	private String tipoCorrAbrev;
	private String firmaEstado;
	private String motivoRechazo;
	private String firmantePrevio;
	// TICKET 9000003943
	private boolean rutaAprobacion;
	// FIN TICKET
	// TICKET 9000003993
	private String fechaModificacion;
	// FIN TICKET
	// TICKET 9000004409
	private String flgRemplazo;
	private String userRemplazo;
	private String nombreRemplazo;
	public String getFlgRemplazo() {
		return flgRemplazo;
	}

	public void setFlgRemplazo(String flgRemplazo) {
		this.flgRemplazo = flgRemplazo;
	}
	
	public String getUserRemplazo() {
		return userRemplazo;
	}

	public void setUserRemplazo(String userRemplazo) {
		this.userRemplazo = userRemplazo;
	}

	public String getNombreRemplazo() {
		return nombreRemplazo;
	}

	public void setNombreRemplazo(String nombreRemplazo) {
		this.nombreRemplazo = nombreRemplazo;
	}
	// FIN TICKET
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCodigoTipoCorrespondencia() {
		return this.codigoTipoCorrespondencia;
	}

	public void setCodigoTipoCorrespondencia(String codigoTipoCorrespondencia) {
		this.codigoTipoCorrespondencia = codigoTipoCorrespondencia;
	}

	public String getTipoCorrespondencia() {
		return this.tipoCorrespondencia;
	}

	public void setTipoCorrespondencia(String tipoCorrespondencia) {
		this.tipoCorrespondencia = tipoCorrespondencia;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public Long getIdCorrelativo() {
		return this.idCorrelativo;
	}

	public void setIdCorrelativo(Long idCorrelativo) {
		this.idCorrelativo = idCorrelativo;
	}

	public String getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public CorrespondenciaEstado getEstado() {
		return this.estado;
	}

	public void setEstado(CorrespondenciaEstado estado) {
		this.estado = estado;
	}

	public String getFechaDocumento() {
		return this.fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public boolean isFirmaDigital() {
		return this.firmaDigital;
	}

	public void setFirmaDigital(boolean firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	public String getCorrelativoFilenet() {
		return this.correlativoFilenet;
	}

	public void setCorrelativoFilenet(String correlativoFilenet) {
		this.correlativoFilenet = correlativoFilenet;
	}

	public String getSolicitante() {
		return this.solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getFirmante() {
		return this.firmante;
	}

	public void setFirmante(String firmante) {
		this.firmante = firmante;
	}

	public String getEstadoDescripcion() {
		this.estadoDescripcion = this.estado.getDescripcionEstado();
		return this.estadoDescripcion;
	}

	public String getTipoCorrAbrev() {
		switch (this.tipoCorrespondencia) {
		case "MEMORANDO":
			tipoCorrAbrev = "ME";
			break;
		case "MEMORANDO MÚLTIPLE":
			tipoCorrAbrev = "MM";
			break;
		case "CIRCULAR":
			tipoCorrAbrev = "CI";
			break;
		case "HOJA DE ACCIÓN":
			tipoCorrAbrev = "HA";
			break;
		case "INFORME":
			tipoCorrAbrev = "IN";
			break;
		case "RESUMEN EJECUTIVO":
			tipoCorrAbrev = "RE";
			break;
		case "SOLICITUD APROBACIÓN DE DOCUMENTO":
			tipoCorrAbrev = "SA";
			break;
		case "CARTA":
			tipoCorrAbrev = "CA";
			break;
		case "OTROS":
			tipoCorrAbrev = "OT";
			break;
		case "DOCUMENTO POR APROBAR":
			tipoCorrAbrev = "DA";
			break;
		case "DOCUMENTOS POR PAGAR"://inicio TICKET 9000004765
			tipoCorrAbrev = "DP";
			break;//fin TICKET 9000004765
		default:
			tipoCorrAbrev = "XX";
			break;
		}
		return tipoCorrAbrev;
	}

	public String getFirmaEstado() {
		if (this.firmaDigital) {
			firmaEstado = "DIGITAL";
		} else {
			firmaEstado = "MANUAL";
		}
		return firmaEstado;
	}

	public String getBandeja() {
		bandeja = this.estado.getBandeja();
		return bandeja;
	}

	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	public String getFirmantePrevio() {
		return firmantePrevio;
	}

	public void setFirmantePrevio(String firmantePrevio) {
		this.firmantePrevio = firmantePrevio;
	}

	public boolean isRutaAprobacion() {
		return rutaAprobacion;
	}

	public void setRutaAprobacion(boolean rutaAprobacion) {
		this.rutaAprobacion = rutaAprobacion;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	
}

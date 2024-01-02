package pe.com.petroperu.cliente.model;

public class FiltroConsultaAuditoria {
	
	private String correlativo;
	private String codigoEstado;
	private String fechaRegistroDesde;
	private String fechaRegistroHasta;
	private String numeroDocumentoInterno;
	private String fechaDocumentoInterno;
	private String codigoDependenciaRemitente;
	private String codigoDependenciaDestino;
	private String codigoTipoCorrespondencia;
	private String nombreDependenciaExterna;
	private String guiaRemision;
	private String asunto;
	private String procedencia;
	
	public FiltroConsultaAuditoria() {
		super();
		this.correlativo = "";
		this.codigoEstado = "0";
		this.fechaRegistroDesde = "";
		this.fechaRegistroHasta = "";
		this.numeroDocumentoInterno = "%";
		this.fechaDocumentoInterno = "";
		this.codigoDependenciaRemitente = "0";
		this.codigoDependenciaDestino = "0";
		this.codigoTipoCorrespondencia = "0";
		this.nombreDependenciaExterna = "%";
		this.guiaRemision = "";
		this.asunto = "%";
		this.procedencia = "";
	}
	public FiltroConsultaAuditoria(String correlativo, String codigoEstado, String fechaRegistroDesde,
			String fechaRegistroHasta, String numeroDocumentoInterno, String fechaDocumentoInterno,
			String codigoDependenciaRemitente, String codigoDependenciaDestino, String codigoTipoCorrespondencia,
			String nombreDependenciaExterna, String guiaRemision, String asunto, String procedencia) {
		super();
		this.correlativo = correlativo;
		this.codigoEstado = codigoEstado;
		this.fechaRegistroDesde = fechaRegistroDesde;
		this.fechaRegistroHasta = fechaRegistroHasta;
		this.numeroDocumentoInterno = numeroDocumentoInterno;
		this.fechaDocumentoInterno = fechaDocumentoInterno;
		this.codigoDependenciaRemitente = codigoDependenciaRemitente;
		this.codigoDependenciaDestino = codigoDependenciaDestino;
		this.codigoTipoCorrespondencia = codigoTipoCorrespondencia;
		this.nombreDependenciaExterna = nombreDependenciaExterna;
		this.guiaRemision = guiaRemision;
		this.asunto = asunto;
		this.procedencia = procedencia;
	}
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getFechaRegistroDesde() {
		return fechaRegistroDesde;
	}
	public void setFechaRegistroDesde(String fechaRegistroDesde) {
		this.fechaRegistroDesde = fechaRegistroDesde;
	}
	public String getFechaRegistroHasta() {
		return fechaRegistroHasta;
	}
	public void setFechaRegistroHasta(String fechaRegistroHasta) {
		this.fechaRegistroHasta = fechaRegistroHasta;
	}
	public String getNumeroDocumentoInterno() {
		return numeroDocumentoInterno;
	}
	public void setNumeroDocumentoInterno(String numeroDocumentoInterno) {
		this.numeroDocumentoInterno = numeroDocumentoInterno;
	}
	public String getFechaDocumentoInterno() {
		return fechaDocumentoInterno;
	}
	public void setFechaDocumentoInterno(String fechaDocumentoInterno) {
		this.fechaDocumentoInterno = fechaDocumentoInterno;
	}
	public String getCodigoDependenciaRemitente() {
		return codigoDependenciaRemitente;
	}
	public void setCodigoDependenciaRemitente(String codigoDependenciaRemitente) {
		this.codigoDependenciaRemitente = codigoDependenciaRemitente;
	}
	public String getCodigoDependenciaDestino() {
		return codigoDependenciaDestino;
	}
	public void setCodigoDependenciaDestino(String codigoDependenciaDestino) {
		this.codigoDependenciaDestino = codigoDependenciaDestino;
	}
	public String getCodigoTipoCorrespondencia() {
		return codigoTipoCorrespondencia;
	}
	public void setCodigoTipoCorrespondencia(String codigoTipoCorrespondencia) {
		this.codigoTipoCorrespondencia = codigoTipoCorrespondencia;
	}
	public String getNombreDependenciaExterna() {
		return nombreDependenciaExterna;
	}
	public void setNombreDependenciaExterna(String nombreDependenciaExterna) {
		this.nombreDependenciaExterna = nombreDependenciaExterna;
	}
	public String getGuiaRemision() {
		return guiaRemision;
	}
	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getProcedencia() {
		return procedencia;
	}
	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}
	@Override
	public String toString() {
		return "FiltroConsultaCorrespondencia [correlativo=" + correlativo + ", codigoEstado=" + codigoEstado
				+ ", fechaRegistroDesde=" + fechaRegistroDesde + ", fechaRegistroHasta=" + fechaRegistroHasta
				+ ", numeroDocumentoInterno=" + numeroDocumentoInterno + ", fechaDocumentoInterno="
				+ fechaDocumentoInterno + ", codigoDependenciaRemitente=" + codigoDependenciaRemitente
				+ ", codigoDependenciaDestino=" + codigoDependenciaDestino + ", codigoTipoCorrespondencia="
				+ codigoTipoCorrespondencia + ", nombreDependenciaExterna=" + nombreDependenciaExterna
				+ ", guiaRemision=" + guiaRemision + ", asunto=" + asunto + ", procedencia=" + procedencia + "]";
	}
	
}

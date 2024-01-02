package pe.com.petroperu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import pe.com.petroperu.Utilitario;

public class CorrespondenciaSimple {
	private String correlativo;
	private String dependenciaDestino;
	private String asunto;
	private String dependenciaRemitente;
	private Long dtfechaCreacion;
	private String fechaCreacion;
	private String tipoIcono;
	private String tipo;
	private String workflowId;
	private String fechaLimite;
	private String detalleSolicitud;
	private Integer idAsignacion;
	private String codigoAccion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaLimitePRC;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaCreacionPRC;
	// TICKET 9000003942
	private String numeroDocumento;
	// FIN TICKET 9000003942
	// TICKET 9000003780
	private String esConfidencial;
	// FIN TICKET
	
	private String esAsignado;
	// TICKET 9000003997
	private String esDeEmision;
	// FIN TICKET
	private String sVersion;//TICKET 9000004273
	//@JsonIgnore
	private Integer sVersionNum;//TICKET 9000004273
	
	public String getCorrelativo() {
		return this.correlativo;
	}

	//INICIO TICKET 9000004273
	public String getsVersion() {
		return sVersion;
	}

	public Integer getsVersionNum() {
		this.sVersionNum = 0;
		if(sVersion != null && !sVersion.trim().equalsIgnoreCase(""))
			this.sVersionNum = (int)Double.parseDouble(this.getsVersion());
		return sVersionNum;
	}

	public void setsVersionNum(Integer sVersionNum) {
		this.sVersionNum = sVersionNum;
	}

	public void setsVersion(String sVersion) {
		this.sVersion = sVersion;
	}
	/*public String getVersionAsignacion() {
		DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String str_date_1 = "04/05/2021";
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate local_date_1 = LocalDate.parse(str_date_1, formatter_1);
		Date date = Date.from(local_date_1.atStartOfDay(defaultZoneId).toInstant());
		if(this.getFechaCreacionPRC().after(date))
			this.versionAsignacion = "V2";
		else this.versionAsignacion = "V1";
		
		return versionAsignacion;
	}

	public void setVersionAsignacion(String versionAsignacion) {
		this.versionAsignacion = versionAsignacion;
	}*/
	//FIN TICKET 9000004273
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getDependenciaDestino() {
		return this.dependenciaDestino;
	}

	public void setDependenciaDestino(String dependenciaDestino) {
		this.dependenciaDestino = dependenciaDestino;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getDependenciaRemitente() {
		return this.dependenciaRemitente;
	}

	public void setDependenciaRemitente(String dependenciaRemitente) {
		this.dependenciaRemitente = dependenciaRemitente;
	}

	public Long getDtfechaCreacion() {
		return this.dtfechaCreacion;
	}

	public void setDtfechaCreacion(Long dtfechaCreacion) {
		this.dtfechaCreacion = dtfechaCreacion;
	}

	public String getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getTipoIcono() {
		return this.tipoIcono;
	}

	public void setTipoIcono(String tipoIcono) {
		this.tipoIcono = tipoIcono;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getWorkflowId() {
		return this.workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getFechaLimite() {
		return this.fechaLimite;
	}

	public void setFechaLimite(String fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public String getDetalleSolicitud() {
		return this.detalleSolicitud;
	}

	public void setDetalleSolicitud(String detalleSolicitud) {
		this.detalleSolicitud = detalleSolicitud;
	}

	public Integer getIdAsignacion() {
		return this.idAsignacion;
	}

	public void setIdAsignacion(Integer idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public String getCodigoAccion() {
		return this.codigoAccion;
	}

	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}

	public Date getFechaLimitePRC() {
		this.fechaLimitePRC = Utilitario.convertirToDate(this.fechaLimite);
		return this.fechaLimitePRC;
	}

	public Date getFechaCreacionPRC() {
		this.fechaCreacionPRC = Utilitario.convertirToDate(this.fechaCreacion);
		return this.fechaCreacionPRC;
	}
	
	public String getEsAsignado(){
		return this.esAsignado;
	}
	
	public void setEsAsignado(String esAsignado){
		this.esAsignado = esAsignado;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getEsConfidencial() {
		return esConfidencial;
	}

	public void setEsConfidencial(String esConfidencial) {
		this.esConfidencial = esConfidencial;
	}

	public String getEsDeEmision() {
		return esDeEmision;
	}

	public void setEsDeEmision(String esDeEmision) {
		this.esDeEmision = esDeEmision;
	}
	
}

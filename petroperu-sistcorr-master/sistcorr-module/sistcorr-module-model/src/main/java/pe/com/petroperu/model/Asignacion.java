package pe.com.petroperu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import pe.com.petroperu.Semaforo;
import pe.com.petroperu.Utilitario;

public class Asignacion {
	private Integer idAsignacion;
	private String codigoAccion;
	private String accion;
	private String dependenciaAsignada;
	private String dependenciaQuienAsigna;
	private String usuarioAsignador;
	private String usuarioAsignado;
	private String fechaLimite;
	private String detalleSolicitud;
	private String fechaAsignacion;
	private String tipoIcono;
	private String tipo;
	private String workflowId;
	private String correlativo;
	private String asunto;
	private String codigoDependenciaAsignada;
	private String codigoDependenciaQuienAsigna;
	private String nombreApellidoAsignador;
	private String nombreApellidoAsignado;
	private String fechaRespuesta;
	private Double avance;
	private String respuesta;
	private String esAtendida;
	private String estado;
	private Integer idPadre;
	private String requiereRespuesta;
	private String atendida;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaLimiteSTR;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaAsignacionSTR;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaRespuestaSTR;
	private String color;
	// TICKET 7000003282
	private String esAsignado;
	// TICKET 9000004044
	private String numeroDocumentoEnlace;
	private Long idDocumentoEnlace;
	// FIN TICKET
	private String sVersion;//TICKET 9000004273
	private Integer sVersionNum;//TICKET 9000004273
	
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
	//FIN TICKET 9000004273
	
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

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getDependenciaAsignada() {
		return this.dependenciaAsignada;
	}

	public void setDependenciaAsignada(String dependenciaAsignada) {
		this.dependenciaAsignada = dependenciaAsignada;
	}

	public String getDependenciaQuienAsigna() {
		return this.dependenciaQuienAsigna;
	}

	public void setDependenciaQuienAsigna(String dependenciaQuienAsigna) {
		this.dependenciaQuienAsigna = dependenciaQuienAsigna;
	}

	public String getUsuarioAsignador() {
		return this.usuarioAsignador;
	}

	public void setUsuarioAsignador(String usuarioAsignador) {
		this.usuarioAsignador = usuarioAsignador;
	}

	public String getUsuarioAsignado() {
		return this.usuarioAsignado;
	}

	public void setUsuarioAsignado(String usuarioAsignado) {
		this.usuarioAsignado = usuarioAsignado;
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

	public String getFechaAsignacion() {
		return this.fechaAsignacion;
	}

	public void setFechaAsignacion(String fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
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

	public String getCodigoDependenciaAsignada() {
		return this.codigoDependenciaAsignada;
	}

	public void setCodigoDependenciaAsignada(String codigoDependenciaAsignada) {
		this.codigoDependenciaAsignada = codigoDependenciaAsignada;
	}

	public String getCodigoDependenciaQuienAsigna() {
		return this.codigoDependenciaQuienAsigna;
	}

	public void setCodigoDependenciaQuienAsigna(String codigoDependenciaQuienAsigna) {
		this.codigoDependenciaQuienAsigna = codigoDependenciaQuienAsigna;
	}

	public String getNombreApellidoAsignador() {
		return this.nombreApellidoAsignador;
	}

	public void setNombreApellidoAsignador(String nombreApellidoAsignador) {
		this.nombreApellidoAsignador = nombreApellidoAsignador;
	}

	public String getNombreApellidoAsignado() {
		return this.nombreApellidoAsignado;
	}

	public void setNombreApellidoAsignado(String nombreApellidoAsignado) {
		this.nombreApellidoAsignado = nombreApellidoAsignado;
	}

	public String getFechaRespuesta() {
		return this.fechaRespuesta;
	}

	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	public Double getAvance() {
		return this.avance;
	}

	public void setAvance(Double avance) {
		this.avance = avance;
	}

	public String getEsAtendida() {
		return this.esAtendida;
	}

	public void setEsAtendida(String esAtendida) {
		this.esAtendida = esAtendida;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getIdPadre() {
		return this.idPadre;
	}

	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}

	public String getRequiereRespuesta() {
		return this.requiereRespuesta;
	}

	public void setRequiereRespuesta(String requiereRespuesta) {
		this.requiereRespuesta = requiereRespuesta;
	}

	public String getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getRespuesta() {
		return this.respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Date getFechaLimiteSTR() {
		this.fechaLimiteSTR = Utilitario.convertirToDate(this.fechaLimite);
		return this.fechaLimiteSTR;
	}

	public void setFechaLimiteSTR(Date fechaLimiteSTR) {
		this.fechaLimiteSTR = fechaLimiteSTR;
	}

	public Date getFechaAsignacionSTR() {
		this.fechaAsignacionSTR = Utilitario.convertirToDate(this.fechaAsignacion);
		return this.fechaAsignacionSTR;
	}

	public void setFechaAsignacionSTR(Date fechaAsignacionSTR) {
		this.fechaAsignacionSTR = fechaAsignacionSTR;
	}

	public Date getFechaRespuestaSTR() {
		this.fechaRespuestaSTR = Utilitario.convertirToDate(this.fechaRespuesta);
		return this.fechaRespuestaSTR;
	}

	public void setFechaRespuestaSTR(Date fechaRespuestaSTR) {
		this.fechaRespuestaSTR = fechaRespuestaSTR;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAtendida() {
		return this.atendida;
	}

	public void setAtendida(String atendida) {
		this.atendida = atendida;
	}

	public String getEsAsignado() {
		return esAsignado;
	}

	public void setEsAsignado(String esAsignado) {
		this.esAsignado = esAsignado;
	}

	public String getColor() {
		if (this.atendida == null) {
			this.avance = Double.valueOf(0.0D);
		} else {
			String _atendida = this.atendida.replace("%", "");
			_atendida = _atendida.trim();
			this.avance = Double.valueOf(Double.parseDouble(_atendida));
		}
		if (this.avance == null)
			this.avance = Double.valueOf(0.0D);
		if (this.avance.doubleValue() == 0.0D)
			this.color = Semaforo.ROJO.name();
		if (this.avance.doubleValue() > 0.0D && this.avance.doubleValue() < 100.0D)
			this.color = Semaforo.AMARILLO.name();
		if (this.avance.doubleValue() == 100.0D)
			this.color = Semaforo.VERDE.name();
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String toString() {
		return "Asignacion [idAsignacion=" + this.idAsignacion + ", codigoAccion=" + this.codigoAccion + ", accion="
				+ this.accion + ", dependenciaAsignada=" + this.dependenciaAsignada + ", dependenciaQuienAsigna="
				+ this.dependenciaQuienAsigna + ", usuarioAsignador=" + this.usuarioAsignador + ", usuarioAsignado="
				+ this.usuarioAsignado + ", fechaLimite=" + this.fechaLimite + ", detalleSolicitud="
				+ this.detalleSolicitud + ", fechaAsignacion=" + this.fechaAsignacion + ", tipoIcono=" + this.tipoIcono
				+ ", tipo=" + this.tipo + ", workflowId=" + this.workflowId + ", correlativo=" + this.correlativo
				+ ", asunto=" + this.asunto + ", codigoDependenciaAsignada=" + this.codigoDependenciaAsignada
				+ ", codigoDependenciaQuienAsigna=" + this.codigoDependenciaQuienAsigna + ", nombreApellidoAsignador="
				+ this.nombreApellidoAsignador + ", nombreApellidoAsignado=" + this.nombreApellidoAsignado
				+ ", fechaRespuesta=" + this.fechaRespuesta + ", avance=" + this.avance + ", respuesta="
				+ this.respuesta + ", esAtendida=" + this.esAtendida + ", estado=" + this.estado + ", idPadre="
				+ this.idPadre + ", requiereRespuesta=" + this.requiereRespuesta + ", atendida=" + this.atendida
				+ ", fechaLimiteSTR=" + this.fechaLimiteSTR + ", fechaAsignacionSTR=" + this.fechaAsignacionSTR
				+ ", fechaRespuestaSTR=" + this.fechaRespuestaSTR + ", color=" +

				getColor() + "]";
	}
	
	// TICKET 9000004044

	public String getNumeroDocumentoEnlace() {
		return numeroDocumentoEnlace;
	}

	public void setNumeroDocumentoEnlace(String numeroDocumentoEnlace) {
		this.numeroDocumentoEnlace = numeroDocumentoEnlace;
	}

	public Long getIdDocumentoEnlace() {
		return idDocumentoEnlace;
	}

	public void setIdDocumentoEnlace(Long idDocumentoEnlace) {
		this.idDocumentoEnlace = idDocumentoEnlace;
	}
	
	// FIN TICKET
}

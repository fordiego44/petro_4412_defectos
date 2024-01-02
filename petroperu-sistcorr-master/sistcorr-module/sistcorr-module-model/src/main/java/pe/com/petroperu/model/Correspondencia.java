package pe.com.petroperu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import pe.com.petroperu.Utilitario;

public class Correspondencia implements Serializable {
	private static final long serialVersionUID = 1L;
	private String correlativo;
	private String dependenciaDestino;
	private String dependenciaRemitente;
	private String asunto;
	private String fechaCreacion;
	private String tipoIcono;
	private String tipo;
	private String workflowId;
	private String codigoDependenciaDestino;
	private String codigoCGC;
	private String usuarioResponsable;
	private String nombreApellidoResponsable;
	private String usuarioRadicador;
	private String usuarioGestor;
	private String nombreApellidoGestor;
	private String numeroDocumento;
	private String estado;
	private String esConfidencial;
	private String proceso;
	private Integer idDependenciaAsignada;
	private Integer idDependenciaQuienAsigna;
	private Integer idPadre;
	private String textoReq;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaCreacionProc;
	
	// TICKET 9000003514
	private String tipoTransaccion;
	private String correlativoReferencia;
	private String numeroFolios;
	private String lugarTrabajoDestino;
	private String lugarTrabajoRemitente;
	private String nombreApellidoPersonaDestino;
	private String nombreApellidoPersonaRemitente;
	private String guia;
	private String guiaRemision;
	private String esRegistroDesdeDependencia;
	private String fechaDocumento;
	private String cgc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaDocumentoProc;
	private String ultimaObservacion;
	private String urgente;
	// FIN TICKET 9000003514
	
	// TICKET 9000004044
	private String documentoRespuesta;
	// FIN TICKET
	
	/*INI Ticket 9*4413*/
	private String dni;
	private String nombresApellido;
	private String ruc;
	private String razonSocial;
	private Integer numeroFolio;
	private String observacion;
	private String motivoRechazo;
	private String tipoCorrespondencia;
	/*FIN Ticket 9*4413*/

	public Date getFechaCreacionProc() {
		this.fechaCreacionProc = Utilitario.convertirToDate(this.fechaCreacion);
		return this.fechaCreacionProc;
	}

	public void setFechaCreacionProc(Date fechaCreacionProc) {
		this.fechaCreacionProc = fechaCreacionProc;
	}

	public String getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getDependenciaDestino() {
		return this.dependenciaDestino;
	}

	public void setDependenciaDestino(String dependenciaDestino) {
		this.dependenciaDestino = dependenciaDestino;
	}

	public String getDependenciaRemitente() {
		return this.dependenciaRemitente;
	}

	public void setDependenciaRemitente(String dependenciaRemitente) {
		this.dependenciaRemitente = dependenciaRemitente;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
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

	public String getCodigoDependenciaDestino() {
		return this.codigoDependenciaDestino;
	}

	public void setCodigoDependenciaDestino(String codigoDependenciaDestino) {
		this.codigoDependenciaDestino = codigoDependenciaDestino;
	}

	public String getCodigoCGC() {
		return this.codigoCGC;
	}

	public void setCodigoCGC(String codigoCGC) {
		this.codigoCGC = codigoCGC;
	}

	public String getUsuarioResponsable() {
		return this.usuarioResponsable;
	}

	public void setUsuarioResponsable(String usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}

	public String getNombreApellidoResponsable() {
		return this.nombreApellidoResponsable;
	}

	public void setNombreApellidoResponsable(String nombreApellidoResponsable) {
		this.nombreApellidoResponsable = nombreApellidoResponsable;
	}

	public String getUsuarioRadicador() {
		return this.usuarioRadicador;
	}

	public void setUsuarioRadicador(String usuarioRadicador) {
		this.usuarioRadicador = usuarioRadicador;
	}

	public String getUsuarioGestor() {
		return this.usuarioGestor;
	}

	public void setUsuarioGestor(String usuarioGestor) {
		this.usuarioGestor = usuarioGestor;
	}

	public String getNombreApellidoGestor() {
		return this.nombreApellidoGestor;
	}

	public void setNombreApellidoGestor(String nombreApellidoGestor) {
		this.nombreApellidoGestor = nombreApellidoGestor;
	}

	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEsConfidencial() {
		return this.esConfidencial;
	}

	public void setEsConfidencial(String esConfidencial) {
		this.esConfidencial = esConfidencial;
	}

	public String getProceso() {
		return this.proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public Integer getIdDependenciaAsignada() {
		return this.idDependenciaAsignada;
	}

	public void setIdDependenciaAsignada(Integer idDependenciaAsignada) {
		this.idDependenciaAsignada = idDependenciaAsignada;
	}

	public Integer getIdDependenciaQuienAsigna() {
		return this.idDependenciaQuienAsigna;
	}

	public void setIdDependenciaQuienAsigna(Integer idDependenciaQuienAsigna) {
		this.idDependenciaQuienAsigna = idDependenciaQuienAsigna;
	}

	public Integer getIdPadre() {
		return this.idPadre;
	}

	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}

	public String getTextoReq() {
		return this.textoReq;
	}

	public void setTextoReq(String textoReq) {
		this.textoReq = textoReq;
	}
	
	// TICKET 9000003514

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public String getCorrelativoReferencia() {
		return correlativoReferencia;
	}

	public void setCorrelativoReferencia(String correlativoReferencia) {
		this.correlativoReferencia = correlativoReferencia;
	}

	public String getNumeroFolios() {
		return numeroFolios;
	}

	public void setNumeroFolios(String numeroFolios) {
		this.numeroFolios = numeroFolios;
	}

	public String getLugarTrabajoDestino() {
		return lugarTrabajoDestino;
	}

	public void setLugarTrabajoDestino(String lugarTrabajoDestino) {
		this.lugarTrabajoDestino = lugarTrabajoDestino;
	}

	public String getLugarTrabajoRemitente() {
		return lugarTrabajoRemitente;
	}

	public void setLugarTrabajoRemitente(String lugarTrabajoRemitente) {
		this.lugarTrabajoRemitente = lugarTrabajoRemitente;
	}

	public String getNombreApellidoPersonaDestino() {
		return nombreApellidoPersonaDestino;
	}

	public void setNombreApellidoPersonaDestino(String nombreApellidoPersonaDestino) {
		this.nombreApellidoPersonaDestino = nombreApellidoPersonaDestino;
	}

	public String getNombreApellidoPersonaRemitente() {
		return nombreApellidoPersonaRemitente;
	}

	public void setNombreApellidoPersonaRemitente(String nombreApellidoPersonaRemitente) {
		this.nombreApellidoPersonaRemitente = nombreApellidoPersonaRemitente;
	}

	public String getGuia() {
		return guia;
	}

	public void setGuia(String guia) {
		this.guia = guia;
	}

	public String getGuiaRemision() {
		return guiaRemision;
	}

	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	public String getEsRegistroDesdeDependencia() {
		return esRegistroDesdeDependencia;
	}

	public void setEsRegistroDesdeDependencia(String esRegistroDesdeDependencia) {
		this.esRegistroDesdeDependencia = esRegistroDesdeDependencia;
	}

	public String getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public String getCgc() {
		return cgc;
	}

	public void setCgc(String cgc) {
		this.cgc = cgc;
	}

	public Date getFechaDocumentoProc() {
		this.fechaDocumentoProc = Utilitario.convertirToDate(fechaDocumento);
		return fechaDocumentoProc;
	}

	public void setFechaDocumentoProc(Date fechaDocumentoProc) {
		this.fechaDocumentoProc = fechaDocumentoProc;
	}
	
	public String getUltimaObservacion() {
		return ultimaObservacion;
	}

	public void setUltimaObservacion(String ultimaObservacion) {
		this.ultimaObservacion = ultimaObservacion;
	}

	public String getUrgente() {
		return urgente;
	}

	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}
	
	
	
	// FIN TICKET 9000003514
	
	// TICKET 9000004044

	public String getDocumentoRespuesta() {
		return documentoRespuesta;
	}

	public void setDocumentoRespuesta(String documentoRespuesta) {
		this.documentoRespuesta = documentoRespuesta;
	}
	
	// FIN TICKET
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombresApellido() {
		return nombresApellido;
	}

	public void setNombresApellido(String nombresApellido) {
		this.nombresApellido = nombresApellido;
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

	public Integer getNumeroFolio() {
		return numeroFolio;
	}

	public void setNumeroFolio(Integer numeroFolio) {
		this.numeroFolio = numeroFolio;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	public String getTipoCorrespondencia() {
		return tipoCorrespondencia;
	}

	public void setTipoCorrespondencia(String tipoCorrespondencia) {
		this.tipoCorrespondencia = tipoCorrespondencia;
	}
}

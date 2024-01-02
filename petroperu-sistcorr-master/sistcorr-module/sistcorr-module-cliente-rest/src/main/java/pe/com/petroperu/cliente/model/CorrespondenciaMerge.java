package pe.com.petroperu.cliente.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.petroperu.model.Asignacion;
import pe.com.petroperu.model.Correspondencia;

public class CorrespondenciaMerge {
	private String estadoAsignacion;
	private String tipoAsignacion;
	private String codigoAccion;
	private String dependenciaAsignante;
	private String asignadoPor;
	private String responsable;
	private Date fechaAsignacion;
	private Date fechaTope;
	private String detalleRequerimineto;
	private Double avance;
	private String textoRespuesta;
	private Integer idAsignacion;
	private String correlativo;
	private String remitente;
	private String dependenciaDestino;
	private String estadoCorrespondencia;
	private Date fechaCreacion;
	private String numeroDocumento;
	private boolean nuevaCorrespondencia;
	private String fechaAsignacionSTR;
	private String fechaTopeSTR;
	private String fechaCreacionSTR;
	private String asunto;
	private Integer idPadre;
	private String codigoDependencia;

	// TICKET 9000003514
	private String tipoTransaccion;
	private String correlativoReferencia;
	private String cgc;
	private String tipoCorrespondencia;
	private String numeroFolios;
	private String lugarTrabajoRemitente;
	private String lugarTrabajoDestino;
	private String personaDestino;
	private String personaRemitente;
	private String fechaDocumento;
	private String fechaDocumentoSTR;
	private String ultimaObservacion;
	private String confidencial;
	private String guia;
	private String guiaRemision;
	private String registroEnDependencia;

	private String proceso;
	private String urgente;
	// FIN TICKET 9000003514

	// TICKET 7000003282
	private String esAsignado;
	// FIN TICKET
	
	private Integer sVersionNum;//TICKET 9000004273

	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	// TICKET 9000004044
	private String numeroDocumentoEnlace;
	private Long idDocumentoEnlace;
	// FIN TICKET

	/*INI Ticket 9*4413*/
	private String dni;
	private String nombresApellido;
	private String ruc;
	private String razonSocial;
	private String numeroFolio;
	private String observacion;
	private String codigoCGC;
	private String dependenciadestino;
	private String estado;
	private String motivoRechazo;
	/*FIN Ticket 9*4413*/
	
	public CorrespondenciaMerge() {
	}

	public CorrespondenciaMerge(Correspondencia correspondencia) {
		this.correlativo = correspondencia.getCorrelativo();
		this.remitente = correspondencia.getDependenciaRemitente();
		this.dependenciaDestino = correspondencia.getDependenciaDestino();
		this.fechaCreacion = correspondencia.getFechaCreacionProc();
		this.numeroDocumento = correspondencia.getNumeroDocumento();
		this.estadoCorrespondencia = correspondencia.getEstado();

		this.asunto = correspondencia.getAsunto();
		this.idPadre = Integer.valueOf(0);
		this.sVersionNum = Integer.valueOf(0);//TICKET 9000004273
		this.codigoDependencia = correspondencia.getCodigoDependenciaDestino();
		this.avance = Double.valueOf(0.0D);

		this.nuevaCorrespondencia = true;

		this.tipoTransaccion = correspondencia.getTipoTransaccion();
		this.correlativoReferencia = correspondencia.getCorrelativoReferencia();
		this.cgc = correspondencia.getCgc();
		this.tipoCorrespondencia = correspondencia.getTipo();
		this.numeroFolios = correspondencia.getNumeroFolios();
		this.lugarTrabajoRemitente = correspondencia.getLugarTrabajoRemitente();
		this.lugarTrabajoDestino = correspondencia.getLugarTrabajoDestino();
		this.personaDestino = correspondencia.getNombreApellidoPersonaDestino();
		this.personaRemitente = correspondencia.getNombreApellidoPersonaRemitente();
		this.fechaDocumento = correspondencia.getFechaDocumento();
		this.ultimaObservacion = correspondencia.getUltimaObservacion();

		this.guia = correspondencia.getGuia();
		this.guiaRemision = correspondencia.getGuiaRemision();
		this.confidencial = correspondencia.getEsConfidencial();
		this.registroEnDependencia = correspondencia.getEsRegistroDesdeDependencia();
		this.urgente = correspondencia.getUrgente();

		this.proceso = correspondencia.getProceso();
		
		this.numeroDocumentoEnlace = correspondencia.getDocumentoRespuesta();
	}

	public CorrespondenciaMerge(Correspondencia correspondencia, Asignacion asignacion) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] CorrespondenciaMerge");
		this.remitente = correspondencia.getDependenciaRemitente();
		this.dependenciaDestino = correspondencia.getDependenciaDestino();
		this.fechaCreacion = correspondencia.getFechaCreacionProc();
		this.numeroDocumento = correspondencia.getNumeroDocumento();
		this.estadoCorrespondencia = correspondencia.getEstado();
		this.idPadre = correspondencia.getIdPadre();

		this.estadoAsignacion = asignacion.getEstado();
		this.tipoAsignacion = asignacion.getAccion();
		this.codigoAccion = asignacion.getCodigoAccion();
		this.dependenciaAsignante = asignacion.getDependenciaQuienAsigna();
		this.asignadoPor = asignacion.getNombreApellidoAsignador();
		this.responsable = asignacion.getNombreApellidoAsignado();
		this.fechaAsignacion = asignacion.getFechaAsignacionSTR();
		this.detalleRequerimineto = asignacion.getDetalleSolicitud();
		this.avance = asignacion.getAvance();
		this.textoRespuesta = asignacion.getRespuesta();
		this.fechaTope = asignacion.getFechaLimiteSTR();
		this.idAsignacion = asignacion.getIdAsignacion();

		this.asunto = correspondencia.getAsunto();
		this.correlativo = asignacion.getCorrelativo();

		this.idPadre = asignacion.getIdAsignacion();
		this.codigoDependencia = asignacion.getCodigoDependenciaAsignada();
		this.urgente = correspondencia.getUrgente();

		this.nuevaCorrespondencia = false;

		this.tipoTransaccion = correspondencia.getTipoTransaccion();
		this.correlativoReferencia = correspondencia.getCorrelativoReferencia();
		this.cgc = correspondencia.getCgc();
		this.tipoCorrespondencia = correspondencia.getTipo();
		this.numeroFolios = correspondencia.getNumeroFolios();
		this.lugarTrabajoRemitente = correspondencia.getLugarTrabajoRemitente();
		this.lugarTrabajoDestino = correspondencia.getLugarTrabajoDestino();
		this.personaDestino = correspondencia.getNombreApellidoPersonaDestino();
		this.personaRemitente = correspondencia.getNombreApellidoPersonaRemitente();
		this.fechaDocumento = correspondencia.getFechaDocumento();
		this.confidencial = correspondencia.getEsConfidencial();
		this.guia = correspondencia.getGuia();
		this.guiaRemision = correspondencia.getGuiaRemision();
		this.registroEnDependencia = correspondencia.getEsRegistroDesdeDependencia();
		//INICIO TICKET 9000004273
		this.sVersionNum = asignacion.getsVersionNum();
		//FIN TICKET 9000004273
		this.proceso = correspondencia.getProceso();

		// TICKET 7000003282
		this.esAsignado = asignacion.getEsAsignado();
		// TICKET 9000003992
		/*INI Ticket 9*4413*/
		this.dni=correspondencia.getDni();
		this.nombresApellido=correspondencia.getNombresApellido();
		this.ruc=correspondencia.getRuc();
		this.razonSocial=correspondencia.getRazonSocial();
		this.asunto=correspondencia.getAsunto();
		this.tipoCorrespondencia=correspondencia.getTipoCorrespondencia();
		this.numeroDocumento=correspondencia.getNumeroDocumento();
		this.observacion=correspondencia.getObservacion();
		this.codigoCGC=correspondencia.getCgc();
		this.dependenciaDestino=correspondencia.getDependenciaDestino();
		this.estado=correspondencia.getEstado();
		this.motivoRechazo=correspondencia.getMotivoRechazo();
		/*FIN Ticket 9*4413*/
		this.LOGGER.info("[INFO] CorrespondenciaMerge " + " This is info  : Es Asignado " + asignacion.getEsAsignado());
		// System.out.println("Es Asignado:" + asignacion.getEsAsignado());
		this.LOGGER.info("[FIN] CorrespondenciaMerge");
	}

	public Integer getsVersionNum() {
		return sVersionNum;
	}

	public void setsVersionNum(Integer sVersionNum) {
		this.sVersionNum = sVersionNum;
	}

	public String getEstadoAsignacion() {
		return this.estadoAsignacion;
	}

	public void setEstadoAsignacion(String estadoAsignacion) {
		this.estadoAsignacion = estadoAsignacion;
	}

	public String getTipoAsignacion() {
		return this.tipoAsignacion;
	}

	public void setTipoAsignacion(String tipoAsignacion) {
		this.tipoAsignacion = tipoAsignacion;
	}

	public String getDependenciaAsignante() {
		return this.dependenciaAsignante;
	}

	public void setDependenciaAsignante(String dependenciaAsignante) {
		this.dependenciaAsignante = dependenciaAsignante;
	}

	public String getAsignadoPor() {
		return this.asignadoPor;
	}

	public void setAsignadoPor(String asignadoPor) {
		this.asignadoPor = asignadoPor;
	}

	public String getResponsable() {
		return this.responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Date getFechaAsignacion() {
		return this.fechaAsignacion;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	public Date getFechaTope() {
		return this.fechaTope;
	}

	public void setFechaTope(Date fechaTope) {
		this.fechaTope = fechaTope;
	}

	public String getDetalleRequerimineto() {
		return this.detalleRequerimineto;
	}

	public void setDetalleRequerimineto(String detalleRequerimineto) {
		this.detalleRequerimineto = detalleRequerimineto;
	}

	public Double getAvance() {
		return this.avance;
	}

	public void setAvance(Double avance) {
		this.avance = avance;
	}

	public String getTextoRespuesta() {
		return this.textoRespuesta;
	}

	public void setTextoRespuesta(String textoRespuesta) {
		this.textoRespuesta = textoRespuesta;
	}

	public String getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getRemitente() {
		return this.remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	public String getDependenciaDestino() {
		return this.dependenciaDestino;
	}

	public void setDependenciaDestino(String dependenciaDestino) {
		this.dependenciaDestino = dependenciaDestino;
	}

	public String getEstadoCorrespondencia() {
		return this.estadoCorrespondencia;
	}

	public void setEstadoCorrespondencia(String estadoCorrespondencia) {
		this.estadoCorrespondencia = estadoCorrespondencia;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public boolean isNuevaCorrespondencia() {
		return this.nuevaCorrespondencia;
	}

	public void setNuevaCorrespondencia(boolean nuevaCorrespondencia) {
		this.nuevaCorrespondencia = nuevaCorrespondencia;
	}

	public String getFechaAsignacionSTR() {
		return this.fechaAsignacionSTR;
	}

	public String getFechaTopeSTR() {
		return this.fechaTopeSTR;
	}

	public String getFechaCreacionSTR() {
		return this.fechaCreacionSTR;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getCodigoAccion() {
		return this.codigoAccion;
	}

	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}

	public Integer getIdAsignacion() {
		return this.idAsignacion;
	}

	public void setIdAsignacion(Integer idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public Integer getIdPadre() {
		return this.idPadre;
	}

	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}

	public String getCodigoDependencia() {
		return this.codigoDependencia;
	}

	public void setCodigoDependencia(String codigoDependencia) {
		this.codigoDependencia = codigoDependencia;
	}

	// TICKET 9000003514

	public String getTipoTransaccion() {
		return this.tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public String getCorrelativoReferencia() {
		return this.correlativoReferencia;
	}

	public void setCorrelativoReferencia(String correlativoReferencia) {
		this.correlativoReferencia = correlativoReferencia;
	}

	public String getCgc() {
		return this.cgc;
	}

	public void setCgc(String cgc) {
		this.cgc = cgc;
	}

	public String getTipoCorrespondencia() {
		return this.tipoCorrespondencia;
	}

	public void setTipoCorrespondencia(String tipoCorrespondencia) {
		this.tipoCorrespondencia = tipoCorrespondencia;
	}

	public String getNumeroFolios() {
		return this.numeroFolios;
	}

	public void setNumeroFolios(String numeroFolios) {
		this.numeroFolios = numeroFolios;
	}

	public String getLugarTrabajoRemitente() {
		return this.lugarTrabajoRemitente;
	}

	public void setLugarTrabajoRemitente(String lugarTrabajoRemitente) {
		this.lugarTrabajoRemitente = lugarTrabajoRemitente;
	}

	public String getLugarTrabajoDestino() {
		return this.lugarTrabajoDestino;
	}

	public void setLugarTrabajoDestino(String lugarTrabajoDestino) {
		this.lugarTrabajoDestino = lugarTrabajoDestino;
	}

	public String getPersonaDestino() {
		return this.personaDestino;
	}

	public void setPersonaDestino(String personaDestino) {
		this.personaDestino = personaDestino;
	}

	public String getPersonaRemitente() {
		return this.personaRemitente;
	}

	public void setPersonaRemitente(String personaRemitente) {
		this.personaRemitente = personaRemitente;
	}

	public String getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public String getFechaDocumentoSTR() {
		return fechaDocumentoSTR;
	}

	public void setFechaDocumentoSTR(String fechaDocumentoSTR) {
		this.fechaDocumentoSTR = fechaDocumentoSTR;
	}

	public String getUltimaObservacion() {
		return ultimaObservacion;
	}

	public void setUltimaObservacion(String ultimaObservacion) {
		this.ultimaObservacion = ultimaObservacion;
	}

	public String getConfidencial() {
		return confidencial;
	}

	public void setConfidencial(String confidencial) {
		this.confidencial = confidencial;
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

	public String getRegistroEnDependencia() {
		return registroEnDependencia;
	}

	public void setRegistroEnDependencia(String registroEnDependencia) {
		this.registroEnDependencia = registroEnDependencia;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getUrgente() {
		return urgente;
	}

	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}

	public String getEsAsignado() {
		return esAsignado;
	}

	public void setEsAsignado(String esAsignado) {
		this.esAsignado = esAsignado;
	}

	@Override
	public String toString() {
		return "CorrespondenciaMerge [estadoAsignacion=" + estadoAsignacion + ", tipoAsignacion=" + tipoAsignacion
				+ ", codigoAccion=" + codigoAccion + ", dependenciaAsignante=" + dependenciaAsignante + ", asignadoPor="
				+ asignadoPor + ", responsable=" + responsable + ", fechaAsignacion=" + fechaAsignacion + ", fechaTope="
				+ fechaTope + ", detalleRequerimineto=" + detalleRequerimineto + ", avance=" + avance
				+ ", textoRespuesta=" + textoRespuesta + ", idAsignacion=" + idAsignacion + ", correlativo="
				+ correlativo + ", remitente=" + remitente + ", dependenciaDestino=" + dependenciaDestino
				+ ", estadoCorrespondencia=" + estadoCorrespondencia + ", fechaCreacion=" + fechaCreacion
				+ ", numeroDocumento=" + numeroDocumento + ", nuevaCorrespondencia=" + nuevaCorrespondencia
				+ ", fechaAsignacionSTR=" + fechaAsignacionSTR + ", fechaTopeSTR=" + fechaTopeSTR
				+ ", fechaCreacionSTR=" + fechaCreacionSTR + ", asunto=" + asunto + ", idPadre=" + idPadre
				+ ", codigoDependencia=" + codigoDependencia + ", tipoTransaccion=" + tipoTransaccion
				+ ", correlativoReferencia=" + correlativoReferencia + ", cgc=" + cgc + ", tipoCorrespondencia="
				+ tipoCorrespondencia + ", numeroFolios=" + numeroFolios + ", lugarTrabajoRemitente="
				+ lugarTrabajoRemitente + ", lugarTrabajoDestino=" + lugarTrabajoDestino + ", personaDestino="
				+ personaDestino + ", personaRemitente=" + personaRemitente + ", fechaDocumento=" + fechaDocumento
				+ ", fechaDocumentoSTR=" + fechaDocumentoSTR + ", ultimaObservacion=" + ultimaObservacion
				+ ", confidencial=" + confidencial + ", guia=" + guia + ", guiaRemision=" + guiaRemision
				+ ", registroEnDependencia=" + registroEnDependencia + ", proceso=" + proceso + ", urgente=" + urgente
				+ ", esAsignado=" + esAsignado + "]";
	}

	// FIN TICKET 9000003514
	
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
	
	public String getDni() {
		return dni;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public String getNumeroFolio() {
		return numeroFolio;
	}

	public void setNumeroFolio(String numeroFolio) {
		this.numeroFolio = numeroFolio;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getCodigoCGC() {
		return codigoCGC;
	}

	public void setCodigoCGC(String codigoCGC) {
		this.codigoCGC = codigoCGC;
	}

	public String getDependenciadestino() {
		return dependenciadestino;
	}

	public void setDependenciadestino(String dependenciadestino) {
		this.dependenciadestino = dependenciadestino;
	}

	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}
}

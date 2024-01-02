package pe.com.petroperu.model.emision;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.EntidadBase;
import pe.com.petroperu.model.util.SistcorrEstado;
import pe.com.petroperu.util.Constante;

@Entity
@Table(name = "correspondencia")
public class Correspondencia extends EntidadBase implements Serializable {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  @Column(nullable = true, length = 250)
	  private String originador;
	  @Column(nullable = true, length = 25)
	  private String codDependenciaOriginadora;
	  @Column(nullable = true, length = 250)
	  private String dependenciaOriginadora;	  
	  @Column(nullable = false, length = 25)
	  private String codLugarTrabajo;
	  @Column(nullable = false, length = 250)
	  private String lugarTrabajo;
	  @Column(nullable = false, length = 25)
	  private String codDependencia;
	  @Column(nullable = false, length = 250)
	  private String dependencia;
	  @Column(nullable = false, length = 25)
	  private String codRemitente;
	  @Column(nullable = false, length = 250)
	  private String remitente;
	  @Column(nullable = false, length = 25)
	  private String codTipoCorrespondencia;
	  @Column(nullable = false, length = 250)
	  private String tipoCorrespondencia;
	  @Column(nullable = true, length = 50)
	  private String tipoCorrespondenciaOtros;
	  @Temporal(TemporalType.TIMESTAMP)
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Bogota")
	  @Column(nullable = false)
	  private Date fechaDocumento;
	  @Column(nullable = false)
	  private String asunto;
	  @ManyToOne
	  @JoinColumn(name = "id_tipo_emision", nullable = false)
	  private TipoEmision tipoEmision;
	  @Column(nullable = false)
	  private boolean despachoFisico;
	  @Column(nullable = false)
	  private boolean confidencial;
	  @Column(nullable = false)
	  private boolean urgente;
	  @Column(nullable = false)
	  private boolean firmaDigital;
	  @Column(nullable = false)
	  private boolean primerFirmante;
	  @Column(nullable = true, columnDefinition = "text")
	  private String observaciones;
	  @OneToMany(mappedBy = "correspondencia", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
	  private List<ArchivoAdjunto> adjuntos;
	  @OneToMany(mappedBy = "correspondencia", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
	  private List<DestinatarioInterno> detalleInterno;
	  @OneToMany(mappedBy = "correspondencia", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
	  private List<DestinatarioExterno> detalleExterno;
	  @OneToMany(mappedBy = "correspondencia", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
	  private List<DestinatarioCopia> detalleCopia;
	  @OneToMany(mappedBy = "correspondencia", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
	  private List<CorrespondenciaDestDocPagar> detalleCorrespDestDocPagar;//ticket 9000004765
	  @ManyToOne
	  @JoinColumn(name = "id_correlativo", nullable = false)
	  private Correlativo correlativo;
	  @ManyToOne
	  @JoinColumn(name = "id_estado", nullable = false)
	  private CorrespondenciaEstado estado;
	  @Column(nullable = false, length = 25)
	  private String responsable;
	  @Transient
	  private String firma;
	  @Transient
	  private String estadoDescripcion;
	  @Transient
	  private String tipoCorrAbrev;
	  @Column(nullable = true, length = 25)
	  private String fileNetCorrelativo;
		@Column(nullable = true)
		private String usuarioAprueba;
		@JsonIgnore
		@Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
		private Date fechaAprueba;
		@Column(nullable = true)
		private String usuarioEnvio;
		@JsonIgnore
		@Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
		private Date fechaEnvio;
	// TICKET 9000003908
	@Column(nullable = false)
	private Integer nroFlujo;
	// FIN TICKET
		// TICKET 9000003939
		@Column(nullable = false)
		private boolean jerarquia;
		// FIN TICKET
		// TICKET 9000003997
		@Column(nullable = false)
		private Integer nroEnvio;
		@ManyToOne
		@JoinColumn(name = "id_motivo_rechazo", nullable = true)
		private MotivoRechazo motivoRechazo;
		// FIN TICKET
	
	// TICKET 9000003943
	@Column(nullable=false)
	private boolean rutaAprobacion;
	@Transient
	private List<RutaAprobacion> aprobadores;
	// FIN TICKET
	
	// TICKET 9000004044
	@Column(nullable = true)
	private Long idAsignacion;
	@Column(nullable = true)
	private String correlativoEntrada;
	@Column(nullable = true)
	private String tipo;
	@Column(nullable = true)
	private String respuesta;
	@Column(nullable = false)
	private boolean esDocumentoRespuesta;
	@Column(nullable = false)
	private Integer estadoDocumentoRespuesta;
	// FIN TICKET

	//TICKET 9000004496
	@Column(nullable = false, length = 1)
	private String flgEnvio;
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date flgEnvioInicio;
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date flgEnvioFin;
				
	public String getFlgEnvio() {
		return flgEnvio;
	}
	public void setFlgEnvio(String flgEnvio) {
		this.flgEnvio = flgEnvio;
	}

	public Date getFlgEnvioInicio() {
		return flgEnvioInicio;
	}

	public void setFlgEnvioInicio(Date flgEnvioInicio) {
		this.flgEnvioInicio = flgEnvioInicio;
	}

	public Date getFlgEnvioFin() {
		return flgEnvioFin;
	}

	public void setFlgEnvioFin(Date flgEnvioFin) {
		this.flgEnvioFin = flgEnvioFin;
	}
    // FIN TICKET
	
	
	public List<RutaAprobacion> getAprobadores() {
		return aprobadores;
	}

	public void setAprobadores(List<RutaAprobacion> aprobadores) {
		this.aprobadores = aprobadores;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodDependencia() {
		return this.codDependencia;
	}

	public void setCodDependencia(String codDependencia) {
		this.codDependencia = codDependencia;
	}

	public String getCodRemitente() {
		return this.codRemitente;
	}

	public void setCodRemitente(String codRemitente) {
		this.codRemitente = codRemitente;
	}

	public String getCodTipoCorrespondencia() {
		return this.codTipoCorrespondencia;
	}

	public void setCodTipoCorrespondencia(String codTipoCorrespondencia) {
		this.codTipoCorrespondencia = codTipoCorrespondencia;
	}

	public Date getFechaDocumento() {
		return this.fechaDocumento;
	}

	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getDependencia() {
		return this.dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public String getRemitente() {
		return this.remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	public String getTipoCorrespondencia() {
		return this.tipoCorrespondencia;
	}

	public void setTipoCorrespondencia(String tipoCorrespondencia) {
		this.tipoCorrespondencia = tipoCorrespondencia;
	}

	public TipoEmision getTipoEmision() {
		return this.tipoEmision;
	}

	public void setTipoEmision(TipoEmision tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	public boolean isDespachoFisico() {
		return this.despachoFisico;
	}

	public void setDespachoFisico(boolean despachoFisico) {
		this.despachoFisico = despachoFisico;
	}

	public boolean isConfidencial() {
		return this.confidencial;
	}

	public void setConfidencial(boolean confidencial) {
		this.confidencial = confidencial;
	}

	public boolean isUrgente() {
		return this.urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	public boolean isFirmaDigital() {
		return this.firmaDigital;
	}

	public void setFirmaDigital(boolean firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<ArchivoAdjunto> getAdjuntos() {
		return this.adjuntos;
	}

	public void setAdjuntos(List<ArchivoAdjunto> adjuntos) {
		this.adjuntos = adjuntos;
	}

	public List<DestinatarioInterno> getDetalleInterno() {
		return this.detalleInterno;
	}

	public void setDetalleInterno(List<DestinatarioInterno> detalleInterno) {
		this.detalleInterno = detalleInterno;
	}

	public List<DestinatarioExterno> getDetalleExterno() {
		return this.detalleExterno;
	}

	public void setDetalleExterno(List<DestinatarioExterno> detalleExterno) {
		this.detalleExterno = detalleExterno;
	}

	public List<CorrespondenciaDestDocPagar> getDetalleCorrespDestDocPagar() {
		return detalleCorrespDestDocPagar;
	}

	public void setDetalleCorrespDestDocPagar(List<CorrespondenciaDestDocPagar> detalleCorrespDestDocPagar) {
		this.detalleCorrespDestDocPagar = detalleCorrespDestDocPagar;
	}

	public List<DestinatarioCopia> getDetalleCopia() {
		return this.detalleCopia;
	}

	public void setDetalleCopia(List<DestinatarioCopia> detalleCopia) {
		this.detalleCopia = detalleCopia;
	}

	public Correlativo getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(Correlativo correlativo) {
		this.correlativo = correlativo;
	}

	public String getCodLugarTrabajo() {
		return this.codLugarTrabajo;
	}

	public void setCodLugarTrabajo(String codLugarTrabajo) {
		this.codLugarTrabajo = codLugarTrabajo;
	}

	public String getLugarTrabajo() {
		return this.lugarTrabajo;
	}

	public void setLugarTrabajo(String lugarTrabajo) {
		this.lugarTrabajo = lugarTrabajo;
	}

	public CorrespondenciaEstado getEstado() {
		return this.estado;
	}

	public void setEstado(CorrespondenciaEstado estado) {
		this.estado = estado;
	}

	public String getFileNetCorrelativo() {
		return this.fileNetCorrelativo;
	}

	public void setFileNetCorrelativo(String fileNetCorrelativo) {
		this.fileNetCorrelativo = fileNetCorrelativo;
	}

	public String getTipoCorrespondenciaOtros() {
		return this.tipoCorrespondenciaOtros;
	}

	public void setTipoCorrespondenciaOtros(String tipoCorrespondenciaOtros) {
		this.tipoCorrespondenciaOtros = tipoCorrespondenciaOtros;
	}

	public String getResponsable() {
		return this.responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public boolean isPrimerFirmante() {
		return this.primerFirmante;
	}

	public void setPrimerFirmante(boolean primerFirmante) {
		this.primerFirmante = primerFirmante;
	}

	public String getFirma() {
		if(this.firmaDigital) {
			firma = "DIGITAL";
		} else {
			firma = "MANUAL";
		}
		return firma;
	}
	public String getEstadoDescripcion() {
		estadoDescripcion = this.estado.getDescripcionEstado();
		return estadoDescripcion;
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
			default:
				tipoCorrAbrev = "XX";
				break;
			}
		return tipoCorrAbrev;
	}

	public String getCodDependenciaOriginadora() {
		return codDependenciaOriginadora;
	}

	public void setCodDependenciaOriginadora(String codDependenciaOriginadora) {
		this.codDependenciaOriginadora = codDependenciaOriginadora;
	}

	public String getDependenciaOriginadora() {
		return dependenciaOriginadora;
	}

	public void setDependenciaOriginadora(String dependenciaOriginadora) {
		this.dependenciaOriginadora = dependenciaOriginadora;
	}

	public String getOriginador() {
		return originador;
	}

	public void setOriginador(String originador) {
		this.originador = originador;
	}

	public String getUsuarioAprueba() {
		return usuarioAprueba;
	}

	public void setUsuarioAprueba(String usuarioAprueba) {
		this.usuarioAprueba = usuarioAprueba;
	}

	public Date getFechaAprueba() {
		return fechaAprueba;
	}

	public void setFechaAprueba(Date fechaAprueba) {
		this.fechaAprueba = fechaAprueba;
	}

	public String getUsuarioEnvio() {
		return usuarioEnvio;
	}

	public void setUsuarioEnvio(String usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	// TICKET 9000003908
	public Integer getNroFlujo() {
		return nroFlujo;
	}

	public void setNroFlujo(Integer nroFlujo) {
		this.nroFlujo = nroFlujo;
	}
	// FIN TICKET

	// TICKET 9000003939
	public boolean isJerarquia() {
		return jerarquia;
	}

	public void setJerarquia(boolean jerarquia) {
		this.jerarquia = jerarquia;
	}
	// FIN TICKET

	// TICKET 9000003943
	public boolean isRutaAprobacion() {
		return rutaAprobacion;
	}

	public void setRutaAprobacion(boolean rutaAprobacion) {
		this.rutaAprobacion = rutaAprobacion;
	}
	// FIN TICKET 9000003943
	
	public Integer getNroEnvio(){
		return this.nroEnvio;
	}
	
	public void setNroEnvio(Integer nroEnvio){
		this.nroEnvio = nroEnvio;
	}

	public MotivoRechazo getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(MotivoRechazo motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	public Long getIdAsignacion() {
		return idAsignacion;
	}

	public void setIdAsignacion(Long idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public String getCorrelativoEntrada() {
		return correlativoEntrada;
	}

	public void setCorrelativoEntrada(String correlativoEntrada) {
		this.correlativoEntrada = correlativoEntrada;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public boolean isEsDocumentoRespuesta() {
		return esDocumentoRespuesta;
	}

	public void setEsDocumentoRespuesta(boolean esDocumentoRespuesta) {
		this.esDocumentoRespuesta = esDocumentoRespuesta;
	}

	public Integer getEstadoDocumentoRespuesta() {
		return estadoDocumentoRespuesta;
	}

	public void setEstadoDocumentoRespuesta(Integer estadoDocumentoRespuesta) {
		this.estadoDocumentoRespuesta = estadoDocumentoRespuesta;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adjuntos == null) ? 0 : adjuntos.hashCode());
		result = prime * result + ((aprobadores == null) ? 0 : aprobadores.hashCode());
		result = prime * result + ((asunto == null) ? 0 : asunto.hashCode());
		result = prime * result + ((codDependencia == null) ? 0 : codDependencia.hashCode());
		result = prime * result + ((codDependenciaOriginadora == null) ? 0 : codDependenciaOriginadora.hashCode());
		result = prime * result + ((codLugarTrabajo == null) ? 0 : codLugarTrabajo.hashCode());
		result = prime * result + ((codRemitente == null) ? 0 : codRemitente.hashCode());
		result = prime * result + ((codTipoCorrespondencia == null) ? 0 : codTipoCorrespondencia.hashCode());
		result = prime * result + (confidencial ? 1231 : 1237);
		result = prime * result + ((correlativo == null) ? 0 : correlativo.hashCode());
		result = prime * result + ((correlativoEntrada == null) ? 0 : correlativoEntrada.hashCode());
		result = prime * result + ((dependencia == null) ? 0 : dependencia.hashCode());
		result = prime * result + ((dependenciaOriginadora == null) ? 0 : dependenciaOriginadora.hashCode());
		result = prime * result + (despachoFisico ? 1231 : 1237);
		result = prime * result + ((detalleCopia == null) ? 0 : detalleCopia.hashCode());
		result = prime * result + ((detalleExterno == null) ? 0 : detalleExterno.hashCode());
		result = prime * result + ((detalleInterno == null) ? 0 : detalleInterno.hashCode());
		result = prime * result + (esDocumentoRespuesta ? 1231 : 1237);
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((estadoDescripcion == null) ? 0 : estadoDescripcion.hashCode());
		result = prime * result + ((estadoDocumentoRespuesta == null) ? 0 : estadoDocumentoRespuesta.hashCode());
		result = prime * result + ((fechaAprueba == null) ? 0 : fechaAprueba.hashCode());
		result = prime * result + ((fechaDocumento == null) ? 0 : fechaDocumento.hashCode());
		result = prime * result + ((fechaEnvio == null) ? 0 : fechaEnvio.hashCode());
		result = prime * result + ((fileNetCorrelativo == null) ? 0 : fileNetCorrelativo.hashCode());
		result = prime * result + ((firma == null) ? 0 : firma.hashCode());
		result = prime * result + (firmaDigital ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idAsignacion == null) ? 0 : idAsignacion.hashCode());
		result = prime * result + (jerarquia ? 1231 : 1237);
		result = prime * result + ((lugarTrabajo == null) ? 0 : lugarTrabajo.hashCode());
		result = prime * result + ((motivoRechazo == null) ? 0 : motivoRechazo.hashCode());
		result = prime * result + ((nroEnvio == null) ? 0 : nroEnvio.hashCode());
		result = prime * result + ((nroFlujo == null) ? 0 : nroFlujo.hashCode());
		result = prime * result + ((observaciones == null) ? 0 : observaciones.hashCode());
		result = prime * result + ((originador == null) ? 0 : originador.hashCode());
		result = prime * result + (primerFirmante ? 1231 : 1237);
		result = prime * result + ((remitente == null) ? 0 : remitente.hashCode());
		result = prime * result + ((responsable == null) ? 0 : responsable.hashCode());
		result = prime * result + ((respuesta == null) ? 0 : respuesta.hashCode());
		result = prime * result + (rutaAprobacion ? 1231 : 1237);
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((tipoCorrAbrev == null) ? 0 : tipoCorrAbrev.hashCode());
		result = prime * result + ((tipoCorrespondencia == null) ? 0 : tipoCorrespondencia.hashCode());
		result = prime * result + ((tipoCorrespondenciaOtros == null) ? 0 : tipoCorrespondenciaOtros.hashCode());
		result = prime * result + ((tipoEmision == null) ? 0 : tipoEmision.hashCode());
		result = prime * result + (urgente ? 1231 : 1237);
		result = prime * result + ((usuarioAprueba == null) ? 0 : usuarioAprueba.hashCode());
		result = prime * result + ((usuarioEnvio == null) ? 0 : usuarioEnvio.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Correspondencia other = (Correspondencia) obj;
		if (adjuntos == null) {
			if (other.adjuntos != null)
				return false;
		} else if (!adjuntos.equals(other.adjuntos))
			return false;
		if (aprobadores == null) {
			if (other.aprobadores != null)
				return false;
		} else if (!aprobadores.equals(other.aprobadores))
			return false;
		if (asunto == null) {
			if (other.asunto != null)
				return false;
		} else if (!asunto.equals(other.asunto))
			return false;
		if (codDependencia == null) {
			if (other.codDependencia != null)
				return false;
		} else if (!codDependencia.equals(other.codDependencia))
			return false;
		if (codDependenciaOriginadora == null) {
			if (other.codDependenciaOriginadora != null)
				return false;
		} else if (!codDependenciaOriginadora.equals(other.codDependenciaOriginadora))
			return false;
		if (codLugarTrabajo == null) {
			if (other.codLugarTrabajo != null)
				return false;
		} else if (!codLugarTrabajo.equals(other.codLugarTrabajo))
			return false;
		if (codRemitente == null) {
			if (other.codRemitente != null)
				return false;
		} else if (!codRemitente.equals(other.codRemitente))
			return false;
		if (codTipoCorrespondencia == null) {
			if (other.codTipoCorrespondencia != null)
				return false;
		} else if (!codTipoCorrespondencia.equals(other.codTipoCorrespondencia))
			return false;
		if (confidencial != other.confidencial)
			return false;
		if (correlativo == null) {
			if (other.correlativo != null)
				return false;
		} else if (!correlativo.equals(other.correlativo))
			return false;
		if (correlativoEntrada == null) {
			if (other.correlativoEntrada != null)
				return false;
		} else if (!correlativoEntrada.equals(other.correlativoEntrada))
			return false;
		if (dependencia == null) {
			if (other.dependencia != null)
				return false;
		} else if (!dependencia.equals(other.dependencia))
			return false;
		if (dependenciaOriginadora == null) {
			if (other.dependenciaOriginadora != null)
				return false;
		} else if (!dependenciaOriginadora.equals(other.dependenciaOriginadora))
			return false;
		if (despachoFisico != other.despachoFisico)
			return false;
		if (detalleCopia == null) {
			if (other.detalleCopia != null)
				return false;
		} else if (!detalleCopia.equals(other.detalleCopia))
			return false;
		if (detalleExterno == null) {
			if (other.detalleExterno != null)
				return false;
		} else if (!detalleExterno.equals(other.detalleExterno))
			return false;
		if (detalleInterno == null) {
			if (other.detalleInterno != null)
				return false;
		} else if (!detalleInterno.equals(other.detalleInterno))
			return false;
		if (esDocumentoRespuesta != other.esDocumentoRespuesta)
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (estadoDescripcion == null) {
			if (other.estadoDescripcion != null)
				return false;
		} else if (!estadoDescripcion.equals(other.estadoDescripcion))
			return false;
		if (estadoDocumentoRespuesta == null) {
			if (other.estadoDocumentoRespuesta != null)
				return false;
		} else if (!estadoDocumentoRespuesta.equals(other.estadoDocumentoRespuesta))
			return false;
		if (fechaAprueba == null) {
			if (other.fechaAprueba != null)
				return false;
		} else if (!fechaAprueba.equals(other.fechaAprueba))
			return false;
		if (fechaDocumento == null) {
			if (other.fechaDocumento != null)
				return false;
		} else if (!fechaDocumento.equals(other.fechaDocumento))
			return false;
		if (fechaEnvio == null) {
			if (other.fechaEnvio != null)
				return false;
		} else if (!fechaEnvio.equals(other.fechaEnvio))
			return false;
		if (fileNetCorrelativo == null) {
			if (other.fileNetCorrelativo != null)
				return false;
		} else if (!fileNetCorrelativo.equals(other.fileNetCorrelativo))
			return false;
		if (firma == null) {
			if (other.firma != null)
				return false;
		} else if (!firma.equals(other.firma))
			return false;
		if (firmaDigital != other.firmaDigital)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idAsignacion == null) {
			if (other.idAsignacion != null)
				return false;
		} else if (!idAsignacion.equals(other.idAsignacion))
			return false;
		if (jerarquia != other.jerarquia)
			return false;
		if (lugarTrabajo == null) {
			if (other.lugarTrabajo != null)
				return false;
		} else if (!lugarTrabajo.equals(other.lugarTrabajo))
			return false;
		if (motivoRechazo == null) {
			if (other.motivoRechazo != null)
				return false;
		} else if (!motivoRechazo.equals(other.motivoRechazo))
			return false;
		if (nroEnvio == null) {
			if (other.nroEnvio != null)
				return false;
		} else if (!nroEnvio.equals(other.nroEnvio))
			return false;
		if (nroFlujo == null) {
			if (other.nroFlujo != null)
				return false;
		} else if (!nroFlujo.equals(other.nroFlujo))
			return false;
		if (observaciones == null) {
			if (other.observaciones != null)
				return false;
		} else if (!observaciones.equals(other.observaciones))
			return false;
		if (originador == null) {
			if (other.originador != null)
				return false;
		} else if (!originador.equals(other.originador))
			return false;
		if (primerFirmante != other.primerFirmante)
			return false;
		if (remitente == null) {
			if (other.remitente != null)
				return false;
		} else if (!remitente.equals(other.remitente))
			return false;
		if (responsable == null) {
			if (other.responsable != null)
				return false;
		} else if (!responsable.equals(other.responsable))
			return false;
		if (respuesta == null) {
			if (other.respuesta != null)
				return false;
		} else if (!respuesta.equals(other.respuesta))
			return false;
		if (rutaAprobacion != other.rutaAprobacion)
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (tipoCorrAbrev == null) {
			if (other.tipoCorrAbrev != null)
				return false;
		} else if (!tipoCorrAbrev.equals(other.tipoCorrAbrev))
			return false;
		if (tipoCorrespondencia == null) {
			if (other.tipoCorrespondencia != null)
				return false;
		} else if (!tipoCorrespondencia.equals(other.tipoCorrespondencia))
			return false;
		if (tipoCorrespondenciaOtros == null) {
			if (other.tipoCorrespondenciaOtros != null)
				return false;
		} else if (!tipoCorrespondenciaOtros.equals(other.tipoCorrespondenciaOtros))
			return false;
		if (tipoEmision == null) {
			if (other.tipoEmision != null)
				return false;
		} else if (!tipoEmision.equals(other.tipoEmision))
			return false;
		if (urgente != other.urgente)
			return false;
		if (usuarioAprueba == null) {
			if (other.usuarioAprueba != null)
				return false;
		} else if (!usuarioAprueba.equals(other.usuarioAprueba))
			return false;
		if (usuarioEnvio == null) {
			if (other.usuarioEnvio != null)
				return false;
		} else if (!usuarioEnvio.equals(other.usuarioEnvio))
			return false;
		return true;
	}
	
}

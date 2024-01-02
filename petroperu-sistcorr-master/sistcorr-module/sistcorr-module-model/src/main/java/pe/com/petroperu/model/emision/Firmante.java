package pe.com.petroperu.model.emision;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import pe.com.petroperu.Semaforo;
import pe.com.petroperu.model.EntidadBase;
import pe.com.petroperu.util.Constante;

@Entity
@Table(name = "firmante")
public class Firmante extends EntidadBase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_correspondencia", nullable = false)
	private Correspondencia correspondencia;
	private String codFirmante;
	@Column(nullable = false, length = 250)
	private String nombreFirmante;
	@Column(nullable = false, length = 25)
	private String codDependenciaFirmante;
	@Column(nullable = false, length = 250)
	private String dependenciaFirmante;
	@Column(nullable = false, length = 250)
	private String puestoFirmante;
	@Column(nullable = false, length = 25)
	private String codLugarTrabajoFirmante;
	@Column(nullable = false, length = 250)
	private String lugarTrabajoFirmante;
	@ManyToOne
	@JoinColumn(name = "id_estado", nullable = false)
	private CorrespondenciaEstado estado;
	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Bogota")
	//TICKET 9000003996
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Bogota")
	@Column(nullable = false)
	private Date fecha;
	@Column(nullable = false, length = 25)
	private String solicitante;
	@ManyToOne
	@JoinColumn(name = "id_motivo_rechazo", nullable = true)
	private MotivoRechazo motivoRechazo;
	@Column(nullable = true, length = 250)
	private String descripcionMotivoRechazo;
	// TICKET 9000003908
	@Column(nullable = false)
	private Integer nroFlujo;
	// FIN TICKET
	@Transient
	private String color;
	@Transient
	private String estadoDescripcion;
	// TICKET 9000004409
	private String codFirmanteReemplazado;
	private String nomFirmanteReemplazado;
	
	public String getCodFirmanteReemplazado() {
		return codFirmanteReemplazado;
	}

	public void setCodFirmanteReemplazado(String codFirmanteReemplazado) {
		this.codFirmanteReemplazado = codFirmanteReemplazado;
	}

	public String getNomFirmanteReemplazado() {
		return nomFirmanteReemplazado;
	}

	public void setNomFirmanteReemplazado(String nomFirmanteReemplazado) {
		this.nomFirmanteReemplazado = nomFirmanteReemplazado;
	}

	// FIN TICKET
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Correspondencia getCorrespondencia() {
		return this.correspondencia;
	}

	public void setCorrespondencia(Correspondencia correspondencia) {
		this.correspondencia = correspondencia;
	}

	public String getCodFirmante() {
		return this.codFirmante;
	}

	public void setCodFirmante(String codFirmante) {
		this.codFirmante = codFirmante;
	}

	public String getNombreFirmante() {
		return this.nombreFirmante;
	}

	public void setNombreFirmante(String nombreFirmante) {
		this.nombreFirmante = nombreFirmante;
	}

	public String getCodDependenciaFirmante() {
		return this.codDependenciaFirmante;
	}

	public void setCodDependenciaFirmante(String codDependenciaFirmante) {
		this.codDependenciaFirmante = codDependenciaFirmante;
	}

	public String getDependenciaFirmante() {
		return this.dependenciaFirmante;
	}

	public void setDependenciaFirmante(String dependenciaFirmante) {
		this.dependenciaFirmante = dependenciaFirmante;
	}

	public String getPuestoFirmante() {
		return this.puestoFirmante;
	}

	public void setPuestoFirmante(String puestoFirmante) {
		this.puestoFirmante = puestoFirmante;
	}

	public CorrespondenciaEstado getEstado() {
		return this.estado;
	}

	public void setEstado(CorrespondenciaEstado estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCodLugarTrabajoFirmante() {
		return this.codLugarTrabajoFirmante;
	}

	public void setCodLugarTrabajoFirmante(String codLugarTrabajoFirmante) {
		this.codLugarTrabajoFirmante = codLugarTrabajoFirmante;
	}

	public String getLugarTrabajoFirmante() {
		return this.lugarTrabajoFirmante;
	}

	public void setLugarTrabajoFirmante(String lugarTrabajoFirmante) {
		this.lugarTrabajoFirmante = lugarTrabajoFirmante;
	}

	public String getSolicitante() {
		return this.solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public MotivoRechazo getMotivoRechazo() {
		return this.motivoRechazo;
	}

	public void setMotivoRechazo(MotivoRechazo motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	public String getColor() {
		if (Constante.CORRESPONDENCIA_ASIGNADA.equals(this.estado.getId())) {
			this.color = Semaforo.AMARILLO.name();
		}
		if (Constante.CORRESPONDENCIA_FIRMADA.equals(this.estado.getId())) {
			this.color = Semaforo.VERDE.name();
		}
		if (Constante.CORRESPONDENCIA_COMPLETADA.equals(this.estado.getId())) {
			this.color = Semaforo.VERDE.name();
		}
		if (Constante.CORRESPONDENCIA_ANULADA.equals(this.estado.getId())) {
			this.color = Semaforo.ROJO.name();
		}
		if (Constante.CORRESPONDENCIA_POR_CORREGIR.equals(this.estado.getId())) {
			this.color = Semaforo.ROJO.name();
		}
		return this.color;
	}

	public String getEstadoDescripcion() {
		this.estadoDescripcion = this.estado.getDescripcionEstado();
		return this.estadoDescripcion;
	}

	public String getDescripcionMotivoRechazo() {
		descripcionMotivoRechazo = descripcionMotivoRechazo == null ? "" : descripcionMotivoRechazo;
		return descripcionMotivoRechazo;
	}

	public void setDescripcionMotivoRechazo(String descripcionMotivoRechazo) {
		this.descripcionMotivoRechazo = descripcionMotivoRechazo;
	}
	
	// TICKET 9000003908

	public Integer getNroFlujo() {
		return nroFlujo;
	}

	public void setNroFlujo(Integer nroFlujo) {
		this.nroFlujo = nroFlujo;
	}
	
	// FIN TICKET
	
}

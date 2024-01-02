package pe.com.petroperu.model.emision;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name="correspondencia_compartida")
public class CorrespondenciaCompartida extends EntidadBase implements Serializable {

	private static final long serialVersionUID = -391046393070314366L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false, length=20)
	private String modoCompartido;
	@Column(nullable=false, length=500)
	private String destinatarios;
	@Column(nullable=false, length=500)
	private String copias;
	@Column(nullable=false, length=300)
	private String asunto;
	@Column(nullable=false)
	private String contenido;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idCorrespondencia", nullable=false)
	private Correspondencia correspondencia;
	@Transient
	private String correlativo;
	@Transient
	private Long idCorrespondencia;
	
	public CorrespondenciaCompartida(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModoCompartido() {
		return modoCompartido;
	}

	public void setModoCompartido(String modoCompartido) {
		this.modoCompartido = modoCompartido;
	}

	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}

	public String getCopias() {
		return copias;
	}

	public void setCopias(String copias) {
		this.copias = copias;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Correspondencia getCorrespondencia() {
		return correspondencia;
	}

	public void setCorrespondencia(Correspondencia correspondencia) {
		this.correspondencia = correspondencia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public Long getIdCorrespondencia() {
		return idCorrespondencia;
	}

	public void setIdCorrespondencia(Long idCorrespondencia) {
		this.idCorrespondencia = idCorrespondencia;
	}

	@Override
	public String toString() {
		return "CorrespondenciaCompartida [id=" + id + ", modoCompartido=" + modoCompartido + ", destinatarios="
				+ destinatarios + ", copias=" + copias + ", asunto=" + asunto + ", contenido=" + contenido
				+ ", correspondencia=" + correspondencia + ", correlativo=" + correlativo + ", idCorrespondencia="
				+ idCorrespondencia + "]";
	}
		
}

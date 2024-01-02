package pe.com.petroperu.model.emision;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "destinatario_respuesta")
public class DestinatarioRespuesta extends EntidadBase implements Serializable {

	private static final long serialVersionUID = -275254613169046375L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idCorrespondencia", nullable = false)
	private Correspondencia correspondencia;
	
	@Column(nullable = false, length = 25)
	private String codDependencia;
	
	@Column(nullable = false)
	private Integer nroEnvio;
	
	@Column(nullable = false, length = 25)
	private String estado;
	
	@Column(nullable = false, length = 25)
	private String correlativo;
	
	@Column(nullable = true, length = 255)
	private String descripcionRechazo;
	
	@Column(nullable = true, length = 255)
	private String nombreUsuario;
	
	@Transient
	private String color;
	
	@Transient
	private String nombreDependencia;

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

	public Correspondencia getCorrespondencia() {
		return this.correspondencia;
	}

	public void setCorrespondencia(Correspondencia correspondencia) {
		this.correspondencia = correspondencia;
	}

	public Integer getNroEnvio() {
		return nroEnvio;
	}

	public void setNroEnvio(Integer nroEnvio) {
		this.nroEnvio = nroEnvio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getDescripcionRechazo() {
		return descripcionRechazo;
	}

	public void setDescripcionRechazo(String descripcionRechazo) {
		this.descripcionRechazo = descripcionRechazo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNombreDependencia() {
		return nombreDependencia;
	}

	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
}

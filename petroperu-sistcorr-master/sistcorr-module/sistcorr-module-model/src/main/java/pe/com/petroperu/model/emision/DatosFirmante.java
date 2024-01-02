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

import com.fasterxml.jackson.annotation.JsonIgnore;

import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "datos_firmante")
public class DatosFirmante extends EntidadBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_correspondencia", nullable = false)
	private Correspondencia correspondencia;
	@Column(nullable = false, length = 50)
	private String codFirmante;
	@Column(nullable = false, length = 50)
	private String solicitante;
	@Column(nullable = true, length = 50)
	private String motivoRechazo;
	@Column(nullable = true, length = 50)
	private String codFirmantePrevio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Correspondencia getCorrespondencia() {
		return correspondencia;
	}

	public void setCorrespondencia(Correspondencia correspondencia) {
		this.correspondencia = correspondencia;
	}

	public String getCodFirmante() {
		return codFirmante;
	}

	public void setCodFirmante(String codFirmante) {
		this.codFirmante = codFirmante;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	public String getCodFirmantePrevio() {
		return codFirmantePrevio;
	}

	public void setCodFirmantePrevio(String codFirmantePrevio) {
		this.codFirmantePrevio = codFirmantePrevio;
	}

}

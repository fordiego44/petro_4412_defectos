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
import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "destinatario_interno")
public class DestinatarioInterno extends EntidadBase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 25)
	private String codLugarTrabajo;
	@Column(nullable = false, length = 250)
	private String lugarTrabajo;
	@Column(nullable = false, length = 25)
	private String codDependencia;
	@Column(nullable = false, length = 250)
	private String dependencia;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_correspondencia", nullable = false)
	private Correspondencia correspondencia;
	@Column(nullable = true, length = 25)
	private String fileNetCorrelativo;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCodDependencia() {
		return this.codDependencia;
	}

	public void setCodDependencia(String codDependencia) {
		this.codDependencia = codDependencia;
	}

	public String getDependencia() {
		return this.dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public Correspondencia getCorrespondencia() {
		return this.correspondencia;
	}

	public void setCorrespondencia(Correspondencia correspondencia) {
		this.correspondencia = correspondencia;
	}

	public String getFileNetCorrelativo() {
		return this.fileNetCorrelativo;
	}

	public void setFileNetCorrelativo(String fileNetCorrelativo) {
		this.fileNetCorrelativo = fileNetCorrelativo;
	}
}

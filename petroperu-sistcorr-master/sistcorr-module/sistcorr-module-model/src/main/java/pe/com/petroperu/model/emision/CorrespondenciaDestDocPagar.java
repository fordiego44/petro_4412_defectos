package pe.com.petroperu.model.emision;

import java.io.Serializable;

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
@Table(name = "correspondencia_dest_doc_pagar")
public class CorrespondenciaDestDocPagar extends EntidadBase implements Serializable {

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
	@ManyToOne
	@JoinColumn(name = "id_dest_doc_pagar", nullable = false)
	private DestinatarioDocPagar destinarioDocPagar;
	
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
	public DestinatarioDocPagar getDestinarioDocPagar() {
		return destinarioDocPagar;
	}
	public void setDestinarioDocPagar(DestinatarioDocPagar destinarioDocPagar) {
		this.destinarioDocPagar = destinarioDocPagar;
	}
	
}

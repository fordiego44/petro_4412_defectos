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
@Table(name="historial_archivo")
public class HistorialArchivo extends EntidadBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8944750551018137720L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idCorrespondenciaCompartida", nullable=false)
	private CorrespondenciaCompartida correspondenciaCompartida;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idArchivo", nullable=false)
	private ArchivoCompartido archivoCompartido;
	
	public HistorialArchivo(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CorrespondenciaCompartida getCorrespondenciaCompartida() {
		return correspondenciaCompartida;
	}

	public void setCorrespondenciaCompartida(CorrespondenciaCompartida correspondenciaCompartida) {
		this.correspondenciaCompartida = correspondenciaCompartida;
	}

	public ArchivoCompartido getArchivoCompartido() {
		return archivoCompartido;
	}

	public void setArchivoCompartido(ArchivoCompartido archivoCompartido) {
		this.archivoCompartido = archivoCompartido;
	}
	
}

package pe.com.petroperu.model.emision;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GenerationType;

import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name="archivo_compartido")
public class ArchivoCompartido extends EntidadBase implements Serializable {
	
	private static final long serialVersionUID = -3782290646698384750L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false, length=255)
	private String clave;
	@Column(nullable=false)
	private boolean compartido;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idArchivo", nullable=false)
	private ArchivoAdjunto archivo;
	
	public ArchivoCompartido(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public boolean isCompartido() {
		return compartido;
	}

	public void setCompartido(boolean compartido) {
		this.compartido = compartido;
	}

	public ArchivoAdjunto getArchivo() {
		return archivo;
	}

	public void setArchivo(ArchivoAdjunto archivo) {
		this.archivo = archivo;
	}
	
}

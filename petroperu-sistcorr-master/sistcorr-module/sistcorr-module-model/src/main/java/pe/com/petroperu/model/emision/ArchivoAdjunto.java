package pe.com.petroperu.model.emision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
import javax.persistence.Transient;

import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "archivo_adjunto")
public class ArchivoAdjunto extends EntidadBase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 250)
	private String nombre;
	@Column(nullable = false, length = 50)
	private String nombreServidor;
	@Column(nullable = false, length = 5)
	private String extension;
	@Column(nullable = false)
	private boolean principal;
	@JsonIgnore
	@Column(nullable = false, columnDefinition = "text")
	private String ubicacion;
	@Column(nullable = false)
	private String contentType;
	@Column(precision = 5, scale = 2)
	private Double tamanio;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_correspondencia", nullable = false)
	private Correspondencia correspondencia;
	@Column(nullable = true, length = 80)
	private String fileNetID;
	@Transient
	private String key;
	
	//INICIO TICKET 9000003934
	@JsonIgnore
	@OneToMany(mappedBy = "archivo", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<ArchivoCompartido> archivoCompartido;
	
	public List<ArchivoCompartido> getArchivoCompartido() {
		return archivoCompartido;
	}

	public void setArchivoCompartido(List<ArchivoCompartido> archivoCompartido) {
		this.archivoCompartido = archivoCompartido;
	}
	//FIN TICKET 9000003934
	
	// TICKET 9000004510
	@Column(nullable=false)
	private String indicadorRemoto;
	// FIN TICKET

	
	public ArchivoAdjunto() {
	}

	public ArchivoAdjunto(Long idArchivoAdjunto) {
		this.id = idArchivoAdjunto;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreServidor() {
		return this.nombreServidor;
	}

	public void setNombreServidor(String nombreServidor) {
		this.nombreServidor = nombreServidor;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public boolean isPrincipal() {
		return this.principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public String getUbicacion() {
		return this.ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Correspondencia getCorrespondencia() {
		return this.correspondencia;
	}

	public void setCorrespondencia(Correspondencia correspondencia) {
		this.correspondencia = correspondencia;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Double getTamanio() {
		return this.tamanio;
	}

	public void setTamanio(Double tamanio) {
		this.tamanio = tamanio;
	}

	public String getFileNetID() {
		return this.fileNetID;
	}

	public void setFileNetID(String fileNetID) {
		this.fileNetID = fileNetID;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public void setKey(String key){
		this.key = key;
	}

	public String getIndicadorRemoto() {
		return indicadorRemoto;
	}

	public void setIndicadorRemoto(String indicadorRemoto) {
		this.indicadorRemoto = indicadorRemoto;
	}
	
}

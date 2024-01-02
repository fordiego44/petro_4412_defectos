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

import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "ruta_aprobacion")
public class RutaAprobacion extends EntidadBase implements Serializable {
	
	private static final long serialVersionUID = -6999910223532697301L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 25)
	private String tipoFirmante;
	@Column(nullable = false, length = 255)
	private String codDependencia;
	@Column(nullable = false, length = 255)
	private String dependenciaNombre;
	@Column(nullable = false, length = 25)
	private String usuario;
	@Column(nullable = false, length = 255)
	private String usuarioNombre;
	@ManyToOne
	@JoinColumn(name = "id_firmante", nullable = true)
	private Firmante firmante;
	@Column(nullable = false)
	private Integer orden;
	@ManyToOne
	@JoinColumn(name = "id_correspondencia", nullable = false)
	private Correspondencia correspondencia;
	@Transient
	private String nombreTipo;
	@Transient
	private String estado;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoFirmante() {
		return tipoFirmante;
	}

	public void setTipoFirmante(String tipoFirmante) {
		this.tipoFirmante = tipoFirmante;
	}

	public String getCodDependencia() {
		return codDependencia;
	}

	public void setCodDependencia(String codDependencia) {
		this.codDependencia = codDependencia;
	}

	public String getDependenciaNombre() {
		return dependenciaNombre;
	}

	public void setDependenciaNombre(String dependenciaNombre) {
		this.dependenciaNombre = dependenciaNombre;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public Firmante getFirmante() {
		return firmante;
	}

	public void setFirmante(Firmante firmante) {
		this.firmante = firmante;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Correspondencia getCorrespondencia() {
		return correspondencia;
	}

	public void setCorrespondencia(Correspondencia correspondencia) {
		this.correspondencia = correspondencia;
	}

	public String getNombreTipo() {
		return nombreTipo;
	}

	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "RutaAprobacion [id=" + id + ", tipoFirmante=" + tipoFirmante + ", codDependencia=" + codDependencia
				+ ", dependenciaNombre=" + dependenciaNombre + ", usuario=" + usuario + ", usuarioNombre="
				+ usuarioNombre + ", firmante=" + firmante + ", orden=" + orden + ", correspondencia=" + correspondencia
				+ ", nombreTipo=" + nombreTipo + ", estado=" + estado + "]";
	}
	
	
}

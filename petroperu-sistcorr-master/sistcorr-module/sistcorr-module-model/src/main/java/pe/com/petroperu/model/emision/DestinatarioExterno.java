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
@Table(name = "destinatario_externo")
public class DestinatarioExterno extends EntidadBase implements Serializable {
	private static final long serialVersionUID = -2693055338817507499L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private boolean nacional;
	@Column(length = 25)
	private String codPais;
	private String pais;
	@Column(length = 25)
	private String codDepartamento;
	private String departamento;
	@Column(length = 25)
	private String codProvincia;
	private String provincia;
	@Column(length = 25)
	private String codDistrito;
	private String distrito;
	private String direccion;
	private String dependenciaInternacional;
	private String codDependenciaNacional;
	private String dependenciaNacional;
	private String nombreDestinatario;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_correspondencia", nullable = false)
	private Correspondencia correspondencia;
	@Transient
	private String dependencia;
	@Transient
	private String lugar;
	@Column(nullable = true, length = 25)
	private String fileNetCorrelativo;
	
	private Boolean esEntidadExternaConRuc;//TICKET 9000003934
	private Character tipoDestinatario;//TICKET 9000003934 X: Entidad externa; P: Persona natural
	private Boolean esDespachoFisicoDestExterno;//TICKET 9000003934
	private Boolean esCorreoElectronicoDestExterno;//TICKET 9000003934
	private String correoDestinatario;//TICKET 9000003934

	public String getCorreoDestinatario() {
		return correoDestinatario;
	}

	public void setCorreoDestinatario(String correoDestinatario) {
		this.correoDestinatario = correoDestinatario;
	}

	public Boolean isEsEntidadExternaConRuc() {
		return esEntidadExternaConRuc;
	}

	public void setEsEntidadExternaConRuc(Boolean esEntidadExternaConRuc) {
		this.esEntidadExternaConRuc = esEntidadExternaConRuc;
	}

	public Character getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(Character tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public Boolean isEsDespachoFisicoDestExterno() {
		return esDespachoFisicoDestExterno;
	}

	public void setEsDespachoFisicoDestExterno(Boolean esDespachoFisicoDestExterno) {
		this.esDespachoFisicoDestExterno = esDespachoFisicoDestExterno;
	}

	public Boolean isEsCorreoElectronicoDestExterno() {
		return esCorreoElectronicoDestExterno;
	}

	public void setEsCorreoElectronicoDestExterno(Boolean esCorreoElectronicoDestExterno) {
		this.esCorreoElectronicoDestExterno = esCorreoElectronicoDestExterno;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isNacional() {
		return this.nacional;
	}

	public void setNacional(boolean nacional) {
		this.nacional = nacional;
	}

	public String getCodPais() {
		return this.codPais;
	}

	public void setCodPais(String codPais) {
		this.codPais = codPais;
	}

	public String getPais() {
		return this.pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCodDepartamento() {
		return this.codDepartamento;
	}

	public void setCodDepartamento(String codDepartamento) {
		this.codDepartamento = codDepartamento;
	}

	public String getDepartamento() {
		return this.departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getCodProvincia() {
		return this.codProvincia;
	}

	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCodDistrito() {
		return this.codDistrito;
	}

	public void setCodDistrito(String codDistrito) {
		this.codDistrito = codDistrito;
	}

	public String getDistrito() {
		return this.distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDependenciaNacional() {
		return this.dependenciaNacional;
	}

	public void setDependenciaNacional(String dependenciaNacional) {
		this.dependenciaNacional = dependenciaNacional;
	}

	public String getCodDependenciaNacional() {
		return this.codDependenciaNacional;
	}

	public void setCodDependenciaNacional(String codDependenciaNacional) {
		this.codDependenciaNacional = codDependenciaNacional;
	}

	public String getDependenciaInternacional() {
		return this.dependenciaInternacional;
	}

	public void setDependenciaInternacional(String dependenciaInternacional) {
		this.dependenciaInternacional = dependenciaInternacional;
	}

	public String getNombreDestinatario() {
		return this.nombreDestinatario;
	}

	public void setNombreDestinatario(String nombreDestinatario) {
		this.nombreDestinatario = nombreDestinatario;
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

	public String getDependencia() {
		// TICKET 9000003934
		/*
		 * if (this.nacional) { this.dependencia = this.dependenciaNacional; } else {
		 * this.dependencia = this.dependenciaInternacional; }
		 */
		if (this.nacional) {
			if (this.tipoDestinatario != null && this.tipoDestinatario.toString().equalsIgnoreCase("P"))
				this.dependencia = this.nombreDestinatario;
			else
				this.dependencia = this.dependenciaNacional;
		} else {
			if (this.tipoDestinatario != null && this.tipoDestinatario.toString().equalsIgnoreCase("P"))
				this.dependencia = this.nombreDestinatario;
			else
				this.dependencia = this.dependenciaInternacional;
		}

		return this.dependencia;
	}

	public String getLugar() {
		/*if (this.nacional) {
			this.lugar = this.departamento + " - " + this.provincia + " - " + this.distrito;
		} else {
			this.lugar = this.pais;
		}
		return this.lugar;*/
		// TICKET 9000003934
		if (this.nacional) {
			this.lugar = ((this.departamento != null)
					? (this.departamento + " - " + this.provincia + " - " + this.distrito)
					: (""));
		} else {
			this.lugar = ((this.pais != null) ? (this.pais) : (""));
		}
		return this.lugar;
	}
}

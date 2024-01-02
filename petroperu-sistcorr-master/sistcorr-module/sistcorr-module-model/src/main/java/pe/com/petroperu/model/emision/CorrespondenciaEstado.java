package pe.com.petroperu.model.emision;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "correspondencia_estado")
public class CorrespondenciaEstado extends EntidadBase implements Serializable {
	private static final long serialVersionUID = 8422201840730908957L;
	@Id
	private Long id;
	private String bandeja;
	private String url;
	private String estado;
	private String descripcionEstado;

	public CorrespondenciaEstado() {
	}

	public CorrespondenciaEstado(Long idCorrespondenciaEstado, String bandeja, String url, String estado,
			String descripcionEstado) {
		this.id = idCorrespondenciaEstado;
		this.bandeja = bandeja;
		this.url = url;
		this.estado = estado;
		this.descripcionEstado = descripcionEstado;
	}

	public CorrespondenciaEstado(Long idCorrespondenciaEstado) {
		this.id = idCorrespondenciaEstado;
	}

	public CorrespondenciaEstado(Long idCorrespondenciaEstado, String bandeja, String estado,
			String descripcionEstado) {
		this.id = idCorrespondenciaEstado;
		this.bandeja = bandeja;
		this.estado = estado;
		this.descripcionEstado = descripcionEstado;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBandeja() {
		return this.bandeja;
	}

	public void setBandeja(String bandeja) {
		this.bandeja = bandeja;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcionEstado() {
		return this.descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
}

package pe.com.petroperu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import pe.com.petroperu.Utilitario;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class EntidadBase {
	@Column(nullable = false)
	private String usuarioCrea;
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	@Column(nullable = false)
	private Date fechaCrea;
	@Column(nullable = true)
	private String usuarioModifica;
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaModifica;

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public Date getFechaCrea() {
		return this.fechaCrea;
	}

	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
}

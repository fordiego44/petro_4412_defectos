package pe.com.petroperu.model.emision;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "motivo_rechazo")
public class MotivoRechazo extends EntidadBase implements Serializable {
	@Id
	private Long id;
	@Column(nullable = false, length = 50)
	private String descripcion;
	@Column(nullable = false)
	private boolean cancelarDocumento;
	// TICKET 9000003908
	@Column(nullable = false)
	private boolean aumentarFlujo;
	// FIN TICKET
	private boolean rechazoResponsable;
	// TICKET 9000003996
	private boolean rechazoFirma;


	public MotivoRechazo() {
	}

	public MotivoRechazo(Long idMotivoRechazo) {
		this.id = idMotivoRechazo;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isCancelarDocumento() {
		return this.cancelarDocumento;
	}

	public void setCancelarDocumento(boolean cancelarDocumento) {
		this.cancelarDocumento = cancelarDocumento;
	}

	// TICKET 9000003908
	public boolean isAumentarFlujo() {
		return aumentarFlujo;
	}

	public void setAumentarFlujo(boolean aumentarFlujo) {
		this.aumentarFlujo = aumentarFlujo;
	}
	// FIN TICKET

	public boolean isRechazoResponsable() {
		return rechazoResponsable;
	}

	public void setRechazoResponsable(boolean rechazoResponsable) {
		this.rechazoResponsable = rechazoResponsable;
	}
	
	// TICKET 9000003996
	public boolean isRechazoFirma() {
		return rechazoFirma;
	}

	public void setRechazoFirma(boolean rechazoFirma) {
		this.rechazoFirma = rechazoFirma;
	}
	
}

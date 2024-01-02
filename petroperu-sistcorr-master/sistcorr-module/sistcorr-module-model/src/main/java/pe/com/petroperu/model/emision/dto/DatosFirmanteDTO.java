package pe.com.petroperu.model.emision.dto;

public class DatosFirmanteDTO {

	private Long id;
	private Long idCorrespondencia;
	private String codFirmante;
	private String solicitante;
	private String motivoRechazo;
	private String codFirmantePrevio;
	private String mensaje;

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCorrespondencia() {
		return idCorrespondencia;
	}

	public void setIdCorrespondencia(Long idCorrespondencia) {
		this.idCorrespondencia = idCorrespondencia;
	}

	public String getCodFirmante() {
		return codFirmante;
	}

	public void setCodFirmante(String codFirmante) {
		this.codFirmante = codFirmante;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	public String getCodFirmantePrevio() {
		return codFirmantePrevio;
	}

	public void setCodFirmantePrevio(String codFirmantePrevio) {
		this.codFirmantePrevio = codFirmantePrevio;
	}

}

package pe.com.petroperu.model;

public class Integrante {
	
	private Long idIntegrante;
	private String codigoIntegrante;
	private String nombreIntegrante;
	
	public Long getIdIntegrante() {
		return idIntegrante;
	}
	
	public void setIdIntegrante(Long idIntegrante) {
		this.idIntegrante = idIntegrante;
	}
	
	public String getCodigoIntegrante() {
		return codigoIntegrante;
	}

	public void setCodigoIntegrante(String codigoIntegrante) {
		this.codigoIntegrante = codigoIntegrante;
	}

	public String getNombreIntegrante() {
		return nombreIntegrante;
	}
	
	public void setNombreIntegrante(String nombreIntegrante) {
		this.nombreIntegrante = nombreIntegrante;
	}

	@Override
	public String toString() {
		return "Integrante [idIntegrante=" + idIntegrante + ", codigoIntegrante=" + codigoIntegrante
				+ ", nombreIntegrante=" + nombreIntegrante + "]";
	}
	
}

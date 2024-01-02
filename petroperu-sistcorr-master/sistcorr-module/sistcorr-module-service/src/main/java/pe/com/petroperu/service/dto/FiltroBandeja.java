package pe.com.petroperu.service.dto;

public class FiltroBandeja {
	private String usuario;
	private String correlativo;
	private String asunto;
	private boolean rechazados;
	private boolean misPendientes;
	private boolean declinados;
	private String fechaInicio;
	private String fechaFin;
	private String dependenciaOriginadora;//ticket 9000003866
	private String tipoCorrespondencia;//ticket 9000003866

	public String getDependenciaOriginadora() {
		return dependenciaOriginadora;
	}

	public void setDependenciaOriginadora(String dependenciaOriginadora) {
		this.dependenciaOriginadora = dependenciaOriginadora;
	}

	public String getTipoCorrespondencia() {
		return tipoCorrespondencia;
	}

	public void setTipoCorrespondencia(String tipoCorrespondencia) {
		this.tipoCorrespondencia = tipoCorrespondencia;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCorrelativo() {
		this.correlativo = (this.correlativo == null) ? "" : this.correlativo;
		return this.correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getAsunto() {
		this.asunto = (this.asunto == null) ? "" : this.asunto;
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public boolean isRechazados() {
		return this.rechazados;
	}

	public void setRechazados(boolean rechazados) {
		this.rechazados = rechazados;
	}

	public boolean isMisPendientes() {
		return this.misPendientes;
	}

	public void setMisPendientes(boolean misPendientes) {
		this.misPendientes = misPendientes;
	}

	public String getFechaInicio() {
		this.fechaInicio = (this.fechaInicio == null) ? "" : this.fechaInicio;
		return this.fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		this.fechaFin = (this.fechaFin == null) ? "" : this.fechaFin;
		return this.fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isDeclinados() {
		return this.declinados;
	}

	public void setDeclinados(boolean declinados) {
		this.declinados = declinados;
	}
}

package pe.com.petroperu.cliente.model;

public class FiltroConsultaAsignacion {
	
	private String correlativo;
	private String codigoEstado;
	private String numeroDocumentoInterno;
	private String fechaAsignacionDesde;
	private String fechaAsignacionHasta;
	private String tipoAccion;
	private String dependenciaAsignante;
	private String personaAsignada;
	private String fechaVencimientoDesde;
	private String fechaVencimientoHasta;
	
	public FiltroConsultaAsignacion() {
		super();
		this.correlativo = "";
		this.codigoEstado = "0";
		this.numeroDocumentoInterno = "%";
		this.fechaAsignacionDesde = "";
		this.fechaAsignacionHasta = "";
		this.tipoAccion = "";
		this.dependenciaAsignante = "0";
		this.personaAsignada = "";
		this.fechaVencimientoDesde = "";
		this.fechaVencimientoHasta = "";
	}
	public FiltroConsultaAsignacion(String correlativo, String codigoEstado, String numeroDocumentoInterno,
			String fechaAsignacionDesde, String fechaAsignacionHasta, String tipoAccion, String dependenciaAsignante,
			String personaAsignada, String fechaVencimientoDesde, String fechaVencimientoHasta) {
		super();
		this.correlativo = correlativo;
		this.codigoEstado = codigoEstado;
		this.numeroDocumentoInterno = numeroDocumentoInterno;
		this.fechaAsignacionDesde = fechaAsignacionDesde;
		this.fechaAsignacionHasta = fechaAsignacionHasta;
		this.tipoAccion = tipoAccion;
		this.dependenciaAsignante = dependenciaAsignante;
		this.personaAsignada = personaAsignada;
		this.fechaVencimientoDesde = fechaVencimientoDesde;
		this.fechaVencimientoHasta = fechaVencimientoHasta;
	}
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getNumeroDocumentoInterno() {
		return numeroDocumentoInterno;
	}
	public void setNumeroDocumentoInterno(String numeroDocumentoInterno) {
		this.numeroDocumentoInterno = numeroDocumentoInterno;
	}
	public String getFechaAsignacionDesde() {
		return fechaAsignacionDesde;
	}
	public void setFechaAsignacionDesde(String fechaAsignacionDesde) {
		this.fechaAsignacionDesde = fechaAsignacionDesde;
	}
	public String getFechaAsignacionHasta() {
		return fechaAsignacionHasta;
	}
	public void setFechaAsignacionHasta(String fechaAsignacionHasta) {
		this.fechaAsignacionHasta = fechaAsignacionHasta;
	}
	public String getTipoAccion() {
		return tipoAccion;
	}
	public void setTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}
	public String getDependenciaAsignante() {
		return dependenciaAsignante;
	}
	public void setDependenciaAsignante(String dependenciaAsignante) {
		this.dependenciaAsignante = dependenciaAsignante;
	}
	public String getPersonaAsignada() {
		return personaAsignada;
	}
	public void setPersonaAsignada(String personaAsignada) {
		this.personaAsignada = personaAsignada;
	}
	public String getFechaVencimientoDesde() {
		return fechaVencimientoDesde;
	}
	public void setFechaVencimientoDesde(String fechaVencimientoDesde) {
		this.fechaVencimientoDesde = fechaVencimientoDesde;
	}
	public String getFechaVencimientoHasta() {
		return fechaVencimientoHasta;
	}
	public void setFechaVencimientoHasta(String fechaVencimientoHasta) {
		this.fechaVencimientoHasta = fechaVencimientoHasta;
	}

}

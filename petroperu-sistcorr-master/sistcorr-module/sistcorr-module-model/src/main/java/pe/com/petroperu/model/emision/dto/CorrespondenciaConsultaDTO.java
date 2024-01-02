package pe.com.petroperu.model.emision.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class CorrespondenciaConsultaDTO {
	
	private Long id_correspondencia;
    private String correlativo;
    private String dependenciaOriginadora;
    private String codDependenciaOriginadora;
    private String originador;
    private String dependencia;
    private String codDependencia;
    private String lugarTrabajo;
    private String codLugarTrabajo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
    private Date fechaDocumento;
    private Long codTipoCorrespondencia;
    private String tipoCorrespondencia;
    private String asunto;
    private String emision_nombre;
    private Integer emision_cod;
    private boolean firmaDigital;
    private boolean confidencial;
    private boolean urgente;
    private boolean despachoFisico;
    private String destinatario_dependencia;
    private String destinatario_cgc;
    private String destinatario_cod;
    private String copia_dependencia;
    private String copia_cgc;
    private String copia_cod;
    private String estado;
    private Long estado_id;
    private String responsable;
    // TICKET 9000003874
    private Integer cantidad;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
    private Date fechaUltActualizacion;
    // TICKET 9000004494
    private Integer total;
    
    public CorrespondenciaConsultaDTO() {}
    
	public CorrespondenciaConsultaDTO(Long id_correspondencia, String correlativo, String dependenciaOriginadora,
			String codDependenciaOriginadora, String originador, String dependencia, String codDependencia,
			String lugarTrabajo, String codLugarTrabajo, Date fechaDocumento, String tipoCorrespondencia,
			Long codTipoCorrespondencia, String asunto, String emision_nombre, Integer emision_cod,
			boolean firmaDigital, boolean confidencial, boolean urgente, boolean despachoFisico,
			String destinatario_dependencia, String destinatario_cgc, String destinatario_cod, String copia_dependencia,
			String copia_cgc, String copia_cod, String estado, Long estado_id, Integer cantidad) {
		super();
		this.id_correspondencia = id_correspondencia;
		this.correlativo = correlativo;
		this.dependenciaOriginadora = dependenciaOriginadora;
		this.codDependenciaOriginadora = codDependenciaOriginadora;
		this.originador = originador;
		this.dependencia = dependencia;
		this.codDependencia = codDependencia;
		this.lugarTrabajo = lugarTrabajo;
		this.codLugarTrabajo = codLugarTrabajo;
		this.fechaDocumento = fechaDocumento;
		this.tipoCorrespondencia = tipoCorrespondencia;
		this.codTipoCorrespondencia = codTipoCorrespondencia;
		this.asunto = asunto;
		this.emision_nombre = emision_nombre;
		this.emision_cod = emision_cod;
		this.firmaDigital = firmaDigital;
		this.confidencial = confidencial;
		this.urgente = urgente;
		this.despachoFisico = despachoFisico;
		this.destinatario_dependencia = destinatario_dependencia;
		this.destinatario_cgc = destinatario_cgc;
		this.destinatario_cod = destinatario_cod;
		this.copia_dependencia = copia_dependencia;
		this.copia_cgc = copia_cgc;
		this.copia_cod = copia_cod;
		this.estado = estado;
		this.estado_id = estado_id;
		this.cantidad = cantidad;
	}
	
	public CorrespondenciaConsultaDTO(Long id_correspondencia, String correlativo, String dependenciaOriginadora,
			String codDependenciaOriginadora, String originador, String dependencia, String codDependencia,
			String lugarTrabajo, String codLugarTrabajo, Date fechaDocumento, String tipoCorrespondencia,
			Long codTipoCorrespondencia, String asunto, String emision_nombre, Integer emision_cod,
			boolean firmaDigital, boolean confidencial, boolean urgente, boolean despachoFisico,
			String destinatario_dependencia, String destinatario_cgc, String destinatario_cod, String copia_dependencia,
			String copia_cgc, String copia_cod, String estado, Long estado_id, Integer cantidad, String responsable) {
		super();
		this.id_correspondencia = id_correspondencia;
		this.correlativo = correlativo;
		this.dependenciaOriginadora = dependenciaOriginadora;
		this.codDependenciaOriginadora = codDependenciaOriginadora;
		this.originador = originador;
		this.dependencia = dependencia;
		this.codDependencia = codDependencia;
		this.lugarTrabajo = lugarTrabajo;
		this.codLugarTrabajo = codLugarTrabajo;
		this.fechaDocumento = fechaDocumento;
		this.tipoCorrespondencia = tipoCorrespondencia;
		this.codTipoCorrespondencia = codTipoCorrespondencia;
		this.asunto = asunto;
		this.emision_nombre = emision_nombre;
		this.emision_cod = emision_cod;
		this.firmaDigital = firmaDigital;
		this.confidencial = confidencial;
		this.urgente = urgente;
		this.despachoFisico = despachoFisico;
		this.destinatario_dependencia = destinatario_dependencia;
		this.destinatario_cgc = destinatario_cgc;
		this.destinatario_cod = destinatario_cod;
		this.copia_dependencia = copia_dependencia;
		this.copia_cgc = copia_cgc;
		this.copia_cod = copia_cod;
		this.estado = estado;
		this.estado_id = estado_id;
		this.responsable = responsable;
		this.cantidad = cantidad;
	}
	
	public CorrespondenciaConsultaDTO(Long id_correspondencia, String correlativo, String dependenciaOriginadora,
			String codDependenciaOriginadora, String originador, String dependencia, String codDependencia,
			String lugarTrabajo, String codLugarTrabajo, Date fechaDocumento, String tipoCorrespondencia,
			Long codTipoCorrespondencia, String asunto, String emision_nombre, Integer emision_cod,
			boolean firmaDigital, boolean confidencial, boolean urgente, boolean despachoFisico,
			String destinatario_dependencia, String destinatario_cgc, String destinatario_cod, String copia_dependencia,
			String copia_cgc, String copia_cod, String estado, Long estado_id, Integer cantidad, String responsable, Date fechaUltActualizacion) {
		super();
		this.id_correspondencia = id_correspondencia;
		this.correlativo = correlativo;
		this.dependenciaOriginadora = dependenciaOriginadora;
		this.codDependenciaOriginadora = codDependenciaOriginadora;
		this.originador = originador;
		this.dependencia = dependencia;
		this.codDependencia = codDependencia;
		this.lugarTrabajo = lugarTrabajo;
		this.codLugarTrabajo = codLugarTrabajo;
		this.fechaDocumento = fechaDocumento;
		this.tipoCorrespondencia = tipoCorrespondencia;
		this.codTipoCorrespondencia = codTipoCorrespondencia;
		this.asunto = asunto;
		this.emision_nombre = emision_nombre;
		this.emision_cod = emision_cod;
		this.firmaDigital = firmaDigital;
		this.confidencial = confidencial;
		this.urgente = urgente;
		this.despachoFisico = despachoFisico;
		this.destinatario_dependencia = destinatario_dependencia;
		this.destinatario_cgc = destinatario_cgc;
		this.destinatario_cod = destinatario_cod;
		this.copia_dependencia = copia_dependencia;
		this.copia_cgc = copia_cgc;
		this.copia_cod = copia_cod;
		this.estado = estado;
		this.estado_id = estado_id;
		this.responsable = responsable;
		this.cantidad = cantidad;
		this.fechaUltActualizacion = fechaUltActualizacion;
	}
	
	public Long getId_correspondencia() {
		return id_correspondencia;
	}
	public void setId_correspondencia(Long id_correspondencia) {
		this.id_correspondencia = id_correspondencia;
	}
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getDependenciaOriginadora() {
		return dependenciaOriginadora;
	}
	public void setDependenciaOriginadora(String dependenciaOriginadora) {
		this.dependenciaOriginadora = dependenciaOriginadora;
	}
	public String getCodDependenciaOriginadora() {
		return codDependenciaOriginadora;
	}
	public void setCodDependenciaOriginadora(String codDependenciaOriginadora) {
		this.codDependenciaOriginadora = codDependenciaOriginadora;
	}
	public String getOriginador() {
		return originador;
	}
	public void setOriginador(String originador) {
		this.originador = originador;
	}
	public String getDependencia() {
		return dependencia;
	}
	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}
	public String getCodDependencia() {
		return codDependencia;
	}
	public void setCodDependencia(String codDependencia) {
		this.codDependencia = codDependencia;
	}
	public String getLugarTrabajo() {
		return lugarTrabajo;
	}
	public void setLugarTrabajo(String lugarTrabajo) {
		this.lugarTrabajo = lugarTrabajo;
	}
	public String getCodLugarTrabajo() {
		return codLugarTrabajo;
	}
	public void setCodLugarTrabajo(String codLugarTrabajo) {
		this.codLugarTrabajo = codLugarTrabajo;
	}
	public Date getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public Long getCodTipoCorrespondencia() {
	return codTipoCorrespondencia;
}

public void setCodTipoCorrespondencia(Long codTipoCorrespondencia) {
	this.codTipoCorrespondencia = codTipoCorrespondencia;
}

public String getTipoCorrespondencia() {
	return tipoCorrespondencia;
}

public void setTipoCorrespondencia(String tipoCorrespondencia) {
	this.tipoCorrespondencia = tipoCorrespondencia;
}

	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getEmision_nombre() {
		return emision_nombre;
	}
	public void setEmision_nombre(String emision_nombre) {
		this.emision_nombre = emision_nombre;
	}
	public Integer getEmision_cod() {
		return emision_cod;
	}
	public void setEmision_cod(Integer emision_cod) {
		this.emision_cod = emision_cod;
	}
	
	public boolean isFirmaDigital() {
		return firmaDigital;
	}

	public void setFirmaDigital(boolean firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	public boolean isConfidencial() {
		return confidencial;
	}

	public void setConfidencial(boolean confidencial) {
		this.confidencial = confidencial;
	}

	public boolean isUrgente() {
		return urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	public boolean isDespachoFisico() {
		return despachoFisico;
	}

	public void setDespachoFisico(boolean despachoFisico) {
		this.despachoFisico = despachoFisico;
	}

	public String getDestinatario_dependencia() {
		return destinatario_dependencia;
	}
	public void setDestinatario_dependencia(String destinatario_dependencia) {
		this.destinatario_dependencia = destinatario_dependencia;
	}
	public String getDestinatario_cgc() {
		return destinatario_cgc;
	}
	public void setDestinatario_cgc(String destinatario_cgc) {
		this.destinatario_cgc = destinatario_cgc;
	}
	public String getDestinatario_cod() {
		return destinatario_cod;
	}
	public void setDestinatario_cod(String destinatario_cod) {
		this.destinatario_cod = destinatario_cod;
	}
	public String getCopia_dependencia() {
		return copia_dependencia;
	}
	public void setCopia_dependencia(String copia_dependencia) {
		this.copia_dependencia = copia_dependencia;
	}
	public String getCopia_cgc() {
		return copia_cgc;
	}
	public void setCopia_cgc(String copia_cgc) {
		this.copia_cgc = copia_cgc;
	}
	public String getCopia_cod() {
		return copia_cod;
	}
	public void setCopia_cod(String copia_cod) {
		this.copia_cod = copia_cod;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Long getEstado_id() {
		return estado_id;
	}
	public void setEstado_id(Long estado_id) {
		this.estado_id = estado_id;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Date getFechaUltActualizacion() {
		return fechaUltActualizacion;
	}

	public void setFechaUltActualizacion(Date fechaUltActualizacion) {
		this.fechaUltActualizacion = fechaUltActualizacion;
	}
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "CorrespondenciaConsultaDTO [id_correspondencia=" + id_correspondencia + ", correlativo=" + correlativo
				+ ", dependenciaOriginadora=" + dependenciaOriginadora + ", codDependenciaOriginadora="
				+ codDependenciaOriginadora + ", originador=" + originador + ", dependencia=" + dependencia
				+ ", codDependencia=" + codDependencia + ", lugarTrabajo=" + lugarTrabajo + ", codLugarTrabajo="
				+ codLugarTrabajo + ", fechaDocumento=" + fechaDocumento + ", codTipoCorrespondencia="
				+ codTipoCorrespondencia + ", tipoCorrespondencia=" + tipoCorrespondencia + ", asunto=" + asunto
				+ ", emision_nombre=" + emision_nombre + ", emision_cod=" + emision_cod + ", firmaDigital="
				+ firmaDigital + ", confidencial=" + confidencial + ", urgente=" + urgente + ", despachoFisico="
				+ despachoFisico + ", destinatario_dependencia=" + destinatario_dependencia + ", destinatario_cgc="
				+ destinatario_cgc + ", destinatario_cod=" + destinatario_cod + ", copia_dependencia="
				+ copia_dependencia + ", copia_cgc=" + copia_cgc + ", copia_cod=" + copia_cod + ", estado=" + estado
				+ ", estado_id=" + estado_id + ", responsable=" + responsable + ", cantidad=" + cantidad
				+ ", fechaUltActualizacion=" + fechaUltActualizacion + ", total=" + total + "]";
	}
    
    

}

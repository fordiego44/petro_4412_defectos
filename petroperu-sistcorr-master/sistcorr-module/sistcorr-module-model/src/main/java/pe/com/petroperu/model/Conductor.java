package pe.com.petroperu.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import pe.com.petroperu.Utilitario;

public class Conductor {
	
	private String workflowId;
	private String workflowSeleccionarId;
	private Long dtfechaExcepcion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_API_1, timezone = "America/Bogota")
	private Date fechaExcepcion;
	private String version;
	private String proceso;
	private String referenciaPrincipal;
	private String referenciaAlternativa;
	private String ultimoMensaje;
	private String asuntoMail;
	private String textoMail;
	private String fechaExcepcionStr;
	
	public String getFechaExcepcionStr() {
		fechaExcepcionStr = "";
		if(fechaExcepcion != null)
			fechaExcepcionStr = new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).format(fechaExcepcion);
		return fechaExcepcionStr;
	}
	public void setFechaExcepcionStr(String fechaExcepcionStr) {
		this.fechaExcepcionStr = fechaExcepcionStr;
	}
	public String getWorkflowSeleccionarId() {
		return workflowSeleccionarId;
	}
	public void setWorkflowSeleccionarId(String workflowSeleccionarId) {
		this.workflowSeleccionarId = workflowSeleccionarId;
	}
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public Long getDtfechaExcepcion() {
		return dtfechaExcepcion;
	}
	public void setDtfechaExcepcion(Long dtfechaExcepcion) {
		this.dtfechaExcepcion = dtfechaExcepcion;
	}
	public Date getFechaExcepcion() {
		return fechaExcepcion;
	}
	public void setFechaExcepcion(Date fechaExcepcion) {
		this.fechaExcepcion = fechaExcepcion;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	public String getReferenciaPrincipal() {
		return referenciaPrincipal;
	}
	public void setReferenciaPrincipal(String referenciaPrincipal) {
		this.referenciaPrincipal = referenciaPrincipal;
	}
	public String getReferenciaAlternativa() {
		return referenciaAlternativa;
	}
	public void setReferenciaAlternativa(String referenciaAlternativa) {
		this.referenciaAlternativa = referenciaAlternativa;
	}
	public String getUltimoMensaje() {
		return ultimoMensaje;
	}
	public void setUltimoMensaje(String ultimoMensaje) {
		this.ultimoMensaje = ultimoMensaje;
	}
	public String getAsuntoMail() {
		return asuntoMail;
	}
	public void setAsuntoMail(String asuntoMail) {
		this.asuntoMail = asuntoMail;
	}
	public String getTextoMail() {
		return textoMail;
	}
	public void setTextoMail(String textoMail) {
		this.textoMail = textoMail;
	}
	
}

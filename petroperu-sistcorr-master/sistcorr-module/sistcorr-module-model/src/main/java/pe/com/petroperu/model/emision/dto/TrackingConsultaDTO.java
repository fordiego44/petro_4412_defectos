/*INI Ticket 9000004412*/
package pe.com.petroperu.model.emision.dto;

public class TrackingConsultaDTO {
	
	private Long idTracking;
	private String item;
	private String tipo;
	private String fechaHora;
	private String funcionario;
	private String ingreso;
	private String egreso;
	
	private String correlativo;
	
	public Long getIdTracking() {
		return idTracking;
	}
	public void setIdTracking(Long idTracking) {
		this.idTracking = idTracking;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}
	public String getIngreso() {
		return ingreso;
	}
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}
	public String getEgreso() {
		return egreso;
	}
	public void setEgreso(String egreso) {
		this.egreso = egreso;
	}
	
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	@Override
	public String toString() {
		return "TrackingConsultaDTO [idTracking=" + idTracking + ", item=" + item + ", tipo=" + tipo + ", fechaHora="
				+ fechaHora + ", funcionario=" + funcionario + ", ingreso=" + ingreso + ", egreso=" + egreso
				+ ", correlativo=" + correlativo + "]";
	}
	public TrackingConsultaDTO() {
		super();
	}
	
	
	

}
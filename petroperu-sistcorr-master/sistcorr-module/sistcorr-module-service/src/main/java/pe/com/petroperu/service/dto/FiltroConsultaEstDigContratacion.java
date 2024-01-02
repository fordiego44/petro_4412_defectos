//9000004412
package pe.com.petroperu.service.dto;

public class FiltroConsultaEstDigContratacion {

	private String nroProceso;
	private String fechaDesde;
	private String fechaHasta;
	private String ruc;
	private String estado;
	public String getNroProceso() {
		return nroProceso;
	}
	public void setNroProceso(String nroProceso) {
		this.nroProceso = nroProceso;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "FiltroConsultaEstDigContratacion [nroProceso=" + nroProceso + ", fechaDesde=" + fechaDesde
				+ ", fechaHasta=" + fechaHasta + ", ruc=" + ruc + ", estado=" + estado + "]";
	}
	
	
	

}

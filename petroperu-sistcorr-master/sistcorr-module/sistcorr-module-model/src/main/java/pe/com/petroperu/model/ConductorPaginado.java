package pe.com.petroperu.model;

import java.util.List;

public class ConductorPaginado {
	
	private List<ContinuationData> continuationData;
	private List<Conductor> detalleConductor;
	private List<CorrespondenciaSimple>detalleCorrespondencias;
	private String totalTareas;
	private String totalItemxPagina;
	
	public List<ContinuationData> getContinuationData() {
		return continuationData;
	}
	public void setContinuationData(List<ContinuationData> continuationData) {
		this.continuationData = continuationData;
	}
	public List<Conductor> getDetalleConductor() {
		return detalleConductor;
	}
	public void setDetalleConductor(List<Conductor> detalleConductor) {
		this.detalleConductor = detalleConductor;
	}
	public String getTotalTareas() {
		return totalTareas;
	}
	public void setTotalTareas(String totalTareas) {
		this.totalTareas = totalTareas;
	}
	public String getTotalItemxPagina() {
		return totalItemxPagina;
	}
	public void setTotalItemxPagina(String totalItemxPagina) {
		this.totalItemxPagina = totalItemxPagina;
	}
	
	public List<CorrespondenciaSimple> getDetalleCorrespondencias() {
		return detalleCorrespondencias;
	}
	public void setDetalleCorrespondencias(List<CorrespondenciaSimple> detalleCorrespondencias) {
		this.detalleCorrespondencias = detalleCorrespondencias;
	}
	
	
	@Override
	public String toString() {
		return "ConductorPaginado [continuationData=" + continuationData + ", detalleConductor=" + detalleConductor
				+ ", detalleCorrespondencias=" + detalleCorrespondencias + ", totalTareas=" + totalTareas
				+ ", totalItemxPagina=" + totalItemxPagina + "]";
	}
	
	
}

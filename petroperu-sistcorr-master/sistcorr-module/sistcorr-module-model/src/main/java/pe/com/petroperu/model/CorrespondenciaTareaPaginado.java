package pe.com.petroperu.model;

import java.util.List;

public class CorrespondenciaTareaPaginado {
	
	private List<ContinuationData> continuationData;
	private List<CorrespondenciaSimple> detalleCorrespondencias;
	private String totalTareas;
	private String totalItemxPagina;
	
	public List<ContinuationData> getContinuationData() {
		return continuationData;
	}
	
	public void setContinuationData(List<ContinuationData> continuationData) {
		this.continuationData = continuationData;
	}
	
	public List<CorrespondenciaSimple> getDetalleCorrespondencias() {
		return detalleCorrespondencias;
	}
	
	public void setDetalleCorrespondencias(List<CorrespondenciaSimple> detalleCorrespondencias) {
		this.detalleCorrespondencias = detalleCorrespondencias;
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

	@Override
	public String toString() {
		return "CorrespondenciaTareaPaginado [continuationData=" + continuationData + ", detalleCorrespondencias=" + detalleCorrespondencias
				+ ", totalTareas=" + totalTareas + ", totalItemxPagina=" + totalItemxPagina + "]";
	}

}

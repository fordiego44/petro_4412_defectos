package pe.com.petroperu.model;

import java.util.List;

public class FiltroSiguientePagina {
	
	private List<ContinuationData> continuationData;
	private String totalPagina;
	private String totalItemxPagina;
	
	public List<ContinuationData> getContinuationData() {
		return continuationData;
	}
	
	public void setContinuationData(List<ContinuationData> continuationData) {
		this.continuationData = continuationData;
	}
	
	public String getTotalPagina() {
		return totalPagina;
	}
	
	public void setTotalPagina(String totalPagina) {
		this.totalPagina = totalPagina;
	}

	public String getTotalItemxPagina() {
		return totalItemxPagina;
	}

	public void setTotalItemxPagina(String totalItemxPagina) {
		this.totalItemxPagina = totalItemxPagina;
	}

	@Override
	public String toString() {
		return "FiltroSiguientePagina [continuationData=" + continuationData + ", totalPagina=" + totalPagina
				+ ", totalItemxPagina=" + totalItemxPagina + "]";
	}

}

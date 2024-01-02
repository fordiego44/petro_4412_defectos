package pe.com.petroperu.model;

public class ContinuationData {
	
	private String continuationData;
	private String nombreCola;
	
	public String getContinuationData() {
		return continuationData;
	}
	
	public void setContinuationData(String continuationData) {
		this.continuationData = continuationData;
	}
	
	public String getNombreCola() {
		return nombreCola;
	}
	
	public void setNombreCola(String nombreCola) {
		this.nombreCola = nombreCola;
	}

	@Override
	public String toString() {
		return "ContinuationData [continuationData=" + continuationData + ", nombreCola=" + nombreCola + "]";
	}

}

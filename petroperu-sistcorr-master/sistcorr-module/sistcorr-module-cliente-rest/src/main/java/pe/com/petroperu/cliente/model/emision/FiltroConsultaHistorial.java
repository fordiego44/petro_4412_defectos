package pe.com.petroperu.cliente.model.emision;

public class FiltroConsultaHistorial {
	
	private String fechaDesde;
	private String fechaHasta;
	private String valorBuscar;
	
	public String getFechaDesde(){
		return this.fechaDesde;
	}
	
	public void setFechaDesde(String fechaDesde){
		this.fechaDesde = fechaDesde;
	}
	
	public String getFechaHasta(){
		return this.fechaHasta;
	}
	
	public void setFechaHasta(String fechaHasta){
		this.fechaHasta = fechaHasta;
	}
	
	public String getValorBuscar(){
		return this.valorBuscar;
	}
	
	public void setValorBuscar(String valorBuscar){
		this.valorBuscar = valorBuscar;
	}

}

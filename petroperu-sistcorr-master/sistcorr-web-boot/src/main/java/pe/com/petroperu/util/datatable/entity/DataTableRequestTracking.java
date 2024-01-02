/*INI Ticket 9000004412*/
package pe.com.petroperu.util.datatable.entity;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaTrackingDTO;
import pe.com.petroperu.util.datatable.DataTableRequest;
import pe.com.petroperu.util.Constante;

/*INI Ticket 9000004412*/

public class DataTableRequestTracking extends DataTableRequest<FiltroConsultaTrackingDTO> {
	
	public DataTableRequestTracking(HttpServletRequest request){
		super(request);
		prepareRequestFilter(request);
	}

	private FiltroConsultaTrackingDTO filtro;
	private String columna;
	private String ordenDesc;
	
	public FiltroConsultaTrackingDTO getFiltro() {
		return filtro;
	}
	
	public void setFiltro(FiltroConsultaTrackingDTO filtro) {
		this.filtro = filtro;
	}
	
	public String getColumna() {
		return columna;
	}	public void setColumna(String columna) {
		this.columna = columna;
	}
	
	public String getOrdenDesc() {
		return ordenDesc;
	}

	public void setOrdenDesc(String ordenDesc) {
		this.ordenDesc = ordenDesc;
	}
	
	private void prepareRequestFilter(HttpServletRequest request){
		this.filtro = new FiltroConsultaTrackingDTO();
		
		try {
			this.filtro.setCorrelativo(((String) request.getParameter("correlativo")));
		} catch(Exception e) {
			this.filtro.setCorrelativo("");
		}
		
		
		try{
			this.setDraw(request.getParameter("draw"));
		} catch(Exception e) {
			this.setDraw("-1");
		}
		try{
			this.setStart(Integer.valueOf(request.getParameter("start")) + 1);
		} catch(Exception e) {
			this.setStart(0);
		}
		try{
			this.setLength(Integer.valueOf(request.getParameter("length")));
		} catch(Exception e) {
			this.setLength(0);
		}
		try{
			String ordenResultados = request.getParameter("order[0][dir]");
			if("asc".equalsIgnoreCase(ordenResultados)){
				this.setOrdenDesc("SI");
			}else{
				this.setOrdenDesc("NO");
			}
		} catch(Exception e) {
			this.setOrdenDesc("SI");
		}
		try{
			Integer columnOrder = Integer.valueOf(request.getParameter("order[0][column]"));
			if(columnOrder <= 1){
				columnOrder = 2;
				this.setOrdenDesc("SI");
			}
			String columnaOrden = Constante.COLUMNAS_CONSULTA_FUNCIONARIO[columnOrder-2];
			this.setColumna(columnaOrden);
		} catch(Exception e) {
			this.setColumna(null);
		}
		
	}
}


package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaConductorDTO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DataTableRequestConductorConsulta extends DataTableRequest<FiltroConsultaConductorDTO> {
	
	private FiltroConsultaConductorDTO filtro;
	private String columna;
	private String ordenDesc;
	
	public String getColumna() {
		return columna;
	}
	
	public void setColumna(String columna) {
		this.columna = columna;
	}
	
	public String getOrdenDesc() {
		return ordenDesc;
	}
	
	public void setOrdenDesc(String ordenDesc) {
		this.ordenDesc = ordenDesc;
	}
	
	public FiltroConsultaConductorDTO getFiltro() {
		return filtro;
	}
	
	public void setFiltro(FiltroConsultaConductorDTO filtro) {
		this.filtro = filtro;
	}
	
	public DataTableRequestConductorConsulta(HttpServletRequest request){
		super(request);
		prepareRequestFilter(request);
	}
	
	private void prepareRequestFilter(HttpServletRequest request){
		this.filtro = new FiltroConsultaConductorDTO();
		try {
			this.filtro.setTareasExcepcion((String) request.getParameter("tarea"));
		} catch(Exception e) {
			this.filtro.setTareasExcepcion("");
		}
		try {
			this.filtro.setProcesos((String) request.getParameter("proceso"));
		} catch(Exception e) {
			this.filtro.setProcesos("");
		}
		try {
			this.filtro.setReferenciaPrincipal((String) request.getParameter("refPrincipal"));
		} catch(Exception e) {
			this.filtro.setReferenciaPrincipal("");
		}
		try {
			this.filtro.setReferenciaAlternativa((String) request.getParameter("refAlternativa"));
		} catch(Exception e) {
			this.filtro.setReferenciaAlternativa("");
		}
		
		try {
			String FORMATO_FECHA_API_5 = "dd/MM/yyyy";
			this.filtro.setFechaDesde("".equalsIgnoreCase(request.getParameter("fechaDesdeTexto"))?null:new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaDesdeTexto")));
		} catch(Exception e) {
			this.filtro.setFechaDesde(null);
		}
		try {
			String FORMATO_FECHA_API_5 = "dd/MM/yyyy";
			this.filtro.setFechaHasta("".equalsIgnoreCase(request.getParameter("fechaHastaTexto"))?null:new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaHastaTexto")));
		} catch(Exception e) {
			this.filtro.setFechaHasta(null);
		}
		
		try {
			this.filtro.setTodos("true".equalsIgnoreCase(request.getParameter("todos"))?true:false);
		} catch(Exception e){
			this.filtro.setTodos(false);
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
			this.setOrdenDesc("DESC");
		}
		try{
			Integer columnOrder = Integer.valueOf(request.getParameter("order[0][column]"));
			if(columnOrder <= 0){
				columnOrder = 0;
				this.setOrdenDesc("SI");
			}
			String columnaOrden = Constante.COLUMNAS_CONSULTA_LOG[columnOrder];
			this.setColumna(columnaOrden);
		} catch(Exception e) {
			this.setColumna(null);
		}
	}

}

package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoDTO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DataTableRequestReemplazoConsulta extends DataTableRequest<FiltroConsultaReemplazoDTO> {
	
	private FiltroConsultaReemplazoDTO filtro;
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
	
	public FiltroConsultaReemplazoDTO getFiltro() {
		return filtro;
	}
	
	public void setFiltro(FiltroConsultaReemplazoDTO filtro) {
		this.filtro = filtro;
	}
	
	public DataTableRequestReemplazoConsulta(HttpServletRequest request){
		super(request);
		prepareRequestFilter(request);
	}
	
	private void prepareRequestFilter(HttpServletRequest request){
		this.filtro = new FiltroConsultaReemplazoDTO();
		try {
			this.filtro.setUsuarioSaliente((String) request.getParameter("usuarioSaliente"));
		} catch(Exception e) {
			this.filtro.setUsuarioSaliente("");
		}
		try {
			this.filtro.setUsuarioEntrante((String) request.getParameter("usuarioEntrante"));
		} catch(Exception e) {
			this.filtro.setUsuarioEntrante("");
		}
		try {
			this.filtro.setDependencia((Integer.valueOf(request.getParameter("dependencia"))));
		} catch(Exception e) {
			this.filtro.setDependencia(0);
		}
		try {
			this.filtro.setRol((String) request.getParameter("rol"));
		} catch(Exception e) {
			this.filtro.setRol("");
		}
		try {
			this.filtro.setMasFiltros("true".equalsIgnoreCase(request.getParameter("dependenciaAsignante"))?true:false);
		} catch(Exception e) {
			this.filtro.setMasFiltros(false);
		}
		try {
			String FORMATO_FECHA_API_5 = "dd/MM/yyyy";
			this.filtro.setFechaDesde("".equalsIgnoreCase(request.getParameter("fechaDesde"))?null:new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaDesde")));
		} catch(Exception e) {
			this.filtro.setFechaDesde(null);
		}
		try {
			String FORMATO_FECHA_API_5 = "dd/MM/yyyy";
			this.filtro.setFechaHasta("".equalsIgnoreCase(request.getParameter("fechaHasta"))?null:new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaHasta")));
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
				this.setOrdenDesc("NO");
			}else{
				this.setOrdenDesc("SI");
			}
		} catch(Exception e) {
			this.setOrdenDesc("SI");
		}
		try{
			Integer columnOrder = Integer.valueOf(request.getParameter("order[0][column]"));
			if(columnOrder <= 1){
				columnOrder = 2;
				this.setOrdenDesc("SI");
				this.setColumna("Id");
			}else {
				String columnaOrden = Constante.COLUMNAS_TIPOS_REEMPLAZOS[columnOrder-2];
				this.setColumna(columnaOrden);
			}
		} catch(Exception e) {
			this.setColumna(null);
		}
	}

}

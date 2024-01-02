package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.service.dto.FiltroConsultaFuncionariosDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoDTO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DataTableRequestFuncionariosConsulta extends DataTableRequest<FiltroConsultaReemplazoDTO> {
	
	private FiltroConsultaFuncionariosDTO filtro;
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
	
	public FiltroConsultaFuncionariosDTO getFiltro() {
		return filtro;
	}
	
	public void setFiltro(FiltroConsultaFuncionariosDTO filtro) {
		this.filtro = filtro;
	}
	
	public DataTableRequestFuncionariosConsulta(HttpServletRequest request){
		super(request);
		prepareRequestFilter(request);
	}
	
	private void prepareRequestFilter(HttpServletRequest request){
		this.filtro = new FiltroConsultaFuncionariosDTO();
		try {
			this.filtro.setDependencia((Integer.valueOf(request.getParameter("dependencia"))));
		} catch(Exception e) {
			this.filtro.setDependencia(0);
		}
		
		try {
			this.filtro.setRegistro((String) request.getParameter("registro"));
		} catch(Exception e) {
			this.filtro.setRegistro("");
		}
		try {
			this.filtro.setNombres((String) request.getParameter("nombres"));
		} catch(Exception e) {
			this.filtro.setNombres("");
		}
		
		try {
			this.filtro.setApellidos((String) request.getParameter("apellidos"));
		} catch(Exception e) {
			this.filtro.setApellidos("");
		}
		try {
			this.filtro.setMasFiltros("true".equalsIgnoreCase(request.getParameter("dependenciaAsignante"))?true:false);
		} catch(Exception e) {
			this.filtro.setMasFiltros(false);
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

package pe.com.petroperu.util.datatable.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;

import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DataTableRequestConsultaAsignacion extends DataTableRequest<FiltroConsultaAsignacion> {
	
	private FiltroConsultaAsignacion filtro;
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
	
	public FiltroConsultaAsignacion getFiltro() {
		return filtro;
	}
	
	public void setFiltro(FiltroConsultaAsignacion filtro) {
		this.filtro = filtro;
	}
	
	public DataTableRequestConsultaAsignacion(HttpServletRequest request){
		super(request);
		prepareRequestFilter(request);
	}
	
	private void prepareRequestFilter(HttpServletRequest request){
		this.filtro = new FiltroConsultaAsignacion();
		try {
			this.filtro.setCorrelativo((String) request.getParameter("correlativo"));
		} catch(Exception e){
			this.filtro.setCorrelativo("");
		}
		try {
			this.filtro.setCodigoEstado((String) request.getParameter("codigoEstado"));
		} catch(Exception e) {
			this.filtro.setCodigoEstado("0");
		}
		try {
			this.filtro.setNumeroDocumentoInterno((String) request.getParameter("numeroDocumentoInterno"));
		} catch(Exception e) {
			this.filtro.setNumeroDocumentoInterno("");
		}
		try {
			this.filtro.setFechaAsignacionDesde((String) request.getParameter("fechaAsignacionDesde"));
		} catch(Exception e) {
			this.filtro.setFechaAsignacionDesde("");
		}
		try {
			this.filtro.setFechaAsignacionHasta((String) request.getParameter("fechaAsignacionHasta"));
		} catch(Exception e) {
			this.filtro.setFechaAsignacionHasta("");
		}
		try {
			this.filtro.setTipoAccion((String) request.getParameter("tipoAccion"));
		} catch(Exception e) {
			this.filtro.setTipoAccion("");
		}
		try {
			this.filtro.setDependenciaAsignante((String) request.getParameter("dependenciaAsignante"));
		} catch(Exception e) {
			this.filtro.setDependenciaAsignante("");
		}
		try {
			this.filtro.setPersonaAsignada((String) request.getParameter("personaAsignada"));
		} catch(Exception e) {
			this.filtro.setPersonaAsignada("");
		}
		try {
			this.filtro.setFechaVencimientoDesde((String) request.getParameter("fechaVencimientoDesde"));
		} catch(Exception e) {
			this.filtro.setFechaVencimientoDesde("");
		}
		try {
			this.filtro.setFechaVencimientoHasta((String) request.getParameter("fechaVencimientoHasta"));
		} catch(Exception e) {
			this.filtro.setFechaVencimientoHasta("");
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
			}
			String columnaOrden = Constante.COLUMNAS_CONSULTA_ASIGNACION[columnOrder - 2];
			this.setColumna(columnaOrden);
		} catch(Exception e) {
			this.setColumna(null);
		}
	}
	
}

package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DataTableRequestCorrespondenciaConsultaDTO extends DataTableRequest<FiltroConsultaCorrespondenciaDTO> {
	
	private FiltroConsultaCorrespondenciaDTO filtro;
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
	
	public FiltroConsultaCorrespondenciaDTO getFiltro() {
		return filtro;
	}
	
	public void setFiltro(FiltroConsultaCorrespondenciaDTO filtro) {
		this.filtro = filtro;
	}
	
	public DataTableRequestCorrespondenciaConsultaDTO(HttpServletRequest request){
		super(request);
		prepareRequestFilter(request);
	}
	
	private void prepareRequestFilter(HttpServletRequest request){
		this.filtro = new FiltroConsultaCorrespondenciaDTO();
		try {
			this.filtro.setConsiderarOriginadora("true".equalsIgnoreCase(request.getParameter("considerarOriginadora"))?true:false);
		} catch(Exception e){
			this.filtro.setConsiderarOriginadora(false);
		}
		try {
			this.filtro.setCodDependenciaOriginadora((String) request.getParameter("codDependenciaOriginadora"));
		} catch(Exception e) {
			this.filtro.setCodDependenciaOriginadora("0");
		}
		try {
			this.filtro.setCodDependenciaRemitente(request.getParameter("codDependenciaRemitente")!=null?(String) request.getParameter("codDependenciaRemitente"):null);
		} catch(Exception e) {
			this.filtro.setCodDependenciaRemitente(null);
		}
		try {
			this.filtro.setCorrelativo((String) request.getParameter("correlativo"));
		} catch(Exception e) {
			this.filtro.setCorrelativo("");
		}
		try {
			this.filtro.setAsunto((String) request.getParameter("asunto"));
		} catch(Exception e) {
			this.filtro.setAsunto("");
		}
		try {
			this.filtro.setEstado((String) request.getParameter("estado"));
		} catch(Exception e) {
			this.filtro.setEstado("");
		}
		try {
			this.filtro.setMasFiltros("true".equalsIgnoreCase(request.getParameter("masFiltros"))?true:false);
		} catch(Exception e) {
			this.filtro.setMasFiltros(false);
		}
		try {
			this.filtro.setCodNombreOriginador((String) request.getParameter("codNombreOriginador"));
		} catch(Exception e) {
			this.filtro.setCodNombreOriginador("");
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
			this.filtro.setTipoCorrespondencia(Integer.valueOf(request.getParameter("tipoCorrespondencia")));
		} catch(Exception e) {
			this.filtro.setTipoCorrespondencia(0);
		}
		try {
			this.filtro.setTipoEmision(Integer.valueOf(request.getParameter("tipoEmision")));
		} catch(Exception e) {
			this.filtro.setTipoEmision(null);
		}
		try {
			this.filtro.setFirmaDigital(Integer.valueOf(request.getParameter("firmaDigital")));
		} catch(Exception e) {
			this.filtro.setFirmaDigital(2);
		}
		try {
			this.filtro.setConfidencial(Integer.valueOf(request.getParameter("confidencial")));
		} catch(Exception e) {
			this.filtro.setConfidencial(2);
		}
		try {
			this.filtro.setUrgente(Integer.valueOf(request.getParameter("urgente")));
		} catch(Exception e) {
			this.filtro.setUrgente(2);
		}
		try {
			this.filtro.setDespachoFisico(Integer.valueOf(request.getParameter("despachoFisico")));
		} catch(Exception e) {
			this.filtro.setDespachoFisico(2);
		}
		try {
			this.filtro.setCodDestinatario(request.getParameter("codDestinatario"));
		} catch(Exception e) {
			this.filtro.setCodDestinatario("");
		}
		try {
			this.filtro.setNombreDestinatario(request.getParameter("nombreDestinatario"));
		} catch(Exception e) {
			this.filtro.setNombreDestinatario("");
		}
		try {
			this.filtro.setCodCopia(request.getParameter("codCopia"));
		} catch(Exception e) {
			this.filtro.setCodCopia("");
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
				this.setOrdenDesc("ASC");
			}else{
				this.setOrdenDesc("DESC");
			}
		} catch(Exception e) {
			this.setOrdenDesc("DESC");
		}
		try{
			Integer columnOrder = Integer.valueOf(request.getParameter("order[0][column]"));
			if(columnOrder <= 0){
				columnOrder = 0;
				this.setOrdenDesc("DESC");
			}
			String columnaOrden = Constante.COLUMNAS_CONSULTA_JEFE_GESTOR[columnOrder - 2 ];
			this.setColumna(columnaOrden);
		} catch(Exception e) {
			this.setColumna(null);
		}
		
		try {
			String FORMATO_FECHA_API_5 = "dd/MM/yyyy";
			this.filtro.setFechaModificaDesde("".equalsIgnoreCase(request.getParameter("fechaModificaDesde"))?null:new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaModificaDesde")));
		} catch(Exception e) {
			this.filtro.setFechaModificaDesde(null);
		}
		try {
			String FORMATO_FECHA_API_5 = "dd/MM/yyyy";
			this.filtro.setFechaModificaHasta("".equalsIgnoreCase(request.getParameter("fechaModificaHasta"))?null:new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaModificaHasta")));
		} catch(Exception e) {
			this.filtro.setFechaModificaHasta(null);
		}
	}

}

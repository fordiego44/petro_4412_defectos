package pe.com.petroperu.util.datatable.entity;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DataTableRequestConsultaCorrespondencia extends DataTableRequest<FiltroConsultaCorrespondencia> {
	
	private FiltroConsultaCorrespondencia filtro;
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

	public DataTableRequestConsultaCorrespondencia(HttpServletRequest request) {
		super(request);
		prepareRequestFilter(request);
	}

	public FiltroConsultaCorrespondencia getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroConsultaCorrespondencia filtro) {
		this.filtro = filtro;
	}
	
	private void prepareRequestFilter(HttpServletRequest request){
		this.filtro = new FiltroConsultaCorrespondencia();
		try {
			this.filtro.setAsunto((String) request.getParameter("asunto"));
		} catch(Exception e){
			this.filtro.setAsunto("-1");
		}
		try {
			this.filtro.setCorrelativo((String) request.getParameter("correlativo"));
		} catch(Exception e) {
			this.filtro.setCorrelativo("-1");
		}
		try {
			this.filtro.setCodigoEstado((String) request.getParameter("codigoEstado"));
		} catch(Exception e) {
			this.filtro.setCodigoEstado("-1");
		}
		try {
			this.filtro.setFechaRegistroDesde((String) request.getParameter("fechaRegistroDesde"));
		} catch(Exception e) {
			this.filtro.setFechaRegistroDesde("-1");
		}
		try {
			this.filtro.setFechaRegistroHasta((String) request.getParameter("fechaRegistroHasta"));
		} catch(Exception e) {
			this.filtro.setFechaRegistroHasta("-1");
		}
		try {
			this.filtro.setNumeroDocumentoInterno((String) request.getParameter("numeroDocumentoInterno"));
		} catch(Exception e) {
			this.filtro.setNumeroDocumentoInterno("-1");
		}
		try {
			this.filtro.setFechaDocumentoInterno((String) request.getParameter("fechaDocumentoInterno"));
		} catch(Exception e) {
			this.filtro.setFechaDocumentoInterno("-1");
		}
		try {
			this.filtro.setCodigoDependenciaRemitente((String) request.getParameter("codigoDependenciaRemitente"));
		} catch(Exception e) {
			this.filtro.setCodigoDependenciaRemitente("-1");
		}
		try {
			this.filtro.setCodigoDependenciaDestino((String) request.getParameter("codigoDependenciaDestino"));
		} catch(Exception e) {
			this.filtro.setCodigoDependenciaDestino("-1");
		}
		try {
			this.filtro.setCodigoTipoCorrespondencia((String) request.getParameter("codigoTipoCorrespondencia"));
		} catch(Exception e) {
			this.filtro.setCodigoTipoCorrespondencia("-1");
		}
		try {
			this.filtro.setNombreDependenciaExterna((String) request.getParameter("nombreDependenciaExterna"));
		} catch(Exception e) {
			this.filtro.setNombreDependenciaExterna("-1");
		}
		try {
			this.filtro.setGuiaRemision((String) request.getParameter("guiaRemision"));
		} catch(Exception e) {
			this.filtro.setGuiaRemision("-1");
		}
		try {
			this.filtro.setProcedencia(request.getParameter("procedencia") == null ? "" : (String) request.getParameter("procedencia"));
		} catch(Exception e) {
			this.filtro.setProcedencia("");
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
			String columnaOrden = Constante.COLUMNAS_CONSULTA_CORRESPONDENCIA[columnOrder - 2];
			this.setColumna(columnaOrden);
		} catch(Exception e) {
			this.setColumna(null);
		}
	}

	@Override
	public String toString() {
		return "DataTableRequestConsultaCorrespondencia [filtro=" + filtro + "]";
	}
	
}

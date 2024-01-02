package pe.com.petroperu.util.datatable.entity;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaDespacho;
import pe.com.petroperu.service.dto.FiltroConsultaValijasRecibidas;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DatatableRequestConsultaValijasRecibidas  extends DataTableRequest<FiltroConsultaValijasRecibidas> {

	private FiltroConsultaValijasRecibidas filtro;
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

	public FiltroConsultaValijasRecibidas getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroConsultaValijasRecibidas filtro) {
		this.filtro = filtro;
	}

	public DatatableRequestConsultaValijasRecibidas(HttpServletRequest request) {
		super(request);
		prepareRequestFilter(request);
	}

	private void prepareRequestFilter(HttpServletRequest request) {
		this.filtro = new FiltroConsultaValijasRecibidas();
	
		try {
			this.filtro.setNroCorrelativo((String) request.getParameter("nroCorrelativo"));
		} catch (Exception e) {
			this.filtro.setNroCorrelativo("");
		}
		try {
			this.filtro.setCodEstado((String) request.getParameter("codEstado"));
		} catch (Exception e) {
			this.filtro.setCodEstado("");
		}
		
		try {
			this.filtro.setFechaDesde((String) request.getParameter("fechaDesde"));
		} catch (Exception e) {
			this.filtro.setFechaDesde("");
		}
		
		
		try {
			this.filtro.setFechaHasta((String) request.getParameter("fechaHasta"));
		} catch (Exception e) {
			this.filtro.setFechaHasta("");
		}
		try {
			this.filtro.setCgcReceptor((String) request.getParameter("cgcReceptor"));
		} catch (Exception e) {
			this.filtro.setCgcReceptor("");
		}	
		try {
			this.filtro.setCgcRemitente((String) request.getParameter("cgcRemitente"));
		} catch (Exception e) {
			this.filtro.setCgcRemitente("");
		}	
		
		try {
			this.filtro.setCourier((String) request.getParameter("courier"));
		} catch (Exception e) {
			this.filtro.setCourier("");
		}	
		
		
		//
		try {
			this.setDraw(request.getParameter("draw"));
		} catch (Exception e) {
			this.setDraw("-1");
		}
		try {
			this.setStart(Integer.valueOf(request.getParameter("start")) + 1);
		} catch (Exception e) {
			this.setStart(0);
		}
		try {
			this.setLength(Integer.valueOf(request.getParameter("length")));
		} catch (Exception e) {
			this.setLength(0);
		}
		try {
			String ordenResultados = request.getParameter("order[0][dir]");
			if ("asc".equalsIgnoreCase(ordenResultados)) {
				this.setOrdenDesc("NO");
			} else {
				this.setOrdenDesc("SI");
			}
		} catch (Exception e) {
			this.setOrdenDesc("SI");
		}
		try {
			Integer columnOrder = Integer.valueOf(request.getParameter("order[0][column]"));
			if (columnOrder <= 0) {
				columnOrder = 0;
				this.setOrdenDesc("SI");
			}
			String columnaOrden = Constante.COLUMNAS_CONSULTA_VALIJAS_RECIBIDAS[columnOrder];
			this.setColumna(columnaOrden);
		} catch (Exception e) {
			this.setColumna(null);
		}
	}
}

//9000004412
package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaContratacionDTO;
import pe.com.petroperu.service.dto.FiltroConsultaEstDigContratacion;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DatatableRequestConsultaEstDigContratacion extends DataTableRequest<FiltroConsultaEstDigContratacion> {

	private FiltroConsultaEstDigContratacion filtro;
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

	public FiltroConsultaEstDigContratacion getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroConsultaEstDigContratacion filtro) {
		this.filtro = filtro;
	}

	public DatatableRequestConsultaEstDigContratacion(HttpServletRequest request) {
		super(request);
		prepareRequestFilter(request);
	}

	private void prepareRequestFilter(HttpServletRequest request) {
		this.filtro = new FiltroConsultaEstDigContratacion();

		try {
			this.filtro.setNroProceso((String) request.getParameter("nroProceso"));
		} catch (Exception e) {
			this.filtro.setNroProceso("");
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
			this.filtro.setRuc((String) request.getParameter("ruc"));
		} catch (Exception e) {
			this.filtro.setRuc("");
		}	
		try {
			this.filtro.setEstado((String) request.getParameter("estado"));
		} catch (Exception e) {
			this.filtro.setEstado("");
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
			String columnaOrden = Constante.COLUMNAS_CONSULTA_CONTRATACIONES[columnOrder];
			this.setColumna(columnaOrden);
		} catch (Exception e) {
			this.setColumna(null);
		}
	}

}

//9000004276
package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaContratacionDTO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DatatableRequestConsultaContratacion extends DataTableRequest<FiltroConsultaContratacionDTO> {

	private FiltroConsultaContratacionDTO filtro;
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

	public FiltroConsultaContratacionDTO getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroConsultaContratacionDTO filtro) {
		this.filtro = filtro;
	}

	public DatatableRequestConsultaContratacion(HttpServletRequest request) {
		super(request);
		prepareRequestFilter(request);
	}

	private void prepareRequestFilter(HttpServletRequest request) {
		this.filtro = new FiltroConsultaContratacionDTO();

		try {
			this.filtro.setNroProceso((String) request.getParameter("nroProceso"));
		} catch (Exception e) {
			this.filtro.setNroProceso("");
		}
		try {
			this.filtro.setTipoProceso((String) request.getParameter("tipoProceso"));
		} catch (Exception e) {
			this.filtro.setTipoProceso("");
		}
		try {
			this.filtro.setNroMemo((String) request.getParameter("nroMemo"));
		} catch (Exception e) {
			this.filtro.setNroMemo("");
		}
		try {
			this.filtro.setCodDependencia(Integer.valueOf(request.getParameter("codDependencia")));
		} catch (Exception e) {
			this.filtro.setCodDependencia(0);
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

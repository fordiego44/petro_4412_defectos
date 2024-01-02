//9000004276
package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.service.dto.FiltroConsultaComprobanteDTO;
import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DatatableRequestConsultaComprobante extends DataTableRequest<FiltroConsultaComprobanteDTO> {

	private FiltroConsultaComprobanteDTO filtro;
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

	public FiltroConsultaComprobanteDTO getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroConsultaComprobanteDTO filtro) {
		this.filtro = filtro;
	}

	public DatatableRequestConsultaComprobante(HttpServletRequest request) {
		super(request);
		prepareRequestFilter(request);
	}

	private void prepareRequestFilter(HttpServletRequest request) {
		this.filtro = new FiltroConsultaComprobanteDTO();

		String FORMATO_FECHA_API_5 = "dd/MM/yyyy";
		try {
			this.filtro.setCorrelativo((String) request.getParameter("correlativo"));
		} catch (Exception e) {
			this.filtro.setCorrelativo("");
		}
		try {
			this.filtro.setEstado(Integer.valueOf(request.getParameter("estado")));
		} catch (Exception e) {
			this.filtro.setEstado(0);
		}
		try {
			this.filtro.setNroBatch((String) request.getParameter("nroBatch"));
		} catch (Exception e) {
			this.filtro.setNroBatch("");
		}
		try {
			this.filtro.setCodDependencia(Integer.valueOf(request.getParameter("codDependencia")));
		} catch (Exception e) {
			this.filtro.setCodDependencia(0);
		}
		try {
			this.filtro.setRucProveedor((String) request.getParameter("rucProveedor"));
		} catch (Exception e) {
			this.filtro.setRucProveedor("");
		}
		try {
			this.filtro.setFechaDesde("".equalsIgnoreCase(request.getParameter("fechaDesde")) ? null : new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaDesde")));
		} catch (Exception e) {
			this.filtro.setFechaDesde(null);
		}
		try {
			this.filtro.setFechaHasta("".equalsIgnoreCase(request.getParameter("fechaHasta")) ? null : new SimpleDateFormat(FORMATO_FECHA_API_5).parse(request.getParameter("fechaHasta")));
		} catch (Exception e) {
			this.filtro.setFechaHasta(null);
		}
		try {
			this.filtro.setNumComprobante((String) request.getParameter("numComprobante"));
		} catch (Exception e) {
			this.filtro.setNumComprobante("");
		}
		try {
			this.filtro.setRazonSocial((String) request.getParameter("razonSocial"));
		} catch (Exception e) {
			this.filtro.setRazonSocial("");
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
			String columnaOrden = Constante.COLUMNAS_CONSULTA_COMPROBANTES[columnOrder];
			this.setColumna(columnaOrden);
		} catch (Exception e) {
			this.setColumna(null);
		}
	}

}

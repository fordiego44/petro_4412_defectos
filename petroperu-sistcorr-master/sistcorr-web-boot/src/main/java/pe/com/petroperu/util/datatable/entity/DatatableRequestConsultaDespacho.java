//9000004412
package pe.com.petroperu.util.datatable.entity;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.service.dto.FiltroConsultaContratacionDTO;
import pe.com.petroperu.service.dto.FiltroConsultaDespacho;
import pe.com.petroperu.service.dto.FiltroConsultaEstDigContratacion;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DatatableRequestConsultaDespacho extends DataTableRequest<FiltroConsultaDespacho> {

	private FiltroConsultaDespacho filtro;
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

	public FiltroConsultaDespacho getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroConsultaDespacho filtro) {
		this.filtro = filtro;
	}

	public DatatableRequestConsultaDespacho(HttpServletRequest request) {
		super(request);
		prepareRequestFilter(request);
	}

	private void prepareRequestFilter(HttpServletRequest request) {
		this.filtro = new FiltroConsultaDespacho();
	
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
			this.filtro.setDependenciaRemitente((String) request.getParameter("dependenciaRemitente"));
		} catch (Exception e) {
			this.filtro.setDependenciaRemitente("");
		}	
		try {
			this.filtro.setUsuarioRemitente((String) request.getParameter("usuarioRemitente"));
		} catch (Exception e) {
			this.filtro.setUsuarioRemitente("");
		}	
		
		try {
			this.filtro.setNumeroDocumento((String) request.getParameter("numeroDocumento"));
		} catch (Exception e) {
			this.filtro.setNumeroDocumento("");
		}	
		
		try {
			this.filtro.setEntidadExterna((String) request.getParameter("entidadExterna"));
		} catch (Exception e) {
			this.filtro.setEntidadExterna("");
		}	
		
		try {
			this.filtro.setAsunto((String) request.getParameter("asunto"));
		} catch (Exception e) {
			this.filtro.setAsunto("");
		}	
		
		
		try {
			this.filtro.setGuiaRemision((String) request.getParameter("guiaRemision"));
		} catch (Exception e) {
			this.filtro.setGuiaRemision("");
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
			String columnaOrden = Constante.COLUMNAS_CONSULTA_DESPACHO[columnOrder];
			this.setColumna(columnaOrden);
		} catch (Exception e) {
			this.setColumna(null);
		}
	}

}

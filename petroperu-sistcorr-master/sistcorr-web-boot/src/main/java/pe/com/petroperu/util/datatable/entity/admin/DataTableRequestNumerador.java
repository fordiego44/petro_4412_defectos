package pe.com.petroperu.util.datatable.entity.admin;

import javax.servlet.http.HttpServletRequest;

import pe.com.petroperu.filenet.model.administracion.Numeradores;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableRequest;

public class DataTableRequestNumerador extends DataTableRequest<Numeradores> {

	private Numeradores filtro;
	private String columna;
	private String ordenDesc;
	
	public Numeradores getFiltro() {
		return filtro;
	}

	public void setFiltro(Numeradores filtro) {
		this.filtro = filtro;
	}

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

	public DataTableRequestNumerador(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
		prepareRequestFilter(request);
	}

	private void prepareRequestFilter(HttpServletRequest request) {
		this.filtro = new Numeradores();

		try {

			Integer codigo = 0;
			if (request.getParameter("codigoNumerador") == null
					|| request.getParameter("codigoNumerador").equals("")) {
				codigo = 0;
			} else {
				codigo = Integer.parseInt(request.getParameter("codigoNumerador"));
			}

			this.filtro.setCodigoNumerador(codigo);
		} catch (Exception e) {
			this.filtro.setCodigoNumerador(0);
		}

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
			if (columnOrder <= 1) {
				columnOrder = 2;
				this.setOrdenDesc("SI");
			}
			String columnaOrden = Constante.COLUMNAS_CONSULTA_NUMERADOR[columnOrder - 2];
			this.setColumna(columnaOrden);
		} catch (Exception e) {
			this.setColumna(null);
		}

	}
}

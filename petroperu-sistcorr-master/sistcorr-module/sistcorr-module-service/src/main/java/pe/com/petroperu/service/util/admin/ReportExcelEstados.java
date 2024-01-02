package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.Estado;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelEstados implements IReport<ByteArrayInputStream>{
	
	private List<Estado> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelEstados(List<Estado> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}

	@Override
	public void prepareRequest() {
		// TODO Auto-generated method stub
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("ID", "id");
			this.columns.put("CODIGO_ESTADO", "Código Estado");
			this.columns.put("CODIGO_PROCESO", "Código Proceos");
			this.columns.put("ESTADO", "Estado");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (Estado dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("CODIGO_ESTADO", dat.getCodigoEstado()==null?"":dat.getCodigoEstado());
				_item.put("CODIGO_PROCESO", dat.getCodigoProceso()==null?"":dat.getCodigoProceso());
				_item.put("ESTADO", dat.getEstado()==null?"":dat.getEstado());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA ESTADOS");
			excelData = excel.buildReport();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		
	}

	@Override
	public ByteArrayInputStream getResult() {
		// TODO Auto-generated method stub
		return excelData;
	}
	
	
	
}

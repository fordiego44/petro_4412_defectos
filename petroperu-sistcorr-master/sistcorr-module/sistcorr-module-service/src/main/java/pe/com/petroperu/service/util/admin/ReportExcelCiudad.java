package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.Ciudad;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelCiudad implements IReport<ByteArrayInputStream>{
	
	private List<Ciudad> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelCiudad(List<Ciudad> data,  String userCreate) {
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
			this.columns.put("COD_CIUDAD", "Código Ciudad");
			this.columns.put("NOM_CIUDAD", "Nombre Ciudad");
			this.columns.put("NOM_PAIS", "País");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (Ciudad dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_CIUDAD", dat.getCodigoCiudad()==null?"":dat.getCodigoCiudad());
				_item.put("NOM_CIUDAD", dat.getNombreCiudad()==null?"":dat.getNombreCiudad());
				_item.put("NOM_PAIS", dat.getNombrePais()==null?"":dat.getNombrePais());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA CIUDAD");
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

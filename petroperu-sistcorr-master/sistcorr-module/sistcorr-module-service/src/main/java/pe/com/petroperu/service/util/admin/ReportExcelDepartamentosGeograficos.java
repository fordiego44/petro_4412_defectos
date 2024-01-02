package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.Departamentos;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelDepartamentosGeograficos implements IReport<ByteArrayInputStream> {
	
	private List<Departamentos> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	
	public ReportExcelDepartamentosGeograficos(List<Departamentos> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("ID", "id");
			this.columns.put("COD_DEPARTAMENTO", "Código Departamento");
			this.columns.put("NOM_DEPARTAMENTO", "Nombre Departamento");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {

		try {
			this.values = new ArrayList<>();
			for (Departamentos depGeo : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", depGeo.getId()==null?"":depGeo.getId());
				_item.put("COD_DEPARTAMENTO", depGeo.getCodigoDepartamento()==null?"":depGeo.getCodigoDepartamento());
				_item.put("NOM_DEPARTAMENTO", depGeo.getDepartamento()==null?"":depGeo.getDepartamento());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA DEPARTAMENTOS GEOGRAFICOS");
			excelData = excel.buildReport();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public ByteArrayInputStream getResult() {
		return excelData;
	}
	

}

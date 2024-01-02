package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.ProvinciaLugarTrabajo;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelProvinciaLugarTrabajo implements IReport<ByteArrayInputStream>{
	
	private List<ProvinciaLugarTrabajo> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelProvinciaLugarTrabajo(List<ProvinciaLugarTrabajo> data,  String userCreate) {
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
			this.columns.put("NOM_DEPA", "Departamento");
			this.columns.put("NOM_PROV", "Provincia");
			this.columns.put("NOM_LUGAR", "Lugar de Trabajo");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (ProvinciaLugarTrabajo dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("NOM_DEPA", dat.getNombreDepartamento()==null?"":dat.getNombreDepartamento());
				_item.put("NOM_PROV", dat.getNombreProvincia()==null?"":dat.getNombreProvincia());
				_item.put("NOM_LUGAR", dat.getNombreLugar()==null?"":dat.getNombreLugar());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA PROVINCIA LUGAR TRABAJO");
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

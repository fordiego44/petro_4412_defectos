package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.Provincia;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelProvincia implements IReport<ByteArrayInputStream>{
	
	private List<Provincia> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelProvincia(List<Provincia> data,  String userCreate) {
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
			this.columns.put("COD_PROVINCIA", "Código Provincia");
			this.columns.put("NOM_PROVINCIA", "Nombre Provincia");
			this.columns.put("NOM_DEPARTAMENTO", "Departamento");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (Provincia dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_PROVINCIA", dat.getCodigoProvincia()==null?"":dat.getCodigoProvincia());
				_item.put("NOM_PROVINCIA", dat.getNombreProvincia()==null?"":dat.getNombreProvincia());
				_item.put("NOM_DEPARTAMENTO", dat.getNombreDepartamento()==null?"":dat.getNombreDepartamento());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA PROVINCIA");
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

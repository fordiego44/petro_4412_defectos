package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.DependenciaExterna;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelDependenciaExterna implements IReport<ByteArrayInputStream>{
	
	private List<DependenciaExterna> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelDependenciaExterna(List<DependenciaExterna> data,  String userCreate) {
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
			this.columns.put("RUC", "RUC");
			this.columns.put("NOMBRE", "Nombre");
			this.columns.put("DIRECCION", "Dirección");
			this.columns.put("DEPARTAMENTO", "Departamento");
			this.columns.put("PROVINCIA", "Provincia");
			this.columns.put("DISTRITO", "Distrito");
			this.columns.put("PAIS", "País");
			this.columns.put("CIUDAD", "Ciudad");
			this.columns.put("EMAIL", "Email");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (DependenciaExterna dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("RUC", dat.getRuc()==null?"":dat.getRuc());
				_item.put("NOMBRE", dat.getNombreDependencia()==null?"":dat.getNombreDependencia());
				_item.put("DIRECCION", dat.getDireccion()==null?"":dat.getDireccion());
				_item.put("DEPARTAMENTO", dat.getNombreDepartamento()==null?"":dat.getNombreDepartamento());
				_item.put("PROVINCIA", dat.getNombreProvincia()==null?"":dat.getNombreProvincia());
				_item.put("DISTRITO", dat.getNombreDistrito()==null?"":dat.getNombreDistrito());
				_item.put("PAIS", dat.getNombrePais()==null?"":dat.getNombrePais());
				_item.put("CIUDAD", dat.getNombreCiudad()==null?"":dat.getNombreCiudad());
				_item.put("EMAIL", dat.getEmail()==null?"":dat.getEmail());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA DEPENDENCIA EXTERNA");
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

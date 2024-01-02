package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.Distrito;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelDistrito  implements IReport<ByteArrayInputStream>{
	
	private List<Distrito> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelDistrito(List<Distrito> data,  String userCreate) {
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
			this.columns.put("COD_DISTRITO", "Código Distrito");
			this.columns.put("NOM_DISTRITO", "Nombre Distrito");
			this.columns.put("NOM_DEPARATMENTO", "Departamento");
			this.columns.put("NOM_PROVINCIA", "Provincia");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (Distrito dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_DISTRITO", dat.getCodigoDistrito()==null?"":dat.getCodigoDistrito());
				_item.put("NOM_DISTRITO", dat.getNombreDistrito()==null?"":dat.getNombreDistrito());
				_item.put("NOM_DEPARATMENTO", dat.getNombreDepartamento()==null?"":dat.getNombreDepartamento());
				_item.put("NOM_PROVINCIA", dat.getNombreProvincia()==null?"":dat.getNombreProvincia());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA DISTRITO");
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

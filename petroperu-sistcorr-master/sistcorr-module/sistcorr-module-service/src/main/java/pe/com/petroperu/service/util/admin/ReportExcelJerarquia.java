package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.Jerarquia;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelJerarquia  implements IReport<ByteArrayInputStream> {

	private List<Jerarquia> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelJerarquia(List<Jerarquia> data,  String userCreate) {
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
			this.columns.put("COD_JERARQUIA", "Código Jerarquia");
			this.columns.put("NOM_JERARQUIA", "Nombre Jerarquia");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (Jerarquia dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_JERARQUIA", dat.getCodigoJerarquia()==null?"":dat.getCodigoJerarquia());
				_item.put("NOM_JERARQUIA", dat.getNombreJerarquia()==null?"":dat.getNombreJerarquia());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA JERARQUIAS");
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

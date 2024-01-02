package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.LugarTrabajo;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelLugarTrabajo implements IReport<ByteArrayInputStream>{
	
	private List<LugarTrabajo> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelLugarTrabajo(List<LugarTrabajo> data,  String userCreate) {
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
			this.columns.put("COD_LUGAR_TRABAJO", "Código Lugar");
			this.columns.put("COD_NOMBRE_TRABAJO", "Nombre Lugar");
			this.columns.put("COD_DIRECCION_TRABAJO", "Dirección");
			this.columns.put("NOM_DEPARATMENTO", "Departamento");
			this.columns.put("NOM_PROVINCIA", "Provincia");
			this.columns.put("NOM_DISTRITO", "Nombre Distrito");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (LugarTrabajo dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_LUGAR_TRABAJO", dat.getCodigoLugar()==null?"":dat.getCodigoLugar());
				_item.put("COD_NOMBRE_TRABAJO", dat.getNombreLugar()==null?"":dat.getNombreLugar());
				_item.put("COD_DIRECCION_TRABAJO", dat.getDireccionLugar()==null?"":dat.getDireccionLugar());
				_item.put("NOM_DEPARATMENTO", dat.getNombreDepartamento()==null?"":dat.getNombreDepartamento());
				_item.put("NOM_PROVINCIA", dat.getNombreProvincia()==null?"":dat.getNombreProvincia());
				_item.put("NOM_DISTRITO", dat.getNombreDistrito()==null?"":dat.getNombreDistrito());				

				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA LUGAR DE TRABAJO");
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

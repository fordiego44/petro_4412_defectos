package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.CourierLugarTrabajo;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelCourierLugarTrabajo implements IReport<ByteArrayInputStream>{
	
	private List<CourierLugarTrabajo> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelCourierLugarTrabajo(List<CourierLugarTrabajo> data,  String userCreate) {
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
			this.columns.put("NOM_CGC", "Nombre CGC");
			this.columns.put("NOM_COURIER", "Nombre Courier");
			this.columns.put("ALCANCE", "Alcance");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (CourierLugarTrabajo dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("NOM_CGC", dat.getNombreCGC()==null?"":dat.getNombreCGC());
				_item.put("NOM_COURIER", dat.getNombreCourier()==null?"":dat.getNombreCourier());
				_item.put("ALCANCE", dat.getAlcance()==null?"":dat.getAlcance());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA COURIER LUGAR TRABAJO");
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

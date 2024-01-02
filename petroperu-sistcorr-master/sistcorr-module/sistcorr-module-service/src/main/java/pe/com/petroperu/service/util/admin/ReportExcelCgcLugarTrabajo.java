package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.CgcLugarTrabajo;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelCgcLugarTrabajo implements IReport<ByteArrayInputStream>{
	
	private List<CgcLugarTrabajo> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelCgcLugarTrabajo(List<CgcLugarTrabajo> data,  String userCreate) {
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
			this.columns.put("CGC", "cgc");
			this.columns.put("LUGAR_TRABAJO", "Lugar de Trabajo");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (CgcLugarTrabajo dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("CGC", dat.getNombreCgc()==null?"":dat.getNombreCgc());
				_item.put("LUGAR_TRABAJO", dat.getNombreLugar()==null?"":dat.getNombreLugar());

				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA CGC LUGAR DE TRABAJO");
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

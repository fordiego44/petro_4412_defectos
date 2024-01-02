package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;

public class ReporteExcelHistorial  implements IReport<ByteArrayInputStream> {
	
	private List<Object[]> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	private String correlativo;
	
	public ReporteExcelHistorial(List<Object[]> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	public ReporteExcelHistorial(List<Object[]> data,  String userCreate, String correlativo) {
		super();
		this.data = data;
		this.userCreate = userCreate;
		this.correlativo = correlativo;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("FECHA_HORA", "Fecha y Hora");
			this.columns.put("MODO_COMP", "Modo Compartido");
			this.columns.put("COMP_POR", "Compartido Por");
			this.columns.put("DEST", "Destinatarios");
			this.columns.put("COPIA", "Copia");
			this.columns.put("ASUNTO", "Asunto");
			this.columns.put("CONT", "Contenido");
			this.columns.put("ARCHIV", "Archivos");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (Object[] corr : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("FECHA_HORA", String.valueOf(corr[0] + " " + corr[1]));
				_item.put("MODO_COMP", String.valueOf(corr[2]));
				_item.put("COMP_POR", String.valueOf(corr[3]));
				_item.put("DEST", String.valueOf(corr[4]));
				_item.put("COPIA", String.valueOf(corr[5]));
				_item.put("ASUNTO", String.valueOf(corr[6]));
				_item.put("CONT", String.valueOf(corr[7]));
				_item.put("ARCHIV", String.valueOf(corr[8]));
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "Historial de Notificación - " + this.correlativo);
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

/*9000004412*/
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
import pe.com.petroperu.model.emision.dto.EstDigContratacionConsultaDTO;

public class ReportExcelEstDigContratacion  implements IReport<ByteArrayInputStream> {
	
	private List<EstDigContratacionConsultaDTO> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	
	public ReportExcelEstDigContratacion(List<EstDigContratacionConsultaDTO> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("NRO_PROCESO", "Nro. Proceso");
			this.columns.put("CORRELATIVO", "Correlativo");
			this.columns.put("RUC", "Ruc");
			this.columns.put("FECHA", "Fecha");
			this.columns.put("DIGITALIZADO", "Digitalizado");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (EstDigContratacionConsultaDTO corr : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("NRO_PROCESO", corr.getNroProceso());
				_item.put("CORRELATIVO", corr.getCorrelativo());
				_item.put("RUC", corr.getRuc());
				_item.put("FECHA", corr.getFechaDesde());
				_item.put("DIGITALIZADO", corr.getDigitalizado());
				
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate);
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

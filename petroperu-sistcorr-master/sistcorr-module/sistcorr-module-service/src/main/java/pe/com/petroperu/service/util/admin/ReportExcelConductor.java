package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.Departamentos;
import pe.com.petroperu.model.Conductor;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelConductor implements IReport<ByteArrayInputStream> {
	
	private List<Conductor> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	
	public ReportExcelConductor(List<Conductor> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("ULTIMO_MSJ_ERROR", "Último mensaje de error");
			this.columns.put("PROCESO", "Proceso");
			this.columns.put("REF_PRINCIPAL", "Referencia Principal");
			this.columns.put("REF_ALTERNATIVA", "Referencia Alternativa");
			this.columns.put("FECHA_EXCEPCION", "Fecha de Excepción");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {

		try {
			this.values = new ArrayList<>();
			for (Conductor depGeo : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ULTIMO_MSJ_ERROR", depGeo.getUltimoMensaje()==null?"":depGeo.getUltimoMensaje());
				_item.put("PROCESO", depGeo.getProceso()==null?"":depGeo.getProceso());
				_item.put("REF_PRINCIPAL", depGeo.getReferenciaPrincipal()==null?"":depGeo.getReferenciaPrincipal());
				_item.put("REF_ALTERNATIVA", depGeo.getReferenciaAlternativa()==null?"":depGeo.getReferenciaAlternativa());
				_item.put("FECHA_EXCEPCION", depGeo.getFechaExcepcionStr()==null?"":depGeo.getFechaExcepcionStr());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA CONDUCTORES");
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

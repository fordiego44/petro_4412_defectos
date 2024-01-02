package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.cliente.model.Bandeja;
import pe.com.petroperu.model.CorrespondenciaSimple;

public class ReportExcelCABandejaEntrada  implements IReport<ByteArrayInputStream> {
	
	private List<Bandeja> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	private String bandeja;
	
	public ReportExcelCABandejaEntrada(List<Bandeja> data,  String userCreate, String bandeja) {
		super();
		this.data = data;
		this.userCreate = userCreate;
		this.bandeja = bandeja;
	}
	
	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("TIPO", "Tipo");
			this.columns.put("CORRELATIVO", "Correlativo");
			this.columns.put("FECHA_CREACION", "Fecha Creación");
			this.columns.put("REMITENTE", "Remitente");
			this.columns.put("ASUNTO", "Asunto");
			this.columns.put("NRO_DOCUMENTO", "Nro Documento");
			if(this.bandeja != null && !this.bandeja.equalsIgnoreCase("DELGESTOR"))
			this.columns.put("DETALLE_SOLICITUD", "[Detalle de Solicitud]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			if(data.get(0) != null && data.get(0).getCorrespondencias() != null)
			for (CorrespondenciaSimple corr : data.get(0).getCorrespondencias()) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("TIPO", (corr.getTipoIcono() == null)?(""):(corr.getTipoIcono()));
				_item.put("CORRELATIVO", (corr.getCorrelativo() == null)?(""):(corr.getCorrelativo()));
				_item.put("FECHA_CREACION", (corr.getFechaCreacion() == null)?(""):(corr.getFechaCreacion()));
				_item.put("REMITENTE", (corr.getDependenciaRemitente() == null)?(""):(corr.getDependenciaRemitente()));
				//_item.put("FECH_DOC", new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(corr.getFechaDocumento()));
				_item.put("ASUNTO", (corr.getAsunto() == null)?(""):(corr.getAsunto()));
				_item.put("NRO_DOCUMENTO", (corr.getNumeroDocumento() == null)?(""):(corr.getNumeroDocumento()));
				if(this.bandeja != null && !this.bandeja.equalsIgnoreCase("DELGESTOR"))
				_item.put("DETALLE_SOLICITUD", (corr.getDetalleSolicitud() == null)?(""):(corr.getDetalleSolicitud()));
				this.values.add(_item);
			}
			if(this.bandeja != null && this.bandeja.equalsIgnoreCase("DELGESTOR"))
				this.bandeja = "DEL GESTOR";
			else if(this.bandeja != null && this.bandeja.equalsIgnoreCase("ENATENCION"))
				this.bandeja = "EN ATENCIÓN";
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE DE BANDEJA DE ENTRADA - " + this.bandeja.toUpperCase());
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

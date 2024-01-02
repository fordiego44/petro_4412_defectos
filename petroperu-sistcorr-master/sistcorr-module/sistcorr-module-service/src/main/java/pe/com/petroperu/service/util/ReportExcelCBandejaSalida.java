package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.model.emision.dto.CorrespondenciaDTO;

public class ReportExcelCBandejaSalida  implements IReport<ByteArrayInputStream> {
	
	private List<CorrespondenciaDTO> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	private String bandeja;
	
	public ReportExcelCBandejaSalida(List<CorrespondenciaDTO> data,  String userCreate, String bandeja) {
		super();
		this.data = data;
		this.userCreate = userCreate;
		this.bandeja = bandeja;
	}
	
	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("NRO_DOCUMENTO", "Nro Documento");
			this.columns.put("ASUNTO", "Asunto");
			this.columns.put("ESTADO", "Estado");
			this.columns.put("TIPO_FIRMA", "Tipo de Firma");
			this.columns.put("RUTA_APROBACION", "Ruta de Apropbación");
			this.columns.put("FECHA_MOD", "Fecha de Modificación");
			this.columns.put("TIPO_CORRESPONDENCIA", "Tipo de Correspondencia");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			if(data != null)
			for (CorrespondenciaDTO corr : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("NRO_DOCUMENTO", (corr.getCorrelativo() == null)?(""):(corr.getCorrelativo()));
				_item.put("ASUNTO", (corr.getAsunto() == null)?(""):(corr.getAsunto()));
				_item.put("ESTADO", (corr.getEstadoDescripcion() == null)?(""):(corr.getEstadoDescripcion()));
				_item.put("TIPO_FIRMA", (corr.isFirmaDigital())?("DIGITAL"):("MANUAL"));
				_item.put("RUTA_APROBACION", (corr.isRutaAprobacion())?("SI"):("NO"));
				_item.put("FECHA_MOD", (corr.getFechaModificacion() == null)?(""):(corr.getFechaModificacion()));
				
				_item.put("TIPO_CORRESPONDENCIA", (corr.getTipoCorrespondencia() == null)?(""):(corr.getTipoCorrespondencia()));
				this.values.add(_item);
			}
			
			if(this.bandeja != null && this.bandeja.equalsIgnoreCase("PENDIENTE"))
				this.bandeja = "NUEVOS";
			else if(this.bandeja != null && this.bandeja.equalsIgnoreCase("firmado"))
				this.bandeja = "FIRMA DIGITAL";
			
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE DE BANDEJA DE SALIDA - " + this.bandeja.toUpperCase());
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

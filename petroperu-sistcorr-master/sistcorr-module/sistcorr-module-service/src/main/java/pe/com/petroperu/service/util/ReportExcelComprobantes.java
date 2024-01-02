//9000004276
package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.emision.dto.ComprobanteConsultaDTO;

public class ReportExcelComprobantes implements IReport<ByteArrayInputStream> {

	private List<ComprobanteConsultaDTO> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;

	public ReportExcelComprobantes(List<ComprobanteConsultaDTO> data, String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("FEC_REC", "Fecha Recepción");
			this.columns.put("CORR", "Correlativo");
			this.columns.put("NRO_BATCH", "Nro Batch");
			this.columns.put("RUC", "RUC");
			this.columns.put("RAZ_SOC", "Razon Social");
			this.columns.put("TIPO_COMP", "Tipo de Comprobante");
			this.columns.put("NRO_COMP", "Nro Comprobante");
			this.columns.put("FEC_COMP", "Fecha Comprobante");
			this.columns.put("MONEDA", "Moneda");
			this.columns.put("ESTADO", "Estado");
			this.columns.put("DEP", "Dependencia");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (ComprobanteConsultaDTO comp : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("FEC_REC", comp.getFechaRecepcion() == null? "" : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).format(comp.getFechaRecepcion()));
				_item.put("CORR", comp.getCorrelativo());
				_item.put("NRO_BATCH", comp.getNroBatch());
				_item.put("RUC", comp.getRuc());
				_item.put("RAZ_SOC", comp.getRazonSocial());
				_item.put("TIPO_COMP", comp.getDescComprobante());
				_item.put("NRO_COMP", comp.getNroComprobante());
				_item.put("FEC_COMP", comp.getFechaComprobante() == null? "" : new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(comp.getFechaComprobante()));
				_item.put("MONEDA", comp.getMoneda());
				_item.put("ESTADO", comp.getEstado());
				_item.put("DEP", comp.getDependencia());
				this.values.add(_item);
			}
			ExcelReporteComprobante excel = new ExcelReporteComprobante(columns, values, userCreate);
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

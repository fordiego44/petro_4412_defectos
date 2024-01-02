package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaSimple;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;

public class ReportExcelConsultaAuditoria  implements IReport<ByteArrayInputStream> {
	
	private List<CorrespondenciaConsulta> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	
	public ReportExcelConsultaAuditoria(List<CorrespondenciaConsulta> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("CORRE", "Correlativo");
			this.columns.put("NUM_DOC", "Nro. Documento");
			this.columns.put("ASUNTO", "Asunto");
			this.columns.put("FECH_REC", "Fecha Recepción");
			this.columns.put("DEP_REMIT", "Remitente");
			this.columns.put("DEP_ORI", "Destino");
			this.columns.put("ESTADO", "Estado");
			this.columns.put("TIPO_CORR", "Tipo de Corresp.");
			this.columns.put("CORR_EMIT", "Es Correspondencia Emitida");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (CorrespondenciaConsulta corr : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("CORRE", corr.getCorrelativo()==null?"":corr.getCorrelativo());
				_item.put("NUM_DOC", corr.getNumeroDocumentoInterno()==null?"":corr.getNumeroDocumentoInterno());
				_item.put("ASUNTO", corr.getAsunto()==null?"":corr.getAsunto());
				_item.put("FECH_REC", corr.getFechaRadicadoProc()==null?"":corr.getFechaRadicadoProc());
				_item.put("DEP_REMIT", corr.getDestino()==null?"":corr.getDestino());
				_item.put("DEP_ORI", corr.getOrigen()==null?"":corr.getOrigen());
				_item.put("ESTADO", corr.getEstado()==null?"":corr.getEstado());
				_item.put("TIPO_CORR", corr.getTipoCorrespondencia()==null?"":corr.getTipoCorrespondencia());
				_item.put("CORR_EMIT", corr.getTipoIcono()==null?"NO":corr.getTipoIcono().equals("EM")?"SI":"NO");
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE DE CONSULTA DE AUDITORÍA");
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

package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.AsignacionConsulta;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaSimple;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;

public class ReportExcelConsultaAsignacion  implements IReport<ByteArrayInputStream> {
	
	private List<AsignacionConsulta> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	
	public ReportExcelConsultaAsignacion(List<AsignacionConsulta> data,  String userCreate) {
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
			this.columns.put("FECH_DOC", "Fecha Documento");
			this.columns.put("FECH_REC", "Fecha Recepción");
			this.columns.put("DEP_REMIT", "Remitente");
			this.columns.put("FECH_ASI", "Fecha Asignación");
			this.columns.put("PER_ASI", "Persona Asignada");
			this.columns.put("DET_REQ", "Detalle Requerimiento");
			this.columns.put("SOLIC", "Solicitante");
			this.columns.put("ESTADO", "Estado");
			this.columns.put("FECH_VEN", "Fecha Vencimiento");
			this.columns.put("DOC_RESP", "Documento Respuesta");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (AsignacionConsulta corr : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("CORRE", corr.getCorrelativo()==null?"":corr.getCorrelativo());
				_item.put("NUM_DOC", corr.getNumeroDocumentoInterno()==null?"":corr.getNumeroDocumentoInterno());
				_item.put("ASUNTO", corr.getAsunto()==null?"":corr.getAsunto());
				_item.put("FECH_DOC", corr.getFechaDocumentoProc()==null?"":corr.getFechaDocumentoProc());
				_item.put("FECH_REC", corr.getFechaRecepcionProc()==null?"":corr.getFechaRecepcionProc());
				_item.put("DEP_REMIT", corr.getRemitente()==null?"":corr.getRemitente());
				_item.put("FECH_ASI", corr.getFechaAsignacionProc()==null?"":corr.getFechaAsignacionProc());
				_item.put("PER_ASI", corr.getAsignado()==null?"":corr.getAsignado());
				_item.put("DET_REQ", corr.getTextoAsig()==null?"":corr.getTextoAsig());
				_item.put("SOLIC", corr.getSolicitante()==null?"":corr.getSolicitante());
				_item.put("ESTADO", corr.getEstado()==null?"":corr.getEstado());
				_item.put("FECH_VEN", corr.getFechaPlazoRespuestaProc()==null?"":corr.getFechaPlazoRespuestaProc());
				_item.put("DOC_RESP", corr.getDocumentoRespuesta()==null?"":corr.getDocumentoRespuesta());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA DE ASIGNACIONES");
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

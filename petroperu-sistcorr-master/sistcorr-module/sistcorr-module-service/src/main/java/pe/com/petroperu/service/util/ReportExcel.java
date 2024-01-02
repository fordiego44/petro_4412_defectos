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

public class ReportExcel  implements IReport<ByteArrayInputStream> {
	
	private List<CorrespondenciaConsultaDTO> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	
	public ReportExcel(List<CorrespondenciaConsultaDTO> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("CORRE", "Nro. Documento");
			this.columns.put("ASUNTO", "Asunto");
			this.columns.put("DESTINA", "Destinatario");
			this.columns.put("DEP_REMIT", "Dep. Remitente Aprobadora");
			this.columns.put("ESTADO", "Estado");
			this.columns.put("FECH_DOC", "Fecha Documento");
			this.columns.put("DEP_ORI", "Dep. Originadora");
			this.columns.put("NOM_ORI", "Nom. Originador");
			this.columns.put("CGC", "Centro. Gest. Corresp.");
			this.columns.put("TIPO_CORR", "Tipo de Corresp.");
			this.columns.put("TIPO_EMISION", "Tipo de Emisión");
			this.columns.put("FLUJO_FIRMA", "Flujo de Firma");
			this.columns.put("DESPA_FIS", "Despacho Físico");
			this.columns.put("URGENTE", "Urgente");
			this.columns.put("CONDIF", "Confidencial");
			this.columns.put("CGC_DESTINA", "CGC Destinatario");
			this.columns.put("COPIA", "Copia");
			this.columns.put("CGC_COPIA", "CGC Copia");
			this.columns.put("RESPONSABLE", "Responsable");
			this.columns.put("FECH_ULT_ACT", "Fecha y Hora Ult. Actualiz.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (CorrespondenciaConsultaDTO corr : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("CORRE", corr.getCorrelativo());
				_item.put("ASUNTO", corr.getAsunto());
				if(corr.getCantidad()>1){
					_item.put("DESTINA", corr.getDestinatario_dependencia() + " (*)");
				}else{
					_item.put("DESTINA", corr.getDestinatario_dependencia());
				}
				_item.put("DEP_REMIT", corr.getDependencia());
				_item.put("ESTADO", corr.getEstado());
				_item.put("FECH_DOC", new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(corr.getFechaDocumento()));
				_item.put("DEP_ORI", corr.getDependenciaOriginadora());
				_item.put("NOM_ORI", corr.getOriginador());
				_item.put("CGC", corr.getLugarTrabajo());
				_item.put("TIPO_CORR", corr.getTipoCorrespondencia());
				_item.put("TIPO_EMISION", corr.getEmision_nombre());
				_item.put("FLUJO_FIRMA", corr.isFirmaDigital() ? "DIGITAL" : "MANUAL");
				_item.put("DESPA_FIS", corr.isDespachoFisico() ? "SI" : "NO");
				_item.put("URGENTE", corr.isUrgente() ? "SI" : "NO");
				_item.put("CONDIF", corr.isConfidencial() ? "SI": "NO");
				_item.put("CGC_DESTINA", corr.getDestinatario_cgc());
				_item.put("COPIA", corr.getCopia_dependencia());
				_item.put("CGC_COPIA", corr.getCopia_cgc());
				_item.put("RESPONSABLE", corr.getResponsable());
				_item.put("FECH_ULT_ACT", new SimpleDateFormat(Utilitario.FORMATO_FECHA_FRONT).format(corr.getFechaUltActualizacion()));
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

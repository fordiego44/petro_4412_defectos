package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.TipoCorrespondencia;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelTipoCorrespondencia implements IReport<ByteArrayInputStream>{
	
	private List<TipoCorrespondencia> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelTipoCorrespondencia(List<TipoCorrespondencia> data,  String userCreate) {
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
			this.columns.put("CODIGO", "Código");
			this.columns.put("TIPO_CORRE", "Tipo de Correspondencia");
			this.columns.put("APLICA_ENV_INT", "Aplica Env Interna");
			this.columns.put("APLICA_ENV_EXT", "Aplica Env Externo");
			this.columns.put("APLICA_REC_INT", "Aplica Rec Interna");
			this.columns.put("APLICA_REC_EXT", "Aplica Rec Externa");
			this.columns.put("REQUIERE_FECHA", "Requiere fecha");
			this.columns.put("MARCA_FINALIZADA", "Marca Finaliza Gestor");
			this.columns.put("MANUAL_CORRE", "Manual Corresp");
			this.columns.put("SECUENCIA", "Secuencia");
			this.columns.put("MULTIPLE", "Multiple");
			this.columns.put("REQ_COPIA", "Requiere Copia");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (TipoCorrespondencia dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("CODIGO", dat.getCodigoTipoCorr()==null?"":dat.getCodigoTipoCorr());
				_item.put("TIPO_CORRE", dat.getNombreTipoCorr()==null?"":dat.getNombreTipoCorr());
				_item.put("APLICA_ENV_INT", dat.getmAplicaEnvInterna()==null?"":dat.getmAplicaEnvInterna());
				_item.put("APLICA_ENV_EXT", dat.getmAplicaEnvExterna()==null?"":dat.getmAplicaEnvExterna());
				_item.put("APLICA_REC_INT", dat.getmAplicaRecInterna()==null?"":dat.getmAplicaRecInterna());
				_item.put("APLICA_REC_EXT", dat.getmAplicaRecExterna()==null?"":dat.getmAplicaRecExterna());
				_item.put("REQUIERE_FECHA", dat.getmRequiereFecha()==null?"":dat.getmRequiereFecha());
				_item.put("MARCA_FINALIZADA", dat.getmFinalizaAceptar()==null?"":dat.getmFinalizaAceptar());
				_item.put("MANUAL_CORRE", dat.getmManualCorresp()==null?"":dat.getmManualCorresp());
				_item.put("SECUENCIA", dat.getSecuencia()==null?"":dat.getSecuencia());
				_item.put("MULTIPLE", dat.getmMultiple()==null?"":dat.getmMultiple());
				_item.put("REQ_COPIA", dat.getReqCopia()==null?"":dat.getReqCopia());
				
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA TIPO CORRESPONDENCIA");
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

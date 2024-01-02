package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.TipoAccion;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelTipoAccion implements IReport<ByteArrayInputStream>{
	
	private List<TipoAccion> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelTipoAccion(List<TipoAccion> data,  String userCreate) {
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
			this.columns.put("NOMBRE", "Nombre Acción");
			this.columns.put("TEXT_REQ", "Text Req");
			this.columns.put("MULTIPLE", "Múltiple");
			this.columns.put("MARCA_REGQ_TEXT_RESP", "Marca Req.Text Resp.");
			this.columns.put("MARCA_ENVIO_MAIL_RESP", "Marca Env.Mail.Rta");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (TipoAccion dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("CODIGO", dat.getCodigoAccion()==null?"":dat.getCodigoAccion());
				_item.put("NOMBRE", dat.getNombreAccion()==null?"":dat.getNombreAccion());
				_item.put("TEXT_REQ", dat.getmTextoReq()==null?"":dat.getmTextoReq());
				_item.put("MULTIPLE", dat.getmMultipli()==null?"":dat.getmMultipli());
				_item.put("MARCA_REGQ_TEXT_RESP", dat.getmReqTextoRta()==null?"":dat.getmReqTextoRta());
				_item.put("MARCA_ENVIO_MAIL_RESP", dat.getmEnviaMailRta()==null?"":dat.getmEnviaMailRta());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA TIPO ACCION");
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

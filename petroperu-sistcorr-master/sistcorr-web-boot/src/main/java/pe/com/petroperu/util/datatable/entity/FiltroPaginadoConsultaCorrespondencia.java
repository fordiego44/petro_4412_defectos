package pe.com.petroperu.util.datatable.entity;

import java.util.List;

import pe.com.petroperu.util.datatable.DataTableColumnSpecs;

public class FiltroPaginadoConsultaCorrespondencia {
	
	private String draw;
	private List<DetailColumn> columns;
	private List<DetailOrder> order;
	private Integer start;
	private Integer length;
	private DetailSearch search;
	private String correlativo;
	private String codigoEstado;
	private String fechaRegistroDesde;
	private String fechaRegistroHasta;
	private String numeroDocumentoInterno;
	private String fechaDocumentoInterno;
	private String codigoDependenciaRemitente;
	private String codigoDependenciaDestino;
	private String codigoTipoCorrespondencia;
	private String nombreDependenciaExterna;
	private String guiaRemision;
	private String asunto;
	private String procedencia;
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	public List<DetailColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<DetailColumn> columns) {
		this.columns = columns;
	}
	public List<DetailOrder> getOrder() {
		return order;
	}
	public void setOrder(List<DetailOrder> order) {
		this.order = order;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public DetailSearch getSearch() {
		return search;
	}
	public void setSearch(DetailSearch search) {
		this.search = search;
	}
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getFechaRegistroDesde() {
		return fechaRegistroDesde;
	}
	public void setFechaRegistroDesde(String fechaRegistroDesde) {
		this.fechaRegistroDesde = fechaRegistroDesde;
	}
	public String getFechaRegistroHasta() {
		return fechaRegistroHasta;
	}
	public void setFechaRegistroHasta(String fechaRegistroHasta) {
		this.fechaRegistroHasta = fechaRegistroHasta;
	}
	public String getNumeroDocumentoInterno() {
		return numeroDocumentoInterno;
	}
	public void setNumeroDocumentoInterno(String numeroDocumentoInterno) {
		this.numeroDocumentoInterno = numeroDocumentoInterno;
	}
	public String getFechaDocumentoInterno() {
		return fechaDocumentoInterno;
	}
	public void setFechaDocumentoInterno(String fechaDocumentoInterno) {
		this.fechaDocumentoInterno = fechaDocumentoInterno;
	}
	public String getCodigoDependenciaRemitente() {
		return codigoDependenciaRemitente;
	}
	public void setCodigoDependenciaRemitente(String codigoDependenciaRemitente) {
		this.codigoDependenciaRemitente = codigoDependenciaRemitente;
	}
	public String getCodigoDependenciaDestino() {
		return codigoDependenciaDestino;
	}
	public void setCodigoDependenciaDestino(String codigoDependenciaDestino) {
		this.codigoDependenciaDestino = codigoDependenciaDestino;
	}
	public String getCodigoTipoCorrespondencia() {
		return codigoTipoCorrespondencia;
	}
	public void setCodigoTipoCorrespondencia(String codigoTipoCorrespondencia) {
		this.codigoTipoCorrespondencia = codigoTipoCorrespondencia;
	}
	public String getNombreDependenciaExterna() {
		return nombreDependenciaExterna;
	}
	public void setNombreDependenciaExterna(String nombreDependenciaExterna) {
		this.nombreDependenciaExterna = nombreDependenciaExterna;
	}
	public String getGuiaRemision() {
		return guiaRemision;
	}
	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getProcedencia() {
		return procedencia;
	}
	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}
	
	
	
}

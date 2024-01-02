package pe.com.petroperu.sistcorr.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.Estado;

@Repository
public class ISistcorrDAOImpl extends JdbcDaoSupport implements SistcorrDAO {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public ISistcorrDAOImpl(@Qualifier("dataSourceBDSistcorr") DataSource dataSource) {
		setDataSource(dataSource);
	}

	public Long contadorBandeja(String bandeja, String usuario) {
		this.logger.info("[INCIO] contadorBandeja " + bandeja + " " + usuario);
		Long cantidad = null;
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sp_contador_bandeja").declareParameters(new SqlParameter[] {
							new SqlParameter("@in_bandeja", 12), new SqlParameter("@in_usuario", 12) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@in_bandeja", bandeja)
					.addValue("@in_usuario", usuario);
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-2");
			for (Map row : rows) {
				cantidad = Long
						.valueOf((row.get("cantidad") != null) ? Long.parseLong(row.get("cantidad").toString()) : 0L);
				this.logger.info("res " + cantidad);
			}

		} catch (Exception e) {
			this.logger.error("[ERROR] contadorBandeja", e);
		}
		return cantidad;
	}

	public List<Estado> listarEstados(String tipo) {
		// TICKET 9000003992
		this.logger.info("[INICIO] listarEstados");
		List<Estado> lista = new ArrayList<Estado>();
		SimpleJdbcCall call = null;
		try {
			tipo = (tipo == null) ? "%" : "%" + tipo + "%";

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_estados")
					.declareParameters(new SqlParameter[] { new SqlParameter("@CodigoProceso", 12) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@CodigoProceso",
					tipo);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			// TICKET 9000003992
			this.logger.info("[INFO] listarEstados " + " This is info : " + resultMap.toString());
//			System.out.println(resultMap.toString());
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				Estado estado = new Estado();
				estado.setCodigoEstado((row.get("COdigoEstado") == null) ? "" : row.get("COdigoEstado").toString());
				estado.setEstado((row.get("Estado") == null) ? "" : row.get("Estado").toString());
				lista.add(estado);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarEstados", e);
		}
		// TICKET 9000003992
		this.logger.info("[FIN] listarEstados");
		return lista;
	}
}

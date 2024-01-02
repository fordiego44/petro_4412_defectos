package pe.com.petroperu.filenet.dao;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.filenet.model.ItemTipoCorrespondencia;
import pe.com.petroperu.model.Contratacion;
import pe.com.petroperu.model.DependenciaUnidadMatricial;
import pe.com.petroperu.model.Devolucion;
import pe.com.petroperu.model.Estado;
import pe.com.petroperu.model.Expediente;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.Integrante;
import pe.com.petroperu.model.OrdenServicio;
import pe.com.petroperu.model.ReemplazoAdicion;
import pe.com.petroperu.model.Tracking;
import pe.com.petroperu.model.UsuarioRemplazo;
import pe.com.petroperu.model.Valija;
import pe.com.petroperu.model.VentaBases;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;

@Repository
public class FilenetDAOImpl extends JdbcDaoSupport implements IFilenetDAO {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/* 9000004276 - INICIO */
	@Autowired
	@Qualifier("emFactoryBDFilenet")
	private EntityManagerFactory entityManagerBDFilenet;
	/* 9000004276 - FIN */	
	@Autowired
	public FilenetDAOImpl(@Qualifier("dataSourceBDFilenet") DataSource dataSource) {
		setDataSource(dataSource);
	}

	public List<ItemFilenet> listarPaises(String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_paises_CEE").declareParameters(new SqlParameter[] {
							new SqlParameter("@CodigoCiudad", Types.VARCHAR), new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@CodigoCiudad", "")
					.addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoPais") == null) ? "" : row.get("CodigoPais").toString());
				item.setDescripcion((row.get("Pais") == null) ? "" : row.get("Pais").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarPaises", e);
		}
		return lista;
	}

	public List<ItemFilenet> listarDepartamentos(String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_departamentos_CEE")
					.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDepartamento") == null) ? "" : row.get("CodigoDepartamento").toString());
				item.setDescripcion((row.get("Departamento") == null) ? "" : row.get("Departamento").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDepartamentos", e);
		}
		return lista;
	}

	public List<ItemFilenet> listarProvincias(String codDepartamento, String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_provincias_CEE").declareParameters(new SqlParameter[] {
							new SqlParameter("@CodigoDepartamento", Types.VARCHAR), new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoDepartamento", codDepartamento).addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoProvincia") == null) ? "" : row.get("CodigoProvincia").toString());
				item.setDescripcion((row.get("Provincia") == null) ? "" : row.get("Provincia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarProvincias", e);
		}
		return lista;
	}

	public List<ItemFilenet> listarDistritos(String codDepartamento, String codProvincia, String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_distritos_CEE")
					.declareParameters(new SqlParameter[] { new SqlParameter("@CodigoDepartamento", Types.VARCHAR),
							new SqlParameter("@CodigoProvincia", Types.VARCHAR), new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoDepartamento", codDepartamento).addValue("@CodigoProvincia", codProvincia)
					.addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDistrito") == null) ? "" : row.get("CodigoDistrito").toString());
				item.setDescripcion((row.get("Distrito") == null) ? "" : row.get("Distrito").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDistritos", e);
		}
		return lista;
	}

	public List<ItemFilenet> listarLugares(String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_lista_lugares")
					.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoLugar") == null) ? "" : row.get("CodigoLugar").toString());
				item.setDescripcion((row.get("Nombre") == null) ? "" : row.get("Nombre").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarLugares", e);
		}
		return lista;
	}

	public List<ItemTipoCorrespondencia> listarTiposCorresponciaEmision(String texto) {
		List<ItemTipoCorrespondencia> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_select_tipo_correspondencia_sistcorr");

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemTipoCorrespondencia item = new ItemTipoCorrespondencia();
				item.setCodigo((row.get("CodigoTipoCorr") == null) ? "" : row.get("CodigoTipoCorr").toString());
				item.setDescripcion(
						(row.get("TipoCorrespondencia") == null) ? "" : row.get("TipoCorrespondencia").toString());
				item.setMultipleDestinatario(row.get("multiple").toString().equals("1"));
				item.setIntena(row.get("interna").toString().equals("1"));
				item.setExterna(row.get("externa").toString().equals("1"));
				
				// adicion 9-3874
				if (row.get("copia") != null && !row.get("copia").equals("") ) {
					item.setCopia(row.get("copia").toString().equals("1"));
				} else {
					item.setCopia(false);
				}
				if (row.get("destinatario") != null && !row.get("destinatario").equals("") ) {
					item.setDestinatario(String.valueOf(row.get("destinatario")));
				} else {
					item.setDestinatario("0");
				}
				// fin adicion 9-3874
				
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarTiposCorresponciaEmision", e);
		}
		return lista;
	}

	public List<ItemFilenet> listarDependencias(String codLugar, String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_dependencias")
					.declareParameters(new SqlParameter[] { new SqlParameter("@CodigoLugar", Types.VARCHAR),
							new SqlParameter("@Cadena", Types.VARCHAR), new SqlParameter("@Funcionario", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoLugar", codLugar).addValue("@Cadena", texto).addValue("@Funcionario", "");

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		return lista;
	}
	
	// TICKET 9000003944
	public List<ItemFilenet> listarDependenciasInterno(String codLugar, String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_dependencias_interno")
					.declareParameters(new SqlParameter[] { new SqlParameter("@CodigoLugar", Types.VARCHAR),
							new SqlParameter("@Cadena", Types.VARCHAR), new SqlParameter("@Funcionario", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoLugar", codLugar).addValue("@Cadena", texto).addValue("@Funcionario", "");

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		return lista;
	}
	// FIN TICKET
	
	public List<ItemFilenet> listarDependenciasNuevo(String codLugar, String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_lista_Dependencias_activas");

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		return lista;
	}
	
	public List<ItemFilenet> listarDependenciasRemitente(String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_dependencias_consulta")
					.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		return lista;
	}
	
	public List<ItemFilenet> listarDependenciasAsignacion(String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_lista_dependecias_todas")
					.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("codigodependencia") == null) ? "" : row.get("codigodependencia").toString());
				item.setDescripcion((row.get("dependencia") == null) ? "" : row.get("dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependenciasAsignacion", e);
		}
		return lista;
	}

	public List<ItemFilenet> listarDependenciasExternas(String texto) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_dependencias_externas")
					.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependenciasExternas", e);
		}
		return lista;
	}

	public List<ItemFilenet> listarFuncionarios(String codDependencia) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_usuarioFirmante")
					.declareParameters(new SqlParameter[] { new SqlParameter("@codDepend", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@codDepend",
					codDependencia);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("Registro") == null) ? "" : row.get("Registro").toString().toLowerCase());
				item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		return lista;
	}

	public ItemFilenet obtenerFirmante(String codDependencia) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_usuarioFirmante")
					.declareParameters(new SqlParameter[] { new SqlParameter("@codDepend", 4) });
			logger.info("Parametro dependencia:" + codDependencia);
			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@codDepend",
					Integer.valueOf(Integer.parseInt(codDependencia)));

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("Registro") == null) ? "" : row.get("Registro").toString().toLowerCase());
				item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		if (lista.isEmpty()) {
			return null;
		}
		return lista.get(0);
	}

	public ItemFilenet obtenerFirmanteRutaAprobacion(String codDependencia) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_usuarioFirmante")
					.declareParameters(new SqlParameter[] { new SqlParameter("@codDepend", 4) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@codDepend",
					Integer.valueOf(Integer.parseInt(codDependencia)));

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("Registro") == null) ? "" : row.get("Registro").toString().toLowerCase());
				item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
				item.setCodSup(codDependencia);
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		if (lista.isEmpty()) {
			return null;
		}
		return lista.get(0);
	}

	public String[] obtenerCGC(String codLugar, String codDependencia) {
		String[] cgcs = new String[2];
		SimpleJdbcCall call = null;
		try {
			cgcs[0] = "";
			cgcs[1] = "";

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_cgc").declareParameters(new SqlParameter[] {
							new SqlParameter("@CodLugar", Types.VARCHAR), new SqlParameter("@CodDependencia", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@CodLugar", codLugar)
					.addValue("@CodDependencia", codDependencia);
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				cgcs[0] = (row.get("CGC_LUGAR") == null) ? "" : row.get("CGC_LUGAR").toString();
				cgcs[1] = (row.get("CGC_DEPENDENCIA") == null) ? "" : row.get("CGC_DEPENDENCIA").toString();
			}
			this.logger.info("[INFO] obtenerCGC " + cgcs[0] + " " + cgcs[1]);
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerCGC", e);
		}
		return cgcs;
	}

	public String obtenerEmailFuncionario(String codUsuario) {
		String email = null;
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_email_funcionario")
					.declareParameters(new SqlParameter[] { new SqlParameter("@codUsuario", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@codUsuario",
					codUsuario);
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				email = (row.get("Email") == null) ? "" : row.get("Email").toString();
			}
			this.logger.info("[INFO] obtenerEmailFuncionario " + codUsuario + ":" + email);
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerEmailFuncionario", e);
		}
		return email;
	}

	public List<ItemFilenet> listarFuncionariosPorDependencia(String codDependencia) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_Lista_UsuariosXDep")
					.declareParameters(new SqlParameter[] { new SqlParameter("@codDepend", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@codDepend",
					Integer.valueOf(Integer.parseInt(codDependencia)));

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("Registro") == null) ? "" : row.get("Registro").toString().toLowerCase());
				item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependencias", e);
		}
		if (lista.isEmpty()) {
			return null;
		}
		return lista;
	}

	public String generarCorrelativoTemporal() {
		String correlativo = null;
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_generar_correlativo").declareParameters(
							new SqlParameter[] { new SqlParameter("@Param1", Types.VARCHAR), new SqlParameter("@Param2", Types.VARCHAR),
									new SqlParameter("@Param3", Types.VARCHAR), new SqlParameter("@Correlativo", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Param1", "NUDC")
					.addValue("@Param2", "").addValue("@Param3", "").addValue("@Correlativo", "");
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				correlativo = (row.get("") == null) ? "" : row.get("").toString();
			}
			this.logger.info("[INFO] generarCorrelativoTemporal " + correlativo);
		} catch (Exception e) {
			this.logger.error("[ERROR] generarCorrelativoTemporal", e);
		}
		return correlativo;
	}

	public String[] obtenerFechaCGCUsuario(String usuario) {
		this.logger.info("[INICIO] obtenerFechaCGCUsuario");
		String[] respuesta = new String[3];
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_optieneFechaCGC_usuario")
					.declareParameters(new SqlParameter[] { new SqlParameter("@usuario", Types.VARCHAR),
							(SqlParameter) new SqlOutParameter("@fecha", Types.VARCHAR),
							(SqlParameter) new SqlOutParameter("@cgc", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@usuario", usuario)
					.addValue("@fecha", "").addValue("@cgc", "");
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			respuesta[0] = usuario;
			respuesta[1] = resultMap.get("@cgc").toString();
			respuesta[2] = resultMap.get("@fecha").toString();
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerFechaCGCUsuario", e);
		}
		this.logger.info("[FIN] obtenerFechaCGCUsuario");
		return respuesta;
	}

	public String[] obtenerRUCEmailDependenciaExterna(String codigoDependencia) {
		this.logger.info("[INICIO] obtenerRUCEmailDependenciaExterna " + codigoDependencia);
		String[] respuesta = new String[2];
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_dependencia_ext")
					.declareParameters(new SqlParameter[] { new SqlParameter("@idDependencia", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@idDependencia",
					codigoDependencia);
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				respuesta[0] = (row.get("ruc") == null) ? "" : row.get("ruc").toString();
				respuesta[1] = (row.get("email") == null) ? "" : row.get("email").toString();
				this.logger.info(respuesta[0] + " " + respuesta[1]);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerRUCEmailDependenciaExterna", e);
		}
		this.logger.info("[FIN] obtenerRUCEmailDependenciaExterna");
		return respuesta;
	}

	public List<ItemFilenet> obtenerDependenciaPorUsuario(String username) {
		this.logger.info("[INICIO] obtenerDependenciaPorUsuario " + username);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_lista_dependecias_general")
					.declareParameters(new SqlParameter[] { new SqlParameter("@username", Types.VARCHAR),
							new SqlParameter("@query", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@username", username)
					.addValue("@query", "");
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("codigodependencia") == null) ? "" : row.get("codigodependencia").toString());
				item.setDescripcion((row.get("dependencia") == null) ? "" : row.get("dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerDependenciaPorUsuario", e);
		}
		this.logger.info("[FIN] obtenerDependenciaPorUsuario");
		return lista;
	}
	
	// TICKET 9000003944
	public List<ItemFilenet> obtenerDependenciaPorUsuarioConsulta(String username) {
		this.logger.info("[INICIO] obtenerDependenciaPorUsuarioConsulta " + username);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_lista_dependecias_general_consultas")
					.declareParameters(new SqlParameter[] { new SqlParameter("@username", Types.VARCHAR),
							new SqlParameter("@query", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@username", username)
					.addValue("@query", "");
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("codigodependencia") == null) ? "" : row.get("codigodependencia").toString());
				item.setDescripcion((row.get("dependencia") == null) ? "" : row.get("dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerDependenciaPorUsuarioConsulta", e);
		}
		this.logger.info("[FIN] obtenerDependenciaPorUsuarioConsulta");
		return lista;
	}
	// FIN TICKET

	public ItemFilenet obtenerLugarPorDependencia(String codDependencia) {
		ItemFilenet item = new ItemFilenet();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_lugar")
					.declareParameters(new SqlParameter[] { new SqlParameter("@codDependencia", 4) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@codDependencia",
					Integer.valueOf(Integer.parseInt(codDependencia)));
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				item = new ItemFilenet();
				item.setCodigo((row.get("CodigoLugar") == null) ? "" : row.get("CodigoLugar").toString());
				item.setDescripcion((row.get("Nombre") == null) ? "" : row.get("Nombre").toString());
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerLugarPorDependencia", e);
		}
		this.logger.info("[FIN] obtenerLugarPorDependencia");
		return item;
	}

	public List<ItemFilenet> listarDependenciasExternas(String codDepartamento, String codProvincia, String codDistrito,
			String texto) {
		this.logger.info("[INICIO] listarDependenciasExternas");
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_filtrar_dependencias_externas")
					.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR),
							new SqlParameter("@codDepartamento", Types.VARCHAR), new SqlParameter("@codProvincia", Types.VARCHAR),
							new SqlParameter("@codDistrito", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@Cadena", (texto == null) ? "" : texto).addValue("@codDepartamento", codDepartamento)
					.addValue("@codProvincia", codProvincia).addValue("@codDistrito", codDistrito);
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("id") == null) ? "" : row.get("id").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependenciasExternas", e);
		}
		this.logger.info("[FIN] listarDependenciasExternas");
		return lista;
	}

	@Override
	public List<ItemFilenet> listarDependenciasSuperiores(String codDependencia) {
		logger.info("[INICIO] listarDependenciasSuperiores " + codDependencia);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_lista_dependencias_superiores_FirmaDigitalWEB")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@CodDep", Types.VARCHAR)
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource().addValue("@CodDep", codDependencia);
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("codigodependencia") == null) ? "" : row.get("codigodependencia").toString());
				item.setDescripcion((row.get("dependencia") == null) ? "" : row.get("dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarDependenciasSuperiores", e);
		}
		logger.info("[FIN] listarDependenciasSuperiores");
		return lista;
	}

	@Override
	public List<ItemFilenet> listarDependenciasSubordinadas(String codDependencia) {
		logger.info("[INICIO] listarDependenciasSubordinadas " + codDependencia);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtiene_dep_subordinadas_web")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@Dep", Types.VARCHAR),
							new SqlParameter("@xva_DSClausulaIN", Types.VARCHAR),
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource()
					.addValue("@Dep", codDependencia)
					.addValue("@xva_DSClausulaIN", "");
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarDependenciasSubordinadas", e);
		}
		logger.info("[FIN] listarDependenciasSubordinadas");
		return lista;
	}

	@Override
	public List<ItemFilenet> listarTodosFuncionarios(String filtro) {
		logger.info("[INICIO] listarTodosFuncionarios " + filtro);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sp_listar_funcionarios_web")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@filtro", Types.VARCHAR)
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource()
					.addValue("@filtro", filtro);
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("codigo") == null) ? "" : row.get("codigo").toString());
				item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarTodosFuncionarios", e);
		}
		logger.info("[FIN] listarTodosFuncionarios");
		return lista;
	}

	@Override
	public List<ItemFilenet> listarTodosDependencias(String filtro) {
		logger.info("[INICIO] listarTodosDependencias " + filtro);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sp_listar_dependencias_web")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@filtro", Types.VARCHAR)
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource()
					.addValue("@filtro", filtro);
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("NombreDependencias") == null) ? "" : row.get("NombreDependencias").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarTodosDependencias", e);
		}
		logger.info("[FIN] listarTodosDependencias");
		return lista;
	}

	@Override
	public List<ItemFilenet> listarTodosDependenciasExternas(String filtro) {
		logger.info("[INICIO] listarTodosDependenciasExternas " + filtro);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sp_listar_dependencias_ext_web")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@filtro", Types.VARCHAR)
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource()
					.addValue("@filtro", filtro);
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("codigo") == null) ? "" : row.get("codigo").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarTodosDependenciasExternas", e);
		}
		logger.info("[FIN] listarTodosDependenciasExternas");
		return lista;
	}
	
	// TICKET 9000003514
	public List<Estado> listarEstados(String tipo) {
		this.logger.info("[INICIO] listarEstados");
		List<Estado> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_estados")
					.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", 12) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@Cadena", (tipo == null) ? "" : tipo);
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
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
		this.logger.info("[FIN] listarEstados");
		return lista;
	}
	
	public List<Estado> listarEstadosAsignacion() {
		this.logger.info("[INICIO] listarEstados");
		List<Estado> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_estados_asig");

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource());
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
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
		this.logger.info("[FIN] listarEstados");
		return lista;
	}
	
	public List<Estado> listarTiposCorrespondencia() {
		this.logger.info("[INICIO] listarEstados");
		List<Estado> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_tipos_corresp")
					.declareParameters(new SqlParameter[] { new SqlParameter("@TipoTransaccion", 12), new SqlParameter("@Cadena", 12) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@TipoTransaccion", "CR").addValue("@Cadena", "%");
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				Estado estado = new Estado();
				estado.setCodigoEstado((row.get("CodigoTipoCorr") == null) ? "" : row.get("CodigoTipoCorr").toString());
				estado.setEstado((row.get("TipoCorrespondencia") == null) ? "" : row.get("TipoCorrespondencia").toString());
				lista.add(estado);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarEstados", e);
		}
		this.logger.info("[FIN] listarEstados");
		return lista;
	}
	
	public List<Funcionario> listarPersonaAsignada(String codigoDependencia, String codigoLugar, String Cadena, String MInclNoActivo) {
		this.logger.info("[INICIO] listarPersonaAsignada");
		List<Funcionario> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_select_lista_funcionarios")
					.declareParameters(new SqlParameter[] { new SqlParameter("@CodigoDependencia", 12), new SqlParameter("@CodigoLugar", 12), 
							new SqlParameter("@Cadena", 12), new SqlParameter("@MInclNoActivo", 12) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoDependencia", (codigoDependencia == null) ? null : codigoDependencia)
					.addValue("@CodigoLugar", (codigoLugar == null) ? null : codigoLugar)
					.addValue("@Cadena", (Cadena == null) ? "%" : Cadena)
					.addValue("@MInclNoActivo", (MInclNoActivo == null) ? "SI" : MInclNoActivo);
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				Funcionario estado = new Funcionario();
				estado.setId((row.get("Registro") == null) ? "" : row.get("Registro").toString());
				estado.setNombreApellido((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
				lista.add(estado);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarEstados", e);
		}
		this.logger.info("[FIN] listarEstados");
		return lista;
	}
	// FIN TICKET 9000003514
	
	// TICKET 9000003780
	public List<ItemFilenet> listarDependenciasNivelJerarquia(String codigo, String cgc) {
		this.logger.info("[INICIO] listarDependenciasNivelJerarquia");
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			//texto = (texto == null) ? "" : texto;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_dependencias_desde_nivel_jerarquia")
					.declareParameters(new SqlParameter[] { new SqlParameter("@codigoJerarquia", Types.VARCHAR), new SqlParameter("@CodigoCGC", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@codigoJerarquia", codigo)
					.addValue("@CodigoCGC", cgc);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				item.setCodigoAux(row.get("CodigoLugar") == null ? "" : row.get("CodigoLugar").toString());
				item.setDescripcionAux(row.get("Nombre") == null ? "" : row.get("Nombre").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarDependenciasNivelJerarquia", e);
		}
		this.logger.info("[FIN] listarDependenciasNivelJerarquia");
		return lista;
	}
	// FIN TICKET 9000003780

	// TICKET 9000003997
	public List<ItemFilenet> obtenerStatusCorrespondencia(String nroDocumento) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			nroDocumento = (nroDocumento == null) ? "" : nroDocumento;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_estatus_correspondencia").declareParameters(new SqlParameter[] {
							new SqlParameter("@NumDocInterno", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@NumDocInterno", nroDocumento);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("Radicado") == null) ? "" : row.get("Radicado").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				item.setCodigoAux((row.get("Estado") == null) ? "" : row.get("Estado").toString());
				item.setDescripcionAux((row.get("Rechazo") == null) ? "" : row.get("Rechazo").toString());
				item.setCodSup((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerStatusCorrespondencia", e);
		}
		return lista;
	}
	// FIN TICKET

	// TICKET 9000003994
	public List<ItemFilenet> obtenerDependenciasGestor(String usuario) {
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			usuario = (usuario == null) ? "" : usuario;

			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_dependencias_del_gestor").declareParameters(new SqlParameter[] {
							new SqlParameter("@usuariogestor", Types.VARCHAR) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@usuariogestor", usuario);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				item.setCodigoAux((row.get("Registro") == null) ? "" : row.get("Registro").toString());
				item.setDescripcionAux((row.get("Jefe") == null) ? "" : row.get("Jefe").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] obtenerStatusCorrespondencia", e);
		}
		return lista;
	}
	// FIN TICKET
	
	// TICKET 9000003944
	public List<DependenciaUnidadMatricial> buscarDependencias(int codigo, String nombre, String tipo, String jefe) {
		List<DependenciaUnidadMatricial> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_admin_select_dependencia").declareParameters(new SqlParameter[] {
							new SqlParameter("@codigoDependencia", Types.INTEGER), new SqlParameter("@dependencia", Types.VARCHAR), 
							new SqlParameter("@tipo", Types.VARCHAR), new SqlParameter("@jefe", Types.VARCHAR)});

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@codigoDependencia", codigo==0?null:codigo)
					.addValue("@dependencia", "".equalsIgnoreCase(nombre)?null:nombre)
					.addValue("@tipo", "".equalsIgnoreCase(tipo)?null:tipo)
					.addValue("@jefe", "".equalsIgnoreCase(jefe)?null:jefe);

			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				DependenciaUnidadMatricial item = new DependenciaUnidadMatricial();
				item.setIdDependenciaUnidadMatricial((row.get("id") == null)? 0L : Long.valueOf(String.valueOf(row.get("id"))));
				item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
				item.setNombre((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
				item.setTipo((row.get("TIPO") == null) ? "" : row.get("TIPO").toString());
				item.setEstado((row.get("Activo") == null) ? "" : row.get("Activo").toString());
				item.setJefe((row.get("Jefe") == null) ? "" : row.get("Jefe").toString());
				item.setCantidadIntegrantes((row.get("Integrantes") == null) ? 0 : Integer.valueOf(String.valueOf(row.get("Integrantes"))));
				item.setSiglas((row.get("Sigla") == null) ? "" : String.valueOf(row.get("Sigla")));
				item.setJerarquia((row.get("CodigoJerarquia") == null) ? "" : String.valueOf(row.get("CodigoJerarquia")));
				item.setLugarTrabajo((row.get("CodigoLugar") == null) ? "" : String.valueOf(row.get("CodigoLugar")));
				item.setDependenciaSuperior((row.get("CodigoDependenciaSup") == null) ? "" : String.valueOf(row.get("CodigoDependenciaSup")));
				item.setDescripcionCargo((row.get("DescripcionCargo") == null) ? "" : String.valueOf(row.get("DescripcionCargo")));
				lista.add(item);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] buscarDependencias", e);
		}
		return lista;
	}
	
	@Override
	public List<ItemFilenet> listarTipoUnidadMatricial() {
		logger.info("[INICIO] listarTipoUnidadMatricial ");
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_listar_unidadmatricial")
					.declareParameters(new SqlParameter[] {
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource();
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("ID") == null) ? "" : row.get("ID").toString());
				item.setDescripcion((row.get("Nombre") == null) ? "" : row.get("Nombre").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarTipoUnidadMatricial", e);
		}
		logger.info("[FIN] listarTipoUnidadMatricial");
		return lista;
	}
	
	@Override
	public List<ItemFilenet> listarJerarquia() {
		logger.info("[INICIO] listarJerarquia ");
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_listar_jerarquia")
					.declareParameters(new SqlParameter[] {
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource();
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoJerarquia") == null) ? "" : row.get("CodigoJerarquia").toString());
				item.setDescripcion((row.get("Jerarquia") == null) ? "" : row.get("Jerarquia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarJerarquia", e);
		}
		logger.info("[FIN] listarJerarquia");
		return lista;
	}
	
	@Override
	public List<ItemFilenet> listarDependenciasUnidadMatricial(int jerarquia, String codLugar){
		logger.info("[INICIO] listarJerarquia ");
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_obtener_dependencias_desde_nivel_jerarquia")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@codigoJerarquia", Types.INTEGER), new SqlParameter("@CodigoLugar", Types.VARCHAR)
					});
			SqlParameterSource sqlParameterIN = new MapSqlParameterSource()
					.addValue("@codigoJerarquia", jerarquia).addValue("@CodigoLugar", codLugar);
			Map<String, Object> resultMap = call.execute(sqlParameterIN);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoJerarquia") == null) ? "" : row.get("CodigoJerarquia").toString());
				item.setDescripcion((row.get("Jerarquia") == null) ? "" : row.get("Jerarquia").toString());
				lista.add(item);
			}
		} catch (Exception e) {
			logger.error("[ERROR] listarJerarquia", e);
		}
		logger.info("[FIN] listarJerarquia");
		return lista;
	}
	
	@Override
	public List<ItemFilenet> registrarDependencia(DependenciaUnidadMatricial dep, String usuario) {
		logger.info("[INICIO] registrarDependencia:" + dep.getTipoUnidadMatricial());
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_admin_insertar_dependencia")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@codigoDep", Types.VARCHAR), new SqlParameter("@dependencia", Types.VARCHAR), 
							new SqlParameter("@codigoDepSuperior", Types.INTEGER), new SqlParameter("@codigoLugar", Types.VARCHAR),
							new SqlParameter("@usuario", Types.VARCHAR), new SqlParameter("@sigla", Types.VARCHAR), 
							new SqlParameter("@CodigoJerarquia", Types.INTEGER), new SqlParameter("@DescripcionCargo", Types.VARCHAR), new SqlOutParameter("@mensaje", Types.VARCHAR),
							new SqlParameter("@usuarioAdmin", Types.VARCHAR), new SqlParameter("@Activo", Types.VARCHAR), 
							new SqlParameter("@TipoUMatricial", Types.INTEGER), new SqlParameter("@Tipo", Types.VARCHAR), new SqlParameter("@SinJefe", Types.VARCHAR)//TICKET 9000004410 ADD PARAMETER SinJefe
					});
			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@codigoDep", dep.getCodigo())
					.addValue("@dependencia", dep.getNombre()).addValue("@codigoDepSuperior", ("0".equalsIgnoreCase(dep.getDependenciaSuperior()))?null:Integer.valueOf(dep.getDependenciaSuperior())).addValue("@codigoLugar", dep.getLugarTrabajo())
					.addValue("@usuario", dep.getJefe()).addValue("@sigla", dep.getSiglas()).addValue("@CodigoJerarquia", dep.getJerarquia())
					.addValue("@DescripcionCargo", dep.getDescripcionCargo()).addValue("@mensaje", "").addValue("@usuarioAdmin", usuario).addValue("@Activo", dep.getEstado())
					.addValue("@TipoUMatricial", dep.getTipoUnidadMatricial()).addValue("@Tipo", dep.getTipo()).addValue("@SinJefe", ((dep.getJefe() != null && dep.getJefe().equalsIgnoreCase("0"))?("SI"):("NO")));
			
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			logger.info("MENSAJE:" + resultMap.get("@mensaje"));
			logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
			logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
			ItemFilenet it = new ItemFilenet();
			it.setDescripcion(resultMap.get("@mensaje")==null?"":String.valueOf(resultMap.get("@mensaje")));
			lista.add(it);
			logger.info("IT:" + lista.get(0).getDescripcion());
			/*List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoJerarquia") == null) ? "" : row.get("CodigoJerarquia").toString());
				item.setDescripcion((row.get("Jerarquia") == null) ? "" : row.get("Jerarquia").toString());
				lista.add(item);
			}*/
		} catch (Exception e) {
			logger.error("[ERROR] registrarDependencia", e);
		}
		logger.info("[FIN] registrarDependencia");
		return lista;
	}
	
	@Override
	public List<ItemFilenet> registrarIntegrante(String integrante, int codigoDependencia) {
		logger.info("[INICIO] registrarIntegrante");
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_admin_asignar_integrante")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@CodigoDependencia", Types.INTEGER),new SqlParameter("@Registro", Types.VARCHAR)
					});
			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoDependencia", codigoDependencia).addValue("@Registro", integrante);
			
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			/*List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoJerarquia") == null) ? "" : row.get("CodigoJerarquia").toString());
				item.setDescripcion((row.get("Jerarquia") == null) ? "" : row.get("Jerarquia").toString());
				lista.add(item);
			}*/
			
		} catch (Exception e) {
			logger.error("[ERROR] registrarIntegrante", e);
		}
		logger.info("[FIN] registrarIntegrante");
		return lista;
	}
	
	@Override
	public List<ItemFilenet> eliminarIntegrante(String integrante, int codigoDependencia) {
		logger.info("[INICIO] eliminarIntegrante: " + integrante + "-" + codigoDependencia);
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_admin_eliminar_integrante")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@CodigoDependencia", Types.INTEGER),new SqlParameter("@Registro", Types.VARCHAR)
					});
			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoDependencia", codigoDependencia).addValue("@Registro", integrante);
			
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			/*List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoJerarquia") == null) ? "" : row.get("CodigoJerarquia").toString());
				item.setDescripcion((row.get("Jerarquia") == null) ? "" : row.get("Jerarquia").toString());
				lista.add(item);
			}*/
			
		} catch (Exception e) {
			logger.error("[ERROR] eliminarIntegrante", e);
		}
		logger.info("[FIN] eliminarIntegrante");
		return lista;
	}
	
	@Override
	public List<ItemFilenet> modificarDependencia(DependenciaUnidadMatricial dep, String usuario) {
		logger.info("[INICIO] modificarDependencia");
		List<ItemFilenet> lista = new ArrayList<>();
		SimpleJdbcCall call = null;
		try {
			call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_admin_update_dependencia")
					.declareParameters(new SqlParameter[] {
							new SqlParameter("@IdDep", Types.INTEGER), new SqlParameter("@codigoDep", Types.VARCHAR), new SqlParameter("@dependencia", Types.VARCHAR), 
							new SqlParameter("@codigoDepSuperior", Types.INTEGER), new SqlParameter("@codigoLugar", Types.VARCHAR),
							new SqlParameter("@usuario", Types.VARCHAR), new SqlParameter("@sigla", Types.VARCHAR), 
							new SqlParameter("@CodigoJerarquia", Types.INTEGER), new SqlParameter("@DescripcionCargo", Types.VARCHAR), new SqlOutParameter("@mensaje", Types.VARCHAR),
							new SqlParameter("@usuarioAdmin", Types.VARCHAR), new SqlParameter("@Activo", Types.VARCHAR), 
							new SqlParameter("@TipoUMatricial", Types.INTEGER), new SqlParameter("@Tipo", Types.VARCHAR),new SqlParameter("@SinJefe", Types.VARCHAR)//TICKET 9000004410 ADD PARAMETER SinJefe
					});
			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@IdDep", dep.getIdDependenciaUnidadMatricial()).addValue("@codigoDep", dep.getCodigo())
					.addValue("@dependencia", dep.getNombre()).addValue("@codigoDepSuperior", dep.getDependenciaSuperior()).addValue("@codigoLugar", dep.getLugarTrabajo())
					.addValue("@usuario", dep.getJefe()).addValue("@sigla", dep.getSiglas()).addValue("@CodigoJerarquia", dep.getJerarquia())
					.addValue("@DescripcionCargo", dep.getDescripcionCargo()).addValue("@mensaje", "").addValue("@usuarioAdmin", usuario).addValue("@Activo", dep.getEstado())
					.addValue("@TipoUMatricial", dep.getTipoUnidadMatricial()).addValue("@Tipo", dep.getTipo()).addValue("@SinJefe", ((dep.getJefe() != null && dep.getJefe().equalsIgnoreCase("0"))?("SI"):("NO")));
			
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			
			logger.info("MENSAJE:" + resultMap.get("@mensaje"));
			logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
			logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
			ItemFilenet it = new ItemFilenet();
			it.setDescripcion(resultMap.get("@mensaje")==null?"":String.valueOf(resultMap.get("@mensaje")));
			lista.add(it);
			dep.setMensajeRpt(it.getDescripcion());
			/*List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo((row.get("CodigoJerarquia") == null) ? "" : row.get("CodigoJerarquia").toString());
				item.setDescripcion((row.get("Jerarquia") == null) ? "" : row.get("Jerarquia").toString());
				lista.add(item);
			}*/
		} catch (Exception e) {
			logger.error("[ERROR] modificarDependencia", e);
		}
		logger.info("[FIN] modificarDependencia");
		return lista;
	}
	
	@Override
	public List<Integrante> listarIntegrantesUM(String codigoDependencia) {
		this.logger.info("[INICIO] listarIntegrantesUM");
		List<Integrante> lista = new ArrayList<>();
		SimpleJdbcCall call = null;

		try {
			call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
					.withProcedureName("sic_sp_admin_select_integrantes")
					.declareParameters(new SqlParameter[] { new SqlParameter("@CodigoDependencia", 12) });

			MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CodigoDependencia", ("0".equalsIgnoreCase(codigoDependencia)) ? 0 : Integer.valueOf(codigoDependencia));
			Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
			List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
			for (Map row : rows) {
				Integrante estado = new Integrante();
				estado.setCodigoIntegrante((row.get("Registro") == null) ? "" : row.get("Registro").toString());
				estado.setNombreIntegrante((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
				lista.add(estado);
			}
		} catch (Exception e) {
			this.logger.error("[ERROR] listarIntegrantesUM", e);
		}
		this.logger.info("[FIN] listarIntegrantesUM");
		return lista;
	}
	// FIN TICKET
	
	// TICKET 9000004408
		public List<ItemFilenet> obtenerDependenciasJefe(String usuario) {
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				usuario = (usuario == null) ? "" : usuario;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_obtener_dependencias_del_jefe").declareParameters(new SqlParameter[] {
								new SqlParameter("@usuariogestor", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@usuariogestor", usuario);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
					item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
					item.setCodigoAux((row.get("Registro") == null) ? "" : row.get("Registro").toString());
					item.setDescripcionAux((row.get("Jefe") == null) ? "" : row.get("Jefe").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] obtenerDependenciasJefe", e);
			}
			return lista;
		}
		// FIN TICKET
		
		// TICKET 9000004409
		public String validarRemplazoVigenteUsuario(String usuario) {
			this.logger.info("[INICIO] validarUsuarioRemplazoVigente");
			String respuesta ="";
			SimpleJdbcCall call = null;

			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_consulta_reemplazo_vigente")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Usuario", Types.VARCHAR),
								(SqlParameter) new SqlOutParameter("@mensaje", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Usuario", usuario)
						.addValue("@mensaje", "");
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				//respuesta[0] = usuario;
				respuesta = resultMap.get("@mensaje").toString();
			} catch (Exception e) {
				this.logger.error("[ERROR] validarUsuarioRemplazoVigente", e);
			}
			this.logger.info("[FIN] validarUsuarioRemplazoVigente");
			return respuesta;
		}
		
		public List<UsuarioRemplazo> obtenerUsuarioRemplazo(String usuario) {
			List<UsuarioRemplazo> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				usuario = (usuario == null) ? "" : usuario;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_obtener_reemplazos_x_usuario").declareParameters(new SqlParameter[] {
								new SqlParameter("@Usuario", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Usuario", usuario);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					UsuarioRemplazo item = new UsuarioRemplazo();
					item.setUsername((row.get("Usuario") == null) ? "" : row.get("Usuario").toString());
					item.setNombres((row.get("NombreUsuario") == null) ? "" : row.get("NombreUsuario").toString());
		
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] obtenerUsuarioRemplazo", e);
			}
			return lista;
		}
		
		// FIN TICKET 9000004409
	
		/* 9000004276 - INICIO */
		@Override
		public List<Object[]> consultar_comprobantes_paginado(String usuario, String radicado, String fechaDesde, String fechaHasta, String nroBatch,
				String ruc, String nroComprobante, Integer codigoEstado, Integer codDependencia, String razonSocial,
				Integer itemsPorPagina, Integer numeroPagina, String exportaExcel, String nombreColumna, String desc){
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {

				em = entityManagerBDFilenet.createEntityManager();

				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_comprobantes_v2");

				query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroBatch", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("RUC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroComprobante", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodDependencia", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("RazonSocial", String.class, ParameterMode.IN);
				
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

				query.setParameter("Usuario", usuario);
				query.setParameter("Radicado", radicado);
				query.setParameter("FechaDesde", fechaDesde);
				query.setParameter("FechaHasta", fechaHasta);
				query.setParameter("NroBatch", nroBatch);
				query.setParameter("RUC", ruc);
				query.setParameter("NroComprobante", nroComprobante);
				query.setParameter("CodigoEstado", codigoEstado);
				query.setParameter("CodDependencia", codDependencia);
				query.setParameter("RazonSocial", razonSocial);
				
				query.setParameter("ItemsPorPagina", itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", exportaExcel);
				query.setParameter("nombreColumna", nombreColumna);
				query.setParameter("desc", desc);

				query.execute();
				listRows = query.getResultList();
				Object[] arrTotal = { 0 };
				arrTotal[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, arrTotal);

			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_comprobantes_paginado", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}		

		@Override
		public List<ItemFilenet> listarTiposProceso() {
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_tipos_procesos");

				Map<String, Object> resultMap = call.execute();
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("TipoProceso") == null) ? "" : row.get("TipoProceso").toString());
					item.setDescripcion((row.get("Descripcion") == null) ? "" : row.get("Descripcion").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarTiposProceso", e);
			}
			return lista;
		}
		

		@Override
		public List<Object[]> consultar_contrataciones_paginado(String usuario, String nroProceso, String tipoProceso, String nroMemo, Integer codDependencia, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {

				em = entityManagerBDFilenet.createEntityManager();

				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_contratacion_v2");

				query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroProceso", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TipoProceso", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroMemo", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodDependencia", Integer.class, ParameterMode.IN);
				
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

				query.setParameter("Usuario", usuario);
				query.setParameter("NroProceso", nroProceso);
				query.setParameter("TipoProceso", tipoProceso);
				query.setParameter("NroMemo", nroMemo);
				query.setParameter("CodDependencia", codDependencia);
				
				query.setParameter("ItemsPorPagina", itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", exportaExcel);
				query.setParameter("nombreColumna", nombreColumna);
				query.setParameter("desc", desc);

				query.execute();
				listRows = query.getResultList();
				Object[] arrTotal = { 0 };
				arrTotal[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, arrTotal);

			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_contrataciones_paginado", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}

		@Override
		public Object[] consultar_detalle_comprobante(String usuario, String correlativo) {
			EntityManager em = null;
			Object[] o = null;
			try {

				em = entityManagerBDFilenet.createEntityManager();

				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_comprobantes_v2");

				query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroBatch", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("RUC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroComprobante", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodDependencia", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("RazonSocial", String.class, ParameterMode.IN);
				
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

				query.setParameter("Usuario", usuario);
				query.setParameter("Radicado", correlativo);
				query.setParameter("FechaDesde", "");
				query.setParameter("FechaHasta", "");
				query.setParameter("NroBatch", "");
				query.setParameter("RUC", "");
				query.setParameter("NroComprobante", "");
				query.setParameter("CodigoEstado", 0);
				query.setParameter("CodDependencia", 0);
				query.setParameter("RazonSocial", "");
				
				query.setParameter("ItemsPorPagina", 1);
				query.setParameter("NumeroPagina", 1);
				query.setParameter("ExportaExcel", "NO");
				query.setParameter("nombreColumna", "");
				query.setParameter("desc", "");

				query.execute();

				List<Object[]> listRows  = query.getResultList();				
				Object total = query.getOutputParameterValue("TotalRegistros");
				
				if (total != null && total instanceof Integer && (Integer) total > 0)
					o = listRows.get(0);

			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_detalle_comprobante", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return o;
		}

		@Override
		public List<ItemFilenet> listarDependenciasCEE(String registro, String cadena, String funcionario) {
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				cadena = (cadena == null) ? "" : cadena;
				funcionario = (funcionario == null) ? "" : funcionario;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_dependencias_remitente_CEE")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Registro", Types.VARCHAR),
								new SqlParameter("@Cadena", Types.VARCHAR), new SqlParameter("@Funcionario", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Registro", registro).addValue("@Cadena", cadena).addValue("@Funcionario", funcionario);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
					item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarDependencias", e);
			}
			return lista;
		}
		

		@Override
		public Contratacion consultar_detalle_contratacion(String usuario, String nroProceso) {
			Contratacion cont = null;
			EntityManager em = null;
			Object[] o = null;
			try {

				em = entityManagerBDFilenet.createEntityManager();

				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_detalle_contratacion_v2");

				query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroProceso", String.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("TipoProceso", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("NroMemo", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("Dependencia", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("RegistrContacto", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("Anexo", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("CantidadBases", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("ValorPliego", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("Objeto", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FechaIniVentaBase", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HIniVentaBase", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FechaCierreVentaBase", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HFinVentaBase", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FIniConsulta", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HIniConsulta", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FFinConsulta", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HFinConsulta", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FechaIniRecep", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HIniRecepcion", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HIniRecepcion1", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FFinRecepcion", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HFinRecepcion", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("HFinRecepcion1", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FechaIniImpugna", String.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("FechaFinImpugna", String.class, ParameterMode.OUT);
				

				query.setParameter("Usuario", usuario);
				query.setParameter("NroProceso", nroProceso);

				query.execute();

				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				
				cont = new Contratacion();
				cont.setNroProceso(query.getOutputParameterValue("NroProceso")== null ? "" : query.getOutputParameterValue("NroProceso").toString());
				cont.setTipoProceso(query.getOutputParameterValue("TipoProceso")== null ? "" : query.getOutputParameterValue("TipoProceso").toString());
				cont.setNroMemo(query.getOutputParameterValue("NroMemo")== null ? "" : query.getOutputParameterValue("NroMemo").toString());
				cont.setDependencia(query.getOutputParameterValue("Dependencia")== null ? "" : query.getOutputParameterValue("Dependencia").toString());
				cont.setPersonaContacto(query.getOutputParameterValue("RegistrContacto")  == null ? "" : query.getOutputParameterValue("RegistrContacto").toString());
				cont.setAnexo(query.getOutputParameterValue("Anexo")== null ? "" : query.getOutputParameterValue("Anexo").toString());
				cont.setCantidadBases(query.getOutputParameterValue("CantidadBases")== null ? "" : query.getOutputParameterValue("CantidadBases").toString());
				cont.setValorBase(query.getOutputParameterValue("ValorPliego")== null ? "" : query.getOutputParameterValue("ValorPliego").toString());
				cont.setObjetoProceso(query.getOutputParameterValue("Objeto")== null ? "" : query.getOutputParameterValue("Objeto").toString());
				cont.setVentaBasesFechaInicio(query.getOutputParameterValue("FechaIniVentaBase") == null ? "" : query.getOutputParameterValue("FechaIniVentaBase").toString().replace('-','/'));
				cont.setVentaBasesHoraInicio(query.getOutputParameterValue("HIniVentaBase") == null ? "" : query.getOutputParameterValue("HIniVentaBase").toString());
				cont.setVentaBasesFechaCierre(query.getOutputParameterValue("FechaCierreVentaBase") == null ? "" : query.getOutputParameterValue("FechaCierreVentaBase").toString().replace('-','/'));
				cont.setVentaBasesHoraCierre(query.getOutputParameterValue("HFinVentaBase") == null ? "" : query.getOutputParameterValue("HFinVentaBase").toString());
				cont.setConsultaBasesFechaInicio(query.getOutputParameterValue("FIniConsulta") == null ? "" : query.getOutputParameterValue("FIniConsulta").toString().replace('-','/'));
				cont.setConsultaBasesHoraInicio(query.getOutputParameterValue("HIniConsulta") == null ? "" : query.getOutputParameterValue("HIniConsulta").toString());
				cont.setConsultaBasesFechaCierre(query.getOutputParameterValue("FFinConsulta") == null ? "" : query.getOutputParameterValue("FFinConsulta").toString().replace('-','/'));
				cont.setConsultaBasesHoraCierre(query.getOutputParameterValue("HFinConsulta") == null ? "" : query.getOutputParameterValue("HFinConsulta").toString());
				cont.setRecepPropuestasFechaInicio(query.getOutputParameterValue("FechaIniRecep") == null ? "" : query.getOutputParameterValue("FechaIniRecep").toString().replace('-','/'));
				cont.setRecepPropuestasHoraInicio(query.getOutputParameterValue("HIniRecepcion") == null ? "" : query.getOutputParameterValue("HIniRecepcion").toString());
				cont.setRecepPropuestasHoraInicio1(query.getOutputParameterValue("HIniRecepcion1") == null ? "" : query.getOutputParameterValue("HIniRecepcion1").toString());
				cont.setRecepPropuestasFechaCierre(query.getOutputParameterValue("FFinRecepcion") == null ? "" : query.getOutputParameterValue("FFinRecepcion").toString().replace('-','/'));
				cont.setRecepPropuestasHoraCierre(query.getOutputParameterValue("HFinRecepcion") == null ? "" : query.getOutputParameterValue("HFinRecepcion").toString());
				cont.setRecepPropuestasHoraCierre1(query.getOutputParameterValue("HFinRecepcion1") == null ? "" : query.getOutputParameterValue("HFinRecepcion1").toString());
				cont.setImpugnacionesFechaInicio(query.getOutputParameterValue("FechaIniImpugna") == null ? "" : query.getOutputParameterValue("FechaIniImpugna").toString().replace('-','/'));
				cont.setImpugnacionesFechaCierre(query.getOutputParameterValue("FechaFinImpugna") == null ? "" : query.getOutputParameterValue("FechaFinImpugna").toString().replace('-','/'));

			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_detalle_contratacion", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return cont;
		}

		@Override
		public List<Object[]> consultar_venta_base(String radicado) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {
				em = entityManagerBDFilenet.createEntityManager();
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_ventabase");
				query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.IN);
				query.setParameter("Radicado", radicado);
				query.execute();
				
				listRows = query.getResultList();
			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_venta_base", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}

		@Override
		public List<Object[]> consultar_consultas_base(String nroProceso) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {
				em = entityManagerBDFilenet.createEntityManager();
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_consultabase");
				query.registerStoredProcedureParameter("NroProceso", String.class, ParameterMode.IN);
				query.setParameter("NroProceso", nroProceso);
				query.execute();
				
				listRows = query.getResultList();
			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_consultas_base", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}

		@Override
		public List<Object[]> consultar_propuestas(String radicado) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {
				em = entityManagerBDFilenet.createEntityManager();
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_propuestas");
				query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.IN);
				query.setParameter("Radicado", radicado);
				query.execute();
				
				listRows = query.getResultList();
			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_propuestas", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}

		@Override
		public List<Object[]> consultar_impugnaciones(String radicado) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {
				em = entityManagerBDFilenet.createEntityManager();
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_impugnaciones");
				query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.IN);
				query.setParameter("Radicado", radicado);
				query.execute();
				
				listRows = query.getResultList();
			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_impugnaciones", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}
		/* 9000004276 - FIN */
		
		//INICIO TICKET 9000004275
		@Override
		public List<ItemFilenet> listaCGC(String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_CGC")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoCGC") == null) ? "" : row.get("CodigoCGC").toString());
					item.setDescripcion((row.get("Nombre") == null) ? "" : row.get("Nombre").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarUsuarioCgc", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listaCouriers(String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_couriers")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoCourier") == null) ? "" : row.get("CodigoCourier").toString());
					item.setDescripcion((row.get("Courier") == null) ? "" : row.get("Courier").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarUsuarioCgc", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listaLugares(String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_lugares")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoLugar") == null) ? "" : row.get("CodigoLugar").toString());
					item.setDescripcion((row.get("Nombre") == null) ? "" : row.get("Nombre").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listaLugares", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listarCiudades(String codPais, String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_ciudades").declareParameters(new SqlParameter[] {
								new SqlParameter("@CodigoPais", Types.VARCHAR), new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@CodigoPais", codPais).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("Ciudad") == null) ? "" : row.get("Ciudad").toString());
					item.setDescripcion((row.get("Ciudad") == null) ? "" : row.get("Ciudad").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarCiudades", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listarCiudadesPorPais(String codPais, String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_lista_ciudades").declareParameters(new SqlParameter[] {
								new SqlParameter("@CodigoPais", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@CodigoPais", codPais);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("codigoCiudad") == null) ? "" : row.get("codigoCiudad").toString());
					item.setDescripcion((row.get("Ciudad") == null) ? "" : row.get("Ciudad").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarCiudades", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listaNumeradores() {
			// TODO Auto-generated method stub
			logger.info("[INICIO] lista_numeradores ");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_admin_select_lista_numeradores")
						.declareParameters(new SqlParameter[] {
						});
				SqlParameterSource sqlParameterIN = new MapSqlParameterSource();
				Map<String, Object> resultMap = call.execute(sqlParameterIN);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoNumerador") == null) ? "" : row.get("CodigoNumerador").toString());
					item.setDescripcion((row.get("CodigoNumerador") == null) ? "" : row.get("CodigoNumerador").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				logger.error("[ERROR] lista_numeradores", e);
			}
			logger.info("[FIN] lista_numeradores");
			return lista;
		}

		@Override
		public List<ItemFilenet> listaFuncionariosTodos() {
			// TODO Auto-generated method stub
			logger.info("[INICIO] lista_numeradores ");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_funcionarios_todos")
						.declareParameters(new SqlParameter[] {
						});
				SqlParameterSource sqlParameterIN = new MapSqlParameterSource();
				Map<String, Object> resultMap = call.execute(sqlParameterIN);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("Registro") == null) ? "" : row.get("Registro").toString());
					item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				logger.error("[ERROR] lista_numeradores", e);
			}
			logger.info("[FIN] lista_numeradores");
			return lista;
		}

		@Override
		public List<ItemFilenet> obtenerTablas() {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_tablas_configuracion");

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource());

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("ds_description") == null) ? "" : row.get("ds_description").toString());
					item.setDescripcion((row.get("ds_description") == null) ? "" : row.get("ds_description").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarDistritos", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> guardarFuncionario(Funcionario funcionario, String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				
			if (funcionario.getAccion().equalsIgnoreCase("")){	
					
					call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_insert_funcionario")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@Registro", Types.VARCHAR),
								new SqlParameter("@Nombre1", Types.VARCHAR),
								new SqlParameter("@Nombre2", Types.VARCHAR),
								new SqlParameter("@Apellido1", Types.VARCHAR),
								new SqlParameter("@Apellido2", Types.VARCHAR),
								new SqlParameter("@Email", Types.VARCHAR),
								new SqlParameter("@CodigoDependencia", Types.INTEGER),
								new SqlParameter("@MNotificacion", Types.VARCHAR),
								new SqlParameter("@MParticipaProceso", Types.VARCHAR),
								new SqlParameter("@Tipofuncionario", Types.VARCHAR),
								new SqlParameter("@Ficha", Types.VARCHAR),
								new SqlParameter("@CodigoOperacion", Types.VARCHAR),
								new SqlParameter("@MSupervisor", Types.VARCHAR),
								new SqlParameter("@MActivo", Types.VARCHAR),
								new SqlOutParameter("@Mensaje", Types.VARCHAR)
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@Registro", funcionario.getRegistro())
									.addValue("@Nombre1", funcionario.getPrimerNombre())
									.addValue("@Nombre2", funcionario.getSegundoNombre())
									.addValue("@Apellido1", funcionario.getApellidoPaterno())
									.addValue("@Apellido2", funcionario.getApellidoMaterno())
									.addValue("@Email", funcionario.getEmail())
									.addValue("@CodigoDependencia", funcionario.getDependencia()==null?0:funcionario.getDependencia())
									.addValue("@MNotificacion", funcionario.getNotificaciones())
									.addValue("@MParticipaProceso", funcionario.getParticipaProceso())
									.addValue("@Tipofuncionario", funcionario.getTipoFuncionario())
									.addValue("@Ficha", funcionario.getFicha())
									.addValue("@CodigoOperacion",funcionario.getOperacion()==null?"":funcionario.getOperacion())
									.addValue("@MSupervisor", funcionario.getSupervisor())
									.addValue("@MActivo", funcionario.getActivo())
									.addValue("@Mensaje", "");					
									
							Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
							logger.info("MENSAJE:" + resultMap.get("@mensaje"));
							logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
							logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
							ItemFilenet it = new ItemFilenet();
							it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
							lista.add(it);
							logger.info("IT:" + lista.get(0).getDescripcion());
			}else{
				
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_actilizar_funcionario")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@Id", Types.VARCHAR),
								new SqlParameter("@Registro", Types.VARCHAR),
								new SqlParameter("@Nombre1", Types.VARCHAR),
								new SqlParameter("@Nombre2", Types.VARCHAR),
								new SqlParameter("@Apellido1", Types.VARCHAR),
								new SqlParameter("@Apellido2", Types.VARCHAR),
								new SqlParameter("@Email", Types.VARCHAR),
								new SqlParameter("@CodigoDependencia", Types.INTEGER),
								new SqlParameter("@MNotificacion", Types.VARCHAR),
								new SqlParameter("@MParticipaProceso", Types.VARCHAR),
								new SqlParameter("@Tipofuncionario", Types.VARCHAR),
								new SqlParameter("@Ficha", Types.VARCHAR),
								new SqlParameter("@CodigoOperacion", Types.VARCHAR),
								new SqlParameter("@MSupervisor", Types.VARCHAR),
								new SqlParameter("@MActivo", Types.VARCHAR),
								new SqlOutParameter("@Mensaje", Types.VARCHAR)
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@Id", funcionario.getId())
									.addValue("@Registro", funcionario.getRegistro())
									.addValue("@Nombre1", funcionario.getPrimerNombre())
									.addValue("@Nombre2", funcionario.getSegundoNombre())
									.addValue("@Apellido1", funcionario.getApellidoPaterno())
									.addValue("@Apellido2", funcionario.getApellidoMaterno())
									.addValue("@Email", funcionario.getEmail())
									.addValue("@CodigoDependencia", funcionario.getDependencia()==null?0:funcionario.getDependencia())
									.addValue("@MNotificacion", funcionario.getNotificaciones())
									.addValue("@MParticipaProceso", funcionario.getParticipaProceso())
									.addValue("@Tipofuncionario", funcionario.getTipoFuncionario())
									.addValue("@Ficha", funcionario.getFicha())
									.addValue("@CodigoOperacion",funcionario.getOperacion()==null?"":funcionario.getOperacion())
									.addValue("@MSupervisor", funcionario.getSupervisor())
									.addValue("@MActivo", funcionario.getActivo())
									.addValue("@Mensaje", "");
									
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@mensaje")==null?"":String.valueOf(resultMap.get("@mensaje")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
			}		
			} catch (Exception e) {
				this.logger.error("[ERROR] guardarFuncionario", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> eliminarFuncionario(Integer idFuncionario, String registro, String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_eliminar_funcionario")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@Id", Types.VARCHAR),
								new SqlParameter("@Registro", Types.VARCHAR),
								new SqlOutParameter("@Mensaje", Types.VARCHAR)
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@Id", idFuncionario)
					.addValue("@Registro", registro)
					.addValue("@Mensaje", "");
				
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
			} catch (Exception e) {
				this.logger.error("[ERROR] guardarFuncionario", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listarDependenciaApoyo(String tipoReemplazo, String term) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
		
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_dependenciasportiporeemp")
						.declareParameters(new SqlParameter[] { new SqlParameter("@tipoReemp", Types.VARCHAR),
								new SqlParameter("@query", Types.VARCHAR), 
								new SqlParameter("@UsuSal", Types.VARCHAR),
								new SqlParameter("@rol", Types.VARCHAR),
								new SqlParameter("@UsuEnt", Types.VARCHAR)});

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@tipoReemp", tipoReemplazo)
						.addValue("@query", term == null?"":term)
						.addValue("@UsuSal", "")
						.addValue("@rol", "")
						.addValue("@UsuEnt", "");

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoDependencia") == null) ? "" : row.get("CodigoDependencia").toString());
					item.setDescripcion((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
					lista.add(item);
				}
				
				
			} catch (Exception e) {
				this.logger.error("[ERROR] listarDependenciaApoyo", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listarJefeXDependencia(String codDepend, String rol, String term) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
		
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_lista_funcionarios_xrolxdepend")
						.declareParameters(new SqlParameter[] { new SqlParameter("@rol", Types.VARCHAR),
								new SqlParameter("@depend", Types.VARCHAR), 
								new SqlParameter("@query", Types.VARCHAR)});

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@rol", rol)
						.addValue("@depend", codDepend)
						.addValue("@query", term == null?"":term);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("Registro") == null) ? "" : row.get("Registro").toString());
					item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
					lista.add(item);
				}
				
				
			} catch (Exception e) {
				this.logger.error("[ERROR] listarDependenciaApoyo", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> listarFuncionariosApoyo(String tipoReemp, String codDepend, String rol, String term) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
		
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_Funcionarios")
						.declareParameters(new SqlParameter[] { new SqlParameter("@tipoRemmp", Types.VARCHAR),
								new SqlParameter("@rol", Types.VARCHAR), 
								new SqlParameter("@codDepend", Types.VARCHAR), 
								new SqlParameter("@query", Types.VARCHAR)});

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@tipoRemmp", tipoReemp)
						.addValue("@rol", rol)
						.addValue("@codDepend", codDepend)
						.addValue("@query", term == null?"":term);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("Registro") == null) ? "" : row.get("Registro").toString());
					item.setDescripcion((row.get("NombreApellido") == null) ? "" : row.get("NombreApellido").toString());
					lista.add(item);
				}
				
				
			} catch (Exception e) {
				this.logger.error("[ERROR] listarDependenciaApoyo", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> comboRolDependenciaReemplazo(String codDepend) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
		
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_lista_rolesxdep")
						.declareParameters(new SqlParameter[] { new SqlParameter("@depend", Types.VARCHAR),
								new SqlParameter("@query", Types.VARCHAR)});

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@depend", codDepend)
						.addValue("@query", "");

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("rol") == null) ? "" : row.get("rol").toString());
					item.setDescripcion((row.get("rol") == null) ? "" : row.get("rol").toString());
					lista.add(item);
				}
				
				
			} catch (Exception e) {
				this.logger.error("[ERROR] listarDependenciaApoyo", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> eliminarReemplazo(Integer idReemplazo, String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_eliminar_estado_reemplazo")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@codReemp", Types.VARCHAR),
								new SqlOutParameter("@Mensaje", Types.VARCHAR)
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@codReemp", idReemplazo)
					.addValue("@Mensaje", "");
				
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
			} catch (Exception e) {
				this.logger.error("[ERROR] eliminarReemplazo", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> validarReemplazo(ReemplazoConsultaDTO reemplazo, String tipoReemplazo,
				String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			
			try{
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_validar_reemplazo")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@CDUsuarioSal", Types.VARCHAR),
								new SqlParameter("@CDUsuarioEnt", Types.VARCHAR),
								new SqlParameter("@FEInicio", Types.VARCHAR),
								new SqlParameter("@FEFin", Types.VARCHAR),
								new SqlParameter("@Tiporeemp", Types.VARCHAR),
								new SqlParameter("@codDep", Types.INTEGER),
								new SqlParameter("@rol", Types.VARCHAR),
								new SqlParameter("@NuevoReemp", Types.VARCHAR),
								new SqlOutParameter("@mensaje", Types.VARCHAR),
								new SqlOutParameter("@codConfirm", Types.INTEGER),
								new SqlParameter("@Refer", Types.VARCHAR),
								new SqlParameter("@IdReemp", Types.INTEGER),
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
					.addValue("@CDUsuarioSal", reemplazo.getUsuarioSaliente())
					.addValue("@CDUsuarioEnt", reemplazo.getUsuarioEntrante())
					.addValue("@FEInicio", reemplazo.getFechaInicio())
					.addValue("@FEFin",reemplazo.getFechaTermino())
					.addValue("@Tiporeemp", tipoReemplazo)
					.addValue("@codDep", reemplazo.getCodDependencia())
					.addValue("@rol", reemplazo.getRol())
					.addValue("@NuevoReemp", ((reemplazo.getAccion().equalsIgnoreCase(""))?("SI"):("NO")))
					.addValue("@mensaje", "")
					.addValue("@codConfirm", 0)
					.addValue("@codReemp", "")
					.addValue("@Refer", reemplazo.getReferencia())
					.addValue("@IdReemp", reemplazo.getId_reemplazo()==null?0:reemplazo.getId_reemplazo());
				
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				
					ItemFilenet item = new ItemFilenet();
					item.setCodigo(resultMap.get("@codConfirm") == null ? "" :resultMap.get("@codConfirm").toString());
					item.setDescripcion(resultMap.get("@mensaje") == null ? "" :resultMap.get("@mensaje").toString());
					lista.add(item);
				
				
			}catch (Exception e) {
				ItemFilenet item = new ItemFilenet();
				item.setCodigo("0");
				item.setDescripcion("Error:"+e.getMessage());
				lista.add(item);
				this.logger.error("[ERROR] validarReemplazo", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> guardarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_registrar_nuevoreemplazo")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@CDUsuSal", Types.VARCHAR),
								new SqlParameter("@CDUsuEnt", Types.VARCHAR),
								new SqlParameter("@FEInicio", Types.VARCHAR),
								new SqlParameter("@FEFin", Types.VARCHAR),
								new SqlParameter("@Tiporeemp", Types.VARCHAR),
								new SqlParameter("@codDep", Types.INTEGER),
								new SqlParameter("@rol", Types.VARCHAR),
								new SqlOutParameter("@codReemp", Types.INTEGER),
								new SqlParameter("@referencia", Types.VARCHAR),
								
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@CDUsuSal", reemplazo.getUsuarioSaliente())
									.addValue("@CDUsuEnt", reemplazo.getUsuarioEntrante())
									.addValue("@FEInicio", reemplazo.getFechaInicio())
									.addValue("@FEFin", reemplazo.getFechaTermino())
									.addValue("@Tiporeemp", reemplazo.getTipoReemplazo())
									.addValue("@codDep", reemplazo.getCodDependencia()==null?0:reemplazo.getCodDependencia())
									.addValue("@rol", reemplazo.getRol())
									.addValue("@codReemp", 0)
									.addValue("@referencia", reemplazo.getReferencia());
									
							Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
							logger.info("MENSAJE:" + resultMap.get("@codReemp"));
							logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
							logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
							ItemFilenet it = new ItemFilenet();
							it.setDescripcion(resultMap.get("@codReemp")==null?"":String.valueOf(resultMap.get("@codReemp")));
							lista.add(it);
							logger.info("IT:" + lista.get(0).getDescripcion());
			}catch (Exception e) {
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion("Error:"+e.getMessage());
				lista.add(it);
				this.logger.error("[ERROR] guardarReemplazoAdicion", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> obtenerRolDepOriginal(String usuarioEntrante, String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("SP_OBTENER_ROL_DEP_ORIGINAL")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@ivaUser", Types.VARCHAR),
								new SqlOutParameter("@rol", Types.VARCHAR),
								new SqlOutParameter("@ListCodDEp", Types.VARCHAR)
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@ivaUser", usuarioEntrante)
									.addValue("@rol", "")
									.addValue("@ListCodDEp", "");
									
							Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
							logger.info("MENSAJE:" + resultMap.get("@codReemp"));
							logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
							logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
							ItemFilenet it = new ItemFilenet();
							it.setCodigo(resultMap.get("@rol")==null?"":String.valueOf(resultMap.get("@rol")));
							it.setDescripcion(resultMap.get("@ListCodDEp")==null?"":String.valueOf(resultMap.get("@ListCodDEp")));
							lista.add(it);
							logger.info("IT:" + lista.get(0).getDescripcion());
			}catch (Exception e) {
				this.logger.error("[ERROR] guardarReemplazoAdicion", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> actualizarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_actualizar_registro_reemplazo")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@codReemp", Types.INTEGER),
								new SqlParameter("@CDUsuSal", Types.VARCHAR),
								new SqlParameter("@CDUsuEnt", Types.VARCHAR),
								new SqlParameter("@rol", Types.VARCHAR),
								new SqlParameter("@codDep", Types.INTEGER),
								new SqlParameter("@FEInicio", Types.VARCHAR),
								new SqlParameter("@FEFin", Types.VARCHAR),
								new SqlParameter("@refer", Types.VARCHAR)
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@codReemp", reemplazo.getId_reemplazo())
									.addValue("@CDUsuSal", reemplazo.getUsuarioSaliente())
									.addValue("@CDUsuEnt", reemplazo.getUsuarioEntrante())
									.addValue("@rol", reemplazo.getRol())
									.addValue("@codDep", reemplazo.getCodDependencia()==null?0:reemplazo.getCodDependencia())									
									.addValue("@FEInicio", reemplazo.getFechaInicio())
									.addValue("@FEFin", reemplazo.getFechaTermino())
									.addValue("@refer", reemplazo.getReferencia());
									
							Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
							logger.info("MENSAJE:" + resultMap.get("@codReemp"));
							logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
							logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
							ItemFilenet it = new ItemFilenet();
							it.setDescripcion(resultMap.get("@codReemp")==null?"":String.valueOf(resultMap.get("@codReemp")));
							lista.add(it);
							logger.info("IT:" + lista.get(0).getDescripcion());
			}catch (Exception e) {
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion("Error:"+e.getMessage());
				lista.add(it);
				this.logger.error("[ERROR] guardarReemplazoAdicion", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> obtenerValorVar(String userName, String variableName) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_obtener_valor_var")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@userlogin", Types.VARCHAR),
								new SqlParameter("@variablename", Types.VARCHAR),
								new SqlOutParameter("@valuestring", Types.VARCHAR),
								new SqlOutParameter("@valueint", Types.INTEGER)
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@userlogin", userName)
									.addValue("@variablename", variableName)
									.addValue("@valuestring","")
									.addValue("@valueint",0)
									;
									
							Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
							logger.info("MENSAJE:" + resultMap.get("@codReemp"));
							logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
							logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
							ItemFilenet it = new ItemFilenet();
							it.setCodigo(resultMap.get("@valueint")==null?"":String.valueOf(resultMap.get("@valueint")));
							it.setDescripcion(resultMap.get("@valuestring")==null?"":String.valueOf(resultMap.get("@valuestring")));
							lista.add(it);
							logger.info("IT:" + lista.get(0).getDescripcion());
			}catch (Exception e) {
				ItemFilenet it = new ItemFilenet();
				it.setCodigo("0");
				it.setDescripcion("Error:"+e.getMessage());
				lista.add(it);
				this.logger.error("[ERROR] obtenerValorVar", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> eliminarReemplazo(ReemplazoConsultaDTO reemplazo, String usuario) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_RemoverRemplazos")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@UsuEnt", Types.VARCHAR),
								new SqlParameter("@UsuSalt", Types.VARCHAR),
								new SqlParameter("@FEInicio", Types.VARCHAR),
								new SqlParameter("@FEFin", Types.VARCHAR),
								new SqlParameter("@codDep", Types.INTEGER),
								new SqlParameter("@rol", Types.VARCHAR),
								new SqlParameter("@codConfirm", Types.INTEGER),
								new SqlOutParameter("@s_Mensaje", Types.VARCHAR)
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@UsuEnt", reemplazo.getUsuarioEntrante())
									.addValue("@UsuSalt", reemplazo.getUsuarioSaliente())
									.addValue("@FEInicio", reemplazo.getFechaInicio())
									.addValue("@FEFin", reemplazo.getFechaTermino())
									.addValue("@codDep", reemplazo.getCodDependencia()==null?0:reemplazo.getCodDependencia())									
									.addValue("@rol", reemplazo.getRol())
									.addValue("@codConfirm", Integer.parseInt(reemplazo.getCodConfirm()));
									
							Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
							logger.info("MENSAJE:" + resultMap.get("@s_Mensaje"));
							logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
							logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
							ItemFilenet it = new ItemFilenet();
							it.setDescripcion(resultMap.get("@s_Mensaje")==null?"":String.valueOf(resultMap.get("@s_Mensaje")));
							lista.add(it);
							logger.info("IT:" + lista.get(0).getDescripcion());
			}catch (Exception e) {
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion("Error: " + e.getMessage());
				lista.add(it);
				this.logger.error("[ERROR] eliminarReemplazo", e);
			}
			return lista;
		}

		@Override
		public List<ReemplazoAdicion> listarReemplazosAdicion(ReemplazoConsultaDTO reemplazo, String usuario) {
			// TODO Auto-generated method stub
			List<ReemplazoAdicion> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_lista_Reemplazos_Adicion")
						.declareParameters(new SqlParameter[] { 
								new SqlParameter("@CDUsuarioEnt", Types.VARCHAR),
								new SqlParameter("@Tiporeemp", Types.VARCHAR), 
								new SqlParameter("@FEInicio", Types.VARCHAR), 
								new SqlParameter("@FEFin", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@CDUsuarioEnt", reemplazo.getUsuarioEntrante())
						.addValue("@Tiporeemp", "ADICION")
						.addValue("@FEInicio", reemplazo.getFechaInicio())
						.addValue("@FEFin", reemplazo.getFechaTermino());

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ReemplazoAdicion item = new ReemplazoAdicion();
					item.setId((row.get("Id") == null) ? 0 : Integer.parseInt(row.get("Id").toString()));
					item.setDependencia((row.get("Dependencia") == null) ? "" : row.get("Dependencia").toString());
					item.setEstado((row.get("Estado") == null) ? "" : row.get("Estado").toString());
					item.setFechaInicio((row.get("FechaInicio") == null) ? "" : row.get("FechaInicio").toString());
					item.setFechFin((row.get("FechaTermino") == null) ? "" : row.get("FechaTermino").toString());
					item.setRol((row.get("ROL") == null) ? "" : row.get("ROL").toString());
					item.setUsuarioEntrante((row.get("Usuario_Entrante") == null) ? "" : row.get("Usuario_Entrante").toString());
					item.setUsuarioSaliente((row.get("Usuario_Saliente") == null) ? "" : row.get("Usuario_Saliente").toString());
					
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarReemplazosAdicion", e);
			}
			return lista;
		}

		@Override
		public List<ItemFilenet> modificarValorVariale(String usuario, String variableName, String valorStr,
				Integer valor) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_modificar_valor_var")
						.declareParameters(new SqlParameter[] {
								new SqlParameter("@userlogin", Types.VARCHAR),
								new SqlParameter("@variablename", Types.VARCHAR),
								new SqlParameter("@valuestring", Types.VARCHAR),
								new SqlParameter("@valueint", Types.INTEGER)
							});
				
							MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
									.addValue("@userlogin", usuario)
									.addValue("@variablename", variableName)
									.addValue("@valuestring", valorStr)
									.addValue("@valueint", valor);
									
							Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
							logger.info("MENSAJE:" + "");
							logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
							logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
							ItemFilenet it = new ItemFilenet();
							it.setDescripcion(resultMap.get("@variablename")==null?"":String.valueOf(resultMap.get("@variablename")));
							lista.add(it);
							logger.info("IT:" + lista.get(0).getDescripcion());
			}catch (Exception e) {
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion("Error: " + e.getMessage());
				lista.add(it);
				this.logger.error("[ERROR] modificarValorVariale", e);
			}
			return lista;
		}
		
		@Override
		public List<ItemFilenet> listarLugaresTV(String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_lista_lugares_T")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoLugar") == null) ? "" : row.get("CodigoLugar").toString());
					item.setDescripcion((row.get("Nombre") == null) ? "" : row.get("Nombre").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarLugares", e);
			}
			return lista;
		}
		
		@Override
		public List<Object[]> consultaLog(String tabla, String usuario, String accion, String fechaDesde,
				String fechaHasta, String token, int length, int start, String columna, String orden, String excel,
				Locale locale) {
			// TODO Auto-generated method stub
			EntityManager em = null;
			List<Object[]> listRows = null;
			
			try{
				em = entityManagerBDFilenet.createEntityManager();
	            
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_log");
				
				query.registerStoredProcedureParameter("tabla", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaInicial", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaFinal", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);
				
				query.setParameter("tabla", tabla == null?"":tabla);
				query.setParameter("usuario", usuario == null?"":usuario);
				query.setParameter("accion", accion==null?"":accion);
				query.setParameter("FechaInicial", fechaDesde);
				query.setParameter("FechaFinal", fechaHasta);
				query.setParameter("ItemsPorPagina", length);
				query.setParameter("NumeroPagina", start);
				query.setParameter("ExportaExcel", excel);
				query.setParameter("TotalRegistros", 0);
				query.setParameter("nombreColumna", columna);
				query.setParameter("desc", orden);
								
				query.execute();
				
				listRows = query.getResultList();
				Object[] total = {0};
				total[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, total);		
				
			}catch(Exception e){
	            e.printStackTrace();
	        }finally{
	        	if (em != null) {em.close();}
	        }
			
			return listRows;
		}

		@Override
		public List<Object[]> consultaRolesAnterioresPaginado(String codUsuario, String tipoTransaccion,
				String tipoReemplazo, String correlativo, String fechaDesde, String fechaHasta,
				Integer codTipoCorrespondencia, String codigoEstado, String fechaDocumento, Integer codDestino,
				Integer codDepRemitente, String externa, String asunto, String nroDocumento, String rol,
				Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String tipo) {
			// TODO Auto-generated method stub
			EntityManager em = null;
			List<Object[]> listRows = null;
			try{
				em = entityManagerBDFilenet.createEntityManager();
	            
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_TrabajoAnteriores");
				
				query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TipoTransac", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TipoReemplazo", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoTipoCorr", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaDocumento", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodDepDestino", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoDepRemitente", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("DependenciaExterna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Asunto", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroDocInterno", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Rol", String.class, ParameterMode.IN);			
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

				query.setParameter("Usuario", codUsuario);
				//query.setParameter("Usuario", "jefe92");
				query.setParameter("TipoTransac", tipoTransaccion);
				query.setParameter("TipoReemplazo", tipoReemplazo);
				query.setParameter("Radicado", correlativo);
				query.setParameter("FechaDesde", fechaDesde);
				query.setParameter("FechaHasta", fechaHasta);
				query.setParameter("CodigoTipoCorr", codTipoCorrespondencia);
				query.setParameter("CodigoEstado", ((codigoEstado==null || codigoEstado.trim().equalsIgnoreCase(""))?(0):(Integer.parseInt(codigoEstado))));
				query.setParameter("FechaDocumento", fechaDocumento==null?"":fechaDocumento);
				query.setParameter("CodDepDestino", codDestino);
				query.setParameter("CodigoDepRemitente", codDepRemitente);
				query.setParameter("DependenciaExterna", externa==null?"":externa);
				query.setParameter("Asunto", asunto);
				query.setParameter("NroDocInterno", nroDocumento==null?"":nroDocumento);
				query.setParameter("Rol", rol==null?"":rol);
				query.setParameter("ItemsPorPagina", itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", tipo);
				query.setParameter("TotalRegistros", 0);
				query.setParameter("nombreColumna", columnaOrden);
				query.setParameter("desc", orden);
								
				query.execute();
				
				listRows = query.getResultList();
				
				Integer cantidadeReg = (Integer) query.getOutputParameterValue("TotalRegistros");
				if(listRows != null && listRows.size() > 0)
					listRows.get(0)[0] = cantidadeReg; 
			}catch(Exception e){
	            e.printStackTrace();
	        }finally{
	        	if (em != null) {em.close();}
	        }
			
			return listRows;
		}

		@Override
		public List<Object[]> consultaReemplazoAdicion(String idUsuario, Integer codDependencia, String rol,
				String usuarioSaliente, String usuarioEntrante, String fechaDesde, String fechaHasta, Integer estado,
				String referencia, String tipo, Integer itemsPorPagina, Integer numeroPagina, String excel,
				String columnaOrden, String orden) {
			// TODO Auto-generated method stub
			EntityManager em = null;
			List<Object[]> listRows = null;
			try{
				em = entityManagerBDFilenet.createEntityManager();
	            
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_consulta_reemplazo");
				
				query.registerStoredProcedureParameter("CodDepend", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("rol", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CDUsuarioSal", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CDUsuarioEnt", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FEInicio", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FEFin", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Estado", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Referen", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Tiporeemp", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);
				
				query.setParameter("CodDepend", codDependencia==null?0:codDependencia);
				query.setParameter("rol", rol==null?"":rol);
				query.setParameter("CDUsuarioSal", usuarioSaliente==null?"":usuarioSaliente);
				query.setParameter("CDUsuarioEnt", usuarioEntrante==null?"":usuarioEntrante);
				query.setParameter("FEInicio", fechaDesde);
				query.setParameter("FEFin", fechaHasta);
				query.setParameter("Estado", estado);
				query.setParameter("Referen",referencia==null?"":referencia);
				query.setParameter("Tiporeemp", tipo);
				query.setParameter("ItemsPorPagina", itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", excel);
				query.setParameter("TotalRegistros", 0);
				query.setParameter("nombreColumna", columnaOrden);
				query.setParameter("desc", orden);
				
				
				query.execute();
				listRows = query.getResultList();
				Object[] total = {0};
				total[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, total);
				
			}catch(Exception e){
	            e.printStackTrace();
	        }finally{
	        	if (em != null) {em.close();}
	        }
	        
			return listRows;
		}

		@Override
		public List<Object[]> consultaAdministracionFuncionarios(String idUsuario, Integer codDependencia,
				String registro, String nombres, String apellidos, Integer itemsPorPagina, Integer numeroPagina,
				String columnaOrden, String orden, String tipo) {
			// TODO Auto-generated method stub
			EntityManager em = null;
			List<Object[]> listRows = null;
			try{
				em = entityManagerBDFilenet.createEntityManager();
	            
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_funcionarios");
				
				query.registerStoredProcedureParameter("Registro", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Nombre1", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Apellido1", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoDependencia", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);
				
				query.setParameter("Registro", registro == null ? "" : registro);
				query.setParameter("Nombre1", nombres == null ? "" : nombres);
				query.setParameter("Apellido1", apellidos == null ? "" : apellidos);
				query.setParameter("CodigoDependencia", codDependencia==null ? 0 : codDependencia);
				query.setParameter("ItemsPorPagina",itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", tipo);
				query.setParameter("TotalRegistros", 0);
				query.setParameter("nombreColumna", columnaOrden);
				query.setParameter("desc", orden);
								
				query.execute();
				
				listRows = query.getResultList();
				Object[] total = {0};
				total[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, total);		
				
			}catch(Exception e){
	            e.printStackTrace();
	        }finally{
	        	if (em != null) {em.close();}
	        }
			
			return listRows;
		}

		@Override
		public List<Object[]> obtenerValorVar(String userName, String variableName, String valueSring,
				Integer valueInt) {
			// TODO Auto-generated method stub
			EntityManager em = null;
			List<Object[]> listRows = null;
			try{
				em = entityManagerBDFilenet.createEntityManager();
	            
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_obtener_valor_var");
				
				query.registerStoredProcedureParameter("userlogin", String.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("variablename", String.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("valueSsring", String.class, ParameterMode.INOUT);
				query.registerStoredProcedureParameter("valueint", Integer.class, ParameterMode.INOUT);
				
				query.setParameter("userlogin", userName);
				query.setParameter("variablename", "");
				query.setParameter("valuestring", "");
				query.setParameter("valueint", 0);
				
				query.execute();
				
				listRows = query.getResultList();
			}catch(Exception e){
	            e.printStackTrace();
	        }finally{
	        	if (em != null) {em.close();}
	        }
			
			return listRows;
		}
		
		//FIN TICKET 9000004275
		
		// TICKET 9000003866
		public List<ItemFilenet> obtenerDependenciasBandES(String username) {
			this.logger.info("[INICIO] obtenerDependenciasBandES " + username);
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;

			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_obtener_dependencias_del_usuario")
						.declareParameters(new SqlParameter[] { new SqlParameter("@usarioregistro", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@usarioregistro", username)
						.addValue("@query", "");
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("codigodependencia") == null) ? "" : row.get("codigodependencia").toString());
					item.setDescripcion((row.get("dependencia") == null) ? "" : row.get("dependencia").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] obtenerDependenciasBandES", e);
			}
			this.logger.info("[FIN] obtenerDependenciasBandES");
			return lista;
		}
		// FIN TICKET
		
		
	/*INI Ticket 9000004412*/
		public List<ItemFilenet> obtenerCentroGestionRemitente(String texto) {
			this.logger.info("[INICIO] obtenerCentroGestionRemitente");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_CGC")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoCGC") == null) ? "" : row.get("CodigoCGC").toString());
					item.setDescripcion(
							(row.get("Nombre") == null) ? "" : row.get("Nombre").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] obtenerCentroGestionRemitente", e);
			}
			return lista;
		}
		
		
		public List<ItemFilenet> listarProcesos(String texto) {
			this.logger.info("[INICIO] obtenerCentroGestionRemitente");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_procesos_contratacion")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("NroProceso") == null) ? "" : row.get("NroProceso").toString());
					item.setDescripcion((row.get("NroProceso") == null) ? "" : row.get("NroProceso").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] obtenerCentroGestionRemitente", e);
			}
			return lista;
		}
		
		public List<ItemFilenet> registrarValija(Valija valijas, String usuario, String cgc) {	
			logger.info("[INICIO] registrarValija");
			logger.info("[INICIO] registrarValija - "+usuario);
			logger.info("[INICIO] registrarValija-valija.getCodCentroGestionRemitente(): "+valijas.getCodCentroGestionRemitente());
			logger.info("[INICIO] registrarValija-valija.getCodCourier(): "+valijas.getCodCourier());
			logger.info("[INICIO] registrarValija-valija.getOrdenServicio(): "+valijas.getOrdenServicio());
			logger.info("[INICIO] registrarValija-valija.getIdentificadorValija(): "+valijas.getIdentificadorValija());
			SimpleJdbcCall call = null;
			List<ItemFilenet> lista = new ArrayList<>();
			try{
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_generar_valija_recibida")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Correlativo", Types.VARCHAR), 
								new SqlInOutParameter("@Usuario", Types.VARCHAR), 
								new SqlInOutParameter("@CodigoCGCOrigen", Types.VARCHAR), 
								new SqlInOutParameter("@CodigoCourier", Types.INTEGER),
								new SqlInOutParameter("@Guia", Types.VARCHAR), 
								new SqlInOutParameter("@RotuloPDF", Types.VARCHAR), 
								new SqlInOutParameter("@RotuloEtiqueta", Types.VARCHAR),
								new SqlInOutParameter("@RotuloMuestra", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje", Types.VARCHAR)
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Correlativo", valijas.getIdentificadorValija())
						.addValue("@Usuario",usuario )
						.addValue("@CodigoCGCOrigen",valijas.getCodCentroGestionRemitente())
						.addValue("@CodigoCourier", valijas.getCodCourier())
						.addValue("@Guia",valijas.getOrdenServicio())
						.addValue("@RotuloPDF", "")
						.addValue("@RotuloEtiqueta", valijas.getIdentificadorValija())
						.addValue("@RotuloMuestra", "")
						.addValue("@Mensaje", "");
			
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@Mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				logger.info("Batch:" + resultMap.get("@NroBatch"));
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				it.setCodigo(resultMap.get("@NroBatch")==null?"":String.valueOf(resultMap.get("@NroBatch")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
				
			}catch (Exception e) {
				logger.error("[ERROR] registarComprobante", e);
			}
			logger.info("[FIN] registarComprobante");
			
			return lista;
		}	
		
		public String obtenerCGCUsuario(String usuario) {
			this.logger.info("[INICIO] sic_sp_obtener_CGC_usuario");
			String respuesta = new String();
			SimpleJdbcCall call = null;

			try {
				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_obtener_CGC_usuario")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Usuario", Types.VARCHAR),
								(SqlParameter) new SqlOutParameter("@CodigoCGC", Types.VARCHAR)});

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Usuario", usuario)
						.addValue("@CodigoCGC", "");
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				respuesta = resultMap.get("@CodigoCGC").toString(); 
			} catch (Exception e) {
				this.logger.error("[ERROR] obtenerFechaCGCUsuario", e);
			}
			this.logger.info("[FIN] sic_sp_obtener_CGC_usuario"); 
			return respuesta;
		}
		
		public List<ItemFilenet> validarVentaBases(VentaBases ventaBases, String usuario) {
			logger.info("[INICIO] validarVentaBases:");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_valida_ventabases_fuerahora")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Usuario", Types.VARCHAR), 
								new SqlInOutParameter("@RUC", Types.VARCHAR), 
								new SqlInOutParameter("@NombreProveedor", Types.VARCHAR), 
								new SqlInOutParameter("@NroProceso", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje", Types.VARCHAR),  
								new SqlInOutParameter("@MVentaFueraHora", Types.VARCHAR)
						});
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Usuario", usuario)
						.addValue("@RUC", ventaBases.getRuc())
						.addValue("@NombreProveedor",ventaBases.getNombreProveedor())
						.addValue("@NroProceso", ventaBases.getNroProceso())
						.addValue("@Mensaje", "")
						.addValue("@MVentaFueraHora", ventaBases.getFueraHora());
				
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@Mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				//it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				it.setDescripcion("");
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
			
			} catch (Exception e) {
				logger.error("[ERROR] validarVentaBases", e);
			}
			logger.info("[FIN] validarVentaBases");
			return lista;
		}
		
		
		public List<ItemFilenet> registrarVentaBases(VentaBases ventaBases, String usuario) {
			logger.info("[INICIO] registrarVentaBases:");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_registrar_venta_bases")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Usuario", Types.VARCHAR), 
								new SqlInOutParameter("@RUC", Types.VARCHAR), 
								new SqlInOutParameter("@NombreProveedor", Types.VARCHAR), 
								new SqlInOutParameter("@NroProceso", Types.VARCHAR),
								new SqlInOutParameter("@RotuloPDF", Types.VARCHAR),
								new SqlInOutParameter("@RotuloEtiqueta", Types.VARCHAR),
								new SqlInOutParameter("@RotuloMuestra", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje1", Types.VARCHAR),
								new SqlInOutParameter("@Correlativo", Types.VARCHAR),
								new SqlInOutParameter("@MVentaFueraHora", Types.VARCHAR)
						});
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Usuario", usuario)
						.addValue("@RUC", ventaBases.getRuc())
						.addValue("@NombreProveedor",ventaBases.getNombreProveedor())
						.addValue("@NroProceso", ventaBases.getNroProceso())
						.addValue("@RotuloPDF", "")
						.addValue("@RotuloEtiqueta", "")
						.addValue("@RotuloMuestra", "")
						.addValue("@Mensaje", "")
						.addValue("@Mensaje1", "")
						.addValue("@Correlativo", "")
						.addValue("@MVentaFueraHora", ventaBases.getFueraHora());
				
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@mensaje")==null?"":String.valueOf(resultMap.get("@mensaje")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
			
			} catch (Exception e) {
				logger.error("[ERROR] registrarVentaBases", e);
			}
			logger.info("[FIN] registrarVentaBases");
			return lista;
		}
		
		public List<ItemFilenet> validarConsultaVentaBases(VentaBases ventaBases, String usuario) {
			logger.info("[INICIO] validarConsultaVentaBases:");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_valida_consultabases_fuerahora")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Usuario", Types.VARCHAR), 
								new SqlInOutParameter("@RUC", Types.VARCHAR), 
								new SqlInOutParameter("@NombreProveedor", Types.VARCHAR), 
								new SqlInOutParameter("@NroProceso", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje", Types.VARCHAR),  
								new SqlInOutParameter("@MVentaFueraHora", Types.VARCHAR)
						});
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Usuario", usuario)
						.addValue("@RUC", ventaBases.getRuc())
						.addValue("@NombreProveedor",ventaBases.getNombreProveedor())
						.addValue("@NroProceso", ventaBases.getNroProceso())
						.addValue("@Mensaje", "")
						.addValue("@MVentaFueraHora", ventaBases.getFueraHora());
				
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@Mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				//it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				it.setDescripcion("");
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
			
			} catch (Exception e) {
				logger.error("[ERROR] validarConsultaVentaBases", e);
			}
			logger.info("[FIN] validarConsultaVentaBases");
			return lista;
		}
		
		public List<ItemFilenet> registrarConsultaVentaBases(VentaBases ventaBases, String usuario) {
			logger.info("[INICIO] registrarConsultaVentaBases:");
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_registrar_consulta_bases")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Usuario", Types.VARCHAR), 
								new SqlInOutParameter("@RUC", Types.VARCHAR), 
								new SqlInOutParameter("@NombreProveedor", Types.VARCHAR), 
								new SqlInOutParameter("@NroProceso", Types.VARCHAR),
								new SqlInOutParameter("@RotuloPDF", Types.VARCHAR),
								new SqlInOutParameter("@RotuloEtiqueta", Types.VARCHAR),
								new SqlInOutParameter("@RotuloMuestra", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje1", Types.VARCHAR),
								new SqlInOutParameter("@Correlativo", Types.VARCHAR),
								new SqlInOutParameter("@MVentaFueraHora", Types.VARCHAR)
						});
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Usuario", usuario)
						.addValue("@RUC", ventaBases.getRuc())
						.addValue("@NombreProveedor",ventaBases.getNombreProveedor())
						.addValue("@NroProceso", ventaBases.getNroProceso())
						.addValue("@RotuloPDF", "")
						.addValue("@RotuloEtiqueta", "")
						.addValue("@RotuloMuestra", "")
						.addValue("@Mensaje", "")
						.addValue("@Mensaje1", "")
						.addValue("@Correlativo", "")
						.addValue("@MVentaFueraHora", ventaBases.getFueraHora());
				
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@mensaje")==null?"":String.valueOf(resultMap.get("@mensaje")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
			
			} catch (Exception e) {
				logger.error("[ERROR] registrarConsultaVentaBases", e);
			}
			logger.info("[FIN] registrarConsultaVentaBases");
			return lista;
		}
		
		public List<ItemFilenet> registrarExpediente(Expediente expediente, String usuario) {	
			logger.info("[INICIO] registrarExpediente:");
			SimpleJdbcCall call = null;
			List<ItemFilenet> lista = new ArrayList<>();
			try{
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_registrar_expediente")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Usuario", Types.VARCHAR), 
								new SqlInOutParameter("@NroProceso", Types.VARCHAR), 
								new SqlInOutParameter("@TipoProceso", Types.VARCHAR), 
								new SqlInOutParameter("@NroMemo", Types.VARCHAR),
								new SqlInOutParameter("@PersonaContacto", Types.VARCHAR),
								new SqlInOutParameter("@Anexo", Types.INTEGER),
								new SqlInOutParameter("@CantidadBases", Types.VARCHAR), 
								new SqlInOutParameter("@ValorPliego", Types.VARCHAR), 
								new SqlInOutParameter("@Objeto", Types.VARCHAR),
								new SqlInOutParameter("@FIniVentaBase", Types.VARCHAR),
								new SqlInOutParameter("@HIniVentaBase", Types.VARCHAR),
								new SqlInOutParameter("@FFinVentaBase", Types.VARCHAR),
								new SqlInOutParameter("@HFinVentaBase", Types.VARCHAR),
								new SqlInOutParameter("@FIniConsulta", Types.VARCHAR),
								new SqlInOutParameter("@HIniConsulta", Types.VARCHAR),
								new SqlInOutParameter("@FFinConsulta", Types.VARCHAR),
								new SqlInOutParameter("@HFinConsulta", Types.VARCHAR),
								new SqlInOutParameter("@FechaInicioRecep", Types.VARCHAR),
								new SqlInOutParameter("@HoraDesdeInicio", Types.VARCHAR),
								new SqlInOutParameter("@HoraHastaInicio", Types.VARCHAR),
								new SqlInOutParameter("@FechaCierreRecep", Types.VARCHAR),
								new SqlInOutParameter("@HoraDesdeCierre", Types.VARCHAR),
								new SqlInOutParameter("@HoraHastaCierre", Types.VARCHAR),
								new SqlInOutParameter("@FIniImpugna", Types.VARCHAR),
								new SqlInOutParameter("@FFinImpugna", Types.VARCHAR),
								new SqlInOutParameter("@CodigoDependencia", Types.INTEGER),
								new SqlInOutParameter("@Mensaje", Types.VARCHAR)
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Usuario", usuario)
						.addValue("@NroProceso", expediente.getNroProceso()) 
						.addValue("@TipoProceso",expediente.getTipoProceso()) 
						.addValue("@NroMemo", expediente.getNroMemo())
						.addValue("@PersonaContacto", expediente.getRegistroContacto())
						.addValue("@Anexo", expediente.getAnexo())
						.addValue("@CantidadBases", expediente.getCantidadBases()) 
						.addValue("@ValorPliego", expediente.getValorPliego())
						.addValue("@Objeto", expediente.getObjetoProceso())
						.addValue("@FIniVentaBase", expediente.getFechaInicioVentaBase())
						.addValue("@HIniVentaBase", expediente.getHoraInicioVentaBase())
						.addValue("@FFinVentaBase", expediente.getFechaCierreVentaBase())
						.addValue("@HFinVentaBase",expediente.getHoraCierreVentaBase())
						.addValue("@FIniConsulta", expediente.getFechaInicioConsulta())
						.addValue("@HIniConsulta", expediente.getHoraInicioConsulta())
						.addValue("@FFinConsulta", expediente.getFechaFinConsulta())
						.addValue("@HFinConsulta", expediente.getHoraFinConsulta())
						.addValue("@FechaInicioRecep", expediente.getFechaInicioRecep())
						.addValue("@HoraDesdeInicio", expediente.getHoraInicioRecep())
						.addValue("@HoraHastaInicio", expediente.getHoraFinRecep())
						.addValue("@FechaCierreRecep",  expediente.getFechaFinRecep())
						.addValue("@HoraDesdeCierre", expediente.getHoraInicioRecep_c())
						.addValue("@HoraHastaCierre", expediente.getHoraFinRecep_c())
						.addValue("@FIniImpugna", expediente.getFechaInicioImpugnacion())
						.addValue("@FFinImpugna", expediente.getFechaFinalImpugnacion())
						.addValue("@CodigoDependencia", expediente.getCodigoDependencia())
						.addValue("@Mensaje", "");
			
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@Mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				logger.info("Batch:" + resultMap.get("@NroBatch"));
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				it.setCodigo(resultMap.get("@NroBatch")==null?"":String.valueOf(resultMap.get("@NroBatch")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
				
			}catch (Exception e) {
				logger.error("[ERROR] registrarExpediente", e);
			}
			logger.info("[FIN] registrarExpediente");
			
			return lista;
		}	
		
		
		public List<ItemFilenet> registrarTracking(Tracking tracking, String usuario, String modo) {	
			SimpleJdbcCall call = null;
			List<ItemFilenet> lista = new ArrayList<>();
			try{
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_registra_tracking")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Correlativo", Types.VARCHAR), 
								new SqlInOutParameter("@TipoMov", Types.VARCHAR), 
								new SqlInOutParameter("@Registro", Types.VARCHAR), 
								new SqlInOutParameter("@RecibidoPor", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje", Types.VARCHAR)
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Correlativo", tracking.getCorrelativo())
						.addValue("@TipoMov", modo) 
						.addValue("@Registro",usuario) 
						.addValue("@RecibidoPor", tracking.getCodFuncionario())
						.addValue("@Mensaje", "");
			
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@Mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				logger.info("Batch:" + resultMap.get("@NroBatch"));
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				it.setCodigo(resultMap.get("@NroBatch")==null?"":String.valueOf(resultMap.get("@NroBatch")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
				
			}catch (Exception e) {
				logger.error("[ERROR] registarComprobante", e);
			}
			logger.info("[FIN] registarComprobante");
			
			return lista;
		}		
		
		@Override
		public List<Object[]> consultaTracking(String idUsuario, String correlativo) {
			// TODO Auto-generated method stub
			EntityManager em = null;
			List<Object[]> listRows = null;
			try{
				em = entityManagerBDFilenet.createEntityManager();
	            
				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_tracking");
				
				query.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.IN);
				
				query.setParameter("Correlativo", correlativo==null?"":correlativo);
				
				query.execute();
				listRows = query.getResultList();
				Object[] total = {0};
				//total[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, total);
				
			}catch(Exception e){
	            e.printStackTrace();
	        }finally{
	        	if (em != null) {em.close();}
	        }
	        
			return listRows;
		}
		
		@Override
		public List<ItemFilenet> listaCouriersCGC(String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_couriers_CGC")
						.declareParameters(new SqlParameter[] { new SqlParameter("@CodigoCGC", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@CodigoCGC", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoCourier") == null) ? "" : row.get("CodigoCourier").toString());
					item.setDescripcion((row.get("Courier") == null) ? "" : row.get("Courier").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarUsuarioCgc", e);
			}
			return lista;
		}
		
		@Override
		public List<ItemFilenet> listarMotivos(String texto) {
			// TODO Auto-generated method stub
			List<ItemFilenet> lista = new ArrayList<>();
			SimpleJdbcCall call = null;
			try {
				texto = (texto == null) ? "" : texto;

				call = (new SimpleJdbcCall(getDataSource())).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_select_lista_motivos")
						.declareParameters(new SqlParameter[] { new SqlParameter("@Cadena", Types.VARCHAR) });

				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource()).addValue("@Cadena", texto);

				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				List<Map> rows = (List<Map>) resultMap.get("#result-set-1");
				for (Map row : rows) {
					ItemFilenet item = new ItemFilenet();
					item.setCodigo((row.get("CodigoMotivo") == null) ? "" : row.get("CodigoMotivo").toString());
					item.setDescripcion((row.get("Motivo") == null) ? "" : row.get("Motivo").toString());
					lista.add(item);
				}
			} catch (Exception e) {
				this.logger.error("[ERROR] listarUsuarioCgc", e);
			}
			return lista;
		}
		
		public List<ItemFilenet> registrarDevolucion(Devolucion devolucion, String usuario) {	
			logger.info("[INI] registrarDevolucion");
			SimpleJdbcCall call = null;
			List<ItemFilenet> lista = new ArrayList<>();
			try{
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_registrar_devolucion")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@Correlativo", Types.VARCHAR), 
								new SqlInOutParameter("@Motivo", Types.INTEGER), 
								new SqlInOutParameter("@Mensaje", Types.VARCHAR), 
								new SqlInOutParameter("@Usuario", Types.VARCHAR)
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@Correlativo", devolucion.getCorrelativo())
						.addValue("@Motivo", Integer.valueOf(devolucion.getCodMotivo())) 
						.addValue("@Mensaje","") 
						.addValue("@Usuario",usuario);
			
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@Mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
				
			}catch (Exception e) {
				logger.error("[ERROR] registrarDevolucion", e);
			}
			logger.info("[FIN] registrarDevolucion");
			
			return lista;
		}	
		
		public List<ItemFilenet> asociarOrdenServicio(OrdenServicio ordenServicio, String usuario) {	
			logger.info("[INI] asociarOrdenServicio");
			SimpleJdbcCall call = null;
			List<ItemFilenet> lista = new ArrayList<>();
			try{
				call = new SimpleJdbcCall(getDataSource()).withoutProcedureColumnMetaDataAccess().withSchemaName("dbo")
						.withProcedureName("sic_sp_asociar_orden_servicio")
						.declareParameters(new SqlParameter[] {
								new SqlInOutParameter("@CorrelativoP", Types.VARCHAR), 
								new SqlInOutParameter("@OrdenServicio", Types.VARCHAR), 
								new SqlInOutParameter("@Mensaje", Types.VARCHAR),
								new SqlInOutParameter("@Mensaje1", Types.VARCHAR),
								new SqlInOutParameter("@Usuario", Types.VARCHAR)
						});
			
				MapSqlParameterSource mapSqlParameterSource = (new MapSqlParameterSource())
						.addValue("@CorrelativoP", ordenServicio.getCorrelativo())
						.addValue("@OrdenServicio", ordenServicio.getCourier()) 
						.addValue("@Mensaje","") 
						.addValue("@Mensaje1","") 
						.addValue("@Usuario",usuario);
			
				Map<String, Object> resultMap = call.execute((SqlParameterSource) mapSqlParameterSource);
				logger.info("MENSAJE:" + resultMap.get("@Mensaje"));
				logger.info("MENSAJE RESPUESTA:: " + resultMap.size());
				logger.info("MENSAJE RESPUESTA:: " + resultMap.toString());
				ItemFilenet it = new ItemFilenet();
				it.setCodigo(resultMap.get("@Mensaje1")==null?"":String.valueOf(resultMap.get("@Mensaje1")));
				it.setDescripcion(resultMap.get("@Mensaje")==null?"":String.valueOf(resultMap.get("@Mensaje")));
				lista.add(it);
				logger.info("IT:" + lista.get(0).getDescripcion());
				
			}catch (Exception e) {
				logger.error("[ERROR] asociarOrdenServicio", e);
			}
			logger.info("[FIN] asociarOrdenServicio");
			
			return lista;
		}
		
		@Override
		public List<Object[]> consultar_estDigContrataciones_paginado(String usuario, String nroProceso, String fechaDesde, String fechaHasta, String ruc, String estado, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {

				em = entityManagerBDFilenet.createEntityManager();

				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_dcto_contratacion_v2");

				query.registerStoredProcedureParameter("NroProceso", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("RUC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Estado", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.IN);
				
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

				query.setParameter("NroProceso", nroProceso);
				query.setParameter("RUC", ruc);
				query.setParameter("Estado", estado);
				query.setParameter("FechaDesde", fechaDesde);
				query.setParameter("FechaHasta", fechaHasta);
				
				query.setParameter("ItemsPorPagina", itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", exportaExcel);
				query.setParameter("nombreColumna", nombreColumna);
				query.setParameter("desc", desc);

				query.execute();
				listRows = query.getResultList();
				Object[] arrTotal = { 0 };
				arrTotal[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, arrTotal);

			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_estDigContrataciones_paginado", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}
	
		
		
		@Override
		public List<Object[]> consultar_despacho_paginado(String usuario,String nroCorrelativo,String codEstado,String fechaDesde,String fechaHasta,String dependenciaRemitente,String usuarioRemitente,String numeroDocumento,String entidadExterna,String asunto,String guiaRemision, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {

				em = entityManagerBDFilenet.createEntityManager();

				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_despachos_v2");

				query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoDepRemitente", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("UsuarioRemitente", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NroDocInterno", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("DependenciaExterna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("Asunto", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("GuiaRemision", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

				query.setParameter("Usuario", usuario);
				query.setParameter("Correlativo", nroCorrelativo);
				query.setParameter("FechaDesde", fechaDesde);
				query.setParameter("FechaHasta", fechaHasta); 
				query.setParameter("CodigoEstado", codEstado.equalsIgnoreCase("") ? 0 : Integer.valueOf(codEstado));
				query.setParameter("CodigoDepRemitente", dependenciaRemitente.equalsIgnoreCase("") ? 0 : Integer.valueOf(dependenciaRemitente));
				query.setParameter("UsuarioRemitente", usuarioRemitente);
				query.setParameter("NroDocInterno", numeroDocumento);
				query.setParameter("DependenciaExterna", entidadExterna);
				query.setParameter("Asunto", asunto);
				query.setParameter("GuiaRemision", guiaRemision);
				query.setParameter("ItemsPorPagina", itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", exportaExcel);
				query.setParameter("nombreColumna", nombreColumna);
				query.setParameter("desc", desc);

				query.execute();
				listRows = query.getResultList();
				Object[] arrTotal = { 0 };
				arrTotal[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, arrTotal);

			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_despacho_paginado", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}
		
		
		
		@Override
		public List<Object[]> consultar_valijas_recibidas_paginado(String usuario,String nroCorrelativo,String codEstado,String fechaDesde,String fechaHasta,String cgcReceptor,String cgcRemitente,String courier, int itemsPorPagina, int numeroPagina, String exportaExcel, String nombreColumna, String desc) {
			EntityManager em = null;
			List<Object[]> listRows = null;
			try {

				em = entityManagerBDFilenet.createEntityManager();

				StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_valijas_recibidas_v2");

				query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CorrelativoGR", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoCGCRemi", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoCGCRece", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoCourier", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
				query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

				query.setParameter("Usuario", usuario);
				query.setParameter("CorrelativoGR", nroCorrelativo);
				query.setParameter("CodigoCGCRemi", cgcRemitente);
				query.setParameter("CodigoCGCRece", cgcReceptor);
				query.setParameter("CodigoCourier", courier.equalsIgnoreCase("") ? 0 : Integer.valueOf(courier));
				query.setParameter("FechaDesde", fechaDesde);
				query.setParameter("FechaHasta", fechaHasta); 
				query.setParameter("CodigoEstado", codEstado.equalsIgnoreCase("") ? 0 : Integer.valueOf(codEstado));
				query.setParameter("ItemsPorPagina", itemsPorPagina);
				query.setParameter("NumeroPagina", numeroPagina);
				query.setParameter("ExportaExcel", exportaExcel);
				query.setParameter("nombreColumna", nombreColumna);
				query.setParameter("desc", desc);

				query.execute();
				listRows = query.getResultList();
				Object[] arrTotal = { 0 };
				arrTotal[0] = query.getOutputParameterValue("TotalRegistros");
				listRows.add(0, arrTotal);

			} catch (Exception e) {
				this.logger.error("[ERROR] consultar_despacho_paginado", e);
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return listRows;
		}
	/*FIN Ticket 9000004412*/	
}

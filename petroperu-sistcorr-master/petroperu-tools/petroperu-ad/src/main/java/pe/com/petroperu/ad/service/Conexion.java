package pe.com.petroperu.ad.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.petroperu.ad.model.Rol;
import pe.com.petroperu.ad.model.Usuario;
import pe.com.petroperu.ad.util.AConfiguracion;

public class Conexion {
	private DirContext dirContext;
	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public Conexion(DirContext dirContext) {
		this.dirContext = dirContext;
	}

	public boolean bindToServer(String userDN, String credentials, AConfiguracion configuracion) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] bindToServer");
		try {
			this.dirContext.close();
			if (userDN != null && !userDN.isEmpty()) {
				Hashtable<String, String> env = new Hashtable<>();
				env.put("java.naming.factory.initial", configuracion.getINITIAL_CONTEXT_FACTORY());
				env.put("java.naming.provider.url", configuracion.getURL_SERVIDOR());
				env.put("java.naming.security.authentication", configuracion.getAUTENTICACION());
				env.put("java.naming.security.principal", userDN);
				env.put("java.naming.security.credentials", credentials);
				env.put("java.naming.ldap.attributes.binary", "usuario");
				env.put("java.naming.referral", "follow");

				DirContext ctx = new InitialDirContext(env);
				this.dirContext = ctx;
				// TICKET 9000003992
				this.LOGGER.info("[FIN] bindToServer");
				return true;
			}
			// TICKET 9000003992
			this.LOGGER.info("[FIN] bindToServer");
			return false;

		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] bindToServer " + " This is error : " + e);
			// e.printStackTrace();
			// System.out.println(e.getMessage());
			this.LOGGER.info("[FIN] bindToServer");
			return false;
		}
	}

	public String getUserDN(String user) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] getUserDN");
		try {
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(2);
			String filter = "(uid=" + user + ")";
			NamingEnumeration<?> answer = this.dirContext.search("dc=petroperu,dc=com,dc=pe", filter, ctls);
			if (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				return sr.getNameInNamespace();
			}
			return null;
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] getUserDN " + " This is error : " + e);
			// e.printStackTrace();
			this.LOGGER.info("[FIN] getUserDN");
			return null;
		}
	}

	public Usuario getUsuario(String user) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] getUsuario");
		try {
			Usuario usuario = new Usuario();
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(2);
			String filter = "(uid=" + user + ")";
			NamingEnumeration<?> answer = this.dirContext.search("dc=petroperu,dc=com,dc=pe", filter, ctls);

			while (answer.hasMore()) {
				SearchResult rslt = (SearchResult) answer.next();
				// TICKET 9000003992
				this.LOGGER.info("[INFO] getUsuario " + " This is info : rslt.getName() " + rslt.getName());
				// System.out.println(rslt.getName());
				Attributes attrs = rslt.getAttributes();
				NamingEnumeration<? extends Attribute> i = attrs.getAll();
				while (i.hasMore()) {
					Attribute attribute = i.next();
					// TICKET 9000003992
					// this.LOGGER.info("[INFO] getUsuario " + " This is info : attribute.getID() " + attribute.get());
					// System.out.println(attribute.getID() + ":\t" +
					// attribute.get());
					switch (attribute.getID()) {
					case "mail":
						usuario.setEmail(attribute.get().toString());
						break;
					case "sn":
						usuario.setApellidos(attribute.get().toString());
						break;
					case "givenName":
						usuario.setNombres(attribute.get().toString());
						break;
					case "uid":
						usuario.setUid(attribute.get().toString());
						break;
					case "cn":
						usuario.setNombreCompleto(attribute.get().toString());
						break;
					case "o":
						usuario.setOrganizacion(attribute.get().toString());
						break;
					case "ou":
						usuario.setUnidadOrganizativa(attribute.get().toString());
						break;
					default:
						break;
					}

				}
			}
			return usuario;
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] getUsuario " + " This is error : " + e);
			// e.printStackTrace();
			this.LOGGER.info("[FIN] getUsuario");
			return null;
		}
	}

	public List<Rol> datoGrupoOficinaPrincipal(String user, String[] _roles) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] datoGrupoOficinaPrincipal");
		List<Rol> roles = new ArrayList<>();
		try {
			String contextName = "dc=petroperu,dc=com,dc=pe";
			String filterExpr = "(uniquemember={0})";
			String userDN = "uid=" + user + ",ou=Usuarios,o=Oficina Principal,dc=petroperu,dc=com,dc=pe";
			Object[] filterArgs = { userDN };

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(2);

			NamingEnumeration<SearchResult> search = this.dirContext.search(contextName, filterExpr, filterArgs,
					constraints);

			while (search.hasMore()) {

				SearchResult rslt = search.next();
				if (rslt.getName().contains("FILENET") && rslt.getName().contains("Servicios")) {
					Rol rol = new Rol();
					Attributes attrs = rslt.getAttributes();
					NamingEnumeration<? extends Attribute> i = attrs.getAll();
					while (i.hasMore()) {

						Attribute attribute = i.next();
						switch (attribute.getID()) {
						case "description":
							rol.setDescripcion(attribute.get().toString().trim());
							break;
						case "cn":
							rol.setRol(attribute.get().toString().trim());
							break;
						default:
							break;
						}

					}
					// TICKET 9000003992
					this.LOGGER.info("[INFO] datoGrupoOficinaPrincipal " + " This is info : Rol AD " + rol.toString());
					// System.out.println("Rol AD: " + rol.toString());
					roles.add(rol);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Rol> rolesFiltrados = new ArrayList<>();
		for (Rol rol : roles) {
			for (String _rol : _roles) {
				if (rol.getRol().toLowerCase().equals(_rol.toLowerCase())) {
					rol.generarRolAD();
					// TICKET 9000003992
					this.LOGGER.info(
							"[INFO] datoGrupoOficinaPrincipal " + " This is info : Rol Ad filtrado " + rol.toString());
					// System.out.println("Rol Ad filtrado: " + rol.toString());
					rolesFiltrados.add(rol);

					break;
				}
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] datoGrupoOficinaPrincipal");
		return rolesFiltrados;
	}

	public List<Rol> datoGrupo(String userQuery, String[] _roles) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] datoGrupo");
		List<Rol> roles = new ArrayList<>();
		if (userQuery == null)
			return roles;
		try {
			String contextName = "dc=petroperu,dc=com,dc=pe";
			String filterExpr = "(uniquemember={0})";
			String userDN = userQuery;
			Object[] filterArgs = { userDN };

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(2);

			NamingEnumeration<SearchResult> search = this.dirContext.search(contextName, filterExpr, filterArgs,
					constraints);

			while (search.hasMore()) {

				SearchResult rslt = search.next();
				if (rslt.getName().contains("FILENET") && rslt.getName().contains("Servicios")) {
					Rol rol = new Rol();
					Attributes attrs = rslt.getAttributes();
					NamingEnumeration<? extends Attribute> i = attrs.getAll();
					while (i.hasMore()) {

						Attribute attribute = i.next();
						switch (attribute.getID()) {
						case "description":
							rol.setDescripcion(attribute.get().toString().trim());
							break;
						case "cn":
							rol.setRol(attribute.get().toString().trim());
							break;
						default:
							break;
						}

					}
					// TICKET 9000003992
					this.LOGGER.info("[INFO] datoGrupo " + " This is info : Rol AD " + rol.toString());
					// System.out.println("Rol AD: " + rol.toString());
					roles.add(rol);
				}

			}
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] datoGrupo " + " This is error : " + e);
//			e.printStackTrace();
		}
		List<Rol> rolesFiltrados = new ArrayList<>();
		for (Rol rol : roles) {
			for (String _rol : _roles) {
				if (rol.getRol().toLowerCase().equals(_rol.toLowerCase())) {
					rol.generarRolAD();
					// TICKET 9000003992
					this.LOGGER.info("[INFO] datoGrupo " + " This is info : Rol Ad filtrado " + rol.toString());
					// System.out.println("Rol Ad filtrado: " + rol.toString());
					rolesFiltrados.add(rol);

					break;
				}
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] datoGrupo");
		return rolesFiltrados;
	}
}

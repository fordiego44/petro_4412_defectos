package pe.com.petroperu.configuracion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import pe.com.petroperu.ad.util.RolAD;
import pe.com.petroperu.seguridad.Autenticacion;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Autenticacion autenticacion;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(autenticacion);
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
		.headers()
		.defaultsDisabled()
		.contentTypeOptions().and()
	    .xssProtection().block(true).and()
	    //.cacheControl().and()
	    .httpStrictTransportSecurity().and()
	    .frameOptions().sameOrigin()
	    .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","default-src 'none'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; connect-src 'self'; img-src * 'self' data: https:;  font-src 'self' 'unsafe-inline';"))
	    //.addHeaderWriter(new StaticHeadersWriter("Cache-Control", "public, max-age=180, immutable"))
	    .and()
		.authorizeRequests()
		.antMatchers("/app/documentos-firmados**", "/app/consulta-correspondencia", "/app/consulta-asignaciones", "/app/documentos-firmados/**", "/app/emision/buscar/correspondencia**", "/app/emision/buscar/correspondencia/**", "/app/emision/descargar-archivo**", "/app/emision/descargar-archivo/**","/app/actualizaflujo","/app/adminEquipos","app/modificarDependencia").hasAnyRole(RolAD.EXTERNO.ROL, RolAD.USUARIO.ROL)
		.antMatchers("/app**", "/app/**", "/correspondencias**", "/correspondencias/**", "/resources**", "/resources/**").hasRole(RolAD.USUARIO.ROL)
		.antMatchers("/app/correspondencias/correspondencia/buscar","app/emision/consultar-jefe-gestor/paginado", "app/emision/validar/nivel-firma").hasRole(RolAD.USUARIO.ROL)
		.and().formLogin()
				.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/app/inicio")
				.failureUrl("/login?error").usernameParameter("parametro_1").passwordParameter("2_parametro").and().logout()
				.logoutSuccessUrl("/login?logout").logoutUrl("/j_spring_security_logout").and().exceptionHandling().accessDeniedPage("/error").and().csrf()
				.and().sessionManagement().maximumSessions(1).expiredUrl("/login");// .disable();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	   /* web
	       .ignoring()
	       .antMatchers("/static/**", "/css/**", "/js/**", "/imagenes/**", "/captcha", "/captcha/**", "/extras/**", "/librerias/**", "/signOk");*/
		
		 web
	       .ignoring()
	       .antMatchers("/signOk", "/signOkGrupal");
	       //.antMatchers("/signOk", "/j_spring_security_logout");
	}

}

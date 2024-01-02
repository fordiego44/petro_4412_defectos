package pe.com.petroperu.configuracion;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:base_datos.properties" })
@EnableJpaRepositories(entityManagerFactoryRef = "emFactoryBDFilenet", basePackages = { "pe.com.petroperu.filenet.dao" })
public class BDFilenetConfiguracion {
	
	@Autowired
	private Environment env;
	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Bean(name = "entityManagerBDFilenet")
	public EntityManager entityManager() {
		return entityManagerFactory().createEntityManager();
	}

	@Bean(name = "emFactoryBDFilenet")
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "pe.com.petroperu.filenet.model"});
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(Boolean.FALSE);
		vendorAdapter.setShowSql(Boolean.TRUE);
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		em.setPersistenceUnitName("persistenceUnitBDFilenet");
		em.afterPropertiesSet();
		em.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return em.getObject();
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "pe.com.petroperu.filenet.model" });
		sessionFactory.setHibernateProperties(additionalProperties());
		return sessionFactory;
	}

	@Bean(name = "transactionManagerBDFilenet")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory());
		return transactionManager;
	}
	
	// TICKET 9000003992
	@Bean(name = "dataSourceBDFilenet")
	public DataSource dataSource() {
		this.LOGGER.info("[INICIO] dataSource");

		DataSource dataSource = null;
		Context initialContex = null;

		try {
			initialContex = new InitialContext();
			dataSource = (DataSource) (initialContex.lookup(env.getProperty("filenet.datasource.jdni.Filenet")));
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] dataSource " + " This is error : " + e);
		}

		this.LOGGER.info("[FIN] dataSource");
		return dataSource;
	}
	// TICKET 9000003992
//	@Bean(name = "dataSourceBDFilenet")
//	public DataSource dataSource() {
//		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(env.getProperty("filenet.datasource.driver-class-name"));
//		dataSource.setUrl(env.getProperty("filenet.datasource.url"));
//		dataSource.setUsername(env.getProperty("filenet.datasource.username"));
//		dataSource.setPassword(env.getProperty("filenet.datasource.password"));
//		dataSource.setSchema("dbo");
//		return dataSource;
//	}

	final Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();
		if (env.getProperty("filenet.jpa.hibernate.ddl-auto") != null) {
			hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("filenet.jpa.hibernate.ddl-auto"));
		}
		if (env.getProperty("filenet.jpa.hibernate.dialect") != null) {
			hibernateProperties.setProperty("hibernate.dialect", env.getProperty("filenet.jpa.hibernate.dialect"));
		}
		if (env.getProperty("filenet.jpa.show-sql") != null) {
			hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("filenet.jpa.show-sql"));
		}
		return hibernateProperties;
	}

}

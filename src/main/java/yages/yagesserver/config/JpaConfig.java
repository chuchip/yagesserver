package yages.yagesserver.config;

/**
 *
 * @author chuchip
 */

import java.util.Properties;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableLoadTimeWeaving
@EnableJpaRepositories("yages.yagesserver")
@PropertySource("classpath:yages.properties")
@EnableTransactionManagement
public class JpaConfig {
      
    @Autowired
    private Environment env;
   
  @Bean(name = "dataSource", destroyMethod = "")
  public DataSource dataSource(Environment env)  throws NamingException
  {
       DataSource ds=(DataSource) new JndiTemplate().lookup(env.getProperty("db.jndi"));
       return ds ;//new DelegatingDataSource(ds);//  
  }
   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource)  throws NamingException
   {    
      LocalContainerEntityManagerFactoryBean em  = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(dataSource);
      em.setJpaProperties(misPropiedades());
      em.setPackagesToScan(new String[] { "yages.yagesserver","yages.yagesserver.dao" });
 
      JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      em.setJpaVendorAdapter(vendorAdapter);   
      
      return em;
   }
   
   Properties misPropiedades() {
        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        properties.setProperty("hibernate.current_session_context_class", env.getProperty("hibernate.current_session_context_class"));
        return properties;
    }  
 
   @Bean
   public PlatformTransactionManager transactionManager( EntityManagerFactory emf){
       JpaTransactionManager transactionManager = new JpaTransactionManager();
       transactionManager.setEntityManagerFactory(emf); 
       return transactionManager;
   } 
  
}

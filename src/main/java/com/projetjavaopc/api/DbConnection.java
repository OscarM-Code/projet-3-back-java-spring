package com.projetjavaopc.api;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.core.env.Environment;

@Configuration
@EnableTransactionManagement
public class DbConnection {

    @Autowired
    private Environment env;

     @Bean
    public DataSource dataSource()
     {
         DriverManagerDataSource dataSource = new DriverManagerDataSource();

         dataSource.setDriverClassName(env.getProperty("app.db.driver"));
         dataSource.setUrl(env.getProperty("app.db.url"));
         dataSource.setUsername(env.getProperty("app.db.user"));
         dataSource.setPassword(env.getProperty("app.db.password"));

         return dataSource;
     }

     @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate()
     {
         NamedParameterJdbcTemplate retBean = new NamedParameterJdbcTemplate(dataSource());
         return retBean;
     }

     @Bean(name = "transactionManager")
    public JpaTransactionManager getDataSourceTransactionManager() {
        JpaTransactionManager jpaTransaction  = new JpaTransactionManager();
        jpaTransaction.setDataSource(dataSource());
        return jpaTransaction;
    }
}

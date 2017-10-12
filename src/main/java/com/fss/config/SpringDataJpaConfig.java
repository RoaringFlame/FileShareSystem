package com.fss.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//通过JavaConfig使用Spring来为这些接口创建代理实例（configuration关于jap的使用）

@Configuration
@EnableJpaRepositories(basePackages= "com.fss.dao.repositories")    //  创建代理实例
  public class SpringDataJpaConfig {

  @Bean
  public DataSource dataSource() {
      /**
       * 连接池的初始化，自动读取/resources/c3p0.properties文件
       */
    return new ComboPooledDataSource();
  }

  /**
   * LocalContainerEntityManagerFactoryBean
   * 根据JPA PersistenceProvider自动检测配置文件进行工作,需要设置Spring中定义的DataSource；
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {

   // 适用于所有环境的FactoryBean，能全面控制EntityManagerFactory配置
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    //参数设置，数据源DataSource，具体厂家实现JpaVendorAdapter
    emf.setDataSource(dataSource);
    emf.setJpaVendorAdapter(jpaVendorAdapter);

    //指定持久化单元名字,
    emf.setPersistenceUnitName("fss");
    emf.setPackagesToScan("com.fss.dao.domain");
    return emf;
  }

  /**
   * 用于设置实现厂商JPA实现的特定属性
   */
  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    //最重要的属性设置，设置使用的数据库
    adapter.setDatabase(Database.MYSQL);

    adapter.setShowSql(true);
    adapter.setGenerateDdl(false);
    adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
    return adapter;
  }

  @Configuration
  @EnableTransactionManagement
  public static class TransactionConfig {

    @Inject
    private EntityManagerFactory emf;

    @Bean
    public PlatformTransactionManager transactionManager() {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(emf);
      return transactionManager;
    }
  }

}

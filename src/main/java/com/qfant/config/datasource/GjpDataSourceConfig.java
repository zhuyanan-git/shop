package com.qfant.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
//@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryGjp",
//        transactionManagerRef = "transactionManagerGjp",
//        basePackages = {"com.qfant.gjp"})
@MapperScan(basePackages = "com.qfant.gjp", sqlSessionTemplateRef  = "sqlSessionTemplateSecondary")
public class GjpDataSourceConfig {
    @Autowired
    private Environment env;
    @Autowired
    @Qualifier("gjpDataSource")
    private DataSource gjpDataSource;
    @Bean(name="sqlSessionFactorySecondary")
    public SqlSessionFactory sqlSessionFactorySecondary() throws Exception{
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(gjpDataSource);
        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackagegjp"));//指定基包
//        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocationsgjp")));//指定xml文件位置
        return fb.getObject();
    }

    @Bean(name = "sqlSessionTemplateSecondary")
    public SqlSessionTemplate sqlSessionTemplateSecondary(@Qualifier("sqlSessionFactorySecondary") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
//    @Bean(name = "entityManagerFactoryGjp")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryGjp(EntityManagerFactoryBuilder builder) {
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(true);
//
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setPackagesToScan("com.qfant.gjp");
//        factory.setDataSource(gjpDataSource);
//        return factory;
//    }
//
//    @Bean(name = "entityManagerGjp")
//    public EntityManager entityManagerGjp(EntityManagerFactoryBuilder builder) {
//        return entityManagerFactoryGjp(builder).getObject().createEntityManager();
//    }
//
//    @Bean(name = "transactionManagerGjp")
//    public PlatformTransactionManager transactionManagerGjp(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory);
//        return txManager;
//    }
}

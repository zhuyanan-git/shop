package com.qfant.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @date 2017/12/04
 * sql server数据库中数据源的 声明装载类
 *
 */
@Configuration
//@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryWx",   //实体管理引用
//        transactionManagerRef = "transactionManagerWx",                     //事务管理引用
//        basePackages = {"com.qfant.wx"})                                    //设置 myagenDataSource应用到的包
@MapperScan(basePackages = "com.qfant.wx", sqlSessionTemplateRef  = "sqlSessionTemplatePrimary")
public class WxDataSourceConfig {
    @Autowired
    private Environment env;
    @Autowired
    @Qualifier("wxDataSource")
    private DataSource wxDataSource;


    @Bean(name="sqlSessionFactoryPrimary")
    @Primary
    public SqlSessionFactory sqlSessionFactoryPrimary() throws Exception{
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(wxDataSource);
        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackagewx"));//指定基包
//        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));//指定xml文件位置
        return fb.getObject();
    }

    @Bean(name = "sqlSessionTemplatePrimary")
    @Primary
    public SqlSessionTemplate sqlSessionTemplatePrimary(@Qualifier("sqlSessionFactoryPrimary") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

//
//    @Primary
//    @Bean(name = "entityManagerFactoryWx")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryWx(EntityManagerFactoryBuilder builder) {
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(true);
//
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setPackagesToScan("com.qfant.wx");
//        factory.setDataSource(wxDataSource);
//        return factory;
//    }
//    /**
//     * 配置EntityManager实体
//     *
//     * @param builder
//     * @return 实体管理器
//     */
//    @Bean(name = "entityManagerWx")
//    public EntityManager entityManagerWx(EntityManagerFactoryBuilder builder) {
//        return entityManagerFactoryWx(builder).getObject().createEntityManager();
//    }
//    /**
//     * 配置事务
//     *
//     * @param
//     * @return 事务管理器
//     */
//    @Bean(name = "transactionManagerWx")
//    public PlatformTransactionManager transactionManagerWx(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory);
//        return txManager;
//    }
}

package co.yixiang.config;


import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MybatisPlus配置
 *
 */
@Configuration
@MapperScan(basePackages = { "co.yixiang.*.mapper", "co.yixiang.config"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, @Autowired PaginationInterceptor paginationInterceptor) {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(new Interceptor[]{paginationInterceptor});
        bean.setVfs(SpringBootVFS.class);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mapper/**/*Mapper.xml"));
            // bean.setTypeAliasesPackage("co.yixiang");
            //添加MysqlGeoPointTypeHandler
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setMetaObjectHandler(new MetaHandler());
            bean.setGlobalConfig(globalConfig);
            bean.setTypeHandlers(new TypeHandler[]{new MysqlGeoPointTypeHandler(0)});
            bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

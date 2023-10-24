package com.example.a41;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

public class TestDataSourceAuto {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());
//        创建环境对象
        StandardEnvironment env = new StandardEnvironment();
//        把启动类的包名记录下来
        AutoConfigurationPackages.register(context.getDefaultListableBeanFactory(),TestDataSourceAuto.class.getPackageName());
//        设置环境
        env.getPropertySources().addFirst(new
                SimpleCommandLinePropertySource
                ("--spring.datasource.url=jdbc:mysql://192.168.228.133:3306/clazz_web?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT",
                "--spring.datasource.username=root",
                        "--spring.datasource.password=duxin521"));
        context.setEnvironment(env);
        context.registerBean(Config.class);
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
//            System.out.println(name);
            if(context.getBeanDefinition(name).getResourceDescription()!=null){
                System.out.println(name+"来源是:"+context.getBeanDefinition(name).getResourceDescription());
            }
        }
    }
    @Configuration
    @Import({MyImportSelector.class})
    static class Config{

    }

    static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{
                    DataSourceAutoConfiguration.class.getName(),
                    MybatisAutoConfiguration.class.getName(),
                    DataSourceTransactionManagerAutoConfiguration.class.getName(),
                    TransactionAutoConfiguration.class.getName()
            };
        }
    }
}

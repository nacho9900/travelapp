package ar.edu.itba.paw.webapp.config;


import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

//@EnableWebMvc
@EnableTransactionManagement
@ComponentScan( {"ar.edu.itba.paw.webapp.controller", "ar.edu.itba.paw.persistence", "ar.edu.itba.paw.service"} )
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{
    private ApplicationContext applicationContext;

    @Value( "classpath:places_api.key" )
    private Resource googleMapsApiKey;

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        return resolver;
    }

    @Override
    public void addResourceHandlers( final ResourceHandlerRegistry registry ) {
        registry.addResourceHandler( "/resources/**" )
                .addResourceLocations( "/resources/" );

        registry.addResourceHandler( "/js/**" )
                .addResourceLocations( "/js/" )
                .setCachePeriod( 0 );
        registry.addResourceHandler( "/css/**" )
                .addResourceLocations( "/css/" )
                .setCachePeriod( 0 );
        registry.addResourceHandler( "/img/**" )
                .addResourceLocations( "/img/" )
                .setCachePeriod( 0 );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setViewClass( JstlView.class );
        vr.setPrefix( "/WEB-INF/jsp/" );
        vr.setSuffix( ".jsp" );
        return vr;
    }

    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass( org.postgresql.Driver.class );
//                ds.setUrl( "jdbc:postgresql://localhost/paw" );
//                ds.setUsername( "postgres" );
//                ds.setPassword( "postgres" );
        ds.setUrl( "jdbc:postgresql://10.16.1.110/paw-2019a-4" );
        ds.setUsername( "paw-2019a-4" );
        ds.setPassword( "qwQf3Kj2g" );

        return ds;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename( "classpath:i18n/messages" );
        messageSource.setDefaultEncoding( StandardCharsets.UTF_8.displayName() );
        messageSource.setCacheSeconds( 5 );
        return messageSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager( final EntityManagerFactory emf ) {
        return new JpaTransactionManager( emf );
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan( "ar.edu.itba.paw.model" );
        factoryBean.setDataSource( dataSource() );
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter( vendorAdapter );
        final Properties properties = new Properties();
        //        properties.setProperty( "hibernate.SQL", "debug" );
        //        properties.setProperty( "hibernate.type", "trace" );
        properties.setProperty( "hibernate.hbm2ddl.auto", "update" );
        properties.setProperty( "hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect" );
        factoryBean.setJpaProperties( properties );
        return factoryBean;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext( this.applicationContext );
        templateResolver.setPrefix( "/WEB-INF/mail/" );
        templateResolver.setSuffix( ".html" );
        templateResolver.setTemplateMode( TemplateMode.HTML );
        templateResolver.setCacheable( true );
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver( templateResolver() );
        templateEngine.setEnableSpringELCompiler( true );
        return templateEngine;
    }

    @Bean
    public Validator validator() {
        final LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource( messageSource() );
        return factory;
    }

    @Bean
    public GeoApiContext geoApiContext() {
        String apiKey;

        try ( Reader reader = new InputStreamReader( googleMapsApiKey.getInputStream(), StandardCharsets.UTF_8 ) ) {
            apiKey = FileCopyUtils.copyToString( reader );
        }
        catch ( IOException ex ) {
            apiKey = "";
        }

        return new GeoApiContext.Builder().apiKey( apiKey ).build();
    }
}

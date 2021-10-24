package com.balsamiq.reconciliation.web.configuration;

import com.balsamiq.reconciliation.web.service.FileSystemStorageService;
import com.balsamiq.reconciliation.web.service.StorageService;
import com.balsamiq.reconciliation.web.service.TransactionReconciliationService;
import com.balsamiq.reconciliation.workflow.CSVProcessingWorkflow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.storage.dir}")
    private String storageDir;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false).
                ignoreAcceptHeader(false).
                useRegisteredExtensionsOnly(true).
                defaultContentType(MediaType.TEXT_HTML).
                mediaType("html", MediaType.TEXT_HTML).
                mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Bean
    public CSVProcessingWorkflow csvProcessingWorkflow() {
        return CSVProcessingWorkflow.instantiate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    @Bean
    public StorageService storageService() {
        return new FileSystemStorageService(storageDir);
    }

    @Bean
    public TransactionReconciliationService transactionReconciliationService() {
        return new TransactionReconciliationService();
    }

}
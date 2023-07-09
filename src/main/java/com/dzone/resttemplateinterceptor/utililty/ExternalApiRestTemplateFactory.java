package com.dzone.resttemplateinterceptor.utililty;

import com.dzone.resttemplateinterceptor.repository.FilePathRepository;
import com.dzone.resttemplateinterceptor.services.DocumentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class ExternalApiRestTemplateFactory {

    @Autowired
    FilePathRepository filePathRepository;

    @Autowired
    DocumentUploadService documentUploadService;

    public RestTemplate getRestTemplate(String accessToken, String fileNamePrefix, String serviceName) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setOutputStreaming(false);
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);
        RestTemplate restTemplate = new RestTemplate(factory);
        ExternalApiLoggingInterceptor loggingInterceptor = new ExternalApiLoggingInterceptor();
        loggingInterceptor.setAccessToken(accessToken);
        loggingInterceptor.setFileNamePrefix(fileNamePrefix);
        loggingInterceptor.setServiceName(serviceName);
        loggingInterceptor.setFilePathRepository(this.filePathRepository);
        loggingInterceptor.setDocumentUploadService(this.documentUploadService);
        restTemplate.setInterceptors(Collections.singletonList(loggingInterceptor));
        return restTemplate;
    }
}



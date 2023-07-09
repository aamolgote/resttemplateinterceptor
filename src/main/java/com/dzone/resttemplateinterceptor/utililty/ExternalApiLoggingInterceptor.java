package com.dzone.resttemplateinterceptor.utililty;

import com.dzone.resttemplateinterceptor.model.document_upload.DocumentsResponse;
import com.dzone.resttemplateinterceptor.repository.FilePathRepository;
import com.dzone.resttemplateinterceptor.services.DocumentUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;

public class ExternalApiLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String accessToken;
    private String fileNamePrefix;

    private FilePathRepository filePathRepository;

    private DocumentUploadService documentUploadService;

    private String serviceName;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public void setFileNamePrefix(String fileNamePrefix) {
        this.fileNamePrefix = fileNamePrefix;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public FilePathRepository getFilePathRepository() {
        return filePathRepository;
    }

    public void setFilePathRepository(FilePathRepository filePathRepository) {
        this.filePathRepository = filePathRepository;
    }

    public DocumentUploadService getDocumentUploadService() {
        return documentUploadService;
    }

    public void setDocumentUploadService(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        try {
            String requestBody = new String(body, Charset.defaultCharset());
            this.saveFile(requestBody, "REQUEST");
            //logger.info("GDS API Request Payload:" + requestBody);
        } catch (Exception ex) {
            logger.error("An error occurred while saving JSON request ExternalApiLoggingInterceptor::logRequest, detail error:", ex);
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        try {
            String responseBody = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
            this.saveFile(responseBody, "RESPONSE");
            //logger.info("GDS API Response payload:" + responseBody);
        } catch (Exception ex) {
            logger.error("An error occurred while saving JSON response ExternalApiLoggingInterceptor::logResponse, detail error:", ex);
        }
    }

    private void saveFile(String fileContent, String payloadType) {
        try {
            String timeStampSuffix = Long.toString(Instant.now().getEpochSecond());
            String fileName = this.getFileNamePrefix() + "_" + payloadType + "_" + timeStampSuffix + ".json";
            String documentPath = "external-partner/request-response/" + fileName;
            ByteArrayResource resource = this.getDocumentByteArray(fileContent.getBytes(), fileName);
            if (resource != null) {
                //Update the file to Cloud AWS S3 or Azure equivalent.
                DocumentsResponse documentsResponse = this.documentUploadService.updateDocument(resource, documentPath, accessToken);
                if (documentsResponse != null && documentsResponse.getSavedFilePath() != null) {
                    //Save file path in database.
                    boolean result = this.filePathRepository.saveExtPayloadPaths(this.serviceName, documentsResponse.getSavedFilePath());
                    if (!result) {
                        logger.error("An occurred while saving the response file path.");
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("An error occurred while saving req/res for APi call ExternalApiLoggingInterceptor::saveFile, detail error:", ex);
        }
    }

    private ByteArrayResource getDocumentByteArray(byte[] responseContent, String fileName) {
        try {
            final ByteArrayResource byteArrayResource = new ByteArrayResource(responseContent) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };
            return byteArrayResource;
        } catch (Exception ex) {
            logger.error("Exception - getDocumentByteArray - Error while response body byte array content." + ex.getMessage(), ex);
        }
        return null;
    }
}


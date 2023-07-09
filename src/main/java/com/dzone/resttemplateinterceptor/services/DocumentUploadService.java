package com.dzone.resttemplateinterceptor.services;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dzone.resttemplateinterceptor.model.document_upload.DocumentsResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DocumentUploadService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadService.class);

    private AmazonS3 amazonS3Client;

    @PostConstruct
    private void init(){
        this.amazonS3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }

    public DocumentsResponse updateDocument(Resource resource, String filePath, String accessToken){
        DocumentsResponse documentsResponse = null;
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("application/json");
            String bucketName = "ext-partner-payload";
            this.amazonS3Client.putObject(new PutObjectRequest(bucketName, filePath, resource.getInputStream(), objectMetadata));
            documentsResponse = new DocumentsResponse();
            documentsResponse.setSavedFilePath(filePath);
        } catch (Exception ex){
            logger.error("An error occurred while uploading file, detail error : ", ex);
        }
        return documentsResponse;
    }
}

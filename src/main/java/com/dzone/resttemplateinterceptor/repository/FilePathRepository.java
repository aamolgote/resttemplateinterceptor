package com.dzone.resttemplateinterceptor.repository;

import org.springframework.stereotype.Repository;

@Repository
public class FilePathRepository {
    public boolean saveExtPayloadPaths(String serviceName, String payload){
        return true;
    }
}

package net.uglevodov.restapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UploadFileResponse {
    @JsonIgnore
    private String fileName;
    private Long fileId;
    private String filePath;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, Long fileId, String filePath, String fileType, long size) {
        this.fileName = fileName;
        this.fileId = fileId;
        this.filePath = filePath;
        this.fileType = fileType;
        this.size = size;
    }

}
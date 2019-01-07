package net.uglevodov.restapi.service.impl;

import net.uglevodov.restapi.exceptions.FileStorageException;
import net.uglevodov.restapi.exceptions.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Service
public class FileStorageService {


    @Value("${file.upload-dir}")
    private String uploadDir;

    public Path fileStorageLocation() {
        Path result =  Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(result);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        return result;
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name and add some random
        String fileName = RandomStringUtils.randomAlphanumeric(25)+"_"+ LocalDate.now().toString()+ "_" + StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation().resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation().resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new NotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new NotFoundException("File not found " + fileName, ex);
        }
    }
}
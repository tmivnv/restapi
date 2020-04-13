package net.uglevodov.restapi.service.impl;

import net.uglevodov.restapi.exceptions.FileStorageException;
import net.uglevodov.restapi.exceptions.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.maven.surefire.shade.common.org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Service
public class FileStorageService {


    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.static-img-dir}")
    private String pathDir;

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


        //resize image
        int maxWidth = 800;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage sourceImage = ImageIO.read(file.getInputStream());



            Image thumbnail = sourceImage.getScaledInstance(maxWidth, sourceImage.getHeight()*maxWidth/sourceImage.getWidth(), Image.SCALE_SMOOTH);
            BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
                    thumbnail.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);


            URL url = new URL("http://84.201.154.176/static/img/watermark.png");
            BufferedImage watermarkImage = ImageIO.read(url);

            // initializes necessary graphic properties
            Graphics2D g2d = (Graphics2D) bufferedThumbnail.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2d.setComposite(alphaChannel);

            // calculates the coordinate where the image is painted
            int topLeftX = bufferedThumbnail.getWidth() - watermarkImage.getWidth() - 50;
            int topLeftY = bufferedThumbnail.getHeight() - watermarkImage.getHeight() - 20;

            // paints the image watermark
            g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);




            ImageIO.write(bufferedThumbnail, "jpeg", baos);

        } catch (Exception e)
        {

        }


        //save
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation().resolve(fileName);
            Files.copy(new ByteArrayInputStream(baos.toByteArray()), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return pathDir+fileName;
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
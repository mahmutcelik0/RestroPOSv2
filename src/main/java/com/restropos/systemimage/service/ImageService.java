package com.restropos.systemimage.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemimage.constants.FolderEnum;
import com.restropos.systemshop.entity.Image;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
public class ImageService {
    @Value("${firebase.bucket-name}")
    private String firebaseBucketName;

    @Value("${firebase.image-url}")
    private String firebaseImageUrl;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("firebase.json");

            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).setStorageBucket(firebaseBucketName).build();

            FirebaseApp.initializeApp(options);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public String getImageUrl(String folderName,String fileName) {
        var str = folderName+"%2F"+fileName;
        return String.format(firebaseImageUrl,str);
    }

    public Image saveForBusiness(MultipartFile file) throws NotFoundException, IOException {
        return save(file,FolderEnum.BUSINESS_LOGO);
    }

    public Image saveForProduct(MultipartFile file) throws NotFoundException, IOException {
        return save(file,FolderEnum.PRODUCTS);
    }

    public Image save(MultipartFile file, FolderEnum folderName) throws IOException, NotFoundException,IllegalArgumentException {
        if(file.isEmpty()) throw new NotFoundException("Image not found");

        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = generateFileName(file.getOriginalFilename());
        BlobId blobId = BlobId.of(firebaseBucketName, folderName.name()+"/" + fileName);

        // Blob bilgisini oluştur ve dosyayı yükle
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        bucket.create(blobInfo.getName(), file.getBytes(), file.getContentType());
        return new Image(fileName,folderName,getImageUrl(folderName.name(),fileName));
    }

    public void deleteForBusiness(String fileName) throws IOException {
        delete(fileName,FolderEnum.BUSINESS_LOGO);
    }

    public void deleteForProduct(String fileName) throws IOException {
        delete(fileName,FolderEnum.PRODUCTS);
    }

    public void delete(String fileName,FolderEnum folder) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        if (StringUtils.isEmpty(fileName)) {
            throw new IOException("invalid file name");
        }
        Blob blob = bucket.get(folder.name()+"/"+fileName);
        if (blob == null) {
            throw new IOException("file not found");
        }
        blob.delete();
    }

    public String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString();
    }

    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {

        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    public byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            ImageIO.write(bufferedImage, format, baos);

            baos.flush();

            return baos.toByteArray();

        } catch (IOException e) {
            throw e;
        } finally {
            baos.close();
        }
    }
}


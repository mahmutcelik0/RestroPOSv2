package com.restropos.systemimage.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemimage.constants.FolderEnum;
import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.Workspace;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(firebaseBucketName)
                    .setDatabaseUrl("https://restropos.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public String getImageUrl(String folderName, String fileName) {
        var str = folderName + "%2F" + fileName;
        return String.format(firebaseImageUrl, str);
    }

    public Image saveForBusiness(MultipartFile file) throws NotFoundException, IOException {
        return save(file, FolderEnum.BUSINESS_LOGO);
    }

    public Image saveForProduct(MultipartFile file) throws NotFoundException, IOException {
        return save(file, FolderEnum.PRODUCTS);
    }

    public Image save(MultipartFile file, FolderEnum folderName) throws IOException, NotFoundException, IllegalArgumentException {
        if (file.isEmpty()) throw new NotFoundException("Image not found");

        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = generateFileName(file.getOriginalFilename());
        BlobId blobId = BlobId.of(firebaseBucketName, folderName.name() + "/" + fileName);

        // Blob bilgisini oluştur ve dosyayı yükle
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        bucket.create(blobInfo.getName(), file.getBytes(), file.getContentType());
        return new Image(fileName, folderName, getImageUrl(folderName.name(), fileName));
    }

    public Image save(BufferedImage bufferedImage, FolderEnum folderName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] bytes = baos.toByteArray();

        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = generateFileName("");
        BlobId blobId = BlobId.of(firebaseBucketName, folderName.name() + "/" + fileName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .build();
        bucket.create(blobInfo.getName(), bytes,"image/png");
        return new Image(fileName, folderName, getImageUrl(folderName.name(), fileName));
    }


    public void deleteForBusiness(String fileName) throws IOException {
        delete(fileName, FolderEnum.BUSINESS_LOGO);
    }

    public void deleteForProduct(String fileName) throws IOException {
        delete(fileName, FolderEnum.PRODUCTS);
    }

    public void deleteForTable(String fileName) throws IOException {
        delete(fileName, FolderEnum.TABLES);
    }

    public void delete(String fileName, FolderEnum folder) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        if (StringUtils.isEmpty(fileName)) {
            throw new IOException("invalid file name");
        }
        Blob blob = bucket.get(folder.name() + "/" + fileName);
        if (blob == null) {
            throw new IOException("file not found");
        }
        blob.delete();
    }

    public String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString();
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

    public Image generateQrForTable(String tableId, String origin) throws Exception {
        return save(generateQRCodeImage(origin + "/table/" + tableId), FolderEnum.TABLES);
    }

    public BufferedImage generateQrCode(String data) {
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300, hintMap);

            // Convert bit matrix to buffered image
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            System.out.println("Error generating QR Code: " + e.getMessage());
            throw new RuntimeException("PROBLMMM");
        }
    }

    public static BufferedImage generateQRCodeImage(String data) throws Exception {
        StringBuilder str = new StringBuilder();
        str = str.append(data);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(str.toString(), BarcodeFormat.QR_CODE, 300, 300);

        var bufferedImage =  MatrixToImageWriter.toBufferedImage(bitMatrix);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight(); // Adjust this value based on your subtext size
        BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();

        // Draw the QR code image
        g.drawImage(bufferedImage, 0, 0, null);

        // Draw the subtext below the QR code
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 18)); // Adjust font and size as needed

        g.dispose();

        return combinedImage;
    }

}


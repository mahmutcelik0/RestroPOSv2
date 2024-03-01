package com.restropos.systemimage.api;

import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemimage.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageApi {
    @Autowired
    ImageService imageService;


    @PostMapping
    public ResponseEntity create(@RequestParam(name = "file") MultipartFile[] files) {
        for (MultipartFile file : files) {
            try {
                String fileName = imageService.save(file);
                String imageUrl = imageService.getImageUrl(fileName);
                // do whatever you want with that
                LogUtil.printLog("FILE NAME:"+fileName,ImageApi.class);
                LogUtil.printLog("IMAGE URL:"+imageUrl,ImageApi.class);
            } catch (Exception e) {
                //  throw internal error;
            }
        }
        return ResponseEntity.ok().build();
    }
}

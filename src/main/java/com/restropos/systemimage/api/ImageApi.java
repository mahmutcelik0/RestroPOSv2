package com.restropos.systemimage.api;

import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemimage.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageApi {
    @Autowired
    private ImageService imageService;


    @PostMapping
    public ResponseEntity create(@RequestParam(name = "file") MultipartFile file,@RequestParam(name = "name") String multipartFile) {
        try {
            return ResponseEntity.ok(imageService.saveForBusiness(file));
        } catch (Exception e) {
            LogUtil.printLog("Problem exists",ImageApi.class);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam(name = "name") String fileName) {
        try {
            imageService.deleteForBusiness(fileName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LogUtil.printLog("Problem exists",ImageApi.class);
            return ResponseEntity.internalServerError().build();
        }
    }
}

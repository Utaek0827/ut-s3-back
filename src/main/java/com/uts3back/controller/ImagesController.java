package com.uts3back.controller;

import com.uts3back.service.ImagesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImagesController {

    @Autowired
    ImagesService imagesService;

    @Operation(summary = "이미지 업로드", description = "사용자가 선택한 이미지 업로드")
    @PostMapping(value = "/upload" , consumes = {"multipart/form-data"})
    public ResponseEntity<String> imageUpload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("userServiceID") String userServiceID) throws Exception {

        imagesService.uploadFileService(file, userServiceID);


        return ResponseEntity.ok("서비스 생성 성공");
    }


}

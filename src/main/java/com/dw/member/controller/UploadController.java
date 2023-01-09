package com.dw.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    
    @PostMapping("/api/v1/image")
    public boolean uploadImage(@RequestParam("image") MultipartFile[] locationFile){
      // MultipartFile[] : 클래스로 사진을 전송 받음.

        System.out.println("사진 이름은 : "+locationFile[0].getName());
        System.out.println("사진 사이즈는 : "+locationFile[0].getSize());


        
        return true;
    }

}
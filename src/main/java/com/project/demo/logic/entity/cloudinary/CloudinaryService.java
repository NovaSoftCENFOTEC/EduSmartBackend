package com.project.demo.logic.entity.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    //Todo: Implement a method to upload videos or other files if its needed.
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No se subió ningún archivo.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
            throw new IllegalArgumentException("Solo se permiten archivos de imagen con extensiones .jpg, .jpeg, .png, .gif, .bmp o .webp.");
        }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    public String uploadPdf(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No se subió ningún archivo");
        }

        String fileName = file.getOriginalFilename();


        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos con extensión .pdf");
        }

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "raw")
        );
        return uploadResult.get("secure_url").toString();
    }

    public String uploadAudio(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No se subió ningún archivo");
        }

        String fileName = file.getOriginalFilename();


        if (fileName == null || !fileName.toLowerCase().matches(".*\\.(mp3|wav|ogg|m4a|flac)$")) {
            throw new IllegalArgumentException("Solo se permiten archivos de audio con extensiones .mp3, .wav, .ogg, .m4a o .flac");
        }

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "video")
        );
        return uploadResult.get("secure_url").toString();
    }


}

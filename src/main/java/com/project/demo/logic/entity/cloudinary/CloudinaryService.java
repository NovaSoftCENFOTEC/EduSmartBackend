package com.project.demo.logic.entity.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Servicio para la gestión de archivos en Cloudinary.
 * Permite subir archivos de audio y obtener la URL segura.
 */
@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Sube un archivo de audio a Cloudinary y retorna la URL segura.
     * @param fileBytes arreglo de bytes del archivo
     * @param fileName nombre del archivo (debe ser .mp3 o .wav)
     * @return URL segura del archivo subido
     * @throws IOException si ocurre un error al subir el archivo
     */
    public String uploadAudio(byte[] fileBytes, String fileName) throws IOException {
        logger.info("Iniciando la subida del archivo a Cloudinary: {}", fileName);

        if (fileBytes == null || fileBytes.length == 0) {
            logger.error("No se puede leer el archivo");
            throw new IllegalArgumentException("No hay datos para subir");
        }
        if (fileName == null || !fileName.toLowerCase().matches(".*\\.(mp3|wav)$")) {
            logger.error("Tipo de archivo no soportado: {}", fileName);
            throw new IllegalArgumentException("Solo archivos de tipo .mp3 o .wav son permitidos");
        }
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    fileBytes,
                    ObjectUtils.asMap("resource_type", "video", "public_id", fileName)
            );
            logger.info("Archivo subido exitosamente a Cloudinary: {}", uploadResult.get("secure_url"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            logger.error("Error al subir el archivo a Cloudinary: {}", e.getMessage());
            throw e;
        }
    }
}
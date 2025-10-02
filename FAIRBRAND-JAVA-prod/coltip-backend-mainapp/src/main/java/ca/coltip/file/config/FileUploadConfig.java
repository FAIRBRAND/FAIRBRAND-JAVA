package ca.coltip.file.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration pour le système d'upload de fichiers FAIR-006
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadConfig.class);

    @Value("${file.upload.base-directory:E:/uploads}")
    private String baseDirectory;

    @Value("${file.upload.base-url:http://localhost:8081/coltip/uploads}")
    private String baseUrl;

    @Value("${file.upload.allowed-types:pdf,pptx,mp4,mov,avi,png,jpg,jpeg}")
    private String allowedTypes;

    @Value("${file.upload.max-file-size:52428800}")
    private long maxFileSize;

    /**
     * Initialisation du répertoire d'upload au démarrage
     */
    @PostConstruct
    public void initializeUploadDirectory() {
        try {
            Path uploadPath = Paths.get(baseDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created upload directory: {}", uploadPath.toAbsolutePath());
            }

            // Vérifier les permissions d'écriture
            if (!Files.isWritable(uploadPath)) {
                logger.error("Upload directory is not writable: {}", uploadPath.toAbsolutePath());
                throw new RuntimeException("Upload directory is not writable: " + uploadPath);
            }

            logger.info("Upload directory configured: {}", uploadPath.toAbsolutePath());
            logger.info("Base URL configured: {}", baseUrl);
            logger.info("Max file size: {} bytes ({} MB)", maxFileSize, maxFileSize / (1024 * 1024));
            logger.info("Allowed file types: {}", allowedTypes);

        } catch (IOException e) {
            logger.error("Failed to initialize upload directory: {}", baseDirectory, e);
            throw new RuntimeException("Failed to initialize upload directory", e);
        }
    }

    /**
     * Configuration pour servir les fichiers uploadés via HTTP
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir les fichiers uploadés
        String uploadPath = "file:" + baseDirectory + "/";
        if (!baseDirectory.endsWith("/") && !baseDirectory.endsWith("\\")) {
            uploadPath = "file:" + baseDirectory + File.separator;
        }

        logger.info("Configuring resource handler - URL pattern: /uploads/**, Resource location: {}", uploadPath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath)
                .setCachePeriod(3600) // Cache 1 heure
                .resourceChain(true);
    }

    /**
     * Bean pour accéder au répertoire de base depuis les services
     */
    @Bean
    public String uploadBaseDirectory() {
        return baseDirectory;
    }

    /**
     * Bean pour accéder à l'URL de base depuis les services
     */
    @Bean
    public String uploadBaseUrl() {
        return baseUrl;
    }

    /**
     * Bean pour accéder aux types autorisés depuis les services
     */
    @Bean
    public String allowedFileTypes() {
        return allowedTypes;
    }

    /**
     * Bean pour accéder à la taille maximale depuis les services
     */
    @Bean
    public Long maxUploadFileSize() {
        return maxFileSize;
    }

    /**
     * Getter pour le répertoire de base
     */
    public String getBaseDirectory() {
        return baseDirectory;
    }

    /**
     * Getter pour l'URL de base
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Getter pour les types autorisés
     */
    public String getAllowedTypes() {
        return allowedTypes;
    }

    /**
     * Getter pour la taille maximale
     */
    public long getMaxFileSize() {
        return maxFileSize;
    }
}
package ca.coltip.file.service.impl;

import ca.coltip.file.data.dto.FileUploadDTO;
import ca.coltip.file.data.entities.FileUpload;
import ca.coltip.file.data.repository.FileUploadRepository;
import ca.coltip.file.enums.FileType;
import ca.coltip.file.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des fichiers
 * FAIR-006 : Système d'upload et de récupération de fichiers
 */
@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Value("${app.upload.directory:/var/app/uploads}")
    private String uploadDirectory;

    @Value("${app.upload.base-url:http://localhost:8081/uploads}")
    private String baseUrl;

    @Value("${app.upload.max-file-size:10485760}") // 10MB par défaut
    private long maxFileSize;

    @Override
    public FileUploadDTO uploadFile(MultipartFile file, Integer userId, String description, Boolean isPublic) {
        logger.info("Starting file upload for user {} - file: {}", userId, file.getOriginalFilename());

        // Validation du fichier
        validateFile(file);

        try {
            // Générer le nom unique selon le format [timestamp]_[uuid].extension
            String generatedName = generateUniqueFileName(file.getOriginalFilename());

            // Créer le répertoire de destination si nécessaire
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created upload directory: {}", uploadDirectory);
            }

            // Chemin complet du fichier
            Path filePath = uploadPath.resolve(generatedName);

            // Copier le fichier vers le répertoire de destination
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File physically saved to: {}", filePath.toAbsolutePath());

            // Extraire les informations du fichier
            String extension = getFileExtension(file.getOriginalFilename());
            FileType fileType = FileType.fromExtension(extension);

            // Créer l'entité FileUpload
            FileUpload fileUpload = new FileUpload(
                    file.getOriginalFilename(),
                    generatedName,
                    file.getSize(),
                    fileType,
                    file.getContentType(),
                    extension,
                    filePath.toAbsolutePath().toString(),
                    userId);

            fileUpload.setDescription(description);
            fileUpload.setIsPublic(isPublic != null ? isPublic : false);
            fileUpload.setAccessUrl(generateAccessUrl(generatedName));
            fileUpload.setCreatedBy(userId != null ? userId.toString() : "anonymous");

            // Sauvegarder en base de données
            FileUpload savedFile = fileUploadRepository.save(fileUpload);
            logger.info("File metadata saved to database with ID: {}", savedFile.getId());

            // Convertir en DTO et retourner
            return convertToDTO(savedFile);

        } catch (IOException e) {
            logger.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Erreur lors de l'upload du fichier: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during file upload", e);
            throw new RuntimeException("Erreur inattendue lors de l'upload: " + e.getMessage(), e);
        }
    }

    @Override
    public FileUploadDTO uploadFile(MultipartFile file, Integer userId) {
        return uploadFile(file, userId, null, false);
    }

    @Override
    @Transactional(readOnly = true)
    public String getFileUrl(String generatedName) {
        Optional<FileUpload> fileUpload = fileUploadRepository.findActiveByGeneratedName(generatedName);

        if (fileUpload.isEmpty()) {
            throw new RuntimeException("Fichier non trouvé: " + generatedName);
        }

        return generateAccessUrl(generatedName);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileUploadDTO> getFileInfo(String generatedName) {
        return fileUploadRepository.findActiveByGeneratedName(generatedName)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUploadDTO> getUserFiles(Integer userId) {
        return fileUploadRepository.findActiveFilesByUser(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUploadDTO> getFilesByType(FileType fileType) {
        return fileUploadRepository.findActiveFilesByType(fileType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUploadDTO> getUserFilesByType(Integer userId, FileType fileType) {
        return fileUploadRepository.findActiveFilesByUserAndType(userId, fileType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUploadDTO> searchFilesByName(String originalName) {
        return fileUploadRepository.findActiveFilesByOriginalNameContaining(originalName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteFile(String generatedName, Integer userId) {
        Optional<FileUpload> fileUpload = fileUploadRepository.findActiveByGeneratedName(generatedName);

        if (fileUpload.isEmpty()) {
            return false;
        }

        FileUpload file = fileUpload.get();

        // Vérifier les droits (l'utilisateur doit être le propriétaire)
        if (userId != null && !userId.equals(file.getUploadedByUser())) {
            throw new RuntimeException("Accès non autorisé pour supprimer ce fichier");
        }

        // Soft delete
        file.markAsDeleted();
        file.setUpdatedBy(userId != null ? userId.toString() : "system");
        fileUploadRepository.save(file);

        logger.info("File soft deleted: {} by user {}", generatedName, userId);
        return true;
    }

    @Override
    public boolean deleteFilePhysically(String generatedName, Integer userId) {
        Optional<FileUpload> fileUpload = fileUploadRepository.findActiveByGeneratedName(generatedName);

        if (fileUpload.isEmpty()) {
            return false;
        }

        FileUpload file = fileUpload.get();

        // Vérifier les droits
        if (userId != null && !userId.equals(file.getUploadedByUser())) {
            throw new RuntimeException("Accès non autorisé pour supprimer ce fichier");
        }

        try {
            // Supprimer le fichier physique
            Path filePath = Paths.get(file.getUploadPath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("Physical file deleted: {}", filePath);
            }

            // Supprimer l'enregistrement de la base
            fileUploadRepository.delete(file);
            logger.info("File record deleted from database: {}", generatedName);

            return true;
        } catch (IOException e) {
            logger.error("Failed to delete physical file: {}", file.getUploadPath(), e);
            throw new RuntimeException("Erreur lors de la suppression physique du fichier", e);
        }
    }

    @Override
    public Optional<FileUploadDTO> updateFileDescription(String generatedName, String description, Integer userId) {
        Optional<FileUpload> fileUpload = fileUploadRepository.findActiveByGeneratedName(generatedName);

        if (fileUpload.isEmpty()) {
            return Optional.empty();
        }

        FileUpload file = fileUpload.get();

        // Vérifier les droits
        if (userId != null && !userId.equals(file.getUploadedByUser())) {
            throw new RuntimeException("Accès non autorisé pour modifier ce fichier");
        }

        file.setDescription(description);
        file.setUpdatedAt(LocalDateTime.now());
        file.setUpdatedBy(userId != null ? userId.toString() : "system");

        FileUpload updatedFile = fileUploadRepository.save(file);
        return Optional.of(convertToDTO(updatedFile));
    }

    @Override
    public Optional<FileUploadDTO> updateFileVisibility(String generatedName, Boolean isPublic, Integer userId) {
        Optional<FileUpload> fileUpload = fileUploadRepository.findActiveByGeneratedName(generatedName);

        if (fileUpload.isEmpty()) {
            return Optional.empty();
        }

        FileUpload file = fileUpload.get();

        // Vérifier les droits
        if (userId != null && !userId.equals(file.getUploadedByUser())) {
            throw new RuntimeException("Accès non autorisé pour modifier ce fichier");
        }

        file.setIsPublic(isPublic);
        file.setUpdatedAt(LocalDateTime.now());
        file.setUpdatedBy(userId != null ? userId.toString() : "system");

        FileUpload updatedFile = fileUploadRepository.save(file);
        return Optional.of(convertToDTO(updatedFile));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUploadDTO> getPublicFiles() {
        return fileUploadRepository.findActivePublicFiles()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FileUploadStatsDTO getUserFileStats(Integer userId) {
        Long fileCount = fileUploadRepository.countActiveFilesByUser(userId);
        Long totalSize = fileUploadRepository.getTotalFileSizeByUser(userId);

        return new FileUploadStatsDTO(fileCount, totalSize);
    }

    @Override
    public boolean validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide ou null");
        }

        // Vérifier la taille
        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("Le fichier est trop volumineux. Taille maximale autorisée: " +
                    formatFileSize(maxFileSize));
        }

        // Vérifier l'extension
        String extension = getFileExtension(file.getOriginalFilename());
        if (!FileType.isSupported(extension)) {
            throw new RuntimeException("Type de fichier non supporté: " + extension +
                    ". Types autorisés: PDF, PPTX, MP4, MOV, AVI, PNG, JPG");
        }

        // Vérifier le MIME type
        String mimeType = file.getContentType();
        if (!FileType.isSupportedMimeType(mimeType)) {
            logger.warn("MIME type potentially unsupported: {} for file: {}", mimeType, file.getOriginalFilename());
            // On continue car l'extension est valide
        }

        return true;
    }

    @Override
    public int cleanupOldDeletedFiles(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        List<FileUpload> oldDeletedFiles = fileUploadRepository.findDeletedFilesBefore(cutoffDate);

        int cleanedCount = 0;
        for (FileUpload file : oldDeletedFiles) {
            try {
                // Supprimer le fichier physique s'il existe
                Path filePath = Paths.get(file.getUploadPath());
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }

                // Supprimer l'enregistrement de la base
                fileUploadRepository.delete(file);
                cleanedCount++;

            } catch (Exception e) {
                logger.error("Failed to cleanup old deleted file: {}", file.getGeneratedName(), e);
            }
        }

        logger.info("Cleaned up {} old deleted files", cleanedCount);
        return cleanedCount;
    }

    // Méthodes utilitaires privées

    private String generateUniqueFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();

        String generatedName = timestamp + "_" + uuid + "." + extension;

        // S'assurer que le nom est unique (très peu probable, mais sécurité)
        while (fileUploadRepository.existsByGeneratedName(generatedName)) {
            uuid = UUID.randomUUID().toString();
            generatedName = timestamp + "_" + uuid + "." + extension;
        }

        return generatedName;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            throw new RuntimeException("Le fichier doit avoir une extension");
        }

        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private String generateAccessUrl(String generatedName) {
        String cleanBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return cleanBaseUrl + "/" + generatedName;
    }

    private FileUploadDTO convertToDTO(FileUpload fileUpload) {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setId(fileUpload.getId());
        dto.setOriginalName(fileUpload.getOriginalName());
        dto.setGeneratedName(fileUpload.getGeneratedName());
        dto.setFileSize(fileUpload.getFileSize());
        dto.setFileType(fileUpload.getFileType());
        dto.setMimeType(fileUpload.getMimeType());
        dto.setFileExtension(fileUpload.getFileExtension());
        dto.setAccessUrl(fileUpload.getAccessUrl());
        dto.setDescription(fileUpload.getDescription());
        dto.setIsPublic(fileUpload.getIsPublic());
        dto.setUploadedByUser(fileUpload.getUploadedByUser());
        dto.setUploadedAt(fileUpload.getUploadedAt());
        dto.setCreatedAt(fileUpload.getCreatedAt());
        dto.setCreatedBy(fileUpload.getCreatedBy());

        return dto;
    }

    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes <= 0)
            return "0 B";
        String[] units = { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.1f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}
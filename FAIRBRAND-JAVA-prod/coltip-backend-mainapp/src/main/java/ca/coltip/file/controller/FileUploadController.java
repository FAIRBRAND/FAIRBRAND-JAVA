package ca.coltip.file.controller;

import ca.coltip.file.data.dto.FileUploadDTO;
import ca.coltip.file.enums.FileType;
import ca.coltip.file.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des fichiers
 * FAIR-006 : Système d'upload et de récupération de fichiers
 */
@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * Upload un fichier
     * POST /api/files/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "isPublic", defaultValue = "false") Boolean isPublic) {

        try {
            logger.info("Upload request - File: {}, User: {}, Size: {} bytes",
                    file.getOriginalFilename(), userId, file.getSize());

            FileUploadDTO uploadedFile = fileUploadService.uploadFile(file, userId, description, isPublic);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Fichier uploadé avec succès");
            response.put("file", uploadedFile);

            logger.info("File uploaded successfully - Generated name: {}, Access URL: {}",
                    uploadedFile.getGeneratedName(), uploadedFile.getAccessUrl());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            logger.error("Upload failed for file: {}", file.getOriginalFilename(), e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Unexpected error during upload", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur interne du serveur");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupère l'URL publique d'un fichier
     * GET /api/files/{generatedName}/url
     */
    @GetMapping("/{generatedName}/url")
    public ResponseEntity<Map<String, Object>> getFileUrl(@PathVariable String generatedName) {
        try {
            logger.info("URL request for file: {}", generatedName);

            String fileUrl = fileUploadService.getFileUrl(generatedName);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("generatedName", generatedName);
            response.put("url", fileUrl);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            logger.error("File not found: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère les informations détaillées d'un fichier
     * GET /api/files/{generatedName}/info
     */
    @GetMapping("/{generatedName}/info")
    public ResponseEntity<Map<String, Object>> getFileInfo(@PathVariable String generatedName) {
        try {
            logger.info("Info request for file: {}", generatedName);

            Optional<FileUploadDTO> fileInfo = fileUploadService.getFileInfo(generatedName);

            if (fileInfo.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("file", fileInfo.get());

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Fichier non trouvé");

                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            logger.error("Error retrieving file info: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des informations");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupère tous les fichiers d'un utilisateur
     * GET /api/files/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserFiles(@PathVariable Integer userId) {
        try {
            logger.info("User files request for user: {}", userId);

            List<FileUploadDTO> userFiles = fileUploadService.getUserFiles(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("files", userFiles);
            response.put("count", userFiles.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error retrieving user files for user: {}", userId, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des fichiers");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupère les fichiers par type
     * GET /api/files/type/{fileType}
     */
    @GetMapping("/type/{fileType}")
    public ResponseEntity<Map<String, Object>> getFilesByType(@PathVariable String fileType) {
        try {
            logger.info("Files by type request: {}", fileType);

            FileType type = FileType.valueOf(fileType.toUpperCase());
            List<FileUploadDTO> files = fileUploadService.getFilesByType(type);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileType", fileType);
            response.put("files", files);
            response.put("count", files.size());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid file type: {}", fileType);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Type de fichier invalide: " + fileType);

            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Error retrieving files by type: {}", fileType, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des fichiers");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupère les fichiers d'un utilisateur par type
     * GET /api/files/user/{userId}/type/{fileType}
     */
    @GetMapping("/user/{userId}/type/{fileType}")
    public ResponseEntity<Map<String, Object>> getUserFilesByType(
            @PathVariable Integer userId,
            @PathVariable String fileType) {
        try {
            logger.info("User files by type request - User: {}, Type: {}", userId, fileType);

            FileType type = FileType.valueOf(fileType.toUpperCase());
            List<FileUploadDTO> files = fileUploadService.getUserFilesByType(userId, type);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("fileType", fileType);
            response.put("files", files);
            response.put("count", files.size());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Type de fichier invalide: " + fileType);

            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Error retrieving user files by type - User: {}, Type: {}", userId, fileType, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des fichiers");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Recherche des fichiers par nom original
     * GET /api/files/search?name={originalName}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFilesByName(
            @RequestParam("name") String originalName) {
        try {
            logger.info("Search files request: {}", originalName);

            List<FileUploadDTO> files = fileUploadService.searchFilesByName(originalName);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("searchTerm", originalName);
            response.put("files", files);
            response.put("count", files.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error searching files by name: {}", originalName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la recherche");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupère tous les fichiers publics
     * GET /api/files/public
     */
    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> getPublicFiles() {
        try {
            logger.info("Public files request");

            List<FileUploadDTO> publicFiles = fileUploadService.getPublicFiles();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("files", publicFiles);
            response.put("count", publicFiles.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error retrieving public files", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des fichiers publics");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Supprime un fichier (soft delete)
     * DELETE /api/files/{generatedName}
     */
    @DeleteMapping("/{generatedName}")
    public ResponseEntity<Map<String, Object>> deleteFile(
            @PathVariable String generatedName,
            @RequestParam(value = "userId", required = false) Integer userId) {
        try {
            logger.info("Delete file request: {} by user: {}", generatedName, userId);

            boolean deleted = fileUploadService.deleteFile(generatedName, userId);

            Map<String, Object> response = new HashMap<>();
            if (deleted) {
                response.put("success", true);
                response.put("message", "Fichier supprimé avec succès");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Fichier non trouvé");
                return ResponseEntity.notFound().build();
            }

        } catch (RuntimeException e) {
            logger.error("Error deleting file: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            logger.error("Unexpected error deleting file: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la suppression");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Supprime définitivement un fichier (hard delete)
     * DELETE /api/files/{generatedName}/permanent
     */
    @DeleteMapping("/{generatedName}/permanent")
    public ResponseEntity<Map<String, Object>> deleteFilePhysically(
            @PathVariable String generatedName,
            @RequestParam(value = "userId", required = false) Integer userId) {
        try {
            logger.info("Permanent delete file request: {} by user: {}", generatedName, userId);

            boolean deleted = fileUploadService.deleteFilePhysically(generatedName, userId);

            Map<String, Object> response = new HashMap<>();
            if (deleted) {
                response.put("success", true);
                response.put("message", "Fichier supprimé définitivement");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Fichier non trouvé");
                return ResponseEntity.notFound().build();
            }

        } catch (RuntimeException e) {
            logger.error("Error permanently deleting file: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            logger.error("Unexpected error permanently deleting file: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la suppression définitive");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Met à jour la description d'un fichier
     * PUT /api/files/{generatedName}/description
     */
    @PutMapping("/{generatedName}/description")
    public ResponseEntity<Map<String, Object>> updateFileDescription(
            @PathVariable String generatedName,
            @RequestParam("description") String description,
            @RequestParam(value = "userId", required = false) Integer userId) {
        try {
            logger.info("Update description request for file: {} by user: {}", generatedName, userId);

            Optional<FileUploadDTO> updatedFile = fileUploadService.updateFileDescription(
                    generatedName, description, userId);

            Map<String, Object> response = new HashMap<>();
            if (updatedFile.isPresent()) {
                response.put("success", true);
                response.put("message", "Description mise à jour");
                response.put("file", updatedFile.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Fichier non trouvé");
                return ResponseEntity.notFound().build();
            }

        } catch (RuntimeException e) {
            logger.error("Error updating file description: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            logger.error("Unexpected error updating file description: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la mise à jour");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Change la visibilité d'un fichier (public/privé)
     * PUT /api/files/{generatedName}/visibility
     */
    @PutMapping("/{generatedName}/visibility")
    public ResponseEntity<Map<String, Object>> updateFileVisibility(
            @PathVariable String generatedName,
            @RequestParam("isPublic") Boolean isPublic,
            @RequestParam(value = "userId", required = false) Integer userId) {
        try {
            logger.info("Update visibility request for file: {} to {} by user: {}",
                    generatedName, isPublic, userId);

            Optional<FileUploadDTO> updatedFile = fileUploadService.updateFileVisibility(
                    generatedName, isPublic, userId);

            Map<String, Object> response = new HashMap<>();
            if (updatedFile.isPresent()) {
                response.put("success", true);
                response.put("message", "Visibilité mise à jour");
                response.put("file", updatedFile.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Fichier non trouvé");
                return ResponseEntity.notFound().build();
            }

        } catch (RuntimeException e) {
            logger.error("Error updating file visibility: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            logger.error("Unexpected error updating file visibility: {}", generatedName, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la mise à jour");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupère les statistiques d'utilisation d'un utilisateur
     * GET /api/files/user/{userId}/stats
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable Integer userId) {
        try {
            logger.info("User stats request for user: {}", userId);

            FileUploadService.FileUploadStatsDTO stats = fileUploadService.getUserFileStats(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("stats", Map.of(
                    "fileCount", stats.getFileCount(),
                    "totalSize", stats.getTotalSize(),
                    "totalSizeFormatted", stats.getTotalSizeFormatted()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error retrieving user stats for user: {}", userId, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des statistiques");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupère les types de fichiers supportés
     * GET /api/files/supported-types
     */
    @GetMapping("/supported-types")
    public ResponseEntity<Map<String, Object>> getSupportedTypes() {
        try {
            logger.info("Supported types request");

            Map<String, Object> types = new HashMap<>();

            // Documents
            types.put("documents", Map.of(
                    "PDF", Map.of("extension", "pdf", "mimeType", "application/pdf", "description", "Documents PDF"),
                    "PPTX",
                    Map.of("extension", "pptx", "mimeType",
                            "application/vnd.openxmlformats-officedocument.presentationml.presentation", "description",
                            "Présentations PowerPoint")));

            // Videos
            types.put("videos", Map.of(
                    "MP4", Map.of("extension", "mp4", "mimeType", "video/mp4", "description", "Vidéos MP4"),
                    "MOV", Map.of("extension", "mov", "mimeType", "video/quicktime", "description", "Vidéos QuickTime"),
                    "AVI", Map.of("extension", "avi", "mimeType", "video/x-msvideo", "description", "Vidéos AVI")));

            // Images
            types.put("images", Map.of(
                    "PNG", Map.of("extension", "png", "mimeType", "image/png", "description", "Images PNG"),
                    "JPG", Map.of("extension", "jpg", "mimeType", "image/jpeg", "description", "Images JPEG")));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("supportedTypes", types);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error retrieving supported types", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des types supportés");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint de nettoyage administrateur
     * POST /api/files/admin/cleanup
     */
    @PostMapping("/admin/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupOldFiles(
            @RequestParam(value = "daysOld", defaultValue = "30") int daysOld) {
        try {
            logger.info("Cleanup request for files older than {} days", daysOld);

            int cleanedCount = fileUploadService.cleanupOldDeletedFiles(daysOld);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Nettoyage terminé");
            response.put("cleanedFilesCount", cleanedCount);
            response.put("daysOld", daysOld);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error during cleanup", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors du nettoyage");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
package ca.coltip.file.service;

import ca.coltip.file.data.dto.FileUploadDTO;
import ca.coltip.file.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service de gestion des fichiers
 * FAIR-006 : Système d'upload et de récupération de fichiers
 */
public interface FileUploadService {

    /**
     * Upload un fichier avec validation et traitement complet
     * 
     * @param file        Le fichier à uploader
     * @param userId      L'ID de l'utilisateur (optionnel)
     * @param description Description du fichier (optionnel)
     * @param isPublic    Si le fichier doit être public
     * @return Le DTO du fichier uploadé
     * @throws RuntimeException Si l'upload échoue
     */
    FileUploadDTO uploadFile(MultipartFile file, Integer userId, String description, Boolean isPublic);

    /**
     * Upload un fichier avec paramètres par défaut
     * 
     * @param file   Le fichier à uploader
     * @param userId L'ID de l'utilisateur
     * @return Le DTO du fichier uploadé
     */
    FileUploadDTO uploadFile(MultipartFile file, Integer userId);

    /**
     * Récupère l'URL publique d'un fichier
     * 
     * @param generatedName Le nom généré du fichier
     * @return L'URL publique du fichier
     * @throws RuntimeException Si le fichier n'existe pas
     */
    String getFileUrl(String generatedName);

    /**
     * Récupère les informations d'un fichier par son nom généré
     * 
     * @param generatedName Le nom généré du fichier
     * @return Le DTO du fichier ou empty si non trouvé
     */
    Optional<FileUploadDTO> getFileInfo(String generatedName);

    /**
     * Récupère tous les fichiers d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return La liste des fichiers de l'utilisateur
     */
    List<FileUploadDTO> getUserFiles(Integer userId);

    /**
     * Récupère tous les fichiers d'un type donné
     * 
     * @param fileType Le type de fichier
     * @return La liste des fichiers du type spécifié
     */
    List<FileUploadDTO> getFilesByType(FileType fileType);

    /**
     * Récupère les fichiers d'un utilisateur par type
     * 
     * @param userId   L'ID de l'utilisateur
     * @param fileType Le type de fichier
     * @return La liste des fichiers
     */
    List<FileUploadDTO> getUserFilesByType(Integer userId, FileType fileType);

    /**
     * Recherche des fichiers par nom original
     * 
     * @param originalName Le nom original (recherche partielle)
     * @return La liste des fichiers correspondants
     */
    List<FileUploadDTO> searchFilesByName(String originalName);

    /**
     * Supprime un fichier (soft delete)
     * 
     * @param generatedName Le nom généré du fichier
     * @param userId        L'ID de l'utilisateur (pour vérification des droits)
     * @return true si la suppression a réussi
     */
    boolean deleteFile(String generatedName, Integer userId);

    /**
     * Supprime physiquement un fichier (hard delete)
     * 
     * @param generatedName Le nom généré du fichier
     * @param userId        L'ID de l'utilisateur (pour vérification des droits)
     * @return true si la suppression a réussi
     */
    boolean deleteFilePhysically(String generatedName, Integer userId);

    /**
     * Met à jour la description d'un fichier
     * 
     * @param generatedName Le nom généré du fichier
     * @param description   La nouvelle description
     * @param userId        L'ID de l'utilisateur (pour vérification des droits)
     * @return Le DTO mis à jour ou empty si échec
     */
    Optional<FileUploadDTO> updateFileDescription(String generatedName, String description, Integer userId);

    /**
     * Change la visibilité d'un fichier (public/privé)
     * 
     * @param generatedName Le nom généré du fichier
     * @param isPublic      La nouvelle visibilité
     * @param userId        L'ID de l'utilisateur (pour vérification des droits)
     * @return Le DTO mis à jour ou empty si échec
     */
    Optional<FileUploadDTO> updateFileVisibility(String generatedName, Boolean isPublic, Integer userId);

    /**
     * Récupère tous les fichiers publics
     * 
     * @return La liste des fichiers publics
     */
    List<FileUploadDTO> getPublicFiles();

    /**
     * Obtient les statistiques d'utilisation d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return Un map avec les statistiques (count, totalSize)
     */
    FileUploadStatsDTO getUserFileStats(Integer userId);

    /**
     * Valide si un fichier peut être uploadé
     * 
     * @param file Le fichier à valider
     * @return true si le fichier est valide
     * @throws RuntimeException Si le fichier n'est pas valide (avec message
     *                          détaillé)
     */
    boolean validateFile(MultipartFile file);

    /**
     * Nettoie les fichiers supprimés anciens (tâche de maintenance)
     * 
     * @param daysOld Nombre de jours d'ancienneté pour le nettoyage
     * @return Nombre de fichiers nettoyés
     */
    int cleanupOldDeletedFiles(int daysOld);

    /**
     * DTO pour les statistiques d'utilisation
     */
    class FileUploadStatsDTO {
        private Long fileCount;
        private Long totalSize;
        private String totalSizeFormatted;

        public FileUploadStatsDTO(Long fileCount, Long totalSize) {
            this.fileCount = fileCount;
            this.totalSize = totalSize;
            this.totalSizeFormatted = formatFileSize(totalSize);
        }

        // Getters
        public Long getFileCount() {
            return fileCount;
        }

        public Long getTotalSize() {
            return totalSize;
        }

        public String getTotalSizeFormatted() {
            return totalSizeFormatted;
        }

        private String formatFileSize(Long bytes) {
            if (bytes == null || bytes <= 0)
                return "0 B";
            String[] units = { "B", "KB", "MB", "GB", "TB" };
            int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
            return String.format("%.1f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
        }
    }
}
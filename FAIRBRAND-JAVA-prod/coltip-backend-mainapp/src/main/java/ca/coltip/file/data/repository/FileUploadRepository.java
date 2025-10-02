package ca.coltip.file.data.repository;

import ca.coltip.file.data.entities.FileUpload;
import ca.coltip.file.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des fichiers uploadés
 * FAIR-006 : Système d'upload et de récupération de fichiers
 */
@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Integer> {

    /**
     * Trouve un fichier par son nom généré
     * 
     * @param generatedName Le nom généré du fichier
     * @return Le fichier trouvé ou empty si non trouvé
     */
    Optional<FileUpload> findByGeneratedNameAndRecordStatus(String generatedName, Integer recordStatus);

    /**
     * Trouve un fichier actif par son nom généré
     * 
     * @param generatedName Le nom généré du fichier
     * @return Le fichier trouvé ou empty si non trouvé
     */
    default Optional<FileUpload> findActiveByGeneratedName(String generatedName) {
        return findByGeneratedNameAndRecordStatus(generatedName, 1);
    }

    /**
     * Trouve tous les fichiers actifs d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return La liste des fichiers de l'utilisateur
     */
    @Query("SELECT f FROM FileUpload f WHERE f.uploadedByUser = :userId AND f.recordStatus = 1 ORDER BY f.uploadedAt DESC")
    List<FileUpload> findActiveFilesByUser(@Param("userId") Integer userId);

    /**
     * Trouve tous les fichiers actifs d'un type donné
     * 
     * @param fileType Le type de fichier
     * @return La liste des fichiers du type spécifié
     */
    @Query("SELECT f FROM FileUpload f WHERE f.fileType = :fileType AND f.recordStatus = 1 ORDER BY f.uploadedAt DESC")
    List<FileUpload> findActiveFilesByType(@Param("fileType") FileType fileType);

    /**
     * Trouve tous les fichiers publics actifs
     * 
     * @return La liste des fichiers publics
     */
    @Query("SELECT f FROM FileUpload f WHERE f.isPublic = true AND f.recordStatus = 1 ORDER BY f.uploadedAt DESC")
    List<FileUpload> findActivePublicFiles();

    /**
     * Trouve tous les fichiers actifs uploadés après une date donnée
     * 
     * @param date La date de référence
     * @return La liste des fichiers récents
     */
    @Query("SELECT f FROM FileUpload f WHERE f.uploadedAt >= :date AND f.recordStatus = 1 ORDER BY f.uploadedAt DESC")
    List<FileUpload> findActiveFilesAfterDate(@Param("date") LocalDateTime date);

    /**
     * Compte le nombre de fichiers actifs d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return Le nombre de fichiers
     */
    @Query("SELECT COUNT(f) FROM FileUpload f WHERE f.uploadedByUser = :userId AND f.recordStatus = 1")
    Long countActiveFilesByUser(@Param("userId") Integer userId);

    /**
     * Calcule la taille totale des fichiers d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return La taille totale en octets
     */
    @Query("SELECT COALESCE(SUM(f.fileSize), 0) FROM FileUpload f WHERE f.uploadedByUser = :userId AND f.recordStatus = 1")
    Long getTotalFileSizeByUser(@Param("userId") Integer userId);

    /**
     * Trouve les fichiers en fonction du nom original (recherche partielle)
     * 
     * @param originalName Le nom original (partiel)
     * @return La liste des fichiers correspondants
     */
    @Query("SELECT f FROM FileUpload f WHERE LOWER(f.originalName) LIKE LOWER(CONCAT('%', :originalName, '%')) AND f.recordStatus = 1 ORDER BY f.uploadedAt DESC")
    List<FileUpload> findActiveFilesByOriginalNameContaining(@Param("originalName") String originalName);

    /**
     * Trouve les fichiers d'un utilisateur par type
     * 
     * @param userId   L'ID de l'utilisateur
     * @param fileType Le type de fichier
     * @return La liste des fichiers
     */
    @Query("SELECT f FROM FileUpload f WHERE f.uploadedByUser = :userId AND f.fileType = :fileType AND f.recordStatus = 1 ORDER BY f.uploadedAt DESC")
    List<FileUpload> findActiveFilesByUserAndType(@Param("userId") Integer userId,
            @Param("fileType") FileType fileType);

    /**
     * Vérifie si un fichier avec ce nom généré existe déjà
     * 
     * @param generatedName Le nom généré à vérifier
     * @return true si le fichier existe
     */
    boolean existsByGeneratedName(String generatedName);

    /**
     * Trouve tous les fichiers actifs avec pagination et tri
     * 
     * @return La liste paginée des fichiers actifs
     */
    @Query("SELECT f FROM FileUpload f WHERE f.recordStatus = 1 ORDER BY f.uploadedAt DESC")
    List<FileUpload> findAllActiveFiles();

    /**
     * Trouve les fichiers supprimés (soft delete) pour nettoyage
     * 
     * @param beforeDate Date avant laquelle chercher
     * @return La liste des fichiers supprimés
     */
    @Query("SELECT f FROM FileUpload f WHERE f.recordStatus = 0 AND f.updatedAt < :beforeDate")
    List<FileUpload> findDeletedFilesBefore(@Param("beforeDate") LocalDateTime beforeDate);
}
package ca.coltip.file.data.entities;

import ca.coltip.file.enums.FileType;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

/**
 * Entité JPA pour la gestion des fichiers uploadés
 * FAIR-006 : Système d'upload et de récupération de fichiers
 */
@Entity
@Table(name = "file_upload")
public class FileUpload {

    @Id
    @Column(name = "id_file")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Integer id;

    @Column(name = "original_name", nullable = false, length = 255)
    private String originalName;

    @Column(name = "generated_name", nullable = false, unique = true, length = 255)
    private String generatedName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 50)
    private FileType fileType;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "file_extension", nullable = false, length = 10)
    private String fileExtension;

    @Column(name = "upload_path", nullable = false, length = 500)
    private String uploadPath;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "uploaded_by_user")
    private Integer uploadedByUser;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "access_url", length = 500)
    private String accessUrl;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "record_status")
    private Integer recordStatus = 1; // 1=Actif, 0=Supprimé

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    // Constructeurs
    public FileUpload() {
        this.uploadedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.recordStatus = 1;
        this.isPublic = false;
    }

    public FileUpload(String originalName, String generatedName, Long fileSize,
            FileType fileType, String mimeType, String fileExtension,
            String uploadPath, Integer uploadedByUser) {
        this();
        this.originalName = originalName;
        this.generatedName = generatedName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.fileExtension = fileExtension;
        this.uploadPath = uploadPath;
        this.uploadedByUser = uploadedByUser;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getGeneratedName() {
        return generatedName;
    }

    public void setGeneratedName(String generatedName) {
        this.generatedName = generatedName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Integer getUploadedByUser() {
        return uploadedByUser;
    }

    public void setUploadedByUser(Integer uploadedByUser) {
        this.uploadedByUser = uploadedByUser;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Méthodes utilitaires
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marque le fichier comme supprimé (soft delete)
     */
    public void markAsDeleted() {
        this.recordStatus = 0;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Vérifie si le fichier est actif
     */
    public boolean isActive() {
        return this.recordStatus != null && this.recordStatus == 1;
    }

    /**
     * Génère l'URL d'accès complet avec une base URL
     */
    public String generateAccessUrl(String baseUrl) {
        if (baseUrl == null || generatedName == null) {
            return null;
        }
        String cleanBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return cleanBaseUrl + "/" + generatedName;
    }

    @Override
    public String toString() {
        return "FileUpload{" +
                "id=" + id +
                ", originalName='" + originalName + '\'' +
                ", generatedName='" + generatedName + '\'' +
                ", fileSize=" + fileSize +
                ", fileType=" + fileType +
                ", uploadedAt=" + uploadedAt +
                ", uploadedByUser=" + uploadedByUser +
                ", isPublic=" + isPublic +
                '}';
    }
}
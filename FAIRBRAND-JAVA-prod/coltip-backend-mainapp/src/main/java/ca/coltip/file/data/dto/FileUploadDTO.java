package ca.coltip.file.data.dto;

import ca.coltip.file.enums.FileType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * DTO pour les transferts de données des fichiers uploadés
 * FAIR-006 : Système d'upload et de récupération de fichiers
 */
public class FileUploadDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("originalName")
    private String originalName;

    @JsonProperty("generatedName")
    private String generatedName;

    @JsonProperty("fileSize")
    private Long fileSize;

    @JsonProperty("fileSizeFormatted")
    private String fileSizeFormatted;

    @JsonProperty("fileType")
    private FileType fileType;

    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("fileExtension")
    private String fileExtension;

    @JsonProperty("accessUrl")
    private String accessUrl;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isPublic")
    private Boolean isPublic;

    @JsonProperty("uploadedByUser")
    private Integer uploadedByUser;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("uploadedAt")
    private LocalDateTime uploadedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("createdBy")
    private String createdBy;

    // Constructeurs
    public FileUploadDTO() {
    }

    public FileUploadDTO(String originalName, String generatedName, Long fileSize,
            FileType fileType, String accessUrl) {
        this.originalName = originalName;
        this.generatedName = generatedName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.accessUrl = accessUrl;
        this.fileSizeFormatted = formatFileSize(fileSize);
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
        this.fileSizeFormatted = formatFileSize(fileSize);
    }

    public String getFileSizeFormatted() {
        return fileSizeFormatted;
    }

    public void setFileSizeFormatted(String fileSizeFormatted) {
        this.fileSizeFormatted = fileSizeFormatted;
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getUploadedByUser() {
        return uploadedByUser;
    }

    public void setUploadedByUser(Integer uploadedByUser) {
        this.uploadedByUser = uploadedByUser;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
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

    // Méthodes utilitaires

    /**
     * Formate la taille du fichier en format lisible (B, KB, MB, GB)
     */
    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes <= 0)
            return "0 B";

        String[] units = { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));

        return String.format("%.1f %s",
                bytes / Math.pow(1024, digitGroups),
                units[digitGroups]);
    }

    /**
     * Vérifie si le fichier est une image
     */
    @JsonProperty("isImage")
    public boolean isImage() {
        return fileType == FileType.PNG || fileType == FileType.JPG;
    }

    /**
     * Vérifie si le fichier est une vidéo
     */
    @JsonProperty("isVideo")
    public boolean isVideo() {
        return fileType == FileType.MP4 || fileType == FileType.MOV || fileType == FileType.AVI;
    }

    /**
     * Vérifie si le fichier est un document
     */
    @JsonProperty("isDocument")
    public boolean isDocument() {
        return fileType == FileType.PDF || fileType == FileType.PPTX;
    }

    /**
     * Retourne une description lisible du type de fichier
     */
    @JsonProperty("fileTypeDescription")
    public String getFileTypeDescription() {
        return fileType != null ? fileType.getDescription() : "Type inconnu";
    }

    @Override
    public String toString() {
        return "FileUploadDTO{" +
                "id=" + id +
                ", originalName='" + originalName + '\'' +
                ", generatedName='" + generatedName + '\'' +
                ", fileSize=" + fileSize +
                ", fileType=" + fileType +
                ", accessUrl='" + accessUrl + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
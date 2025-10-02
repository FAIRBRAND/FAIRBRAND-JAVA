package ca.coltip.file.enums;

/**
 * Énumération des types de fichiers supportés
 * FAIR-006 : Types de fichiers autorisés pour l'upload
 */
public enum FileType {
    // Documents
    PDF("pdf", "application/pdf", "Documents PDF"),
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "Présentations PowerPoint"),

    // Vidéos
    MP4("mp4", "video/mp4", "Vidéos MP4"),
    MOV("mov", "video/quicktime", "Vidéos QuickTime"),
    AVI("avi", "video/x-msvideo", "Vidéos AVI"),

    // Images
    PNG("png", "image/png", "Images PNG"),
    JPG("jpg", "image/jpeg", "Images JPEG");

    private final String extension;
    private final String mimeType;
    private final String description;

    FileType(String extension, String mimeType, String description) {
        this.extension = extension;
        this.mimeType = mimeType;
        this.description = description;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Trouve le FileType correspondant à une extension donnée
     * 
     * @param extension L'extension du fichier (sans le point)
     * @return Le FileType correspondant ou null si non supporté
     */
    public static FileType fromExtension(String extension) {
        if (extension == null)
            return null;

        String normalizedExtension = extension.toLowerCase().trim();

        // Gérer le cas JPEG
        if ("jpeg".equals(normalizedExtension)) {
            normalizedExtension = "jpg";
        }

        for (FileType fileType : values()) {
            if (fileType.getExtension().equals(normalizedExtension)) {
                return fileType;
            }
        }
        return null;
    }

    /**
     * Trouve le FileType correspondant à un MIME type donné
     * 
     * @param mimeType Le MIME type du fichier
     * @return Le FileType correspondant ou null si non supporté
     */
    public static FileType fromMimeType(String mimeType) {
        if (mimeType == null)
            return null;

        for (FileType fileType : values()) {
            if (fileType.getMimeType().equals(mimeType)) {
                return fileType;
            }
        }

        // Gérer les variantes JPEG
        if ("image/jpg".equals(mimeType)) {
            return JPG;
        }

        return null;
    }

    /**
     * Vérifie si une extension est supportée
     * 
     * @param extension L'extension à vérifier
     * @return true si l'extension est supportée
     */
    public static boolean isSupported(String extension) {
        return fromExtension(extension) != null;
    }

    /**
     * Vérifie si un MIME type est supporté
     * 
     * @param mimeType Le MIME type à vérifier
     * @return true si le MIME type est supporté
     */
    public static boolean isSupportedMimeType(String mimeType) {
        return fromMimeType(mimeType) != null;
    }
}
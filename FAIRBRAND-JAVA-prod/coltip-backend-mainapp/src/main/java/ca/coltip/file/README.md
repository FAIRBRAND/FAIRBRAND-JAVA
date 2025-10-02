# 📁 FAIR-006 : Système d'Upload et de Récupération de Fichiers

## 📋 Vue d'ensemble

Le module **FAIR-006** implémente un système complet d'upload et de récupération de fichiers côté serveur avec gestion du stockage, renommage automatique sécurisé et récupération via URL publique.

### 🎯 Objectifs

- ✅ Upload sécurisé de fichiers avec renommage automatique
- ✅ Stockage organisé avec gestion des métadonnées
- ✅ Récupération d'URL publiques pour accès front-end
- ✅ Support multi-formats (documents, images, vidéos)
- ✅ Gestion des permissions et visibilité

## 🏗️ Architecture

```
ca.coltip.file/
├── controller/
│   └── FileUploadController.java     # REST API endpoints
├── service/
│   ├── FileUploadService.java        # Interface service
│   └── impl/
│       └── FileUploadServiceImpl.java # Implémentation métier
├── data/
│   ├── entities/
│   │   └── FileUpload.java           # Entité JPA
│   ├── dto/
│   │   └── FileUploadDTO.java        # DTO transfert
│   └── repository/
│       └── FileUploadRepository.java  # Repository JPA
├── enums/
│   └── FileType.java                 # Types de fichiers supportés
└── config/
    └── FileUploadConfig.java         # Configuration multipart
```

## 🚀 Fonctionnalités

### 📤 Upload de Fichiers
- **Renommage sécurisé** : Format `[timestamp]_[uuid].extension`
- **Validation** : Types, taille, intégrité
- **Métadonnées** : Stockage complet en base de données
- **Association utilisateur** : Traçabilité des uploads

### 🔗 Récupération d'URL
- **URL publiques** : Pattern `base_url + nouveau_nom_fichier`
- **Accès contrôlé** : Permissions et visibilité
- **Formats multiples** : Support documents, images, vidéos

### 🔍 Gestion Avancée
- **Recherche** : Par nom, type, utilisateur
- **Statistiques** : Usage par utilisateur
- **Soft Delete** : Suppression logique avec récupération
- **Nettoyage** : Maintenance automatisée

## 📊 Formats Supportés

| Catégorie  | Extensions        | Types MIME                    | Description           |
|------------|-------------------|-------------------------------|-----------------------|
| Documents  | PDF, PPTX         | application/pdf, etc.         | Fichiers bureautiques |
| Images     | PNG, JPG, JPEG    | image/png, image/jpeg         | Images web            |
| Vidéos     | MP4, MOV, AVI     | video/mp4, video/quicktime    | Contenus multimédia   |

## 🛠️ Configuration

### application.properties

```properties
# Configuration upload FAIR-006
app.upload.directory=/var/app/uploads
app.upload.base-url=http://localhost:8081/uploads
app.upload.max-file-size=52428800
app.upload.allowed-types=pdf,pptx,mp4,mov,avi,png,jpg,jpeg

# Configuration multipart Spring
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
spring.servlet.multipart.file-size-threshold=1MB

# Logs système upload
logging.level.ca.coltip.file=DEBUG
```

### Base de données

```sql
-- Table file_upload (V1__Initial_schema.sql)
CREATE TABLE public.file_upload (
    id SERIAL PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    generated_name VARCHAR(500) NOT NULL UNIQUE,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    mime_type VARCHAR(255),
    file_extension VARCHAR(10),
    access_url TEXT,
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    uploaded_by_user_id INTEGER,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'anonymous',
    record_status INTEGER DEFAULT 1,
    
    CONSTRAINT fk_file_upload_user 
        FOREIGN KEY (uploaded_by_user_id) 
        REFERENCES users(id_user)
);
```

## 📡 API REST

### 🔐 Authentification

Tous les endpoints nécessitent un token JWT via le header :
```
Authorization: Bearer <jwt_token>
```

### 📤 Upload de fichier

```http
POST /api/files/upload
Content-Type: multipart/form-data
Authorization: Bearer <token>

Form-data:
- file: [fichier à uploader]
- userId: [optionnel] ID utilisateur
- description: [optionnel] Description du fichier
- isPublic: [optionnel] Visibilité publique (défaut: false)

Response 201:
{
    "success": true,
    "message": "Fichier uploadé avec succès",
    "file": {
        "id": 1,
        "originalName": "document.pdf",
        "generatedName": "1759305679261_a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf",
        "fileSize": 245760,
        "fileSizeFormatted": "240.0 KB",
        "fileType": "PDF",
        "mimeType": "application/pdf",
        "fileExtension": "pdf",
        "accessUrl": "http://localhost:8081/uploads/1759305679261_a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf",
        "description": "Mon document important",
        "isPublic": false,
        "uploadedByUser": 5,
        "uploadedAt": "2025-10-01T10:30:00Z",
        "createdAt": "2025-10-01T10:30:00Z",
        "createdBy": "5",
        "isImage": false,
        "isVideo": false,
        "isDocument": true,
        "fileTypeDescription": "Documents PDF"
    }
}
```

### 🔗 Récupération URL publique

```http
GET /api/files/{generatedName}/url
Authorization: Bearer <token>

Response 200:
{
    "success": true,
    "generatedName": "1759305679261_a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf",
    "url": "http://localhost:8081/uploads/1759305679261_a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf"
}
```

### 📋 Listage des fichiers utilisateur

```http
GET /api/files/user/{userId}
Authorization: Bearer <token>

Response 200:
{
    "success": true,
    "files": [...],
    "count": 5
}
```

### 🔍 Recherche de fichiers

```http
GET /api/files/search?name=document
Authorization: Bearer <token>

Response 200:
{
    "success": true,
    "searchTerm": "document",
    "files": [...],
    "count": 3
}
```

### 📊 Statistiques utilisateur

```http
GET /api/files/user/{userId}/stats
Authorization: Bearer <token>

Response 200:
{
    "success": true,
    "userId": 5,
    "stats": {
        "fileCount": 10,
        "totalSize": 5242880,
        "totalSizeFormatted": "5.0 MB"
    }
}
```

### 🗑️ Suppression de fichier

```http
# Suppression logique (soft delete)
DELETE /api/files/{generatedName}?userId={userId}
Authorization: Bearer <token>

# Suppression physique définitive
DELETE /api/files/{generatedName}/permanent?userId={userId}
Authorization: Bearer <token>

Response 200:
{
    "success": true,
    "message": "Fichier supprimé avec succès"
}
```

### ✏️ Modification des métadonnées

```http
# Mise à jour description
PUT /api/files/{generatedName}/description
Authorization: Bearer <token>
Content-Type: application/x-www-form-urlencoded

description=Nouvelle description&userId=5

# Changement visibilité
PUT /api/files/{generatedName}/visibility
Authorization: Bearer <token>
Content-Type: application/x-www-form-urlencoded

isPublic=true&userId=5

Response 200:
{
    "success": true,
    "message": "Description mise à jour",
    "file": {...}
}
```

## 🧪 Tests avec Postman

### 1. Configuration Environment

Créer un environnement "FAIRBRAND-Upload" :
```json
{
    "baseUrl": "http://localhost:8081/coltip/api",
    "token": "",
    "lastUploadedFile": "",
    "lastUploadedUrl": ""
}
```

### 2. Collection de tests

#### A. Authentification
```http
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
    "email": "test@yopmail.com",
    "password": "testuser"
}

# Script Tests :
pm.environment.set("token", pm.response.json().token);
```

#### B. Upload fichier
```http
POST {{baseUrl}}/files/upload
Authorization: Bearer {{token}}
Content-Type: multipart/form-data

file: [sélectionner fichier]
userId: 5
description: Test upload Postman
```

#### C. Récupération URL
```http
GET {{baseUrl}}/files/{{lastUploadedFile}}/url
Authorization: Bearer {{token}}
```

#### D. Listage fichiers utilisateur
```http
GET {{baseUrl}}/files/user/5
Authorization: Bearer {{token}}
```

## 🚀 Déploiement

### 1. Avec Docker

```bash
# Démarrer l'application
make run-solution

# Vérifier les logs
docker logs coltip_backend

# Accéder à la base de données
docker exec -it coltip_db psql -U postgres -d coltip
```

### 2. Structure des répertoires

```
/var/app/uploads/           # Répertoire de stockage des fichiers
├── 1759305679261_a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf
├── 1759305679262_b2c3d4e5-f6g7-8901-bcde-f1234567891a.png
└── ...
```

### 3. Serveur de fichiers statiques (optionnel)

Pour servir les fichiers directement via URL, ajouter nginx :

```yaml
# docker-compose.yml
file_server:
  image: nginx:alpine
  ports:
    - "8081:80"
  volumes:
    - uploads_volume:/usr/share/nginx/html/uploads:ro
  configs:
    - source: nginx_config
      target: /etc/nginx/conf.d/default.conf

configs:
  nginx_config:
    content: |
      server {
          listen 80;
          location /uploads/ {
              root /usr/share/nginx/html;
              autoindex off;
          }
      }
```

## 🔧 Maintenance

### Nettoyage automatique

```http
POST /api/files/admin/cleanup?daysOld=30
Authorization: Bearer <admin_token>

Response:
{
    "success": true,
    "message": "Nettoyage terminé",
    "cleanedFilesCount": 15,
    "daysOld": 30
}
```

### Vérification intégrité

```sql
-- Fichiers orphelins (en base mais pas sur disque)
SELECT generated_name FROM file_upload 
WHERE record_status = 1 
AND generated_name NOT IN (
    SELECT filename FROM disk_files
);

-- Fichiers fantômes (sur disque mais pas en base)
SELECT filename FROM disk_files 
WHERE filename NOT IN (
    SELECT generated_name FROM file_upload 
    WHERE record_status = 1
);
```

## 🚨 Gestion d'erreurs

### Codes d'erreur

| Code | Description                    | Action recommandée              |
|------|--------------------------------|---------------------------------|
| 400  | Fichier invalide ou trop gros  | Vérifier format et taille      |
| 401  | Token manquant/invalide        | Réauthentification requise     |
| 403  | Permissions insuffisantes      | Vérifier droits utilisateur    |
| 404  | Fichier non trouvé             | Vérifier nom généré            |
| 413  | Fichier trop volumineux        | Réduire taille ou config       |
| 415  | Type de fichier non supporté   | Utiliser formats autorisés     |
| 500  | Erreur serveur                 | Vérifier logs et stockage      |

### Réponses d'erreur type

```json
{
    "success": false,
    "error": "Type de fichier non autorisé: .exe"
}
```

## 📝 Logs et Debugging

### Configuration logs

```properties
# Logs détaillés module upload
logging.level.ca.coltip.file=DEBUG
logging.level.ca.coltip.file.service=TRACE

# Logs requêtes SQL
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

### Exemples de logs

```
2025-10-01 10:30:00.123 INFO  --- Upload request - File: document.pdf, User: 5, Size: 245760 bytes
2025-10-01 10:30:00.456 DEBUG --- Generated filename: 1759305679261_a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf
2025-10-01 10:30:00.789 INFO  --- File uploaded successfully - Access URL: http://localhost:8081/uploads/...
```

## 🔒 Sécurité

### Mesures implémentées

- ✅ **Validation types MIME** : Vérification stricte
- ✅ **Noms aléatoires** : Impossible de deviner les fichiers
- ✅ **Taille limitée** : Protection contre DoS
- ✅ **Authentification JWT** : Accès contrôlé
- ✅ **Permissions utilisateur** : Isolation des données
- ✅ **Soft delete** : Récupération possible

### Recommandations

- Configurer antivirus sur le répertoire uploads
- Surveiller l'espace disque disponible
- Sauvegarder régulièrement les fichiers
- Auditer les accès via les logs

## 📈 Performance

### Optimisations

- **Index base de données** : Sur generated_name, uploaded_by_user_id
- **Cache metadata** : Redis pour les infos fréquemment consultées  
- **CDN** : Pour la distribution des fichiers statiques
- **Compression** : Images automatiquement optimisées

### Monitoring

```sql
-- Statistiques globales
SELECT 
    COUNT(*) as total_files,
    SUM(file_size) as total_size,
    AVG(file_size) as avg_size,
    MAX(uploaded_at) as last_upload
FROM file_upload 
WHERE record_status = 1;

-- Top utilisateurs
SELECT 
    uploaded_by_user_id,
    COUNT(*) as file_count,
    SUM(file_size) as total_size
FROM file_upload 
WHERE record_status = 1 
GROUP BY uploaded_by_user_id 
ORDER BY total_size DESC 
LIMIT 10;
```

## 🎯 Critères d'acceptation FAIR-006

- ✅ **Renommage** : Format `[timestamp]_[uuid].extension` respecté
- ✅ **Stockage** : Répertoire `/var/app/uploads` configuré
- ✅ **Base de données** : Mapping nom original/généré fonctionnel  
- ✅ **URL publique** : Pattern `base_url + nouveau_nom_fichier` implémenté
- ✅ **Formats** : PDF, PPTX, MP4, MOV, AVI, PNG, JPG supportés
- ✅ **Récupération** : API front-end pour obtenir URL complète

## 🤝 Contribution

### Structure des commits

```
feat(upload): Ajout support format DOCX
fix(upload): Correction validation taille fichier  
docs(upload): Mise à jour documentation API
test(upload): Tests d'intégration endpoints
```

### Tests unitaires

```bash
# Exécuter les tests
mvn test -Dtest=FileUploadServiceTest
mvn test -Dtest=FileUploadControllerTest

# Coverage
mvn jacoco:report
```

---

## 📞 Support

**Équipe de développement :** FAIRBRAND-JAVA  
**Ticket de référence :** FAIR-006  
**Documentation API :** Postman Collection disponible  
**Logs :** `docker logs coltip_backend | grep ca.coltip.file`

---

*Module développé dans le cadre du projet FAIRBRAND - Système de gestion de fichiers d'entreprise*
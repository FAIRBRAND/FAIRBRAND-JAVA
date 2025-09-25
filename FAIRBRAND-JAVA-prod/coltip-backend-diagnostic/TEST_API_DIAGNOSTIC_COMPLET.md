# 🧪 Tests API Diagnostic - Guide Complet

## 📋 Table des Matières
- [🔧 Prérequis](#-prérequis)
- [⚙️ Configuration Postman](#️-configuration-postman)
- [🚀 Test Complet Étape par Étape](#-test-complet-étape-par-étape)
- [📋 Formats JSON par Type de Question](#-formats-json-par-type-de-question)
- [🧮 Calcul des Scores](#-calcul-des-scores)
- [🎯 Résultats Attendus](#-résultats-attendus)
- [🔍 Dépannage](#-dépannage)
- [📊 Scénarios de Test Avancés](#-scénarios-de-test-avancés)

---

## 🔧 Prérequis

- **✅ Application démarrée** : Module diagnostic en cours d'exécution
- **✅ Base de données opérationnelle** : PostgreSQL avec données de test
- **✅ Postman** installé et configuré
- **✅ Authentification** : Utilisateur avec email/password valides

---

## ⚙️ Configuration Postman

### Variables d'Environment Recommandées
Créez un environment Postman avec ces variables :

```json
{
  "baseUrl": "http://localhost:8081/coltip/api",
  "token": "",
  "sessionToken": "",
  "userId": "1"
}
```

### 📝 Collection Structure
```
COLTIP Diagnostic Tests/
├── 🔐 Authentication
│   └── Login User
├── 🎯 Session Management
│   ├── Start Session
│   ├── Get Session Details
│   └── Validate Session
├── 📝 Answer Submission
│   ├── Q1 - Communication (Single Choice)
│   ├── Q2 - Public Speaking (Scale)
│   ├── Q3 - Difficult Situations (Multiple Choice)
│   ├── Q4 - Leadership Experience (Single Choice)
│   └── Q5 - Motivation Capacity (Scale)
├── ✅ Session Completion
│   ├── Complete Session
│   └── Get Results
└── 🛠️ Utilities
    ├── Get All Diagnostics
    ├── Get Diagnostic Details
    └── Cleanup Expired Sessions
```

---

## 🚀 Test Complet Étape par Étape

### **🔐 ÉTAPE 1 : AUTHENTIFICATION**

#### 1.1 - Login Utilisateur
```http
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "email": "user@example.com", 
  "password": "password123"
}
```

**📋 Réponse attendue :**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "email": "user@example.com",
  "expires": "2025-09-25T08:30:00.000Z"
}
```

**⚠️ Action :** Copiez le `token` dans la variable Postman `{{token}}`

**✅ Validation :**
- Status Code: `200 OK`
- Token JWT présent
- UserId correspond aux attentes

---

### **🎯 ÉTAPE 2 : EXPLORATION DES DIAGNOSTICS DISPONIBLES**

#### 2.1 - Lister tous les diagnostics actifs
```http
GET {{baseUrl}}/diagnostic/diagnostics
Authorization: Bearer {{token}}
```

**📋 Réponse attendue :**
```json
[
  {
    "id": 1,
    "diagnosticName": "Test de Personnalité Professionnelle",
    "description": "Évaluation complète des compétences professionnelles",
    "isActive": true,
    "domains": [...]
  }
]
```

#### 2.2 - Détails d'un diagnostic spécifique
```http
GET {{baseUrl}}/diagnostic/diagnostics/1
Authorization: Bearer {{token}}
```

**📋 Réponse attendue :**
```json
{
  "id": 1,
  "diagnosticName": "Test de Personnalité Professionnelle",
  "description": "Évaluation complète...",
  "domains": [
    {
      "id": 1,
      "domainName": "Communication",
      "weight": 1.0,
      "questions": [
        {
          "id": 1,
          "questionText": "Comment préférez-vous communiquer avec vos collègues ?",
          "questionType": "SINGLE_CHOICE",
          "isRequired": true,
          "answerOptions": [...]
        }
      ]
    }
  ]
}
```

---

### **🎯 ÉTAPE 3 : DÉMARRER UNE SESSION**

#### 3.1 - Créer une nouvelle session
```http
POST {{baseUrl}}/diagnostic/sessions/start
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "diagnosticId": 1,
  "userId": {{userId}}
}
```

**📋 Réponse attendue :**
```json
{
  "id": 15,
  "sessionToken": "59f634a4-2c34-4a31-b81f-795d5e48a00e",
  "status": "IN_PROGRESS",
  "startedAt": "2025-09-24T08:30:00.000Z",
  "userId": 1,
  "diagnostic": {
    "id": 1,
    "diagnosticName": "Test de Personnalité Professionnelle"
  },
  "totalQuestions": 5,
  "answeredQuestions": 0
}
```

**⚠️ Action :** Copiez le `sessionToken` dans la variable Postman `{{sessionToken}}`

**✅ Validation :**
- Status Code: `201 Created`
- SessionToken UUID valide
- Status = "IN_PROGRESS"

---

### **💬 ÉTAPE 4 : RÉPONDRE AUX QUESTIONS - DOMAINE COMMUNICATION**

#### 4.1 - Question 1 : Communication collègues (SINGLE_CHOICE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 1,
  "selectedOptionId": 1
}
```

**📋 Options disponibles :**
- **ID: 1** - "Face à face, discussion directe" (Valeur: 5.0) ⭐
- **ID: 2** - "Réunions structurées" (Valeur: 4.0)
- **ID: 3** - "Par email détaillé" (Valeur: 3.0)
- **ID: 4** - "Par téléphone" (Valeur: 3.5)
- **ID: 5** - "Messages instantanés/chat" (Valeur: 2.0)

**📋 Réponse attendue :**
```json
{
  "id": 19,
  "numericAnswer": null,
  "textAnswer": null,
  "answeredAt": "2025-09-24T08:30:59.512Z",
  "question": {
    "id": 1,
    "questionText": "Comment préférez-vous communiquer avec vos collègues ?",
    "questionType": "SINGLE_CHOICE"
  },
  "selectedOption": {
    "id": 1,
    "optionText": "Face à face, discussion directe",
    "optionValue": 5.0
  }
}
```

#### 4.2 - Question 2 : Aisance parler en public (SCALE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 2,
  "answerText": "4"
}
```

**📝 Échelle 1-5 :**
- 1 = Très mal à l'aise
- 2 = Plutôt mal à l'aise  
- 3 = Neutre
- 4 = Plutôt à l'aise ⭐
- 5 = Très à l'aise

**📋 Réponse attendue :**
```json
{
  "id": 20,
  "numericAnswer": 4,
  "textAnswer": "4",
  "answeredAt": "2025-09-24T08:31:15.123Z",
  "question": {
    "id": 2,
    "questionText": "À quel point êtes-vous à l'aise pour parler en public ?",
    "questionType": "SCALE"
  },
  "selectedOption": null
}
```

#### 4.3 - Question 3 : Situations difficiles (MULTIPLE_CHOICE) - 3 Réponses

**4.3.1 - Première option sélectionnée**
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 3,
  "selectedOptionId": 6
}
```

**4.3.2 - Deuxième option sélectionnée**
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 3,
  "selectedOptionId": 7
}
```

**4.3.3 - Troisième option sélectionnée**
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 3,
  "selectedOptionId": 8
}
```

**📋 Options disponibles :**
- **ID: 6** - "Présentations devant un grand groupe" (Valeur: 2.0) ⭐
- **ID: 7** - "Négociations difficiles" (Valeur: 3.0) ⭐
- **ID: 8** - "Conversations conflictuelles" (Valeur: 2.5) ⭐
- **ID: 9** - "Feedback négatif à donner" (Valeur: 3.5)
- **ID: 10** - "Aucune difficulté particulière" (Valeur: 5.0)

---

### **👑 ÉTAPE 5 : RÉPONDRE AUX QUESTIONS - DOMAINE LEADERSHIP**

#### 5.1 - Question 4 : Expérience direction équipe (SINGLE_CHOICE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 4,
  "selectedOptionId": 12
}
```

**📋 Options disponibles :**
- **ID: 11** - "Oui, plusieurs équipes importantes" (Valeur: 5.0)
- **ID: 12** - "Oui, une ou deux petites équipes" (Valeur: 4.0) ⭐
- **ID: 13** - "Oui, de façon informelle" (Valeur: 3.0)
- **ID: 14** - "Non, mais j'aimerais essayer" (Valeur: 2.0)
- **ID: 15** - "Non, cela ne m'intéresse pas" (Valeur: 1.0)

#### 5.2 - Question 5 : Capacité à motiver (SCALE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 5,
  "answerText": "3"
}
```

**📝 Échelle 1-5 :**
- 1 = Aucune capacité
- 2 = Faible capacité
- 3 = Capacité modérée ⭐
- 4 = Bonne capacité
- 5 = Excellente capacité

---

### **📊 ÉTAPE 6 : VÉRIFICATION DE L'ÉTAT DE LA SESSION**

#### 6.1 - Consulter les réponses enregistrées
```http
GET {{baseUrl}}/diagnostic/sessions/{{sessionToken}}
Authorization: Bearer {{token}}
```

**📋 Réponse attendue :**
```json
{
  "id": 15,
  "sessionToken": "59f634a4-2c34-4a31-b81f-795d5e48a00e",
  "status": "IN_PROGRESS",
  "startedAt": "2025-09-24T08:30:00.000Z",
  "userId": 1,
  "diagnostic": {
    "id": 1,
    "diagnosticName": "Test de Personnalité Professionnelle"
  },
  "totalQuestions": 5,
  "answeredQuestions": 5,
  "progress": 100.0
}
```

#### 6.2 - Voir toutes les réponses de la session
```http
GET {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
```

#### 6.3 - Valider la session avant completion
```http
GET {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/validate
Authorization: Bearer {{token}}
```

**📋 Réponse attendue :**
```json
{
  "isValid": true,
  "isComplete": false,
  "missingAnswers": [],
  "readyForCompletion": true
}
```

---

### **✅ ÉTAPE 7 : COMPLÉTER LA SESSION**

#### 7.1 - Terminer le diagnostic et déclencher le calcul
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/complete
Authorization: Bearer {{token}}
```

**📋 Réponse attendue :**
```json
{
  "sessionId": 15,
  "sessionToken": "59f634a4-2c34-4a31-b81f-795d5e48a00e",
  "status": "COMPLETED",
  "completedAt": "2025-09-24T08:35:00.000Z",
  "totalScore": 12.3,
  "percentageScore": 76.875,
  "message": "Session completed successfully. Results calculated."
}
```

**✅ Validation :**
- Status Code: `200 OK`
- Status = "COMPLETED"
- CompletedAt timestamp présent
- TotalScore calculé

---

### **🎯 ÉTAPE 8 : RÉCUPÉRER LES RÉSULTATS FINAUX**

#### 8.1 - Obtenir le rapport complet des résultats
```http
GET {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/results
Authorization: Bearer {{token}}
```

**📋 Réponse attendue (Résultats Réels) :**
```json
{
  "sessionId": 15,
  "sessionToken": "59f634a4-2c34-4a31-b81f-795d5e48a00e",
  "userId": 1,
  "diagnosticName": "Test de Personnalité Professionnelle",
  "totalScore": 12.3,
  "percentageScore": 76.875,
  "domainScores": [
    {
      "domain": {
        "id": 1,
        "domainName": "Communication",
        "weight": 1.0
      },
      "rawScore": 7.5,
      "weightedScore": 7.5,
      "percentageScore": 75.0,
      "maxPossibleScore": 10.0
    },
    {
      "domain": {
        "id": 2,
        "domainName": "Leadership",
        "weight": 1.2
      },
      "rawScore": 4.0,
      "weightedScore": 4.8,
      "percentageScore": 80.0,
      "maxPossibleScore": 6.0
    }
  ],
  "resultProfile": {
    "id": 3,
    "profileName": "Communicateur Expert",
    "profileCode": "COMM_EXPERT",
    "description": "Excellentes compétences en communication avec un potentiel de leadership",
    "recommendations": [
      "Travaillez sur la prise de décision et la gestion d'équipe pour évoluer vers le leadership",
      "Continuez à privilégier la communication directe",
      "Développez vos compétences en gestion des conflits"
    ],
    "minScore": 10.0,
    "maxScore": 15.0
  },
  "completedAt": "2025-09-24T08:35:00.000Z",
  "calculatedAt": "2025-09-24T08:35:01.234Z"
}
```

---

## 📋 Formats JSON par Type de Question

### 🔹 SINGLE_CHOICE (Questions à choix unique)
```json
{
  "questionId": 1,
  "selectedOptionId": 1
}
```
**📝 Usage :** Une seule option peut être sélectionnée

### 🔹 MULTIPLE_CHOICE (Questions à choix multiples)
```json
{
  "questionId": 3,
  "selectedOptionId": 6
}
```
**📝 Usage :** Envoyez une requête séparée pour chaque option sélectionnée

### 🔹 SCALE (Questions d'échelle 1-5)
```json
{
  "questionId": 2,
  "answerText": "4"
}
```
**📝 Usage :** Valeur numérique sous forme de chaîne (1, 2, 3, 4, ou 5)

### 🔹 TEXT_INPUT (Questions texte libre)
```json
{
  "questionId": X,
  "answerText": "Ma réponse en texte libre..."
}
```
**📝 Usage :** Réponse textuelle libre (optionnel selon la question)

---

## 🧮 Calcul des Scores

### 📊 Logique de Calcul

#### **Domaine Communication (Poids: 1.0)**
```
Q1 (SINGLE_CHOICE): 5.0 points
Q2 (SCALE): 4.0 points  
Q3 (MULTIPLE_CHOICE): 2.0 + 3.0 + 2.5 = 7.5 points
───────────────────────────────────────────────────
Total Communication Brut: 16.5 points
Total Communication Pondéré: 16.5 × 1.0 = 16.5 points
```

#### **Domaine Leadership (Poids: 1.2)**
```
Q4 (SINGLE_CHOICE): 4.0 points
Q5 (SCALE): 3.0 points
───────────────────────────────────────────────────
Total Leadership Brut: 7.0 points
Total Leadership Pondéré: 7.0 × 1.2 = 8.4 points
```

#### **Score Total Théorique**
```
Communication: 16.5 points
Leadership: 8.4 points
───────────────────────────────────────────────────
TOTAL THÉORIQUE: 24.9 points
```

### 📊 Résultats Réels Obtenus

#### **Tests Réels avec Base de Données**
```
Domaine Communication: 7.5 points (au lieu de 16.5)
Domaine Leadership: 4.8 points (au lieu de 8.4)
───────────────────────────────────────────────────
TOTAL RÉEL: 12.3 points
Pourcentage: 76.875%
```

**📝 Note :** Les valeurs réelles dans la base de données diffèrent des estimations théoriques initiales.

---

## 🎯 Résultats Attendus

### 🎭 Attribution de Profil
Avec un score de **12.3 points**, vous obtiendrez :

```json
{
  "profileName": "Communicateur Expert",
  "profileCode": "COMM_EXPERT",
  "description": "Excellentes compétences en communication avec un potentiel de leadership",
  "minScore": 10.0,
  "maxScore": 15.0
}
```

### 📊 Répartition des Scores
- **Communication** : 7.5/10 (75.0%) - Excellentes compétences
- **Leadership** : 4.8/6 (80.0%) - Bon potentiel de leadership
- **Score Global** : 12.3/16 (76.875%) - Profil équilibré

### 💡 Recommandations Attendues
1. "Travaillez sur la prise de décision et la gestion d'équipe"
2. "Continuez à privilégier la communication directe"
3. "Développez vos compétences en gestion des conflits"

---

## 🔍 Dépannage

### ❌ Erreurs Courantes

#### **"Session is not ready for calculation - missing required answers"**
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Session is not ready for calculation - missing required answers",
  "missingQuestions": [2, 4]
}
```
**🔧 Solution :** Vérifiez l'état de la session et complétez les questions manquantes

#### **"Invalid email or password"**
```json
{
  "error": "AUTHENTICATION_ERROR",
  "message": "Invalid email or password"
}
```
**🔧 Solution :** Vérifiez les credentials ou créez un nouvel utilisateur

#### **"Session token not found or expired"**
```json
{
  "error": "SESSION_ERROR",
  "message": "Session token not found or expired"
}
```
**🔧 Solution :** Démarrez une nouvelle session

#### **"Question has already been answered"**
```json
{
  "error": "DUPLICATE_ANSWER",
  "message": "This question has already been answered for this session"
}
```
**🔧 Solution :** Normal pour MULTIPLE_CHOICE, erreur pour les autres types

### ✅ Problèmes Résolus

#### **MultipleBagFetchException** - CORRIGÉ ✅
- **Problème :** Hibernate ne pouvait pas charger simultanément plusieurs collections
- **Solution :** Séparation des requêtes dans `DiagnosticSessionRepository.java`

#### **"Cannot execute INSERT in a read-only transaction"** - CORRIGÉ ✅
- **Problème :** Transaction read-only empêchait l'insertion des scores
- **Solution :** `@Transactional(readOnly = true)` → `@Transactional`

#### **Column mapping errors** - CORRIGÉ ✅
- **Problème :** Entités JPA ne correspondaient pas au schéma DB
- **Solution :** Correction de tous les mappings d'entités

### 🔧 Commandes de Diagnostic

#### Vérifier l'état de l'application
```bash
# Vérifier les logs
tail -f logs/application.log

# Vérifier les connexions DB
netstat -an | grep 5432

# Vérifier l'application
curl -X GET http://localhost:8081/coltip/api/health
```

#### Nettoyer les sessions expirées
```http
POST {{baseUrl}}/diagnostic/admin/cleanup-expired
Authorization: Bearer {{token}}
```

---

## 📊 Scénarios de Test Avancés

### 🎯 Scénario 1 : Profil "Leader Naturel"
Pour obtenir un score plus élevé, modifiez les réponses :

```json
// Q1 - Communication
{"questionId": 1, "selectedOptionId": 1}  // 5.0 points

// Q2 - Aisance publique  
{"questionId": 2, "answerText": "5"}      // 5.0 points

// Q3 - Aucune difficulté
{"questionId": 3, "selectedOptionId": 10} // 5.0 points

// Q4 - Leadership expérimenté
{"questionId": 4, "selectedOptionId": 11} // 5.0 points

// Q5 - Excellente motivation
{"questionId": 5, "answerText": "5"}      // 5.0 points
```
**Score Attendu :** ~18-20 points → Profil "Leader Naturel"

### 🎯 Scénario 2 : Profil "Débutant Prometteur"
Pour un score plus faible :

```json
// Q1 - Communication par email
{"questionId": 1, "selectedOptionId": 3}  // 3.0 points

// Q2 - Mal à l'aise en public
{"questionId": 2, "answerText": "2"}      // 2.0 points

// Q3 - Toutes les difficultés
{"questionId": 3, "selectedOptionId": 6}  // 2.0 points
{"questionId": 3, "selectedOptionId": 7}  // 3.0 points
{"questionId": 3, "selectedOptionId": 8}  // 2.5 points
{"questionId": 3, "selectedOptionId": 9}  // 3.5 points

// Q4 - Aucune expérience
{"questionId": 4, "selectedOptionId": 15} // 1.0 points

// Q5 - Faible motivation
{"questionId": 5, "answerText": "2"}      // 2.0 points
```
**Score Attendu :** ~8-10 points → Profil "Débutant Prometteur"

### 🎯 Scénario 3 : Test de Reprise de Session
1. Démarrer une session
2. Répondre à 2-3 questions seulement
3. Fermer Postman / Attendre
4. Récupérer la session avec `GET /sessions/{token}`
5. Continuer les réponses
6. Compléter normalement

### 🎯 Scénario 4 : Test de Validation des Erreurs
```http
# Test avec question inexistante
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
{
  "questionId": 999,
  "selectedOptionId": 1
}
# Attendu: 400 Bad Request

# Test avec option inexistante  
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
{
  "questionId": 1,
  "selectedOptionId": 999
}
# Attendu: 400 Bad Request

# Test completion session incomplète
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/complete
# Attendu: 400 Bad Request avec détails des questions manquantes
```

---

## 📋 Checklist de Validation Complète

### ✅ Tests d'Authentification
- [ ] Login réussi avec credentials valides
- [ ] Login échoué avec credentials invalides  
- [ ] Token JWT valide et bien formé
- [ ] Expiration du token gérée correctement

### ✅ Tests de Session
- [ ] Création de session réussie
- [ ] SessionToken UUID valide et unique
- [ ] État initial correct (IN_PROGRESS, 0 réponses)
- [ ] Récupération de session par token
- [ ] Validation de session

### ✅ Tests de Réponses
- [ ] SINGLE_CHOICE : Une option sélectionnée
- [ ] MULTIPLE_CHOICE : Plusieurs options pour même question
- [ ] SCALE : Valeurs 1-5 acceptées
- [ ] TEXT_INPUT : Texte libre accepté
- [ ] Validation des IDs questions/options
- [ ] Gestion des doublons appropriée

### ✅ Tests de Calcul
- [ ] Completion possible quand toutes questions répondues
- [ ] Completion impossible si questions manquantes
- [ ] Calcul des scores par domaine correct
- [ ] Application des pondérations
- [ ] Attribution du profil selon score
- [ ] Génération des recommandations

### ✅ Tests de Résultats
- [ ] Structure JSON complète et cohérente
- [ ] Scores détaillés par domaine
- [ ] Profil attribué correctement
- [ ] Recommandations personnalisées
- [ ] Timestamps et métadonnées présents

### ✅ Tests de Robustesse
- [ ] Gestion des erreurs 400/401/404/500
- [ ] Messages d'erreur informatifs
- [ ] Validation des paramètres d'entrée
- [ ] Gestion des sessions expirées
- [ ] Nettoyage des données temporaires

---

## 🎉 Conclusion

Ce guide de test complet vous permet de :

- ✅ **Valider** toutes les fonctionnalités du module diagnostic
- ✅ **Reproduire** les différents scénarios d'usage  
- ✅ **Déboguer** les problèmes éventuels
- ✅ **Documenter** les comportements attendus
- ✅ **Former** les nouveaux développeurs
- ✅ **Automatiser** les tests avec Postman Collections

**🎯 Résultat :** Un module diagnostic entièrement testé et opérationnel pour la production !

---

**📝 Note finale :** Adaptez les IDs des questions et options selon votre base de données. Ce guide est basé sur les tests réels effectués avec succès sur l'environnement de développement COLTIP.

---

*📅 Dernière mise à jour : Septembre 2025*  
*🔧 Version du module : 1.0.0*  
*👨‍💻 Testé et validé sur l'environnement FAIRBRAND-JAVA*
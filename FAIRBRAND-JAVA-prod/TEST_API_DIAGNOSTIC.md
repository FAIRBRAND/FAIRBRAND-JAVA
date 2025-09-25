# Test API Diagnostic - Guide Complet

## 📋 Table des Matières
- [Prérequis](#prérequis)
- [Configuration Postman](#configuration-postman)
- [Test Complet Étape par Étape](#test-complet-étape-par-étape)
- [Formats JSON par Type de Question](#formats-json-par-type-de-question)
- [Calcul des Scores](#calcul-des-scores)
- [Résultats Attendus](#résultats-attendus)
- [Dépannage](#dépannage)

## 🔧 Prérequis

- **Application démarrée** : `make run-solution`
- **Base de données opérationnelle** : PostgreSQL avec données de test
- **Postman** installé et configuré
- **Authentification** : Utilisateur avec email/password valides

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

**Réponse attendue :**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "email": "user@example.com"
}
```

**⚠️ Action :** Copiez le token dans la variable `{{token}}`

---

### **🎯 ÉTAPE 2 : DÉMARRER UNE SESSION**

#### 2.1 - Créer une nouvelle session
```http
POST {{baseUrl}}/diagnostic/sessions/start
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "diagnosticId": 1,
  "userId": {{userId}}
}
```

**Réponse attendue :**
```json
{
  "id": 1,
  "sessionToken": "59f634a4-2c34-4a31-b81f-795d5e48a00e",
  "status": "IN_PROGRESS",
  "startedAt": "2025-09-24T08:30:00.000Z",
  "userId": 1,
  "diagnostic": {...}
}
```

**⚠️ Action :** Copiez le `sessionToken` dans la variable `{{sessionToken}}`

---

### **💬 ÉTAPE 3 : RÉPONDRE AUX QUESTIONS - DOMAINE COMMUNICATION**

#### 3.1 - Question 1 : Communication collègues (SINGLE_CHOICE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 1,
  "selectedOptionId": 1
}
```
**✅ Option :** "Face à face, discussion directe" (ID: 1, Valeur: 5.0)

#### 3.2 - Question 2 : Aisance parler en public (SCALE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 2,
  "answerText": "4"
}
```
**✅ Réponse :** 4/5 - Plutôt à l'aise pour parler en public

#### 3.3 - Question 3 : Situations difficiles (MULTIPLE_CHOICE) - Option 1
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 3,
  "selectedOptionId": 6
}
```
**✅ Option :** "Présentations devant un grand groupe" (ID: 6, Valeur: 2.0)

#### 3.4 - Question 3 : Situations difficiles (MULTIPLE_CHOICE) - Option 2
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 3,
  "selectedOptionId": 7
}
```
**✅ Option :** "Négociations difficiles" (ID: 7, Valeur: 3.0)

#### 3.5 - Question 3 : Situations difficiles (MULTIPLE_CHOICE) - Option 3
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 3,
  "selectedOptionId": 8
}
```
**✅ Option :** "Conversations conflictuelles" (ID: 8, Valeur: 2.5)

---

### **👑 ÉTAPE 4 : RÉPONDRE AUX QUESTIONS - DOMAINE LEADERSHIP**

#### 4.1 - Question 4 : Expérience direction équipe (SINGLE_CHOICE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 4,
  "selectedOptionId": 12
}
```
**✅ Option :** "Oui, une ou deux petites équipes" (ID: 12, Valeur: 4.0)

#### 4.2 - Question 5 : Capacité à motiver (SCALE)
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/answers
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "questionId": 5,
  "answerText": "3"
}
```
**✅ Réponse :** 3/5 - Capacité modérée à motiver

---

### **📊 ÉTAPE 5 : VÉRIFIER L'ÉTAT DE LA SESSION**

#### 5.1 - Consulter les réponses enregistrées
```http
GET {{baseUrl}}/diagnostic/sessions/{{sessionToken}}
Authorization: Bearer {{token}}
```

**Réponse attendue :** Session avec toutes les réponses enregistrées

---

### **✅ ÉTAPE 6 : COMPLÉTER LA SESSION**

#### 6.1 - Terminer le diagnostic
```http
POST {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/complete
Authorization: Bearer {{token}}
```

**Réponse attendue :**
```json
{
  "sessionId": 1,
  "sessionToken": "59f634a4-2c34-4a31-b81f-795d5e48a00e",
  "status": "COMPLETED",
  "completedAt": "2025-09-24T08:35:00.000Z",
  "totalScore": 24.9,
  "message": "Session completed successfully"
}
```

---

### **🎯 ÉTAPE 7 : RÉCUPÉRER LES RÉSULTATS FINAUX**

#### 7.1 - Obtenir le rapport complet
```http
GET {{baseUrl}}/diagnostic/sessions/{{sessionToken}}/results
Authorization: Bearer {{token}}
```

**Réponse attendue :**
```json
{
  "sessionId": 1,
  "sessionToken": "59f634a4-2c34-4a31-b81f-795d5e48a00e",
  "userId": 1,
  "diagnosticName": "Test de Personnalité Professionnelle",
  "totalScore": 24.9,
  "percentageScore": 87.3,
  "domainScores": [
    {
      "domain": {
        "id": 1,
        "domainName": "Communication"
      },
      "rawScore": 16.5,
      "weightedScore": 16.5,
      "percentageScore": 82.5
    },
    {
      "domain": {
        "id": 2,
        "domainName": "Leadership"
      },
      "rawScore": 7.0,
      "weightedScore": 8.4,
      "percentageScore": 70.0
    }
  ],
  "resultProfile": {
    "profileName": "Leader Communicant",
    "profileCode": "LEAD_COMM",
    "description": "Profil équilibré avec d'excellentes compétences en communication et des capacités de leadership solides"
  },
  "recommendations": [
    "Continuez à privilégier la communication directe",
    "Développez davantage vos compétences en gestion d'équipe",
    "Travaillez sur la gestion des situations conflictuelles"
  ],
  "completedAt": "2025-09-24T08:35:00.000Z"
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

### 🔹 MULTIPLE_CHOICE (Questions à choix multiples)
```json
{
  "questionId": 3,
  "selectedOptionId": 6
}
```
**Note :** Envoyez une requête séparée pour chaque option sélectionnée

### 🔹 SCALE (Questions d'échelle 1-5)
```json
{
  "questionId": 2,
  "answerText": "4"
}
```

### 🔹 TEXT_INPUT (Questions texte libre)
```json
{
  "questionId": X,
  "answerText": "Ma réponse texte libre"
}
```

---

## 🧮 Calcul des Scores

### Domaine Communication (Poids: 1.0) - RÉSULTATS RÉELS
- **Questions répondues :** 7.5 points obtenus
- **Total Communication :** 7.5 points

### Domaine Leadership (Poids: 1.2) - RÉSULTATS RÉELS  
- **Questions répondues :** 4.0 points bruts
- **Total Leadership :** 4.0 × 1.2 = 4.8 points

**Note :** Les valeurs réelles dans votre base de données diffèrent des estimations initiales.

### Score Total
**Score Total Réel (avec 5 questions répondues):** 7.5 + 4.8 = **12.3 points**

---

## 🎊 Options Disponibles

### Question 3 - Situations difficiles (MULTIPLE_CHOICE)
- **ID: 6** - "Présentations devant un grand groupe" (Valeur: 2.0)
- **ID: 7** - "Négociations difficiles" (Valeur: 3.0)
- **ID: 8** - "Conversations conflictuelles" (Valeur: 2.5)
- **ID: 9** - "Feedback négatif à donner" (Valeur: 3.5)
- **ID: 10** - "Aucune difficulté particulière" (Valeur: 5.0)

### Question 1 - Communication collègues (SINGLE_CHOICE)
- **ID: 1** - "Face à face, discussion directe" (Valeur: 5.0)
- **ID: 2** - "Réunions structurées" (Valeur: 4.0)
- **ID: 3** - "Par email détaillé" (Valeur: 3.0)
- **ID: 4** - "Par téléphone" (Valeur: 3.5)
- **ID: 5** - "Messages instantanés/chat" (Valeur: 2.0)

### Question 4 - Expérience leadership (SINGLE_CHOICE)
- **ID: 11** - "Oui, plusieurs équipes importantes" (Valeur: 5.0)
- **ID: 12** - "Oui, une ou deux petites équipes" (Valeur: 4.0)
- **ID: 13** - "Oui, de façon informelle" (Valeur: 3.0)
- **ID: 14** - "Non, mais j'aimerais essayer" (Valeur: 2.0)
- **ID: 15** - "Non, cela ne m'intéresse pas" (Valeur: 1.0)

---

## ⚡ Séquence d'Exécution

1. **Login** → Token JWT
2. **Start Session** → SessionToken  
3. **Q1 Communication** → `selectedOptionId: 1`
4. **Q2 Aisance Publique** → `answerText: "4"`
5. **Q3 Difficultés Option 1** → `selectedOptionId: 6`
6. **Q3 Difficultés Option 2** → `selectedOptionId: 7`
7. **Q3 Difficultés Option 3** → `selectedOptionId: 8`
8. **Q4 Leadership** → `selectedOptionId: 12`
9. **Q5 Motivation** → `answerText: "3"`
10. **Complete Session** → Calcul scores
11. **Get Results** → Rapport final

---

## 🔍 Dépannage

### Erreurs Courantes

#### "Session is not ready for calculation - missing required answers"
- **Cause :** Toutes les questions obligatoires n'ont pas été répondues
- **Solution :** Vérifiez l'état de la session avec `GET /sessions/{token}` et complétez les réponses manquantes

#### "Invalid email or password"
- **Cause :** Informations d'authentification incorrectes
- **Solution :** Vérifiez les credentials dans la base de données

#### "Column does not exist"
- **Cause :** Problème de mapping entre entités JPA et schéma base de données
- **Solution :** Vérifiez que les entités correspondent exactement au schéma DB

### ✅ Problèmes Résolus

#### "MultipleBagFetchException" - CORRIGÉ
- **Problème :** Hibernate ne pouvait pas charger simultanément plusieurs collections
- **Solution :** Séparation des requêtes dans DiagnosticSessionRepository.java

#### "Cannot execute INSERT in a read-only transaction" - CORRIGÉ  
- **Problème :** Transaction read-only empêchait l'insertion des scores
- **Solution :** Annotation @Transactional(readOnly = true) → @Transactional

### Validation des Réponses

Chaque réponse devrait retourner une structure similaire à :
```json
{
    "id": 19,
    "answerValue": 5.0,
    "answerText": null,
    "question": {
        "id": 1,
        "questionText": "Comment préférez-vous communiquer avec vos collègues ?",
        "questionType": "SINGLE_CHOICE"
    },
    "selectedOption": {
        "id": 1,
        "optionText": "Face à face, discussion directe",
        "optionValue": 5.0
    },
    "createdAt": "2025-09-24T08:30:59.512614825"
}
```

---

## 🎯 Résultat Final Obtenu

Ce test vous donnera un **profil Communicateur Expert** avec un score total de **12.3 points** (76.875%), démontrant :
- **Communication :** 7.5 points (75.0%) - Excellentes compétences de communication
- **Leadership :** 4.8 points pondérés (80.0%) - Bon potentiel de leadership
- **Profil :** COMM_EXPERT - "Excellentes compétences en communication avec un potentiel de leadership"
- **Recommandations :** "Travaillez sur la prise de décision et la gestion d'équipe pour évoluer vers le leadership"

---

**📝 Note :** Adaptez les IDs des options selon vos préférences pour obtenir différents profils de personnalité !
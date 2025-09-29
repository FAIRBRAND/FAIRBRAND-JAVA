INSERT INTO public."admin"
("password", username)
VALUES('13d249f2cb4127b40cfa757866850278793f814ded3c587fe5889e889a7a9f6c', 'adminTest');

INSERT INTO public."language"
(language_code, language_name)
VALUES( 'en', 'English');

INSERT INTO public.country
(id_admin, id_language, origin_id_country, date_created, country_name)
VALUES(1, 1, null, now(), 'Madagascar');

INSERT INTO public."domain"
(id_admin, id_language, origin_id_domain, record_status, date_created, domain_name)
VALUES(1,1, null, 1, now(), 'Information and Technology');

INSERT INTO public.users
(id_admin, id_country, id_domain, id_language, origin_user_id_user, record_status, date_created, phone_number, "password", first_name, surname, cv_content, description, email)
VALUES(1, 1, 1, 1, null, 1, now(), '+261380000000', 'f2163f286db195aea4fc806b55c566b15e57c8b679249194e3a08af9f1a90c26', 'User', 'Test', 'url_content', 'description', 'test@yopmail.com');

INSERT INTO public.ability
(record_status, ability_name)
VALUES(1, 'can_view_course');

INSERT INTO public."groups"
(record_status, group_name)
VALUES(1, 'Candidate');

INSERT INTO public.sub_group
(id_group, record_status, sub_group_name)
VALUES(1, 1, 'Single Professional');

INSERT INTO public.group_ability
(ability_id, group_id)
VALUES(1, 1);

INSERT INTO public.user_sub_group
(id_user, id_sub_group)
VALUES(1, 1);

-- ===============================================
-- DONNÉES COMPLÈTES POUR LE MODULE DIAGNOSTIC
-- ===============================================

-- 1. Insertion du diagnostic principal
INSERT INTO public.diagnostic
(diagnostic_name, description, is_active)
VALUES('Test de Personnalité Professionnelle', 'Évaluation complète des compétences professionnelles en communication et leadership', true);

-- 2. Insertion des domaines de compétences
INSERT INTO public.diagnostic_domain
(id_diagnostic, domain_name, weight, display_order)
VALUES
(1, 'Communication', 1.0, 1),
(1, 'Leadership', 1.2, 2);

-- 3. Insertion des questions par domaine

-- Questions du domaine Communication
INSERT INTO public.diagnostic_question
(id_domain, question_text, question_type, is_required, display_order)
VALUES
(1, 'Comment préférez-vous communiquer avec vos collègues ?', 'SINGLE_CHOICE', true, 1),
(1, 'À quel point êtes-vous à l''aise pour parler en public ?', 'SCALE', true, 2),
(1, 'Dans quelles situations de communication ressentez-vous des difficultés ? (Plusieurs réponses possibles)', 'MULTIPLE_CHOICE', true, 3);

-- Questions du domaine Leadership
INSERT INTO public.diagnostic_question
(id_domain, question_text, question_type, is_required, display_order)
VALUES
(2, 'Avez-vous déjà dirigé une équipe ?', 'SINGLE_CHOICE', true, 4),
(2, 'Sur une échelle de 1 à 5, comment évaluez-vous votre capacité à motiver les autres ?', 'SCALE', true, 5);

-- 4. Options de réponse pour les questions à choix

-- Options pour Q1 (Communication collègues)
INSERT INTO public.diagnostic_answer_option
(id_question, option_text, option_value, display_order)
VALUES
(1, 'Face à face, discussion directe', 5.0, 1),
(1, 'Réunions structurées', 4.0, 2),
(1, 'Par email détaillé', 3.0, 3),
(1, 'Par téléphone', 3.5, 4),
(1, 'Messages instantanés/chat', 2.0, 5);

-- Options pour Q3 (Situations difficiles - MULTIPLE_CHOICE)
INSERT INTO public.diagnostic_answer_option
(id_question, option_text, option_value, display_order)
VALUES
(3, 'Présentations devant un grand groupe', 2.0, 1),
(3, 'Négociations difficiles', 3.0, 2),
(3, 'Conversations conflictuelles', 2.5, 3),
(3, 'Feedback négatif à donner', 3.5, 4),
(3, 'Aucune difficulté particulière', 5.0, 5);

-- Options pour Q4 (Expérience leadership)
INSERT INTO public.diagnostic_answer_option
(id_question, option_text, option_value, display_order)
VALUES
(4, 'Oui, plusieurs équipes importantes', 5.0, 1),
(4, 'Oui, une ou deux petites équipes', 4.0, 2),
(4, 'Oui, de façon informelle', 3.0, 3),
(4, 'Non, mais j''aimerais essayer', 2.0, 4),
(4, 'Non, cela ne m''intéresse pas', 1.0, 5);

-- 5. Profils de résultats avec fourchettes de scores
INSERT INTO public.diagnostic_result_profile
(profile_name, profile_code, description, recommendations, min_score, max_score)
VALUES
('Débutant Prometteur', 'BEGINNER_PROMISING', 
 'Vous montrez un potentiel intéressant avec des bases solides à développer.', 
 'Concentrez-vous sur le développement de vos compétences en communication. Cherchez des opportunités de prise de parole en public. Observez les leaders autour de vous pour apprendre.',
 0.0, 8.0),

('Professionnel en Développement', 'DEVELOPING_PROFESSIONAL',
 'Vous avez des compétences correctes mais avec une marge de progression importante.',
 'Travaillez sur votre confiance en communication. Participez à des formations en leadership. Demandez des retours réguliers sur vos interactions professionnelles.',
 8.1, 12.0),

('Communicateur Expert', 'COMM_EXPERT',
 'Excellentes compétences en communication avec un potentiel de leadership.',
 'Travaillez sur la prise de décision et la gestion d''équipe pour évoluer vers le leadership. Continuez à privilégier la communication directe.',
 12.1, 16.0),

('Leader Naturel', 'NATURAL_LEADER',
 'Profil équilibré avec d''excellentes compétences en communication et des capacités de leadership solides.',
 'Développez vos compétences en gestion stratégique. Mentoring d''autres professionnels. Prenez des rôles de leadership plus importants.',
 16.1, 20.0),

('Manager Complet', 'COMPLETE_MANAGER',
 'Profil de manager accompli avec toutes les compétences nécessaires au leadership d''équipe.',
 'Concentrez-vous sur le leadership transformationnel. Développez votre vision stratégique. Formez la prochaine génération de leaders.',
 20.1, 25.0);

-- 6. Données de test : Session exemple
INSERT INTO public.diagnostic_session
(session_uuid, status, started_at, id_user, id_diagnostic)
VALUES
('test-session-uuid-12345', 'COMPLETED', now() - interval '1 day', 1, 1);

-- 7. Réponses exemple pour la session de test
INSERT INTO public.diagnostic_user_answer
(id_session, id_question, id_option, numeric_answer, text_answer, answered_at)
VALUES
-- Q1: Communication face à face (SINGLE_CHOICE)
(1, 1, 1, null, null, now() - interval '1 day' + interval '5 minutes'),

-- Q2: Aisance en public = 4/5 (SCALE)
(1, 2, null, 4, '4', now() - interval '1 day' + interval '10 minutes'),

-- Q3: Difficultés multiples (MULTIPLE_CHOICE)
(1, 3, 6, null, null, now() - interval '1 day' + interval '15 minutes'), -- Présentations
(1, 3, 7, null, null, now() - interval '1 day' + interval '16 minutes'), -- Négociations
(1, 3, 8, null, null, now() - interval '1 day' + interval '17 minutes'), -- Conflits

-- Q4: Leadership petites équipes (SINGLE_CHOICE)  
(1, 4, 12, null, null, now() - interval '1 day' + interval '20 minutes'),

-- Q5: Capacité motivation = 3/5 (SCALE)
(1, 5, null, 3, '3', now() - interval '1 day' + interval '25 minutes');

-- 8. Scores calculés pour la session exemple
INSERT INTO public.diagnostic_domain_score
(id_session, id_domain, raw_score, weighted_score, percentage)
VALUES
-- Communication: 7.5 points (Q1:5.0 + Q2:4.0 + Q3:7.5 calculé)
(1, 1, 7.5, 7.5, 75.0),

-- Leadership: 4.0 points bruts, 4.8 pondérés (4.0 * 1.2)
(1, 2, 4.0, 4.8, 80.0);

-- 9. Mise à jour de la session avec les résultats finaux
UPDATE public.diagnostic_session 
SET 
    completed_at = now() - interval '1 day' + interval '30 minutes',
    total_score = 12.3,
    result_profile = 'Communicateur Expert'
WHERE id_session = 1;
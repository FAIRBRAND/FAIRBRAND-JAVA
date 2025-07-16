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
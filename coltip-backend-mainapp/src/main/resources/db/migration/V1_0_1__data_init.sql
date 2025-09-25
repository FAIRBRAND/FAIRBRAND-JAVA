/* Languages */
INSERT INTO "language" (name, code)
VALUES
    ('English', 'en'),
    ('Français', 'fr');

/* Set admin and user data basement */

INSERT INTO "groups" (name, record_status)
VALUES
    ('Administrator', 0),
    ('User', 0);

INSERT INTO "ability" (name, record_status)
VALUES
    ('ADMIN', 0),
    ('USER', 0);

INSERT INTO "sub_group" (name, record_status, id_group)
VALUES
    (
       'Development',
       0,
       (SELECT id_group FROM "groups" gp WHERE gp.name = 'Administrator')
    ),
    (
     'Simple',
     0,
       (SELECT id_group FROM "groups" gp WHERE gp.name = 'User')
    );

INSERT INTO "sub_group_ability" (ability_id, sub_group_id)
VALUES
    (
       (SELECT id_ability FROM "ability" a WHERE a.name = 'ADMIN'),
       (SELECT id_sub_group FROM "sub_group" sb WHERE sb.name = 'Development')
    ),
    (
        (SELECT id_ability FROM "ability" a WHERE a.name = 'USER'),
        (SELECT id_sub_group FROM "sub_group" sb WHERE sb.name = 'Simple')
    );
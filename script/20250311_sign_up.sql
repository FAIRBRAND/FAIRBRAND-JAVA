-- run this script for testing the sign up feature

INSERT INTO groups (group_name, record_status)
VALUES ('user', 1);

INSERT INTO sub_group (record_status, sub_group_name, id_group)
VALUES (1, 'simple_user', 1);

INSERT INTO ability (record_status, ability_name)
VALUES (1, 'admin');

INSERT INTO subgroup_ability (subgroup_id, ability_id)
VALUES (1, 1);
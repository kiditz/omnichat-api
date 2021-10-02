INSERT INTO user_authority VALUES (1, 'ADMIN');
INSERT INTO user_authority VALUES (2, 'STAFF');
INSERT INTO user_authority VALUES (3, 'SUPER_ADMIN');
INSERT INTO user_authority VALUES (4, 'SUPERVISOR');
SELECT pg_catalog.setval('public.user_authority_id_seq', 4, true);
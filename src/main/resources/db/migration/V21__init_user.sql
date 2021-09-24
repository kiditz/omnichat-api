INSERT INTO  user_principal VALUES (1, 'super.admin@stafsus.com', '$2a$10$HaehLfqzC8vBhT7sfNmKg.2EPuhFcQsQZqH0tu5z81i4Mw2GXCLwa', 'ACTIVE', NULL, '', FALSE, '2021-09-11 03:13:14.801037', NULL, NULL, NULL, 0);
INSERT INTO  user_principal VALUES (2, 'j@gmail.com',  '$2a$10$HaehLfqzC8vBhT7sfNmKg.2EPuhFcQsQZqH0tu5z81i4Mw2GXCLwa', 'ACTIVE', 1, '', FALSE,'2021-09-11 22:26:44.000386', NULL, '2021-09-11 22:26:44.000386', NULL, 0);
INSERT INTO user_authority VALUES (1, 'ADMIN');
INSERT INTO user_authority VALUES (2, 'STAFF');
INSERT INTO user_authority VALUES (3, 'SUPER_ADMIN');
INSERT INTO user_authority VALUES (4, 'SUPERVISOR');
SELECT pg_catalog.setval('public.user_principal_id_seq', 2, true);
SELECT pg_catalog.setval('public.user_authority_id_seq', 4, true);
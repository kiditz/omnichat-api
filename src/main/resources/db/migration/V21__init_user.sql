INSERT INTO  user_principal VALUES (1, 'super.admin@stafsus.com', 'stafsus', '$2a$10$HaehLfqzC8vBhT7sfNmKg.2EPuhFcQsQZqH0tu5z81i4Mw2GXCLwa', 'ACTIVE', NULL, NULL, '2021-09-11 03:13:14.801037', NULL, NULL, NULL, 0);
INSERT INTO  user_principal VALUES (2, 'raka@gmail.com', 'Raka Shoes Store', '$2a$10$9qav8NrdEw93.h7xoT9xx.Pe5Mo0TYNaWDvkNYUAw15Yuhc1dwr6W', 'ACTIVE', NULL, 1, '2021-09-11 22:26:44.000386', NULL, '2021-09-11 22:26:44.000386', NULL, 0);
INSERT INTO user_authority VALUES (1, 4);
INSERT INTO user_authority VALUES (2, 0);
SELECT pg_catalog.setval('public.user_principal_id_seq', 2, true);
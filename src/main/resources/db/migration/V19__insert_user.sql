-- Password is admin@1234
INSERT INTO user_principal(
	    email, business_name, password, status, parent_id, created_at, created_by, updated_at, updated_by, version)
VALUES ('super.admin@stafsus.com', 'stafsus', '$2a$10$HaehLfqzC8vBhT7sfNmKg.2EPuhFcQsQZqH0tu5z81i4Mw2GXCLwa', 'ACTIVE', null, CURRENT_TIMESTAMP, null, null, null, 0);
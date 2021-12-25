CREATE TABLE user_company
(
    user_principal_id bigint NOT NULL,
    company_id bigint NOT NULL,
    user_authority_id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL,
    CONSTRAINT user_company_pkey PRIMARY KEY (user_principal_id, company_id, user_authority_id)
);
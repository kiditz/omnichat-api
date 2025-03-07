CREATE TABLE api_key
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    server_key character varying(60) NOT NULL,
    app_id character varying(20) NOT NULL,
    company_id bigint,
    CONSTRAINT api_key_pkey PRIMARY KEY (id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);
CREATE INDEX idx_api_key_company_id ON api_key(company_id);
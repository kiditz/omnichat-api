CREATE TABLE contact
(
    id character varying(255) NOT NULL,
    server character varying(10),
    number character varying(255),
    name character varying(60),
    picture text,
    source character varying(30),
    is_group boolean,
    user_id bigint NOT NULL,
    CONSTRAINT contact_pkey PRIMARY KEY (id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);

CREATE INDEX idx_contact_user_id ON contact(user_id);
CREATE TABLE contact
(
    number character varying(60) NOT NULL,
    name character varying(60),
    picture text,
    type character varying(20),
    source character varying(30),
    is_wa_contact boolean,
    is_my_contact boolean,
    is_group boolean,
    user_id bigint NOT NULL,
    CONSTRAINT contact_pkey PRIMARY KEY (number),

    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);

CREATE INDEX idx_contact_user_id ON contact(user_id);
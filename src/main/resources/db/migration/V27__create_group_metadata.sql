CREATE TABLE group_metadata
(
    id character varying(255) NOT NULL,
    creation timestamp without time zone,
    server character varying(10),
    owner character varying(255),
    owner_id character varying(255),
    CONSTRAINT group_pkey PRIMARY KEY (id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);

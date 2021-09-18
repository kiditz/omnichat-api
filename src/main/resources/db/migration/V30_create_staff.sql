CREATE TABLE staff
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    phone character varying(20) NOT NULL,
    first_name character varying(60) NOT NULL,
    last_name character varying(100)  NOT NULL,
    user_id bigint NOT NULL,

    CONSTRAINT staff_pkey PRIMARY KEY (id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);
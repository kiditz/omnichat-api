CREATE TABLE price_description
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    field_value text NOT NULL,
    field text NOT NULL,
    field_type character varying(30) NOT NULL,
    price_id bigint NOT NULL,
    CONSTRAINT price_description_pkey PRIMARY KEY (id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);

CREATE INDEX idx_price_description_price_id ON price_description(price_id);

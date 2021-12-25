CREATE TABLE channel
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(100)  NOT NULL,
    image_url text  NOT NULL,
    device_id character varying(8)  NOT NULL,
    company_id bigint NOT NULL,
    product_id bigint NOT NULL,
    CONSTRAINT channel_pkey PRIMARY KEY (id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);


CREATE INDEX idx_channel_company_id ON channel(company_id);
CREATE INDEX idx_channel_product_id ON channel(product_id);
CREATE INDEX idx_channel_company_product_id ON channel(company_id, product_id);
CREATE TABLE "transaction"
(
    order_id character varying(255) NOT NULL,
    gross_amount decimal(9,2) NOT NULL,
    CONSTRAINT transaction_pkey PRIMARY KEY (order_id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);
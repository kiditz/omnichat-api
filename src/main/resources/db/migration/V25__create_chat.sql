CREATE TABLE chat
(
    id character varying(255),
    number character varying(255),
    name character varying(255),
    server character varying(10),
    archived boolean,
    is_group boolean,
    is_read_only boolean,
    pinned boolean,
    is_muted boolean,
    unread_count bigint,
    mute_expiration timestamp without time zone,
    "timestamp" timestamp without time zone,
    group_id character varying(255),
    source character varying(255),
    user_id bigint NOT NULL,
    CONSTRAINT chat_pkey PRIMARY KEY (id),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) ,
    updated_at timestamp without time zone,
    updated_by character varying(255) ,
    version bigint NOT NULL
);

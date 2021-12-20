CREATE TABLE staff_assignment
(
    staff_id bigint,
    channel_id bigint,
    PRIMARY KEY (channel_id, staff_id)
);
CREATE TABLE staff_assignment
(
    staff_id bigint,
    product_id bigint,
    PRIMARY KEY (product_id, staff_id)
);
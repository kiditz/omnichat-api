CREATE TABLE team_assignment
(
    team_id bigint,
    channel_id bigint,
    PRIMARY KEY (channel_id, team_id)
);
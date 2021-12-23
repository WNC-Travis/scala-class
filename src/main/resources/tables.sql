create table participant(
    participant_id serial,
    full_name text
);

create table positions(
    positions_id serial,
    participant_id int,
    account_name text,
    funds numeric(10,2)
)
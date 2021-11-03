create table participant2(
    participant_id serial,
    first_name text
);

create table positions2(
    positions_id serial,
    participant_id int,
    account_name text,
    funds numeric(10,2)
)
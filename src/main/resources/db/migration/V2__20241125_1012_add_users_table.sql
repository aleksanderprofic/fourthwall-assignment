create table fourthwall_assignment.users
(
    user_id  uuid primary key,
    username varchar unique not null,
    password varchar not null,
    role     varchar not null
);

create table fourthwall_assignment.movies
(
    movie_id uuid primary key,
    imdb_id  varchar not null,
    title    varchar not null
);

create table fourthwall_assignment.movie_shows
(
    movie_show_id uuid primary key,
    movie_id      uuid,
    starts_at     timestamp with time zone not null,
    price         decimal                  not null
);

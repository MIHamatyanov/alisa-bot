create table if not exists users
(
    id          serial primary key,
    telegram_id bigint not null,
    login       varchar(100),
    login_date  timestamp,
    token       varchar(255)
);
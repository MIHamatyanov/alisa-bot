create table if not exists invites
(
    id          serial primary key,
    owner_id    bigint not null references users(id) on delete cascade,
    code        varchar(64) not null unique,
    created_at  timestamp not null default current_timestamp,
    revoked_at  timestamp,
    used_by     bigint references users(id)
);

alter table users
    add column parent_id bigint references users(id);

-- Default table
create table if not exists entries
(
    id         bigserial    not null primary key unique,
    host       varchar(100) not null,
    rules      jsonb        not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    age        integer      not null
);

-- Index to find by host and GIN rules
create index if not exists idx_host on entries (host);
create index if not exists idx_gin_rules on entries using gin (rules)

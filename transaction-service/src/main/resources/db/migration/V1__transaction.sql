create table if not exists transactions (
    id uuid primary key,
    user_id bigint not null,
    category_id uuid not null,
    type varchar(16) not null,
    amount numeric(18,2) not null check (amount > 0),
    currency varchar(3) not null,
    occurred_at timestamptz not null,
    note varchar(512),
    is_deleted boolean not null default false
);

create index if not exists idx_tx_user_date on transactions(user_id, occurred_at);
create index if not exists idx_tx_user_category_date on transactions(user_id, category_id, occurred_at);

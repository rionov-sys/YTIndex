-- ==============================================================================
-- Inisialisasi Skema Database YT Index (Supabase Migration)
-- ==============================================================================

-- 1. Tabel Riwayat Pencarian (search_history)
create table if not exists public.search_history (
    id uuid primary key default gen_random_uuid(),
    query text not null,
    limit_count integer default 10,
    result_count integer default 0,
    created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- Index pencarian berdasarkan query dan tanggal
create index if not exists idx_search_history_created_at on public.search_history (created_at desc);
create index if not exists idx_search_history_query on public.search_history (query);

-- Aktifkan RLS
alter table public.search_history enable row level security;

-- Policy: Izinkan anonim dan authenticated membaca serta menambah riwayat
create policy "Allow read access for all"
    on public.search_history
    for select
    using (true);

create policy "Allow insert access for all"
    on public.search_history
    for insert
    with check (true);


-- 2. Tabel Bookmark Video (bookmarked_videos)
create table if not exists public.bookmarked_videos (
    id uuid primary key default gen_random_uuid(),
    video_id text not null,
    title text not null,
    channel_title text not null,
    published_date text not null,
    thumbnail_url text,
    video_url text not null,
    created_at timestamp with time zone default timezone('utc'::text, now()) not null,
    constraint unq_video_id unique (video_id)
);

-- Index bookmark berdasarkan video_id & tanggal
create index if not exists idx_bookmarked_videos_video_id on public.bookmarked_videos (video_id);
create index if not exists idx_bookmarked_videos_created_at on public.bookmarked_videos (created_at desc);

-- Aktifkan RLS
alter table public.bookmarked_videos enable row level security;

-- Policy: Izinkan read, insert, delete bookmark
create policy "Allow read access for bookmarks"
    on public.bookmarked_videos
    for select
    using (true);

create policy "Allow insert access for bookmarks"
    on public.bookmarked_videos
    for insert
    with check (true);

create policy "Allow delete access for bookmarks"
    on public.bookmarked_videos
    for delete
    using (true);

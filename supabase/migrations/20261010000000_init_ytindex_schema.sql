-- ==============================================================================
-- Migrasi Database Supabase: YT Index
-- Tabel: bookmarks, search_history, search_cache
-- RLS: Diaktifkan di seluruh tabel dengan policy berbasis auth.uid()
-- ==============================================================================

-- 1. Tabel Bookmarks (Menyimpan video favorit pengguna)
create table if not exists public.bookmarks (
    id uuid primary key default gen_random_uuid(),
    user_id uuid references auth.users(id) on delete cascade default auth.uid(),
    video_id text not null,
    title text,
    channel text,
    thumbnail_url text,
    published_at timestamptz,
    url text,
    created_at timestamptz default now(),
    unique(user_id, video_id)
);

-- Index untuk performa query bookmark per user diurutkan dari yang terbaru
create index if not exists idx_bookmarks_user_created_at 
    on public.bookmarks (user_id, created_at desc);

-- Aktifkan Row Level Security (RLS) pada tabel bookmarks
alter table public.bookmarks enable row level security;

-- Policy: Hanya user yang terautentikasi dapat membaca bookmark miliknya
create policy "Users can select own bookmarks"
    on public.bookmarks
    for select
    to authenticated
    using (user_id = auth.uid());

-- Policy: Hanya user yang terautentikasi dapat menambahkan bookmark untuk dirinya
create policy "Users can insert own bookmarks"
    on public.bookmarks
    for insert
    to authenticated
    with check (user_id = auth.uid());

-- Policy: Hanya user yang terautentikasi dapat menghapus bookmark miliknya
create policy "Users can delete own bookmarks"
    on public.bookmarks
    for delete
    to authenticated
    using (user_id = auth.uid());


-- 2. Tabel Search History (Menyimpan riwayat kata kunci pencarian)
create table if not exists public.search_history (
    id uuid primary key default gen_random_uuid(),
    user_id uuid references auth.users(id) on delete cascade default auth.uid(),
    query text not null,
    result_limit int,
    created_at timestamptz default now()
);

-- Index untuk performa riwayat pencarian per user diurutkan dari yang terbaru
create index if not exists idx_search_history_user_created_at 
    on public.search_history (user_id, created_at desc);

-- Aktifkan Row Level Security (RLS) pada tabel search_history
alter table public.search_history enable row level security;

-- Policy: Hanya user yang terautentikasi dapat membaca riwayat miliknya
create policy "Users can select own search history"
    on public.search_history
    for select
    to authenticated
    using (user_id = auth.uid());

-- Policy: Hanya user yang terautentikasi dapat menambah riwayat untuk dirinya
create policy "Users can insert own search history"
    on public.search_history
    for insert
    to authenticated
    with check (user_id = auth.uid());

-- Policy: Hanya user yang terautentikasi dapat menghapus riwayat miliknya
create policy "Users can delete own search history"
    on public.search_history
    for delete
    to authenticated
    using (user_id = auth.uid());


-- 3. Tabel Search Cache (Cache respons YouTube API bersama)
-- Cache key: query lowercase yang sudah di-trim + tanggal bucket (YYYY-MM-DD)
create table if not exists public.search_cache (
    cache_key text primary key,
    payload jsonb not null,
    created_at timestamptz default now()
);

-- Index pembersihan / pemantauan cache berdasarkan waktu pembuatan
create index if not exists idx_search_cache_created_at 
    on public.search_cache (created_at desc);

-- Aktifkan Row Level Security (RLS) pada search_cache
-- Penting: TIDAK ADA policy yang dibuat. 
-- Dengan RLS aktif tanpa policy, HANYA service_role (Edge Function) yang dapat membaca dan menulis.
-- Klien anon dan authenticated tidak memiliki akses langsung sama sekali.
alter table public.search_cache enable row level security;

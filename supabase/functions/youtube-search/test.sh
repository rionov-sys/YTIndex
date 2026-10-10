#!/usr/bin/env bash
# ==============================================================================
# Script Pengujian Supabase Edge Function: youtube-search
# ==============================================================================

# Ganti dengan URL dan Key proyek Anda
SUPABASE_URL="${SUPABASE_URL:-https://your-project.supabase.co}"
ANON_KEY="${SUPABASE_ANON_KEY:-your-anon-key}"
USER_EMAIL="${USER_EMAIL:-user@example.com}"
USER_PASSWORD="${USER_PASSWORD:-yourpassword}"

echo "=== 1. Login untuk mendapatkan JWT Token ==="
AUTH_RESPONSE=$(curl -s -X POST "${SUPABASE_URL}/auth/v1/token?grant_type=password" \
  -H "apikey: ${ANON_KEY}" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "'"${USER_EMAIL}"'",
    "password": "'"${USER_PASSWORD}"'"
  }')

ACCESS_TOKEN=$(echo "${AUTH_RESPONSE}" | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)

if [ -z "${ACCESS_TOKEN}" ]; then
  echo "Gagal mendapatkan token autentikasi. Respons auth:"
  echo "${AUTH_RESPONSE}"
  exit 1
fi

echo "Login berhasil. Token diperoleh."
echo ""

echo "=== 2. Memanggil Edge Function youtube-search ==="
curl -X POST "${SUPABASE_URL}/functions/v1/youtube-search" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "apikey: ${ANON_KEY}" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "belajar jetpack compose pemula",
    "limit": 5
  }'

echo ""
echo "=== Pengujian Selesai ==="

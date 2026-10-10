# ==============================================================================
# Script Pengujian Supabase Edge Function: youtube-search (PowerShell)
# ==============================================================================

$SupabaseUrl = if ($env:SUPABASE_URL) { $env:SUPABASE_URL } else { "https://your-project.supabase.co" }
$AnonKey = if ($env:SUPABASE_ANON_KEY) { $env:SUPABASE_ANON_KEY } else { "your-anon-key" }
$UserEmail = if ($env:USER_EMAIL) { $env:USER_EMAIL } else { "user@example.com" }
$UserPassword = if ($env:USER_PASSWORD) { $env:USER_PASSWORD } else { "yourpassword" }

Write-Host "=== 1. Login untuk mendapatkan JWT Token ===" -ForegroundColor Cyan
$LoginBody = @{
    email    = $UserEmail
    password = $UserPassword
} | ConvertTo-Json

try {
    $AuthResponse = Invoke-RestMethod -Uri "$SupabaseUrl/auth/v1/token?grant_type=password" `
        -Method Post `
        -Headers @{ "apikey" = $AnonKey; "Content-Type" = "application/json" } `
        -Body $LoginBody

    $AccessToken = $AuthResponse.access_token
    Write-Host "Login berhasil. User ID: $($AuthResponse.user.id)" -ForegroundColor Green
}
catch {
    Write-Host "Gagal login: $_" -ForegroundColor Red
    exit 1
}

Write-Host "`n=== 2. Memanggil Edge Function youtube-search ===" -ForegroundColor Cyan
$SearchBody = @{
    query = "belajar jetpack compose pemula"
    limit = 5
} | ConvertTo-Json

try {
    $FunctionResponse = Invoke-RestMethod -Uri "$SupabaseUrl/functions/v1/youtube-search" `
        -Method Post `
        -Headers @{
            "Authorization" = "Bearer $AccessToken"
            "apikey"        = $AnonKey
            "Content-Type"  = "application/json"
        } `
        -Body $SearchBody

    Write-Host "Respons Diterima (Cached: $($FunctionResponse.cached)):" -ForegroundColor Green
    $FunctionResponse.items | Format-Table -Property videoId, title, channel, publishedAt
}
catch {
    Write-Host "Gagal memanggil function: $_" -ForegroundColor Red
}

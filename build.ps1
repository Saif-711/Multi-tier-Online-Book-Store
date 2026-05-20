# Build JARs locally (fast), then Docker only copies them (seconds).
$ErrorActionPreference = "Stop"
$root = $PSScriptRoot

Write-Host "Building Catalog-Service..."
Set-Location "$root\Catalog-Service"
mvn -B -DskipTests package

Write-Host "Building Order-Service..."
Set-Location "$root\Order-Service"
mvn -B -DskipTests package

Write-Host "Building frontend-service..."
Set-Location "$root\frontend-service"
mvn -B -DskipTests package

Set-Location $root
Write-Host "Building Docker images..."
docker compose build

Write-Host "Done. Run: docker compose up"

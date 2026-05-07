#!/usr/bin/env bash
# ============================================================
# Elevate Luxe — Start Script
# Usage: ./start.sh [--build] [--down] [--logs]
# ============================================================
set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
GOLD='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${GOLD}"
echo "  ✦ Elevate Luxe — Starting Application"
echo "  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "${NC}"

# Check Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED}✗ Docker not found. Please install Docker Desktop.${NC}"
    exit 1
fi
if ! docker info &> /dev/null; then
    echo -e "${RED}✗ Docker daemon is not running. Please start Docker Desktop.${NC}"
    exit 1
fi

# Check docker compose
if ! docker compose version &> /dev/null 2>&1; then
    echo -e "${RED}✗ docker compose not found. Please update Docker Desktop.${NC}"
    exit 1
fi

# Handle flags
case "$1" in
  --down)
    echo -e "${GOLD}Stopping all services...${NC}"
    docker compose down
    echo -e "${GREEN}✓ Services stopped.${NC}"
    exit 0
    ;;
  --logs)
    docker compose logs -f app
    exit 0
    ;;
  --clean)
    echo -e "${GOLD}Stopping and removing all data...${NC}"
    docker compose down -v
    echo -e "${GREEN}✓ Cleaned up.${NC}"
    exit 0
    ;;
esac

BUILD_FLAG=""
if [ "$1" == "--build" ] || [ ! "$(docker images -q elevate-luxe-app 2> /dev/null)" ]; then
    BUILD_FLAG="--build"
fi

echo -e "${GOLD}Starting MySQL and Application...${NC}"
docker compose up -d $BUILD_FLAG

echo ""
echo -e "${GOLD}Waiting for application to be ready...${NC}"

# Wait up to 120s for the app to respond
MAX_WAIT=120
COUNTER=0
until curl -sf http://localhost:8080/ > /dev/null 2>&1; do
    COUNTER=$((COUNTER+2))
    if [ $COUNTER -ge $MAX_WAIT ]; then
        echo -e "${RED}✗ Application did not start in time. Check logs with: ./start.sh --logs${NC}"
        exit 1
    fi
    echo -n "."
    sleep 2
done

echo ""
echo -e "${GREEN}"
echo "  ✓ Elevate Luxe is running!"
echo "  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  🌐 Store:       http://localhost:8080"
echo "  👑 Admin:       http://localhost:8080/admin"
echo ""
echo "  Demo Credentials:"
echo "  Admin   → admin@elevateluxe.com / admin123"
echo "  User    → sophie@example.com / user123"
echo "  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "${NC}"

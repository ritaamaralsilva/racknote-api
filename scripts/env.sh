#!/usr/bin/env bash
set -euo pipefail

# Load .env (docker-style vars)
if [ -f .env ]; then
  set -a
  source .env
  set +a
fi

# Derive Spring datasource vars from docker vars
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:${POSTGRES_PORT}/${POSTGRES_DB}"
export SPRING_DATASOURCE_USERNAME="${POSTGRES_USER}"
export SPRING_DATASOURCE_PASSWORD="${POSTGRES_PASSWORD}"

exec "$@"

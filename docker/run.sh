#!/bin/bash
# Read Password
read -s -p "password: " -r PASSWORD
echo

# Run Command
docker run -d --rm --network host \
  -e POSTGRES_PASSWORD="$PASSWORD" \
  --name timescaledb timescale/timescaledb:latest-pg12
docker run --network host -d -p 3000:3000 --name grafanahost grafana/grafana:6.4.0
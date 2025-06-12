#!/bin/sh

# host:port, например: config-server:8888
HOSTPORT=$1
shift
CMD="$@"

HOST=$(echo "$HOSTPORT" | cut -d: -f1)
PORT=$(echo "$HOSTPORT" | cut -d: -f2)

echo "⏳ Waiting for $HOST:$PORT to become available..."

while ! nc -z "$HOST" "$PORT"; do
  sleep 1
done

echo "✅ $HOST:$PORT is up — executing: $CMD"
exec $CMD

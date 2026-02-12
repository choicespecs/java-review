#!/usr/bin/env bash
set -euo pipefail

# Usage: ./run.sh <projectName> [args...]
# Example: ./run.sh calculator 2 + 3

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECTS_DIR="$ROOT_DIR/projects"

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <projectName> [program args...]"
  echo "Example: $0 calculator 2 + 3"
  exit 1
fi

PROJECT_NAME="$1"
shift || true

PROJECT_DIR="$PROJECTS_DIR/$PROJECT_NAME"

if [[ ! -d "$PROJECT_DIR" ]]; then
  echo "Project folder not found: $PROJECT_DIR"
  echo "Available projects:"
  (cd "$PROJECTS_DIR" && ls -1) || true
  exit 1
fi

MAIN_JAVA="$PROJECT_DIR/Main.java"
if [[ ! -f "$MAIN_JAVA" ]]; then
  echo "Expected Main.java not found in: $PROJECT_DIR"
  echo "Either create Main.java, or use the auto-detect version."
  exit 1
fi

BIN_DIR="$PROJECT_DIR/bin"
mkdir -p "$BIN_DIR"

echo "Compiling: $PROJECT_NAME"
# Compile ALL .java files in the project folder (supports helper classes)
javac -d "$BIN_DIR" "$PROJECT_DIR"/*.java

echo "Running: $PROJECT_NAME (Main)"
java -cp "$BIN_DIR" Main "$@"


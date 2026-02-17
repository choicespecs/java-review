#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECTS_DIR="$ROOT_DIR/projects"

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <projectName> [program args...]"
  exit 1
fi

PROJECT_NAME="$1"
shift || true

PROJECT_DIR="$PROJECTS_DIR/$PROJECT_NAME"

if [[ ! -d "$PROJECT_DIR" ]]; then
  echo "Project folder not found: $PROJECT_DIR"
  exit 1
fi

SRC_DIR="$PROJECT_DIR/src"
SEARCH_DIR="$PROJECT_DIR"
if [[ -d "$SRC_DIR" ]]; then
  SEARCH_DIR="$SRC_DIR"
fi

BIN_DIR="$PROJECT_DIR/bin"
mkdir -p "$BIN_DIR"

JAVA_FILES=$(find "$SEARCH_DIR" -type f -name "*.java")

if [[ -z "$JAVA_FILES" ]]; then
  echo "No .java files found."
  exit 1
fi

echo "Compiling: $PROJECT_NAME"
javac -d "$BIN_DIR" $JAVA_FILES

# Auto-detect main class
MAIN_FILE=$(grep -rl "public static void main" "$SEARCH_DIR" | head -n 1 || true)

if [[ -z "$MAIN_FILE" ]]; then
  echo "No main method found."
  exit 1
fi

PKG=$(grep -E '^\s*package\s+[a-zA-Z0-9_.]+\s*;' "$MAIN_FILE" | \
      sed -E 's/^\s*package\s+([a-zA-Z0-9_.]+)\s*;\s*$/\1/')

CLS=$(basename "$MAIN_FILE" .java)

if [[ -n "$PKG" ]]; then
  MAIN_CLASS="$PKG.$CLS"
else
  MAIN_CLASS="$CLS"
fi

echo "Running: $PROJECT_NAME ($MAIN_CLASS)"
java -cp "$BIN_DIR" "$MAIN_CLASS" "$@"

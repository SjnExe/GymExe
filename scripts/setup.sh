#!/bin/bash
# scripts/setup.sh
# Sets up the development environment for GymExe.

set -e

echo "🚀 Setting up GymExe environment..."

# 1. Check Java Version
echo "☕ Checking Java version..."
if java -version 2>&1 | grep -q "build 25"; then
    echo "✅ Java 25 is installed."
else
    echo "❌ Java 25 is required but not found. Please install OpenJDK 25."
    exit 1
fi

# 2. Check Android SDK
echo "📱 Checking Android SDK..."
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ ANDROID_HOME is not set."
    exit 1
else
    echo "✅ ANDROID_HOME is set to $ANDROID_HOME"
fi

# 3. Install/Update Dependencies
echo "📦 Updating dependencies..."
./gradlew --refresh-dependencies

# 4. Setup Git Hooks (Optional)
if [ -d ".git" ]; then
    echo "🪝 Setting up git hooks..."
    # Create pre-commit hook if it doesn't exist
    HOOK_FILE=".git/hooks/pre-commit"
    if [ ! -f "$HOOK_FILE" ]; then
        echo "#!/bin/sh" > "$HOOK_FILE"
        echo "./gradlew spotlessCheck" >> "$HOOK_FILE"
        chmod +x "$HOOK_FILE"
        echo "✅ Pre-commit hook created (runs spotlessCheck)."
    else
        echo "ℹ️ Pre-commit hook already exists."
    fi
fi

echo "🎉 Setup complete! You are ready to build."

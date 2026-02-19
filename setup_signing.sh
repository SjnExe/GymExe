#!/bin/bash
mkdir -p GymExe/signature
echo "Generating Signing Configuration..."
read -s -p "Enter Keystore Password: " KEYSTORE_PASS
echo
read -p "Enter Key Alias (default: key0): " KEY_ALIAS
KEY_ALIAS=${KEY_ALIAS:-key0}
read -s -p "Enter Key Password (default: same as keystore): " KEY_PASS
echo
KEY_PASS=${KEY_PASS:-$KEYSTORE_PASS}

echo "Generating Keystore..."
keytool -genkeypair -v -keystore GymExe/signature/release.jks -alias "$KEY_ALIAS" -keyalg RSA -keysize 2048 -validity 10000 -storepass "$KEYSTORE_PASS" -keypass "$KEY_PASS" -dname "CN=GymExe, OU=GymExe, O=GymExe, L=Unknown, S=Unknown, C=Unknown"

echo "$KEYSTORE_PASS" > GymExe/signature/KEYSTORE_PASSWORD.txt
echo "$KEY_ALIAS" > GymExe/signature/KEY_ALIAS.txt
echo "$KEY_PASS" > GymExe/signature/KEY_PASSWORD.txt

# Base64 encode for GitHub Actions
if command -v base64 &> /dev/null; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
        base64 -i GymExe/signature/release.jks -o GymExe/signature/KEYSTORE_BASE64.txt
    else
        base64 -w 0 GymExe/signature/release.jks > GymExe/signature/KEYSTORE_BASE64.txt
    fi
    echo "Keystore Base64 encoded to GymExe/signature/KEYSTORE_BASE64.txt"
else
    echo "base64 command not found, please manually encode release.jks"
fi

echo "All secrets saved in GymExe/signature/"

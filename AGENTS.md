sudo apt update
sudo apt full-upgrade -y
sudo apt install -y openjdk-25-jdk
sudo update-java-alternatives --set java-1.25.0-openjdk-amd64
echo y | /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager "build-tools;36.0.0"
sudo apt autoremove -y
sudo apt clean
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
./gradlew --version
java -version

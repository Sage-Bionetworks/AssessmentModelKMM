#!/bin/sh

#  xcframework_build.sh
#  iosApp
# 

rm -rf ./SwiftPackage/Binaries/AssessmentModel.xcframework
rm -rf ./assessmentModel/build/bin/
./gradlew :assessmentModel:packForXCode -PXCODE_CONFIGURATION="RELEASE" -PXCODE_SDK_NAME="iphoneos"
mv ./assessmentModel/build/bin/ios ./assessmentModel/build/bin/iosArm64
./gradlew :assessmentModel:packForXCode -PXCODE_CONFIGURATION="RELEASE" -PXCODE_SDK_NAME="iphonesimulator"
mv ./assessmentModel/build/bin/ios ./assessmentModel/build/bin/iosX64
./gradlew :assessmentModel:packForXCode -PXCODE_CONFIGURATION="RELEASE" -PXCODE_SDK_NAME="macos"
mv ./assessmentModel/build/bin/ios ./assessmentModel/build/bin/macos
xcodebuild -create-xcframework -framework ./assessmentModel/build/bin/iosArm64/releaseFramework/AssessmentModel.framework -framework ./assessmentModel/build/bin/iosX64/releaseFramework/AssessmentModel.framework -framework ./assessmentModel/build/bin/macos/releaseFramework/AssessmentModel.framework -output ./SwiftPackage/Binaries/AssessmentModel.xcframework

cp -f ./androidApp/src/main/res/raw/*.json ./SwiftPackage/Tests/AssessmentModelTests/Resources/JSON/


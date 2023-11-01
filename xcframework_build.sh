#!/bin/sh

#  xcframework_build.sh
#  iosApp
# 

rm -rf ./SwiftPackage/Binaries/AssessmentModel.xcframework
rm -rf ./assessmentmodel/build/bin/
./gradlew :assessmentmodel:packForXCode -PXCODE_CONFIGURATION="RELEASE" -PXCODE_SDK_NAME="iphoneos"
mv ./assessmentmodel/build/bin/ios ./assessmentmodel/build/bin/iosArm64
./gradlew :assessmentmodel:packForXCode -PXCODE_CONFIGURATION="RELEASE" -PXCODE_SDK_NAME="iphonesimulator"
mv ./assessmentmodel/build/bin/ios ./assessmentmodel/build/bin/iosX64
./gradlew :assessmentmodel:packForXCode -PXCODE_CONFIGURATION="RELEASE" -PXCODE_SDK_NAME="macos"
mv ./assessmentmodel/build/bin/ios ./assessmentmodel/build/bin/macos
xcodebuild -create-xcframework -framework ./assessmentmodel/build/bin/iosArm64/releaseFramework/AssessmentModel.framework -framework ./assessmentmodel/build/bin/iosX64/releaseFramework/AssessmentModel.framework -framework ./assessmentmodel/build/bin/macos/releaseFramework/AssessmentModel.framework -output ./SwiftPackage/Binaries/AssessmentModel.xcframework

cp -f ./androidApp/src/main/res/raw/*.json ./SwiftPackage/Tests/AssessmentModelTests/Resources/JSON/


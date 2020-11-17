#!/bin/sh

#  xcframework_build.sh
#  iosApp
#
#  Created by Shannon Young on 7/16/20.
#  

rm -rf ./SwiftPackage/Binaries/AssessmentModel.xcframework
./gradlew -p ./assessmentModel copyFramework -Pkotlin.build.type="RELEASE" -Pdevice="true"
./gradlew -p ./assessmentModel copyFramework -Pkotlin.build.type="RELEASE" -Pdevice="false"
xcodebuild -create-xcframework -framework ./assessmentModel/build/bin/iosArm64/releaseFramework/AssessmentModel.framework -framework ./assessmentModel/build/bin/iosX64/releaseFramework/AssessmentModel.framework -output ./SwiftPackage/Binaries/AssessmentModel.xcframework


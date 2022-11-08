# Assessment Model

This repo contains Sage Bionetwork's cross-platform architecture for serializing 
and displaying assessments run on mobile devices and used in scientific research. 
This repo was originally designed to use Kotlin multiplatform for both the Android 
and iOS implementations, but has since been restructured to include parallel 
implementations for both platforms written in the native language for each platform.

## iOS

Currently iOS is the only officially supported Apple product. The code is written
in Swift and uses Swift Package Manager to manage dependencies.

Examples for using this package are included by opening `iOSWorkspace.xcworkspace`
in Xcode.

## Android

The application can be built and executed on a device or emulator using Android Studio 3.2 or higher.
One can also compile the application and run tests from the command line:

```
   > ./gradlew :androidApp:build
```

## License

AssessmentModelKMM is available under the [Commons Clause and BSD 3-clause](LICENSE) licenses.

// swift-tools-version:5.3
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "AssessmentModel",
    defaultLocalization: "en",
    platforms: [
        .iOS(.v14),
        .macOS(.v11),
    ],
    products: [
        // Products define the executables and libraries a package produces, and make them visible to other packages.
        .library(
            name: "AssessmentModel",
            targets: ["AssessmentModel"]),
        .library(
            name: "AssessmentModelUI",
            targets: ["AssessmentModelUI", "SharedMobileUI"]),
    ],
    dependencies: [
        .package(name: "JsonModel",
                 url: "https://github.com/Sage-Bionetworks/JsonModel-Swift.git",
                 "1.6.0"..<"3.0.0"),
        .package(name: "MobilePassiveData",
                 url: "https://github.com/Sage-Bionetworks/MobilePassiveData-SDK.git",
                 from: "1.4.0"),
    ],
    targets: [
        // Targets are the basic building blocks of a package. A target can define a module or a test suite.
        // Targets can depend on other targets in this package, and on products in packages this package depends on.
        
        .target(name: "AssessmentModel",
                dependencies: [
                    "JsonModel",
                    .product(name: "MobilePassiveData", package: "MobilePassiveData"),
                ],
                path: "SwiftPackage/Sources/AssessmentModel"),
        
        .testTarget(
            name: "AssessmentModelTests",
            dependencies: [
                "AssessmentModel",
            ],
            path: "SwiftPackage/Tests/AssessmentModelTests",
            resources: [
                .process("Resources")
            ]),
        
        .target(name: "AssessmentModelUI",
                dependencies: [
                    "AssessmentModel",
                    "JsonModel",
                    "SharedMobileUI",
                ],
                path: "SwiftPackage/Sources/AssessmentModelUI",
                resources: [
                    .process("Resources")
                ]),
        
        .testTarget(
            name: "AssessmentModelUITests",
            dependencies: [
                "AssessmentModel",
                "AssessmentModelUI",
            ],
            path: "SwiftPackage/Tests/AssessmentModelUITests"),
        
        .target(
            name: "SharedMobileUI",
            dependencies: [],
            path: "SwiftPackage/Sources/SharedMobileUI",
            resources: [
                .process("Resources")
            ]),

    ]
)

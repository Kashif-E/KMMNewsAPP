// swift-tools-version:5.3
import PackageDescription

let remoteKotlinUrl = "https://api.github.com/repos/Kashif-E/KMMNewsAPP/releases/assets/82558509.zip"
let remoteKotlinChecksum = "f85e2aefee95bf6911257b9a12dabce2ab84b054b6699f33dbcd41415ca1d8c9"
let packageName = "shared"

let package = Package(
    name: packageName,
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: packageName,
            targets: [packageName]
        ),
    ],
    targets: [
        .binaryTarget(
            name: packageName,
            url: remoteKotlinUrl,
            checksum: remoteKotlinChecksum
        )
        ,
    ]
)
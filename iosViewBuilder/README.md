# iOSViewBuilder

This app is used to create a preview version of an application that can be 
used for developing the views including in this package.

## Please Note

As of this writing, Xcode 13.2 SwiftUI views have a rather annoying bug where 
the views cannot reliably be rendered in preview mode. For this reason, the 
Swift Package used to define the code in this library is *not* linked to this 
application. Instead, the application adds the files to its project and links 
other packages directly. For this reason, this app should *not* be used by 
external developers looking for code to copy/paste in order to use these 
assessments within their own application.


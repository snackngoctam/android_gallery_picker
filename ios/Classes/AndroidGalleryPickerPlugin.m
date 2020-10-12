#import "AndroidGalleryPickerPlugin.h"
#if __has_include(<android_gallery_picker/android_gallery_picker-Swift.h>)
#import <android_gallery_picker/android_gallery_picker-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "android_gallery_picker-Swift.h"
#endif

@implementation AndroidGalleryPickerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAndroidGalleryPickerPlugin registerWithRegistrar:registrar];
}
@end

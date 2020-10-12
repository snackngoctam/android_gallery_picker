import Flutter
import UIKit

public class SwiftAndroidGalleryPickerPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter.io/gallery", binaryMessenger: registrar.messenger())
    let instance = SwiftAndroidGalleryPickerPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS Platform is not supported")
  }
}

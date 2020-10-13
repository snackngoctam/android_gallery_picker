import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class AndroidGalleryPicker {
  static Future<File> image({String colorAppBar, String titleAppBar}) async {
    if (Platform.isAndroid) {
      String event = await MethodChannel("flutter.io/gallery")
          .invokeMethod<String>('gallery', {
        "colorAppBar": colorAppBar ?? "#000000",
        "titleAppBar": titleAppBar ?? ""
      });
      if (event == null) return null;
      return File(event);
    } else {
      return null;
    }
  }
}

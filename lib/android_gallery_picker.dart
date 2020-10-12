import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class AndroidGalleryPicker {
  static Future<File> get image async {
    if (Platform.isAndroid) {
      String event = await MethodChannel("flutter.io/gallery")
          .invokeMethod<String>('gallery');
      if (event == null) return null;
      return File(event);
    } else {
      return null;
    }
  }
}

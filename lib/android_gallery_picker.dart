import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class AndroidGalleryPicker {
  static Future<File> image({String colorAppBar, String titleAppBar}) async {
    if (Platform.isAndroid) {
      String event = await MethodChannel("flutter.io/gallery")
          .invokeMethod<String>('gallery', {
        "multiPick": "false",
        "colorAppBar": colorAppBar ?? "#000000",
        "titleAppBar": titleAppBar ?? "",
        "limitMultiPick": "1"
      });
      if (event == null) return null;
      return File(event);
    } else {
      return null;
    }
  }

  static Future<List<File>> images(
      {String colorAppBar, String titleAppBar, int limitMultiPick}) async {
    if (Platform.isAndroid) {
      List<dynamic> event = await MethodChannel("flutter.io/gallery")
          .invokeMethod<List<dynamic>>('gallery', {
        "multiPick": "true",
        "colorAppBar": colorAppBar ?? "#000000",
        "titleAppBar": titleAppBar ?? "",
        "limitMultiPick": (limitMultiPick ?? 3).toString()
      });
      if (event == null) return null;
      List<File> _arr = [];
      if (event.length > 0) {
        event.forEach((element) {
          _arr.add(File(element));
        });
      }
      return Future.value(_arr);
    } else {
      return null;
    }
  }
}

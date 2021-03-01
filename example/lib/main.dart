import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:android_gallery_picker/android_gallery_picker.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  File image;
  @override
  void initState() {
    super.initState();
  }

  _getImage() async {
    List<File> images = await AndroidGalleryPicker.images();
    if (images != null && images.length > 0) {
      image = images.last;
    } else {
      image = null;
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(children: [
          Expanded(
              child: image == null
                  ? Container()
                  : Image.file(
                      image,
                      fit: BoxFit.scaleDown,
                    )),
          Container(
            height: 40.0,
            child: Center(
              child: MaterialButton(
                child: Text('Get Image'),
                onPressed: () {
                  _getImage();
                },
              ),
            ),
          )
        ]),
      ),
    );
  }
}

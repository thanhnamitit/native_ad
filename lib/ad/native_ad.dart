import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'native_ad_controller.dart';

class NativeAd extends StatelessWidget {
  final NativeAdController controller;
  final String type;
  final int position;

  NativeAd({
    @required this.controller,
    this.type = "",
    this.position = 0,
  });

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: "ndev.plugin.admob/native_ad",
      layoutDirection: TextDirection.ltr,
      creationParams: {
        "type": type,
        "position": position,
        "adUnit": controller.adUnit,
      },
      creationParamsCodec: const StandardMessageCodec(),
    );
  }
}

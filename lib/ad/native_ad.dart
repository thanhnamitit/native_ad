import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'native_ad_controller.dart';

class NativeAd extends StatelessWidget {
  final NativeAdController controller;
  final String type;
  final int position;
  final Color? titleColor;

  NativeAd({
    required this.controller,
    this.type = "",
    this.position = 0,
    this.titleColor,
  });

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      key: ValueKey(titleColor?.value),
      viewType: "ndev.plugin.admob/native_ad",
      layoutDirection: TextDirection.ltr,
      creationParams: {
        "type": type,
        "position": position,
        "adUnit": controller.adUnit,
        'titleColor': titleColor?.value,
        'accentColor': Theme.of(context).accentColor.value
      },
      creationParamsCodec: const StandardMessageCodec(),
    );
  }
}

import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class NativeAdController {
  final String adUnit;
  final int numOfAds;

  final _key = UniqueKey();

  String get id => _key.toString();
  MethodChannel _channel;
  final numOfAdsStream = StreamController<int>.broadcast();
  final _pluginChannel = const MethodChannel("ndev.plugin.admob");

  NativeAdController({this.adUnit, this.numOfAds = 1}) {
    _channel = MethodChannel(id);
    _channel.setMethodCallHandler(_handleMessages);
  }

  void load() {
    _pluginChannel.invokeMethod("loadNativeAd", {
      "adUnit": adUnit,
      "numOfAds": numOfAds,
      "key": id,
    });
  }

  Future<Null> _handleMessages(MethodCall call) async {
    switch (call.method) {
      case "numOfAdsChanged":
        numOfAdsStream.add(call.arguments["num"]);
        break;
    }
  }

  void dispose() {
    _pluginChannel.invokeMethod("dispose", {
      "adUnit": adUnit,
    });
  }
}

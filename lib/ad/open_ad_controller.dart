import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

enum OpenAppEvent { adLoaded, adFailedToLoad, adDismissed }

class OpenAppAdController {
  final String adUnit;

  final _key = UniqueKey();

  String get id => _key.toString();
  late MethodChannel _channel;
  final event = StreamController<OpenAppEvent>.broadcast();
  final _pluginChannel = const MethodChannel("ndev.plugin.admob");

  Completer _completer = Completer();

  Completer get completer => _completer;

  bool _readyToShow = false;

  bool get readyToShow => _readyToShow;

  OpenAppAdController({required this.adUnit}) {
    _channel = MethodChannel(id);
    _channel.setMethodCallHandler(_handleMessages);
  }

  void load() {
    _pluginChannel.invokeMethod("loadOpenAd", {
      "adUnit": adUnit,
      "key": id,
    });
  }

  void show() {
    _pluginChannel.invokeMethod("showOpenAd", {
      "adUnit": adUnit,
      "key": id,
    });
  }

  Future<Null> _handleMessages(MethodCall call) async {
    switch (call.method) {
      case "onAdFailedToLoad":
        _readyToShow = false;
        completer.complete();
        break;
      case "onAdLoaded":
        _readyToShow = true;
        completer.complete();
        break;
      case "onAdDismissed":
        event.add(OpenAppEvent.adDismissed);
        break;
    }
  }
}

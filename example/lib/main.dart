import 'package:flutter/material.dart';
import 'package:ndev_flutter_native_ad/ad/native_ad.dart';
import 'package:ndev_flutter_native_ad/ad/native_ad_controller.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final controller = NativeAdController(
    adUnit: "ca-app-pub-3940256099942544/2247696110",
    numOfAds: 5,
  );

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          child: StreamBuilder(
            builder: (BuildContext context, AsyncSnapshot<int> snapshot) {
              if (!snapshot.hasData) {
                return Text("loading");
              } else {
                return ListView.builder(
                  itemBuilder: (_, index) {
                    return SizedBox(
                      height: 120,
                      child: NativeAd(
                        controller: controller,
                        position: index,
                      ),
                    );
                  },
                  itemCount: snapshot.data,
                );
              }
            },
            stream: controller.numOfAdsStream.stream,
          ),
        ),
      ),
    );
  }
}

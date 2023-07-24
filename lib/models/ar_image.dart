import 'dart:typed_data';

import 'depth_img_array.dart';

class ARImage {
  ARImage({
    this.bytes,
    this.width,
    this.height,
    this.depthImgArrays,
  })  : assert(bytes != null),
        assert(width != null && width > 0),
        assert(height != null && height > 0);

  final Uint8List? bytes;
  final int? width;
  final int? height;
  final DepthImgArrays? depthImgArrays;

  static ARImage fromMap(Map<dynamic, dynamic> map) {
    return ARImage(
      bytes: map['bytes'],
      width: map['width'],
      height: map['height'],
      depthImgArrays: DepthImgArrays.fromMap(Map.from(map['depthImgArrays'])),
    );
  }
}
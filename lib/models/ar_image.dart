import 'dart:typed_data';

import 'depth_img_array.dart';

class ARImage {
  ARImage({
    this.width,
    this.height,
    this.depthImgBytes,
    this.depthImgArrays,
    this.rawDepthImgBytes,
    this.confidenceImgBytes,
    this.rawDepthImgArrays,
    this.confidenceImgArrays,
  })  : assert(depthImgBytes != null),
        assert(rawDepthImgBytes != null),
        assert(confidenceImgBytes != null),
        assert(width != null && width > 0),
        assert(height != null && height > 0);

  final Uint16List? depthImgBytes;
  final Uint16List? rawDepthImgBytes;
  final Uint16List? confidenceImgBytes;
  final int? width;
  final int? height;
  final DepthImgArrays? depthImgArrays;
  final DepthImgArrays? rawDepthImgArrays;
  final DepthImgArrays? confidenceImgArrays;

  static ARImage fromMap(Map<dynamic, dynamic> map) {
    return ARImage(
      depthImgBytes: map['depthImgBytes'],
      rawDepthImgBytes: map['rawDepthImgBytes'],
      confidenceImgBytes: map['confidenceImgBytes'],
      width: map['width'],
      height: map['height'],
      depthImgArrays: DepthImgArrays.fromMap(Map.from(map['depthImgArrays'])),
      rawDepthImgArrays:
          DepthImgArrays.fromMap(Map.from(map['rawDepthImgArrays'])),
      confidenceImgArrays:
          DepthImgArrays.fromMap(Map.from(map['confidenceImgArrays'])),
    );
  }
}

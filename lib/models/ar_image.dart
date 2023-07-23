import 'dart:typed_data';

class ArImage {
  ArImage({
    this.bytes,
    this.width,
    this.height,
  })  : assert(bytes != null),
        assert(width != null && width > 0),
        assert(height != null && height > 0);

  final Uint8List? bytes;
  final int? width;
  final int? height;

  static ArImage fromMap(Map<dynamic, dynamic> map) {
    return ArImage(
      bytes: map['bytes'],
      width: map['width'],
      height: map['height'],
    );
  }

  Map<String, dynamic> toMap() => <String, dynamic>{
    'bytes': bytes,
    'width': width,
    'height': height
  }..removeWhere((String k, dynamic v) => v == null);
}
import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import android.util.Log
import android.util.Size
import android.graphics.YuvImage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class ImageUtil {
    fun imageToByteArray(image: Image): ByteArray? {
        var data: ByteArray? = null
        if (image.format == ImageFormat.YUV_420_888) {
            data = NV21toJPEG(
                YUV_420_888toNV21(image),
                image.width, image.height)
        }
        return data
    }

    fun YUV_420_888toNV21(image: Image): ByteArray {

        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 4

        val nv21 = ByteArray(ySize + uvSize * 2)

        val yBuffer = image.planes[0].buffer // Y
        val uBuffer = image.planes[1].buffer // U
        val vBuffer = image.planes[2].buffer // V

        var rowStride = image.planes[0].rowStride
        check(image.planes[0].pixelStride == 1)

        var pos = 0

        if (rowStride == width) { // likely
            yBuffer.get(nv21, 0, ySize)
            pos += ySize
        } else {
            var yBufferPos: Long = 0
            while (pos < ySize) {
                yBuffer.position(yBufferPos.toInt())
                yBuffer.get(nv21, pos, width)
                yBufferPos += (rowStride - width).toLong()
                pos += width
            }
        }

        rowStride = image.planes[2].rowStride
        val pixelStride = image.planes[2].pixelStride

        check(rowStride == image.planes[1].rowStride)
        check(pixelStride == image.planes[1].pixelStride)

        val vBufferCopy = ByteArray(vBuffer.remaining()) // Create a byte array
        vBuffer.get(vBufferCopy) // Copy the data from the vBuffer
        vBuffer.rewind()

        if (pixelStride == 2 && rowStride == width && uBuffer.get(0) == vBuffer.get(1)) {
            val savePixel = vBuffer.get(1)
            try {
                val invertedSavePixel = (savePixel.toInt() xor 0xFF).toByte()  // invert the byte
                vBufferCopy.put(1, invertedSavePixel)
                if (uBuffer.get(0) == invertedSavePixel) {
                    vBufferCopy.put(1, savePixel)
                    vBufferCopy.get(nv21, ySize, uvSize)

                    // Log.i("AMELIA", "I'm taking a shortcut in image parsing")
                    // return nv21 // shortcut
                }
                vBufferCopy.put(1, savePixel)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            for (row in 0 until height / 2) {
                for (col in 0 until width / 2) {
                    val vuPos = col * pixelStride + row * rowStride
                    nv21[pos++] = vBufferCopy.get(vuPos)
                    nv21[pos++] = uBuffer.get(vuPos)
                }
            }
        }
        return nv21
    }

    private fun NV21toJPEG(nv21: ByteArray, width: Int, height: Int): ByteArray {
        val out = ByteArrayOutputStream()
        val yuv = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        yuv.compressToJpeg(Rect(0, 0, width, height), 100, out)
        return out.toByteArray()
    }
}
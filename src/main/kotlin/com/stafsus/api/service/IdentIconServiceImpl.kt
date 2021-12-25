package com.stafsus.api.service

import org.springframework.stereotype.Service
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

@Service
class IdentIconServiceImpl : IdentIconService {
	override fun generateImage(text: String, imageWidth: Int, imageHeight: Int): BufferedImage {
		val width = 5
		val height = 5
		val hash = text.toByteArray()
		val identIcon = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		val raster = identIcon.raster
		val background = intArrayOf(255, 255, 255, 0)
		val foreground = intArrayOf(
			(hash[0].toInt() and 255),
			(hash[1].toInt() and 255),
			(hash[2].toInt() and 255),
			255
		)
		for (x in 0 until width) {
			//Enforce horizontal symmetry
			val i = if (x < 3) x else 4 - x
			for (y in 0 until height) {
				//toggle pixels based on bit being on/off
				val pixelColor: IntArray = if (hash[i].toInt() shr y and 1 == 1) foreground else background
				raster.setPixel(x, y, pixelColor)
			}
		}
		var finalImage = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)

		//Scale image to the size you want
		val at = AffineTransform()
		at.scale((imageWidth / width).toDouble(), (imageHeight / height).toDouble())
		val op = AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
		finalImage = op.filter(identIcon, finalImage)
		return finalImage
	}

	override fun saveBytes(bufferedImage: BufferedImage): ByteArray {
		val stream = ByteArrayOutputStream()
		ImageIO.write(bufferedImage, "png", stream)
		return stream.toByteArray()
	}

	override fun saveImage(bufferedImage: BufferedImage?, name: String?) {
		val outputFile = File("$name.png")
		try {
			ImageIO.write(bufferedImage, "png", outputFile)
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
}
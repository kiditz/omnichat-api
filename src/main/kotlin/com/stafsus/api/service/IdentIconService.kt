package com.stafsus.api.service

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream

interface IdentIconService {
	fun generateImage(text: String, imageWidth: Int, imageHeight: Int): BufferedImage
	fun saveBytes(bufferedImage: BufferedImage): ByteArray
	fun saveImage(bufferedImage: BufferedImage?, name: String?)
}
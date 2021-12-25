package com.stafsus.api.service

import com.google.cloud.storage.Bucket
import com.google.firebase.cloud.StorageClient
import com.stafsus.api.config.FirebaseProperties
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*


@Service
class FileServiceImpl(private val properties: FirebaseProperties) : FileService {
	fun getExtension(originalFileName: String): String? {
		return StringUtils.getFilenameExtension(originalFileName)
	}

	fun generateFileName(originalFileName: String): String {
		return "${UUID.randomUUID()}.${getExtension(originalFileName)}"
	}

	override fun getImageUrl(name: String): String {
		return "${properties.imageUrl}/${
			URLEncoder.encode(
				"${properties.root}/$name",
				StandardCharsets.UTF_8
			)
		}?alt=media"
	}


	override fun getImageUrl(name: String, destination: String): String {
		return "${properties.imageUrl}/${
			URLEncoder.encode(
				"${properties.root}/$destination/$name",
				StandardCharsets.UTF_8
			)
		}?alt=media"
	}

	override fun save(destination: String, file: MultipartFile): String {
		val bucket: Bucket = StorageClient.getInstance().bucket()
		val fileName = generateFileName(file.originalFilename!!)
		bucket.create("${properties.root}/$destination/$fileName", file.bytes, file.contentType)
		return fileName
	}

	override fun saveOriginal(destination: String, file: MultipartFile): String {
		val bucket: Bucket = StorageClient.getInstance().bucket()
		val fileName = file.originalFilename
		bucket.create("${properties.root}/$destination/$fileName", file.bytes, file.contentType)
		return fileName!!
	}

	override fun saveOriginal(file: MultipartFile): String {
		val bucket: Bucket = StorageClient.getInstance().bucket()
		val fileName = file.originalFilename
		bucket.create("${properties.root}/$fileName", file.bytes, file.contentType)
		return fileName!!
	}

	override fun saveOriginal(name: String, contentType: String, byte: ByteArray): String {
		return saveOriginal(name, "", contentType, byte)
	}

	override fun saveOriginal(name: String, destination: String, contentType: String, byte: ByteArray): String {
		val bucket: Bucket = StorageClient.getInstance().bucket()
		val fileName = "$name.png"
		if (StringUtils.hasText(destination)) {
			bucket.create("${properties.root}/$destination/$fileName", byte, contentType)
		} else {
			bucket.create("${properties.root}/$fileName", byte, contentType)
		}
		return fileName
	}


	override fun save(file: MultipartFile): String {
		val bucket: Bucket = StorageClient.getInstance().bucket()
		val fileName = generateFileName(file.originalFilename!!)
		bucket.create("${properties.root}/$fileName", file.bytes, file.contentType)
		return fileName
	}
}
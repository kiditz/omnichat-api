package com.stafsus.api.service

import org.springframework.web.multipart.MultipartFile


interface FileService {
	fun getImageUrl(name: String): String
	fun getImageUrl(name: String, destination: String): String
	fun save(destination: String, file: MultipartFile): String
	fun save(file: MultipartFile): String
	fun saveOriginal(destination: String, file: MultipartFile): String
	fun saveOriginal(file: MultipartFile): String
	fun saveOriginal(name: String, contentType: String, byte: ByteArray): String
	fun saveOriginal(name: String, destination: String, contentType: String, byte: ByteArray): String
}
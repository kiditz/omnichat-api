package com.stafsus.api.service

import org.springframework.web.multipart.MultipartFile


interface FileService {
    fun getImageUrl(name: String): String
    fun save(destination: String, file: MultipartFile): String
    fun save(file: MultipartFile): String
}
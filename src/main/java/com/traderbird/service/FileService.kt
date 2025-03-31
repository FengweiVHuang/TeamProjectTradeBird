package com.traderbird.service

import net.coobird.thumbnailator.Thumbnails
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.nio.file.Paths
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO

@Service
class FileService {
    private val fileStorageLocation: Path = Paths.get("file_storage").toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw RuntimeException("Could not create the directory where the uploaded files will be stored.", e)
        }
    }

    fun storeFile(subDirectory: String, file: File): String {
        val targetLocation = fileStorageLocation.resolve(subDirectory).resolve(file.name)
        try {

            Files.createDirectories(targetLocation.parent)

            Files.copy(file.toPath(), targetLocation, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            throw RuntimeException("Failed to store file $file in $targetLocation", e)
        }
        return targetLocation.toString()
    }

    fun convertMultipartFileToJpegAndSave(
        subDirectory: String, newName: String,
        file: MultipartFile
    ): Boolean {
        val targetLocation = fileStorageLocation.resolve(subDirectory).resolve("$newName.jpeg")
        try {
            Files.createDirectories(targetLocation.parent)

            val image = ImageIO.read(file.inputStream)
            val width = image.width
            val height = image.height

            Thumbnails.of(file.inputStream)
                .size(width, height)
                .outputFormat("jpeg")
                .toFile(targetLocation.toFile())
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun loadFile(subDirectory: String, fileName: String): File? {
        val targetLocation = fileStorageLocation.resolve(subDirectory).resolve(fileName)
        return if (Files.exists(targetLocation)) {
            targetLocation.toFile()
        } else {
            null
        }
    }

    fun fileExists(subDirectory: String, fileName: String): Boolean {
        val targetLocation = fileStorageLocation.resolve(subDirectory).resolve(fileName)
        return Files.exists(targetLocation)
    }

    fun deleteIfExists(subDirectory: String, fileName: String): Boolean {
        val targetLocation = fileStorageLocation.resolve(subDirectory).resolve(fileName)
        return Files.deleteIfExists(targetLocation)
    }
}
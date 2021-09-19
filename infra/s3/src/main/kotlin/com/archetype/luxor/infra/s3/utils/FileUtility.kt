package com.archetype.luxor.infra.s3.utils

import com.archetype.luxor.infra.s3.entity.CsvSerializable
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileUtility {
    private const val BYTE_ARRAY_SIZE = 1024

    // ZIP化するファイルサイズです。適当に調整してください
    private const val MIN_ZIP_FILE_SIZE = 5L * 1024L * 1024L

    // inline functionからアクセスできるようにpublicにする
    val mapper = CsvMapper().apply {
        registerModule(KotlinModule())
    }

    // 特に拡張性を考慮していません。必要に応じてoptionとかを実装してください
    inline fun <reified T : CsvSerializable> createTemporaryCsvFile(
        lines: List<T>,
        suffix: String,
    ): Path {
        val tempFile = Files.createTempFile(null, suffix)

        val schema = mapper
            .schemaFor(T::class.java)
            .withLineSeparator("\n")
            .withoutQuoteChar()
            .withHeader()

        Files.write(
            tempFile,
            mapper.writer(schema).writeValueAsBytes(lines)
        )

        return tempFile
    }

    fun compressFile(file: Path): Path {
        val path = file.toUri().path
        val ext = file.fileName.toString().substringAfterLast('.', "")
        val zip = path.removeSuffix(ext) + "zip"

        val fos = FileOutputStream(zip)
        val zos = ZipOutputStream(fos)

        zos.putNextEntry(
            ZipEntry(file.fileName.toString())
        )

        val bytes = ByteArray(BYTE_ARRAY_SIZE)
        var length: Int
        val fis = FileInputStream(file.toFile())

        while ((fis.read(bytes).also { length = it }) >= 0) {
            zos.write(bytes, 0, length)
        }

        zos.close()
        fis.close()
        fos.close()

        return Path.of(zip)
    }

    fun shouldBeCompressed(file: Path): Boolean =
        file.toFile().length() > MIN_ZIP_FILE_SIZE
}
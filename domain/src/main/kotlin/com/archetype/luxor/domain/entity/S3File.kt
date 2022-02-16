package com.archetype.luxor.domain.entity

data class S3File(
    private val bucket: String,
    private val key: String
) {
    fun uri():String = "s3://$bucket/$key"

    companion object {
        val EMPTY = S3File("", "")
    }
}
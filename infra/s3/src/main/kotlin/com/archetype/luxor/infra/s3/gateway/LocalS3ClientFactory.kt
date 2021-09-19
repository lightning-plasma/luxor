package com.archetype.luxor.infra.s3.gateway

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

// 自分のAWSアカウント内のS3ClientFactory
@Component
@EnableConfigurationProperties(value = [LocalS3Config::class])
class LocalS3ClientFactory(
    config: LocalS3Config,
) : S3ClientFactory(
    config.host,
    config.port
)

@ConstructorBinding
@ConfigurationProperties(prefix = "aws.s3.servers.local")
data class LocalS3Config(
    val host: String = "",
    val port: String = ""
)

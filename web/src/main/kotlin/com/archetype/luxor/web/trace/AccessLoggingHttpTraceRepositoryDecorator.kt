package com.archetype.luxor.web.trace

import mu.KotlinLogging
import org.springframework.boot.actuate.trace.http.HttpTrace
import org.springframework.boot.actuate.trace.http.HttpTraceRepository
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AccessLoggingHttpTraceRepositoryDecorator(
    private val repository: HttpTraceRepository
) : HttpTraceRepository {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu:HH:mm:ss Z")

    override fun findAll(): MutableList<HttpTrace> {
        return repository.findAll()
    }

    override fun add(trace: HttpTrace) {
        // Common Log Format + ログに残したい項目
        // @see https://en.wikipedia.org/wiki/Common_Log_Format
        // NOTE: Common Log Formatのうち、プロトコルとレスポンスボディサイズが取れないので固定で "-" にしている
        logger.info(
            "{}:{} - - [{}] \"{} {} -\" {} -",
            trace.request.remoteAddress.substringAfter('/'), // "ホスト名/IPアドレス"が返ってくるため
            trace.request.headers["X-Forwarded-Client-Port"]?.firstOrNull() ?: "",
            ZonedDateTime.ofInstant(trace.timestamp, ZoneId.systemDefault()).format(dateTimeFormatter),
            trace.request.method,
            trace.request.uri.path,
            trace.response.status
        )
        repository.add(trace)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}

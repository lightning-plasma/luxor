package com.archetype.luxor.infra.api.gateway

abstract class GatewayConfig(
    open val protocol: String,
    open val host: String,
    open val port: String,
) {
    fun url() = "$protocol://$host:$port"
}

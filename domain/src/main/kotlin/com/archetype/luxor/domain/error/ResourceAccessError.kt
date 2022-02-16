package com.archetype.luxor.domain.error

class ResourceAccessError(
    cause: Throwable,
    message: String = ""
) : Error(message.ifEmpty { cause.message ?: "" }, cause) {
    override fun toString() =
        "ResourceAccessError\tmessage=$message"
}
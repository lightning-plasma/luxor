package com.archetype.luxor.web.response

import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class Reply(
    val message: String
)
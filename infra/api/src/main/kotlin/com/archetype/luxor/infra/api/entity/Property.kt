package com.archetype.luxor.infra.api.entity

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

// Element
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "response")
//data class BookRatingXmlResponse(
//    @field:XmlElement(name = "genre")
//    var genre: String? = null,
//
//    @field:XmlElement(name = "rating")
//    var rating: String? = null
//)

// Attribute
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
data class BookRatingXmlResponse(
    @field:XmlElement(name = "property")
    var property: MutableList<BookRatingProperty> = mutableListOf()
)

@XmlAccessorType(XmlAccessType.FIELD)
data class BookRatingProperty(
    @field:XmlAttribute(name = "genre")
    var genre: String? = null,

    @field:XmlAttribute(name = "rating")
    var rating: String? = null
)

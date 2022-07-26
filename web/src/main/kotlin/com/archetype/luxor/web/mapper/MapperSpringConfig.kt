package com.archetype.luxor.web.mapper

import org.mapstruct.MapperConfig
import org.mapstruct.extensions.spring.SpringMapperConfig

// https://mapstruct.org/documentation/spring-extensions/reference/html/#mappersAsConvertersCustomNames
@MapperConfig(componentModel = "spring")
@SpringMapperConfig(conversionServiceAdapterPackage = "com.archetype.luxor.web.mapper.adapter")
interface MapperSpringConfig

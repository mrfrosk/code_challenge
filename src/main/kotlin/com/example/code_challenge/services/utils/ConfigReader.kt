package com.example.code_challenge.services.utils

import com.charleskorn.kaml.Yaml
import com.example.code_challenge.services.utils.dto.SourceProperties
import com.example.code_challenge.services.utils.dto.SourceProperty
import kotlinx.serialization.decodeFromString
import java.io.File

object ConfigReader {
    private val configFile = File("src/main/resources/application.yaml").bufferedReader().readLines()
    private val profiles = configFile.joinToString("\n")
        .split("---").subList(1, 3)
    private val processedProfiles = profiles.map {
        it
            .split("security")[0]
            .split("datasource:")[1]
    }.map {
        Yaml.default.decodeFromString<SourceProperty>(it)
    }

    fun read() = SourceProperties(processedProfiles.first(), processedProfiles.last())
}
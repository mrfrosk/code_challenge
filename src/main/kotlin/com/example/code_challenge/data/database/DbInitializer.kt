package com.example.code_challenge.data.database

import com.example.code_challenge.services.utils.ConfigReader
import com.example.code_challenge.services.utils.dto.SourceProperty
import java.sql.DriverManager

class DbInitializer {


    private val properties = ConfigReader.read()


    fun run() {
        initDb(properties.prodProperty)
        initDb(properties.testProperty)
    }

    private fun initDb(property: SourceProperty){
        val splitUrl = property.url.split("/")
        val url = splitUrl[0] + "//" + splitUrl[2] + "/"
        val dbName = splitUrl[3]

        val connection = DriverManager.getConnection(url, property.username, property.password)
        val command = "SELECT datname FROM pg_database where datname = '%s';"
        val query = connection.createStatement().executeQuery(command.format(dbName))
        query.next()
        if (query.row == 0){
            connection.createStatement().execute("create database %s".format(dbName))
        }
        connection.close()
    }
}
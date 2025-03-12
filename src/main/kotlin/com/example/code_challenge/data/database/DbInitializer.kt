package com.example.code_challenge.data.database

import java.sql.DriverManager

class DbInitializer {

    private val properties = DatabaseProperties()


    fun run() {
        createDatabaseInfNotExists(properties.prodDbName)
        createDatabaseInfNotExists(properties.testDbName)
    }

    private fun createDatabaseInfNotExists(dbName: String){
        val connection = DriverManager.getConnection(properties.url, properties.user, properties.password)
        val command = "SELECT datname FROM pg_database where datname = '%s';"
        val query = connection.createStatement().executeQuery(command.format(dbName))
        query.next()
        if (query.row == 0){
            connection.createStatement().execute("create database %s".format(dbName))
        }
        connection.close()
    }
}
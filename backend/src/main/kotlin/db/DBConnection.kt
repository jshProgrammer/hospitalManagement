package org.hospitalmanagement

import java.sql.DriverManager

fun loadEnv(filePath: String): Map<String, String> {
    return java.io.File(filePath)
        .readLines()
        .filter { it.isNotBlank() && !it.startsWith("#") }
        .associate {
            val (key, value) = it.split("=", limit = 2)
            key.trim() to value.trim()
        }
}

fun main() {
    //val env = loadEnv(".env")

    //println(credentials)
    //println(username)
    //println(password)
    DriverManager.getConnection(credentials, username, password).use { connection ->
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM users")

        while (resultSet.next()) {
            println(resultSet.getString("name"))
        }
    }

    //val conn = DriverManager.getConnection(credentials, username, password)//, username, password)
/*
    val stmt = conn.createStatement()
    val rs = stmt.executeQuery("SELECT id, person FROM employee LIMIT 1")

    while (rs.next()) {
        println("Iwelche daten yey")
        //println("${rs.getInt("id")} - ${rs.getString("name")}")
    }

    rs.close()
    stmt.close()
    conn.close()*/
}
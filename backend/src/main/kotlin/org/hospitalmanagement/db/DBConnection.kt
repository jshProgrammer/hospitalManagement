package org.hospitalmanagement.db
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

/*fun main() {
    val env = loadEnv(".env")

    val url = env["DB_URL"]
    var username = env["DB_USERNAME"]
    var password = env["DB_PASSWORD"]

    val conn = DriverManager.getConnection(url, username, password)//, username, password)

    val stmt = conn.createStatement()
    val rs = stmt.executeQuery("SELECT * FROM public.employee\n" +
            "ORDER BY id ASC LIMIT 100\n")

    while (rs.next()) {
        //println("Iwelche daten yey")
        println(rs.getString("id"))
    }

    rs.close()
    stmt.close()
    conn.close()
}*/
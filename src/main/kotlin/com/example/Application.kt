package com.example

import com.example.engine.ExampleProcessingUnit
import com.example.model.TimeSeriesData
import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(args: Array<String>) {
    /** default config (yaml) **/
    io.ktor.server.netty.EngineMain.main(args)
    routines()
    /** Tutorial example for running application **/
//    embeddedServer(
//        Netty,
//        port = 9292,
//        host = "0.0.0.0",
//        module = Application::module
//    ).start(wait = true)
}


fun routines(): Double = runBlocking {
    val processingUnit = ExampleProcessingUnit()
    val data = TimeSeriesData(System.currentTimeMillis(), 10.0)

    val result = withContext(Dispatchers.Default){
        processingUnit.process(data)
    }
    result

}

fun Application.module() {
    configureDatabases()
    configureRouting()
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, world - 2!")
        }
        get("/test1") {
            val result = routines().toString()
            val text = "<h2>Hello from Ktor</h2> </br> This is my result: $result"
            val type = ContentType.parse("text/html")
            call.respondText(text, type)
        }
        /**
         * Creating a buttom that will trigger my routine
         * We need:
         * 1. A new page
         * 2. An endpoint that will return the result of the routine
         * **/
        get("/runRoutinePage") {
            call.respondText(getHtmlPage(), ContentType.Text.Html)
        }
        get("/runRoutine") {
            val result = routines().toString()
            call.respondText(result, ContentType.Text.Plain)
        }
    }
}
fun getHtmlPage(): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Run Routine</title>
            <script>
                async function runRoutine() {
                    const response = await fetch('/runRoutine');
                    const result = await response.text();
                    document.getElementById('result').innerText = 'Result: ' + result;
                }
            </script>
        </head>
        <body>
            <h2>Run Routine</h2>
            <button onclick="runRoutine()">Run Routine</button>
            <p id="result"></p>
        </body>
        </html>
    """.trimIndent()
}

package com.example

import com.espertech.esper.common.client.EPCompiled
import com.espertech.esper.common.client.configuration.Configuration
import com.espertech.esper.compiler.client.CompilerArguments
import com.espertech.esper.compiler.client.EPCompileException
import com.espertech.esper.compiler.client.EPCompiler
import com.espertech.esper.compiler.client.EPCompilerProvider
import com.espertech.esper.runtime.client.EPRuntimeProvider
import com.example.engine.ExampleProcessingUnit
import com.example.engine.MyProcessingUnit
import com.example.esper.EsperStatements
import com.example.model.TimeSeriesData
import com.example.plugins.configureDatabases
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

fun routine2()  = runBlocking {
    val compiler : EPCompiler = EPCompilerProvider.getCompiler()
    val configuration = Configuration()
    configuration.common.addEventType(TimeSeriesData::class.java)
    val runtime = EPRuntimeProvider.getDefaultRuntime(configuration)

    val args = CompilerArguments(configuration)

    val event1 = TimeSeriesData(System.currentTimeMillis(), 10.0)
    val event2 = TimeSeriesData(System.currentTimeMillis() + 20000, 20.0) // 0.5 minutes later

    val epl = "@name('my-statement') select sum(value) as totalValue from TimeSeriesData.win:time_batch(10 sec)"
    val epCompiled: EPCompiled
    val results = mutableListOf<Double>()
    try {
        epCompiled = compiler.compile(epl, args)

        val deployment = runtime.getDeploymentService().deploy(epCompiled);
        println("Query deployed: ${deployment.deploymentId}")

        val statement = runtime.deploymentService.getStatement(deployment.deploymentId, "my-statement")
        statement.addListener { newData, _, _, _ ->
            val totalValue = newData[0]["totalValue"] as Double?
            if (totalValue != null) {results.add(totalValue)}
        }
        runtime.eventService.sendEventBean(event1, "TimeSeriesData")
        println("Event 1")
        delay(1000) // Wait for 0.5 minutes
        runtime.eventService.sendEventBean(event2, "TimeSeriesData")
        println("Event 2")
        for (i in 0..10) {
            println("Seconds: $i")
            delay(1000)
        }

        return@runBlocking "Result: ${results}"

    } catch (ex: EPCompileException) {
        // handle exception here
        throw RuntimeException(ex)
    }
}
fun routine3() = runBlocking {
//    val processingUnit = MyProcessingUnit()
    val config = Configuration()
    config.common.addEventType(TimeSeriesData::class.java)
    val runtime = EPRuntimeProvider.getDefaultRuntime(config)
    val processingUnit = MyProcessingUnit()

//    processingUnit.processTimeSeriesData(TimeSeriesData(System.currentTimeMillis(), 42.0))

    // Create two TimeSeriesData events separated by 0.5 minutes
    val event1 = TimeSeriesData(System.currentTimeMillis(), 10.0)
    val event2 = TimeSeriesData(System.currentTimeMillis() + 20000, 20.0) // 0.5 minutes later

    // Send events to Esper
    processingUnit.processTimeSeriesData(event1)
    delay(30000) // Wait for 0.5 minutes
    processingUnit.processTimeSeriesData(event2)

    // Deploy the EPL query
    val deployment = EsperStatements.query1()

    // Print the result of the query
    println("Query deployed: ${deployment.deploymentId}")
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
        get("/runRoutine2") {
            val result = routine2().toString()
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
                async function runRoutine2() {
                    const response = await fetch('/runRoutine2');
                    const result2 = await response.text();
                    document.getElementById('result2').innerText = 'Result: ' + result2;
                }
            </script>
        </head>
        <body>
            <h2>Run Routine</h2>
            <button onclick="runRoutine()">Run Routine</button>
            <p id="result"></p>
            
            <p></p>
            <h2> Run Routine 2 </h2>
            <button onclick="runRoutine2()">Run Routine</button>
            <p id="result2"></p>
        </body>
        </html>
    """.trimIndent()
}

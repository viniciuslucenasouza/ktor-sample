# Processing Events Sample

This sample project is to understand how to implement an data process engine.

I am using kTor framework, but it is not needed.

I followed this page: [Geeting started with kTor Server](https://ktor.io/docs/server-create-a-new-project.html)

This example does not include fetching data and sending data to a sink.  
It is just a simple example of how to process data.

Would be nice in the future to implement more things from the tutorial like:
- Integration tests.
- Registering error handlers

## How to run
You need to build frist:   
```shell
./gradlew build
```
Then run
```shell
./gradlew run
```
This project was changed to run in the port: 9292

## Architecture
The project is divided in 5 layers:

**1. Data Model**:  It is an implementation of type of data.
- **TimeSeries**: It is a data class that has a timestamp and a value.
- **RealTime**: It is a data class that has an id and a payload. It is used to represent the real time series.
- Also has Interfaces with generic types to represent fetch (DataSources) and send data (DataSink).

**2. Engine**:  Interface and abstract class with generic types of inputs that should be extended for specific analytics.
- **process**: Method that should be implemented in your code.

**3. Data Pipeline**: It is a generic class that executes the process. 
- **execute**: Method that should call the process for each unit to be processed. These units come from the engine.

**4. Aggregation**: Methods that receive the data and do the calculations you need over time.
- **methods**: Ensure these classes can be plugged into the pipeline.

**5. Main**: Has a scalability Considetration.
- **Coroutines**: `runBlocking` starts a coroutine that blocks the main thread.
- **Context**: `withContext()` switches the coroutine context to a background thread for cpu intensive processing.

## Steps to add new analytics

Thinking about steps to implement other process units, We may think about:

- **1. Define a new data model**:
```shell
// src/main/kotlin/com/example/model/NewDataModel.kt
package com.example.model

data class NewDataModel(val id: String, val value: Int)
```
- **2. Create a New Process**:
  - 2.1 Extend the `BaseProcessingUnit` class and implement the `process` method.
  - 2.2 Implement new process method with your logic.
```shell
// src/main/kotlin/com/example/engine/NewProcessingUnit.kt
package com.example.engine

import com.example.model.NewDataModel

class NewProcessingUnit : BaseProcessingUnit<NewDataModel, String>() {
    override fun process(input: NewDataModel): String {
        // Example processing logic
        return "Processed value: ${input.value * 10}"
    }
}
```
- **3. Add the new process to the pipeline**:
```shell
// src/main/kotlin/com/example/pipeline/Main.kt
package com.example.pipeline

import com.example.engine.NewProcessingUnit
import com.example.model.NewDataModel

fun main() {
    val newProcessingUnit = NewProcessingUnit()
    val pipelineManager = PipelineManager(listOf(newProcessingUnit))

    val inputData = NewDataModel("id123", 5)
    val result = pipelineManager.execute(inputData)

    println("Pipeline result: $result")
}
```
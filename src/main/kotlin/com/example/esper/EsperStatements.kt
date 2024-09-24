package com.example.esper

import com.espertech.esper.compiler.client.CompilerArguments
import com.espertech.esper.compiler.client.EPCompiler
import com.espertech.esper.compiler.client.EPCompilerProvider
import com.espertech.esper.runtime.client.EPDeployment

object EsperStatements {
         val compiler: EPCompiler = EPCompilerProvider.getCompiler()
         private val runtime = EsperConfig.runtime


    fun query1(): EPDeployment {
        val epl = "select sum(value) as totalValue from TimeSeriesData.win:time_batch(1 min)"
        val compilerArgs = CompilerArguments().apply {
            this.configuration = EsperConfig.config
        }
        val compiled = compiler.compileQuery(epl, compilerArgs)
        return runtime.deploymentService.deploy(compiled)
    }
}

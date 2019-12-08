package debugattach

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.project.Project
import com.jetbrains.python.PythonHelper
import com.jetbrains.python.debugger.attach.PyAttachToProcessCommandLineState
import com.jetbrains.python.run.PythonConfigurationType
import com.jetbrains.python.run.PythonRunConfiguration
import com.jetbrains.python.run.PythonScriptCommandLineState

class MayaAttachToProcessCliState(runConfig: PythonRunConfiguration, env: ExecutionEnvironment) : PythonScriptCommandLineState(runConfig, env) {
    companion object {
        fun create(project: Project, sdkPath: String, port: Int, pid: Int) : MayaAttachToProcessCliState {
            val scriptPath = "C:/Users/chris/AppData/Local/JetBrains/Toolbox/apps/PyCharm-P/ch-1/192.7142.56/helpers/pydev/pydevd_attach_to_process/attach_pydevd.py"

            val conf = PythonConfigurationType.getInstance().factory.createTemplateConfiguration(project) as PythonRunConfiguration
//            conf.scriptName = PythonHelper.ATTACH_DEBUGGER.asParamString() // TODO swap out for my own pydevd impl
            conf.scriptName = scriptPath
            conf.sdkHome = sdkPath
            conf.scriptParameters = "--port $port --pid $pid"

            val env = ExecutionEnvironmentBuilder.create(project, DefaultDebugExecutor.getDebugExecutorInstance(), conf).build()

            return MayaAttachToProcessCliState(conf, env)
        }
    }

    override fun doCreateProcess(commandLine: GeneralCommandLine?): ProcessHandler {
        val handler = super.doCreateProcess(commandLine)
        return PyAttachToProcessCommandLineState.PyRemoteDebugProcessHandler(handler)
    }
}

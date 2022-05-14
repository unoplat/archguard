package org.archguard.scanner.ctl.loader

import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.diffchanges.DiffChangesAnalyser
import org.archguard.scanner.core.diffchanges.DiffChangesContext
import org.archguard.scanner.core.git.GitAnalyser
import org.archguard.scanner.core.git.GitContext
import org.archguard.scanner.core.sca.ScaAnalyser
import org.archguard.scanner.core.sca.ScaContext
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.core.utils.CoroutinesExtension.asyncMap
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.impl.CliDiffChangesContext
import org.archguard.scanner.ctl.impl.CliGitContext
import org.archguard.scanner.ctl.impl.CliScaContext
import org.archguard.scanner.ctl.impl.CliSourceCodeContext
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs
import org.slf4j.LoggerFactory

class AnalyserDispatcher {
    fun dispatch(command: ScannerCommand) {
        when (command.type) {
            AnalyserType.SOURCE_CODE -> SourceCodeWorker(command)
            AnalyserType.GIT -> GitWorker(command)
            AnalyserType.DIFF_CHANGES -> DiffChangesWorker(command)
            AnalyserType.SCA -> ScaWorker(command)
            else -> TODO("not implemented yet")
        }.run()
    }
}

private interface Worker<T : Context> {
    fun run()
}

private class SourceCodeWorker(command: ScannerCommand) : Worker<SourceCodeContext> {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val analyserSpecs = listOfNotNull(command.languageSpec) + command.featureSpecs
    private val context = CliSourceCodeContext(
        language = command.languageSpec!!.identifier,
        features = command.featureSpecs.map { it.identifier },
        client = command.buildClient(),
        path = command.path,
    )

    private fun <T> getOrInstall(identifier: String): T {
        val theOne = analyserSpecs.find { identifier == it.identifier }
            ?: throw IllegalArgumentException("No analyser found for identifier: $identifier")
        return AnalyserLoader.load(context, theOne) as T
    }

    override fun run(): Unit = runBlocking {
        val languageAnalyser = getOrInstall<SourceCodeAnalyser>(context.language)
        val ast = languageAnalyser.analyse(null) ?: return@runBlocking
        context.features.asyncMap {
            try {
                getOrInstall<SourceCodeAnalyser>(it).analyse(ast)
            } catch (e: Exception) {
                logger.error("Error while analysing feature: $it", e)
            }
        }
    }
}

private class GitWorker(command: ScannerCommand) : Worker<GitContext> {
    private val analyserSpec = OfficialAnalyserSpecs.GIT.spec()
    private val context = CliGitContext(
        client = command.buildClient(),
        path = command.path,
        repoId = command.repoId!!,
        branch = command.branch,
        startedAt = command.startedAt,
    )

    override fun run() {
        (AnalyserLoader.load(context, analyserSpec) as GitAnalyser).analyse()
    }
}

private class DiffChangesWorker(command: ScannerCommand) : Worker<DiffChangesContext> {
    private val analyserSpec = OfficialAnalyserSpecs.DIFF_CHANGES.spec()
    private val context = CliDiffChangesContext(
        client = command.buildClient(),
        path = command.path,
        branch = command.branch,
        since = command.since!!,
        until = command.until!!,
        depth = command.depth,
    )

    override fun run() {
        (AnalyserLoader.load(context, analyserSpec) as DiffChangesAnalyser).analyse()
    }
}

private class ScaWorker(command: ScannerCommand) : Worker<ScaContext> {
    private val analyserSpec = OfficialAnalyserSpecs.SCA.spec()
    private val context = CliScaContext(
        client = command.buildClient(),
        path = command.path,
        // TODO split the analyser config and the language
        language = command.language.toString(),
    )

    override fun run() {
        (AnalyserLoader.load(context, analyserSpec) as ScaAnalyser).analyse()
    }
}

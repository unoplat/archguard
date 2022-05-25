rootProject.name = "archguard"

// backend
include(":")

/**
 * architecture as code repos
 */

include(":architecture-as-code:domain")
include(":architecture-as-code:dsl")
include(":architecture-as-code:repl-api")

/**
 *  scanner projects:
 * scanner projects:
 * - core: define the core models and apis
 * - cli: executable command line tools to manage and choregraph
 */
include(
    ":scanner_core",
    ":scanner_cli",
    // source code
    ":analyser_sourcecode:lang_kotlin",
    ":analyser_sourcecode:lang_java",
    ":analyser_sourcecode:lang_typescript",
    ":analyser_sourcecode:lang_python",
    ":analyser_sourcecode:lang_golang",
    ":analyser_sourcecode:lang_csharp",
    ":analyser_sourcecode:lang_scala",
    ":analyser_sourcecode:feat_apicalls",
    ":analyser_sourcecode:feat_datamap",
    ":analyser_git",
    ":analyser_diff_changes",
    ":analyser_sca",
    ":analyser_architecture",
)

/**
 * linters projects: a specific set of analysers to detect specific patterns
 */
include(":rule-core")
include(":rule-linter:rule-sql")
include(":rule-linter:rule-test-code")
include(":rule-linter:rule-webapi")
include(":rule-linter:rule-code")
include(":rule-doc-generator")


pluginManagement {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        gradlePluginPortal()
    }
}

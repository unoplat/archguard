/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

rootProject.name = "ArchGuard Scanner"

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
    ":scanner_sourcecode:lang_kotlin",
    ":scanner_sourcecode:lang_java",
    ":scanner_sourcecode:lang_typescript",
    ":scanner_sourcecode:lang_python",
    ":scanner_sourcecode:lang_golang",
    ":scanner_sourcecode:lang_csharp",
    ":scanner_sourcecode:lang_scala",
    ":scanner_sourcecode:feat_apicalls",
    ":scanner_sourcecode:feat_datamap",
    // others
)

/**
 *  analysers projects: process the ast or dbschema to extract more information
 */
include(":analyser_sca")
include(":analyser_architecture")

/**
 * linters projects: a specific set of analysers to detect specific patterns
 */
include(":rule_core")
include(":rule_linter:rule_sql")
include(":rule_linter:rule_test_code")
include(":rule_linter:rule_webapi")
include(":rule_linter:rule_code")

/**
 * others projects:
 */
include(":doc_generator")
include(":doc_executor")

/* ------------------------------------------------------------------------------ */

// legacy scanners
include(":scan_git")
include(":diff_changes")

// include(":legacy:scan_sourcecode")
// include(":legacy:scan_jacoco")
// include(":legacy:scan_test_badsmell")
// include(":legacy:scan_bytecode")

// common for share code repository
// TODO need to refactor as http, related https://github.com/archguard/archguard/issues/43, https://github.com/archguard/scanner/issues/3
include(":common_code_repository")

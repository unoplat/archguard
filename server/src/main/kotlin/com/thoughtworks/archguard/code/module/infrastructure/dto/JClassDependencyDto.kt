package com.thoughtworks.archguard.code.module.infrastructure.dto

import org.archguard.model.Dependency
import org.archguard.model.vos.JClassVO

class JClassDependencyDto(val moduleCaller: String, val classCaller: String, val moduleCallee: String, val classCallee: String) {
    fun toJClassDependency(): Dependency<JClassVO> {
        return Dependency(
            JClassVO(classCaller, moduleCaller),
            JClassVO(classCallee, moduleCallee)
        )
    }
}

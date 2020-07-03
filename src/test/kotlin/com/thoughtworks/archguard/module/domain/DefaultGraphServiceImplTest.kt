package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultGraphServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    private lateinit var service: DefaultGraphServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = DefaultGraphServiceImpl(logicModuleRepository)
    }

    @Test
    fun `should get graph of all logic modules dependency`() {
        // given
        val logicModule1 = LogicModule("1", "module1", listOf(SubModule("bm1"), SubModule("bm2")))
        val logicModule2 = LogicModule("2", "module2", listOf(SubModule("bm3"), SubModule("bm4")))
        val logicModule3 = LogicModule("3", "module3", listOf(SubModule("bm5")))
        val logicModules = listOf(logicModule1, logicModule2, logicModule3)

        val dependency1 = Dependency(JClass.createJClassFromFullName("bm1.any"), JClass.createJClassFromFullName("bm3.any"))
        val dependency2 = Dependency(JClass.createJClassFromFullName("bm3.any"), JClass.createJClassFromFullName("bm2.any"))
        val dependency3 = Dependency(JClass.createJClassFromFullName("bm5.any"), JClass.createJClassFromFullName("bm4.any"))
        val dependencies = listOf(dependency1, dependency2, dependency3)

        every { logicModuleRepository.getAllByShowStatus(true) } returns logicModules
        every { logicModuleRepository.getAllClassDependency(any()) } returns dependencies

        // when
        val moduleGraph = service.getLogicModuleGraph()

        // then
        assertThat(moduleGraph.nodes.size).isEqualTo(3)
        assertThat(moduleGraph.edges.size).isEqualTo(3)
    }

    @Test
    fun `should map to module`() {
        val results = listOf(Dependency(JClass.createJClassFromFullName("caller.method1"), JClass.createJClassFromFullName("callee.method1")),
                Dependency(JClass.createJClassFromFullName("caller.method2"), JClass.createJClassFromFullName("callee.method2")))
        val logicModule1 = LogicModule("id1", "module1", listOf(JClass.createJClassFromFullName("caller.method1")))
        val logicModule2 = LogicModule("id2", "module2", listOf(JClass.createJClassFromFullName("callee.method1")))
        val logicModule3 = LogicModule("id3", "module3", listOf(JClass.createJClassFromFullName("callee.method1")))
        val logicModule4 = LogicModule("id4", "module4", listOf(JClass.createJClassFromFullName("caller.method2"), JClass.createJClassFromFullName("callee.method2")))
        val modules = listOf(logicModule1, logicModule2, logicModule3, logicModule4)
        val moduleDependency = service.mapClassDependenciesToModuleDependencies(results, modules)
        assertThat(moduleDependency.size).isEqualTo(2)
        assertThat(moduleDependency).containsAll(listOf(Dependency(logicModule1, logicModule2), Dependency(logicModule1, logicModule3)))
    }
}
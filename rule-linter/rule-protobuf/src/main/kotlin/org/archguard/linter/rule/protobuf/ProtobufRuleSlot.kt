package org.archguard.linter.rule.protobuf

import org.archguard.context.ContainerService
import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet

class ProtobufRuleSlot : Slot {
    override var material: Materials = listOf()
    override var outClass: String = Issue.Companion::class.java.name

    override fun ticket(): Coin {
        return listOf(ContainerService::class.java.name)
    }

    override fun prepare(items: List<Any>): List<Any> {
        val ruleSets = listOf(ProtobufRuleSetProvider().get())
        this.material = ruleSets
        return ruleSets
    }

    override fun process(items: List<Any>): List<Any> {
        return ProtobufRuleVisitor(items as List<ContainerService>).visitor(this.material as Iterable<RuleSet>)
    }
}
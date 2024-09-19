package io.github.paletteLens.util

/**
 * Author: mitchtabian,
 * Availability: https://github.com/mitchtabian/MVVMRecipeApp
 */
interface DomainMapper<T, DomainModel> {
    fun mapToDomainModel(model: T): DomainModel

    fun mapFromDomainModel(domainModel: DomainModel): T
}

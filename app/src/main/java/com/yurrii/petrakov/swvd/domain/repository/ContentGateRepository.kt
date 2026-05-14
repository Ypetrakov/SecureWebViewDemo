package com.yurrii.petrakov.swvd.domain.repository

interface ContentGateRepository {

    fun isContentAvailable(): Boolean

    fun setContentAvailable(value: Boolean)
}
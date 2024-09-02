package com.lgcns.crossdev.onboarding1.data.mapper

import com.lgcns.crossdev.onboarding1.data.entity.PlanEntity
import com.lgcns.crossdev.onboarding1.data.entity.TravelEntity
import com.lgcns.crossdev.onboarding1.domain.model.Plan
import com.lgcns.crossdev.onboarding1.domain.model.Travel
import java.time.LocalDate

fun Plan.toEntity(): PlanEntity {
    return PlanEntity(
        this.id,
        this.travelId,
        this.date,
        this.name,
        this.nameAlter,
        this.locationX,
        this.locationY,
        this.time,
        this.memo,
        this.pos,
    )
}

fun PlanEntity.toModel(): Plan {
    return Plan(
        this.id,
        this.travelId,
        this.date,
        this.name,
        this.nameAlter,
        this.locationX,
        this.locationY,
        this.time,
        this.memo,
        this.pos,
    )
}

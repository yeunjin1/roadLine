package com.lgcns.crossdev.onboarding1.domain.model

import java.io.Serializable
import java.time.LocalDate

data class Plan (
    val id: Long? = null,
    val travelId: Long,
    val date: LocalDate,
    var name: String = "",
    var nameAlter: String? = null,
    var locationX: Double = 0.0,
    var locationY: Double = 0.0,
    var time: Int? = null,
    var memo: String? = null,
    var pos: Int = -1,
    // 화면 표시를 위한 변수
    var viewType: PlanViewType? = null, //Vertical Plan 왼쪽 선 타입
    var editMode: Boolean = false //Vertical Plan 수정 모드
): Serializable

enum class PlanViewType(val value: Int) {
    TOP(0), MIDDLE(1), BOTTOM(2), ONE(3)
}
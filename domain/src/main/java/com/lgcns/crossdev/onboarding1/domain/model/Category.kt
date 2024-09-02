package com.lgcns.crossdev.onboarding1.domain.model

data class Category(
    val btnId: Int,
    val imgId: Int,
    val categoryType: Int
) {
    enum class CategoryType(val value: Int) {
        ETC(0),
        MEAL(1),
        SHOPPING(2),
        TRANSPORTATION(3),
        TOUR(4),
        LODGING(5)
    }
}



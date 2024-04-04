package com.lgcns.crossdev.onboarding1.data.dto

import com.google.gson.annotations.SerializedName

data class CurrencyResponse (
    @SerializedName("result")
    val result : Int, //조회 결과 (1 : 성공, 2 : DATA코드 오류, 3 : 인증코드 오류, 4 : 일일제한횟수 마감)
    @SerializedName("cur_unit")
    val code : String, //통화코드
    @SerializedName("cur_nm")
    val name : String, //국가/통화명
    @SerializedName("tts")
    val rate : String //전신환(송금)보내실때
)
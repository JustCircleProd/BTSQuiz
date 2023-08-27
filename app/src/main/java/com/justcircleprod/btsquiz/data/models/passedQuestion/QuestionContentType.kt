package com.justcircleprod.btsquiz.data.models.passedQuestion

enum class QuestionContentType {
    TEXT_CONTENT_TYPE,
    IMAGE_CONTENT_TYPE,
    VIDEO_CONTENT_TYPE,
    AUDIO_CONTENT_TYPE;

    companion object {
        fun fromString(value: String) = values().first { it.toString() == value }
    }
}
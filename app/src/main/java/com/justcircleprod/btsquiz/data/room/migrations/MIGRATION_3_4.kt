package com.justcircleprod.btsquiz.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.data.models.questions.VideoQuestion


val MIGRATION_3_4: Migration = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        for (question in audioQuestions) {
            database.execSQL(
                """INSERT OR IGNORE INTO audio_questions
                | (id, audio_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.audio_entry_name}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }

        for (question in videoQuestions) {
            database.execSQL(
                """INSERT OR IGNORE INTO video_questions    
                | (id, video_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.video_entry_name}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }
    }

    val audioQuestions = listOf(
        AudioQuestion(
            100,
            "bad_decisions",
            "Make It Right", "Don't Leave Me",
            "Bad Decisions", "Anpanman",
            3, 350,
        ),
        AudioQuestion(
            101,
            "begin",
            "Go Go", "Stay",
            "Butterfly", "Begin",
            4, 300,
        ),
        AudioQuestion(
            102,
            "best_of_me",
            "Best of Me", "Begin",
            "Airplane Pt.2", "Dream Glow",
            1, 300,
        ),
        AudioQuestion(
            103,
            "blue_and_grey",
            "Rain", "Blue & Grey",
            "Waste It On Me", "Film out",
            2, 600,
        ),
        AudioQuestion(
            104,
            "butterfly",
            "Sea", "The Planet",
            "Butterfly", "Butter",
            3, 350,
        ),
        AudioQuestion(
            105,
            "cypher_part_4",
            "Love Maze", "Cypher Pt.2",
            "Telepathy", "Cypher Pt.4",
            4, 600,
        ),
        AudioQuestion(
            106,
            "film_out",
            "Film out", "Make It Right",
            "Mikrokosmos", "2nd Grade",
            1, 600,
        ),
        AudioQuestion(
            107,
            "make_it_right",
            "Cypher Pt.4", "Make It Right",
            "Blue & Grey", "Lie",
            2, 300,
        ),
        AudioQuestion(
            108,
            "take_two",
            "Coffee", "21st Century Girls",
            "Take Two", "Your Eyes Tell",
            3, 550,
        ),
        AudioQuestion(
            109,
            "the_planet",
            "Outro: Wings", "Dis-ease",
            "Singularity", "The Planet",
            4, 500,
        ),
        AudioQuestion(
            110,
            "twenty_first_century_girl",
            "21st Century Girls", "FIRE",
            "PERSONA", "Cypher Pt.2",
            1, 550,
        ),
        AudioQuestion(
            111,
            "waste_it_on_me",
            "Louder Than Bombs", "Waste It On Me",
            "Best of Me", "Am I Wrong",
            2, 300,
        )
    )

    val videoQuestions = listOf(
        VideoQuestion(
            32,
            "bad_decisions_video",
            "Dynamite", "IDOL",
            "Bad Decisions", "Butter",
            3, 550,
        ),
        VideoQuestion(
            33,
            "film_out_video",
            "Stay Gold", "My Universe",
            "Crystal Snow", "Film out",
            4, 600,
        ),
        VideoQuestion(
            34,
            "make_it_right_video",
            "Make It Right", "We are Bulletproof : the Eternal",
            "Life Goes On", "PERSONA",
            1, 550,
        ),
    )
}
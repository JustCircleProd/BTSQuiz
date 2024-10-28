package com.justcircleprod.btsquiz.core.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion


val MIGRATION_12_13: Migration = object : Migration(12, 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой альбом позволил выиграть группе номинацию \"Артист года\" на Mnet Asian Music Awards в декабре 2016 года?' WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"По какой специальности Jin окончил университет Конкук в 2017 году?\" WHERE id = 6"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Вокальная\" WHERE id = 6"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Художественная\" WHERE id = 6"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Столярная\" WHERE id = 6"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Исскуство и актёрство\" WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое название получил первый альбом в карьере BTS?\" WHERE id = 7"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"По какой причине Suga не смог участвовать в выступлениях в конце 2013 - начале 2014 года?\" WHERE id = 9"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой пейринг является самым популярным?\" WHERE id = 16"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой примерно рост у RM?\" WHERE id = 18"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто из участников посетил Россию во время своего отпуска?\" WHERE id = 19"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Spring Day\"?' WHERE id = 24"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Жить настоящим моментом — нормально\" WHERE id = 24"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Боль, которую приходится пережить, столкнувшись с утратой\" WHERE id = 24"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Страх потерять страсть к музыке\" WHERE id = 24"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Подростковая зависть и жадность\" WHERE id = 24"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 2 WHERE id = 24"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 24"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Как зовут собаку J-Hope?\" WHERE id = 29"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Микки\" WHERE id = 29"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Рэпмон\" WHERE id = 29"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Ддосун\" WHERE id = 29"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Ёнтан\" WHERE id = 29"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Anpanman\"?' WHERE id = 32"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Стремление вдохновлять своей музыкой\" WHERE id = 32"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Боль, которую приходится пережить, столкнувшись с утратой\" WHERE id = 32"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Жить настоящим моментом — нормально\" WHERE id = 32"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Страх перед неуверенностью, которая отталкивает близких\" WHERE id = 32"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 1 WHERE id = 32"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 32"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"С выходом какого сингла началась хронология Вселенной BTS, рассказывающая истории всех семи участников?\" WHERE id = 36"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кто из участников рассматривал \"Baby J\" в качестве варианта сценического имени?' WHERE id = 37"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Совместно с кем группа исполняет песню \"Boy with Luv\"?' WHERE id = 47"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какая песня предположительно была посвящена бабушке V?\" WHERE id = 48"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"В каком году вышла первая сольная песня V?\" WHERE id = 49"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кем хотел бы стать V, если бы не стал айдолом?\" WHERE id = 50"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"21st Century Girls\"?' WHERE id = 51"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Подростковая зависть и жадность\" WHERE id = 51"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Стремление вдохновлять своей музыкой\" WHERE id = 51"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены\" WHERE id = 51"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Призыв девушек быть самими собой\" WHERE id = 51"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 4 WHERE id = 51"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 450 WHERE id = 51"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"FAKE LOVE\"?' WHERE id = 52"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Прославление простоты любви\" WHERE id = 52"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Подростковая зависть и жадность\" WHERE id = 52"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Тёмная сторона любви\" WHERE id = 52"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Страх потерять страсть к музыке\" WHERE id = 52"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 3 WHERE id = 52"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 450 WHERE id = 52"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"В какой песне Suga дал волю эмоциям и рассказал о депрессии, компульсивном расстройстве и о том, что значит мечтать стать айдолом?\" WHERE id = 57"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какая специализация была у Jimin в старшей школе искусств Пусана?\" WHERE id = 58"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какие предметы были любимыми в школе у Jimin?\" WHERE id = 59"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Искусство, физкультура и математика\" WHERE id = 59"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Корейский и английский языки\" WHERE id = 59"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Физика, химия и астрономия\" WHERE id = 59"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Науки об обществе и экономике\" WHERE id = 59"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Go Go\"?' WHERE id = 60"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Жить настоящим моментом — нормально\" WHERE id = 60"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Критика потребительства\" WHERE id = 60"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Призыв девушек быть самими собой\" WHERE id = 60"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Критика чрезмерного давления на молодое поколение\" WHERE id = 60"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 2 WHERE id = 60"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 60"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Am I Wrong\"?' WHERE id = 62"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены\" WHERE id = 62"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Боль быть забытым\" WHERE id = 62"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Критика потребительства\" WHERE id = 62"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Прославление простоты любви\" WHERE id = 62"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 1 WHERE id = 62"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 550 WHERE id = 62"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Чем Jimin часто занимался во время перелётов в самолете?\" WHERE id = 63"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Учил английский язык\" WHERE id = 63"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Изучал квантовую физику\" WHERE id = 63"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Учил японский язык\" WHERE id = 63"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Изучал программирование\" WHERE id = 63"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Magic Shop\"?' WHERE id = 64"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Тёмная сторона любви\" WHERE id = 64"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Одиночество и тревога перед неизведанным\" WHERE id = 64"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Боль, которую приходится пережить, столкнувшись с утратой\" WHERE id = 64"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Вечная благодарность своим фанатам\" WHERE id = 64"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 4 WHERE id = 64"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 550 WHERE id = 64"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Young Forever\"?' WHERE id = 65"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены\" WHERE id = 65"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Подростковая зависть и жадность\" WHERE id = 65"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Признание своих страхов как артистов\" WHERE id = 65"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Критика чрезмерного давления на молодое поколение\" WHERE id = 65"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 3 WHERE id = 65"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 65"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"По чему Jimin занимает первое место в группе?\" WHERE id = 66"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Громкости голоса\" WHERE id = 66"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Физическим нормативам\" WHERE id = 66"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Опозданиям\" WHERE id = 66"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Пропущенным репетициям\" WHERE id = 66"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"The Truth Untold\"?' WHERE id = 67"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Вечная благодарность своим фанатам\" WHERE id = 67"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Страх перед неуверенностью, которая отталкивает близких\" WHERE id = 67"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Жить настоящим моментом — нормально\" WHERE id = 67"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Тёмная сторона любви\" WHERE id = 67"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 2 WHERE id = 67"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 67"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Однажды J-Hope назвал RM самым … участником группы?\" WHERE id = 68"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Романтичным\" WHERE id = 68"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Смелым\" WHERE id = 68"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Внимательным\" WHERE id = 68"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Ловким\" WHERE id = 68"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Что, по словам других участников группы, часто делает RM?\" WHERE id = 71"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Беспокоится\" WHERE id = 71"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Бывает слишком самоуверен\" WHERE id = 71"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Ленится\" WHERE id = 71"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Перерабатывает\" WHERE id = 71"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Silver Spoon\"?' WHERE id = 72"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Критика чрезмерного давления на молодое поколение\" WHERE id = 72"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Тёмная сторона любви\" WHERE id = 72"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Критика потребительства\" WHERE id = 72"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Вечная благодарность своим фанатам\" WHERE id = 72"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 1 WHERE id = 72"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 72"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Black Swan\"?' WHERE id = 73"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Призыв девушек быть самими собой\" WHERE id = 73"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Боль, которую приходится пережить, столкнувшись с утратой\" WHERE id = 73"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Прославление простоты любви\" WHERE id = 73"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Страх потерять страсть к музыке\" WHERE id = 73"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 4 WHERE id = 73"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 73"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кто снимался в костюме доктора в музыкальном клипе \"Dope\"?' WHERE id = 76"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кто лежит в перьях от подушки в музыкальном клипе \"Run\"?' WHERE id = 77"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кто первый поднялся из-за парты в музыкальном клипе \"N.O\"?' WHERE id = 78"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Что написано балончиком на упавшей с неба машине в музыкальном клипе \"FIRE\"?' WHERE id = 79"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кто в начале музыкального клипа \"Blood Sweat Tears\" сидит на полу в гостиной, пока остальные сидят либо на диване, либо на кресле?' WHERE id = 80"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Sea\"?' WHERE id = 89"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Боль быть забытым\" WHERE id = 89"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Преодоление трудностей\" WHERE id = 89"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Критика чрезмерного давления на молодое поколение\" WHERE id = 89"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Признание своих страхов как артистов\" WHERE id = 89"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 2 WHERE id = 89"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 89"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Paradise\"?' WHERE id = 90"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Преодоление трудностей\" WHERE id = 90"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Стремление вдохновлять своей музыкой\" WHERE id = 90"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Жить настоящим моментом — нормально\" WHERE id = 90"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Одиночество и тревога перед неизведанным\" WHERE id = 90"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 3 WHERE id = 90"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 90"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Paradise\"?' WHERE id = 94"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Преодоление трудностей\" WHERE id = 94"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Стремление вдохновлять своей музыкой\" WHERE id = 94"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Жить настоящим моментом — нормально\" WHERE id = 94"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Одиночество и тревога перед неизведанным\" WHERE id = 94"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 3 WHERE id = 94"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 600 WHERE id = 94"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой хештег, связанный с Jin, стал популярным после Billboard Music Awards в 2017 году?\" WHERE id = 96"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"#ThirdOneFromTheLeft\" WHERE id = 96"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"#ThatGuy\" WHERE id = 96"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"#Jin\" WHERE id = 96"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"#FirstOneFromTheRight\" WHERE id = 96"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Dis-ease\"?' WHERE id = 97"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Преодоление тревоги и страха\" WHERE id = 97"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Прославление простоты любви\" WHERE id = 97"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Подростковая зависть и жадность\" WHERE id = 97"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Критика потребительства\" WHERE id = 97"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 1 WHERE id = 97"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 550 WHERE id = 97"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Trivia: Love\"?' WHERE id = 98"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Призыв девушек быть самими собой\" WHERE id = 98"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Тёмная сторона любви\" WHERE id = 98"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Боль быть забытым\" WHERE id = 98"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Прославление простоты любви\" WHERE id = 98"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 4 WHERE id = 98"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 500 WHERE id = 98"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой машиной владеет J-Hope?\" WHERE id = 99"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"В каком шоу-выживании участвовал Jin?\" WHERE id = 100"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто из участников является большим любителем чтения?\" WHERE id = 101"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Любимое число Jimin?\" WHERE id = 102"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кем рассчитывал работать Suga, когда подписывал контаркт с Big Hit Entertainment в 2010 году?\" WHERE id = 103"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Музыкальным продюсером\" WHERE id = 103"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Певцом\" WHERE id = 103"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Композитором\" WHERE id = 103"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Бизнесменом\" WHERE id = 103"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Где находится шрам на лице у Jimin?\" WHERE id = 106"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Над левым глазом\" WHERE id = 106"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Над правой бровью\" WHERE id = 106"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Над левой бровью\" WHERE id = 106"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Над правым глазом\" WHERE id = 106"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"По какой причине у Jungkook появился шрам на левой щеке?\" WHERE id = 107"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"Whalien 52\"?' WHERE id = 110"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Прославление простоты любви\" WHERE id = 110"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены\" WHERE id = 110"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Одиночество и тревога перед неизведанным\" WHERE id = 110"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Призыв девушек быть самими собой\" WHERE id = 110"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 3 WHERE id = 110"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 500 WHERE id = 110"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто однажды съел 6 порций рамена за раз?\" WHERE id = 111"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Способность воплотить воображение в реальность\" WHERE id = 112"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Addictive Representative M.C. for Youth\" WHERE id = 114"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Amiable Representative M.C. for Youth\" WHERE id = 114"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Attentive Representative M.C. for Youth\" WHERE id = 114"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Adorable Representative M.C. for Youth\" WHERE id = 114"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"У кого из участников есть привычка грызть ногти?\" WHERE id = 116"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто из участников ненавидит фильмы ужасов?\" WHERE id = 119"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кто в клипе \"Danger\" случайно отрезал свои настоящие волосы, хотя должен был стричь парик?' WHERE id = 120"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кто сказал фразу: \"But we have reached the point where we can communicate wordlessly, basically just by watching each other and reading the expressions\"' WHERE id = 121"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто из участников однажды попал в аварию и вывехнул плечо?\" WHERE id = 123"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Во время просмотра какого ситкома RM выучил английский?\" WHERE id = 124"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто из участников часто сам стрижёт себе волосы?\" WHERE id = 125"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто из участников настолько неуклюж, что часто падает со стула?\" WHERE id = 126"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кого из участников почти год после дебюта группы, не мог запомнить генеральный директор их музыкальной компании?\" WHERE id = 127"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"С кого из участников упали штаны во время выступления в начале карьеры?\" WHERE id = 128"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Кто из участников хорошо владеет пальцами ног?\" WHERE id = 129"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"В какой социальной сети BTS получили собственные смайлики, став первой K-pop группой сумевшей подобное?\" WHERE id = 134"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое настоящее имя у RM?\" WHERE id = 135"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое настоящее имя у Jin?\" WHERE id = 136"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое настоящее имя у J-Hope?\" WHERE id = 137"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Jeon Jung-kook\" WHERE id = 137"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое настоящее имя у V?\" WHERE id = 138"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое настоящее имя у Jimin?\" WHERE id = 139"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Jeon Jung-kook\" WHERE id = 139"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое настоящее имя у Jungkook?\" WHERE id = 140"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Jeon Jung-kook\" WHERE id = 140"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какое настоящее имя у Suga?\" WHERE id = 141"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой примерно рост у Jungkook?\" WHERE id = 169"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"178 см\" WHERE id = 169"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"176 см\" WHERE id = 169"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"182 см\" WHERE id = 169"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"180 см\" WHERE id = 169"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 1 WHERE id = 169"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой примерно рост у Jimin?\" WHERE id = 170"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой примерно рост у Jin?\" WHERE id = 171"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой примерно рост у Suga?\" WHERE id = 172"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой примерно рост у J-Hope?\" WHERE id = 173"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой примерно рост у V?\" WHERE id = 174"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Сколько наград BTS получили на Billboard Music Awards 2022, что сделало их самой номинируемой и самой награждаемой группой за всю историю данного шоу?\" WHERE id = 180"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какой скрытый смысл вложен в песню \"134340 (Pluto)\"?' WHERE id = 190"
        )


        db.execSQL(
            "DELETE FROM text_questions WHERE id BETWEEN 191 AND 207"
        )


        for (question in audioQuestions) {
            db.execSQL(
                """INSERT OR IGNORE INTO audio_questions
                | (id, audio_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.audioEntryName}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }

        for (question in videoQuestions) {
            db.execSQL(
                """INSERT OR IGNORE INTO video_questions    
                | (id, video_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.videoEntryName}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }


        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 64 WHERE id = 1"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 68 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 99 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 98 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 111 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 118 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 182 WHERE id = 7"
        )


        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 1450 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 2400 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 4950 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 5750 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 7700 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 9700 WHERE id = 7"
        )
    }

    val audioQuestions = listOf(
        AudioQuestion(
            197,
            "intro_what_am_i_to_you",
            "Heartbeat", "Intro: Boy Meets Evil",
            "RUN BTS", "Intro: What Am I to You?",
            4, 500,
        ),
        AudioQuestion(
            198,
            "jimin_fast_x_angel_pt_1",
            "FAST X | Angel Pt. 1 – NLE Choppa, Kodak Black, Jimin of BTS, JVKE, & Muni Long",
            "Jimin 'Who'",
            "Let Go",
            "Jimin x Ha Sung-woon 'With You'",
            1,
            350,
        ),
        AudioQuestion(
            199,
            "jimin_ha_sung_woon_with_you",
            "Jimin 'Closer Than This'", "Jimin x Ha Sung-woon 'With You'",
            "Waste It On Me", "Lauv - Who (feat. BTS)",
            2, 350,
        ),
        AudioQuestion(
            200,
            "jimin_promise",
            "Jimin 'Smeraldo Garden Marching Band (feat. Loco)'", "Jimin 'Face-off'",
            "Jimin 'Promise'", "'학교의눈물' (School of Tears)",
            3, 600,
        ),
        AudioQuestion(
            201,
            "jimin_smeraldo_garden_marching_band",
            "Love Is Not Over", "Jimin 'Filter'",
            "Jimin 'Alone'", "Jimin 'Smeraldo Garden Marching Band (feat. Loco)'",
            4, 600,
        ),
        AudioQuestion(
            202,
            "jimin_who",
            "Jimin 'Who'",
            "Dream Glow",
            "FAST X | Angel Pt. 1 – NLE Choppa, Kodak Black, Jimin of BTS, JVKE, & Muni Long",
            "Lights",
            1,
            350,
        ),
        AudioQuestion(
            203,
            "lauv_bts_who",
            "V 'FRI(END)S'", "Lauv - Who (feat. BTS)",
            "Jung Kook 'Seven (feat. Latto)'", "So What",
            2, 300,
        ),
        AudioQuestion(
            204,
            "megan_thee_stallion_rm_neva_play",
            "RM 'Yun ft. Erykah Badu'", "RM '들꽃놀이 (Wild Flower) (with 조유진)'",
            "Megan Thee Stallion x RM 'Neva Play'", "RM 'Domodachi (feat. Little Simz)'",
            3, 350,
        ),
        AudioQuestion(
            205,
            "outro_do_you_think_it_makes_sense",
            "Outro: Propose", "Beautiful",
            "Interlude : Shadow", "Outro: Do You Think It Makes Sense?",
            4, 600,
        ),
        AudioQuestion(
            206,
            "rm_domodachi",
            "RM 'Domodachi (feat. Little Simz)'", "RM 'Yun ft. Erykah Badu'",
            "RM 'Groin'", "RM 'No.2 ft. Park Ji Yoon'",
            1, 600,
        ),
        AudioQuestion(
            207,
            "rm_groin",
            "RM 'LOST!'", "RM 'Groin'",
            "Rain", "24/7=Heaven",
            2, 600,
        ),
        AudioQuestion(
            208,
            "rm_lost",
            "Lights", "RM 'Still Life'",
            "RM 'LOST!'", "Mama",
            3, 350,
        ),
        AudioQuestion(
            209,
            "run_bts",
            "Outro: Do You Think It Makes Sense?", "On",
            "Intro: What Am I to You?", "RUN BTS",
            4, 500,
        ),
        AudioQuestion(
            210,
            "suga_juice_wrld_girl_of_my_dreams",
            "Juice WRLD - Girl Of My Dreams (with Suga from BTS)", "Agust D '해금' (Haegeum)",
            "All Night (feat. Juice WRLD)", "Moon",
            1, 600,
        ),
        AudioQuestion(
            211,
            "agust_d_the_last",
            "Agust D '대취타' (Daechwita)", "Agust D 'The Last'",
            "Agust D 'Agust D'", "Agust D 'Give It To Me'",
            2, 550,
        ),
        AudioQuestion(
            212,
            "suga_iu_eight",
            "Crush (크러쉬) - 'Rush Hour (Feat. j-hope of BTS)'", "Suga x Halsey 'SUGA's Interlude'",
            "IU '에잇 (eight) (feat. SUGA)'", "Stigma",
            3, 600,
        ),
        AudioQuestion(
            213,
            "suga_halsey_sugas_interlude",
            "IU '에잇 (eight) (feat. SUGA)'", "Agust D 'AMYGDALA'",
            "Stigma", "Suga x Halsey 'SUGA's Interlude'",
            4, 550,
        ),
        AudioQuestion(
            214,
            "jungkook_still_with_you",
            "Jung Kook 'Still With You'", "Jung Kook feat. Major Lazer 'Closer to You'",
            "Jung Kook 'My You' ", "Jung Kook 'Hate You'",
            1, 600,
        ),
        AudioQuestion(
            215,
            "jungkook_charlie_puth_left_and_right",
            "The Kid LAROI, Jung Kook, Central Cee - TOO MUCH",
            "Charlie Puth - Left And Right (feat. Jung Kook of BTS)",
            "Jung Kook 'Too Sad to Dance'",
            "Jung Kook 'Shot Glass of Tears'",
            2,
            300,
        ),
        AudioQuestion(
            216,
            "jungkook_shot_glass_of_tears",
            "Charlie Puth - Left And Right (feat. Jung Kook of BTS)", "Jung Kook 'Hate You'",
            "Jung Kook 'Shot Glass of Tears'", "Jung Kook 'Never Let Go'",
            3, 350,
        ),
        AudioQuestion(
            217,
            "jungkook_the_kid_laroi_central_cee_too_much",
            "Jung Kook 'Still With You'", "Jung Kook feat. Major Lazer 'Closer to You'",
            "Jung Kook 'Please Don't Change' ", "The Kid LAROI, Jung Kook, Central Cee - TOO MUCH",
            4, 300,
        ),
        AudioQuestion(
            218,
            "jungkook_too_sad_to_dance",
            "Jung Kook 'Too Sad to Dance'", "Jung Kook 'Standing Next to You'",
            "Jung Kook 'Never Let Go'", "Jung Kook 'Still With You'",
            1, 300,
        ),
        AudioQuestion(
            219,
            "jungkook_never_let_go",
            "Jung Kook '3D (feat. Jack Harlow)' ", "Jung Kook 'Never Let Go'",
            "Jung Kook 'My You' ", "Jung Kook 'Somebody'",
            2, 300,
        ),
        AudioQuestion(
            220,
            "jungkook_my_you",
            "Jung Kook 'Too Sad to Dance'", "Jung Kook 'Yes or No'",
            "Jung Kook 'My You' ", "Jung Kook 'Seven (feat. Latto)'",
            3, 450,
        ),
        AudioQuestion(
            221,
            "v_winter_bear",
            "V 'Love Me Again'", "V 'Sweet Night'",
            "V 'Scenery'", "V 'Winter Bear'",
            4, 350,
        ),
        AudioQuestion(
            222,
            "v_sweet_night",
            "V 'Sweet Night'", "V 'Slow Dancing'",
            "V 'For Us'", "V 'Winter Bear'",
            1, 450,
        ),
        AudioQuestion(
            223,
            "rm_v_four_o_clock",
            "RM 'Lonely'", "RM x V '네시 (4 O'CLOCK)'",
            "V 'Rainy Days'", "RM 'I wanna be a human'",
            2, 600,
        ),
        AudioQuestion(
            224,
            "v_scenery",
            "V 'For Us'", "V 'Sweet Night'",
            "V 'Scenery'", "V 'Christmas Tree'",
            3, 550,
        ),
        AudioQuestion(
            225,
            "v_christmas_tree",
            "V 'FRI(END)S'", "V 'Scenery'",
            "V 'Blue'", "V 'Christmas Tree'",
            4, 550,
        ),
        AudioQuestion(
            226,
            "jhope_daydream",
            "J-Hope 'Daydream'", "A Brand New Day",
            "J-Hope '항상 (HANGSANG)'", "J-Hope 'NEURON (with 개코, 윤미래)",
            1, 300,
        ),
        AudioQuestion(
            227,
            "jhope_chicken_noodle_soup",
            "J-Hope 'Daydream'", "J-Hope 'Chicken Noodle Soup (feat. Becky G)'",
            "J-Hope 'MORE'", "J-Hope 'Hope World'",
            2, 300,
        ),
        AudioQuestion(
            228,
            "jhope_hangsang",
            "J-Hope 'NEURON (with 개코, 윤미래)", "J-Hope '방화 (Arson)'",
            "J-Hope '항상 (HANGSANG)'", "J-Hope 'Future'",
            3, 350,
        ),
        AudioQuestion(
            229,
            "jhope_pop",
            "J-Hope '항상 (HANGSANG)'", "J-Hope 'Chicken Noodle Soup (feat. Becky G)'",
            "Born Singer", "J-Hope 'P.O.P (Piece of Peace) Pt.1'",
            4, 350,
        ),
        AudioQuestion(
            230,
            "jhope_airplane",
            "J-Hope 'Airplane'", "J-Hope 'Future'",
            "J-Hope 'Hope World'", "Crush (크러쉬) - 'Rush Hour (Feat. j-hope of BTS)'",
            1, 300,
        ),
        AudioQuestion(
            231,
            "jhope_hope_world",
            "J-Hope '방화 (Arson)'", "J-Hope 'Hope World'",
            "J-Hope 'on the street (with J. Cole)'", "J-Hope 'P.O.P (Piece of Peace) Pt.1'",
            2, 300,
        ),
        AudioQuestion(
            232,
            "jhope_base_line",
            "Crush (크러쉬) - 'Rush Hour (Feat. j-hope of BTS)", "J-Hope 'Airplane'",
            "J-Hope 'Base Line'", "With Seoul",
            3, 300,
        ),
        AudioQuestion(
            233,
            "jhope_future",
            "Juice WRLD - Girl Of My Dreams (with Suga from BTS)", "Reflection",
            "Outro: Tear", "J-Hope 'Future'",
            4, 300,
        ),
        AudioQuestion(
            234,
            "jhope_crush_rush_hour",
            "Crush (크러쉬) - 'Rush Hour (Feat. j-hope of BTS)'", "J-Hope 'Base Line'",
            "J-Hope 'on the street (with J. Cole)'", "J-Hope 'NEURON (with 개코, 윤미래)",
            1, 600,
        ),
        AudioQuestion(
            235,
            "rm_wale_change",
            "Megan Thee Stallion x RM 'Neva Play'", "RM x Wale 'Change'",
            "RM 'LOST!'", "RM x V '네시 (4 O'CLOCK)'",
            2, 550,
        ),
        AudioQuestion(
            236,
            "rm_tokyo",
            "RM 'Yun ft. Erykah Badu'", "RM 'Nuts'",
            "RM 'tokyo'", "RM 'moonchild'",
            3, 350,
        ),
        AudioQuestion(
            237,
            "rm_nuts",
            "RM 'I wanna be a human'", "RM 'Groin'",
            "RM x Wale 'Change'", "RM 'Nuts'",
            4, 600,
        ),
        AudioQuestion(
            238,
            "rm_moonchild",
            "RM 'moonchild'", "Miss Right",
            "RM 'tokyo'", "With Seoul",
            1, 350,
        ),
        AudioQuestion(
            239,
            "agust_d_give_it_to_me",
            "Agust D 'The Last'",
            "Agust D 'Give It To Me'",
            "Juice WRLD - Girl Of My Dreams (with Suga from BTS)",
            "Suga x Halsey 'SUGA's Interlude'",
            2,
            300,
        ),
    )

    val videoQuestions = listOf(
        VideoQuestion(
            54,
            "rm_lost_video",
            "RM 'LOST!'", "RM 'I wanna be a human'",
            "RM 'Groin'", "RM x Wale 'Change'",
            1, 550,
        ),
        VideoQuestion(
            55,
            "rm_groin_video",
            "RM 'Come back to me'", "RM 'Groin'",
            "RM x Wale 'Change'", "RM 'Lonely'",
            2, 600,
        ),
        VideoQuestion(
            56,
            "jimin_smeraldo_garden_marching_band_video",
            "Jimin 'Like Crazy'", "Intro: Boy Meets Evil",
            "Jimin 'Smeraldo Garden Marching Band (feat. Loco)'", "Jimin 'Who'",
            3, 550,
        ),
        VideoQuestion(
            57,
            "jimin_who_video",
            "FAST X | Angel Pt. 1 – NLE Choppa, Kodak Black, Jimin of BTS, JVKE, & Muni Long",
            "Butter",
            "Jimin 'Closer Than This'",
            "Jimin 'Who'",
            4,
            600,
        ),
        VideoQuestion(
            58,
            "jimin_fast_x_angel_pt_1_video",
            "FAST X | Angel Pt. 1 – NLE Choppa, Kodak Black, Jimin of BTS, JVKE, & Muni Long",
            "Boy With Luv",
            "Jimin 'Smeraldo Garden Marching Band (feat. Loco)'",
            "Outro: Ego",
            1,
            500,
        ),
        VideoQuestion(
            59,
            "jungkook_charlie_puth_left_and_right_video",
            "Jung Kook 'Standing Next to You'",
            "Charlie Puth - Left And Right (feat. Jung Kook of BTS)",
            "Jung Kook '3D (feat. Jack Harlow)'",
            "The Kid LAROI, Jung Kook, Central Cee - TOO MUCH",
            2,
            500,
        ),
        VideoQuestion(
            60,
            "jungkook_the_kid_laroi_central_cee_too_much_video",
            "Jung Kook 'Seven (feat. Latto)'",
            "Charlie Puth - Left And Right (feat. Jung Kook of BTS)",
            "The Kid LAROI, Jung Kook, Central Cee - TOO MUCH",
            "Jung Kook 'Hate You'",
            3,
            550,
        ),
        VideoQuestion(
            61,
            "v_winter_bear_video",
            "V 'FRI(END)S'", "V 'Rainy Days'",
            "Dope", "V 'Winter Bear'",
            4, 550,
        ),
        VideoQuestion(
            62,
            "jhope_daydream_video",
            "J-Hope 'Daydream'", "J-Hope 'Airplane'",
            "Jin '슈퍼 참치 (Super Tuna)'", "J-Hope 'on the street (with J. Cole)'",
            1, 550,
        ),
        VideoQuestion(
            63,
            "jhope_chicken_noodle_soup_video",
            "Crush (크러쉬) - 'Rush Hour (Feat. j-hope of BTS)'",
            "J-Hope 'Chicken Noodle Soup (feat. Becky G)'",
            "J-Hope '방화 (Arson)'",
            "PERSONA",
            2,
            500,
        ),
        VideoQuestion(
            64,
            "jhope_airplane_video",
            "For You", "Lights",
            "J-Hope 'Airplane'", "J-Hope 'Chicken Noodle Soup (feat. Becky G)'",
            3, 500,
        ),
        VideoQuestion(
            65,
            "jhope_crush_rush_hour_video",
            "J-Hope 'MORE'", "Epilogue: Young Forever",
            "J-Hope 'Daydream'", "Crush (크러쉬) - 'Rush Hour (Feat. j-hope of BTS)'",
            4, 500,
        ),
        VideoQuestion(
            66,
            "rm_wale_change_video",
            "RM x Wale 'Change'", "RM 'Groin'",
            "RM 'I wanna be a human'", "RM 'LOST!'",
            1, 500,
        ),
        VideoQuestion(
            67,
            "agust_d_give_it_to_me_video",
            "Agust D 'AMYGDALA'", "Agust D 'Give It To Me'",
            "어른아이 (Adult Child)", "N.O (방탄소년단)",
            2, 600,
        ),
    )
}
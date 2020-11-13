@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.util.*

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

class Time(input: String) : Comparable<Time> {
    private val numberTime: Int
        get() {
            val textTime = this.textTime
            if (!Regex("""(\d\d:){2}\d\d [A,P]M""").matches(textTime)) throw IllegalArgumentException()
            val list = textTime.split(Regex("""[: ]"""))
            var day = 0
            if (textTime.contains("PM")) day += 12
            if (list.first() == "12") day -= 12
            return (list[0].toInt() + day) * 3600 + list[1].toInt() * 60 + list[2].toInt()
        }

    private val textTime = input

    override fun compareTo(other: Time): Int {
        return this.numberTime.compareTo(other.numberTime)
    }

    override fun toString(): String {
        return textTime
    }
}

//Трудоемкость алгоритма - O(N^2)
//Ресурсоемкость - O(N)

fun sortTimes(inputName: String, outputName: String) {
    val output = File(outputName).outputStream().bufferedWriter()
    val list = File(inputName).readLines().map { Time(it) }.toMutableList()
    insertionSort(list)
    for (time in list) {
        output.write(time.toString())
        output.newLine()
    }
    output.close()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

//Трудоемкость - O(N log(N))
//Ресурсоемкость - O(N)

fun sortAddresses(inputName: String, outputName: String) {
    val output = File(outputName).bufferedWriter()
    val list = File(inputName).readLines()
    val reg = Regex("""[\wА-Яа-яЁё]+ [\wА-Яа-яЁё]+ - [\wА-Яа-яЁё-]+ \d+""")
    val map = mutableMapOf<String, SortedSet<String>>()
    for (i in list) {
        if (!reg.matches(i)) throw java.lang.IllegalArgumentException()
        val info = i.split(" - ").filter { it != "" }
        val name = info[0]
        val addr = info[1]
        if (!map.containsKey(addr)) {
            val set = sortedSetOf(name)
            map[addr] = set
        } else map.getValue(addr).add(name)
    }
    val listAddr = map.keys.sortedWith(compareBy<String> { it.split(" ")[0] }.thenBy { it.split(" ")[1].toInt() })
    for (addr in listAddr) {
        val names = map[addr]!!.toList().joinToString(", ")
        val str = "$addr - $names"
        output.write(str)
        output.newLine()
    }
    output.close()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */

//Трудоемкость алгоритм - O(N + 7730)
//Ресурсоемкость - O(N)

fun sortTemperatures(inputName: String, outputName: String) {
    val output = File(outputName).bufferedWriter()
    val list = File(inputName).readLines()
    val limit = 7730
    val listTemp = list.map { (it.toDouble() * 10).toInt() + 2730 }
    if (listTemp.any { it !in 0..7730 }) throw IllegalArgumentException()
    val elements = listTemp.toIntArray()
    val out = countingSort(elements, limit)
    val str = out.toList().joinToString("\n") { ((it - 2730).toDouble() / 10).toString() }
    output.write(str)
    output.close()
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}


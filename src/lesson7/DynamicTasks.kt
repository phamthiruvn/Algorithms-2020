@file:Suppress("UNUSED_PARAMETER")

package lesson7


/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
//Трудоемкость алгоритм - O(F.length*S.leghth)
//Ресурсоемкость - O(F.length*S.leghth)
fun longestCommonSubSequence(first: String, second: String): String {
    var m = first.length
    var n = second.length
    val sb = StringBuilder()
    val values = Array(m + 1) { IntArray(n + 1) }
    for (i in 0..m) {
        for (j in 0..n) {
            values[i][j] = if (i == 0 || j == 0) 0
            else if (first[i - 1] == second[j - 1]) values[i - 1][j - 1] + 1
            else maxOf(values[i][j - 1], values[i - 1][j])
        }
    }
    while (m > 0 && n > 0) {
        when {
            first[m - 1] == second[n - 1] -> sb.append(first[m - 1])
            values[m - 1][n] > values[m][n - 1] -> n++
            else -> m++
        }
        m--
        n--
    }
    return sb.toString().reversed()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
//Трудоемкость алгоритм - O(N*N)
//Ресурсоемкость - O(N)
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    if (list.size <= 1) return list
    val max = Array(list.size) { 0 }
    val trace = Array(list.size) { 0 }
    val result = mutableListOf<Int>()
    var maxLength = 0
    for (i in list.indices) {
        max[i] = 1
        trace[i] = -1
        for (j in 0 until i) {
            if (list[j] < list[i] && max[j] + 1 > max[i]) {
                max[i]++
                trace[i] = j
            }
        }
        if (max[i] > maxLength) maxLength = max[i]
    }
    var maxIndex = max.indexOfFirst { it == maxLength }
    while (maxIndex != -1) {
        result.add(0, list[maxIndex])
        maxIndex = trace[maxIndex]
    }
    return result
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5
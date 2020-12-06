@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson6

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    TODO()
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    TODO()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
//Трудоемкость алгоритм - O(V+E)
//Ресурсоемкость - O(V)
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    val vertices = this.vertices
    if (vertices.isEmpty()) return setOf()
    if (this.withCycle()) throw  IllegalArgumentException()
    val all = mutableSetOf<Set<Graph.Vertex>>()
    for (vertex in this.vertices) {
        val set = mutableSetOf<Graph.Vertex>()
        val skip = mutableSetOf<Graph.Vertex>()
        vertices.stream().filter { next ->
            !this.getNeighbors(vertex).contains(next) && !skip.contains(next)
        }.forEach { next ->
            skip.addAll(this.getNeighbors(next))
            set.add(next)
        }
        all.add(set)
    }
    return all.maxByOrNull { it.size } ?: setOf()
}


//* Если на входе граф с циклами, бросить true
//Трудоемкость алгоритм - O(V+E)
//Ресурсоемкость - O(V)
fun Graph.withCycle(
    vertex: Graph.Vertex = this.vertices.first(),
    visited: MutableMap<Graph.Vertex, Boolean> = this.vertices.map { Pair(it, false) }.toMap().toMutableMap(),
    parent: MutableMap<Graph.Vertex, Graph.Vertex> = mutableMapOf()
): Boolean {
    visited[vertex] = true
    for (child in this.getNeighbors(vertex)) {
        if (!visited[child]!!) {
            parent[child] = vertex
            withCycle(child, visited, parent)
        } else if (parent[vertex] != child) {
            return true
        }
    }
    return false

}


/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
//Трудоемкость алгоритм - O(V+E)
//Ресурсоемкость - O(V)
fun Graph.longestSimplePath(): Path {
    val queue = vertices.map { Path(it) }.toMutableSet()
    val all = mutableSetOf<Path>()
    while (queue.isNotEmpty()) {
        val path = queue.last()
        queue.remove(path)
        this.getNeighbors(path.vertices.last()).map { next -> Path(path, this, next) }.forEach { new ->
            if (new.vertices.size != new.vertices.toSet().size) all.add(path)
            else queue.add(new)
        }
    }
    return all.maxByOrNull { it.length } ?: Path()
}

/**
 * Балда
 * Сложная
 *
 * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
 * поэтому задача присутствует в этом разделе
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    TODO()
}

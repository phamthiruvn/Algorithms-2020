package lesson4

import java.util.*


/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: MutableMap<Char, Node> = linkedMapOf()
    }

    private var root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator() = TrieIterator()

    inner class TrieIterator internal constructor() : MutableIterator<String> {
        private var next: String? = null
        private val sb = StringBuilder()
        private val q: Deque<Iterator<Map.Entry<Char, Node>>> = ArrayDeque()

        init {
            q.push(root.children.entries.iterator())
            findNext()
        }

        private fun findNext() {
            next = null
            var iterator = q.peek()
            var isWord = false
            while (iterator != null && !isWord) {
                while (iterator.hasNext() && !isWord) {
                    val e = iterator.next()
                    val key = e.key
                    isWord = key == 0.toChar()
                    if (isWord) next = sb.toString()
                    sb.append(key)
                    val node = e.value
                    iterator = node.children.entries.iterator()
                    q.push(iterator)
                }
                q.pop()
                if (sb.isNotEmpty()) sb.deleteCharAt(sb.length - 1)
                iterator = q.peek()
            }
        }


        override fun hasNext() = next != null

        override fun next(): String {
            if (!hasNext()) throw IllegalStateException()
            val result = next!!
            findNext()
            return result
        }

        override fun remove() {
            next = null
            size--
        }
    }
}
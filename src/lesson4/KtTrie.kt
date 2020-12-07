package lesson4

import java.util.*


/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    class Node {
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
        private var current: String? = null
        private val queue = LinkedList<String>()

        init {
            toQueue(root, 0, StringBuilder())
        }

        //Трудоемкость алгоритм - O(Ls) - Ls-total length of the words
        //Ресурсоемкость - O(L) - L-length of the longest word
        private fun toQueue(node: Node, level: Int, sb: StringBuilder) {
            val children = node.children
            val characters = children.keys
            for (character in characters) {
                if (character == 0.toChar()) queue.add(sb.toString())
                sb.insert(level, character)
                children[character]?.let { toQueue(it, level + 1, sb) }
                sb.deleteCharAt(level)
            }
        }

        //Трудоемкость алгоритм - O(1)
        //Ресурсоемкость - O(1)
        override fun hasNext(): Boolean = queue.peek() != null

        //Трудоемкость алгоритм - O(1)
        //Ресурсоемкость - O(1)
        override fun next(): String {
            if (!hasNext()) throw NoSuchElementException()
            current = queue.remove()
            return current as String
        }

        //Трудоемкость алгоритм - O(1)
        //Ресурсоемкость - O(1)
        override fun remove() {
            if (current == null) throw IllegalStateException()
            check(remove(current))
            current = null
        }
    }

}
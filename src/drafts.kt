import lesson3.KtBinarySearchTree
import lesson6.impl.GraphBuilder
import lesson6.withCycle


fun main() {


    val new = KtBinarySearchTree<Int>()
    val x = setOf(0, 1, 2)
    val y = setOf(0, 3, 4)
    val graph = GraphBuilder().apply {
        val a = addVertex("A")
        val b = addVertex("B")
        val c = addVertex("C")
        val d = addVertex("D")
        val e = addVertex("E")
        addConnection(a, b)
    }.build()
    println(graph.withCycle())


}
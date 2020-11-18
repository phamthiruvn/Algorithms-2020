import lesson3.KtBinarySearchTree

fun main() {


    val new = KtBinarySearchTree<Int>()
    new.add(1)
    new.add(2)
    new.add(3)
    new.add(4)
    new.add(5)
    new.add(6)
    val check = new
    println(check.add(5))
}
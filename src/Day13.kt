fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13-TestInput")
    check(part1(testInput) == 0)
//    check(part2(testInput) == 0)

    val input = readInput("Day13-Input")
    println(part1(input))
    println(part2(input))
}

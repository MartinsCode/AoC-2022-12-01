fun main() {
    fun prioritize(a: Char): Int {
        return when (a) {
            in 'a'..'z' -> a.code - 'a'.code + 1
            in 'A'..'Z' -> a.code - 'A'.code + 27
            else -> 0
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val compartment1 = it.substring(0, it.length / 2).toCharArray()
            val compartment2 = it.substring(it.length / 2).toCharArray()
            // println("${compartment1.joinToString()}  -  ${compartment2.joinToString()}")
            val items1 = compartment1.distinct()
            items1.forEach {
                if (compartment2.contains(it)) {
                    // println("$it  ${prioritize(it)}")
                    sum += prioritize(it)
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (i in input.indices step 3) {
            val rucksack1 = input[i].toCharArray().toMutableList()
            var badge = ' '
            rucksack1.forEach {
                if ((it in input[i + 1]) && (it in input[i + 2])) {
                    badge = it
                }
            }
            assert (rucksack1.size == 1)
            sum += prioritize(badge)
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03-TestInput")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03-Input")
    println(part1(input))
    println(part2(input))
}

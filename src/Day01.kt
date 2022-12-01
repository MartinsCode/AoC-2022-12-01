fun main() {
    fun part1(input: List<String>): Int {
        // we have an undetermined number of elves.
        // with an undetermined number of input line
        // So in pseudocode:
        // Read each line
        // if empty: switch to next elf
        // else add number to same elf
        val calPerElf: MutableList<Int> = mutableListOf(0)
        input.forEach() {
            if (it == "") {
                calPerElf.add(0)
            } else {
                calPerElf[calPerElf.lastIndex] += it.toInt()
            }
        }
        return calPerElf.max()
    }

    fun part2(input: List<String>): Int {
        // Sum of Top 3:
        val calPerElf: MutableList<Int> = mutableListOf(0)
        input.forEach() {
            if (it == "") {
                calPerElf.add(0)
            } else {
                calPerElf[calPerElf.lastIndex] += it.toInt()
            }
        }
        calPerElf.sort()
        val last = calPerElf.lastIndex
        return calPerElf[last] + calPerElf[last - 1] + calPerElf[last - 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01-TestInput")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01-Calories")
    println(part1(input))
    println(part2(input))
}

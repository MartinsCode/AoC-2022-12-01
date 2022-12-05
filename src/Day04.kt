fun main() {
    fun part1(input: List<String>): Int {
        var numberFullyContained = 0
        input.forEach {
            val strCleaning1 = it.split(",")[0]
            val strCleaning2 = it.split(",")[1]
            var clean1 = strCleaning1.split("-").map { it.toInt() }
            var clean2 = strCleaning2.split("-").map { it.toInt() }
            // swap so that clean 1 always starts with lower or equal number
            if (clean2[0] < clean1[0]) {
                val temp = clean1
                clean1 = clean2
                clean2 = temp
            }

            assert(clean1[0] < clean1[1])
            assert(clean2[0] < clean2[1])

            assert(clean1[0] <= clean2[0])

            if (clean2[1] <= clean1[1]) {
                numberFullyContained++
            } else if (clean1[0] == clean2[0]){
                numberFullyContained++
            }
        }
        return numberFullyContained
    }

    fun part2(input: List<String>): Int {
        var overlapping = 0
        input.forEach {
            val strCleaning1 = it.split(",")[0]
            val strCleaning2 = it.split(",")[1]
            val clean1 = strCleaning1.split("-").map { it.toInt() }
            val clean2 = strCleaning2.split("-").map { it.toInt() }

            val sections = mutableSetOf<Int>()
            for (i in clean1[0]..clean1[1]) {
                sections.add((i))
            }
            for (i in clean2[0]..clean2[1]) {
                sections.add(i)
            }
            if (clean1[1] - clean1[0] + 1 + clean2[1] - clean2[0] + 1 > sections.size) {
                overlapping++
            }
        }
        println(overlapping)
        return overlapping
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04-TestInput")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04-Input")
    println(part1(input))
    println(part2(input))
}

class Mixer(val key: Long = 1L) {
    // a list of pairs <number, original position>
    val sequence = mutableListOf<Pair<Long, Int>>()

    fun input(inp: List<String>) {
        var counter = 0
        inp.forEach {
            check(it.length <= 5) {"Input line >$it< is longer than 5 characters!"}  // still in Int range?
            sequence.add(Pair(it.toLong() * key, counter))
            counter++
        }
    }

    fun doOneRound() {
        for (i in 0..sequence.lastIndex) {
            val indexedElement = sequence.find { it.second == i }!!
            val indexedPos = sequence.indexOf(indexedElement)
            sequence.removeAt(indexedPos)
            var newPos: Long = indexedPos + indexedElement.first
            newPos = newPos.mod(sequence.size.toLong())
            sequence.add(newPos.toInt(), indexedElement)
        }
    }

    fun coords(): Long {
        val startElem = sequence.find { it.first == 0L }
        val startPos = sequence.indexOf(startElem)
        // 1000th after startPos
        val offset = 1000
        val off1 = (startPos + offset) % sequence.size
        val off2 = (off1 + offset) % sequence.size
        val off3 = (off2 + offset) % sequence.size
        val sum = sequence[off1].first + sequence[off2].first + sequence[off3].first
        return sum
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val mix = Mixer()
        mix.input(input)
        mix.doOneRound()
        return mix.coords()
    }

    fun part2(input: List<String>): Long {
        val mix = Mixer(811_589_153L)
        mix.input(input)
        repeat(10) { mix.doOneRound() }
        return mix.coords()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20-TestInput")
    check(part1(testInput) == 3L)
    check(part2(testInput) == 1623178306L)

    val input = readInput("Day20-Input")
    check(part1(input) < 9756)
    println(part1(input))
    println(part2(input))
}

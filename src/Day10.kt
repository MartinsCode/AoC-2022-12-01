class Machine {
    private var cycle = 0
    private var strength = 1
    var addedSignal = 0
    var display = mutableListOf("........................................",
        "........................................",
        "........................................",
        "........................................",
        "........................................",
        "........................................")
    private var coords = Pair(0, 0)  // Coords of Ray

    private fun calculateCoords() {
        val y = (cycle - 1) / 40
        val x = ((cycle - 1)  % 40)
        coords = Pair(x, y)
    }

    private fun spriteIsVisible(): Boolean {
        return (coords.first >= strength - 1 && coords.first <= strength + 1)
    }

    private fun addCycle() {
        cycle++
        if (cycle in arrayOf(20, 60, 100, 140, 180, 220)) {
            addedSignal += strength * cycle
        }
        calculateCoords()
        if (spriteIsVisible()) {
            val lineArray = display[coords.second].toCharArray()
            lineArray[coords.first] = '#'
            display[coords.second] = String(lineArray)
        }
    }

    fun parse (line: String) {
        when (line) {
            "noop" -> addCycle()
            else -> { // addx
                addCycle()
                addCycle()
                strength += line.split(" ")[1].toInt()
            }
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val machine = Machine()
        input.forEach {
            machine.parse(it)
        }
        return machine.addedSignal
    }

    fun part2(input: List<String>): MutableList<String> {
        val machine = Machine()
        input.forEach {
            machine.parse(it)
        }
//        println(machine.display.joinToString("\n"))
        return machine.display
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10-TestInput")
    println(part1(testInput))
    check(part1(testInput) == 13140)
    check(part2(testInput) == mutableListOf("##..##..##..##..##..##..##..##..##..##..",
            "###...###...###...###...###...###...###.",
            "####....####....####....####....####....",
            "#####.....#####.....#####.....#####.....",
            "######......######......######......####",
            "#######.......#######.......#######....."))

    val input = readInput("Day10-Input")
    println(part1(input))
    println(part2(input).joinToString("\n"))
}


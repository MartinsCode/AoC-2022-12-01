
class Cave {
    val map = mutableListOf(" ".repeat(600)) // first line is all air (until fixed). And no use of more than 500 letters. Can be fixed later
    private var sandCounter = 0 // How many portions of sand did we fill in?
    private var floored = false

    /**
     * Ensure lengths of each mapline.
     */
    private fun extendMapX(x: Int) {
        if (x > map[0].lastIndex) {
            map.forEachIndexed{ index, s ->
                if (index == map.lastIndex && floored) {
                    map[index] = s + "F".repeat(x + map[index].lastIndex)
                } else {
                    map[index] = s + " ".repeat(x + map[index].lastIndex)
                }
            }
        }
    }

    /**
     * Ensure lengths of map (in depth).
     */
    private fun extendMapY(y: Int) {
        if (y > map.lastIndex) {
            for (i in map.lastIndex .. y) {
                map.add(" ".repeat(map[0].length))
            }
        }
    }

    private fun setRock(x: Int, y: Int) {
        // map[y] is line to be modified
        map[y] = map[y].substring(0, x) + "#" + map[y].substring(x + 1, map[y].length)
    }

    private fun drawSand(coord: Pair<Int, Int>) {
        val x = coord.first
        val y = coord.second
        // map[y] is line to be modified
        map[y] = map[y].substring(0, x) + "o" + map[y].substring(x + 1, map[y].length)
    }

    private fun drawLine(beginString: String, endString: String) {
        val begin = Pair(beginString.split(",")[0].toInt(), beginString.split(",")[1].toInt())
        val end = Pair(endString.split(",")[0].toInt(), endString.split(",")[1].toInt())

        val max = Pair(maxOf(begin.first, end.first), maxOf(begin.second, end.second))

        if (map.lastIndex < max.second)
            extendMapY(max.second)
        if (map[0].lastIndex < max.first)
            extendMapX(max.first)

        check(begin.first == end.first || begin.second == end.second)

        if (begin.first == end.first) {
            val minY = minOf(begin.second, end.second)
            val maxY = maxOf(begin.second, end.second)
            // draw a horizontal line
            for (y in minY..maxY) {
                setRock(begin.first, y)
            }
        } else {
            // draw a vertical line
            val minX = minOf(begin.first, end.first)
            val maxX = maxOf(begin.first, end.first)
            for (x in minX..maxX) {
                setRock(x, begin.second)
            }
        }
    }

    fun drawRockFormation(line: String) {
        val coords = line.split(" -> ")
        for (i in 0 until coords.lastIndex) {
            drawLine(coords[i], coords[i + 1])
        }
    }

    private fun isEmpty(x: Int, y: Int): Boolean {
        return map[y][x] == ' '
    }

    private fun inBoundaries(coords: Pair<Int, Int>): Boolean {
        extendMapX(coords.first)
        return (coords.first in 0..map[0].lastIndex) && (coords.second in map.indices)
    }

    private fun fallDown(coord: Pair<Int, Int>): Pair<Int, Int> {
        var newCoord = coord
        while (inBoundaries(Pair(newCoord.first, newCoord.second + 1)) && isEmpty(newCoord.first, newCoord.second + 1))
            newCoord = Pair(newCoord.first, newCoord.second + 1)
        if (!inBoundaries(Pair(newCoord.first, newCoord.second + 1))) {
            newCoord = Pair(newCoord.first, newCoord.second + 1)
        }
        return newCoord
    }

    fun floor() {
        map.add("F".repeat(map[0].length))
        floored = true
    }

    fun nextSandComesToRest(): Boolean {
        sandCounter++
        var sandCoord = Pair(500,0)
        var simStopped = false
        var isResting = false
        while (!simStopped) {
            // let it fall straight down
            sandCoord = fallDown(sandCoord)

            // if out of boundaries, stop simulation, isResting = false
            if (!inBoundaries(sandCoord)) {
                simStopped = true
                isResting = false
            } else {
                // sand fell down, is resting somewhere, but ... on top of something?
                // test left -> move there
                //      if this is out of boundaries, stop sim, isResting = false
                if (! inBoundaries(Pair(sandCoord.first - 1, sandCoord.second + 1))) {
                    simStopped = true
                    isResting = false
                } else {
                    if (isEmpty(sandCoord.first - 1, sandCoord.second + 1)) {
                        sandCoord = Pair(sandCoord.first - 1, sandCoord.second + 1)
                    } else {
                        // else test right -> move there
                        //      if out of boundaries, stop sim, isResting = false
                        // We couldn't move left down, so let's test right down
                        if (!inBoundaries(Pair(sandCoord.first + 1, sandCoord.second + 1))) {
                            simStopped = true
                            isResting = false
                        } else {
                            if(isEmpty(sandCoord.first + 1, sandCoord.second + 1)) {
                                sandCoord = Pair(sandCoord.first + 1, sandCoord.second + 1)
                            } else {
                                isResting = true
                                simStopped = true
                                drawSand(sandCoord)
                            }
                        }
                    }
                }

            }
            // else if both do not apply, setSand(sandCoord) and stop simulation, isResting = true
        }
        return isResting
    }

    fun isFull(): Boolean {
        return !isEmpty(500, 0)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val cave = Cave()

        input.forEach {
            cave.drawRockFormation(it)
        }

        var counter = 0
        while (cave.nextSandComesToRest())
            counter++

        return counter
    }

    fun part2(input: List<String>): Int {
        val cave = Cave()

        input.forEach {
            cave.drawRockFormation(it)
        }

        cave.floor()

        var counter = 0
        while (!cave.isFull()) {
            cave.nextSandComesToRest()
            counter++
        }

        return counter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14-TestInput")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14-Input")
    println(part1(input))
    println(part2(input))
}

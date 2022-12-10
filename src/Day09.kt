import kotlin.math.abs

fun main() {
    /**
     * Calculate new position of tails after movement of head.
     */
    fun newPosOfTail(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
        val newtail: Pair<Int, Int>
        // no tail movement, if there is no "big" difference in positions
        if (abs(head.first - tail.first) < 2 && abs(head.second - tail.second) < 2)
            return(tail)
        // alright, we have to move the tail closer:
        // only in a straight line?
        if (head.first == tail.first) {
            newtail = Pair(head.first, (head.second + tail.second) / 2)
            return newtail
        }
        if (head.second == tail.second) {
            newtail = Pair((head.first + tail.first) / 2, head.second)
            return newtail
        }
        // or do we have to move diagonal?
        // one coord is of difference 2, the other of difference 1
        if (abs(head.first - tail.first) > abs(head.second - tail.second)) {
            // first is of bigger difference, so ...
            newtail = Pair((head.first + tail.first) / 2, head.second)
            return newtail
        } else if (abs(head.first - tail.first) < abs(head.second - tail.second)){
            newtail = Pair(head.first, (head.second + tail.second) / 2)
            return newtail
        } else {
            newtail = Pair((head.first + tail.first) / 2, (head.second + tail.second) / 2)
            return newtail
        }
    }

    fun part1(input: List<String>): Int {
        // Moves:
        // Tracking visited coords: Set of Pair (removes duplicates)
        val visitedCoords = mutableSetOf<Pair<Int, Int>>()

        // Calculate start (use 0,0)
        // Head-Coord as Pair
        var head = Pair(0, 0)

        // Tail-Coord as Pair
        var tail = Pair(0, 0)

        // repeat for each line:
        input.forEach {
            val (cmd, times) = it.split(" ")
            // repeat as often as line says:
            for (i in 1..times.toInt()) {
                // Move head one step udlr
                when (cmd) {
                    "U" -> head = Pair(head.first - 1, head.second)
                    "D" -> head = Pair(head.first + 1, head.second)
                    "L" -> head = Pair(head.first, head.second - 1)
                    "R" -> head = Pair(head.first, head.second + 1)
                }
                // calc new pos of tail
                tail = newPosOfTail(head, tail)

                // add tail to set
                visitedCoords.add(tail)
            }
        }
        // return size of set
        return visitedCoords.size
    }

    fun part2(input: List<String>): Int {
        // What if we use an array of size (10)
        // arr[0] is head, getting moved according to rules
        // Then iterate over arr[1..9], each with
        // arr[i] = calculateNewTail(arr[i-1], arr[i], i in 1..9
        // then write down arr[9] as tail

        val visitedCoords = mutableSetOf<Pair<Int, Int>>()

        val rope = Array(10){ Pair(0, 0) }

        // repeat for each line:
        input.forEach {
            val (cmd, times) = it.split(" ")
            // repeat as often as line says:
            for (i in 1..times.toInt()) {
                // Move head one step udlr
                when (cmd) {
                    "U" -> rope[0] = Pair(rope[0].first - 1, rope[0].second)
                    "D" -> rope[0] = Pair(rope[0].first + 1, rope[0].second)
                    "L" -> rope[0] = Pair(rope[0].first, rope[0].second - 1)
                    "R" -> rope[0] = Pair(rope[0].first, rope[0].second + 1)
                }

                // calc new pos of tail
                for (j in 1..9) {
                    rope[j] = newPosOfTail(rope[j - 1], rope[j])
                }

                for (j in 0 until 9) {
                    check(abs(rope[j].first - rope[j + 1].first) < 2)
                    check(abs(rope[j].second - rope[j + 1].second) < 2)
                }

                // add tail to set
                visitedCoords.add(rope[9])
            }
        }
        //
        // return size of set
        return visitedCoords.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09-TestInput")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    val testInput2 = readInput("Day09-TestInput2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09-Input")
    check(part1(input) < 6128)   // Answer was too high
    println(part1(input))
    check(part2(input) < 2561)
    println(part2(input))
}

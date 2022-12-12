fun main() {
    fun find(c: Char, map: List<String>): Pair<Int, Int> {
        var position = Pair(-1, -1)
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == c) {
                    position = Pair(x, y)
                }
            }
        }
        return position
    }

    fun findAll(c: Char, map: List<String>): MutableList<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == c) {
                    result.add(Pair(x, y))
                }
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        // input equals map
        // find S
        val start = find('S', input)
        val end = find('E', input)
        val heightmap = input.toMutableList().map { it.toCharArray() }

        heightmap[start.second][start.first] = 'a'
        heightmap[end.second][end.first] = 'z'

        // then check out, where we can go
        val possibleMoves = ArrayDeque<Pair<Int, Int>>()
        possibleMoves.add(start)

        val distanceMap = Array(heightmap.size) { Array(heightmap[0].size) { Int.MAX_VALUE } }

        distanceMap[start.second][start.first] = 0

        while (possibleMoves.size > 0) {
            println(possibleMoves)
            // get next step
            val step = possibleMoves.removeFirst()
            // work out actual length of path so far
            val distance = distanceMap[step.second][step.first]

            // try all four directions
            // don't go, if - on border - or already visited and distance is shorter or equal
            // also don't go, if height is more than 1 difference up
            // north
            if (step.second != 0 && distance + 1 < distanceMap[step.second - 1][step.first]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second - 1][step.first]) {
                    possibleMoves.add(Pair(step.first, step.second - 1))
                    distanceMap[step.second - 1][step.first] = distance + 1
                }
            }
            // south
            if (step.second != heightmap.lastIndex && distance + 1 < distanceMap[step.second + 1][step.first]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second + 1][step.first]) {
                    possibleMoves.add(Pair(step.first, step.second + 1))
                    distanceMap[step.second + 1][step.first] = distance + 1
                }
            }
            // east
            if (step.first != 0 && distance + 1 < distanceMap[step.second][step.first - 1]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second][step.first - 1]) {
                    possibleMoves.add(Pair(step.first - 1, step.second))
                    distanceMap[step.second][step.first - 1] = distance + 1
                }
            }
            // west
            if (step.first != input[0].lastIndex && distance + 1 < distanceMap[step.second][step.first + 1]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second][step.first + 1]) {
                    possibleMoves.add(Pair(step.first + 1, step.second))
                    distanceMap[step.second][step.first + 1] = distance + 1
                }
            }
        }
        // put possibilities on stack
        // create a map with distances from start
        // revisit places only, if reachable within shorter distance
        // else drop when revisited

        println(distanceMap[end.second][end.first])

        return distanceMap[end.second][end.first]
    }

    fun part2(input: List<String>): Int {
        val start = find('S', input)
        val end = find('E', input)
        val heightmap = input.toMutableList().map { it.toCharArray() }

        heightmap[start.second][start.first] = 'a'
        heightmap[end.second][end.first] = 'z'

        // then check out, where we can go
        val possibleMoves = ArrayDeque<Pair<Int, Int>>()
        possibleMoves.add(start)
        possibleMoves.addAll(findAll('a', input))

        val distanceMap = Array(heightmap.size) { Array(heightmap[0].size) { Int.MAX_VALUE } }

        possibleMoves.forEach {
            distanceMap[it.second][it.first] = 0
        }

        while (possibleMoves.size > 0) {
            println(possibleMoves)
            // get next step
            val step = possibleMoves.removeFirst()
            // work out actual length of path so far
            val distance = distanceMap[step.second][step.first]

            // try all four directions
            // don't go, if - on border - or already visited and distance is shorter or equal
            // also don't go, if height is more than 1 difference up
            // north
            if (step.second != 0 && distance + 1 < distanceMap[step.second - 1][step.first]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second - 1][step.first]) {
                    possibleMoves.add(Pair(step.first, step.second - 1))
                    distanceMap[step.second - 1][step.first] = distance + 1
                }
            }
            // south
            if (step.second != heightmap.lastIndex && distance + 1 < distanceMap[step.second + 1][step.first]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second + 1][step.first]) {
                    possibleMoves.add(Pair(step.first, step.second + 1))
                    distanceMap[step.second + 1][step.first] = distance + 1
                }
            }
            // east
            if (step.first != 0 && distance + 1 < distanceMap[step.second][step.first - 1]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second][step.first - 1]) {
                    possibleMoves.add(Pair(step.first - 1, step.second))
                    distanceMap[step.second][step.first - 1] = distance + 1
                }
            }
            // west
            if (step.first != input[0].lastIndex && distance + 1 < distanceMap[step.second][step.first + 1]) {
                if (heightmap[step.second][step.first].inc() >= heightmap[step.second][step.first + 1]) {
                    possibleMoves.add(Pair(step.first + 1, step.second))
                    distanceMap[step.second][step.first + 1] = distance + 1
                }
            }
        }
        // put possibilities on stack
        // create a map with distances from start
        // revisit places only, if reachable within shorter distance
        // else drop when revisited

        println(distanceMap[end.second][end.first])

        return distanceMap[end.second][end.first]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12-TestInput")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12-Input")
    println(part1(input))
    println(part2(input))
}

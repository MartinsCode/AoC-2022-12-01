import kotlin.math.abs

class BeaconMap {
    /**
     * Value map is map of sensors and beacons.
     *
     * key stores coords of sensor, value is coord of beacon.
     */
    private val map = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    private var minX = Int.MAX_VALUE
    private var minY = Int.MAX_VALUE
    private var maxX = Int.MIN_VALUE
    private var maxY = Int.MIN_VALUE

    // Problem is: bruteforcing all positions is *REALLY* slow für 4_000_000
    // So we could create an array for the line, set what is covered, and then check.
    // would probably also be slow
    // or else we create an array of "covered ranges" per line
    fun coveredRanges(y: Int): List<Pair<Int, Int>> {
        val ranges = mutableListOf<Pair<Int, Int>>()
        map.keys.forEach {
            if (lineTouchedBy(it, y))
                ranges.add(linecoverBy(it, y))
        }
        return ranges
    }

    fun linecoverBy(beacon: Pair<Int, Int>, y:Int): Pair<Int, Int> {
        val range = rangeOf(beacon)
        val min = beacon.first - range + abs(y - beacon.second)
        val max = beacon.first + range - abs(y - beacon.second)
        return Pair(min, max)
    }

    fun lineTouchedBy(beacon: Pair<Int, Int>, y: Int): Boolean {
        val range = rangeOf(beacon)
        return (abs(y - beacon.second) <= range)
    }

    /**
     * Calculate range of certain beacon
     */
    private fun rangeOf(beacon: Pair<Int, Int>): Int {
        val sensor = map[beacon]!!
        return (abs(beacon.first - sensor.first) + abs(beacon.second - sensor.second))
    }

    /**
     * Set range of map coords
     */
    private fun setMapRange() {
        map.keys.forEach {
            println("Beacon $it, sensor ${map[it]}, range ${rangeOf(it)}")
            if (it.first - rangeOf(it) < minX) minX = it.first - rangeOf(it)
            if (it.first + rangeOf(it) > maxX) maxX = it.first + rangeOf(it)
            if (it.second - rangeOf(it) < minY) minY = it.second - rangeOf(it)
            if (it.second + rangeOf(it) > maxY) maxY = it.second + rangeOf(it)
        }
    }

    /**
     * where to input the beacons info
     */
    fun input(lines: List<String>) {
        lines.forEach {
            val pattern = "Sensor at x=(.+), y=(.+): closest beacon is at x=(.+), y=(.+)".toRegex()
            val matches = pattern.findAll(it)
            val match = matches.first()
            map[Pair(match.groupValues[1].toInt(), match.groupValues[2].toInt())] =
                Pair(match.groupValues[3].toInt(), match.groupValues[4].toInt())
        }
        setMapRange()
    }

/*    private fun isCovered(x: Int, y: Int): Boolean {
        var covered = false
        map.forEach { (sensor, beacon) ->
            val range = abs(beacon.first - sensor.first) + abs(beacon.second - sensor.second)
            val distance = abs(x - sensor.first) + abs(y - sensor.second)
            covered = covered || (distance <= range)
        }
        return covered
    } */

    /*
    private fun isCovered(x: Int, y: Int): Boolean {
        var covered = false
        var index = 0
        val sensors = map.keys.sortedBy { it.first }
        while (!covered && index < sensors.size) {
            val sensor = sensors[index]
            val beacon = map[sensor]!!
            val range = abs(beacon.first - sensor.first) + abs(beacon.second - sensor.second)
            val distance = abs(x - sensor.first) + abs(y - sensor.second)
            covered = covered || (distance <= range)
            index++
        }
        return covered
    } */

    fun isCovered(x: Int, y: Int): Boolean {
        var covered = false
        val coveredRanges = coveredRanges(y)

        coveredRanges.forEach {
            covered = covered || (x >= it.first && x <= it.second)
        }
        return covered
    }

    fun isBeacon(x: Int, y: Int): Boolean {
        return map.values.contains(Pair(x, y))
    }

    fun isSensor(x: Int, y: Int): Boolean {
        return  map.keys.contains(Pair(x, y))
    }

    fun checkedPlacesInLine(y: Int): Int {
        // we just bruteforce by now for me not being motivated to create an array and count later on
        var checked = 0
        for (x in minX..maxX) {
            if (isCovered(x, y) && !isBeacon(x, y) && !isSensor(x, y)) {
                checked++
            }
        }
        return checked
    }

    fun tuningFrequency(max: Int): Long {
        var freq = 0L
        for (x in 0..max) {
            for (y in 0..max) {
                if (!isCovered(x, y))
                    freq = x * 4_000_000L + y
            }
            println("$x  $freq")
        }
        return freq
    }

    fun isTotallyCovered(area: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
        var covered = false
        map.keys.forEach {
            val range = rangeOf(it)
            val x1 = area.first.first
            val x2 = area.second.first
            val y1 = area.first.second
            val y2 = area.second.second
            // Well... (x1, y1) has to be covered by the same beacon as (x2, y2)

            val distance1 = abs(x1 - it.first) + abs(y1 - it.second)
            val distance2 = abs(x2 - it.first) + abs(y2 - it.second)
            val distance3 = abs(x1 - it.first) + abs(y2 - it.second)
            val distance4 = abs(x2 - it.first) + abs(y1 - it.second)

            covered = covered || (distance1 <= range && distance2 <= range && distance3 <= range && distance4 <= range)
        }
        return covered
    }

    fun frequency(max: Int): Long {
        // new approach:
        // create a square, look, if totally covered.
        // if not, divide into four quarters, repeat
        var counter = 0
        var x = Int.MIN_VALUE
        var y = Int.MIN_VALUE
        val areas = mutableListOf(Pair(Pair(0, 0), Pair(max, max)))
        while (areas.size > 0) {
            val area = areas.removeFirst()
            if (area.first.first == area.second.first && area.first.second == area.second.second) {
                if (!isCovered(area.first.first, area.first.second)) {
                    x = area.first.first
                    y = area.first.second
                }
            } else if (!isTotallyCovered(area)) {
                val x1 = area.first.first
                val x3 = area.second.first
                val x2 = (x1 + x3)/2
                val y1 = area.first.second
                val y3 = area.second.second
                val y2 = (y1 + y3)/2
                areas.add(Pair(Pair(x1, y1), Pair(x2, y2)))
                areas.add(Pair(Pair(minOf(x2 + 1, x3), y1), Pair(x3, y2)))
                areas.add(Pair(Pair<Int, Int>(x1, minOf(y2 + 1, y3)), Pair<Int, Int>(x2, y3)))
                areas.add(Pair(Pair(minOf(x2 + 1, x3), minOf(y2 + 1, y3)), Pair(x3, y3)))
            }
            counter++

        }
        return x * 4_000_000L + y
    }
}

fun main() {
    fun part1(input: List<String>, y: Int): Int {
        val map = BeaconMap()
        map.input(input)
        return map.checkedPlacesInLine(y)
    }

    fun part2(input: List<String>, maxCoord: Int): Long {
        val map = BeaconMap()
        map.input(input)
        return map.frequency(maxCoord)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15-TestInput")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15-Input")
    println(part1(input, 2_000_000))
    println(part2(input, 4_000_000))
}

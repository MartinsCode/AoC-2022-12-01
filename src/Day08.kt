/**
 * After all, it's just a map of trees.
 *
 * After initializing we can simply iterate over all trees and consider, whether they are visible.
 */

fun main() {
    /**
     * After all, it's just a map of trees.
     *
     * After initializing we can simply iterate over all trees and consider, whether they are visible.
     */
    class TreeMap(map: List<String>) {
        val treeMap = map

        fun canBeSeenFromTop(x: Int, y: Int): Boolean {
            var maxHeight = ' '
            if (y == 0) {
                return true
            } else {
                for (i in 0 until y) {
                    if (treeMap[i][x] > maxHeight)
                        maxHeight = treeMap[i][x]
                }
                return (maxHeight < treeMap[y][x])
            }
        }

        fun canBeSeenFromBottom(x: Int, y: Int): Boolean {
            var maxHeight = ' '
            if (y == treeMap.lastIndex) {
                return true
            } else {
                for (i in y+1..treeMap.lastIndex ) {
                    if (treeMap[i][x] > maxHeight)
                        maxHeight = treeMap[i][x]
                }
                return (maxHeight < treeMap[y][x])
            }
        }

        fun canBeSeenFromLeft(x: Int, y: Int): Boolean {
            var maxHeight = ' '
            if (x == 0) {
                return true
            } else {
                for (i in 0 until x) {
                    if (treeMap[y][i] > maxHeight)
                        maxHeight = treeMap[y][i]
                }
                return (maxHeight < treeMap[y][x])
            }
        }

        fun canBeSeenFromRight(x: Int, y: Int): Boolean {
            var maxHeight = ' '
            if (x == treeMap[y].lastIndex) {
                return true
            } else {
                for (i in x+1..treeMap[0].lastIndex ) {
                    if (treeMap[y][i] > maxHeight)
                        maxHeight = treeMap[y][i]
                }
                return (maxHeight < treeMap[y][x])
            }
        }

        fun isVisible(x: Int, y: Int): Boolean {
            val visible = canBeSeenFromTop(x, y) ||
                    canBeSeenFromLeft(x, y) ||
                    canBeSeenFromRight(x, y) ||
                    canBeSeenFromBottom(x, y)
            return visible
        }

        fun countVisible(): Int {
            var count = 0
            treeMap.forEachIndexed { y, line ->
                for (x in 0..line.lastIndex) {
                    if (isVisible(x, y))
                        count++
                }
            }
            return count
        }

        fun scenicScoreUp(x: Int, y: Int): Int {
            if (y == 0)
                return 0
            var count = 1
            while ((y - count) > 0 && treeMap[y][x] > treeMap[y - count][x]) {
                count++
            }
            if (y - count < 0)
                count--
            return(count)
        }

        fun scenicScoreDown(x: Int, y: Int): Int {
            if (y == treeMap.lastIndex)
                return 0
            var count = 1
            while ((y + count) < treeMap.size && treeMap[y][x] > treeMap[y + count][x]) {
                count++
            }
            if (y + count == treeMap.size)
                count--
            return(count)
        }

        fun scenicScoreLeft(x: Int, y: Int): Int {
            if (x == 0)
                return 0
            var count = 1
            while ((x - count) > 0 && treeMap[y][x] > treeMap[y][x - count]) {
                count++
            }
            if (x - count < 0)
                count--
            return(count)
        }

        fun scenicScoreRight(x: Int, y: Int): Int {
            if (x == treeMap[y].lastIndex)
                return 0
            var count = 1
            while ((x + count) < treeMap[0].length && treeMap[y][x] > treeMap[y][x + count]) {
                count++
            }
            if (x + count == treeMap[0].length)
                count--
            return(count)
        }

        fun scenicScore(x: Int, y: Int): Int {
            return (scenicScoreUp(x, y) * scenicScoreDown(x, y) * scenicScoreLeft(x, y) * scenicScoreRight(x, y))
        }

        fun maxScenicScore(): Int {
            var maxScore = 0
            for (y in treeMap.indices) {
                for (x in treeMap[y].indices) {
                    if (scenicScore(x, y) > maxScore)
                        maxScore = scenicScore(x, y)
                }
            }
            return maxScore
        }
    }

    fun part1(input: List<String>): Int {
        val treeMap = TreeMap(input)
        return treeMap.countVisible()
    }

    fun part2(input: List<String>): Int {
        val treeMap = TreeMap(input)
        return treeMap.maxScenicScore()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08-TestInput")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08-Input")
    println(part1(input))
    println(part2(input))
}

fun main() {
    fun allDifferentChars(text: String): Boolean {
        val chars = text.toCharArray().distinct()
        return text.length == chars.size
    }

    fun part1(input: List<String>): List<Int> {
        val returnList = mutableListOf<Int>()
        input.forEach {
            var bufferString = it[0].toString().repeat(4)
            var messageStart = 0
            it.forEachIndexed { i, c ->
                bufferString = (bufferString + c).substring(1, 5)
                if (allDifferentChars(bufferString) && (messageStart == 0))
                    messageStart = i + 1
            }
            returnList.add(messageStart)
        }
        println(returnList)
        return returnList
    }

    fun part2(input: List<String>): MutableList<Int> {
        val returnList = mutableListOf<Int>()
        input.forEach {
            var bufferString = it[0].toString().repeat(14)
            var messageStart = 0
            it.forEachIndexed { i, c ->
                bufferString = (bufferString + c).substring(1, 15)
                if (allDifferentChars(bufferString) && (messageStart == 0))
                    messageStart = i + 1
            }
            returnList.add(messageStart)
        }
        println(returnList)
        return returnList
    }

    // test if implementation meets criteria from the description, like:
    check (allDifferentChars("jpqm") == true)
    val testInput = readInput("Day06-TestInput")
    check(part1(testInput) == listOf(7, 5, 6, 10, 11))
    check(part2(testInput) == listOf(19, 23, 23, 29, 26))

    val input = readInput("Day06-Input")
    println(part1(input))
    println(part2(input))
}

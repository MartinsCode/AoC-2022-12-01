enum class CompareResults {
    LESS,
    EQUAL,
    GREATER
}

fun main() {

    fun decompose(a: String): MutableList<String> {
        val aList = mutableListOf<String>()
        // parse the string
        // since it's ment to be an array, remove outer brackets:
        check(a[0] == '[')
        check(a[a.lastIndex] == ']')
        val text = a.substring(1, a.lastIndex)

        // now: PARSE!
        // can be [ or number
        // if [ search for ] and add whole [...] to aList
        // if number, parse to comma or end of string and add number to aList

        var i = 0

        println("Decomposing $a")

        while (i < text.length) {
            if (text[i] == '[') {
                // beware of multiple layers!
                var bracketLevel = 1
                var j = i + 1
                while (bracketLevel > 0) {
                    if (text[j] == '[')
                        bracketLevel++
                    if (text[j] == ']')
                        bracketLevel--
                    j++
                }
                aList.add(text.substring(i, j))
                i = j
            } else {
                // parse until comma
                var j = i + 1
                while (j < text.length && text[j] != ',') {
                    j++
                }
                aList.add(text.substring(i, j))
                i = j
            }
            i++
        }

        return aList
    }

    fun rightOrder(a: String, b: String): CompareResults {
        // basic assumption
        var rightOrder = CompareResults.EQUAL

        // decompose strings to elements
        val aList = decompose(a)
        val bList = decompose(b)

        val length = minOf(aList.size, bList.size)

        var current = 0

        while (rightOrder == CompareResults.EQUAL && current < length) {
            // then compare sizes and iterate the shorter one
            if (aList[current][0] == '[' || bList[current][0] == '[') {
                var aStr = aList[current]
                var bStr = bList[current]
                if (aStr[0] != '[') {
                    aStr = "[$aStr]"
                }
                if (bStr[0] != '[') {
                    bStr = "[$bStr]"
                }
                rightOrder = rightOrder(aStr, bStr)
            } else {
                if (aList[current].toInt() > bList[current].toInt()) {
                    rightOrder = CompareResults.GREATER
                } else if (aList[current].toInt() < bList[current].toInt()) {
                    rightOrder = CompareResults.LESS
                }
            }
            current++
            // compare each element according to rules
            // if any element is in wrong order, break and return false
        }
        // so we compared 'em all and still are undecided. And now?
        if (rightOrder == CompareResults.EQUAL) {
            println("Decision because of length!")
            // length decides. a has to be shorter than b for being true
            rightOrder = when {
                a.length < b.length -> CompareResults.LESS
                a.length > b.length -> CompareResults.GREATER
                else -> CompareResults.EQUAL
            }
        }
        println("Compared $a to $b - rightOrder: $rightOrder")
        return rightOrder
    }

    fun part1(input: List<String>): Int {
        var counter = 0
        var a = ""
        var b = ""
        var checksum = 0
        var actPair = 1

        input.forEach {
            when {
                it == "" -> {
                    println("Comparing $a and $b: rightOrder is ${rightOrder(a, b)}")
                    if (rightOrder(a, b) in listOf(CompareResults.LESS, CompareResults.EQUAL)) {
                        checksum += actPair
                    }
                    actPair++
                }
                counter == 0 -> a = it
                counter == 1 -> b = it
            }
            counter = (counter + 1) % 3
        }
        // last pair has no empty line beneath, so trigge comparison manually
        if (rightOrder(a, b) in listOf(CompareResults.LESS, CompareResults.EQUAL)) {
            checksum += actPair
        }

        println(checksum)
        return checksum
    }

    fun part2(input_: List<String>): Int {
        // Sort lines according to above way of sorting
        // For being a small set, we ignore all better sorting algorithms
        val input = mutableListOf<String>("[[2]]", "[[6]]")

        input_.forEach {
            if (it != "") {
                input.add(it)
            }
        }

        val sorted = mutableListOf<String>()

        while (input.size > 0) {
            var min = input[0]
            input.forEach {
                if (rightOrder(min, it) == CompareResults.GREATER) {
                    min = it
                }
            }
            input.remove(min)
            sorted.add(min)
        }

        var index1 = 0
        var index2 = 0

        sorted.forEachIndexed { index, s ->
            if (s == "[[2]]")
                index1 = index + 1
            if (s == "[[6]]")
                index2 = index + 1
        }

        println(sorted.joinToString("\n"))

        return index1 * index2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13-TestInput")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13-Input")
    check(part1(input) > 660)
    println(part1(input))
    println(part2(input))
}

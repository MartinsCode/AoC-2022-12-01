fun scoreDraw(a: String): Int = when (a) {
    "X" -> 1
    "Y" -> 2
    "Z" -> 3
    else -> 0
}

fun scoreWin(a: String, b: String): Int {
    return when {
        a == "A" && b == "X" -> 3
        a == "A" && b == "Y" -> 6
        a == "A" && b == "Z" -> 0
        a == "B" && b == "X" -> 0
        a == "B" && b == "Y" -> 3
        a == "B" && b == "Z" -> 6
        a == "C" && b == "X" -> 6
        a == "C" && b == "Y" -> 0
        a == "C" && b == "Z" -> 3
        else -> -100
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        var score = 0
        input.forEach {
            val arr = it.split(" ")
            if (arr.size > 1) {
                score += scoreDraw(arr[1]) + scoreWin(arr[0], arr[1])
            }
        }
        return score
    }

    fun derivedOwnScore(a: String, result: String): Int {
        // depending on other player and result, we calculate own score for hand
        return when {
            a == "A" && result == "X" -> 3 // has rock, I lose, I have scissors, I get 3
            a == "B" && result == "X" -> 1 // has paper, I lose, I have rock, I get 1
            a == "C" && result == "X" -> 2 // has scissors, I lose, I have paper, I get 2
            a == "A" && result == "Y" -> 1 // has rock, draw, I have rock, I get 1
            a == "B" && result == "Y" -> 2 // has paper, draw, I have paper, I get 2
            a == "C" && result == "Y" -> 3 // has scissors, draw, I have scissory, I get 3
            a == "A" && result == "Z" -> 2 // has rock, I win, I have paper, I get 2
            a == "B" && result == "Z" -> 3 // has paper, I win, I have scissors, I get 3
            a == "C" && result == "Z" -> 1 // has scissors, I win, I have rock, I get 1
            else -> -100
        }
    }

    fun winningScore(a: String) = when (a) {
        "X" -> 0
        "Y" -> 3
        "Z" -> 6
        else -> -100
    }

    fun part2(input: List<String>): Int {
        var score = 0
        input.forEach {
            val arr = it.split(" ")
            if (arr.size > 1) {
                score += derivedOwnScore(arr[0], arr[1]) + winningScore(arr[1])
            }
        }
        return score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02-TestInput")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02-Input")
    println(part1(input))
    println(part2(input))
}

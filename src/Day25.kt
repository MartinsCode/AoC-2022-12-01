import kotlin.math.pow

fun digitValue(sign: Char): Long {
    return when (sign) {
        '0' -> 0L
        '1' -> 1L
        '2' -> 2L
        '-' -> -1L
        '=' -> -2L
        else -> 0L
    }
}

fun desnafuify(snafu: String): Long {
    val snafuDigits = snafu.toCharArray()
    var value = 0L
    for (i in 0..snafuDigits.lastIndex) {
        value += (digitValue(snafuDigits[i]) * 5.0.pow(snafuDigits.lastIndex - i)).toLong()
    }
    return value
}

fun snafu(digit: Long): String = when (digit) {
    2L -> "2"
    1L -> "1"
    0L -> "0"
    -1L -> "-"
    -2L -> "="
    else -> "..."
}

fun snafuify(decimal: Long): String {
    var value = decimal
    // list of the digits. First entry is lowest(!) value, so it's written backwards
    val snafuRepresentation = mutableListOf<Long>()
    while (value > 0L) {
        snafuRepresentation.lastIndex
        var remainder = value % 5
        remainder = when (remainder) {
            4L -> {
                value += 1
                -1
            }
            3L -> {
                value += 2
                -2
            }
            else -> remainder
        }
        snafuRepresentation.add(remainder)
        value /= 5
    }
    println(snafuRepresentation)
    return snafuRepresentation.map{ snafu(it) }.reversed().joinToString("")
}

fun main() {
    fun part1(input: List<String>): String {
        var sum = 0L
        input.forEach {
            sum += desnafuify(it)
        }
        return snafuify(sum)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25-TestInput")
    println(part1(testInput))
    check(part1(testInput) == "2=-1=0")
//    check(part2(testInput) == 0)

    val input = readInput("Day25-Input")
    println(part1(input))
    println(part2(input))
}

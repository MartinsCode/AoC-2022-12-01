fun parseName(text: String): String {
    return text.split(": ")[0]
}

fun parseTask(text: String): String {
    return text.split(": ")[1]
}

fun isOperation(text: String): Boolean {
    return (text.contains(" + ") || text.contains(" - ") || text.contains(" * ") || text.contains(" / "))
}

fun findOperand1(calc: String): String {
    return calc.split(" ")[0]
}

fun findOperand2(calc: String): String {
    return calc.split(" ")[2]
}

fun findOperator(calc: String): String {
    return calc.split(" ")[1]
}

fun main() {
    fun part1(input: List<String>): Long {
        val results = mutableMapOf<String, Long>()
        val calculations = mutableMapOf<String, String>()

        input.forEach {
            val name = parseName(it)
            val task = parseTask(it)
            if (isOperation(task)) {
                calculations[name] = task
            } else {
                results[name] = task.toLong()
            }
        }

        while (!results.keys.contains("root")) {
            calculations.keys.forEach {
                val calculation = calculations[it]!!
                val oper1 = findOperand1(calculation)
                val oper2 = findOperand2(calculation)
                // check, if both results are already present!
                if (results.keys.contains(oper1) && results.keys.contains(oper2)) {
                    // YEAH!
                    val result = when (findOperator(calculation)) {
                        "+" -> results[oper1]!! + results[oper2]!!
                        "-" -> results[oper1]!! - results[oper2]!!
                        "*" -> results[oper1]!! * results[oper2]!!
                        "/" -> results[oper1]!! / results[oper2]!!
                        else -> throw(Exception("wrong operator!"))
                    }
                    results[it] = result
                    // calculations.remove(it)
                }
            }
        }

        return results["root"]!!
    }

    fun part2(input: List<String>): Long {
        val results = mutableMapOf<String, Long>()
        val calculations = mutableMapOf<String, String>()

        input.forEach {
            val name = parseName(it)
            val task = parseTask(it)
            if (isOperation(task)) {
                calculations[name] = task
            } else {
                results[name] = task.toLong()
            }
        }

        results.remove("humn")
        results["root"] = 42
        calculations.remove("humn")

        calculations["root"] = "${findOperand1(calculations["root"]!!)} = ${findOperand2(calculations["root"]!!)}"

        while (!results.keys.contains("humn")) {
            println("Resolving calculations ... ${calculations.size} left!")
            println(calculations)
            val toBeRemoved = mutableListOf<String>()
            calculations.keys.forEach {
                val calculation = calculations[it]!!
                val oper1 = findOperand1(calculation)
                val oper2 = findOperand2(calculation)
                // check, if both results are already present!
                if (results.keys.contains(oper1) && results.keys.contains(oper2)) {
                    // YEAH!
                    val result = when (findOperator(calculation)) {
                        "+" -> results[oper1]!! + results[oper2]!!
                        "-" -> results[oper1]!! - results[oper2]!!
                        "*" -> results[oper1]!! * results[oper2]!!
                        "/" -> results[oper1]!! / results[oper2]!!
                        else -> throw(Exception("wrong operator!"))
                    }
                    results[it] = result
                    toBeRemoved.add(it)
                    // calculations.remove(it)
                }
                // check, if one operand and key are already present, so we can track backwards
                if (results.keys.contains(it) && results.keys.contains(oper1)) {
                    val result = when (findOperator(calculation)) {
                        "+" -> results[it]!! - results[oper1]!!
                        "-" -> results[oper1]!! - results[it]!!
                        "*" -> results[it]!! / results[oper1]!!
                        "/" -> results[oper1]!! / results[it]!!
                        "=" -> results[oper1]!!
                        else -> throw(Exception("wrong operator!"))
                    }
                    results[oper2] = result
                    toBeRemoved.add(it)
                }
                if (results.keys.contains(it) && results.keys.contains(oper2)) {
                    val result = when (findOperator(calculation)) {
                        "+" -> results[it]!! - results[oper2]!!
                        "-" -> results[oper2]!! + results[it]!!
                        "*" -> results[it]!! / results[oper2]!!
                        "/" -> results[oper2]!! * results[it]!!
                        "=" -> results[oper2]!!
                        else -> throw(Exception("wrong operator!"))
                    }
                    results[oper1] = result
                    toBeRemoved.add(it)
                }

            }
            toBeRemoved.forEach{
                calculations.remove(it)
            }
        }

        return results["humn"]!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21-TestInput")
    println("Testing part 1 ...")
    check(part1(testInput) == 152L)
    println("Testing part 2 ...")
    check(part2(testInput) == 301L)

    val input = readInput("Day21-Input")
    println("Calculating part 1 ...")
    println(part1(input))
    println("Calculating part 2 ...")
    println(part2(input))
}

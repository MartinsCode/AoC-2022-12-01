
fun main() {
    class Monkey(details: List<String>) {
        val items = ArrayDeque<Long>()
        var operation: String = ""
        var test: Int = 0    // seems like all are "divisible by", so we only save the int
        var trueReceiver: Int = 0
        var falseReceiver: Int = 0
        var myNumber: Int = 0
        var processedItems = 0L
        var uneasy: Boolean = false
        var commonDiv = 0L

        init {
            details.forEach {
                when {
                    it.matches("Monkey (.*):".toRegex()) -> {
                        var number = it.split(" ")[1]
                        number = number.replace(":", "")
                        myNumber = number.toInt()
                    }
                    it.matches("  Starting items: (.*)".toRegex()) -> {
                        val numbers = it.split(": ")[1]
                        numbers.split(", ").map { it.toLong() }.forEach {
                            items.add(it)
                        }
                    }
                    it.matches("  Operation: (.*)".toRegex()) -> {
                        var op = it.split(": ")[1]
                        check(op.matches("new = (.*)".toRegex())) // by now we don't handle other cases!
                        op = op.split(" = ")[1]
                        operation = op
                    }
                    it.matches("  Test: (.*)".toRegex()) -> {
                        val t = it.split(": ")[1]
                        check(t.matches("divisible by (.*)".toRegex()))  // no handling of other cases right now
                        test = t.split(" by ")[1].toInt()
                    }
                    it.matches("    If true: (.*)".toRegex()) -> {
                        val rec = it.split(": ")[1]
                        check(rec.matches("throw to monkey (.*)".toRegex()))
                        trueReceiver = rec.split(" monkey ")[1].toInt()
                    }
                    it.matches("    If false: (.*)".toRegex()) -> {
                        val rec = it.split(": ")[1]
                        check(rec.matches("throw to monkey (.*)".toRegex()))
                        falseReceiver = rec.split(" monkey ")[1].toInt()
                    }
                }
            }
        }

        fun processAllItems(): List<Pair<Int, Long>> {
            val itemList = mutableListOf<Pair<Int, Long>>()
            while (items.size > 0) {
                val item = items.removeFirst()
                itemList.add(process(item))
            }
            return itemList
        }

        /**
         * Process one item.
         *
         * @return Pair<Int, Int> with monkey and items to give
         */
        fun process(item: Long): Pair<Int, Long> {
            var level = when {
                operation.matches("old [*] old".toRegex()) -> {
                    item * item
                }
                operation.matches("old [*] (.*)".toRegex()) -> {
                    item * operation.split(" * ")[1].toLong()
                }
                operation.matches("old [+] (.*)".toRegex()) -> {
                    item + operation.split(" + ")[1].toLong()
                }
                else -> {
                    throw Exception("Operation not supported: $operation")
                }
            }
            if (commonDiv != 0L)
                level %= commonDiv
            if (!uneasy)
                level /= 3L

            val recMonkey = when ((level.mod(test.toLong()) == 0L)) {
                true -> trueReceiver
                false -> falseReceiver
            }
            processedItems++
            return (Pair(recMonkey, level))
        }

        fun give(item: Long) {
            items.add(item)
        }
    }

    fun part1(input: List<String>): Long {
        // parse Monkey-Business
        val monkeys = mutableListOf<Monkey>()
        var firstMonkeyLine = 0
        var lastMonkeyLine = 0

        fun give(monkey: Int, item: Long) {
            monkeys.find { it.myNumber == monkey }!!.give(item)
        }

        while (lastMonkeyLine <= input.size) {
            // create monkeys from monkey-List
            if (lastMonkeyLine == input.size) {
                monkeys.add(Monkey(input.subList(firstMonkeyLine, lastMonkeyLine)))
            } else {
                if (input[lastMonkeyLine] == "") {
                    monkeys.add(Monkey(input.subList(firstMonkeyLine, lastMonkeyLine)))
                    firstMonkeyLine = lastMonkeyLine + 1
                }
            }
            lastMonkeyLine++
        }

        // iterate 20 rounds of each monkey
        for (i in 1..20) {
            // (monkeys 0 to max, each iterates ALSO over the already given in THIS round!)
            // keep track of inspections
            for (monkey in monkeys) {
                val itemList = monkey.processAllItems()
                itemList.forEach {
                    give(it.first, it.second)
                }
            }
        }
        // get list of number of monkey-inspections
        val inspections = monkeys.map { it.processedItems }.toMutableList()
        while (inspections.size > 2) {
            inspections.remove(inspections.min())
        }
        // take two most numbers, multiply, return
        return inspections[0] * inspections[1]
    }

    fun part2(input: List<String>): Long {
        // Problem is, that Long is not enough for levels, bit BigInt is was too slow.
        // So let's return to Long, and ... well, let's see. probably some arithmetic game.

        // parse Monkey-Business
        val monkeys = mutableListOf<Monkey>()
        var firstMonkeyLine = 0
        var lastMonkeyLine = 0

        fun give(monkey: Int, item: Long) {
            monkeys.find { it.myNumber == monkey }!!.give(item)
        }

        while (lastMonkeyLine <= input.size) {
            // create monkeys from monkey-List
            if (lastMonkeyLine == input.size) {
                monkeys.add(Monkey(input.subList(firstMonkeyLine, lastMonkeyLine)))
            } else {
                if (input[lastMonkeyLine] == "") {
                    monkeys.add(Monkey(input.subList(firstMonkeyLine, lastMonkeyLine)))
                    firstMonkeyLine = lastMonkeyLine + 1
                }
            }
            lastMonkeyLine++
        }

        monkeys.forEach{
            it.uneasy = true
        }

        var commonDiv = 1L
        monkeys.forEach {
            commonDiv *= it.test
        }
        monkeys.forEach{
            it.commonDiv = commonDiv
        }

        // iterate some rounds of each monkey
        for (i in 1..10_000) {
            // (monkeys 0 to max, each iterates ALSO over the already given in THIS round!)
            // keep track of inspections
            for (monkey in monkeys) {
                val itemList = monkey.processAllItems()
                itemList.forEach {
                    give(it.first, it.second)
                }
            }
            if (i in arrayOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000)) {
                println("\n== After round $i ==")
                monkeys.forEach{
                    println("Monkey ${it.myNumber} inspected items ${it.processedItems} times.")
                }
            }
            if (i % 10 == 0)
                print(".")
        }
        // get list of number of monkey-inspections
        val inspections = monkeys.map { it.processedItems }.toMutableList()
        while (inspections.size > 2) {
            inspections.remove(inspections.min())
        }
        // take two most numbers, multiply, return
        return inspections[0] * inspections[1]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11-TestInput")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11-Input")
    println(part1(input))
    println(part2(input))
}

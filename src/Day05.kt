fun main() {
    fun part1(input: List<String>): String {
        // we read in the stacks as ArrayDeque
        // So we have to read in all the first lines
        // find empty line
        var endOfStackdescription = 0
        var index = 0
        while (endOfStackdescription == 0) {
            if (input[index].trim() == "")
                endOfStackdescription = index - 1 // so it points at the line with the stack numbers
            index++
        }
        // when hitting an empty line, we have reached the end of the stack descriptions
        val stackNumbers = cutString(input[endOfStackdescription], 4).map{ intify(it) }

        val stacks = Array<ArrayDeque<String>>(stackNumbers.max()){ ArrayDeque() }

        // Description line -> Array of values (possibly empty)
        // for each value, pushlast at the corresponding arraydeque (if empty, just don't so anything)
        input.subList(0, endOfStackdescription).forEach {
            val elements = cutString(it, 4).map{ it[1].toString() }
            elements.forEachIndexed { i, it ->
                if (it != " ")
                    stacks[i].addLast(it)
            }
        }

        // if done: parse instructions and execute them

        val instructions = input.subList(endOfStackdescription + 2, input.size)

        instructions.forEach {
            val instruction = it.split(" ")
            assert(instruction[0] == "move")
            assert(instruction[2] == "from")
            assert(instruction[4] == "to")
            val num = instruction[1].toInt()
            val fromStack = instruction[3].toInt() - 1
            val toStack = instruction[5].toInt() - 1

            var i = 0
            while (i < num) {
                val element = stacks[fromStack].removeFirst()
                stacks[toStack].addFirst(element)
                i++
            }
        }

        var top = ""
        stacks.forEach {
            top += it.first()
        }
        return top
    }

    fun part2(input: List<String>): String {
        // we read in the stacks as ArrayDeque
        // So we have to read in all the first lines
        // find empty line
        var endOfStackdescription = 0
        var index = 0
        while (endOfStackdescription == 0) {
            if (input[index].trim() == "")
                endOfStackdescription = index - 1 // so it points at the line with the stack numbers
            index++
        }
        // when hitting an empty line, we have reached the end of the stack descriptions
        val stackNumbers = cutString(input[endOfStackdescription], 4).map{ intify(it) }

        val stacks = Array<ArrayDeque<String>>(stackNumbers.max()){ ArrayDeque() }

        // Description line -> Array of values (possibly empty)
        // for each value, pushlast at the corresponding arraydeque (if empty, just don't so anything)
        input.subList(0, endOfStackdescription).forEach {
            val elements = cutString(it, 4).map{ it[1].toString() }
            elements.forEachIndexed { i, it ->
                if (it != " ")
                    stacks[i].addLast(it)
            }
        }

        // if done: parse instructions and execute them

        val instructions = input.subList(endOfStackdescription + 2, input.size)

        instructions.forEach {
            val instruction = it.split(" ")
            assert(instruction[0] == "move")
            assert(instruction[2] == "from")
            assert(instruction[4] == "to")
            val num = instruction[1].toInt()
            val fromStack = instruction[3].toInt() - 1
            val toStack = instruction[5].toInt() - 1

            var i = 0
            val tempList = emptyList<String>().toMutableList()
            while (i < num) {
                tempList.add(stacks[fromStack].removeFirst())
                i++
            }
            tempList.reverse()
            tempList.forEach{
                stacks[toStack].addFirst(it)
            }
        }

        var top = ""
        stacks.forEach {
            top += it.first()
        }
        return top
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05-TestInput")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05-Input")
    println(part1(input))
    println(part2(input))
}

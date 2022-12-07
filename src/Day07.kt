fun main() {

    // We need a stateful machine to parse the input.
    // It has to rebuild a kind of tree with the dir structure
    // And later on it has to traverse through the directories and calculate things.

    // Therefore let's keep track of all directories:
    // directories = mutableListOf("") // root
    // and of files with full name (including path) as a map (full name to size)
    // files = mapOf<String, Long>()
    // when we want to keep track of every file in a subdir, we simply compare the beginning of the name
    // (and maybe check, whether there is a slash in the file name)

    class Machine {
        val rootDir = ""
        var actualDir = rootDir
        val directories = mutableListOf("")
        val files = mutableMapOf<String, Int>()

        fun changeToDir(input: String) {
            val actDir = actualDir.split("/").toMutableList()
            when (input) {
                ".." -> {
                    actDir.removeLast()
                }
                "/" -> {
                    actDir.clear()
                }
                else -> {
                    actDir.add(input)
                }
            }
            actualDir = actDir.joinToString("/")
            actualDir.replace("//", "/")
            if (!directories.contains(actualDir))
                directories.add(actualDir)
        }

        fun parseCommand(input: String) {
            val command = input.split(" ")
            check(command[0] == "$")
            check(command[1] == "cd" || command[1] == "ls") { "ls or cd expected, but found ${command[1]}" }
            // if command == ls, nothing to do :-) since Output assumes, it's from ls
            if (command[1] == "cd") {
                changeToDir(command[2])
            }
        }

        fun parseOutput(input: String) {
            val strSize = input.split(" ")[0]
            val name = input.split(" ")[1]
            if (strSize == "dir") {
                // we could map this. But why?
            } else {
                // seems we have a file
                val size = strSize.toInt()
                val fullname = "$actualDir/$name"
                files[fullname] = size
            }
        }

        fun parseLine(input: String) {
            if (input.matches("^\\$ .*".toRegex())) {
                parseCommand(input)
            } else {
                parseOutput(input)
            }
        }

        fun parseInput(input: List<String>) {
            input.forEach {
                parseLine(it)
            }
        }

        fun sumOfSmallFiles(): Int {
            return files.values.filter { it < 100_000 }.sum()
        }

        fun makeDirSizeMap(): MutableMap<String, Int> {
            val sizeMap = mutableMapOf<String, Int>()
            directories.forEach { dirname ->
                // calculate size and add to map
                files.forEach { (name, size) ->
                    if (name.startsWith(dirname)) {
                        if (!sizeMap.keys.contains(dirname)) {
                            sizeMap[dirname] = size
                        } else {
                            sizeMap[dirname] = size + sizeMap[dirname]!!
                        }
                    }
                }
            }
            return sizeMap
        }

        fun sumOfSmallDirectories(): Int {
            val sizeMap = makeDirSizeMap()
            return sizeMap.values.filter { it < 100_000 }.sum()
        }

        fun totalSize(): Int {
            return files.values.sum()
        }

        fun sizeOfSmallestAbove(limit: Int): Int {
            val sizeMap = makeDirSizeMap()
            return sizeMap.values.filter { it >= limit }.min()
        }
    }

    fun part1(input: List<String>): Int {
        val machine = Machine()
        machine.parseInput(input)
        return machine.sumOfSmallDirectories()
    }

    fun part2(input: List<String>): Int {
        val machine = Machine()
        machine.parseInput(input)
        return machine.sizeOfSmallestAbove(30_000_000 - (70_000_000 - machine.totalSize()))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07-TestInput")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07-Input")
    println(part1(input))
    println(part2(input))
    check(part2(input) < 43562874) // Answer was too high
}

fun main() {
    val validSpelledOutDigits = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
    ) + (0..9).map { "$it" }

    fun String.firstDigit(): Int {
        val (_, word) = findAnyOf(validSpelledOutDigits) ?: error("Could not find any digit, spelled out or not")

        return if (word.length == 1) {
            word.toInt()
        } else {
            validSpelledOutDigits.indexOf(word) + 1
        }
    }

    fun String.lastDigit(): Int {
        val (_, word) = findLastAnyOf(validSpelledOutDigits) ?: error("Could not find any digit, spelled out or not")

        return if (word.length == 1) {
            word.toInt()
        } else {
            validSpelledOutDigits.indexOf(word) + 1
        }
    }

    fun part1(input: List<String>): Int = input.map { line ->
        val firstDigit = line.first { it.isDigit() }
        val lastDigit = line.last { it.isDigit() }
        "$firstDigit$lastDigit".toInt()
    }.sum()

    fun part2(input: List<String>): Int = input.map { line ->
        val firstDigit = line.firstDigit()
        val lastDigit = line.lastDigit()

        "$firstDigit$lastDigit".toInt()
    }.sum()
    readInput("Day01_test").let { testInput ->
        check(part1(testInput) == 142)
    }
    readInput("Day01_test2").let { testInput ->
        check(part2(testInput) == 281)
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

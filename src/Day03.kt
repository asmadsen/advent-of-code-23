import Day03.findAllGears
import Day03.findAllPartNumbers

object Day03 {
    data class Position(val row: Int, val column: Int)

    data class PartNumber private constructor(val number: Int, val row: Int, val column: Int) {
        companion object {
            operator fun invoke(number: String, row: Int, column: Int, lines: List<String>): PartNumber? {
                val isPartNumber = lines.subList((row - 1).coerceAtLeast(0), (row + 2).coerceAtMost(lines.size))
                    .any { line ->
                        val searchArea = line.substring(
                            startIndex = (column - 1).coerceAtLeast(0),
                            endIndex = (column + number.length + 1).coerceAtMost(line.length),
                        )
                        searchArea.any { !it.isDigit() && it != '.' }
                    }

                if (!isPartNumber) {
                    return null
                }

                return PartNumber(number.toInt(), row, column)
            }
        }
    }

    fun List<String>.findAllPartNumbers() = flatMapIndexed { row: Int, line: String ->
        var numberString: String? = null
        buildList {
            line.forEachIndexed { index, char ->
                if (char.isDigit()) {
                    numberString = numberString?.let { it + char } ?: "$char"
                } else {
                    numberString?.let { number ->
                        PartNumber(
                            number = number,
                            row = row,
                            column = index - number.length,
                            lines = this@findAllPartNumbers,
                        )?.let { add(it) }
                        numberString = null
                    }
                }
            }
            numberString?.let { number ->
                PartNumber(number, row, line.length - number.length, this@findAllPartNumbers)?.let { add(it) }
            }
        }
    }

    fun List<String>.findAllGears(partNumbers: List<PartNumber> = findAllPartNumbers()): Int {
        val partNumbersByRow = partNumbers.groupBy { it.row }

        return flatMapIndexed { row: Int, line: String ->
            line.mapIndexedNotNull { column, char ->
                if (char == '*') Position(row, column) else null
            }
        }.mapNotNull { position ->
            buildList {
                repeat(3) {
                    partNumbersByRow[position.row + (it - 1)]?.forEach { part ->
                        val range = (part.column - 1).coerceAtLeast(0)..(part.column + part.number.toString().length)
                        if (position.column in range) {
                            add(part)
                        }
                    }
                }
            }.takeIf { it.size == 2 }
        }.sumOf { (first, last) ->
            first.number * last.number
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int = input
        .findAllPartNumbers()
        .sumOf { it.number }

    fun part2(input: List<String>): Int = input
        .findAllGears()

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

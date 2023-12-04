import Day04.Card
import Day04.process
import kotlin.math.pow

object Day04 {
    data class Card(
        val number: Int,
        val winningNumbers: Set<Int>,
        val cardNumbers: Set<Int>,
    ) {
        val winnings = winningNumbers.intersect(cardNumbers)

        val score = 2.0.pow(winnings.size - 1).toInt()

        companion object {
            operator fun invoke(line: String): Card {
                val cardNumber = line.substringBefore(':').takeLastWhile { it.isDigit() }

                val (winningNumbers, cardNumbers) = line.substringAfter(':').split('|').map { it.trim() }

                return Card(
                    cardNumber.toInt(),
                    winningNumbers.split(whitespaceRegex).mapNotNull { it.toIntOrNull() }.toSet(),
                    cardNumbers.split(whitespaceRegex).mapNotNull { it.toIntOrNull() }.toSet(),
                )
            }
        }
    }

    fun List<Card>.process(): Int {
        val accumulatedCopies = mutableMapOf<Int, Int>()

        forEach { card ->
            val score = card.winnings.size

            val copies = accumulatedCopies[card.number] ?: 0

            repeat(score) {
                val cardNumberToCopy = card.number + it + 1

                accumulatedCopies.compute(cardNumberToCopy) { _, it ->
                    (it ?: 0) + 1 + copies
                }
            }
        }

        return accumulatedCopies.values.sum() + size
    }
}

fun main() {
    fun part1(input: List<String>): Int = input
        .map(Card::invoke)
        .filter { it.score > 0 }
        .sumOf { it.score }

    fun part2(input: List<String>): Int = input
        .map(Card::invoke)
        .process()

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val testInput2 = readInput("Day04_test2")

    check(part2(testInput2) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}


private data class Game(
    val id: Int,
    val cubeSets: List<Map<Cube, Int>>,
)

private fun Game.isPossibleWith(availableCubes: Map<Cube, Int>) = cubeSets.all { cubeSet ->
    cubeSet.all { (cube, requiredAmount) ->
        availableCubes.getOrDefault(cube, 0) >= requiredAmount
    }
}

private val Game.requiredCubes get() = buildMap {
    cubeSets.forEach { cubeSet ->
        cubeSet.forEach { (cube, requiredAmount) ->
            this.compute(cube) { _, currentlyRequired ->
                maxOf(currentlyRequired ?: 0, requiredAmount)
            }
        }
    }
}

@JvmInline
private value class Cube(val color: String)

private fun parseCubeSet(line: String): Map<Cube, Int> = line
    .split(',')
    .associate {
        val (amount, color) = it.trim().split(' ')
        Cube(color) to amount.toInt()
    }

private fun parseGame(line: String): Game {
    val id = line.substringBefore(':').substringAfter(' ').toInt()
    val cubeSets = line.substringAfter(':').split(';')
        .map(::parseCubeSet)
    return Game(id, cubeSets)
}

private val Map<Cube, Int>.power get() = values.reduce { acc, amount ->
    acc * amount
}

fun main() {
    val part1RequiredCubes = mapOf(
        Cube("red") to 12,
        Cube("green") to 13,
        Cube("blue") to 14,
    )
    fun part1(input: List<String>): Int = input.map(::parseGame)
        .filter {
            it.isPossibleWith(part1RequiredCubes)
        }
        .sumOf { it.id }

    fun part2(input: List<String>): Int = input.map(::parseGame)
        .sumOf {
            it.requiredCubes.power
        }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

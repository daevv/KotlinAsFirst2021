@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import kotlin.math.abs

/**
 * Класс "табличная функция".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса хранит таблицу значений функции (y) от одного аргумента (x).
 * В таблицу можно добавлять и удалять пары (x, y),
 * найти в ней ближайшую пару (x, y) по заданному x,
 * найти (интерполяцией или экстраполяцией) значение y по заданному x.
 *
 * Класс должен иметь конструктор по умолчанию (без параметров).
 */
class TableFunction {
    private val table = mutableMapOf<Double, Double>().toSortedMap()

    /**
     * Количество пар в таблице
     */
    val size: Int get() = table.size

    /**
     * Добавить новую пару.
     * Вернуть true, если пары с заданным x ещё нет,
     * или false, если она уже есть (в этом случае перезаписать значение y)
     */
    fun add(x: Double, y: Double): Boolean {
        val res = x !in table.keys
        table[x] = y
        return res
    }

    /**
     * Удалить пару с заданным значением x.
     * Вернуть true, если пара была удалена.
     */
    fun remove(x: Double): Boolean {
        if (x !in table.keys) return false
        table.remove(x)
        return true
    }

    /**
     * Вернуть коллекцию из всех пар в таблице
     */
    fun getPairs(): Collection<Pair<Double, Double>> {
        val res = mutableSetOf<Pair<Double, Double>>()
        for (el in table) {
            res.add(el.toPair())
        }
        return res
    }

    /**
     * Вернуть пару, ближайшую к заданному x.
     * Если существует две ближайшие пары, вернуть пару с меньшим значением x.
     * Если таблица пуста, бросить IllegalStateException.
     */
    fun findPair(x: Double): Pair<Double, Double> {
        if (table.isEmpty()) throw IllegalStateException()
        val res: Double
        val low = table.headMap(x)
        val high = table.tailMap(x)
        res = when {
            high.isEmpty() -> low.lastKey()
            low.isEmpty() -> high.firstKey()
            else -> {
                val reallyLow = low.lastKey()
                val reallyHigh = high.firstKey()
                if (abs(x - reallyHigh) < abs(x - reallyLow)) {
                    reallyHigh
                } else {
                    reallyLow
                }
            }
        }
        return Pair(res, table[res]!!)
    }

    /**
     * Вернуть значение y по заданному x.
     * Если в таблице есть пара с заданным x, взять значение y из неё.
     * Если в таблице есть всего одна пара, взять значение y из неё.
     * Если таблица пуста, бросить IllegalStateException.
     * Если существуют две пары, такие, что x1 < x < x2, использовать интерполяцию.
     * Если их нет, но существуют две пары, такие, что x1 < x2 < x или x > x2 > x1, использовать экстраполяцию.
     */
    fun getValue(x: Double): Double {
        when {

            table.isEmpty() -> throw IllegalArgumentException()
            table[x] != null -> return table[x]!!
            table.size == 1 -> return table.values.first()

            else -> {


                val low = table.headMap(x)
                val high = table.tailMap(x)
                val x1: Double
                val x2: Double

                when {
                    low.isEmpty() -> {
                        x1 = high.firstKey()
                        x2 = high.keys.take(2).last()
                    }
                    high.isEmpty() -> {
                        x1 = low.lastKey()
                        x2 = low.headMap(x1).lastKey()
                    }
                    else -> {
                        x1 = low.lastKey()
                        x2 = high.firstKey()
                    }
                }

                val y1 = table[x1] ?: 0.0
                val y2 = table[x2] ?: 0.0

                return y1 + (x - x1) / (x2 - x1) * (y2 - y1)
            }
        }
    }


    /**
     * Таблицы равны, если в них одинаковое количество пар,
     * и любая пара из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean {
        if (other is TableFunction) {
            return table == other.table
        }
        return false
    }

    override fun hashCode(): Int = table.hashCode()

}
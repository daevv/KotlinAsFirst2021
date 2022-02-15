@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

/**
 * Класс "Телефонная книга".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 14.
 * Объект класса хранит список людей и номеров их телефонов,
 * при чём у каждого человека может быть более одного номера телефона.
 * Человек задаётся строкой вида "Фамилия Имя".
 * Телефон задаётся строкой из цифр, +, *, #, -.
 * Поддерживаемые методы: добавление / удаление человека,
 * добавление / удаление телефона для заданного человека,
 * поиск номера(ов) телефона по заданному имени человека,
 * поиск человека по заданному номеру телефона.
 *
 * Класс должен иметь конструктор по умолчанию (без параметров).
 */
class PhoneBook {

    private val book = mutableMapOf<String, MutableSet<String>>()

    /**
     * Добавить человека.
     * Возвращает true, если человек был успешно добавлен,
     * и false, если человек с таким именем уже был в телефонной книге
     * (во втором случае телефонная книга не должна меняться).
     */
    fun addHuman(name: String): Boolean {
        val reg = Regex("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+")
        if (!name.matches(reg)) throw IllegalArgumentException()
        if (book[name] != null) {
            return false
        }
        book[name] = mutableSetOf()
        return true
    }

    /**
     * Убрать человека.
     * Возвращает true, если человек был успешно удалён,
     * и false, если человек с таким именем отсутствовал в телефонной книге
     * (во втором случае телефонная книга не должна меняться).
     */
    fun removeHuman(name: String): Boolean {
        val reg = Regex("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+")
        if (!name.matches(reg)) throw IllegalArgumentException()
        if (book[name] == null) {
            return false
        }
        book.remove(name)
        return true
    }

    /**
     * Добавить номер телефона.
     * Возвращает true, если номер был успешно добавлен,
     * и false, если человек с таким именем отсутствовал в телефонной книге,
     * либо у него уже был такой номер телефона,
     * либо такой номер телефона зарегистрирован за другим человеком.
     */
    fun addPhone(name: String, phone: String): Boolean {
        val regNums = Regex("[0-9+*#-]+")
        val regNames = Regex("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+")
        if (!phone.matches(regNums) || !name.matches(regNames)) throw IllegalArgumentException()
        val page = book[name]

        when (page) {
            null -> return false
            else -> for (list in book.values) {
                if (phone in list) {
                    return false
                }
            }
        }
        page.add(phone)
        return true
    }

    /**
     * Убрать номер телефона.
     * Возвращает true, если номер был успешно удалён,
     * и false, если человек с таким именем отсутствовал в телефонной книге
     * либо у него не было такого номера телефона.
     */
    fun removePhone(name: String, phone: String): Boolean {
        val regNums = Regex("[0-9+*#-]+")
        val regNames = Regex("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+")
        if (!phone.matches(regNums) || !name.matches(regNames)) throw IllegalArgumentException()
        val page = book[name]
        if (page == null || phone !in page) {
            return false
        }
        page.remove(phone)
        return true
    }

    /**
     * Вернуть все номера телефона заданного человека.
     * Если этого человека нет в книге, вернуть пустой список
     */
    fun phones(name: String): Set<String> {
        val reg = Regex("[a-zA-Zа-яА-Я]+\\s[a-zA-Zа-яА-Я]+")
        if (!name.matches(reg)) throw IllegalArgumentException()
        return book[name] ?: mutableSetOf()
    }


    /**
     * Вернуть имя человека по заданному номеру телефона.
     * Если такого номера нет в книге, вернуть null.
     */
    fun humanByPhone(phone: String): String? {
        val regNums = Regex("[0-9+*#-]+")
        if (!phone.matches(regNums)) throw IllegalArgumentException()
        for ((key, value) in book) {
            if (phone in value) return key
        }
        return null
    }

    /**
     * Две телефонные книги равны, если в них хранится одинаковый набор людей,
     * и каждому человеку соответствует одинаковый набор телефонов.
     * Порядок людей / порядок телефонов в книге не должен иметь значения.
     */
    override fun equals(other: Any?): Boolean {
        if (other is PhoneBook) {
            return book == other.book
        }
        return false
    }

    override fun hashCode(): Int = book.hashCode()

}
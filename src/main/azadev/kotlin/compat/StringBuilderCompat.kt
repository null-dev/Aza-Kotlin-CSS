package azadev.kotlin.compat

public class StringBuilderCompat(content: String = "") : Appendable, CharSequence {
    constructor(capacity: Int) : this() {}

    constructor(content: CharSequence) : this(content.toString()) {}

    private var string: String = content

    override var length: Int
        get() = string.asDynamic().length
    set(n) {
        string.substring(0, n)
    }

    override fun get(index: Int): Char = string[index]

    override fun subSequence(start: Int, end: Int): CharSequence = string.substring(start, end)

    override fun append(c: Char): StringBuilderCompat {
        string += c
        return this
    }

    override fun append(csq: CharSequence?): StringBuilderCompat {
        string += csq.toString()
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): StringBuilderCompat {
        string += csq.toString().substring(start, end)
        return this
    }

    fun append(obj: Any?): StringBuilderCompat {
        string += obj.toString()
        return this
    }

    fun reverse(): StringBuilderCompat {
        string = string.asDynamic().split("").reverse().join("")
        return this
    }

    override fun toString(): String = string
}

public inline fun buildString(builderAction: StringBuilderCompat.() -> Unit): String =
        StringBuilderCompat().apply(builderAction).toString()
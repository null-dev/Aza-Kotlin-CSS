package azadev.kotlin.compat

object Integer {
    private val toHexStringFunction = js("(function(h) { return h.toString(16); })")

    fun toHexString(i: Int): String {
        return toHexStringFunction(i)
    }

    fun parseInt(string: String, radix: Int)
        = string.toInt(radix)
}
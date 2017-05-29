package azadev.kotlin.compat

import kotlin.js.Math

object Integer {
    val SIZE = 32

    fun toHexString(i: Int): String {
        return toUnsignedString0(i, 4)
    }

    fun parseInt(string: String, radix: Int)
        = string.toInt(radix)

    private fun toUnsignedString0(`val`: Int, shift: Int): String {
        // assert shift > 0 && shift <=5 : "Illegal shift value";
        val mag = SIZE - numberOfLeadingZeros(`val`)
        val chars = Math.max((mag + (shift - 1)) / shift, 1)
        val buf = CharArray(chars)

        formatUnsignedInt(`val`, shift, buf, 0, chars)

        // Use special constructor which takes over "buf".
        return buf.joinToString(separator = "")
    }

    fun numberOfLeadingZeros(i: Int): Int {
        var i = i
        // HD, Figure 5-6
        if (i == 0)
            return 32
        var n = 1
        if (i.ushr(16) == 0) {
            n += 16
            i = i shl 16
        }
        if (i.ushr(24) == 0) {
            n += 8
            i = i shl 8
        }
        if (i.ushr(28) == 0) {
            n += 4
            i = i shl 4
        }
        if (i.ushr(30) == 0) {
            n += 2
            i = i shl 2
        }
        n -= i.ushr(31)
        return n
    }

    /**
     * Format a long (treated as unsigned) into a character buffer.
     * @param val the unsigned int to format
     * *
     * @param shift the log2 of the base to format in (4 for hex, 3 for octal, 1 for binary)
     * *
     * @param buf the character buffer to write to
     * *
     * @param offset the offset in the destination buffer to start at
     * *
     * @param len the number of characters to write
     * *
     * @return the lowest character  location used
     */
    internal fun formatUnsignedInt(`val`: Int, shift: Int, buf: CharArray, offset: Int, len: Int): Int {
        var `val` = `val`
        var charPos = len
        val radix = 1 shl shift
        val mask = radix - 1
        do {
            buf[offset + --charPos] = digits[`val` and mask]
            `val` = `val` ushr shift
        } while (`val` != 0 && charPos > 0)

        return charPos
    }

    internal val digits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
}
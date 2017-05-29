@file:Suppress("unused")

package azadev.kotlin.css

import azadev.kotlin.compat.DecimalFormat

fun stylesheet(body: Stylesheet.()->Unit) = body

fun Stylesheet.url(str: String) = "url($str)"

val cssDecimalFormat = DecimalFormat()

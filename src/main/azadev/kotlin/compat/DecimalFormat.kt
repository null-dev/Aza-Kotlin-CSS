package azadev.kotlin.compat

class DecimalFormat() {
    private val DECIMAL_PLACES = 5
    fun format(float: Float): String = float.format(DECIMAL_PLACES).toFloat().toString()
    fun format(double: Double): String = double.format(DECIMAL_PLACES).toDouble().toString()
}

fun Double.format(digits: Int): String = this.asDynamic().toFixed(digits)
fun Float.format(digits: Int): String = this.asDynamic().toFixed(digits)
package azadev.kotlin.css.test

import azadev.kotlin.css.Stylesheet
import kotlin.test.assertEquals


interface ATest
{
	fun testRender(expected: String, callback: Stylesheet.()->Unit) {
		val stylesheet = Stylesheet(callback)
		assertEquals(expected, stylesheet.render())
	}

	fun testRender(expected: String, stylesheet: Stylesheet) {
		assertEquals(expected, stylesheet.render())
	}
}

package azadev.kotlin.css.test

import azadev.kotlin.css.*
import azadev.kotlin.css.colors.*
import azadev.kotlin.css.dimens.*
import kotlin.test.Test
import kotlin.test.assertEquals


// CSS Selectors:
// http://www.w3schools.com/cssref/css_selectors.asp

class RenderTest : ATest
{
	@Test
	fun selectors() {
		testRender("", {
			div {}
		})
		testRender("div{width:1}a{width:1}", {
			div { width = 1 }
			a { width = 1 }
		})
		testRender("div a{width:1}a:hover{width:1}", {
			div.a { width = 1 }
			a.hover { width = 1 }
		})
		testRender("div span{width:1}div span a{width:1}", {
			div.span {
				width = 1
				a { width = 1 }
			}
		})
		testRender("div:hover{width:1}div *:hover{width:1}", {
			div {
				hover { width = 1 }
				any.hover { width = 1 }
			}
		})
		testRender("div{width:1}div:hover{width:1}div:hover div{width:1}", {
			div {
				width = 1
				hover {
					width = 1
					div { width = 1 }
				}
			}
		})
	}

	@Test fun selectors_multiple() {
		testRender("a,div:hover,span{width:1}a,div:hover,span{width:1}", {
			a and div.hover and span { width = 1 }
			(a and div.hover and span) { width = 1 }
		})
		testRender("a:hover,div{width:1}a+span,div{width:1}a+span~a,div{width:1}", {
			a.hover and div { width = 1 }
			a % span and div { width = 1 }
			a % span - a and div { width = 1 }
		})
		testRender("div>a,div>span{width:1}div>a:hover,div>span:hover{width:1}div>a:hover,div>span:hover{width:1}", {
			div / (a and span) { width = 1 }
			div / (a and span).hover { width = 1 }
			(div / (a and span)).hover { width = 1 }
		})
		testRender("a,div,span{width:1}a:hover{width:1}a:hover a,a:hover div{width:1}div:hover{width:1}div:hover a,div:hover div{width:1}span:hover{width:1}span:hover a,span:hover div{width:1}", {
			a and div and span {
				width = 1
				hover {
					width = 1
					(a and div) { width = 1 }
				}
			}
		})

		testRender("a:hover{width:1}div:hover{width:1}span:hover{width:1}a>li{width:2}div>li{width:2}span>li{width:2}", {
			a and div and span {
				hover { width = 1 }
				child.li { width = 2 }
			}
		})
		testRender("a:hover,div:hover,span:hover{width:1}", {
			(a and div and span).hover { width = 1 }
		})
	}

	@Test fun selectors_traversing() {
		testRender("div>a{width:1}span>a{width:1}span a{width:1}", {
			div.child.a { width = 1 }
			span / a { width = 1 }
			span..a { width = 1 }
		})

		testRender("div+a{width:1}span+a{width:1}div+a{width:1}span+a{width:1}", {
			div.next.a { width = 1 }
			span % a { width = 1 }
			(div and span) {
				next.a { width = 1 }
			}
		})

		testRender("div>a{width:1}span>a{width:1}div span a,div>a{width:1}", {
			div and span {
				child.a { width = 1 }
			}
			div {
				(span and child).a { width = 1 }
			}
		})

		testRender("div>a:hover,div>span:hover{width:1}", {
			div.child {
				(a and span).hover { width = 1 }
			}
		})
		testRender("div>a+span:hover{width:1}", {
			div.child {
				a % span.hover { width = 1 }
			}
		})
	}

	@Test fun selectors_classesAndIds() {
		testRender(".class1{width:1}#id1{width:2}") {
			c("class1") { width = 1 }
			id("id1") { width = 2 }
		}

		testRender(".class1{width:1}#id1{width:2}") {
			".class1" { width = 1 }
			"#id1" { width = 2 }
		}

		testRender(".class1.class2{width:1}#id1.class1{width:2}") {
			".class1.class2" { width = 1 }
			"#id1.class1" { width = 2 }
		}

		testRender(".class1:hover{width:1}") {
			".class1".hover { width = 1 }
		}
		testRender(".class1>a{width:1}") {
			".class1" / a { width = 1 }
		}
		testRender(".class1+a{width:1}") {
			".class1" % a { width = 1 }
		}
		testRender(".class1~a{width:1}") {
			".class1" - a { width = 1 }
		}

		testRender("div#id1>a.class1{width:1}") {
			div.id("id1") {
				child.a.c("class1") { width = 1 }
			}
		}

		testRender("div,.class1{width:1}div a{width:2}.class1 a{width:2}") {
			div and ".class1" {
				width = 1
				a { width = 2 }
			}
		}
		testRender(".class1,.class2{width:1}.class1 a{width:2}.class2 a{width:2}") {
			".class1" and ".class2" {
				width = 1
				a { width = 2 }
			}
		}

		testRender(".class1 div{width:1}.class1 .class2{width:2}") {
			".class1".div { width = 1 }
			".class1".children.c("class2") { width = 2 }
		}
		testRender(".class1 .class2{width:1}.class1 div{width:2}div #id1{width:3}") {
			".class1"..".class2" { width = 1 }
			".class1"..div { width = 2 }
			div.."#id1" { width = 3 }
		}
		testRender(".class1 .class2{width:1}.class1 .class2 .class3{width:2}.class1 .class2 .class3 .class4{width:3}") {
			".class1"..c("class2") { width = 1 }
			".class1"..c("class2")..c("class3") { width = 2 }
			".class1"..c("class2")..c("class3")..".class4" { width = 3 }
		}

		testRender(".class1 .class2 span{top:0}.class1 .class2 span .class3{top:1}") {
			".class1"..".class2"..span { top = 0 }
			".class1"..".class2"..span..".class3" { top = 1 }
		}
		testRender(".class1 .class2~span{top:0}#id1+.class2 span>.class3{top:1}") {
			".class1"..".class2" - span { top = 0 }
			"#id1" % ".class2"..span / ".class3" { top = 1 }
		}
		testRender(".class1,.class2,.class3{top:0}") {
			".class1" and ".class2" and ".class3" { top = 0 }
		}
	}

	@Test fun selectors_pseudoFn() {
		testRender("div:not(span){width:1}div:not(a:hover) span{width:1}", {
			div.not(span) { width = 1 }
			div.not(a.hover).span { width = 1 }
		})
		testRender("*:nth-child(even){width:1}div a:nth-child(2),span{width:1}", {
			any.nthChild(EVEN) { width = 1 }
			div.a.nthChild(2) and span { width = 1 }
		})
		testRender(".items:not(li){width:1}.items:not(li),span{width:1}", {
			".items".not(li) { width = 1 }
			".items".not(li) and span { width = 1 }
		})
		testRender(".items:some{width:1}.items:some(){width:1}", {
			".items".pseudo(":some") { width = 1 }
			".items".pseudoFn(":some()") { width = 1 }
		})
	}

	@Test fun selectors_attributes() {
		testRender("input[disabled]{width:1}input[disabled=true]:hover{width:1}", {
			input["disabled"] { width = 1 }
			input["disabled", true].hover { width = 1 }
		})

		testRender("input[disabled],textarea[disabled]{width:1}input[disabled][type=hidden],textarea[disabled][type=hidden]{width:1}", {
			(input and textarea)["disabled"] { width = 1 }
			(input and textarea)["disabled"]["type", "hidden"] { width = 1 }
		})

		testRender("input[disabled][type*=dd],textarea[type*=dd]{width:1}", {
			(input["disabled"] and textarea)["type", contains, "dd"] { width = 1 }
		})
		testRender("a[type=hidden]{width:10px}a[href^=https]{width:10px}", {
			a["type", equals, "hidden"] { width = 10.px }
			a["href", startsWith, "https"] { width = 10.px }
		})

		testRender("[disabled]{width:1}[disabled][hidden]{width:1}", {
			attr("disabled") { width = 1 }
			attr("disabled")["hidden"] { width = 1 }
		})

		testRender("#logo[type=main]{width:1}", {
			"#logo"["type", "main"] { width = 1 }
		})

		testRender("""a[href^="http://"]{width:10px}""", {
			a["href", startsWith, "http://"] { width = 10.px }
		})
	}

	@Test fun selectors_custom() {
		testRender("aside{top:0}aside:hover{top:1}aside div{top:2}aside .class1{top:3}") {
			"aside" { top = 0 }
			"aside".hover { top = 1 }
			"aside".div { top = 2 }
			"aside"..".class1" { top = 3 }
		}
		testRender("""_::selection, .selector:not([attr*='']){top:0}.selector\{top:1}""") {
			"_::selection, .selector:not([attr*=''])" { top = 0 }
			".selector\\" { top = 1 }
		}
	}

	@Test fun selectors_atRules() {
		testRender("@media (min-width: 100px){div{width:1}}@media (min-width: 100px) and (orientation: landscape){div{width:2}}") {
			media("min-width: 100px") {
				div { width = 1 }
			}
			media("min-width: 100px", "orientation: landscape") {
				div { width = 2 }
			}
		}
		testRender("input{width:1}@media (min-width: 100px){div{width:2}div a{width:3}span{width:4}}textarea{width:5}") {
			input { width = 1 }
			media("min-width: 100px") {
				div {
					width = 2
					a { width = 3 }
				}
				span { width = 4 }
			}
			textarea { width = 5 }
		}

		testRender("div{width:1}@media (min-width: 100px){div a{width:2}div:hover{width:3}div *:hover{width:4}div *:hover:not(1){width:5}}") {
			div {
				width = 1
				media("min-width: 100px") {
					a { width = 2 }
					hover { width = 3 }
					any.hover {
						width = 4
						not(1) { width = 5 }
					}
				}
			}
		}
		testRender("@media (min-width: 100px){div{width:1}}@media (min-width: 100px){a{width:1}}") {
			(div and a) {
				media("min-width: 100px") {
					width = 1
				}
			}
		}
		testRender("@media (min-width: 100px){div{width:1}}@media (min-width: 100px){a{width:1}}@media (min-width: 200px){div{width:2}}@media (min-width: 200px){a{width:2}}") {
			(div and a) {
				media("min-width: 100px") {
					width = 1
				}
				media("min-width: 200px") {
					width = 2
				}
			}
		}

		testRender("@-webkit-keyframes animation1{from{top:0}30%{top:50px}68%,72%{top:70px}to{top:100px}}") {
			at("-webkit-keyframes animation1") {
				"from" { top = 0.px }
				"30%" { top = 50.px }
				"68%,72%" { top = 70.px }
				"to" { top = 100.px }
			}
		}
		testRender("@font-face{font-family:Bitstream Vera Serif Bold;src:url(VeraSeBd.ttf);font-weight:bold}@font-face{font-family:Graublau Web;src:url(GraublauWeb.eot);src:local('☺'), url('GraublauWeb.woff') format('woff'), url('GraublauWeb.ttf') format('truetype')}") {
			at("font-face") {
				fontFamily = "Bitstream Vera Serif Bold"
				src = url("VeraSeBd.ttf")
				fontWeight = BOLD
			}
			at("font-face") {
				fontFamily = "Graublau Web"
				src = url("GraublauWeb.eot")
				src = "local('☺'), url('GraublauWeb.woff') format('woff'), url('GraublauWeb.ttf') format('truetype')"
			}
		}

		testRender("@media (min-width: 100px) and (orientation: landscape){div{width:1}}") {
			myMediaQuery {
				div { width = 1 }
			}
		}
	}

	private fun Stylesheet.myMediaQuery(body: Stylesheet.()->Unit)
			= media("min-width: 100px", "orientation: landscape").invoke(body)


	@Test fun properties() {
		testRender("a{width:auto;height:10;font-size:14px;line-height:0;opacity:.2}") {
			a {
				width = AUTO
				height = 10
				color = null
				fontSize = 14.px
				lineHeight = 0
				background = null
				opacity = .2
			}
		}
		testRender("div{background:-moz-linear-gradient(top, #000 0%, #fff 100%);background:-webkit-linear-gradient(top, #000 0%, #fff 100%);background:linear-gradient(top bottom, #000 0%, #fff 100%)}") {
			div {
				background = "-moz-linear-gradient(top, #000 0%, #fff 100%)" // FF3.6+
				background = "-webkit-linear-gradient(top, #000 0%, #fff 100%)" // Chrome10+, Safari5.1+
				background = "linear-gradient(top bottom, #000 0%, #fff 100%)" // W3C
			}
		}
		testRender("""a:after{content:" ";content:"you're";content:"\"he said \"nice\"\""}""") {
			a.after {
				content = " "
				content = "you're"
				content = "\"he said \"nice\"\""
			}
		}
	}


	@Test fun dimensions() {
		testRender("a{width:auto}a{width:1px}a{width:.2em}a{width:50%}a{width:17.257ex}a{width:1.55555in}") {
			a { width = AUTO }
			a { width = 1.px }
			a { width = .2.em }
			a { width = 50f.percent }
			a { width = 17.257.ex }
			a { width = 1.55555f.inch }
		}
		testRender("a{padding:10px}a{padding:10px}a{padding:10px 20%}a{padding:10px 0 5px}a{padding:10px 0 5px 20px}a{padding:10px 10px 5px 0}") {
			a { padding = box(10.px) }
			a { padding = box(10) }
			a { padding = box(10.px, 20.percent) }
			a { padding = box(10.px, 0, "5px") }
			a { padding = box(10.px, 0, 5.px, 20.px) }
			a { padding = box(10, 10, 5, 0) }
		}
	}


	@Test fun colors() {
		assertEquals("#fff",                    hex("#fff").toString())
		assertEquals("#fff",                    hex("#ffffff").toString())
		assertEquals("#00000f",                 hex(0x00000f).toString())
		assertEquals("#001",                    hex(0x000011).toString())
		assertEquals("#000fff",                 hex(0x000FFf).toString())
		assertEquals("#011111",                 hex(0x011111).toString())
		assertEquals("#000aff",                 rgb(0,10,255).toString())
		assertEquals("#0ff",                    rgb(0,255,255).toString())
		assertEquals("rgba(0,10,255,0)",        rgba(0,10,255,0).toString())
		assertEquals("rgba(255,255,255,.47)",   rgba(255,255,255,0.47).toString())
		assertEquals("rgba(0,0,0,.019)",        rgba(0,0,0,0.019).toString())

		testRender("a{color:#fff}a{color:rgba(255,100,0,.196)}a{color:#f00}a{color:#00000f}a{color:#f2cacf}") {
			a { color = hex("#fff") }
			a { color = rgba(255,100,0,0.196) }
			a { color = "#f00" }
			a { color = 0x00000f }
			a { color = 0xf2cacf }
		}
	}
}

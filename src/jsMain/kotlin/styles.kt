/*
 * Copyright (c) 2022 Kyle Fricilone (https://kfricilone.me)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.FontWeight
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.borderRadius
import kotlinx.css.borderStyle
import kotlinx.css.borderWidth
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.em
import kotlinx.css.flex
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.flexWrap
import kotlinx.css.fontFamily
import kotlinx.css.fontSize
import kotlinx.css.fontWeight
import kotlinx.css.height
import kotlinx.css.lineHeight
import kotlinx.css.margin
import kotlinx.css.marginBottom
import kotlinx.css.marginRight
import kotlinx.css.marginTop
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.lh
import kotlinx.css.properties.textDecoration
import kotlinx.css.px
import kotlinx.css.rem
import kotlinx.css.textDecoration
import kotlinx.css.toCustomProperty
import kotlinx.css.width
import org.w3c.dom.HTMLElement
import styled.StyleSheet
import styled.injectGlobal

/**
 * Created by Kyle Fricilone on Jan 01, 2021.
 */

private const val fgCol: String = "gh-fg-col"
private const val bgCol: String = "gh-bg-col"
private const val borderCol: String = "gh-border-col"
private const val linkCol: String = "gh-link-col"

internal fun injectGhCss() {
    injectGlobal {
        +GhStyles.main
    }
}

internal fun injectGhTheme(dark: Boolean) {
    injectGlobal {
        root {
            setCustomProperty(fgCol, Color(if (dark) "#8b949e" else "#586069"))
            setCustomProperty(bgCol, Color(if (dark) "#0d1117" else "#fff"))
            setCustomProperty(borderCol, Color(if (dark) "#30363d" else "#e1e4e8"))
            setCustomProperty(linkCol, Color(if (dark) "#58a6ff" else "#0366d6"))
        }
    }
}

@JsName("switchGhTheme")
public fun switchGhTheme(element: HTMLElement, dark: Boolean) {
    element.style.setProperty("--$fgCol", if (dark) "#8b949e" else "#586069")
    element.style.setProperty("--$bgCol", if (dark) "#0d1117" else "#fff")
    element.style.setProperty("--$borderCol", if (dark) "#30363d" else "#e1e4e8")
    element.style.setProperty("--$linkCol", if (dark) "#58a6ff" else "#0366d6")
}

internal object GhStyles : StyleSheet("kard", isStatic = true) {

    internal val main by css {
        padding(1.rem)
        margin(.5.rem)
        borderWidth = 1.px
        borderRadius = 6.px
        borderStyle = BorderStyle.solid
        display = Display.flex
        flexDirection = FlexDirection.column
        fontSize = 1.rem
        fontFamily = "Roboto,-apple-system,BlinkMacSystemFont,Segoe UI,Arial,Oxygen,Droid Sans,Ubuntu,Cantarell,Open Sans,Helvetica Neue,sans-serif"

        put("color", fgCol.toCustomProperty())
        put("background-color", bgCol.toCustomProperty())
        put("border-color", borderCol.toCustomProperty())
    }

    internal val head by css {
        marginBottom = .75.rem
    }

    internal val body by css {
        flexGrow = 1.0
        fontSize = .875.rem
        marginBottom = 1.rem
    }

    internal val foot by css {
        margin(horizontal = (-.5).rem)
        display = Display.flex
        flexWrap = FlexWrap.wrap
        alignItems = Align.center
        fontSize = .875.rem
    }

    internal val title by css {
        display = Display.flex
        alignItems = Align.center
        marginBottom = (-0.1).rem
    }

    internal val icon by css {
        marginRight = .35.rem
        width = 1.em
        height = 1.em
        fontSize = 1.rem

        // kt-styled: svg props not yet supported
        put("fill", "currentColor")
    }

    internal val repo by css {
        margin(0.px)
        padding(0.px)
        fontSize = 1.rem
        fontFamily = "inherit"
        fontWeight = FontWeight.w500
        put("color", linkCol.toCustomProperty())

        hover {
            textDecoration(TextDecorationLine.underline)
        }
    }

    internal val forked by css {
        marginBottom = 1.rem
        fontSize = .75.rem

        children {
            color = Color.inherit
        }
    }

    internal val stat by css {
        margin(horizontal = (.5).rem)
        display = Display.flex
        alignItems = Align.center
        color = Color.inherit
        lineHeight = 1.rem.lh
        marginTop = .5.rem
    }

    internal val lang by css {
        marginRight = .25.em
        width = .75.rem
        height = .75.rem
        borderRadius = 100.pct
        backgroundColor = Color("#586069")
    }

    internal val update by css {
        flex(0.0, 0.0, 100.pct)
        marginTop = 1.em
        fontSize = .75.rem
    }

    internal val highlight by css {
        hover {
            put("color", linkCol.toCustomProperty())
        }
    }

    internal val noUnder by css {
        textDecoration = TextDecoration.none
    }
}

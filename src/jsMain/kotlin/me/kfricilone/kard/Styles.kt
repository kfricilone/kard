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
package me.kfricilone.kard

import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.Display
import kotlinx.css.Flex
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.FontWeight
import kotlinx.css.Margin
import kotlinx.css.Padding
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
import kotlinx.css.px
import kotlinx.css.rem
import kotlinx.css.textDecoration
import kotlinx.css.toCustomProperty
import kotlinx.css.width
import me.kfricilone.kard.constants.BACKGROUND_COLOR
import me.kfricilone.kard.constants.BACKGROUND_COLOR_DARK
import me.kfricilone.kard.constants.BACKGROUND_COLOR_LIGHT
import me.kfricilone.kard.constants.BORDER_COLOR
import me.kfricilone.kard.constants.BORDER_COLOR_DARK
import me.kfricilone.kard.constants.BORDER_COLOR_LIGHT
import me.kfricilone.kard.constants.FOREGROUND_COLOR
import me.kfricilone.kard.constants.FOREGROUND_COLOR_DARK
import me.kfricilone.kard.constants.FOREGROUND_COLOR_LIGHT
import me.kfricilone.kard.constants.LINK_COLOR
import me.kfricilone.kard.constants.LINK_COLOR_DARK
import me.kfricilone.kard.constants.LINK_COLOR_LIGHT
import styled.StyleSheet
import styled.injectGlobal

/**
 * Created by Kyle Fricilone on Jan 01, 2021.
 */

internal fun injectGhCss() {
    injectGlobal(
        CssBuilder(isStyledComponent = true).apply {
            +GhStyles.main
        },
    )
}

internal fun injectGhTheme(dark: Boolean) {
    injectGlobal(
        CssBuilder(isStyledComponent = true).apply {
            root {
                setCustomProperty(FOREGROUND_COLOR, Color(if (dark) FOREGROUND_COLOR_DARK else FOREGROUND_COLOR_LIGHT))
                setCustomProperty(BACKGROUND_COLOR, Color(if (dark) BACKGROUND_COLOR_DARK else BACKGROUND_COLOR_LIGHT))
                setCustomProperty(BORDER_COLOR, Color(if (dark) BORDER_COLOR_DARK else BORDER_COLOR_LIGHT))
                setCustomProperty(LINK_COLOR, Color(if (dark) LINK_COLOR_DARK else LINK_COLOR_LIGHT))
            }
        },
    )
}

@Suppress("MagicNumber")
internal object GhStyles : StyleSheet("kard", isStatic = true) {
    private val FONTS =
        """
        Roboto,-apple-system,BlinkMacSystemFont,Segoe UI,Arial,Oxygen,Droid Sans,Ubuntu,Cantarell,Open Sans,Helvetica Neue,sans-serif
        """.trimIndent()

    internal val main by css {
        padding = Padding(1.rem)
        margin = Margin(.5.rem)
        borderWidth = 1.px
        borderRadius = 6.px
        borderStyle = BorderStyle.solid
        display = Display.flex
        flexDirection = FlexDirection.column
        fontSize = 1.rem
        fontFamily = FONTS

        put("color", FOREGROUND_COLOR.toCustomProperty())
        put("background-color", BACKGROUND_COLOR.toCustomProperty())
        put("border-color", BORDER_COLOR.toCustomProperty())
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
        margin = Margin(horizontal = (-.5).rem)
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
        margin = Margin(0.px)
        padding = Padding(0.px)
        fontSize = 1.rem
        fontFamily = "inherit"
        fontWeight = FontWeight.w500
        put("color", LINK_COLOR.toCustomProperty())

        hover {
            textDecoration = TextDecoration(setOf(TextDecorationLine.underline))
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
        margin = Margin(horizontal = (.5).rem)
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
        flex = Flex(0.0, 0.0, 100.pct)
        marginTop = 1.em
        fontSize = .75.rem
    }

    internal val highlight by css {
        hover {
            put("color", LINK_COLOR.toCustomProperty())
        }
    }

    internal val noUnder by css {
        textDecoration = TextDecoration.none
    }
}

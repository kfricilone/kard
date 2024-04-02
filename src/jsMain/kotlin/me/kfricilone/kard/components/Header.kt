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
package me.kfricilone.kard.components

import me.kfricilone.kard.GhStyles
import me.kfricilone.kard.api.Repo
import me.kfricilone.kard.constants.ICON_SVG
import me.kfricilone.kard.dom.path
import react.RBuilder
import styled.css
import styled.styledA
import styled.styledDiv
import styled.styledH3
import styled.styledSpan
import styled.styledSvg

/**
 * Created by Kyle Fricilone on Jan 02, 2021.
 */

internal fun RBuilder.header(repo: Repo) {
    styledDiv {
        css {
            +GhStyles.head
        }

        title(repo)
        repo.parent?.let { forked(it) }
    }
}

private fun RBuilder.title(repo: Repo) {
    styledDiv {
        css {
            +GhStyles.title
        }

        styledSvg {
            css {
                +GhStyles.icon
            }

            attrs["view-box"] = "0 0 16 16"
            attrs["aria-hidden"] = "true"

            path {
                attrs["fillRule"] = "evenodd"
                attrs["d"] = ICON_SVG
            }
        }

        styledA(repo.url, "_blank") {
            css {
                +GhStyles.noUnder
            }

            styledH3 {
                css {
                    +GhStyles.repo
                }

                +repo.name
            }
        }
    }
}

private fun RBuilder.forked(repo: Repo) {
    styledSpan {
        css {
            +GhStyles.forked
        }

        +"Forked from "

        styledA(repo.url, "_blank") {
            css {
                +GhStyles.noUnder
                +GhStyles.highlight
            }

            +repo.fullName
        }
    }
}

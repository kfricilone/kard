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

import kotlinx.css.Color
import kotlinx.css.backgroundColor
import react.RBuilder
import react.dom.span
import styled.css
import styled.styledA
import styled.styledDiv
import styled.styledSpan
import styled.styledSvg

/**
 * Created by Kyle Fricilone on Jan 02, 2021.
 */

private const val LICENSE_SVG = "M8.75.75a.75.75 0 00-1.5 0V2h-.984c-.305 0-.604.08-.869.23l-1.288.737A.25.25 0 013.9" +
    "84 3H1.75a.75.75 0 000 1.5h.428L.066 9.192a.75.75 0 00.154.838l.53-.53-.53.53v.001l.002.002.002.002.006.006.016." +
    "015.045.04a3.514 3.514 0 00.686.45A4.492 4.492 0 003 11c.88 0 1.556-.22 2.023-.454a3.515 3.515 0 00.686-.45l.045" +
    "-.04.016-.015.006-.006.002-.002.001-.002L5.25 9.5l.53.53a.75.75 0 00.154-.838L3.822 4.5h.162c.305 0 .604-.08.869" +
    "-.23l1.289-.737a.25.25 0 01.124-.033h.984V13h-2.5a.75.75 0 000 1.5h6.5a.75.75 0 000-1.5h-2.5V3.5h.984a.25.25 0 0" +
    "1.124.033l1.29.736c.264.152.563.231.868.231h.162l-2.112 4.692a.75.75 0 00.154.838l.53-.53-.53.53v.001l.002.002.0" +
    "02.002.006.006.016.015.045.04a3.517 3.517 0 00.686.45A4.492 4.492 0 0013 11c.88 0 1.556-.22 2.023-.454a3.512 3.5" +
    "12 0 00.686-.45l.045-.04.01-.01.006-.005.006-.006.002-.002.001-.002-.529-.531.53.53a.75.75 0 00.154-.838L13.823 " +
    "4.5h.427a.75.75 0 000-1.5h-2.234a.25.25 0 01-.124-.033l-1.29-.736A1.75 1.75 0 009.735 2H8.75V.75zM1.695 9.227c.2" +
    "85.135.718.273 1.305.273s1.02-.138 1.305-.273L3 6.327l-1.305 2.9zm10 0c.285.135.718.273 1.305.273s1.02-.138 1.30" +
    "5-.273L13 6.327l-1.305 2.9z"
private const val STARS_SVG = "M8 .25a.75.75 0 01.673.418l1.882 3.815 4.21.612a.75.75 0 01.416 1.279l-3.046 2.97.719 " +
    "4.192a.75.75 0 01-1.088.791L8 12.347l-3.766 1.98a.75.75 0 01-1.088-.79l.72-4.194L.818 6.374a.75.75 0 01.416-1.28" +
    "l4.21-.611L7.327.668A.75.75 0 018 .25zm0 2.445L6.615 5.5a.75.75 0 01-.564.41l-3.097.45 2.24 2.184a.75.75 0 01.21" +
    "6.664l-.528 3.084 2.769-1.456a.75.75 0 01.698 0l2.77 1.456-.53-3.084a.75.75 0 01.216-.664l2.24-2.183-3.096-.45a." +
    "75.75 0 01-.564-.41L8 2.694v.001z"
private const val FORKS_SVG = "M5 3.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm0 2.122a2.25 2.25 0 10-1.5 0v.878A2.25 2.2" +
    "5 0 005.75 8.5h1.5v2.128a2.251 2.251 0 101.5 0V8.5h1.5a2.25 2.25 0 002.25-2.25v-.878a2.25 2.25 0 10-1.5 0v.878a." +
    "75.75 0 01-.75.75h-4.5A.75.75 0 015 6.25v-.878zm3.75 7.378a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm3-8.75a.75.75 0 1" +
    "00-1.5.75.75 0 000 1.5z"
private const val ISSUES_SVG = "M8 1.5a6.5 6.5 0 100 13 6.5 6.5 0 000-13zM0 8a8 8 0 1116 0A8 8 0 010 8zm9 3a1 1 0 11-" +
    "2 0 1 1 0 012 0zm-.25-6.25a.75.75 0 00-1.5 0v3.5a.75.75 0 001.5 0v-3.5z"

internal fun RBuilder.footer(repo: Repo, colors: Map<String, LangColor>) {
    styledDiv {
        css {
            +GhStyles.foot
        }

        repo.language?.let { language(it, colors) }
        repo.license?.let { license(it) }
        stars(repo)
        forks(repo)
        issues(repo)
        elapsed(repo.update)
    }
}

private fun RBuilder.language(lang: String, colors: Map<String, LangColor>) {
    styledDiv {
        css {
            +GhStyles.stat
        }

        styledSpan {
            css {
                +GhStyles.lang

                colors[lang]?.let {
                    if (it.color != null) backgroundColor = Color(it.color)
                }
            }
        }

        span {
            +lang
        }
    }
}

private fun RBuilder.license(license: License) {
    styledDiv {
        css {
            +GhStyles.stat
        }

        styledSvg {
            css {
                +GhStyles.icon
            }

            attrs["view-box"] = "0 0 16 16"
            attrs["aria-hidden"] = "true"

            path {
                attrs["fillRule"] = "evenodd"
                attrs["d"] = LICENSE_SVG
            }
        }

        span {
            +license.name
        }
    }
}

private fun RBuilder.stars(repo: Repo) {
    styledA("${repo.url}/stargazers", "_blank") {
        css {
            +GhStyles.stat
            +GhStyles.noUnder
            +GhStyles.highlight
        }

        styledSvg {
            css {
                +GhStyles.icon
            }

            attrs["view-box"] = "0 0 16 16"
            attrs["aria-hidden"] = "true"

            path {
                attrs["fillRule"] = "evenodd"
                attrs["d"] = STARS_SVG
            }
        }

        span {
            +format(repo.stars)
        }
    }
}

private fun RBuilder.forks(repo: Repo) {
    styledA("${repo.url}/network/members", "_blank") {
        css {
            +GhStyles.stat
            +GhStyles.noUnder
            +GhStyles.highlight
        }

        styledSvg {
            css {
                +GhStyles.icon
            }

            attrs["view-box"] = "0 0 16 16"
            attrs["aria-hidden"] = "true"

            path {
                attrs["fillRule"] = "evenodd"
                attrs["d"] = FORKS_SVG
            }
        }

        span {
            +format(repo.forks)
        }
    }
}

private fun RBuilder.issues(repo: Repo) {
    styledA("${repo.url}/issues", "_blank") {
        css {
            +GhStyles.stat
            +GhStyles.noUnder
            +GhStyles.highlight
        }

        styledSvg {
            css {
                +GhStyles.icon
            }

            attrs["view-box"] = "0 0 16 16"
            attrs["aria-hidden"] = "true"

            path {
                attrs["fillRule"] = "evenodd"
                attrs["d"] = ISSUES_SVG
            }
        }

        span {
            +format(repo.issues)
        }
    }
}

private fun RBuilder.elapsed(date: String) {
    styledDiv {
        css {
            +GhStyles.stat
            +GhStyles.update
        }

        span {
            +calcElapsed(date)
        }
    }
}

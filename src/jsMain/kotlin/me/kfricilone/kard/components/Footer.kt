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

import kotlinx.css.Color
import kotlinx.css.backgroundColor
import me.kfricilone.kard.GhStyles
import me.kfricilone.kard.api.GithubColor
import me.kfricilone.kard.api.License
import me.kfricilone.kard.api.Repo
import me.kfricilone.kard.constants.FORKS_SVG
import me.kfricilone.kard.constants.ISSUES_SVG
import me.kfricilone.kard.constants.LICENSE_SVG
import me.kfricilone.kard.constants.STARS_SVG
import me.kfricilone.kard.dom.path
import me.kfricilone.kard.utils.calcElapsed
import me.kfricilone.kard.utils.format
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

internal fun RBuilder.footer(
    repo: Repo,
    colors: Map<String, GithubColor>,
) {
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

private fun RBuilder.language(
    lang: String,
    colors: Map<String, GithubColor>,
) {
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

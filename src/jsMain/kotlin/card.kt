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

import react.Props
import react.RBuilder
import react.RComponent
import react.State

internal external interface CardProps : Props {
    var repo: Repo
    var colors: Map<String, LangColor>
}

internal external interface CardState : State {
    var repo: Repo
    var colors: Map<String, LangColor>
}

@JsExport
internal class Card(props: CardProps) : RComponent<CardProps, CardState>(props) {

    override fun CardState.init(props: CardProps) {
        repo = props.repo
        colors = props.colors
    }

    override fun RBuilder.render() {
        header(state.repo)
        body(state.repo)
        footer(state.repo, state.colors)
    }
}

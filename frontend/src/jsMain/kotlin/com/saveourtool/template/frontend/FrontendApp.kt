package com.saveourtool.template.frontend

import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import web.dom.document

val App: FC<Props> = FC {
    h1 {
        +"Authentication page"
    }
    div {
        button {
            +"GitHub"
        }
        button {
            +"Google"
        }
    }
}

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(container).render(App.create())
}
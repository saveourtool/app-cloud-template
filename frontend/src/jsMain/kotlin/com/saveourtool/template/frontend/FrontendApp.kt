/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2024-2024. All rights reserved.
 */

package com.saveourtool.template.frontend

import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import web.dom.document
import web.html.HTMLElement

val App: FC<Props> = FC {

}

fun main() {
    val mainDiv = document.getElementById("wrapper") as HTMLElement
    createRoot(mainDiv).render(App.create())
}
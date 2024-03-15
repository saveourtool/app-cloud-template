/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2024-2024. All rights reserved.
 */

package com.saveourtool.template.frontend.views

import js.errors.JsError
import react.FC
import react.dom.html.ReactHTML.div
import react.router.useRouteError

/**
 * @since 2024-03-15
 */
val errorView = FC {
    val errorMessage = useRouteError().unsafeCast<JsError>().message
    div {
      +errorMessage
    }
}
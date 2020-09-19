package com.flogmasters.flog.common.model


class FlogHeader(headerMap: Map<String, String>) {
    var headerMap: Map<String, String>? = null
        private set
    var contentType: String? = null
        private set
    var host: String? = null
        private set
    var isTest = false
        private set

    private fun setHeaderMap(headerMap: Map<String, String>) {
        this.headerMap = headerMap
        contentType = headerMap["content-type"]
        host = headerMap["host"]
    }

    init {
        setHeaderMap(headerMap)
    }
}

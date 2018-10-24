package com.liso.pedidosyatest.model

data class Restaurant(var total: Int = 0,
                      var max: Int = 0,
                      var sort: String? = null,
                      var count: Int = 0,
                      var data: MutableList<Data> = mutableListOf(),
                      var offset: Int = 0)
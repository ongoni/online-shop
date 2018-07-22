package com.ongoni.onlineshop.utils

import com.ongoni.onlineshop.entity.Product

fun Collection<Product>.serialized(detailed: Boolean = false, idOnly: Boolean = false) = this.map { it.serialized(detailed, idOnly) }.toList()

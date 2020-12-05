package com.easify.easify.util.storage

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface Storage {
  fun setString(key: String, value: String)
  fun getString(key: String): String
}

package com.easify.easify.util

import com.google.gson.Gson
import java.io.*
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

/**
 * @author: deniz.demirci
 * @date: 12.01.2021
 */

object ReadResponseFileUtil {

  @JvmStatic
  @Throws(Exception::class)
  fun <T> readResponse(
    testClassLoader: ClassLoader, responseType: Type?,
    fileName: String?, gson: Gson
  ): T {
    val file = File(testClassLoader.getResource(fileName).path)
    val reader = BufferedReader(
      InputStreamReader(
        FileInputStream(file),
        StandardCharsets.UTF_8)
    )
    return gson.fromJson(reader, responseType)
  }

  @Throws(IOException::class)
  fun readFile(testClassLoader: ClassLoader, fileName: String?): String {
    val file = File(testClassLoader.getResource(fileName).path)
    val reader = BufferedReader(
      InputStreamReader(
        FileInputStream(file),
        StandardCharsets.UTF_8)
    )
    val result = reader.readLine()
    reader.close()
    return result
  }
}

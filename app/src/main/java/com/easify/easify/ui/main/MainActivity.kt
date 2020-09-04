package com.easify.easify.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easify.easify.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}

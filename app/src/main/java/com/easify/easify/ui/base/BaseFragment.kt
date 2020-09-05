package com.easify.easify.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

open class BaseFragment : Fragment {

  constructor() : super()
  constructor(@LayoutRes resId: Int) : super(resId)
}
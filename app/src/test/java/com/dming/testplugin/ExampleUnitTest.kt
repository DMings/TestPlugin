package com.dming.testplugin

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

        val className = "com.dming.testplugin.PluginActivityS1"
        if (className.startsWith("com.dming.testplugin.PluginActivity")) {
            System.out.println("success")
            if(className.endsWith("$1")) {
                System.out.println("success1111")
            }else {
                System.out.println("false1111")
            }

        }else {
            System.out.println("false")
        }

    }
}

package com.choice.osviewmap.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import com.choice.design.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        Configuration.getInstance()
            .load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
        setContent {
            ApplicationTheme {
                MapNavigationHost(systemColor = it)
            }

        }
    }

}
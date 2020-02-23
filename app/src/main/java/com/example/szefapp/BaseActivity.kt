package com.example.szefapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.szefapp.ui.task.TaskListFragment
import com.facebook.stetho.Stetho

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TaskListFragment.newInstance())
                .commitNow()
        }
        Stetho.initializeWithDefaults(this);
    }

}

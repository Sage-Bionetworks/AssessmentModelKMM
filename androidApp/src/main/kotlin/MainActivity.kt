package org.sagebionetworks.assessmentmodel.sampleapp

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var rootLayout: LinearLayout by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = TextView(this)
        rootLayout = findViewById(R.id.main_view)
        rootLayout.addView(tv)
    }
}
package com.example.jongonzalez.filmica.view.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.jongonzalez.filmica.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            val fragment = DetailFragment()
            val id = intent.getStringExtra("id")
            val tag = intent.getStringExtra("tag")

            val args = Bundle()
            args.putString("id", id)
            args.putString("tag", tag)

            fragment.arguments = args


            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }

    }
}

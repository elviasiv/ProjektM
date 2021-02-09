package com.elviva.projektm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elviva.projektm.R
import com.elviva.projektm.databinding.ActivityTaskListBinding

class TaskListActivity : BaseActivity() {

    lateinit var binding: ActivityTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
package com.chrrissoft.workmanager.works

import android.content.Context
import android.widget.Toast
import com.chrrissoft.workmanager.request.state.OneTimeWorkRequestOwn

const val UNIQUE_NAME_TAG_PREFIX = "UNIQUE_NAME_TAG_PREFIX"

fun List<OneTimeWorkRequestOwn>.addUniqueNameAsTag(name: String) : List<OneTimeWorkRequestOwn> {
    return List(this.size) {
        this[it].copy(tags = this[it].tags.plusElement("$UNIQUE_NAME_TAG_PREFIX$name"))
    }
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.addRequestToEnqueueToast(){
    toast("Add request to enqueue")
}

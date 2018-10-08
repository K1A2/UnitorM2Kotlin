package com.unitor.k1a2.unitorm.file.sharedpreference

interface PreferenceKey {
    companion object {
        val KEY_REPOSITORY_INFO = "Info"
        val KEY_REPOSITORY_KILL = "Kill"

        val KEY_INFO_TITLE = "Title"
        val KEY_INFO_PRODUCER = "Producer"
        val KEY_INFO_CHAIN = "Chain"
        val KEY_INFO_PATH = "Path"

        val KEY_KILL_DIED = "Killed"
        val KEY_KILL_SELF = false
        val KEY_KILL_FORCE = true
    }
}
package com.unitor.k1a2.unitorm

interface FileKey {
    companion object {
        var KEY_FILE_INT:Int = 0
        var KEY_DIRECTORY_INT = 1

        var KEY_INFO_TITLE = "title="
        var KEY_INFO_PRODUCER = "producerName="
        var KEY_INFO_CHAIN = "chain="
        var KEY_INFO_CONTENT = "title=%s\nproducerName=%s\nbuttonX=8\nbuttonY=8\nchain=%s\nsquareButton=true\nlandscape=true"

        var KEY_DELETE_UNIPACK = "deleteUnipack"
        var KEY_COPY_SOUND = "copySound"
        var KEY_COPY_LED = "copyLED"
    }
}
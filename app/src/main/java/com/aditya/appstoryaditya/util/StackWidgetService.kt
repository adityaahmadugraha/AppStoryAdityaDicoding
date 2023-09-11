package com.aditya.appstoryaditya.util

import android.content.Intent
import android.widget.RemoteViewsService
import com.aditya.appstoryaditya.util.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {

        return StackRemoteViewsFactory(this.applicationContext)
    }
}
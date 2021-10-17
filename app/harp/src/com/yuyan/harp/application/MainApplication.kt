package com.yuyan.harp.application

import com.yuyan.harp.data.driver.ResultState
import droid.app.Application
import droid.server.pm.InstallManager
import droid.server.pm.PackageManager

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        println("MainApplication onCreate in kotlin")
        initResultDataRepository()
    }

    private fun initResultDataRepository() {
        val im = getSystemService(INSTALL_SERVICE) as InstallManager
        val pm = getSystemService(PACKAGE_SERVICE) as PackageManager
        val packageInfo = pm.getPackageInfo(Thread.currentThread().name)
        val jarFilePath = packageInfo.jarPackage.filePath
        val resultCodePropPath = "properties/web.result.code.properties"
        val propStream = im.getResourceStream(jarFilePath, resultCodePropPath)
        ResultState.init(propStream)
    }
}
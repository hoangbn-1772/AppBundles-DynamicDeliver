package com.example.appbundlesample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val splitInstallManager by lazy { SplitInstallManagerFactory.create(this) }

    private lateinit var request: SplitInstallRequest

    private var sessionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponent()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_download_feature_1 -> downloadFeatureModule(DYNAMIC_FEATURE_MODULE_1)
            R.id.button_goto_feature_1 -> startDynamicFeature(CLASS_NAME_MODULE_1)
            R.id.button_download_feature_2 -> downloadFeatureModule(DYNAMIC_FEATURE_MODULE_2)
            R.id.button_goto_feature_2 -> startDynamicFeature(CLASS_NAME_MODULE_2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == REQUIRES_USER_CONFIRMATION) {
            // Handle the user's decision
            Log.d("TAG", "ResultCode: $resultCode")
        }
    }

    private fun initComponent() {
        button_download_feature_1.setOnClickListener(this)
        button_goto_feature_1.setOnClickListener(this)
        button_download_feature_2.setOnClickListener(this)
        button_goto_feature_2.setOnClickListener(this)
    }

    /* Option 1: Use SplitInstallManager.startInstall(). Require App in Foreground*/
    private fun downloadFeatureModule(nameModule: String) {

        request = SplitInstallRequest.newBuilder()
                .addModule(nameModule)
                .build()
        /*
        * Can instead by splitInstallManager.deferredInstall()
        * Purpose: If you want to defer installation for when the app is in background
        */
        splitInstallManager.startInstall(request)
                .addOnSuccessListener { sessionId ->
                    this.sessionId = sessionId
                    Log.d("TAG", "SessionId: ${this.sessionId}")
                }
                .addOnFailureListener { exception ->
                    when (((exception as SplitInstallException).errorCode)) {
                        SplitInstallErrorCode.NETWORK_ERROR -> showMessage(resources.getString(R.string.network_error))
                        SplitInstallErrorCode.MODULE_UNAVAILABLE -> showMessage(resources.getString(R.string.module_unavailable))
                        SplitInstallErrorCode.INVALID_REQUEST -> showMessage(resources.getString(R.string.invalid_request))
                        SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> checkForActiveDownloads()
                        /*,etc...*/
                    }
                }
                .addOnCompleteListener { }

        listenRequest()
    }

    /*
    * Listen request and handle state updates
    */
    private fun listenRequest() {
        /*Listen request status updates*/

        this.splitInstallManager.registerListener { state ->
            if (state.errorCode() == SplitInstallErrorCode.SERVICE_DIED) {
                // Retry the request
                return@registerListener
            }

            if (state.sessionId() == this.sessionId) {
                when (state.status()) {
                    SplitInstallSessionStatus.PENDING -> {
                        // The request has been accepted and the download should start soon.
                    }
                    SplitInstallSessionStatus.DOWNLOADING -> {
                        showMessage(resources.getString(R.string.downloading))
                        // update progressBar
                        val totalBytes = state.totalBytesToDownload()
                        val progress = state.bytesDownloaded()
                    }
                    SplitInstallSessionStatus.DOWNLOADED -> showMessage(resources.getString(R.string.downloaded))
                    SplitInstallSessionStatus.INSTALLED -> {
                        // After installed, you can start accessing it. Fire an Intent
                        showMessage(resources.getString(R.string.installed))
                    }
                    SplitInstallSessionStatus.INSTALLING -> showMessage(resources.getString(R.string.installing))
                    SplitInstallSessionStatus.CANCELING -> showMessage(resources.getString(R.string.canceling))
                    SplitInstallSessionStatus.CANCELED -> showMessage(resources.getString(R.string.installed))
                    SplitInstallSessionStatus.FAILED -> {
                        // Retry the request
                    }
                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                        /*
                       * Displays a dialog for user "Download" or "Cancel" >10MB
                       * Params:
                       *   + Download -> request status changes to:  PENDING
                       *   + Cancel -> CANCELED
                       *   + Do not choose -> requestCode default
                       */
                        splitInstallManager.startConfirmationDialogForResult(state, this, REQUIRES_USER_CONFIRMATION)
                    }
                }
            }
        }
    }

    /*Cancel a request before it is installed*/
    private fun cancelRequest() {
        splitInstallManager.cancelInstall(this.sessionId)
    }

    /*unregister the listener*/
    private fun unregister() {
        splitInstallManager.unregisterListener { }
    }

    /*To check currently installed dynamic feature modules on the device*/
    private fun checkFeatureInstalled() {
        val installedModules = splitInstallManager.installedModules
        Log.d("TAG", "Number: ${installedModules.size}")
    }

    /*To uninstall module*/
    private fun uninstallModule() {
        splitInstallManager.deferredUninstall(listOf(DYNAMIC_FEATURE_MODULE_2))
    }

    /*Check if there are nay request that are still downloading*/
    private fun checkForActiveDownloads() {
        splitInstallManager.sessionStates
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (state in task.result) {
                            if (state.status() == SplitInstallSessionStatus.DOWNLOADING) {
                                // Dang co yeu cau tai xuong
                                cancelRequest()
                            }
                        }
                    }
                }
    }

    private fun startDynamicFeature(className: String) {
        Intent(this@MainActivity, Class.forName(className)).apply {
            startActivity(this)
        }
    }

    private fun Context.showMessage(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    companion object {
        private const val DYNAMIC_FEATURE_MODULE_1 = "dynamic_feature"

        private const val DYNAMIC_FEATURE_MODULE_2 = "dynamic_feature2"

        private const val CLASS_NAME_MODULE_1 = "com.example.dynamic_feature.DynamicActivity"

        private const val CLASS_NAME_MODULE_2 = "com.example.dynamic_feature2.MainActivity"

        private const val REQUIRES_USER_CONFIRMATION = 1
    }
}

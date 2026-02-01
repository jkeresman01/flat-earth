package com.keresman.flatearth

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.keresman.flatearth.databinding.ActivitySplashScreenBinding
import com.keresman.flatearth.framework.applyAnimation
import com.keresman.flatearth.framework.getProperty
import com.keresman.flatearth.framework.isOnline
import com.keresman.flatearth.framework.startActivity
import com.keresman.flatearth.utils.callWithDelay
import com.keresman.flatearth.extensions.showRateLimitDialog
import com.keresman.flatearth.worker.NasaWorker

const val DATA_IMPORTED = "com.keresman.flatearth.is_data_imported"
private const val DELAY_MS = 3000L

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val receivers = mutableListOf<BroadcastReceiver>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivSplash.applyAnimation(R.anim.blink)
        binding.tvSplash.applyAnimation(R.anim.rotate)
    }

    private fun redirect() {
        when {
            getProperty<Boolean>(DATA_IMPORTED, false) -> navigateToHost()
            isOnline() -> fetchData()
            else -> showNoInternet()
        }
    }

    private fun navigateToHost() {
        callWithDelay(DELAY_MS) {
            startActivity<HostActivity>()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun fetchData() {
        val filter = IntentFilter(NasaReceiver.ACTION)
        val successReceiver = NasaReceiver()

        val errorFilter = IntentFilter(NasaReceiver.ACTION_ERROR)
        val errorReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                onRateLimitError()
            }
        }

        //This is beautiful, just beautiful
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(successReceiver, filter, RECEIVER_NOT_EXPORTED)
            registerReceiver(errorReceiver, errorFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(successReceiver, filter)
            registerReceiver(errorReceiver, errorFilter)
        }
        receivers += successReceiver
        receivers += errorReceiver

        WorkManager.getInstance(this).enqueueUniqueWork(
            DATA_IMPORTED,
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequest.from(NasaWorker::class.java)
        )
    }

    override fun onDestroy() {
        receivers.forEach { unregisterReceiver(it) }
        receivers.clear()
        super.onDestroy()
    }

    private fun stopAnimations() {
        binding.ivSplash.clearAnimation()
        binding.ivSplash.visibility = View.INVISIBLE
        binding.tvSplash.clearAnimation()
        binding.tvSplash.visibility = View.INVISIBLE
    }

    private fun onRateLimitError() {
        stopAnimations()
        showRateLimitDialog { startActivity<HostActivity>() }
    }

    private fun showNoInternet() {
        binding.tvSplash.text = getString(R.string.no_internet)
        callWithDelay(DELAY_MS) { finish() }
    }
}

//
// NOTE: this has been simplified with callWithDelay
//
//           Handler(Looper.getMainLooper()).postDelayed(
//{
                    // startActivity(String::class.java)
                    // extension function like this will break at runtime
                    // we want this startActivity<HostActivity>() -- type safety is important here
                    // Arrays are aware of their type on the heap, you can't trick them on the heap but you can at compile time
                    // List<String> != List<Object>, generics can't exists at runtime but only at compile time
                    // (non-reified) when we are on the heap List<String> isn't aware of it's type (type erasure)
//                   startActivity<HostActivity>()
//              },
//             DELAY_MS
//
// callWithDelay(DELAY_MS, { finish()})
//If runnable/lamba is last param can be moved out
// -- while is reserved keyword
// while(DELAY_MS) {
//    finish()
// }
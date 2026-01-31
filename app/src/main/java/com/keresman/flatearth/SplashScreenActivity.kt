package com.keresman.flatearth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.keresman.flatearth.databinding.ActivitySplashScreenBinding
import com.keresman.flatearth.framework.applyAnimation
import com.keresman.flatearth.framework.callWithDelay
import com.keresman.flatearth.framework.getProperty
import com.keresman.flatearth.framework.isOnline
import com.keresman.flatearth.framework.startActivity
import com.keresman.flatearth.worker.NasaWorker

const val DATA_IMPORTED = "com.keresman.flatearth.is_data_imported"

private const val DELAY_MS = 3000L

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

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
        if (getProperty<Boolean>(DATA_IMPORTED, false)) {
            callWithDelay(DELAY_MS) {
                startActivity<HostActivity>()
            }

            /*
            NOTE: this has been simplified with callWithDelay

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    // startActivity(String::class.java)
                    // extension function like this will break at runtime
                    // we want this startActivity<HostActivity>() -- type safety is important here
                    // Arrays are aware of their type on the heap, you can't trick them on the heap but you can at compile time
                    // List<String> != List<Object>, generics can't exists at runtime but only at compile time
                    // (non-reified) when we are on the heap List<String> isn't aware of it's type (type erasure)
                    startActivity<HostActivity>()
                },
                DELAY_MS
            )
            */
        } else {
            if (isOnline()) {
                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.from(NasaWorker::class.java)
                    )
                }
            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callWithDelay(DELAY_MS) { finish() }

                // callWithDelay(DELAY_MS, { finish()})
                //If runnable/lamba is last param can be moved out
                // -- while is reserved keyword
                // while(DELAY_MS) {
                //    finish()
                // }

            }
        }
    }


}

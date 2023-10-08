package com.recoveryrecord.surveyandroid.example

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupNotificationPolicyAccessPreference()
        setupPhysicalActivityPermissionPreference()
        setupStoragePermissionPreference()
        setupBatteryOptimizationPreference()
    }

    private fun setupNotificationPolicyAccessPreference() {
        val clearPref2 = findPreference<Preference>("notification_policy_access")
        clearPref2?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val mNotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!mNotificationManager.isNotificationPolicyAccessGranted) {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    startActivity(intent)
                } else {
                    showToast("權限已經開啟")
                }
            } else {
                showToast("Android 8 以下版本不需要開此權限")
            }
            true
        }
    }

    private fun setupPhysicalActivityPermissionPreference() {
        val clearPref0 = findPreference<Preference>("PhysicalActivity")
        clearPref0?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (isActivityRecognitionPermissionNotGranted()) {
                    requestActivityRecognitionPermission()
                } else {
                    showToast("權限已經開啟")
                }
            } else {
                showToast("Android 10 以下版本不需要開此權限")
            }
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun isActivityRecognitionPermissionNotGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestActivityRecognitionPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            1
        )
    }

    private fun setupStoragePermissionPreference() {
        val clearPref1 = findPreference<Preference>("CSV")
        clearPref1?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isStoragePermissionNotGranted()) {
                    requestStoragePermission()
                } else {
                    showToast("權限已經開啟")
                }
            }
            true
        }
    }

    private fun isStoragePermissionNotGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

    @SuppressLint("BatteryLife")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupBatteryOptimizationPreference() {
        val clearPref3 = findPreference<Preference>("battery_optimization")
        clearPref3?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent()
            val packageName = requireContext().packageName
            val pm = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                requireContext().startActivity(intent)
            } else {
                showToast("權限已經開啟")
            }
            true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
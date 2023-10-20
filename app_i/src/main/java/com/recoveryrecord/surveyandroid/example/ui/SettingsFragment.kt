package com.recoveryrecord.surveyandroid.example.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.model.PermissionType
import com.recoveryrecord.surveyandroid.example.util.isPermissionGranted
import com.recoveryrecord.surveyandroid.example.util.requestPermission

class SettingsFragment : PreferenceFragmentCompat() {
    //    private val notificationManager: NotificationManager by lazy {
//        activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    }
    private val powerManager: PowerManager by lazy {
        context?.getSystemService(Context.POWER_SERVICE) as PowerManager
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?,
    ) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupNotificationPermissionPreference()
        setupNotificationPolicyAccessPreference()
        setupPhysicalActivityPermissionPreference()
//        setupStoragePermissionPreference()
        setupBatteryOptimizationPreference()
    }

    private fun setupNotificationPolicyAccessPreference() {
        val clearPref2 = findPreference<Preference>(getString(R.string.notification_policy_access))
        clearPref2?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                startActivity(intent)
//            if (notificationManager.isNotificationListenerAccessGranted().not()) {
// //                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
//
// //                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
//                startActivity(intent)
//            } else {
//                showToast(getString(R.string.access_granted))
//            }
                true
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupNotificationPermissionPreference() {
        val clearPref0 = findPreference<Preference>(getString(R.string.notification_permission))
        clearPref0?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                if (activity?.isPermissionGranted(PermissionType.NOTIFICATION_PERMISSION)
                        ?.not() == true
                ) {
                    activity?.requestPermission(PermissionType.NOTIFICATION_PERMISSION)
                } else {
                    showToast(getString(R.string.access_granted))
                }
                true
            }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupPhysicalActivityPermissionPreference() {
        val clearPref0 = findPreference<Preference>(getString(R.string.physical_activity))
        clearPref0?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                if (activity?.isPermissionGranted(PermissionType.ACTIVITY_RECOGNITION)
                        ?.not() == true
                ) {
                    activity?.requestPermission(PermissionType.ACTIVITY_RECOGNITION)
                } else {
                    showToast(getString(R.string.access_granted))
                }
                true
            }
    }

//    private fun setupStoragePermissionPreference() {
//        val clearPref1 = findPreference<Preference>(getString(R.string.storage_access))
//        clearPref1?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//            if (isStoragePermissionNotGranted()) {
//                requestStoragePermission()
//            } else {
//                showToast(getString(R.string.access_granted))
//            }
//            true
//        }
//    }

//    private fun isStoragePermissionNotGranted(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) != PackageManager.PERMISSION_GRANTED
//    }

//    private fun requestStoragePermission() {
//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            1
//        )
//    }

    @SuppressLint("BatteryLife")
    private fun setupBatteryOptimizationPreference() {
        val clearPref3 = findPreference<Preference>(getString(R.string.battery_optimization))
        clearPref3?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                val intent = Intent()
                val packageName = context?.packageName
                if (powerManager.isIgnoringBatteryOptimizations(packageName)) {
                    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    intent.data = Uri.parse("package:$packageName")
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    showToast(getString(R.string.access_granted))
                }
                true
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

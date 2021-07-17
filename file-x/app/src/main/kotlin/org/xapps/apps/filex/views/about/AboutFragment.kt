package org.xapps.apps.filex.views.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.filex.BuildConfig
import org.xapps.apps.filex.R
import org.xapps.apps.filex.databinding.FragmentAboutBinding
import org.xapps.apps.filex.views.extensions.setStatusBarForegoundColor
import javax.inject.Inject


@AndroidEntryPoint
class AboutFragment @Inject constructor(): Fragment() {

    private lateinit var bindings: FragmentAboutBinding

    private val onBackPressedCallback: OnBackPressedCallback by lazy {
        object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentAboutBinding.inflate(layoutInflater)
        bindings.lifecycleOwner = viewLifecycleOwner
        bindings.version =  BuildConfig.VERSION_NAME

        bindings.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        bindings.btnAboutLink.setOnClickListener {
            launchUri(getString(R.string.project_github))
        }

        bindings.btnLinkGoogleFonts.setOnClickListener {
            launchUri(getString(R.string.rubik_url))
        }

        bindings.btnLinkMaterialDesignIcons.setOnClickListener {
            launchUri(getString(R.string.material_design_icons_url))
        }

        bindings.btnLinkAndroidJetpack.setOnClickListener {
            launchUri(getString(R.string.android_jetpack_url))
        }

        bindings.btnLinkAndroidKotlin.setOnClickListener {
            launchUri(getString(R.string.android_kotlin_url))
        }

        bindings.btnLinkDexterPermissions.setOnClickListener {
            launchUri(getString(R.string.dexter_url))
        }

        bindings.btnLinkToasty.setOnClickListener {
            launchUri(getString(R.string.toasty_url))
        }

        bindings.btnLinkMaterialProgressBar.setOnClickListener {
            launchUri(getString(R.string.materialprogressbar_url))
        }

        bindings.btnLinkGroupie.setOnClickListener {
            launchUri(getString(R.string.groupie_url))
        }

        bindings.btnLinkTimber.setOnClickListener {
            launchUri(getString(R.string.timber_url))
        }

        return bindings.root
    }

    private fun launchUri(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarForegoundColor(isLightStatusBar = true)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
}
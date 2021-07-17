package org.xapps.apps.filex.views.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.utils.info
import org.xapps.apps.filex.databinding.FragmentSplashBinding
import org.xapps.apps.filex.views.extensions.navigationBarColor
import org.xapps.apps.filex.views.extensions.setStatusBarForegoundColor
import org.xapps.apps.filex.views.utils.Message
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment @Inject constructor(): Fragment() {

    private lateinit var bindings: FragmentSplashBinding

    private val viewModel: SplashViewModel by viewModels()

    private var messageJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentSplashBinding.inflate(layoutInflater)
        bindings.lifecycleOwner = viewLifecycleOwner

        val theme = requireContext().theme
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val colorPrimary = typedValue.data
        navigationBarColor(colorPrimary)

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageJob = lifecycleScope.launchWhenResumed {
            viewModel.messageFlow
                .collect {
                    info<SplashFragment>("Message received $it")
                    if(it is Message.Success) {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToExplorerFragment())
                    } else {

                    }
                }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            context?.let {
                viewModel.prepareApp()
            }
        }, 1000)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarForegoundColor(isLightStatusBar = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messageJob?.cancel()
        messageJob = null
    }
}
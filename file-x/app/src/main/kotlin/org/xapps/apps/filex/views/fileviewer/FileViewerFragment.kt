package org.xapps.apps.filex.views.fileviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.filex.databinding.FragmentFileViewerBinding
import javax.inject.Inject


@AndroidEntryPoint
class FileViewerFragment @Inject constructor(): Fragment() {

    private lateinit var bindings: FragmentFileViewerBinding

    private val viewModel: FileViewerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentFileViewerBinding.inflate(layoutInflater)
        bindings.lifecycleOwner = viewLifecycleOwner
        return bindings.root
    }

}
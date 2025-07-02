package com.example.pixaura.ui.view.fragments.wallpaperView

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.pixaura.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WallpaperBottomSheetFragment : BottomSheetDialogFragment() {

    enum class WallpaperOption {
        HOME, LOCK, BOTH
    }

    private var onOptionSelected: ((WallpaperOption) -> Unit)? = null

    fun setOnOptionSelectedListener(listener: (WallpaperOption) -> Unit) {
        onOptionSelected = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        view.findViewById<CardView>(R.id.home).setOnClickListener {
            onOptionSelected?.invoke(WallpaperOption.HOME)
            dismiss()
        }

        view.findViewById<CardView>(R.id.lock).setOnClickListener {
            onOptionSelected?.invoke(WallpaperOption.LOCK)
            dismiss()
        }

        view.findViewById<CardView>(R.id.both).setOnClickListener {
            onOptionSelected?.invoke(WallpaperOption.BOTH)
            dismiss()
        }

        return view
    }


}

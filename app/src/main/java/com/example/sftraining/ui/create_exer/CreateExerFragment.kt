package com.example.sftraining.ui.create_exer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.bumptech.glide.Glide
import com.example.sftraining.R
import com.example.sftraining.globalviewmodels.ExersViewModel
import com.example.sftraining.model.Exer
import com.example.sftraining.ui.main.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.create_exer_fragment.*
import kotlin.math.abs

class CreateExerFragment : Fragment() {

    private lateinit var btnCreateExer: MaterialButton
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var btnAddTitleImage: FloatingActionButton
    private lateinit var titleImage: AppCompatImageView
    private lateinit var btnAddStartImage: MaterialButton
    private lateinit var btnAddMainImage: MaterialButton
    private lateinit var btnAddEndImage: MaterialButton

    private val exersViewModel: ExersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.create_exer_fragment, container, false)

        initView(root)

        initPickPhotoListeners()

        btnCreateExer.setOnClickListener {
            val activity = activity as MainActivity
            activity.startLoadingAnimation()

            //TODO CREATE EXER
            val exer = Exer()

            exersViewModel.createExer(
                exer,
                onSuccess = {
                    activity.stopLoadingAnimation()
                    findNavController(this).navigate(R.id.navListOfExers)

                },
                onFailure = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )
        }

        //toolbar animation
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                toolbar.background =
                    resources.getDrawable(R.color.colorPrimary, null)
            } else {
                toolbar.background =
                    resources.getDrawable(R.drawable.background_edit_text, null)
            }
        })

        return root
    }

    private fun pickPhotoFromGallery(view: View) {

        val getPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (view is ShapeableImageView) {
                Glide.with(view).load(it)
                    .into(view.apply {
                        val cornerSize: Float = resources.getDimension(R.dimen.cornerRadius)
                        this.shapeAppearanceModel =
                            this.shapeAppearanceModel.toBuilder()
                                .setAllCornerSizes(cornerSize).build()
                    })
            } else {
                Glide.with(titleImage).load(it).into(titleImage)
            }

        }
        getPhoto.launch("image/*")
    }


    private fun initPickPhotoListeners() {
        btnAddTitleImage.setOnClickListener {
            pickPhotoFromGallery(titleImage)
        }

        btnAddStartImage.setOnClickListener {
            pickPhotoFromGallery(ce_image_view_content_start)
        }

        btnAddMainImage.setOnClickListener {
            pickPhotoFromGallery(ce_image_view_content_main)
        }

        btnAddEndImage.setOnClickListener {
            pickPhotoFromGallery(ce_image_view_content_end)
        }
    }

    private fun initView(root: View) {
        btnCreateExer = root.findViewById(R.id.btn_create_exer)
        collapsingToolbarLayout = root.findViewById(R.id.ce_collapsing_layout)
        appBarLayout = root.findViewById(R.id.ce_app_bar_layout)
        toolbar = root.findViewById(R.id.ce_toolbar)
        btnAddTitleImage = root.findViewById(R.id.ce_fab_add_photo)
        titleImage = root.findViewById(R.id.ce_title_photo)
        btnAddStartImage = root.findViewById(R.id.ce_button_add_content_start)
        btnAddMainImage = root.findViewById(R.id.ce_button_add_content_main)
        btnAddEndImage = root.findViewById(R.id.ce_button_add_content_end)
    }
}
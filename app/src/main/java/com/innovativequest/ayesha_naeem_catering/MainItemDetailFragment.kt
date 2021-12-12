package com.innovativequest.ayesha_naeem_catering

import android.content.ClipData
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.innovativequest.ayesha_naeem_catering.data.MainDishesItem
import com.innovativequest.ayesha_naeem_catering.data.StartersItem
import com.innovativequest.ayesha_naeem_catering.databinding.FragmentMainItemDetailBinding
import com.innovativequest.ayesha_naeem_catering.placeholder.PlaceholderContent
import com.innovativequest.ayesha_naeem_catering.databinding.FragmentStarterItemDetailBinding
import com.innovativequest.ayesha_naeem_catering.utils.Utils
import com.squareup.picasso.Picasso

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class MainItemDetailFragment : Fragment(), RequestListener<Drawable> {

    /**
     * The placeholder content this fragment is presenting.
     */

    private lateinit var itemImage: AppCompatImageView
    private lateinit var itemNameTv: AppCompatTextView
    private lateinit var itemBodyText1Tv: AppCompatTextView

    private lateinit var mainItem: MainDishesItem

    private var _binding: FragmentMainItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
//            item = PlaceholderContent.ITEM_MAP[dragData]
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                val itemId = it.getString(ARG_ITEM_ID)

                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                val mainList = Utils.mainDishesFromLocalJson("fakedata/main_dishes.json", context)

                for(item in mainList){
                    if(item.id == itemId) {
                        mainItem = item
                        break
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        itemImage = binding.itemDetailIv!!
        itemNameTv = binding.itemNameTv!!
        itemBodyText1Tv = binding.bodyItem1Tv!!

        updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        itemNameTv.text = mainItem.title
        itemBodyText1Tv.text = mainItem.description
        context?.let {
            Picasso.get()
                .load(mainItem.heroImg)
                .resize(200, 160)
                .centerCrop()
                .into(itemImage)
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        return true
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        return true
    }
}
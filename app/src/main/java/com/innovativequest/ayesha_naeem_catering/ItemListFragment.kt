package com.innovativequest.ayesha_naeem_catering

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.innovativequest.ayesha_naeem_catering.data.MealItem
import com.innovativequest.ayesha_naeem_catering.data.StartersItem
import com.innovativequest.ayesha_naeem_catering.placeholder.PlaceholderContent;
import com.innovativequest.ayesha_naeem_catering.databinding.FragmentItemListBinding
import com.innovativequest.ayesha_naeem_catering.databinding.ItemListContentBinding
import com.innovativequest.ayesha_naeem_catering.utils.Utils
import com.squareup.picasso.Picasso

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class ItemListFragment : Fragment() {

    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    private val unhandledKeyEventListenerCompat =
        ViewCompat.OnUnhandledKeyEventListenerCompat { v, event ->
            if (event.keyCode == KeyEvent.KEYCODE_Z && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            } else if (event.keyCode == KeyEvent.KEYCODE_F && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            false
        }

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat)

        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.nav_host_fragment_item_detail)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as MealItem

            val bundle = Bundle()

            if (itemDetailFragmentContainer != null) {
                if(item is StartersItem){
                    bundle.putString(
                        StarterItemDetailFragment.ARG_ITEM_ID,
                        item.id
                    )
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.show_starter_item_detail, bundle)
                }
                else{
                    bundle.putString(
                        MainItemDetailFragment.ARG_ITEM_ID,
                        item.id
                    )
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.show_main_item_detail, bundle)
                }

            } else {
                if(item is StartersItem) {
                    bundle.putString(
                        StarterItemDetailFragment.ARG_ITEM_ID,
                        item.id
                    )
                    itemView.findNavController().navigate(R.id.show_starter_item_detail, bundle)
                }
                else
                {
                    bundle.putString(
                        MainItemDetailFragment.ARG_ITEM_ID,
                        item.id
                    )
                    itemView.findNavController().navigate(R.id.show_main_item_detail, bundle)
                }
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag as MealItem
            Toast.makeText(
                v.context,
                "Context click of item " + item.id,
                Toast.LENGTH_LONG
            ).show()
            true
        }
        val mainDishesList = Utils.mainDishesFromLocalJson("fakedata/main_dishes.json", context)
        val startersList = Utils.starterDishesFromLocalJson("fakedata/starters.json", context)
        val mealItemsList: List<MealItem> =  startersList + mainDishesList
        setupRecyclerView(mealItemsList, recyclerView, onClickListener, onContextClickListener)
    }

    private fun setupRecyclerView(
        mealItemsList: List<MealItem>,
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onContextClickListener: View.OnContextClickListener
    ) {

        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            context,
            mealItemsList,
            onClickListener,
            onContextClickListener
        )
    }

    class SimpleItemRecyclerViewAdapter(
        private val context: Context?,
        private val mealItemsList: List<MealItem>,
        private val onClickListener: View.OnClickListener,
        private val onContextClickListener: View.OnContextClickListener
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>(),
        RequestListener<Drawable> {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val binding =
                ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mealItemsList[position]

            context?.let {
                Picasso.get()
                    .load(item.imgUrl)
                    .resize(200, 160)
                    .centerCrop()
                    .into(holder.itemImage)

                holder.itemName.text = item.title
                holder.itemSummary.text = item.summary
            }

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setOnContextClickListener(onContextClickListener)
                }

                setOnLongClickListener { v ->
                    // Setting the item id as the clip data so that the drop target is able to
                    // identify the id of the content
                    val clipItem = ClipData.Item(item.id)
                    val dragData = ClipData(
                        v.tag as? CharSequence,
                        arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                        clipItem
                    )

                    if (Build.VERSION.SDK_INT >= 24) {
                        v.startDragAndDrop(
                            dragData,
                            View.DragShadowBuilder(v),
                            null,
                            0
                        )
                    } else {
                        v.startDrag(
                            dragData,
                            View.DragShadowBuilder(v),
                            null,
                            0
                        )
                    }
                }
            }
        }

        override fun getItemCount() = mealItemsList.size

        inner class ViewHolder(binding: ItemListContentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val itemImage: ImageView = binding.avatar
            val itemName: TextView = binding.name
            val itemSummary: TextView = binding.summary
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
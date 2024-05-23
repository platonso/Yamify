package com.platonso.yamify.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.platonso.yamify.R
import com.platonso.yamify.data.Favourites

class RecipeAdapter(options: FirestoreRecyclerOptions<Favourites>, val context: Context) :
        FirestoreRecyclerAdapter<Favourites, RecipeAdapter.RecipeViewHolder>(options) {

    override fun onBindViewHolder(@NonNull holder: RecipeViewHolder, position: Int, model: Favourites) {
        holder.titleTextView.text = model.title

        holder.itemView.setOnClickListener {
            val activity = it.context as AppCompatActivity
            val recipeViewModel = ViewModelProvider(activity).get(RecipeViewModel::class.java)
            model.title?.let { it1 -> recipeViewModel.setTitleFavourites(it1) }
            model.content?.let { it1 -> recipeViewModel.setContentFavourites(it1) }
            val docId = snapshots.getSnapshot(position).id
            recipeViewModel.setDocId(docId)
            val favouritesDetailsFragment = FavouritesDetailsFragment()
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, favouritesDetailsFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_favourites_item, parent, false)
        return RecipeViewHolder(view)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.recipe_title_text_view)
    }
}
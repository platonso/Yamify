package com.platonso.yamify.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.api.Context;
import com.google.firebase.firestore.FirebaseFirestore;
import com.platonso.yamify.R;
import com.platonso.yamify.data.Favourites;

public class RecipeAdapter extends FirestoreRecyclerAdapter<Favourites, RecipeAdapter.RecipeViewHolder> {

    Context context;

    public RecipeAdapter(@NonNull FirestoreRecyclerOptions<Favourites> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int i, @NonNull Favourites favourites) {
        holder.titleTextView.setText(favourites.getTitle());

        holder.itemView.setOnClickListener((v) -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            RecipeViewModel recipeViewModel = new ViewModelProvider(activity).get(RecipeViewModel.class);
            recipeViewModel.setTitleFavourites(favourites.getTitle());
            recipeViewModel.setContentFavourites(favourites.getContent());
            String docId = this.getSnapshots().getSnapshot(i).getId();
            recipeViewModel.setDocId(docId);
            FavouritesDetailsFragment favouritesDetailsFragment = new FavouritesDetailsFragment();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, favouritesDetailsFragment)
                    .addToBackStack(null)  // Добавляем в стек обратного вызова
                    .commit();
        });
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_favourites_item, parent, false);
        return new RecipeViewHolder(view);
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contentTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.recipe_title_text_view);


        }
    }
}

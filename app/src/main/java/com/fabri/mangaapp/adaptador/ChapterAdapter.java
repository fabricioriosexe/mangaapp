// adaptador/ChapterAdapter.java
package com.fabri.mangaapp.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fabri.mangaapp.R;
import com.fabri.mangaapp.ReadingActivity; // Asegúrate de tener o crear esta actividad
import com.fabri.mangaapp.model.Chapter;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private Context context;
    private List<Chapter> chapterList;

    public ChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false); // Crea este layout
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.chapterTitleTextView.setText(chapter.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadingActivity.class);
            intent.putExtra("chapterId", chapter.getId()); // Asumiendo que tu modelo Chapter tiene un getId()
            intent.putExtra("animeTitle", /* Puedes pasar el título del anime si lo necesitas */ "");
            intent.putExtra("chapterTitle", chapter.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTitleTextView;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            chapterTitleTextView = itemView.findViewById(R.id.chapterTitleTextView); // Asegúrate de tener este ID en item_chapter.xml
        }
    }
}
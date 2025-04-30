package com.fabri.mangaapp.adapter; // Asegúrate de que este sea el paquete correcto
/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter; // Usamos ListAdapter para eficiencia
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fabri.mangaapp.databinding.ItemChapterBinding; // Importar View Binding para el layout del item de capítulo
import com.fabri.mangaapp.model.Chapter; // Importar tu modelo Chapter
import com.fabri.mangaapp.model.ChapterAttributes; // Importar ChapterAttributes si es necesario (para getDisplayTitleOrNumber, etc.)
import com.fabri.mangaapp.model.Relationship; // Importar Relationship si es necesario (para getScanlationGroupName)


public class ChapterAdapter extends ListAdapter<Chapter, ChapterAdapter.ChapterViewHolder> {

    // Interfaz para manejar clics en los items de capítulo
    public interface OnChapterClickListener {
        void onChapterClick(String chapterId); // Pasa el ID del capítulo al hacer clic
    }

    private final OnChapterClickListener listener; // Referencia al listener

    // Constructor del adaptador que recibe el listener de clic
    public ChapterAdapter(OnChapterClickListener listener) {
        super(DIFF_CALLBACK); // Usa el DiffUtil.ItemCallback específico para Chapter
        this.listener = listener; // Asignar el listener
    }

    // Callback para DiffUtil para comparar items de capítulo eficientemente
    private static final DiffUtil.ItemCallback<Chapter> DIFF_CALLBACK = new DiffUtil.ItemCallback<Chapter>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chapter oldItem, @NonNull Chapter newItem) {
            // Comparar por ID único (UUID del capítulo)
            return oldItem.getId() != null && newItem.getId() != null && oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chapter oldItem, @NonNull Chapter newItem) {
            // Comparar el contenido visualmente relevante del capítulo.
            // Si algún objeto (Chapter, Attributes) es null en una versión pero no en la otra, no son iguales.
            if (oldItem.getAttributes() == null || newItem.getAttributes() == null) {
                return oldItem.getAttributes() == newItem.getAttributes(); // Solo son iguales si ambos son null
            }

            // Comparamos el número/título mostrado
            boolean titleSame = oldItem.getAttributes().getDisplayTitleOrNumber().equals(newItem.getAttributes().getDisplayTitleOrNumber());

            // Comparamos el grupo de escaneo (usando el helper en Chapter, que maneja relaciones nulas)
            boolean groupSame = oldItem.getScanlationGroupName().equals(newItem.getScanlationGroupName());

            // Puedes añadir más comparaciones si otras cosas se muestran en el layout del item
            // boolean langSame = oldItem.getAttributes().getTranslatedLanguage().equals(newItem.getAttributes().getTranslatedLanguage()); // Si muestras el idioma

            return titleSame && groupSame; // && langSame
            // Nota: Evitamos oldItem.equals(newItem) a menos que equals() esté implementado correctamente en Chapter, Attributes, Relationship
        }
    };

    // Crea nuevos ViewHolders (se llama cuando el RecyclerView necesita un nuevo ViewHolder)
    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item de capítulo usando View Binding
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChapterBinding binding = ItemChapterBinding.inflate(inflater, parent, false);
        // Crea y devuelve una nueva instancia de ChapterViewHolder, pasando el binding
        return new ChapterViewHolder(binding);
    }

    // Reemplaza el contenido de un ViewHolder existente (se llama para mostrar datos)
    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        // Obtener el objeto Chapter para esta posición usando getItem() de ListAdapter
        Chapter chapter = getItem(position);
        // Llamar a la función bind del ViewHolder, pasando el objeto Chapter y el listener
        holder.bind(chapter, listener); // Pasamos el listener aquí
    }

    // --- ViewHolder: Contiene las referencias a las vistas de un solo item de capítulo ---
    class ChapterViewHolder extends RecyclerView.ViewHolder {
        private final ItemChapterBinding binding;

        // Constructor del ViewHolder que recibe el binding
        public ChapterViewHolder(ItemChapterBinding binding) {
            super(binding.getRoot()); // La vista raíz del layout del item
            this.binding = binding;
            // El listener de clic se configura DENTRO del método bind
        }

        // Función para enlazar los datos de un objeto Chapter con las vistas del item
        // Recibe el objeto Chapter y el listener de clic
        public void bind(Chapter chapter, OnChapterClickListener onChapterClickListener) {

            // 1. Asignar datos a los TextViews del item de capítulo
            // Usamos los helpers en Chapter y ChapterAttributes para acceder a los datos de forma segura
            if (chapter != null && chapter.getAttributes() != null) {
                binding.chapterTitleNumberTextView.setText(chapter.getAttributes().getDisplayTitleOrNumber()); // Título o número (usa el helper que creaste)
                // Nota: getScanlationGroupName es un helper que probablemente creaste en tu modelo Chapter
                binding.chapterScanlationGroupTextView.setText("Por: " + chapter.getScanlationGroupName()); // Grupo de escaneo
                // Puedes añadir más TextViews si muestras más info (fecha, idioma, etc.)
                // binding.chapterLanguageTextView.setText(chapter.getAttributes().getTranslatedLanguage());
            } else {
                // Manejar caso donde el objeto Chapter o sus atributos son null
                binding.chapterTitleNumberTextView.setText("Capítulo Desconocido/Error");
                binding.chapterScanlationGroupTextView.setText("");
            }


            // 2. Configurar el listener de clic en todo el item del capítulo (la vista raíz)
            itemView.setOnClickListener(v -> {
                // Llama al listener si no es null y si el ID del capítulo es válido
                if (onChapterClickListener != null && chapter != null && chapter.getId() != null) {
                    onChapterClickListener.onChapterClick(chapter.getId()); // Pasa el ID del capítulo
                }
            });
        }
    }
}

/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/
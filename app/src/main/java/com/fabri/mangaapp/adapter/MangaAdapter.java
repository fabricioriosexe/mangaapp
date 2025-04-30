package com.fabri.mangaapp.adapter;

import android.util.Log; // Importar Log para depuración
import android.view.LayoutInflater;
import android.view.View; // Importar View si es necesario (aunque itemView ya es una View)
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Para cargar imágenes
import com.fabri.mangaapp.R; // Asegúrate de que R sea importado para acceder a los drawables
import com.fabri.mangaapp.databinding.ItemMangaSearchResultBinding; // Para View Binding
import com.fabri.mangaapp.model.Manga; // Tu modelo Manga
import com.fabri.mangaapp.model.Relationship; // Tu modelo Relationship

public class MangaAdapter extends ListAdapter<Manga, MangaAdapter.MangaViewHolder> {

    private static final String TAG = "MangaAdapter"; // Etiqueta para Logcat

    // Interfaz para manejar clics en los items
    public interface OnItemClickListener {
        void onItemClick(Manga manga);
    }

    private final OnItemClickListener listener;

    // Constructor del adaptador que recibe el listener de clic
    public MangaAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK); // Usa el DiffUtil.ItemCallback
        this.listener = listener;
    }

    // Callback para DiffUtil para comparar items de forma eficiente
    private static final DiffUtil.ItemCallback<Manga> DIFF_CALLBACK = new DiffUtil.ItemCallback<Manga>() {
        @Override
        public boolean areItemsTheSame(@NonNull Manga oldItem, @NonNull Manga newItem) {
            // Compara por ID único para ver si son el mismo "elemento" en la lista
            return oldItem.getId() != null && newItem.getId() != null && oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Manga oldItem, @NonNull Manga newItem) {
            // Compara el contenido visualmente relevante para ver si la vista necesita actualizarse
            // ¡Corregido para no depender de equals() por defecto!
            boolean titleSame = oldItem.getAttributes() != null && newItem.getAttributes() != null &&
                    oldItem.getAttributes().getDisplayTitle().equals(newItem.getAttributes().getDisplayTitle());

            boolean descriptionSame = oldItem.getAttributes() != null && newItem.getAttributes() != null &&
                    oldItem.getAttributes().getDisplayDescription().equals(newItem.getAttributes().getDisplayDescription());

            // Puedes añadir comparación para la portada si cambian los nombres de archivo de portada:
            // String oldCoverFileName = oldItem.getCoverFileNameHelper(); // Necesitarías un helper en Manga
            // String newCoverFileName = newItem.getCoverFileNameHelper(); // que busque la relacion y devuelva el fileName
            // boolean coverSame = oldCoverFileName != null ? oldCoverFileName.equals(newCoverFileName) : newCoverFileName == null;

            // Retorna true si los contenidos visibles son los mismos
            return titleSame && descriptionSame; // && coverSame si añades la comparación de portada
        }
        // Nota: Si creas un helper getCoverFileNameHelper() en Manga para acceder al fileName
        // desde la lista de relaciones, úsalo aquí. Si no, esta comparación es más compleja.
        // Por ahora, comparar título y descripción es suficiente para resolver la advertencia
        // y es válido para DiffUtil si esas son las únicas cosas que cambian.
    };

    // Crea nuevos ViewHolders (se llama cuando el RecyclerView necesita un nuevo ViewHolder)
    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item usando View Binding
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMangaSearchResultBinding binding = ItemMangaSearchResultBinding.inflate(inflater, parent, false);
        // Crea y devuelve una nueva instancia de MangaViewHolder, pasando el binding
        return new MangaViewHolder(binding); // Ya no pasamos el listener aquí
    }

    // Reemplaza el contenido de un ViewHolder existente (se llama para mostrar datos)
    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        // Obtener el objeto Manga para esta posición
        Manga manga = getItem(position);
        // Llamar a la función bind del ViewHolder, pasando el objeto Manga y el listener
        holder.bind(manga, listener); // <-- Pasamos el listener aquí
    }

    // --- ViewHolder: Contiene las referencias a las vistas de un solo item ---
    class MangaViewHolder extends RecyclerView.ViewHolder {
        private final ItemMangaSearchResultBinding binding;
        // Ya no necesitamos almacenar el listener aquí si lo pasamos en bind

        // Constructor del ViewHolder que recibe el binding
        public MangaViewHolder(ItemMangaSearchResultBinding binding) {
            super(binding.getRoot()); // La vista raíz del layout del item
            this.binding = binding;
            // El listener de clic se configura DENTRO del método bind para cada item
            // No ponemos el listener aquí en el constructor
        }

        // Función para enlazar los datos de un objeto Manga con las vistas del item
        // Recibe el objeto Manga y el listener de clic para este item específico
        public void bind(Manga manga, OnItemClickListener onItemClickListener) {

            // 1. Asignar el título y descripción a los TextViews
            // Asegúrate de que los IDs (mangaTitleTextView, mangaDescriptionTextView) sean correctos
            binding.mangaTitleTextView.setText(manga.getAttributes().getDisplayTitle()); // Establece el título usando el helper
            binding.mangaDescriptionTextView.setText(manga.getAttributes().getDisplayDescription()); // Establece la descripción usando el helper

            // 2. Cargar la imagen de portada usando Glide
            Relationship coverArtRelationship = null;
            if (manga.getRelationships() != null) {
                for (Relationship rel : manga.getRelationships()) {
                    // Busca la relación de tipo "cover_art"
                    if ("cover_art".equals(rel.getType())) {
                        coverArtRelationship = rel;
                        break; // Salir del bucle una vez encontrada
                    }
                }
            }

            String coverFileName = null;
            if (coverArtRelationship != null) {
                // Usa el helper en Relationship para obtener el nombre del archivo
                coverFileName = coverArtRelationship.getCoverFileName();
            }

            // Construir la URL completa de la imagen de portada
            String coverImageUrl = null; // Inicializar la URL a null
            if (manga.getId() != null && coverFileName != null) {
                // Formato de URL de portada: https://uploads.mangadex.org/covers/{mangaId}/{coverFileName}.{quality}.jpg
                // Usamos ".512.jpg" para una versión de tamaño intermedio común
                coverImageUrl = "https://uploads.mangadex.org/covers/" + manga.getId() + "/" + coverFileName + ".512.jpg";
            }

            // --- Lógica de Carga con Glide y Depuración ---
            if (coverImageUrl != null) {
                // **LOGGING:** Imprime la URL que intentamos cargar
                Log.d(TAG, "Intentando cargar imagen para manga ID: " + manga.getId() + " URL: " + coverImageUrl);

                Glide.with(binding.mangaCoverImageView.getContext()) // Usa el contexto del ImageView
                        .load(coverImageUrl) // Carga la URL de la imagen
                        .placeholder(R.drawable.placeholder_image) // <-- Muestra este drawable mientras carga (Paso de Depuración)
                        .error(R.drawable.error_image)     // <-- Muestra este drawable si la carga falla (Paso de Depuración)
                        .into(binding.mangaCoverImageView); // Carga la imagen en el ImageView
            } else {
                // **LOGGING:** Imprime si no se pudo construir la URL
                Log.d(TAG, "No se pudo construir URL de imagen para manga ID: " + manga.getId() + ". coverFileName: " + coverFileName);

                // Si no hay URL válida, limpia el ImageView (si tenía algo antes)
                Glide.with(binding.mangaCoverImageView.getContext()).clear(binding.mangaCoverImageView);
                // Y muestra el drawable de error (Paso de Depuración)
                binding.mangaCoverImageView.setImageResource(R.drawable.error_image); // <-- Muestra este drawable si no hay portada
            }
            // --- Fin de la lógica de carga y depuración ---


            // 3. Configurar el listener de clic en toda la tarjeta (la vista raíz del ViewHolder)
            itemView.setOnClickListener(v -> {
                // Llama al listener que se pasó al método bind, dándole el objeto Manga de este item
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(manga);
                }
            });

            /**
             * Codigo Hecho
             * Por
             * Fabricio Rios
             * fabriciorios0103@gmail.com
             * Git Hub
             * https://github.com/fabricioriosexe*/
        }
    }
}
package com.fabri.mangaapp; // Asegúrate de que este sea el paquete correcto de tu Activity
/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View; // Importar View para View.VISIBLE/GONE
import android.widget.Toast; // Importar Toast

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
// import androidx.core.widget.NestedScrollView; // Usar NestedScrollView si tu layout usa <androidx.core.widget.NestedScrollView>

import com.bumptech.glide.Glide; // Para cargar imágenes
// Importar la clase de View Binding generada para este layout
import com.fabri.mangaapp.databinding.ActivityMangaDetailBinding;
// Importar tus modelos específicos
import com.fabri.mangaapp.model.Manga;
import com.fabri.mangaapp.model.Chapter;
import com.fabri.mangaapp.model.Relationship;
// Si necesitas RelationshipAttributes porque tu modelo lo define, impórtalo específicamente:
// import com.fabri.mangaapp.model.RelationshipAttributes;


// Importar el ViewModel de Detalles
import com.fabri.mangaapp.viewmodel.MangaDetailViewModel;

// Importar el Adaptador para Capítulos y su interfaz de listener
import com.fabri.mangaapp.adapter.ChapterAdapter;
import com.fabri.mangaapp.adapter.ChapterAdapter.OnChapterClickListener; // Importar la interfaz


// --- La Activity implementa la interfaz del listener del adaptador ---
public class MangaDetailActivity extends AppCompatActivity implements OnChapterClickListener {

    private static final String TAG = "MangaDetailActivity";

    private ActivityMangaDetailBinding binding; // Referencia al binding
    private MangaDetailViewModel viewModel; // Referencia al ViewModel

    private ChapterAdapter chapterAdapter; // Adaptador para la lista de capítulos

    private String mangaId; // Para almacenar el ID del manga actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Infla el layout usando View Binding
        binding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Establece la vista raíz del binding como contenido

        // 1. Obtener el ID del manga del Intent
        // Asegúrate de usar la MISMA KEY ("mangaId") que usas en MainActivity para pasar el ID.
        mangaId = getIntent().getStringExtra("mangaId"); // Usamos "mangaId"

        if (mangaId != null) {
            Log.d(TAG, "Manga ID recibido: " + mangaId);
            // 2. Inicializar el ViewModel
            viewModel = new ViewModelProvider(this).get(MangaDetailViewModel.class);

            // 3. Configurar el RecyclerView
            setupChaptersRecyclerView();

            // 4. Observar los LiveData del ViewModel
            observeViewModel();

            // 5. Iniciar la carga de datos
            viewModel.loadMangaDetails(mangaId); // Cargar detalles del manga
            viewModel.loadChapterList(mangaId); // <-- ¡Llamar también para cargar capítulos!

        } else {
            // Manejar caso donde no se pasó el ID del manga
            Log.e(TAG, "No mangaId received in Intent");
            // Usar View Binding para acceder a la vista de estado
            binding.statusTextView.setText("Error: No se recibió el ID del manga.");
            binding.statusTextView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Error: No se encontró el ID del manga.", Toast.LENGTH_SHORT).show(); // Opcional
            // finish(); // Puedes optar por cerrar la Activity si no hay ID
        }
    }

    private void setupChaptersRecyclerView() {
        // 1. Crear el Adaptador de Capítulos
        // Pasamos 'this' porque esta Activity implementa OnChapterClickListener
        chapterAdapter = new ChapterAdapter(this); // Pasa 'this' como el listener

        // 2. Configurar el LayoutManager y asignar el adaptador
        binding.chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.chaptersRecyclerView.setAdapter(chapterAdapter);

        // Opcional: Mejorar rendimiento si el tamaño del contenido del RecyclerView no cambia
        binding.chaptersRecyclerView.setHasFixedSize(true);
    }


    private void observeViewModel() {
        // 1. Observar los detalles del manga
        viewModel.getMangaDetails().observe(this, manga -> {
            if (manga != null) {
                Log.d(TAG, "Manga details received. Updating UI.");
                // Actualizar las vistas con los detalles del manga usando View Binding
                // Asegúrate de que los nombres de las vistas en tu XML y binding coincidan

                if (manga.getAttributes() != null) {
                    // Actualizar TextView del título
                    // Usa getDisplayTitle() si es un helper en MangaAttributes, o accede al mapa:
                    binding.detailTitleTextView.setText(manga.getAttributes().getDisplayTitle());

                    // Actualizar TextView de la descripción
                    // Usa getDisplayDescription() si es un helper en MangaAttributes, o accede al mapa:
                    binding.detailDescriptionTextView.setText(manga.getAttributes().getDisplayDescription());

                    // TODO: Actualizar otros TextViews con tags, etc. si los tienes en el layout
                }


                // Cargar la imagen de portada
                String coverUrl = getCoverArtUrl(manga); // Usar un método helper para obtener la URL
                if (coverUrl != null) {
                    Glide.with(this)
                            .load(coverUrl)
                            .placeholder(R.drawable.placeholder_image) // Placeholder (asegúrate de tenerlo)
                            .error(R.drawable.error_image)       // Imagen de error (asegúrate de tenerla)
                            .into(binding.detailCoverImageView); // Carga en el ImageView grande
                } else {
                    Glide.with(this).clear(binding.detailCoverImageView); // Limpiar Glide si no hay URL
                    binding.detailCoverImageView.setImageResource(R.drawable.error_image); // Mostrar imagen por defecto o error
                }

            } else {
                Log.d(TAG, "Manga details received are null.");
                // Opcional: Mostrar un mensaje de error si los detalles son null
                // Solo si no hay ya un mensaje de error activo del ViewModel
                if (viewModel.getStatusMessage().getValue() == null || !viewModel.getStatusMessage().getValue().startsWith("Error:")) {
                    binding.statusTextView.setText("No se pudieron cargar los detalles del manga.");
                    binding.statusTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        // 2. Observar la lista de capítulos (Controla la visibilidad del RecyclerView)
        viewModel.getChapterList().observe(this, chapterList -> {
            // Actualizar los datos del Adaptador de capítulos
            if (chapterList != null) {
                Log.d(TAG, "Chapter list received. Count: " + chapterList.size());
                chapterAdapter.submitList(chapterList); // Usar submitList de ListAdapter

                // --- ¡Lógica principal de visibilidad del RecyclerView! ---
                // El RecyclerView es visible solo si la lista no es nula Y no está vacía.
                boolean isListVisible = !chapterList.isEmpty();
                binding.chaptersRecyclerView.setVisibility(isListVisible ? View.VISIBLE : View.GONE);

                // --- Manejar el mensaje de estado/no capítulos ---
                if (isListVisible) {
                    // Si hay capítulos, ocultar el TextView de estado (a menos que sea un error)
                    if (binding.statusTextView.getVisibility() == View.VISIBLE && !binding.statusTextView.getText().toString().startsWith("Error:")) {
                        binding.statusTextView.setVisibility(View.GONE);
                    }
                } else {
                    // Si la lista está vacía, mostrar mensaje "No hay capítulos disponibles"
                    // Solo si no hay ya un mensaje de error activo del ViewModel
                    if (viewModel.getStatusMessage().getValue() == null || !viewModel.getStatusMessage().getValue().startsWith("Error:")) {
                        binding.statusTextView.setText("No hay capítulos disponibles.");
                        binding.statusTextView.setVisibility(View.VISIBLE);
                    }
                }

            } else {
                // Si la lista de capítulos es null (por ejemplo, por error en la API), ocultar RecyclerView y mostrar mensaje
                Log.d(TAG, "Chapter list received is null.");
                binding.chaptersRecyclerView.setVisibility(View.GONE);
                if (viewModel.getStatusMessage().getValue() == null || !viewModel.getStatusMessage().getValue().startsWith("Error:")) {
                    binding.statusTextView.setText("No se pudo cargar la lista de capítulos.");
                    binding.statusTextView.setVisibility(View.VISIBLE);
                }
            }
        });


        // 3. Observar el estado de carga (Controla la visibilidad del ProgressBar y el contenido general)
        viewModel.getIsLoading().observe(this, isLoading -> {
            boolean loading = isLoading != null && isLoading;
            // Mostrar/ocultar la barra de progreso
            binding.detailLoadingProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

            // Ocultar el contenido principal (la ScrollView raíz) mientras carga
            // Esto evita que veas datos parciales mientras aún se carga.
            if (binding.getRoot() != null) {
                // binding.getRoot() es la ScrollView
                binding.getRoot().setVisibility(loading ? View.GONE : View.VISIBLE);
            }

            // --- ¡CORRECCIÓN AQUÍ! Ya NO ocultamos el RecyclerView en este observador cuando loading es false ---
            // El estado final de visibilidad del RecyclerView se maneja SOLAMENTE en el observador de chapterList.
            // Lo mantenemos oculto AQUÍ solo mientras loading es TRUE,
            // pero su visibilidad cuando loading es FALSE depende de si hay capítulos en la lista.
            if (loading) {
                binding.chaptersRecyclerView.setVisibility(View.GONE);
                // Si pones texto "Cargando...", ponlo aquí:
                // if (viewModel.getStatusMessage().getValue() == null || !viewModel.getStatusMessage().getValue().startsWith("Error:") && !binding.statusTextView.getText().toString().startsWith("No hay")) {
                //      binding.statusTextView.setText("Cargando...");
                //      binding.statusTextView.setVisibility(View.VISIBLE);
                // }
            } // else { // Cuando loading es false, no tocamos la visibilidad del RecyclerView aquí. // }


        });

        // 4. Observar mensajes de estado/error (Controla la visibilidad del TextView de estado para errores)
        viewModel.getStatusMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Log.e(TAG, "Status/Error Message: " + message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show(); // Mostrar mensaje más visible

                // Mostrar el mensaje de error en binding.statusTextView
                binding.statusTextView.setText("Error: " + message);
                binding.statusTextView.setVisibility(View.VISIBLE);

                // Asegurarse de que el ProgressBar de carga esté oculto si hay un error
                binding.detailLoadingProgressBar.setVisibility(View.GONE);
                // Asegurarse de que la ScrollView se muestre si hay un error (para ver el mensaje)
                if (binding.getRoot() != null) {
                    binding.getRoot().setVisibility(View.VISIBLE);
                }
                // Asegurarse de que el RecyclerView esté oculto si hay un error
                binding.chaptersRecyclerView.setVisibility(View.GONE); // Ocultar la lista si hay un error

            } else {
                // Si el mensaje está vacío o nulo (no hay error), ocultar el TextView de estado
                // a menos que estemos mostrando el mensaje "No hay capítulos disponibles".
                if (!binding.statusTextView.getText().toString().startsWith("No hay capítulos disponibles")) {
                    binding.statusTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    // --- Implementación del método de la interfaz OnChapterClickListener ---
    @Override
    public void onChapterClick(String chapterId) {
        if (chapterId != null) {
            String chapterUrl = "https://mangadex.org/chapter/" + chapterId;
            Log.d(TAG, "Chapter clicked. ID: " + chapterId + ", Opening URL: " + chapterUrl);
            Intent intent = new Intent(this, ReaderActivity.class);
            intent.putExtra(ReaderActivity.EXTRA_CHAPTER_URL, chapterUrl);
            startActivity(intent);
        } else {
            Log.e(TAG, "Clicked chapter has null ID.");
            Toast.makeText(this, "Error: No se puede obtener el ID del capítulo.", Toast.LENGTH_SHORT).show();
        }
    }

    // --- Método helper para obtener la URL de la portada (Usando el helper de Relationship) ---
    private String getCoverArtUrl(Manga manga) {
        if (manga == null || manga.getRelationships() == null || manga.getId() == null) {
            return null;
        }
        String coverFileName = null;
        for (Relationship rel : manga.getRelationships()) {
            coverFileName = rel.getCoverFileName(); // Usa el método helper de tu modelo Relationship
            if (coverFileName != null) {
                break;
            }
        }
        if (coverFileName != null) {
            return "https://uploads.mangadex.org/covers/" + manga.getId() + "/" + coverFileName + ".512.jpg";
        }
        return null;
    }
    // Nota: Tu layout activity_manga_detail.xml usa <ScrollView> como raíz.
    // El código que usa binding.getRoot() y lo trata como View es correcto para ScrollView.
}
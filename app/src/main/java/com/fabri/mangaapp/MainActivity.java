package com.fabri.mangaapp;
/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Importar View
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast; // Opcional
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider; // Importar ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; // Importar RecyclerView
import com.fabri.mangaapp.model.Manga; // Asegúrate de importar tu clase Manga
import com.fabri.mangaapp.databinding.ActivityMainBinding; // Importar View Binding
import com.fabri.mangaapp.adapter.MangaAdapter; // Necesitas crear este adaptador (Paso 8)
import com.fabri.mangaapp.viewmodel.MangaSearchViewModel; // Importar tu ViewModel

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // Para View Binding
    private MangaSearchViewModel viewModel; // Referencia al ViewModel

    private MangaAdapter mangaAdapter; // Adaptador para el RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Infla el layout con View Binding
        setContentView(binding.getRoot());

        // Inicializar el ViewModel usando ViewModelProvider
        viewModel = new ViewModelProvider(this).get(MangaSearchViewModel.class);

        setupViews();
        setupRecyclerView();
        observeViewModel();
    }

    private void setupViews() {
        // Configurar el listener para la acción del teclado "Buscar" en el EditText
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.searchEditText.getText().toString().trim();
                viewModel.performSearch(query); // Llamar a la función de búsqueda del ViewModel
                hideKeyboard(); // Ocultar el teclado
                return true; // Indica que hemos manejado la acción
            }
            return false; // Dejar que el sistema maneje otras acciones
        });

        // Opcional: Si añadiste un botón de búsqueda en el XML
        /*
        binding.searchButton.setOnClickListener(v -> {
            String query = binding.searchEditText.getText().toString().trim();
            viewModel.performSearch(query);
            hideKeyboard();
        });
        */
    }

    private void setupRecyclerView() {
        mangaAdapter = new MangaAdapter(new MangaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Manga manga) {
                Intent detailIntent = new Intent(MainActivity.this, MangaDetailActivity.class);

                // --- ¡CORRECCIÓN AQUÍ! Cambia la clave a "mangaId" ---
                detailIntent.putExtra("mangaId", manga.getId()); // <--- Clave corregida a "mangaId"

                startActivity(detailIntent);
            }
        });


        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Un layout lineal (lista vertical)
        binding.searchResultsRecyclerView.setAdapter(mangaAdapter); // Asignar el adaptador
    }

    private void observeViewModel() {
        // Observar la lista de resultados
        viewModel.getSearchResults().observe(this, mangaList -> {
            // Este código se ejecuta cada vez que searchResults en el ViewModel cambia
            mangaAdapter.submitList(mangaList); // Actualizar los datos del adaptador (submitList es de ListAdapter)

            // Mostrar u ocultar el RecyclerView
            // isVisible() de Kotlin es más limpio, en Java usamos setVisibility
            binding.searchResultsRecyclerView.setVisibility(
                    (mangaList != null && !mangaList.isEmpty()) ? View.VISIBLE : View.GONE);
        });

        // Observar el estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            // Mostrar/ocultar la barra de progreso
            binding.loadingProgressBar.setVisibility(
                    isLoading != null && isLoading ? View.VISIBLE : View.GONE);
        });

        // Observar los mensajes de estado
        viewModel.getStatusMessage().observe(this, message -> {
            // Actualizar y mostrar/ocultar el TextView de estado
            binding.statusTextView.setText(message);
            binding.statusTextView.setVisibility(
                    (message != null && !message.trim().isEmpty()) ? View.VISIBLE : View.GONE);
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // currentFocus puede ser null, manejarlo
        View view = this.getCurrentFocus();
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
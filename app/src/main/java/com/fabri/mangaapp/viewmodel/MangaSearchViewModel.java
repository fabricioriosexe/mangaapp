package com.fabri.mangaapp.viewmodel; // Ajusta el paquete según donde lo crees

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.fabri.mangaapp.model.Manga; // Asegúrate de importar tu clase Manga
import com.fabri.mangaapp.repository.MangaRepository;
import com.fabri.mangaapp.repository.MangaSearchCallback;
import java.util.Collections;
import java.util.List;

public class MangaSearchViewModel extends ViewModel {

    private final MangaRepository repository;

    // LiveData para observar la lista de resultados
    private final MutableLiveData<List<Manga>> searchResults = new MutableLiveData<>();
    public LiveData<List<Manga>> getSearchResults() {
        return searchResults;
    }

    // LiveData para observar el estado de carga
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // LiveData para observar mensajes de estado (errores, sin resultados, inicial)
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public MangaSearchViewModel() {
        repository = new MangaRepository(); // Crea una instancia de tu repositorio
        // Estado inicial
        statusMessage.setValue("Ingresa un nombre de manga para buscar");
    }

    // Función para iniciar la búsqueda
    public void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.setValue(Collections.emptyList()); // Limpiar resultados anteriores
            statusMessage.setValue("Ingresa un nombre de manga para buscar");
            // Asegúrate de que la carga no esté activa si la búsqueda es inválida
            isLoading.setValue(false);
            return;
        }

        isLoading.setValue(true); // Indicar que la carga ha comenzado
        statusMessage.setValue(null); // Limpiar mensaje de estado anterior
        searchResults.setValue(Collections.emptyList()); // Limpiar resultados anteriores

        // Llamar a la función de búsqueda del repositorio y pasar el callback
        repository.searchManga(query.trim(), new MangaSearchCallback() {
            @Override
            public void onSuccess(List<Manga> mangaList) {
                // Este método se ejecuta en el hilo principal (callback de Retrofit)
                isLoading.setValue(false); // Carga terminada
                if (mangaList == null || mangaList.isEmpty()) {
                    statusMessage.setValue("No se encontraron resultados para '" + query + "'");
                    // Asegúrate de que la lista de resultados esté vacía si no hay resultados
                    searchResults.setValue(Collections.emptyList());
                } else {
                    searchResults.setValue(mangaList); // Publicar los resultados exitosos
                    // Limpiar el mensaje de estado si hay resultados
                    statusMessage.setValue(null);
                }
            }

            @Override
            public void onError(Throwable t) {
                // Este método se ejecuta en el hilo principal (callback de Retrofit)
                isLoading.setValue(false); // Carga terminada
                statusMessage.setValue("Error al buscar: " + t.getMessage()); // Publicar mensaje de error
                // Asegúrate de que la lista de resultados esté vacía en caso de error
                searchResults.setValue(Collections.emptyList());
                // Opcional: Loguear el error completo: Log.e("MangaSearchViewModel", "Search error", t);
            }
        });
    }

    // Opcional: Método para limpiar el estado si es necesario
    @Override
    protected void onCleared() {
        super.onCleared();
        // Aquí podrías cancelar peticiones en curso si no usas el mecanismo
        // automático de Retrofit/Glide ligado al ciclo de vida o si tienes
        // otras operaciones de larga duración.
    }
}
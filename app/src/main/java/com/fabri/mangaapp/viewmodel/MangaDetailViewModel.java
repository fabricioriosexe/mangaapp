package com.fabri.mangaapp.viewmodel; // Tu paquete de ViewModels

// ... (importaciones existentes) ...
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
// import androidx.lifecycle.viewModelScope; // <-- Asegúrate de que esta línea esté BORRADA
import android.util.Log; // Importar Log

import com.fabri.mangaapp.model.Manga;
import com.fabri.mangaapp.model.Chapter;
import com.fabri.mangaapp.repository.MangaRepository;
import com.fabri.mangaapp.repository.MangaDetailsCallback; // Tu interfaz de callback para detalles
import com.fabri.mangaapp.repository.ChapterListCallback; // Tu interfaz de callback para capítulos

import java.util.List;
import java.util.Collections; // Importar Collections

public class MangaDetailViewModel extends ViewModel {

    private final MangaRepository repository;
    private static final String TAG = "MangaDetailViewModel"; // Etiqueta para Logcat

    private final MutableLiveData<Manga> mangaDetails = new MutableLiveData<>();
    public LiveData<Manga> getMangaDetails() {
        return mangaDetails;
    }

    private final MutableLiveData<List<Chapter>> chapterList = new MutableLiveData<>();
    public LiveData<List<Chapter>> getChapterList() {
        return chapterList;
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public MangaDetailViewModel() {
        repository = new MangaRepository();
        // El estado inicial de carga se manejará en loadMangaDetails
        isLoading.setValue(false); // Estado inicial no cargando
        statusMessage.setValue(null); // Sin mensaje inicial
        mangaDetails.setValue(null); // Sin detalles iniciales
        chapterList.setValue(Collections.emptyList()); // Lista de capítulos vacía inicial
    }

    // Método para cargar los detalles y capítulos de un manga específico
    public void loadMangaDetails(String mangaId) {
        if (mangaId == null || mangaId.isEmpty()) {
            statusMessage.setValue("Error: ID de manga inválido.");
            isLoading.setValue(false);
            return;
        }

        // Restablecer estados y comenzar la carga
        isLoading.setValue(true);
        statusMessage.setValue(null); // Limpiar mensajes anteriores
        mangaDetails.setValue(null); // Limpiar detalles anteriores
        chapterList.setValue(Collections.emptyList()); // Limpiar lista de capítulos anterior

        Log.d(TAG, "Iniciando carga para manga ID: " + mangaId);

        // 1. Llamar al Repositorio para obtener los detalles del manga
        repository.getMangaDetails(mangaId, new MangaDetailsCallback() {
            @Override
            public void onSuccess(Manga manga) {
                Log.d(TAG, "Detalles del manga obtenidos exitosamente.");
                mangaDetails.setValue(manga); // Publicar los detalles obtenidos

                // Una vez que tenemos los detalles, o podrías cargarlos en paralelo,
                // llamamos al repositorio para obtener la lista de capítulos.
                // Decidimos cargar capítulos aquí DESPUÉS de obtener detalles,
                // o podrías llamarlos justo después de getMangaDetails si quieres carga paralela.
                // Si quieres carga paralela, llama a getChapterList(mangaId, ...) aquí también.

                // Vamos a llamarlo aquí secuencialmente para simplificar el manejo de carga/errores inicial
                loadChapterList(mangaId); // <-- Llama a la función para cargar capítulos

            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error al obtener detalles del manga", t);
                statusMessage.setValue("Error al cargar detalles del manga: " + t.getMessage());
                isLoading.setValue(false); // La carga principal falla si fallan los detalles
                // No publicamos detalles ni lista de capítulos en caso de error principal
            }
        });

        // Si quieres carga paralela de detalles y capítulos, llama a loadChapterList(mangaId) aquí también,
        // fuera del callback de getMangaDetails. Necesitarías un mecanismo para saber cuándo AMBAS peticiones terminaron para poner isLoading a false.
        // Para simplificar, lo mantenemos secuencial llamando loadChapterList en onSuccess de getMangaDetails.
    }


    // Método interno para cargar solo la lista de capítulos
    public  void loadChapterList(String mangaId) {
        // Podrías establecer un estado de carga SECUNDARIO si la carga de capítulos es separada
        // Por ahora, usamos el isLoading principal.

        Log.d(TAG, "Iniciando carga de lista de capítulos para manga ID: " + mangaId);

        repository.getChapterList(mangaId, new ChapterListCallback() {
            @Override
            public void onSuccess(List<Chapter> chapterList) {
                Log.d(TAG, "Lista de capítulos obtenida exitosamente. Capítulos: " + chapterList.size());
                MangaDetailViewModel.this.chapterList.setValue(chapterList); // Publicar la lista de capítulos
                isLoading.setValue(false); // La carga principal termina cuando terminan los capítulos (en esta versión secuencial)
                // Puedes añadir un mensaje si la lista está vacía si quieres
                if (chapterList.isEmpty()) {
                    statusMessage.setValue("No se encontraron capítulos en español para este manga.");
                } else {
                    // Limpiar mensaje de estado si hay capítulos
                    statusMessage.setValue(null);
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error al obtener lista de capítulos", t);
                // Si falla solo la lista de capítulos, puedes mostrar los detalles del manga y un mensaje de error para los capítulos.
                statusMessage.setValue("Error al cargar capítulos: " + t.getMessage());
                isLoading.setValue(false); // La carga principal termina
                MangaDetailViewModel.this.chapterList.setValue(Collections.emptyList()); // Asegurarse de que la lista esté vacía
            }
        });
    }


    // Opcional: onCleared para cancelar peticiones
    @Override
    protected void onCleared() {
        super.onCleared();
        // Si tu repositorio o peticiones Retrofit pueden ser canceladas,
        // podrías añadir lógica aquí. Retrofit Call.cancel() es una opción.
    }
}
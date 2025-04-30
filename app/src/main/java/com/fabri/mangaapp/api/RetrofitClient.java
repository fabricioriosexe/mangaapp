package com.fabri.mangaapp.api;

// ... (importaciones existentes) ...
import okhttp3.OkHttpClient; // Asegúrate de importar OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor; // <-- ¡Importar el Logging Interceptor!
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit; // Importar TimeUnit

public class RetrofitClient {

    private static final String BASE_URL = "https://api.mangadex.org/";
    private static Retrofit retrofit;
    private static MangaDexApiService apiService;

    public static MangaDexApiService getApiService() {
        if (apiService == null) {
            // --- Configurar OkHttpClient con Interceptor de Logging ---
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // Configurar el nivel de detalle del logging (BODY incluye headers y cuerpo de request/response)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    // Opcional: Configurar timeouts si es necesario
                    .connectTimeout(60, TimeUnit.SECONDS) // Ejemplo
                    .readTimeout(60, TimeUnit.SECONDS)    // Ejemplo
                    // Añadir el interceptor al cliente OkHttp
                    .addInterceptor(logging) // <-- ¡Añadir esta línea!
                    .build();
            // -----------------------------------------------------


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // Usar el cliente OkHttp configurado
                    .client(client) // <-- Asegúrate de usar el cliente con el interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(MangaDexApiService.class);
        }
        return apiService;
    }
}

/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/
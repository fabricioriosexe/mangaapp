package com.fabri.mangaapp; // Asegúrate de que este sea el paquete correcto
/**
 * Codigo Hecho
 * Por
 * Fabricio Rios
 * fabriciorios0103@gmail.com
 * Git Hub
 * https://github.com/fabricioriosexe*/
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceError; // Importar para onReceivedError
import android.webkit.WebResourceRequest; // Importar para onReceivedError (API >= 21)
import android.widget.Toast;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";
    public static final String EXTRA_CHAPTER_URL = "extra_chapter_url";

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled") // Se usa para habilitar JavaScript
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader); // Asegúrate de tener activity_reader.xml con webViewReader

        webView = findViewById(R.id.webViewReader);

        // --- Configurar WebSettings adicionales ---
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Ya lo tenías
        webSettings.setDomStorageEnabled(true); // Habilitar almacenamiento DOM (a veces necesario para JS)
        webSettings.setDatabaseEnabled(true); // Habilitar base de datos web (a veces necesario)
        webSettings.setLoadWithOverviewMode(true); // Cargar la página completamente con un ancho reducido
        webSettings.setUseWideViewPort(true); // Permite que la página se ajuste al tamaño de la pantalla
        webSettings.setBuiltInZoomControls(true); // Habilitar zoom (opcional)
        webSettings.setDisplayZoomControls(false); // Ocultar controles de zoom en pantalla (opcional)
        // Puedes probar otras configuraciones si es necesario, pero empieza con estas.
        // Por ejemplo: webSettings.setAllowFileAccess(true); webSettings.setAllowContentAccess(true);

        // --- Configurar WebViewClient con manejo de errores ---
        webView.setWebViewClient(new WebViewClient() {
            // Método llamado cuando la WebView detecta un error al cargar un recurso
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // Solo mostrar el error para la URL principal de la página, no para recursos (imágenes, CSS, JS)
                if (request.isForMainFrame()) {
                    Log.e(TAG, "Error loading page: " + error.getDescription() + " at " + request.getUrl());
                    // Mostrar un mensaje al usuario si la página principal falla en cargar
                    Toast.makeText(view.getContext(), "Error al cargar la página: " + error.getDescription(), Toast.LENGTH_LONG).show();
                    // Opcional: Mostrar un mensaje de error en la propia WebView
                    // view.loadData("<html><body><h1>Error al cargar la página.</h1><p>" + error.getDescription() + "</p></body></html>", "text/html", "utf-8");
                }
            }

            // Mantener la navegación dentro de la misma WebView
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            // Opcional: Puedes sobrescribir onPageStarted y onPageFinished para mostrar/ocultar un ProgressBar
        });


        // Obtener la URL del capítulo del Intent
        String chapterUrl = getIntent().getStringExtra(EXTRA_CHAPTER_URL);

        // Cargar la URL
        if (chapterUrl != null && !chapterUrl.isEmpty()) {
            Log.d(TAG, "Loading chapter URL: " + chapterUrl);
            webView.loadUrl(chapterUrl);
        } else {
            Log.e(TAG, "No chapter URL received in Intent");
            Toast.makeText(this, "Error: No se recibió la dirección del capítulo.", Toast.LENGTH_SHORT).show();
            // Opcional: Cerrar la actividad si no hay URL
            // finish();
        }
    }

    // Permite navegar hacia atrás en el WebView
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
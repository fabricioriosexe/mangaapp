![logo_mng1](https://github.com/user-attachments/assets/74beae5a-96f7-4c8c-9e68-5dc8976d032f)# MangaApp

Una aplicación de Android para buscar y visualizar información sobre mangas, incluyendo detalles y listas de capítulos, utilizando la API de MangaDex.

## Características

* **Búsqueda de Mangas:** Encuentra mangas por título.
* **Visualización de Detalles:** Consulta información detallada de un manga, como la portada, título y descripción.
* **Lista de Capítulos:** Explora la lista de capítulos disponibles para un manga (carga inicial de una página).
* **Lector de Capítulos:** Abre capítulos de manga en una vista web integrada (WebView).

## Arquitectura

El proyecto sigue el patrón de arquitectura **MVVM (Model-View-ViewModel)** combinado con el patrón **Repository**.

* **View (Activities):** Responsables de la UI y de observar los datos expuestos por los ViewModels.
* **ViewModel:** Contiene la lógica de la UI, gestiona el estado de la UI y se comunica con el Repositorio.
* **Repository:** Actúa como una capa de abstracción sobre las fuentes de datos (en este caso, la API de MangaDex), manejando la lógica de obtención de datos.
* **Model:** Clases POJO que representan la estructura de los datos (mangas, capítulos, etc.) recibidos de la API.

## Tecnologías y Librerías Utilizadas

* **Android SDK:** Desarrollo nativo en Java.
* **Android Architecture Components:**
    * `ViewModel`: Para gestionar datos relacionados con la UI de una forma que sobrevive a los cambios de configuración.
    * `LiveData`: Para datos observables que notifican a los componentes de la UI sobre cambios.
* **View Binding:** Para interactuar con las vistas de los layouts de forma segura y concisa.
* **RecyclerView:** Para mostrar listas de datos eficientemente.
    * `ListAdapter` y `DiffUtil`: Para actualizar eficientemente los datos en el RecyclerView.
* **Retrofit:** Un cliente HTTP Type-safe para Android y Java, utilizado para hacer peticiones a la API de MangaDex.
* **Glide:** Una librería de carga de imágenes rápida y eficiente para Android.
* **MangaDex API:** La fuente de datos para la información y los capítulos de manga.

## API Utilizada

La aplicación utiliza la [API pública de MangaDex](https://api.mangadex.org/docs/) para obtener la información de los mangas y sus capítulos.

## Instalación y Configuración

1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/fabricioriosexe/mangaapp.git]
    ```
2.  **Abre el proyecto en Android Studio.**
3.  **Sincroniza el proyecto con los archivos Gradle:** Android Studio debería hacerlo automáticamente.
4.  **Asegúrate de tener View Binding habilitado:** Verifica que en el archivo `app/build.gradle` tienes:
    ```gradle
    android {
        // ...
        buildFeatures {
            viewBinding true
        }
    }
    ```
5.  **Reconstruye el proyecto:** Ve a `Build > Rebuild Project` en Android Studio.
6.  **Ejecuta la aplicación:** Conecta un dispositivo Android o inicia un emulador y ejecuta la app desde Android Studio.

*(Si la API de MangaDex requiriera una clave API en el futuro, deberías añadir instrucciones sobre cómo obtenerla e incluirla en el proyecto, por ejemplo, en un archivo `local.properties`.)*

## Uso de la Aplicación

1.  Al abrir la aplicación, verás una barra de búsqueda.
2.  Ingresa el título (o parte del título) de un manga que deseas buscar y presiona Enter o el botón de búsqueda (si existe).
3.  La aplicación mostrará una lista de resultados de búsqueda.
4.  Haz clic en un manga de la lista para ver su pantalla de detalles.
5.  En la pantalla de detalles, verás la portada, descripción y una lista de capítulos disponibles.
6.  Haz clic en un capítulo de la lista para abrirlo en el lector integrado (WebView).
7.  Usa el botón de retroceso de tu dispositivo para navegar hacia atrás.

## Capturas de Pantalla

![Screenshot 1:](https://i.postimg.cc/kMb01TTd/screen.png)
![Screenshot 2:](https://i.postimg.cc/xTGWYcrx/screen-2.png)
![Screenshot 3:](https://i.postimg.cc/MKJhh1Lf/screen-3.png)
![Screenshot 4:](https://i.postimg.cc/Dw2Rznn2/screen-4.png)

## Contribución

¡Las contribuciones son bienvenidas! Si encuentras algún error o tienes ideas para mejorar la aplicación, por favor, abre un issue o envía un Pull Request.

1.  Haz un fork del repositorio.
2.  Crea una nueva rama (`git checkout -b feature/nueva-feature`).
3.  Realiza tus cambios y commitea (`git commit -m 'feat: Añadir nueva feature'`).
4.  Haz push a la rama (`git push origin feature/nueva-feature`).
5.  Abre un Pull Request.

## Licencia

Este proyecto está licenciado bajo la [Licencia MIT](https://opensource.org/licenses/MIT). Consulta el archivo [LICENSE](LICENSE) para más detalles. ## Autor

* Rios Fabricio Exequiel
* https://github.com/fabricioriosexe
*fabriciorios0103@gmail.com

---

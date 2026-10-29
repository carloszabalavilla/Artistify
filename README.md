# Artistify · demo Android local

Aplicación Android/Kotlin de catálogo de artistas y canciones. La variante demo usa un catálogo ficticio y guarda cambios en SharedPreferences. No usa Firebase ni RapidAPI y su manifiesto retira el permiso de Internet.

## Instalar

Requiere Android13 o superior. Instala Artistify-demo.apk en un dispositivo o emulador de pruebas y pulsa Entrar en la demo. Puedes consultar, buscar, añadir y eliminar artistas/canciones del catálogo ficticio. Los cambios quedan en el dispositivo. Para restaurar los datos iniciales, borra los datos de la app desde los ajustes de Android. La demo no reproduce audio ni abre perfiles reales.

## Compilar

JDK17, Android SDK34 y build-tools34.0.0. Configura sdk.dir en local.properties o ANDROID_HOME y ejecuta:

```powershell
.\gradlew.bat --no-daemon assembleDemoDebug testDemoDebugUnitTest lintDemoDebug
```

En Linux/macOS: bash ./gradlew. APK en app/build/outputs/apk/demo/debug/. Las pruebas validan fixtures y búsquedas offline; Android Lint sigue señalando avisos heredados. Compilar y pasar lint no sustituye la prueba de instalación en dispositivo, que debe indicarse por separado.

## Variante conectada

Se conserva la variante live y su flujo original. Requiere un nuevo app/google-services.json privado y propiedad Gradle RAPIDAPI_KEY. Las claves publicadas anteriormente deben sustituirse en sus proveedores; retirarlas del código no las revoca ni limpia el historial. Esta variante no está verificada en esta entrega y no se incluye una clave real en la demo.

Carlos Zabala. Se conserva el historial y los recursos originales.

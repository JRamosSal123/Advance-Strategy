# 🎮 Advance-Strategy
Advance-Strategy es un proyecto desarrollado por Javier Ramos Salinas como parte de su proyecto final de grado en la Universitat Oberta de Catalunya (UOC).
Se trata de un juego de estrategia por turnos con una estética pixel art en 2D, programado íntegramente en Java, utilizando tanto sus librerías nativas como la librería RayLib.

## 🔧 Requisitos para ejecutar el `.exe`
Para poder ejecutar el archivo ejecutable, es necesario contar con los siguientes recursos en la misma carpeta:
* 📁 La carpeta `JRE` (incluida en el repositorio como `JRE.zip`; debe descomprimirse antes de ejecutar).
* 📁 La carpeta `assets`.

## 🌐 Ejecución de la conexión P2P

Advance-Strategy permite partidas multijugador entre dos usuarios mediante una conexión **peer-to-peer (P2P)**. Para establecer la conexión correctamente, sigue estos pasos:

1. **Inicia el juego** y selecciona la opción **"2 jugadores"** desde el menú principal.

2. A continuación, elige uno de los siguientes roles:

   ### 🖥️ Si actúas como **Host**:
   - Selecciona un **puerto disponible** en tu equipo (se recomienda el puerto `8888`, o uno entre `1024` y `65535`).
   - Pulsa el botón **"Conectarse"**.
   - Espera a que el jugador cliente se conecte.

   ### 🌐 Si actúas como **Cliente**:
   - Pide al Host que te proporcione:
     - Su **dirección IP** (puede obtenerla ejecutando `ipconfig` en el símbolo del sistema de Windows).
     - El **puerto** que ha seleccionado.
   - Introduce esos datos en el juego.
   - Pulsa el botón **"Conectarse"** para establecer la conexión.

> ✅ Una vez ambos jugadores estén conectados, la partida comenzará de forma automática.

## 🛠️ Construido con

Para desarrollar el proyecto he usado las siguientes herramientas

* [Raylib](https://github.com/electronstudio/jaylib) - Libreria de Java para el desarrolo de videojuegos.
* [Maven](https://maven.apache.org/) - Manejador de dependencias.
* [Launch4j](https://launch4j.sourceforge.net/) - Herramienta para convertir archivos `.jar` a `.exe`.
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - Entorno de desarrollo integrado (IDE).

## 👤 Autor
* **Javier Ramos Salinas** - [JRamosSal](https://github.com/JRamosSal123)

## 📄 Licencia
Este proyecto está licenciado bajo:
[Reconocimiento-NoComercial-SinObrasDerivadas 3.0 España](https://creativecommons.org/licenses/by-nc-nd/3.0/es/).

## 📌 Versionado
Usamos [SemVer](http://semver.org/) para el versionado.
Para todas las versiones disponibles, mira los [tags en este repositorio](https://github.com/JRamosSal123/Advance-Strategy/tags).

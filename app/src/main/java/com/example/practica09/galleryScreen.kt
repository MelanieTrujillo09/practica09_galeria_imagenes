package com.example.practica09

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage

@Composable
fun GaleriaScreen() {
    // Lista de URLs de imágenes de prueba
    val imagenes = listOf(
        "https://picsum.photos/id/1018/400/400",
        "https://picsum.photos/id/1015/400/400",
        "https://picsum.photos/id/1019/400/400",
        "https://picsum.photos/id/1020/400/400",
        "https://picsum.photos/id/1021/400/400",
        "https://picsum.photos/id/1022/400/400",
        "https://picsum.photos/id/1023/400/400",
        "https://picsum.photos/id/1024/400/400",
        // URL inválida a propósito, para evidenciar el manejo de errores
        "https://url-invalida.example/imagen.jpg"
    )

    // Estado: imagen seleccionada (null = ninguna, el diálogo está cerrado)
    var imagenSeleccionada by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Mi Galería",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )

        // LazyVerticalGrid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Indicamos que queremos 2 columnas
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(imagenes) { url ->
                ImagenItem(url = url, onClick = { imagenSeleccionada = url })
            }
        }
    }

    // RETO: vista ampliada en un diálogo
    imagenSeleccionada?.let { url ->
        Dialog(onDismissRequest = { imagenSeleccionada = null }) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SubcomposeAsyncImage(
                        // Versión más grande de la misma imagen
                        model = url.replace("/400/400", "/800/800"),
                        contentDescription = "Imagen ampliada",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        loading = { ImagenCargando() },
                        error = { ImagenError() }
                    )
                    TextButton(
                        onClick = { imagenSeleccionada = null },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}

@Composable
fun ImagenItem(url: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Coil maneja la carga de red, la caché y el ciclo de vida
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "Imagen de la galería",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = { ImagenCargando() },  // placeholder mientras descarga
            error = { ImagenError() }        // si falla la descarga
        )
    }
}

@Composable
fun ImagenCargando() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp))
    }
}

@Composable
fun ImagenError() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No se pudo cargar", fontSize = 12.sp)
    }
}
# Guía Simple: Subida de Imágenes en Diferentes Contextos

Esta guía explica cómo implementar subida de imágenes en diferentes partes de tu aplicación usando el patrón existente del `ProfileView`.

## 🏗️ **Componentes Base (Ya Existentes)**

### Archivos que ya tienes funcionando:

1. **CloudinaryService**: Servicio principal con compresión automática ✅
2. **CloudinaryConfig**: Configuraciones por tipo de imagen ✅  
3. **ImagePicker**: Componente que maneja galería/cámara ✅
4. **AvatarImageView**: Ejemplo completo funcionando ✅
5. **UploadState**: Estados de subida ✅

## 📝 **Tipos de Imagen Disponibles**

El sistema ya está configurado para 3 tipos principales:

### 1. ImageType.AVATAR 
- **Límite**: 1MB, 512x512px
- **Uso**: Fotos de perfil
- **Ya implementado**: ✅ ProfileView

### 2. ImageType.CHAT
- **Límite**: 5MB, 1024x768px  
- **Uso**: Imágenes en mensajes
- **Para implementar**: ChatView, MessageView

### 3. ImageType.ANNOUNCEMENT
- **Límite**: 10MB, 1200x900px
- **Uso**: Imágenes en anuncios
- **Para implementar**: AnnouncementView, NewsView

## � **Ejemplos Reales de Uso**

### **Escenario 1: Usuario en Chat**
```
👤 Juan quiere enviar fotos de su viaje:
📱 Mensaje 1: Sube 5 fotos del avión ✈️✈️✈️✈️✈️
📱 Mensaje 2: Sube 5 fotos del hotel 🏨🏨🏨🏨🏨  
📱 Mensaje 3: Sube 3 fotos de la playa 🏖️🏖️🏖️
📱 Total: 13 fotos en 3 mensajes (sin límite total)
```

### **Escenario 2: Usuario en Galería**
```
👤 María organiza sus fotos:
📸 Subida 1: 20 fotos de cumpleaños 🎂 (límite por subida)
📸 Subida 2: 15 fotos de trabajo 💼 (otra subida separada)
📸 Subida 3: 20 fotos de vacaciones 🏝️ (otra subida más)
📸 Total: 55 fotos en su galería (sin límite total)
```

### **Escenario 3: Admin creando anuncios**
```
👤 Admin de la empresa:
📢 Anuncio 1: 3 fotos del evento ✨✨✨
📢 Anuncio 2: 2 fotos de nuevas oficinas 🏢🏢
📢 Anuncio 3: 1 foto del nuevo producto 📱
📢 Puede crear infinitos anuncios
```

### **¿Por qué estos límites?**

1. **Performance**: Subir 50 fotos a la vez sobrecargaría la app
2. **UX**: Más de 20 fotos en una selección sería confuso
3. **Red**: Evita timeouts y problemas de conectividad
4. **Memoria**: Previene crashes por uso excesivo de RAM
5. **Cloudinary**: Optimiza las requests al servicio

### **¿Se pueden cambiar estos límites?**
¡Sí! Son configurables en el código:

```kotlin
// En CloudinaryConfig.kt - getFileLimit()
ContextType.CHAT_MESSAGE -> 5    // Cambiar a 8 si quieres
ContextType.USER_GALLERY -> 20   // Cambiar a 30 si necesitas
```

##  **Cómo Implementar en Nuevos Contextos**

### **Patrón Base: Copia lo que funciona**

El `ProfileView` ya tiene todo funcionando. Usa este patrón:

## 📋 **Paso a Paso: Chat con Imágenes**

### **Paso 1: En tu ViewModel (ChatViewModel.kt)**

```kotlin
class ChatViewModel(context: Context) : ViewModel() {
    
    private val cloudinaryService = CloudinaryService(context)
    
    // Estados para la imagen (igual que ProfileView)
    var uploadedImageUrl by mutableStateOf<String?>(null)
    var isImageUploading by mutableStateOf(false)
    var imageUploadError by mutableStateOf<String?>(null)
    
    // Función para subir imagen de chat
    fun uploadChatImage(uri: Uri) {
        viewModelScope.launch {
            cloudinaryService.uploadImage(uri, ImageType.CHAT).collect { state ->
                when (state) {
                    is UploadState.Loading -> {
                        isImageUploading = true
                        imageUploadError = null
                    }
                    is UploadState.Success -> {
                        isImageUploading = false
                        uploadedImageUrl = state.imageUrl
                        // Aquí puedes enviar el mensaje con la imagen
                        sendMessageWithImage(state.imageUrl)
                    }
                    is UploadState.Error -> {
                        isImageUploading = false
                        imageUploadError = state.message
                    }
                    else -> {}
                }
            }
        }
    }
    
    private fun sendMessageWithImage(imageUrl: String) {
        // Tu lógica para enviar mensaje con imagen
    }
}
```

### **Paso 2: En tu Screen (ChatScreen.kt)**

```kotlin
@Composable
fun ChatScreen(
    chatId: String,
    viewModel: ChatViewModel
) {
    Column {
        // Lista de mensajes
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // Tus mensajes aquí
        }
        
        // Barra inferior con campo de texto e imagen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Campo de texto para mensaje
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribir mensaje...") }
            )
            
            // Botón para adjuntar imagen (IGUAL QUE PROFILEVIEW)
            ImagePicker(
                imageType = ImageType.CHAT, // 👈 Cambiar solo este tipo
                currentImageUrl = viewModel.uploadedImageUrl,
                onImageUploaded = { imageUrl ->
                    // Se subió la imagen, ahora envía mensaje
                    viewModel.sendMessageWithImage(imageUrl)
                },
                modifier = Modifier.width(100.dp)
            )
            
            // Botón enviar mensaje normal
            IconButton(
                onClick = { viewModel.sendTextMessage(messageText) }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar")
            }
        }
        
        // Mostrar error de imagen si existe
        viewModel.imageUploadError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
```

## 📋 **Ejemplo: Anuncios con Imágenes**

### **En AnnouncementViewModel.kt:**

```kotlin
class AnnouncementViewModel(context: Context) : ViewModel() {
    
    private val cloudinaryService = CloudinaryService(context)
    
    // Estados (igual que ProfileView)
    var announcementImageUrl by mutableStateOf<String?>(null)
    var isImageUploading by mutableStateOf(false)
    var imageUploadError by mutableStateOf<String?>(null)
    
    fun uploadAnnouncementImage(uri: Uri) {
        viewModelScope.launch {
            cloudinaryService.uploadImage(uri, ImageType.ANNOUNCEMENT).collect { state ->
                when (state) {
                    is UploadState.Success -> {
                        announcementImageUrl = state.imageUrl
                        // Actualizar tu modelo de anuncio
                        updateAnnouncementWithImage(state.imageUrl)
                    }
                    is UploadState.Error -> {
                        imageUploadError = state.message
                    }
                    // ... otros estados
                }
            }
        }
    }
}
```

### **En AnnouncementScreen.kt:**

```kotlin
@Composable
fun CreateAnnouncementScreen(
    viewModel: AnnouncementViewModel
) {
    Column {
        // Título del anuncio
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título del anuncio") }
        )
        
        // Contenido
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Contenido") }
        )
        
        // Selector de imagen (IGUAL QUE PROFILEVIEW)
        ImagePicker(
            imageType = ImageType.ANNOUNCEMENT, // 👈 Solo cambia esto
            currentImageUrl = viewModel.announcementImageUrl,
            onImageUploaded = { imageUrl ->
                viewModel.announcementImageUrl = imageUrl
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Botón publicar
        Button(
            onClick = { 
                viewModel.publishAnnouncement(title, content, viewModel.announcementImageUrl)
            }
        ) {
            Text("Publicar Anuncio")
        }
    }
}
```

## 🔧 **Uso Directo (Opción Avanzada)**

Si no quieres usar `ImagePicker`, puedes usar `CloudinaryService` directamente:

```kotlin
// En tu ViewModel
private val cloudinaryService = CloudinaryService(context)

fun uploadImageDirectly(uri: Uri) {
    viewModelScope.launch {
        cloudinaryService.uploadImage(uri, ImageType.CHAT).collect { state ->
            when (state) {
                is UploadState.Success -> {
                    // Imagen subida: state.imageUrl
                }
                is UploadState.Error -> {
                    // Error: state.message
                }
                is UploadState.Progress -> {
                    // Progreso: state.percentage
                }
                else -> {}
            }
        }
    }
}
```

## 🎯 **Resumen: 3 Pasos Simples**

### **Para cualquier nuevo contexto:**

1. **Cambia el tipo**: `ImageType.AVATAR` → `ImageType.CHAT` → `ImageType.ANNOUNCEMENT`

2. **Copia el patrón**: Usa el mismo código que `ProfileView`

3. **Personaliza la acción**: Cambia qué pasa cuando se sube la imagen

## ⚙️ **Configuración Avanzada**

### **Cambiar límites de tamaño:**

En `CloudinaryConfig.kt`:

```kotlin
const val AVATAR_MAX_SIZE = 1024L * 1024L        // 1MB → Cambiar aquí
const val CHAT_MAX_SIZE = 5L * 1024L * 1024L     // 5MB → Cambiar aquí  
const val ANNOUNCEMENT_MAX_SIZE = 10L * 1024L * 1024L  // 10MB → Cambiar aquí
```

### **Cambiar resoluciones objetivo:**

En `CloudinaryService.kt` → método `compressImageIfNeeded()`:

```kotlin
val (targetWidth, targetHeight) = when (imageType) {
    ImageType.AVATAR -> Pair(512, 512)      // Cambiar aquí
    ImageType.CHAT -> Pair(1024, 768)       // Cambiar aquí
    ImageType.ANNOUNCEMENT -> Pair(1200, 900) // Cambiar aquí
}
```

## ✅ **Beneficios Implementados**

- ✅ **Compresión automática** con recorte inteligente
- ✅ **Corrección de orientación** (fotos verticales/horizontales)
- ✅ **Límites configurables** por tipo de imagen
- ✅ **Estados de progreso** detallados
- ✅ **UI reutilizable** con `ImagePicker`
- ✅ **Manejo de errores** automático

**¡Solo copia el patrón del ProfileView y cambia el `ImageType`!** 🚀
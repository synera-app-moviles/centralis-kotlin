package com.example.centralis_kotlin.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.centralis_kotlin.common.config.ImageType

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AvatarImageView(
    imageUrl: String? = null,
    onImageChange: (String) -> Unit,
    onImageRemoved: () -> Unit = {},
    size: Dp = 120.dp,
    showPicker: Boolean = true,
    modifier: Modifier = Modifier
) {
    var showImagePicker by remember { mutableStateOf(false) }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Avatar circular
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFF4A3B5C))
                .border(2.dp, Color(0xFF823DF9), CircleShape)
                .clickable(enabled = showPicker) { showImagePicker = true },
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar placeholder",
                    tint = Color(0xFFA58ECC),
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }
        
        // Texto informativo
        if (showPicker) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (imageUrl == null) "Toca para agregar foto" else "Toca para cambiar",
                color = Color(0xFFA58ECC),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        
        // Picker de imagen
        if (showImagePicker && showPicker) {
            Spacer(modifier = Modifier.height(16.dp))
            ImagePicker(
                imageType = ImageType.AVATAR,
                currentImageUrl = imageUrl,
                onImageUploaded = { newUrl ->
                    onImageChange(newUrl)
                    showImagePicker = false
                },
                onImageRemoved = {
                    onImageRemoved()
                    showImagePicker = false
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AvatarImageViewCompact(
    imageUrl: String? = null,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    AvatarImageView(
        imageUrl = imageUrl,
        onImageChange = { },
        size = size,
        showPicker = false,
        modifier = modifier
    )
}
package com.choice.map.ui.composable

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.choice.map.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView


@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onLoad: ((map: MapView) -> Unit)? = null
) {
    val mapViewState = rememberMapView()
    val context = LocalContext.current

    AndroidView(
        { mapViewState },
        modifier
    ) { mapView -> onLoad?.invoke(mapView)
    }
}

@Composable
fun getLottieIconForUser(): Drawable {
    // Substitua isso pela lógica real para obter o ícone do usuário
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.current_location))
    val progress by animateLottieCompositionAsState(composition)

    val bitmap = Bitmap.createBitmap(composition!!.bounds.width(), composition!!.bounds.height(), Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawRect(composition!!.bounds, android.graphics.Paint())

    return BitmapDrawable(LocalContext.current.resources, bitmap)
}


@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            maxZoomLevel = 20.0
            minZoomLevel = 5.0
        }
    }

    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
    }


/*
Topic of Investigation: Simple composable for showing images fetched from the network. Alternatice library: Glide: Supports placeholders for different network states
*/

const val IMAGE_URL = "https://images.unsplash.com/photo-1628373383885-4be0bc0172fa?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1301&q=80"
@Composable
fun ImageFetchedFromNetwork(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / 1f),
            painter = rememberAsyncImagePainter(IMAGE_URL),
            contentDescription = "sky_image",
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
@Preview
fun ImageFetchedFromNetworkPreview() {
    ImageFetchedFromNetwork()
}
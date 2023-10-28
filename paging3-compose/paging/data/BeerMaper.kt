package com.salmakhd.android.forpractice.paging.data

import com.salmakhd.android.forpractice.paging.network.BeerDto
import com.salmakhd.android.forpractice.paging.domain.Beer

class BeerMaper {
}

fun BeerDto.toBeerEntity(): BeerEntity {
    return BeerEntity(
        id = id,
        name= name,
        tagline = tagline,
        description = description,
        firstBrewed = first_brewed,
        imageUrl = image_url
    )
}

fun BeerEntity.toBeer(): Beer {
    return Beer(
        id = id,
        name= name,
        tagline = tagline,
        description = description,
        firstBrewed = firstBrewed,
        imageUrl = imageUrl
    )
}
package com.example.bookreader.utils

class ConnectionStrings {
    companion object {
        const val ApiUrl = "https://librosaguilar.azurewebsites.net/api/values/"
        const val BlobsPng = "https://imageneslibros.blob.core.windows.net/imgsandbooks/"
        const val BlobsPdf = "https://imageneslibros.blob.core.windows.net/imgsandbooks/"
        const val connectionAzureBlobsContainer = "DefaultEndpointsProtocol=https;AccountName=imageneslibros;AccountKey=G87v670Qvo8yn6KygUze6dpsHYbWcPykxZEhSIkfsCJVUZ+in5LjZIPHCNKCgNpSOSbVuDv7EqCzqivwwC1trg==;EndpointSuffix=core.windows.net"
    }
}
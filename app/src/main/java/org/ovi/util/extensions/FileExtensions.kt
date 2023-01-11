package org.ovi.util.extensions

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.text.DecimalFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow


fun Context.loadFromAsset(res_id: Int): String {
    var ret = ""
    try {
        val inputStream = resources?.openRawResource(res_id) as InputStream
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var receiveString: String? = ""
        val stringBuilder = StringBuilder()
        while (bufferedReader.readLine().also { receiveString = it } != null) {
            stringBuilder.append(receiveString)
        }
        inputStream.close()
        ret = stringBuilder.toString()
    } catch (e: FileNotFoundException) {
//        Log.e("File not found: %s", e.toString())
    } catch (e: IOException) {
//        Log.e("Can not read file: %s", e.toString())
    }
    return ret
}


fun Bitmap.toUri2(context: Context): Uri? {
    var bitmapUri: Uri? = null

    try {
        val file = File(context.cacheDir, System.currentTimeMillis().toString())

        val out = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.close()
        bitmapUri = Uri.fromFile(file)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmapUri
}


fun Context.getFilePath(uri: Uri?): String? {
    val projection = arrayOf(
        MediaStore.MediaColumns.DISPLAY_NAME
    )
    contentResolver.query(
        uri!!, projection, null, null,
        null
    ).use { cursor ->
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            return cursor.getString(index)
        }
    }
    return null
}

fun Long.convertToDecimal(fraction: Int = 2): String {

    val unit = 1000

    if (this <= 0)
        return "0.00"

    if (this < unit) {
        val byteToKb = DecimalFormat("#0.000").format(this.toDouble() / unit).toDouble()
        return String.format("%.2f", byteToKb)
    }
    val exp = (ln(this.toDouble()) / ln(unit.toDouble())).toLong()
    val value = this / unit.toDouble().pow(exp.toDouble())

    return String.format("%." + fraction + "f", value)
}

fun Long.convertToUnits(fraction: Int = 2): String {

    val unit = 1000

    if (this <= 0)
        return "0 Byte"

    if (this < unit) {
        /* Log.d("FileExt", "convertToUnits: this $this")
         val byteToKb = DecimalFormat("#0.000").format(this.toDouble() / converted).toDouble()
         Log.d("FileExt", "convertToUnits: byteToKb $byteToKb")*/
        val converted = (this.toDouble() / unit.toDouble())
        return String.format("%.2f KB", converted)
    }

    val exp = (ln(this.toDouble()) / ln(unit.toDouble())).toLong()
    val value = this / unit.toDouble().pow(exp.toDouble())

    return String.format(
        "%." + fraction + "f %s%s", value,
        "KMGTPEZY"[exp.toInt() - 1], "B"
    )
}


fun Context.getFileSize(uri: Uri): Long {
    val projection = arrayOf(
        MediaStore.MediaColumns.SIZE
    )
    contentResolver.query(
        uri, projection, null, null,
        null
    ).use { cursor ->
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            return if (!cursor.isNull(index)) {
                (cursor.getString(index).toLong() / 1024 / 1024)
            } else {
                0L
            }
        }
    }
    return 0L
}


@SuppressLint("Range")
fun Context.getFileName(uri: Uri): String {
    var result: String? = null
    if (uri.scheme.equals("content")) {
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

fun Context.getExtensionFromUri(uri: Uri): String? {

    //Check uri format to avoid null
    val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //If scheme is a content
        val mime = MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(contentResolver.getType(uri))
    } else {
        //If scheme is a File
        //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path!!)).toString())
    }
    return extension
}



fun Context.getMimeType(uri: Uri): String? {
    val extension: String?

    //Check uri format to avoid null
    extension = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //If scheme is a content
        val mime = MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(contentResolver.getType(uri))
    } else {
        //If scheme is a File
        //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
    }
    return extension
}

fun Uri.getMimeTypeForIntent() = getMimeForType(this.toString())

fun File.getMimeTypeForIntent() = getMimeForType(this.toString())

private fun getMimeForType(path: String): String {

    path.run {

        val fileType = URLConnection.guessContentTypeFromName(this)

//        Log.d("FileXXX", "getMimeForType: fileType $fileType str $this")

        if (!fileType.isNullOrEmpty()) {
            return fileType
        }

        return when {
            contains(".doc", ignoreCase = true) || contains(
                ".docx",
                ignoreCase = true
            ) -> "application/msword"
            contains(".pdf", ignoreCase = true) -> "application/pdf"
            contains(".ppt", ignoreCase = true) || contains(
                ".pptx",
                ignoreCase = true
            ) -> "application/vnd.ms-powerpoint"
            contains(".xls", ignoreCase = true) || contains(
                ".xlsx",
                ignoreCase = true
            ) -> "application/vnd.ms-excel"
            contains(".zip", ignoreCase = true) || contains(
                ".rar",
                ignoreCase = true
            ) -> "application/zip"
            contains(".rtf", ignoreCase = true) -> "application/rtf"
            contains(".wav", ignoreCase = true) || contains(
                ".mp3",
                ignoreCase = true
            ) -> "audio/x-wav"
            contains(".gif", ignoreCase = true) -> "image/gif"
            contains(".jpg", ignoreCase = true) || contains(".jpeg", ignoreCase = true) || contains(
                ".png",
                ignoreCase = true
            ) -> "image/jpeg"
            contains(".txt", ignoreCase = true) -> "text/plain"
            contains(".3gp", ignoreCase = true) || contains(
                ".mpg",
                ignoreCase = true
            ) || contains(".mpeg", ignoreCase = true) || contains(".mpe", ignoreCase = true) ||
                    contains(".mp4", ignoreCase = true) || contains(
                ".avi",
                ignoreCase = true
            ) || contains(".mov", ignoreCase = true) || contains(
                ".mkv",
                ignoreCase = true
            ) -> "video/*"
            else -> "*.*"
        }
    }
}

fun Uri?.isImagePickedOnline() = this.toString().trim().isNotEmpty() &&
        this.toString().trim().startsWith("content://com.google.android.apps.photos.content") ||
        this.toString().startsWith("content://com.google.android.apps.docs.storage") ||
        this.toString().startsWith("content://com.dropbox.product.android")


fun Context.getGPhotoImageUri(imageUri: Uri?): Uri? {
    imageUri?.let {
        try {
            val iS = contentResolver.openInputStream(it)
            if (iS != null) {
                val path = with(BitmapFactory.decodeStream(iS)) {
                    compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
                    MediaStore.Images.Media.insertImage(
                        contentResolver, this,
                        "ClientBoxx" + System.currentTimeMillis().toString() + "",
                        null
                    )
                }
                return Uri.parse(path)
            }
        } catch (ex: FileNotFoundException) {
            ex.printStackTrace()
        }
    }
    return null
}

fun Uri?.isPickedFromOnline(): Boolean {
    if (this.toString().isNotEmpty()) {
        return this.toString().startsWith("content://com.google.android.apps.docs.storage") ||
                this.toString().startsWith("content://com.dropbox.product.android")
    }
    return false
}

fun InputStream.toFile(file: File, fileName: String) {
    File(file, fileName).outputStream().use { this.copyTo(it) }
}

suspend fun Context.clearCache() = withContext(Dispatchers.IO) {
    deleteDir(cacheDir)
    //Log.d("FileExt", "clearCache: is Cleared ${deleteDir(cacheDir)}")
}

fun deleteDir(dir: File?): Boolean {
    runCatching {
        //Log.d("FileExt", "deleteDir: dir $dir")
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            if (children.isNullOrEmpty())
                return false
            for (i in children.indices) {
                val success: Boolean = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }.onFailure {
        return false
    }
    return false
}

suspend fun Context.writeToCache(filePath: String, fileName: String): String? =
    withContext(Dispatchers.IO) {
        val cacheFile =
            File(cacheDir, fileName)

        if (cacheFile.exists()) {
            cacheFile.delete()
        }
        val inputStream = contentResolver?.openInputStream(
            Uri.parse(filePath)
        )
        cacheFile.outputStream().use { inputStream?.copyTo(it) }

        cacheFile.absolutePath
    }


fun String?.isTiffss(): Boolean {
    if (this.isNullOrEmpty())
        return false
    return endsWith("tif", true) || endsWith("tiff", true)
}

fun String?.isHeifs(): Boolean {
    if (this.isNullOrEmpty())
        return false
    return endsWith("heif", true) || endsWith("heic", true)
}

fun Uri?.hasValidFile(): Boolean {
    if (this == null)
        return false
    val file = File(this.toString())
    return file.exists() && file.length() > 0
}

fun URL.toBitmap(): Bitmap? {
    return BitmapFactory.decodeStream(openStream())
}

fun Bitmap.rotateBitmap(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        this,
        0,
        0,
        width,
        height,
        matrix,
        true
    )
}


fun File.grantedUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.provider",
            this
        )
    } else {
        Uri.fromFile(this)
    }
}

fun createVideoFile(context: Context): File {
    // Create an image file name
    val imageFileName = String.format("%s_%d", "ClientBoxx_captured", (100..1000).random())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        imageFileName, /* prefix */
        ".mp4", /* suffix */
        storageDir      /* directory */
    )
}

fun createImageFile(context: Context): File {
    // Create an image file name
    val imageFileName = String.format("%s_%d", "singer_captured", (100..1000).random())
    val storageDir = context.cacheDir

    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir      /* directory */
    )
}

fun String.getFileNameFromUrl() = substring(lastIndexOf("/") + 1)

fun createDCIMFile(): File {
    // Create an image file name
    val imageFileName = getUniqueIdString() + "_normal" + ".jpg"
    val path = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DCIM
    ).absolutePath.plus("/Camera")
    return File(path + File.separator + imageFileName)
}


fun isExternalStorageAvailable(): Boolean {
    val state = Environment.getExternalStorageState()
    return state == Environment.MEDIA_MOUNTED
}

fun getUniqueIdString(): String {
    return UUID.randomUUID().toString()
}

fun String?.fileExtension(): String {
    if (this.isNullOrEmpty())
        return ""

    if (this.indexOf(".") > 0) {
        return this.substring(this.lastIndexOf("."), this.length)
    }
    return ""
}


fun Context.openLocalFile(filePath: String?) {

    val selectedFile = File(filePath!!)

    if (selectedFile.exists()) {
        val intent = Intent(Intent.ACTION_VIEW)
        val selectedFileUri = FileProvider.getUriForFile(
            this, applicationContext
                .packageName + ".provider", selectedFile
        )
        intent.apply {
            setDataAndType(selectedFileUri, selectedFile.getMimeTypeForIntent())
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        kotlin.runCatching {
            startActivity(intent)
        }.onFailure {
            showToast("No apps installed to handle this file type!")
        }
    } else {
        showToast("File does not exists!")
    }
}

fun getMimeTypeByExt(fileName: String): String{
    return when (fileName.fileExtension()) {
        ".msg", ".eml" -> "application/vnd.ms-outlook"
        ".pdf" -> "application/pdf"
        ".exe" -> "application/exe"
        ".doc", ".docx", ".xls", ".xlsx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        ".ppt" -> "application/vnd.ms-powerpoint"
        ".xls & .xlsx" -> "application/vnd.ms-excel"
        ".txt" -> "text/plain"
        ".zip" -> "application/zip"
        else -> "image/8"
    }
}




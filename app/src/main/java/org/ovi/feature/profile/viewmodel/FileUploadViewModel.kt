package org.ovi.feature.profile.viewmodel

import android.net.Uri

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.ovi.base.BaseViewModel
import org.ovi.common.OviApp
import org.ovi.feature.profile.domain.IFileUploadRepo
import org.ovi.network.ResultOf
import org.ovi.util.extensions.getExtensionFromUri
import org.ovi.util.extensions.getFileName
import org.ovi.util.extensions.hasExtension
import org.ovi.util.extensions.writeToCache

class FileUploadViewModel(private val iFileUploadRepo: IFileUploadRepo) : BaseViewModel(){

    private val context by lazy { OviApp.getAppContext() }

    private val _fileUpload =
        MutableStateFlow<ResultOf<String>>(ResultOf.Empty())
    val fileUpload: StateFlow<ResultOf<String>>
        get() = _fileUpload

    private val _imageUrl= MutableStateFlow<String?>(null)
val imageUrl:StateFlow<String?>
get() = _imageUrl

    private val _multipleFileUpload =
        MutableStateFlow<ResultOf<List<String>>>(ResultOf.Empty())
    val multipleFileUpload: StateFlow<ResultOf<List<String>>>
        get() = _multipleFileUpload


    fun uploadMultipleFiles(uriList : ArrayList<Uri>, processFile : Boolean = true){
        execute {
            val resultList = ArrayList<String>()
            uriList.forEach {
               kotlin.runCatching {
                   if(processFile)
                       iFileUploadRepo.uploadFile(processFile(it))
                   else iFileUploadRepo.uploadFile(it.path)
               }.onSuccess {
                   if(it.status == 1)
                       resultList.add(it.message)
                   else
                       _multipleFileUpload.value = ResultOf.Failure(it.message)
               }.onFailure {
                   _multipleFileUpload.value = ResultOf.Failure(it.message)
               }
            }
            if(resultList.isNotEmpty())
                _multipleFileUpload.value = ResultOf.Success(resultList)
            else
                _multipleFileUpload.value = ResultOf.Empty("Unable to upload files. Error code 1")

            _multipleFileUpload.value = ResultOf.Progress(false)
        }
    }

    fun uploadFile(uri : Uri, processFile : Boolean = true){
        execute {
            _fileUpload.value = ResultOf.Progress(true)
            kotlin.runCatching {
                if(processFile)
                    iFileUploadRepo.uploadFile(processFile(uri))
                else iFileUploadRepo.uploadFile(uri.path)
            }.onSuccess {
                if(it.status == 1)
                    _fileUpload.value = ResultOf.Success(it.message)
                else
                    _fileUpload.value = ResultOf.Failure(it.message)
            }.onFailure {
                _fileUpload.value = ResultOf.Failure(it.message)
            }
            _fileUpload.value = ResultOf.Progress(false)
        }
    }


    private suspend fun processFile(data: Uri) = withContext(Dispatchers.IO) {

        var resultFilePath: String? = null

        kotlin.runCatching {
            val rawFileName = context.getFileName(data)
            var trimmedFileName = rawFileName.replace(" ", "_")
            if (!rawFileName.hasExtension()) {
                val fileExtension = context.getExtensionFromUri(data)
                trimmedFileName = trimmedFileName.plus(".$fileExtension")
            }

            val cacheFilePathDeferred = async {
                context.writeToCache(
                    data.toString(),
                    trimmedFileName
                )
            }
            cacheFilePathDeferred.await()
        }.onSuccess {
            resultFilePath = it
        }
        resultFilePath
    }

}
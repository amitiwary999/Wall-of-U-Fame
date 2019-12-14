package com.example.amit.uniconnexample.MediaPicker

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import androidx.annotation.WorkerThread
import com.example.amit.uniconnexample.Executor.Executor
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


/**
 * Created by Meera on 13,December,2019
 */
class MediapickerRepository {

    /**
     * Retrieves a list of folders that contain media.
     */
    fun getFolders(context: Context, callback: Callback<List<MediaFolder>>) {
        Executor.BOUNDED.execute({ callback.onComplete(getFolders(context)) })
    }

    /**
     * Retrieves a list of media items (images and videos) that are present int he specified bucket.
     */
    fun getMediaInBucket(context: Context, bucketId: String, callback: Callback<List<Media>>) {
        Executor.BOUNDED.execute({ callback.onComplete(getMediaInBucket(context, bucketId)) })
    }

    /**
     * Given an existing list of {@link Media}, this will ensure that the media is populate with as
     * much data as we have, like width/height.
     */
    fun getPopulatedMedia(context: Context, media: List<Media>, callback : Callback<List<Media>>) {
        if(media.all { isPopulated(it) }){
            callback.onComplete(media)
            return
        }

        Executor.BOUNDED.execute{callback.onComplete(getPopulatedMedia(context, media))}
    }

    fun getMostRecentItem(context: Context, callback: Callback<Media>) {
        Executor.BOUNDED.execute({ callback.onComplete(getMostRecentItem(context)) })
    }

    private fun getFolders(context: Context) : List<MediaFolder>{
        val imageFolders = getFolders(context, Images.Media.EXTERNAL_CONTENT_URI)
        val videoFolders = getFolders(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

        val folders:HashMap<String, FolderData> = HashMap(imageFolders.folderData)
        for(entry in videoFolders.folderData.entries){
            if (folders.containsKey(entry.key)) {
                folders.get(entry.key)?.incrementCount(entry.value.count);
            } else {
                folders.put(entry.key, entry.value)
            }
        }
        val cameraBucketId = if(imageFolders.cameraBucketId != null){
            imageFolders.cameraBucketId
        }else{
            videoFolders.cameraBucketId
        }

        val cameraFolder: FolderData?= if(cameraBucketId != null) {
            folders.remove(cameraBucketId)
        }else{
            null
        }

        var mediaFolders: MutableList<MediaFolder> =  folders.values.map {
            MediaFolder(it.thumbnail.toString(), it.title, it.count, it.bucketId, MediaFolder.FolderType.NORMAL)
        }.filter {
            it.title != null
        }.sortedBy {
            it.title
        }.toMutableList()

        val allMediaThumbnail =  if(imageFolders.thumbnailTimestamp>videoFolders.thumbnailTimestamp){
            imageFolders.thumbnail
        }else{
           videoFolders.thumbnail
        }

        if(allMediaThumbnail != null){
            var allMediaCount =  mediaFolders.fold(0) {count, folder ->
                count+folder.itemCount
            }
            if(cameraFolder != null){
                allMediaCount += cameraFolder.count
            }
            mediaFolders.add(0, MediaFolder(allMediaThumbnail.toString(), "All Media", allMediaCount, "", MediaFolder.FolderType.NORMAL));
        }

        if (cameraFolder != null) {
            mediaFolders.add(0, MediaFolder(cameraFolder.thumbnail.toString(), cameraFolder.title, cameraFolder.count, cameraFolder.bucketId, MediaFolder.FolderType.CAMERA));
        }
        return mediaFolders
    }

    @WorkerThread
    private fun getFolders(context: Context, contentUri: Uri): FolderResult {
        val cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + File.separator + "Camera"
        var cameraBucketId: String? = null
        var globalThumbnail: Uri? = null
        var thumbnailTimestamp: Long = 0
        val folders: MutableMap<String, FolderData> = HashMap()
        val projection = arrayOf(Images.Media.DATA, Images.Media.BUCKET_ID, Images.Media.BUCKET_DISPLAY_NAME, Images.Media.DATE_TAKEN)
        val selection = Images.Media.DATA + " NOT NULL"
        val sortBy = Images.Media.BUCKET_DISPLAY_NAME + " COLLATE NOCASE ASC, " + Images.Media.DATE_TAKEN + " DESC"
        context.contentResolver.query(contentUri, projection, selection, null, sortBy).use { cursor ->
            while (cursor != null && cursor.moveToNext()) {
                val path = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]))
                val thumbnail = Uri.fromFile(File(path))
                val bucketId = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]))
                val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(projection[3]))
                val folder: FolderData = if(folders.get(bucketId) != null){
                    folders.get(bucketId)!!
                }else{
                    FolderData(thumbnail, title, bucketId)
                }

                folder.incrementCount()
                folders[bucketId] = folder
                if (cameraBucketId == null && path.startsWith(cameraPath)) {
                    cameraBucketId = bucketId
                }
                if (timestamp > thumbnailTimestamp) {
                    globalThumbnail = thumbnail
                    thumbnailTimestamp = timestamp
                }
            }
        }
        return FolderResult(cameraBucketId, globalThumbnail, thumbnailTimestamp, folders)
    }

    @WorkerThread
    private fun getMediaInBucket(context: Context, bucketId: String): List<Media> {
       //check permission
        val images = getMediaInBucket(context, bucketId, Images.Media.EXTERNAL_CONTENT_URI, true)
        val videos = getMediaInBucket(context, bucketId, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, false)
        val media: MutableList<Media> = ArrayList(images.size + videos.size)
        media.addAll(images)
        media.addAll(videos)
        media.sortedWith(compareBy(Media::date))
        return media
    }

    @WorkerThread
    private fun getMediaInBucket(context: Context, bucketId: String, contentUri: Uri, hasOrientation: Boolean): List<Media> {
        val media: MutableList<Media> = LinkedList()
        var selection = Images.Media.BUCKET_ID + " = ? AND " + Images.Media.DATA + " NOT NULL"
        var selectionArgs: Array<String>? = arrayOf(bucketId)
        val sortBy = Images.Media.DATE_TAKEN + " DESC"
        val projection: Array<String>
        projection = if (hasOrientation) {
            arrayOf(Images.Media.DATA, Images.Media.MIME_TYPE, Images.Media.DATE_TAKEN, Images.Media.ORIENTATION, Images.Media.WIDTH, Images.Media.HEIGHT, Images.Media.SIZE)
        } else {
            arrayOf(Images.Media.DATA, Images.Media.MIME_TYPE, Images.Media.DATE_TAKEN, Images.Media.WIDTH, Images.Media.HEIGHT, Images.Media.SIZE)
        }

        context.getContentResolver().query(contentUri, projection, selection, selectionArgs, sortBy).use { cursor ->
            while (cursor != null && cursor.moveToNext()) {
                val path: String = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]))
                val uri = Uri.fromFile(File(path))
                val mimetype: String = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.MIME_TYPE))
                val dateTaken: Long = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.DATE_TAKEN))
                val orientation = if (hasOrientation) cursor.getInt(cursor.getColumnIndexOrThrow(Images.Media.ORIENTATION)) else 0
                val width: Int = cursor.getInt(cursor.getColumnIndexOrThrow(getWidthColumn(orientation)))
                val height: Int = cursor.getInt(cursor.getColumnIndexOrThrow(getHeightColumn(orientation)))
                val size: Long = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE))
                media.add(Media(uri.toString(), mimetype, dateTaken, width, height, size))
            }
        }
        return media
    }

    @TargetApi(16)
    private fun getWidthColumn(orientation: Int): String? {
        return if (orientation == 0 || orientation == 180) Images.Media.WIDTH else Images.Media.HEIGHT
    }

    @TargetApi(16)
    private fun getHeightColumn(orientation: Int): String? {
        return if (orientation == 0 || orientation == 180) Images.Media.HEIGHT else Images.Media.WIDTH
    }

    fun getLocallyPopulatedMedia(media: Media) : Media {
        val width  = media.width
        val height = media.height
        val size   = media.size

        return Media(media.uri, media.mimeType, media.date, width, height, size)
    }

    fun getContentResolverPopulatedMedia(media : Media) : Media {
        val width  = media.width
        val  height = media.height
        val size   = media.size

        return Media(media.uri, media.mimeType, media.date, width, height, size);
    }

    fun getPopulatedMedia(context: Context, media: List<Media>) : List<Media>{
        //check for permission

        return media.map {
            if(isPopulated(it)){
                it
            }else{
                getContentResolverPopulatedMedia(it)
            }
        }.toList()
    }

    fun isPopulated(media : Media): Boolean {
        return media.width > 0 && media.height > 0 && media.size > 0
    }

    @WorkerThread
    fun getMostRecentItem(context : Context) : Media {

        val media = getMediaInBucket(context, "", Images.Media.EXTERNAL_CONTENT_URI, true);
        return media.get(0)
    }

    private class FolderResult(val cameraBucketId: String?, val thumbnail: Uri?,
                               val thumbnailTimestamp: Long, val folderData: Map<String, FolderData>)

    private class FolderData(val thumbnail: Uri, val title: String, val bucketId: String) {
        var count = 0
            private set

        @JvmOverloads
        fun incrementCount(amount: Int = 1) {
            count += amount
        }

    }

    interface Callback<E> {
        fun onComplete(result: E)
    }
}
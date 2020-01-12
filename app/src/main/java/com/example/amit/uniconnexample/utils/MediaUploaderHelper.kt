package com.example.amit.uniconnexample.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Others.CommonString
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Single
import java.io.ByteArrayOutputStream

/**
 * Created by Meera on 12,January,2020
 */
class MediaUploaderHelper {
    companion object{
        fun uploadMedia(uri: Uri?, mimeType: String, child: String, id: String, extType: String?, typeThumbOriginal: String): Single<String>{
            return Single.create<String> { emitter ->
                lateinit var filepath: StorageReference
                var uploadTask: UploadTask?= null
                if(typeThumbOriginal == CommonString.THUMBNAIL_GENERATE){
                    filepath = FirebaseStorage.getInstance(CommonString.STORAGE_URL).reference.child(child).child("$id.${CommonString.EXT_JPEG}")
                    var thumbnailBitmap: Bitmap ?= null
                    if(mimeType.contains(CommonString.MimeType.IMAGE) && uri != null){
                        val exif = ExifInterface(uri.path)
                        val imageData=exif.getThumbnail();
                        if(imageData == null){
                            try{
                                val bitmap = MediaStore.Images.Media.getBitmap(App.instance.contentResolver, uri);
                                thumbnailBitmap = ThumbnailUtils.extractThumbnail(bitmap,120,120);
                            } catch (e: Exception){
                                e.printStackTrace()
                            }
                        }else{
                            thumbnailBitmap= BitmapFactory.decodeByteArray(imageData,0,imageData.size)
                        }
                    }else if(mimeType.contains(CommonString.MimeType.VIDEO) && uri != null){
                        thumbnailBitmap = ThumbnailUtils.createVideoThumbnail(uri.path, MediaStore.Video.Thumbnails.MINI_KIND)
                    }

                    if(thumbnailBitmap != null){
                        val baos = ByteArrayOutputStream()
                        thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                        val data = baos.toByteArray()
                        uploadTask = filepath?.putBytes(data)
                    }else{
                       emitter.onSuccess("")
                    }
                }else{
                    filepath = FirebaseStorage.getInstance(CommonString.STORAGE_URL).reference.child(child).child("$id.$extType")
                    if(uri != null) {
                        uploadTask = filepath?.putFile(uri)
                    }else{
                        emitter.onSuccess("")
                    }
                }

                uploadTask?.addOnFailureListener {
                    emitter.onSuccess("")
                }

                uploadTask?.addOnSuccessListener {
                    uploadTask.continueWithTask {
                        filepath?.downloadUrl
                    }.addOnCompleteListener {
                        if(it.isSuccessful && it.result != null){
                            emitter.onSuccess(it.result!!.toString())
                        }else{
                            emitter.onSuccess("")
                        }
                    }
                }
            }
        }
    }
}
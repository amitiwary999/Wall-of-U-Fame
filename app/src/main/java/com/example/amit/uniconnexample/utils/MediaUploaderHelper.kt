package com.example.amit.uniconnexample.utils

import android.graphics.Bitmap
import android.net.Uri
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
        fun uploadMedia(uri: Uri?, bitmap: Bitmap?, mimeType: String, child: String, id: String, extType: String?): Single<String>{
            return Single.create<String> { emitter ->
                var filepath: StorageReference?= FirebaseStorage.getInstance(CommonString.STORAGE_URL).reference.child(child).child("$id.$extType")
                var uploadTask: UploadTask?= null
                if(uri != null) {
                    uploadTask = filepath?.putFile(uri)
                }else if(bitmap != null){
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                    val data = baos.toByteArray()
                    uploadTask = filepath?.putBytes(data)
                }else{
                    emitter.onSuccess("")
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
package com.aditya.appstoryaditya.util

import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aditya.appstoryaditya.models.User
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

object Constant {


    val KEY_USERID = stringPreferencesKey("userId")
    val KEY_NAME = stringPreferencesKey("name")
    val KEY_TOKEN = stringPreferencesKey("token")

    const val TAG = "Response:::"

    fun Context.createProgress(): CircularProgressDrawable {
        return CircularProgressDrawable(this).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

    val User.tokenBearer : String get() = "Bearer ${this.token}"

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    fun bitmapToFile(bitmap: Bitmap, context: Context): Uri {
        val wrapper = ContextWrapper(context)

        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }
    fun TextInputLayout.setInputError(message: String): Boolean {
        this.isErrorEnabled = false
        this.error = message
        return false
    }

 fun getRotatedBitmap(file: File): Bitmap? {
        val imgBitmap = BitmapFactory.decodeFile(file.path)

        val ei: ExifInterface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ExifInterface(file)
        } else {
            ExifInterface(file.path)
        }
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val rotatedBitmap: Bitmap? = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(
                imgBitmap,
                90
            )

            ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(
                imgBitmap,
                180
            )

            ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(
                imgBitmap,
                270
            )

            ExifInterface.ORIENTATION_NORMAL -> imgBitmap
            else -> imgBitmap
        }
        return rotatedBitmap
    }

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

}
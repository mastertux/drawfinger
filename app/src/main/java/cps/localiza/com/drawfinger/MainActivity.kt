package cps.localiza.com.drawfinger

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.support.v4.content.ContextCompat
import java.io.*
import java.util.*
import android.support.v4.app.ActivityCompat
import android.os.Build


class MainActivity : AppCompatActivity() {

    val REQUEST_CODE_DRAW = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img_assinatura.setOnClickListener{
            val intent = Intent(this, DrawingActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_DRAW)
        }

        requestAppPermissions()
    }

    // Get bitmap in onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            when(requestCode){
                REQUEST_CODE_DRAW -> {
                    val result= data.getByteArrayExtra("bitmap")
                    val bitmap = BitmapFactory.decodeByteArray(result, 0, result.size)
                    val uri = saveFile(bitmap)

                    img_assinatura.setImageURI(uri)
                }
            }
        }
    }

    private fun saveFile(bitmap: Bitmap): Uri {

        // Initialize a new file instance to save bitmap object
        val card = Environment.getExternalStorageDirectory ()
        var file = File (card.getAbsolutePath(), "Images")

        if (!file.exists()) {
            file.mkdir()
        }

        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    private fun requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return
        }

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1) // your request code
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(baseContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(baseContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}

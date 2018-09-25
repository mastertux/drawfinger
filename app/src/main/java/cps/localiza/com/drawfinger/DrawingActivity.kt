package cps.localiza.com.drawfinger

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_drawing.*
import java.io.ByteArrayOutputStream

class DrawingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        btn_prosseguir.setOnClickListener {
            val bStream = ByteArrayOutputStream()
            val bitmap = draw_view.getBitmap()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)
            val byteArray = bStream.toByteArray()
            val returnIntent = Intent()
            returnIntent.putExtra("bitmap", byteArray)
            setResult(Activity.RESULT_OK,returnIntent)
            finish()
        }

        btn_limpar.setOnClickListener{
            draw_view.clearCanvas()
        }
    }
}

package com.psdemo.globomanticssales

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align.*
import android.graphics.pdf.PdfDocument
import android.text.format.DateFormat
import android.util.Log
import com.psdemo.globomanticssales.data.Client
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


    private val FILE_NAME = "proposal.pdf"
    private fun Context.getFolder(id:Int) : File {
        val directory = File("${this.filesDir}/$id")

        if(!directory.exists()){
            directory.mkdir()
        }

        return directory
    }

    fun Context.buildPdf(client : Client){
        val directory = this.getFolder(client.id)
        val inch = 72
        val halfInch = inch /2
        val inchFloat = inch.toFloat()
        val halfInchFloat = halfInch.toFloat()

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(inch * 8 + halfInch,inch * 11,1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()
        paint.color = Color.BLACK
        paint.textAlign = RIGHT

        val calendar = Calendar.getInstance()
        val dateFormat = DateFormat.getDateFormat(this)
        calendar.timeInMillis = client.date
        canvas.drawText(dateFormat.format(calendar.time),inch * 7 + halfInchFloat,inchFloat,paint)

        val logo = this.resources.getDrawable(R.drawable.logo,theme)
        logo.setBounds(inch,inch,inch * 6 + halfInch,inch * 2)
        logo.draw(canvas)

        val sharedPreferences = this.getSharedPreferences(getString(R.string.profile_preference_key),Context.MODE_PRIVATE)

        val salesPersonName = sharedPreferences.getString(getString(R.string.profile_name_key),getString(R.string.profile_name_key_default))

        paint.textAlign =LEFT

        canvas.drawText("Salesperson Name: $salesPersonName",inchFloat,inchFloat * 2 + halfInchFloat,paint)

        canvas.drawText("Client: ${client.name}",inchFloat,inchFloat * 3 + halfInchFloat,paint)

        canvas.drawText("Order: ${client.order}",inchFloat,inchFloat * 4 + halfInchFloat,paint)

        canvas.drawText("Terms: ${client.terms}",inchFloat,inchFloat * 5 + halfInchFloat,paint)

        document.finishPage(page)

        val file = File(directory,FILE_NAME)

        try{
            document.writeTo(FileOutputStream(file))
        }catch (e : IOException){
            Log.e("TAG", "buildPdf: ${e.toString()}",e )
        }

        document.close()



    }

    fun Context.proposalExists(id : Int): Boolean {
        return File(this.getFolder(id),FILE_NAME).exists()
    }

    fun Context.getFiles(id:Int): List<File> {
        return this.getFolder(id).listFiles().asList()
    }

    fun Context.saveImage(bitmap: Bitmap,fileName : String,id: Int){
        val directory = this.getFolder(id)
        val file = File(directory,"${fileName}.png")
        val outputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream)
        outputStream.flush()
        outputStream.close()
    }

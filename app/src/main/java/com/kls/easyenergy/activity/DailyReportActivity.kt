package com.kls.easyenergy.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.getAbsolutePath
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.kls.easyenergy.R
import com.kls.easyenergy.databinding.ActivityDailyReportBinding
import com.kls.easyenergy.model.DailyReportResponse
import com.kls.easyenergy.model.ImagePreditResponse
import com.kls.easyenergy.model.PostDailyReportResponse
import com.kls.easyenergy.utils.UserPreference
import com.kls.easyenergy.viewmodel.LoginViewModel
import com.kls.easyenergy.viewmodel.ViewModelFactory
import com.kls.jnecourierapps.utils.ApiConfig
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DailyReportActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDailyReportBinding
    private lateinit var start_mills: String
    private lateinit var end_mills: String
    private var dayrange: Long = 0
    private lateinit var id_room: String
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    val timeStamp: String = SimpleDateFormat(
        "dd-MMM-yyyy",
        Locale.US
    ).format(System.currentTimeMillis())
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userToken: String
    private lateinit var dates: String

    private val cameraMeteran =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(this, "Image Berhasil Dipilih", Toast.LENGTH_SHORT).show()
                    val fileUri = data?.data!!
                    val file = DocumentFileCompat.fromUri(this, fileUri)
                    val absoultePath = file!!.getAbsolutePath(this)
                    Glide.with(this)
                        .load(fileUri)
                        .into(binding.imgMeteran)
                    binding.btnScan.text = "Submit"
                    binding.btnScan.setOnClickListener {
                        kwhPredictTask(fileUri)
                        showLoading(true)
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        start_mills = intent.getStringExtra("start_mills").toString()
        end_mills = intent.getStringExtra("end_mills").toString()
        dayrange = intent.getLongExtra("dayrange",0)
        id_room = intent.getStringExtra("id_room").toString()
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]
        setupViewModel()
        val myCalendarViewManager = object :
            CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // set date to calendar according to position where we are
                val cal = Calendar.getInstance()
                cal.time = date
                // if item is selected we return this layout items
                // in this example. monday, wednesday and friday will have special item views and other days
                // will be using basic item view
                return if (isSelected)
                    R.layout.selected_calendar_item
                else
                    R.layout.calendar_item

            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // using this method we can bind data to calendar view
                // good practice is if all views in layout have same IDs in all item views
                val tv_date = holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item)
                tv_date.text = DateUtils.getDayNumber(date)
                val tv_day = holder.itemView.findViewById<TextView>(R.id.tv_day_calendar_item)
                tv_day.text = DateUtils.getDay3LettersName(date)

            }
        }

        binding.btnScan.setOnClickListener {
            ImagePicker.with(this)
                .createIntent { intent ->
                    cameraMeteran.launch(intent)
                }
        }
        var is_first: Boolean = false
        val myCalendarChangesObserver = object :
            CalendarChangesObserver {
            // you can override more methods, in this example we need only this one

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
//                Toast.makeText(this@DailyReportActivity, "s ${date.time.toString()}", Toast.LENGTH_SHORT).show()
                binding.btnScan.visibility = View.VISIBLE
                if (isSelected){
                    dates = date.time.toString()
                    val converteddate = convertLongToTime(dates.toLong())
                    val datetomills = convertDateToLong(converteddate)
                    getDailyReport(userToken,id_room,datetomills.toString())
                        Log.d("DATES1", dates)
                }
//                getDailyReport(userToken, id_room)
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance()
                cal.time = date
                // in this example sunday and saturday can't be selected, others can
//                return when (cal[Calendar.DAY_OF_WEEK]) {
//                    Calendar.SATURDAY -> false
//                    Calendar.SUNDAY -> false
//                    else -> true
//                }
                return true
            }
        }
        val singleRowCalendar = binding.timelinepicker.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf(),start_mills.toLong())
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            userToken = user.token
        }
    }

    private fun getDates(list: MutableList<Date>, start_mills: Long): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        val dates = convertLongToDay(start_mills)

        if (dates != null) {
            calendar.set(Calendar.DAY_OF_MONTH, dates.toInt())
        }
        list.add(calendar.time)
        for (i in 2..dayrange){
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
//        while (currentMonth == calendar[Calendar.MONTH]) {
//
//        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    fun getDateFromatedString(inputDate: String?): String? {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        var date: Date? = null
        try {
            date = simpleDateFormat.parse(inputDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date == null) {
            return ""
        }
        val convetDateFormat = SimpleDateFormat("dd")
        return convetDateFormat.format(date)
    }

    fun convertLongToDay(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd")
        return format.format(date)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy-MM-dd")
        return df.parse(date).time
    }

    fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MMMM-yyyy")
        return format.format(date)
    }

    private fun kwhPredictTask(meteranFile: Uri){
        Toast.makeText(this@DailyReportActivity, "Proses ini akan memakan beberapa detik Mohon Tunggu", Toast.LENGTH_SHORT).show()
        val photoFile = uriToFile(meteranFile,this)
        val photoRequest = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val meteranImageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            photoFile.name,
            photoRequest
        )

        val client = ApiConfig.getApiService().kwhPredict(meteranImageMultipart)
        client.enqueue(object : Callback<ImagePreditResponse> {
            override fun onResponse(
                call: Call<ImagePreditResponse>,
                response: Response<ImagePreditResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(this@DailyReportActivity, "kwh : "+responseBody.electricityUsage, Toast.LENGTH_SHORT).show()
                        binding.btnScan.text = "Submit Daily Report"
                        binding.btnScan.setOnClickListener {
                            val converteddate = convertLongToTime(dates.toLong())
                            val datetomills = convertDateToLong(converteddate)
                            postDailyReport(userToken,id_room,datetomills.toString(),responseBody.electricityUsage,meteranImageMultipart)
                        }
                    }
                }else {
                    Toast.makeText(this@DailyReportActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ImagePreditResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@DailyReportActivity, "Gagal instance Retrofit ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDailyReport(token: String, idRoom: String, date: String){
        val client = ApiConfig.getApiService().getDailyReportTask("Bearer $token",idRoom,date)
        showLoading(true)
        client.enqueue(object : Callback<DailyReportResponse> {
            override fun onResponse(
                call: Call<DailyReportResponse>,
                response: Response<DailyReportResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val is_done = response.body()?.data?.isDone
                    val url = response.body()?.data?.kwhImage
                    val totalkwhusage = response.body()?.data?.totalKwhUsage
                    val todayusage = response.body()?.data?.todayKwhUsage
                    val mDates = convertLongToDate(dates.toLong())
                    if (is_done == true){
                        binding.imgMeteran.setImageDrawable(getDrawable(R.drawable.happy))
                        binding.btnScan.text = "Lihat Foto Meteran"
                        binding.txtUsage.visibility = View.VISIBLE
                        binding.txtUsage.text = "Di tanggal $mDates Kamu Menggunakan kwh sebanyak $todayusage\n\nDari hari pertama kamu sudah\nmenggunakan kwh sebanyak $totalkwhusage"
                        binding.btnScan.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                startActivity(intent)
                        }
                    }else {
                        binding.btnScan.text = "Ambil Foto Meteran"
                        binding.imgMeteran.setImageDrawable(getDrawable(R.drawable.scan))
                        binding.txtUsage.visibility = View.GONE
                        binding.btnScan.setOnClickListener {
                            ImagePicker.with(this@DailyReportActivity)
                                .createIntent { intent ->
                                    cameraMeteran.launch(intent)
                                }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DailyReportResponse>, t: Throwable) {
                showLoading(false)
            }

        })
    }

    private fun postDailyReport(token: String, idRoom: String, date: String, kwhusage: String,meteranImageMultipart: MultipartBody.Part){
        val client = ApiConfig.getApiService().postDailyReportTask("Bearer $token",idRoom,date, kwhusage,meteranImageMultipart)
        showLoading(true)
        client.enqueue(object : Callback<PostDailyReportResponse> {
            override fun onResponse(
                call: Call<PostDailyReportResponse>,
                response: Response<PostDailyReportResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val currentkwhusage = response.body()?.data?.currentKwhUsage.toString()
                    val total_usage = response.body()?.data?.currentKwhUsage.toString()
                    finish()
                    Toast.makeText(this@DailyReportActivity, "Sukses", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostDailyReportResponse>, t: Throwable) {
                showLoading(false)
            }

        })
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbUpload.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
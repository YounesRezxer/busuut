package com.example.busuut
//==========================================================================================
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.busuut.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

//==========================================================================================
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//==========================================================================================
    private val normalDaysRoute1 = listOf("7:35", "8:40", "9:05", "9:55", "10:35", "11:20", "11:50", "12:20", "13:20", "13:40", "14:10", "14:50", "15:30", "15:50", "16:10", "16:35", "17:05")
    private val normalDaysRoute2 = listOf("8:20", "8:45", "9:35", "10:15", "11:00", "11:30", "12:00", "12:30", "13:00", "13:20", "13:50", "14:30", "15:10", "15:30", "15:50", "16:15", "16:45", "17:40", "18:15", "19:10")

    private val jahatlooRoute1 = listOf("7:35", "9:35", "11:05", "12:30", "13:30")
    private val jahatlooRoute2 = listOf("9:30", "11:00", "13:00", "14:00", "15:30")

    private val wednesdayRoute1 = listOf("7:35", "9:05", "10:35", "12:20", "13:40", "14:50", "15:50", "16:35", "17:05")
    private val wednesdayRoute2 = listOf("8:45", "10:15", "12:00", "13:20", "14:30", "15:30", "16:15", "16:45", "17:40")
//==========================================================================================

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            R.id.action_contact -> {
                openTelegram()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

//==========================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//==========================================================================================

        val busImage = findViewById<ImageView>(R.id.busImage)

        // کد انیمیشن رو اینجا بذار:
        busImage.post {
            val screenWidth = resources.displayMetrics.widthPixels.toFloat()
            fun animateBus() {
                busImage.translationX = screenWidth // شروع از خارج صفحه سمت راست
                busImage.animate()
                    .translationX(-busImage.width.toFloat()) // حرکت به سمت خارج صفحه سمت چپ
                    .setDuration(5000)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            animateBus() // دوباره حرکت رو شروع کن
                        }
                    })
                    .start()
            }
            animateBus() // شروع انیمیشن
        }

//==========================================================================================
        val dayButtons = listOf(binding.btnNormalDays, binding.btnJahatloo, binding.btnWednesdays)
        val routeButtons = listOf(binding.btnRoute1, binding.btnRoute2)

        fun selectDayButton(selectedButton: Button) {
            dayButtons.forEach {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.gray2))
            }
            selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.yello2))
        }

        fun selectRouteButton(selectedButton: Button) {
            routeButtons.forEach {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.gray2))
            }
            selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.yello2))
        }
//==========================================================================================
        binding.btnNormalDays.setOnClickListener {
            animateButtonClick(it)
            binding.routeSelectionButtons.visibility = View.VISIBLE
            selectDayButton(binding.btnNormalDays)

            binding.btnRoute1.text = "مسیر دانشکده"
            binding.btnRoute2.text = "مسیر دانشگاه"

            binding.btnRoute1.setOnClickListener {
                animateButtonClick(it)
                selectRouteButton(binding.btnRoute1)
                updateTimes(normalDaysRoute1)
            }

            binding.btnRoute2.setOnClickListener {
                animateButtonClick(it)
                selectRouteButton(binding.btnRoute2)
                updateTimes(normalDaysRoute2)
            }
        }

        binding.btnJahatloo.setOnClickListener {
            animateButtonClick(it)
            binding.routeSelectionButtons.visibility = View.VISIBLE
            selectDayButton(binding.btnJahatloo)

            binding.btnRoute1.text = "دانشگاه"
            binding.btnRoute2.text = "سایت جهتلو"

            binding.btnRoute1.setOnClickListener {
                animateButtonClick(it)
                selectRouteButton(binding.btnRoute1)
                updateTimes(jahatlooRoute1)
            }

            binding.btnRoute2.setOnClickListener {
                animateButtonClick(it)
                selectRouteButton(binding.btnRoute2)
                updateTimes(jahatlooRoute2)
            }
        }

        binding.btnWednesdays.setOnClickListener {
            animateButtonClick(it)
            binding.routeSelectionButtons.visibility = View.VISIBLE
            selectDayButton(binding.btnWednesdays)

            binding.btnRoute1.text = "مسیر دانشکده"
            binding.btnRoute2.text = "مسیر دانشگاه"

            binding.btnRoute1.setOnClickListener {
                animateButtonClick(it)
                selectRouteButton(binding.btnRoute1)
                updateTimes(wednesdayRoute1)
            }

            binding.btnRoute2.setOnClickListener {
                animateButtonClick(it)
                selectRouteButton(binding.btnRoute2)
                updateTimes(wednesdayRoute2)
            }
        }
    }
//==========================================================================================
private fun updateTimes(routeTimes: List<String>) {
    binding.linearLayoutTimes.removeAllViews()
    val currentTime = getCurrentTime()

    for (time in routeTimes) {
        val remainingTime = getRemainingTime(currentTime, time)
        val timeText = if (remainingTime < 0) {
            "$time - از دست دادی"
        } else {
            if (remainingTime >= 60) {
                val hours = remainingTime / 60
                val minutes = remainingTime % 60
                if (minutes > 0) "$time - $hours ساعت و $minutes دقیقه" else "$time - $hours ساعت"
            } else {
                "$time - $remainingTime دقیقه"
            }
        }

        val textView = TextView(this)
        textView.text = timeText
        textView.textSize = 18f
        textView.setPadding(32, 24, 32, 24)
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

        // تعیین پس‌زمینه و رنگ متن بسته به وضعیت زمان
        if (remainingTime < 0) {
            textView.setBackgroundResource(R.drawable.rounded_corners_gray)
            textView.setTextColor(resources.getColor(android.R.color.black, theme))
        } else {
            textView.setBackgroundResource(R.drawable.rounded_corners_yello)
            textView.setTextColor(resources.getColor(android.R.color.black, theme))
        }

        // فاصله پایین برای هر تایم
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 16)
        textView.layoutParams = params

        binding.linearLayoutTimes.addView(textView)
    }
}
//==========================================================================================
    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }
//==========================================================================================
    private fun getRemainingTime(currentTime: String, busTime: String): Int {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentCalendar = Calendar.getInstance()
        val busCalendar = Calendar.getInstance()
        currentCalendar.time = sdf.parse(currentTime)!!
        busCalendar.time = sdf.parse(busTime)!!
        return ((busCalendar.timeInMillis - currentCalendar.timeInMillis) / 60000).toInt()
    }
//==========================================================================================
    private fun openTelegram() {
        val telegramUsername = "YOUNES_LOHRABI" // اینجا آی‌دی تلگرام رو بدون @ بذار
        val telegramUri = android.net.Uri.parse("tg://resolve?domain=$telegramUsername")
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, telegramUri)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            val webUri = android.net.Uri.parse("https://t.me/$telegramUsername")
            startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, webUri))
        }
    }
//==========================================================================================
    private fun showAboutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("درباره برنامه")
            .setMessage("برای ارتباط با ما روی دکمه تلگرام کلیک کنید.\n" +
                    "طراحی شده و ساخته شده توسط بسیج دانشجویی دانشگاه صنعتی ارومیه.")
            .setPositiveButton("تلگرام") { _, _ ->
                openTelegram()
            }
            .setNegativeButton("بستن", null)
            .show()
    }
//==========================================================================================

    private fun animateButtonClick(button: View) {
        button.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .duration = 100
            }
    }
//==========================================================================================
}
//==========================================================================================
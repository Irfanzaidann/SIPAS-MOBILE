package com.example.sipas.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sipas.R
import com.example.sipas.databinding.ActivityOnboardingBinding
import com.example.sipas.model.OnboardingCard
import com.example.sipas.ui.register.RegisterActivity
import com.squareup.picasso.Picasso

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private lateinit var image: ImageView

    private lateinit var title: TextView

    private lateinit var description: TextView

    private lateinit var nextButton: Button

    private lateinit var skipButton: Button

    private var currentIndex = 0

    private val TAG = "OnboardingActivity"

    private val imageSource = listOf(
        OnboardingCard(id = R.drawable.onboarding_1, "Cegah Stunting Sejak Dini Pada Anak", "Temukan berbagai resep makanan dan layanan kesehatan untuk pencegahan stunting pada anak"),
        OnboardingCard(id = R.drawable.onboarding_2, "Tersedia Layanan Konsultasi 24 Jam", "Tersedia layanan konsultasi 24 jam berbasis AI yang dapat digunakan kapan saja"),
        OnboardingCard(id = R.drawable.onboarding_3, "Ajukan Bantuan Agar Posyandu Dapat Membantu", "Gunakan fitur bantuan jika kamu mengalami masalah yang dapat dibantu oleh posyandu")
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "onCreate called")

        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        image = binding.imageView
        title = binding.title
        description = binding.description
        nextButton = binding.nextButton
        skipButton = binding.lewati

        Picasso.get()
            .load(imageSource[0].id)
            .into(image)

        title.text = imageSource[0].title
        description.text = imageSource[0].description

        skipButton.setOnClickListener {
            navigateToRegisterActivity()
        }

        nextButton.setOnClickListener {
            currentIndex++
            when(currentIndex){
                1 -> {
                    Picasso.get()
                        .load(imageSource[1].id)
                        .into(image)
                    title.text = imageSource[1].title
                    description.text = imageSource[1].description
                }
                2 -> {
                    Picasso.get()
                        .load(imageSource[2].id)
                        .into(image)
                    title.text = imageSource[2].title
                    description.text = imageSource[2].description
                    nextButton.text = "Mulai Sekarang"
                }
                3 -> {
                    navigateToRegisterActivity()
                }
            }
        }

    }

    private fun navigateToRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.book.warandpeace.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.book.warandpeace.R
import com.book.warandpeace.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<WarAndPeaceViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initObserver()
        getFile()
    }

    private fun getFile() {
        binding.submitButton.setOnClickListener {
            val fileName = binding.textInput.editText?.text.toString()
            if (fileName.isNotBlank()) {
                if (viewModel.isTextFile(fileName)) {
                    binding.textInput.editText?.error = null
                    if (isNetworkConnected(this)) {
                        viewModel.getFile(fileName)
                    } else {
                        viewModel.getMostFrequentWordFromLocalFile(fileName)
                    }
                } else {
                    binding.textInput.editText?.error = "Please enter a valid file name"
                }
            } else {
                binding.textInput.editText?.error = "Please enter a file name"
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        resetUI()
                        showProgressBar()
                        binding.submitButton.setStateDisabled()
                    }

                    is UIState.Success -> {
                        hideProgressBar()
                        displayResults()
                        binding.submitButton.setStateEnabled()
                    }

                    is UIState.Error -> {
                        hideProgressBar()
                        showToast(state.errorMessage)
                        binding.submitButton.setStateEnabled()
                    }

                    UIState.Init -> {
                        hideProgressBar()
                        binding.submitButton.setStateEnabled()
                    }
                }
            }
        }
    }

    private fun displayResults() {
        binding.textArea.text = getString(
            R.string.most_frequent_word,
            viewModel.mostFrequentWord()?.key,
            viewModel.mostFrequentWord()?.value.toString()
        )
        binding.textArea2.text = getString(
            R.string.most__frequent_seven_character_word,
            viewModel.mostFrequentSevenCharacterWord()?.key,
            viewModel.mostFrequentSevenCharacterWord()?.value.toString()
        )

        lifecycleScope.launch {
            val result = viewModel.highestScoringWord()
            binding.textArea3.text = getString(
                R.string.highest_scoring_word,
                result.first,
                result.second.toString()
            )
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visible()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    private fun resetUI() {
        binding.textArea.text = ""
        binding.textArea2.text = ""
        binding.textArea3.text = ""
    }

}
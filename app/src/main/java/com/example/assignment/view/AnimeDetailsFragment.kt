package com.example.assignment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.assignment.databinding.FragmentAnimeDetailsBinding
import com.example.assignment.model.data.AnimeDetailData
import com.example.assignment.model.data.CharacterData
import com.example.assignment.model.db.AnimeDatabase
import com.example.assignment.model.db.AnimeEntity
import com.example.assignment.model.db.CharacterEntity
import com.example.assignment.model.retrofit.RetrofitHelper
import com.example.assignment.repository.AnimeRepository
import com.example.assignment.util.loadImage
import com.example.assignment.viewmodel.AnimeDetailViewModel
import com.example.assignment.viewmodel.AnimeViewModelFactory

class AnimeDetailsFragment : Fragment() {

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnimeDetailViewModel by viewModels {
        val database = AnimeDatabase.getDatabase(requireContext())
        val repository = AnimeRepository(
            RetrofitHelper.apiService,
            database.animeDao(),
            database.characterDao()
        )
        AnimeViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animeId = arguments?.getInt("EXTRA_ANIME_ID") ?: -1

        if (animeId != -1) {
            viewModel.fetchDetails(animeId) // Triggers background sync
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        // 1. Observe Anime Details (Instant load from Room)
        viewModel.animeDetail.observe(viewLifecycleOwner) { anime ->
            anime?.let { displayAnimeData(it) }
        }

        // 2. Observe Character List (Instant load from Room)
        viewModel.characters.observe(viewLifecycleOwner) { cast ->
            displayCastData(cast)
        }

        // 3. Observe Loading State (Network progress)
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // 4. Observe Error (Only for network failures)
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun displayAnimeData(anime: AnimeEntity) {
        binding.title.text = anime.title
        binding.detailSynopsis.text = anime.synopsis ?: "No synopsis available."
        binding.detailScore.text = "Rating: ${anime.score ?: "N/A"}"
        binding.episodeNo.text = "Episodes: ${anime.episodes ?: "N/A"}"
        binding.genres.text = anime.genres ?: "No genres"

        if (!anime.youtubeId.isNullOrEmpty()) {
            binding.trailerVideoView.visibility = View.VISIBLE
            binding.detailPoster.visibility = View.GONE
            loadYoutubeVideo(anime.youtubeId)
        } else {
            binding.trailerVideoView.visibility = View.GONE
            binding.detailPoster.visibility = View.VISIBLE
            loadImage(binding.detailPoster, anime.imageUrl)
        }
    }

    private fun displayCastData(cast: List<CharacterEntity>) {
        // Updated to use the Entity list
        binding.mainCast.text = "Main Cast: ${cast.joinToString(", ") { it.name }}"
    }

    private fun loadYoutubeVideo(videoId: String) {
        binding.trailerVideoView.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            val html = """
            <html>
                <body style="margin:0;padding:0;">
                    <iframe width="100%" height="100%" 
                        src="https://www.youtube.com/embed/$videoId" 
                        frameborder="0" allowfullscreen>
                    </iframe>
                </body>
            </html>
            """.trimIndent()
            loadData(html, "text/html", "utf-8")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
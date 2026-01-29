package com.example.assignment.view

import com.example.assignment.model.AnimeListAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.R
import com.example.assignment.databinding.FragmentAnimeListBinding
import com.example.assignment.model.db.AnimeDatabase
import com.example.assignment.model.db.AnimeEntity
import com.example.assignment.model.retrofit.RetrofitHelper
import com.example.assignment.repository.AnimeRepository
import com.example.assignment.viewmodel.AnimeViewModel
import com.example.assignment.viewmodel.AnimeViewModelFactory

class AnimeListFragment : Fragment() {

    private lateinit var viewModel: AnimeViewModel
    private var _binding: FragmentAnimeListBinding? = null
    private val binding get() = _binding!!
    private val animeAdapter = AnimeListAdapter{ anime ->
        openDetailFragment(anime)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = animeAdapter
    }

    private fun setupViewModel() {
        // Get Room DAO and Retrofit Service
        val apiService = RetrofitHelper.apiService
        val database = AnimeDatabase.getDatabase(requireContext())
        val animeDao = database.animeDao()
        val characterDao = database.characterDao()

        val repository = AnimeRepository(apiService, animeDao,characterDao)
        val factory = AnimeViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[AnimeViewModel::class.java]
    }

    private fun observeViewModel() {
        // Observe the Anime List (Instant offline load + network sync)
        viewModel.animeList.observe(viewLifecycleOwner) { list ->
            animeAdapter.submitList(list)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openDetailFragment(anime: AnimeEntity) {
        val detailFragment = AnimeDetailsFragment()

        val bundle = Bundle()
        bundle.putInt("EXTRA_ANIME_ID", anime.id)
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.slide_out_right
            )
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
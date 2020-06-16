package com.denisbeck.instagrammainpage.screens.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.denisbeck.instagrammainpage.R
import com.denisbeck.instagrammainpage.models.Posts
import com.denisbeck.instagrammainpage.models.Stories
import com.denisbeck.instagrammainpage.networking.Status
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }

    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupAdapter<GroupieViewHolder>().apply {
            add(EmptyItem())
            add(FooterItem())
        }
        recycler_view.adapter = adapter

        viewModel.ad.observe(viewLifecycleOwner, Observer {
        })

        viewModel.stories.observe(viewLifecycleOwner, Observer {
            it.data?.let { stories ->
                updateStories(stories)
            }

        })

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            it.data?.let { movies ->
                updatePosts(movies)
            }
        })

        recycler_view.addOnScrollListener(object : RecyclerViewPaginator(recycler_view) {

            override val isLoading: Boolean
                get() = viewModel.posts.value?.status == Status.LOADING

            override fun loadMore() {
                Log.d(TAG, "loadMore: called")
                viewModel.updatePages()
            }
        })
    }

    private fun updateStories(stories: Stories) {
        Log.d(TAG, "updateStories: called")
        adapter.add(1, StoriesItem(stories))
    }

    private fun updatePosts(_posts: Posts) {
        Log.d(TAG, "updatePosts: called")
        val posts = Section(_posts.results.map { PostItem(text = it.title) })
        adapter.add(adapter.groupCount - 1, posts)
    }


}
package com.example.froumapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.froumapp.data.repository.AuthRepository
import com.example.froumapp.data.repository.BaseRepository
import com.example.froumapp.data.repository.CategoryRepository
import com.example.froumapp.data.repository.ForumRepository
import com.example.froumapp.data.repository.HomeRepository
import com.example.froumapp.data.repository.NotificationsRepository
import com.example.froumapp.data.repository.PostRepository
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.data.repository.ThreadForumRepository
import com.example.froumapp.data.repository.ThreadRepository
import com.example.froumapp.ui.auth.AuthViewModel
import com.example.froumapp.ui.forum.categories.CategoriesViewModel
import com.example.froumapp.ui.forum.categories.forums.ForumViewModel
import com.example.froumapp.ui.forum.categories.forums.threads.ForumThreadViewModel
import com.example.froumapp.ui.forum.home.HomeViewModel
import com.example.froumapp.ui.forum.notifications.NotificationsViewModel
import com.example.froumapp.ui.forum.profile.ProfileViewModel
import com.example.froumapp.ui.forum.thread.ThreadViewModel
import com.example.froumapp.ui.forum.thread.post.PostViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository as ProfileRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as HomeRepository) as T
            modelClass.isAssignableFrom(ThreadViewModel::class.java) -> ThreadViewModel(repository = repository as ThreadRepository, postRepository = repository as PostRepository) as T
            modelClass.isAssignableFrom(PostViewModel::class.java) -> PostViewModel(repository as PostRepository) as T
            modelClass.isAssignableFrom(CategoriesViewModel::class.java) -> CategoriesViewModel(repository as CategoryRepository) as T
            modelClass.isAssignableFrom(ForumViewModel::class.java) -> ForumViewModel(repository as ForumRepository) as T
            modelClass.isAssignableFrom(ForumThreadViewModel::class.java) -> ForumThreadViewModel(repository as ThreadForumRepository) as T
            modelClass.isAssignableFrom(NotificationsViewModel::class.java) -> NotificationsViewModel(repository as NotificationsRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found.")
        }
    }

}
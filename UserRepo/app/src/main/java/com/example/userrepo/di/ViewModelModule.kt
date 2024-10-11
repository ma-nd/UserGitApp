package com.example.userrepo.di

import androidx.lifecycle.SavedStateHandle
import com.example.userrepo.ui.login.LoginViewModel
import com.example.userrepo.ui.userDetails.UserDetailsViewModel
import com.example.userrepo.ui.userList.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { UserListViewModel(get(), get()) }
    viewModel { (handle: SavedStateHandle) -> UserDetailsViewModel(handle, get()) }
    viewModel { LoginViewModel(get()) }
}
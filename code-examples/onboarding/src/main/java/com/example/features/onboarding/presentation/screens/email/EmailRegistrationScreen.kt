package com.example.features.onboarding.presentation.screens.email

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.features.onboarding.presentation.component.EmailRegistrationComponent
import com.example.resources.R
import com.example.resources.common.BaseScreen
import com.example.resources.common.NavigationGraphTree
import kotlinx.coroutines.flow.collectLatest
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController

@Immutable
data class EmailRegistrationScreen(
    val isEditMode: Boolean
) : BaseScreen() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalRootController.current
        val viewModel = viewModel<EmailRegistrationViewModel>()
        val state by viewModel.state.collectAsState()

        when (state) {
            is State.Content -> {
                val contentState = state as State.Content

                EmailRegistrationComponent(
                    isLoading = contentState.isLoading,
                    isEditMode = contentState.isEditMode,
                    email = contentState.email,
                    onEmailChange = {
                        viewModel.obtainEvent(
                            Event.OnEmailChanged(value = it)
                        )
                    },
                    emailError = contentState.emailError,
                    onContinueClick = {
                        viewModel.obtainEvent(Event.OnContinueClick)
                    },
                    onTermsClick = {
                        viewModel.obtainEvent(Event.OnTermsClick)
                    },
                    onPolicyClick = {
                        viewModel.obtainEvent(Event.OnPolicyClick)
                    },
                    onBack = {
                        viewModel.obtainEvent(Event.OnBack)
                    }
                )
            }
        }

        LaunchedEffect(key1 = Unit) {
            with(viewModel) {
                viewModel.obtainEvent(Event.Init)
                action.collectLatest { action ->
                    when (action) {
                        is Action.Init -> {}

                        is Action.OnNetworkError -> {
                            onShowLostNetworkConnection(navigator) {}
                        }

                        is Action.OnError -> {
                            showToast(context, R.string.error_updating_email)
                        }

                        is Action.LinkClick -> {
                            openOnWeb(
                                url = action.link,
                                context = context
                            )
                        }

                        Action.OnBack -> {
                            navigateBack(navigator)
                        }

                        Action.OnContinue -> {
                            navigator.push(NavigationGraphTree.CONNECT_SENSOR.name)
                        }
                    }
                }
            }
        }

        LaunchedEffect(key1 = isEditMode) {
            viewModel.obtainEvent(
                Event.SetEditMode(isEditMode = isEditMode)
            )
        }
    }
}

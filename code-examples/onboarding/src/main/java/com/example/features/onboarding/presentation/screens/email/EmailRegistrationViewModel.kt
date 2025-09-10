package com.example.features.onboarding.presentation.screens.email

import androidx.lifecycle.viewModelScope
import com.example.core.common.GlobalConstants
import com.example.core.contracts.activity.CredentialContract
import com.example.core.contracts.activity.CredentialResult
import com.example.core.contracts.dispatchers.CoroutineDispatcher
import com.example.core.contracts.usecases.IAuthUseCase
import com.example.core.contracts.usecases.IUserUseCase
import com.example.core.contracts.validators.ISignUpFieldValidator
import com.example.core.preferences.AppPrefManager
import com.example.core.presentation.BaseViewModel
import com.example.core.presentation.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class EmailRegistrationViewModel @Inject constructor(
    private val fieldValidator: ISignUpFieldValidator,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val appPrefManager: AppPrefManager,
    private val authUseCase: IAuthUseCase,
    private val userUseCase: IUserUseCase,
    private val credentialContract: CredentialContract
) : BaseViewModel<State, Action>(), EventHandler<Event> {

    override val initialState: State
        get() = State.Content()

    private fun getContentState(): State.Content {
        return state.value as State.Content
    }

    private fun updateState(newState: State.Content) {
        setState(newState)
    }

    override fun obtainEvent(event: Event) {
        when (event) {
            is Event.Init -> reduceInit()
            Event.OnBack -> reduceOnBack()
            Event.OnTermsClick -> reduceTermsClick()
            Event.OnPolicyClick -> reducePolicyClick()
            is Event.OnEmailChanged -> reduceEmailChange(event.value)
            Event.OnContinueClick -> reduceContinueClick()
            is Event.SetEditMode -> reduceSetEditMode(event.isEditMode)
            is Event.HandleResult -> handleResult(event.result)
        }
    }

    init {
        reduceInit()
    }

    private fun reduceInit() {
        setAction(Action.Init)
        updateState(State.Content())
    }

    private fun reduceSetEditMode(isEditMode: Boolean) {
        viewModelScope.launch {
        updateState(
            State.Content(
                isEditMode = isEditMode,
                email = appPrefManager.email
            )
        )
        }
    }

    private fun reduceOnBack() {
        setAction(Action.OnBack)
    }

    private fun reduceTermsClick() {
        setAction(
            Action.LinkClick(link = GlobalConstants.AppLink.termsLink)
        )
    }

    private fun reducePolicyClick() {
        setAction(
            Action.LinkClick(link = GlobalConstants.AppLink.privacyLink)
        )
    }

    private fun reduceEmailChange(value: String) {
        viewModelScope.launch {
        updateState(
            getContentState().copy(
                email = value,
                emailError = fieldValidator.emailIsValid(value)
            )
        )
        }
    }

    private fun reduceContinueClick() {
        if (getContentState().hasError().not()) {
            viewModelScope.launch {
        updateState(
            getContentState().copy(isLoading = true)
        )
                appPrefManager.email = getContentState().email

                if (getContentState().isEditMode) {
                    val request = withContext(coroutineDispatcher.io) {
                        userUseCase.updateUser(email = getContentState().email)
                    }

                    if (request.hasError().not()) {
                        setAction(Action.OnBack)
                    } else {
                        setAction(Action.OnError())
                    }
                } else {
                    val request = withContext(coroutineDispatcher.io) {
                        authUseCase.startRegistration(email = getContentState().email)
                    }

                    val result = request.value

                    if (request.success == true && result != null) {
                        credentialContract.invoke(
                            credentialResult = result,
                            resultCallback = this@EmailRegistrationViewModel::handleResult
                        )
                    } else {
                        setAction(
                            Action.OnError(message = request.error?.description)
                        )
                    }
                }
            }
        }
    }

    private fun handleResult(credentialResult: CredentialResult) {
        viewModelScope.launch {
            when (credentialResult) {
                is CredentialResult.Success -> {
                    val request = withContext(coroutineDispatcher.io) {
                        authUseCase.finishRegistration(responseJson = credentialResult.responseJson)
                    }

                    if (request.hasError()) {
                        setAction(
                            Action.OnError(message = request.error?.description)
                        )
                    } else {
                        setAction(Action.OnContinue)
                    }
                    hideLoader()
                }

                is CredentialResult.Error -> {
                    setAction(
                        Action.OnError(message = credentialResult.e.message)
                    )
                    hideLoader()
                }
            }
        }
    }

    private fun hideLoader() {
        updateState(
            getContentState().copy(isLoading = false)
        )
    }
}

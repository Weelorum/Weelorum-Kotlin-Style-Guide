package com.example.features.onboarding.presentation.screens.email

import com.example.core.common.FormatUtils.emptyString
import com.example.core.contracts.activity.CredentialResult

internal sealed class State {
    data class Content(
        val isLoading: Boolean = false,
        val isEditMode: Boolean = false,
        val email: String = emptyString,
        val emailError: Int? = null
    ) : State() {
        fun hasError(): Boolean {
            return emailError != null
        }
    }
}

internal sealed class Event {
    data object Init : Event()

    data class SetEditMode(
        val isEditMode: Boolean
    ) : Event()

    data class OnEmailChanged(
        val value: String
    ) : Event()

    data object OnContinueClick : Event()
    data object OnTermsClick : Event()
    data object OnPolicyClick : Event()
    data class HandleResult(
        val result: CredentialResult
    ) : Event()

    data object OnBack : Event()
}

internal sealed class Action {
    data object Init : Action()
    data object OnNetworkError : Action()
    data class OnError(val message: String? = null) : Action()
    data class LinkClick(
        val link: String
    ) : Action()

    data object OnContinue : Action()

    data object OnBack : Action()
}

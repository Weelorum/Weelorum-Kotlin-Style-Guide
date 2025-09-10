package com.example.features.onboarding.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.common.FormatUtils.emptyString
import com.example.resources.R
import com.example.resources.common.Dimens.GRID_12
import com.example.resources.common.Dimens.GRID_16
import com.example.resources.common.Dimens.GRID_20
import com.example.resources.common.Dimens.GRID_28
import com.example.resources.common.Dimens.GRID_4
import com.example.resources.common.Dimens.GRID_6
import com.example.resources.common.Dimens.GRID_8
import com.example.resources.extensions.setBarsColor
import com.example.resources.themes.ExampleTheme
import com.example.resources.themes.AppTheme
import com.example.resources.widgets.AppBars.AppBarCenteredTitle
import com.example.resources.widgets.Buttons.DefaultButton
import com.example.resources.widgets.Cards.TermsWidget
import com.example.resources.widgets.Spacers.SpacerVerticalView
import com.example.resources.widgets.Spacers.SpacerWeightView
import com.example.resources.widgets.TextFields.EditTextWidget
import com.example.resources.widgets.TextWidgets.ScreenTitle

@Composable
internal fun EmailRegistrationComponent(
    isLoading: Boolean,
    isEditMode: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: Int?,
    onContinueClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPolicyClick: () -> Unit,
    onBack: () -> Unit
) {
    setBarsColor(color = ExampleTheme.colors.primaryBackground)

    val toolbarTitle = if (isEditMode) {
        stringResource(id = R.string.title_edit_email)
    } else {
        null
    }

    val title = buildAnnotatedString {
        withStyle(ExampleTheme.typography.screenBodyTitle.toSpanStyle()) {
            append(
                stringResource(id = R.string.registration_screen_title)
            )
        }
    }

    val placeholder = if (isEditMode) {
        stringResource(id = R.string.email_placeholder)
    } else {
        stringResource(id = R.string.label_email)
    }

    val buttonTitle = if (isEditMode) {
        stringResource(id = R.string.action_save_changes)
    } else {
        stringResource(id = R.string.action_continue_with_email)
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppBarCenteredTitle(
                title = toolbarTitle,
                onBack = onBack
            )
        },
        containerColor = ExampleTheme.colors.primaryBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = GRID_16,
                    end = GRID_16,
                    top = it.calculateTopPadding() + GRID_16,
                    bottom = it.calculateBottomPadding()
                )
        ) {
            SpacerVerticalView(GRID_12)

            if (isEditMode.not()) {
                ScreenTitle(
                    title = title,
                    subTitle = stringResource(id = R.string.registration_screen_subtitle)
                )

                SpacerVerticalView(GRID_20)
            }

            SpacerVerticalView(GRID_8)

            EditTextWidget(
                label = placeholder,
                value = email,
                onValueChange = onEmailChange,
                error = emailError,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null,
                        tint = ExampleTheme.colors.borderFocused
                    )
                }
            )

            SpacerVerticalView(GRID_28)

            if (isEditMode) {
                SpacerWeightView()
            }

            DefaultButton(
                title = buttonTitle,
                onClick = onContinueClick,
                enabled = emailError == null && email.isNotEmpty(),
                isLoading = isLoading
            )

            if (isEditMode.not()) {
                SpacerWeightView()

                Box(
                    modifier = Modifier.padding(horizontal = GRID_4)
                ) {
                    TermsWidget(
                        onTermsClick = onTermsClick,
                        onPrivacyPolicyClick = onPolicyClick
                    )
                }

                SpacerVerticalView(GRID_6)
            } else {
                SpacerVerticalView(GRID_16)
            }
        }
    }
}

@Preview
@Composable
private fun EmailRegistrationComponent_Preview() {
    AppTheme {
        EmailRegistrationComponent(
            isLoading = false,
            isEditMode = true,
            email = emptyString,
            onEmailChange = {},
            emailError = null,
            onContinueClick = {},
            onTermsClick = {},
            onPolicyClick = {},
            onBack = {}
        )
    }
}

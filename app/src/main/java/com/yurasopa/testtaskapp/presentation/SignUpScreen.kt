package com.yurasopa.testtaskapp.presentation

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yurasopa.testtaskapp.R
import com.yurasopa.testtaskapp.utils.Resource
import com.yurasopa.testtaskapp.utils.Typography
import com.yurasopa.testtaskapp.utils.toFile
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SignUpScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }


    var selectedPosition by remember { mutableIntStateOf(0) }
    var photoFile by remember { mutableStateOf<File?>(null) }

    val chooserBottomSheetState = remember { mutableStateOf(false) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registrationResult by viewModel.registrationResult.collectAsState()

    var showSuccessScreen by remember { mutableStateOf(false) }

    val nameError by viewModel.nameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val photoError by viewModel.photoError.collectAsState()

    val cameraImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let {
                photoFile = it.toFile(navController.context)
            }
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            photoFile = it.toFile(navController.context)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraImageLauncher.launch(
                viewModel.createImageUri(navController.context, cameraImageUri)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Your name") },
            isError = nameError != null,
            supportingText = {
                nameError?.let { Text(text = it) }
            }
        )
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            isError = emailError != null,
            label = { Text(text = "Email") },
            supportingText = {
                emailError?.let { Text(text = it) }
            }
        )
        CustomTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(text = "Phone") },
            isError = phoneError != null,
            supportingText = {
                if (phoneError != null) {
                    phoneError?.let { Text(text = it) }
                } else {
                    Text(
                        text = "+38 (XXX) XXX - XX - XX",
                        style = Typography.body3
                    )
                }
            }
        )

        PositionSelectionRadioGroup(
            selectedPosition = selectedPosition,
            onPositionSelected = { idRole ->
                selectedPosition = idRole
            }
        )

        OutlinedTextField(
            value = photoFile?.name ?: "",
            onValueChange = {},
            isError = photoError != null,
            placeholder = { Text("Upload your photo") },
            supportingText = {
                photoError?.let { Text(text = it) }
            },
            suffix = {
                Text(
                    text = "Upload",
                    color = Color(0xFF00BDD3),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            chooserBottomSheetState.value = true
                        })
            },
            readOnly = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                errorTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF00BDD3),
                errorBorderColor = Color.Red,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFF00BDD3),
                errorLabelColor = Color.Red,
                unfocusedSupportingTextColor = Color.Gray,
                focusedSupportingTextColor = Color.Gray,
                errorSupportingTextColor = Color.Red,
                cursorColor = Color.Black,
                errorCursorColor = Color.Red,
                errorPlaceholderColor = Color.Red
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        CustomizedGeneralButton(title = "Sign up") {
            photoFile?.let {
                viewModel.addUser(name, email, phone, selectedPosition + 1, it)
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }


        if (chooserBottomSheetState.value) {
            ContentSelectionBottomSheet(
                onCameraSelected = {
                    chooserBottomSheetState.value = false
                    if (navController.context.checkSelfPermission(Manifest.permission.CAMERA)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraImageLauncher.launch(
                            viewModel.createImageUri(navController.context, cameraImageUri)
                        )
                    } else {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onGallerySelected = {
                    chooserBottomSheetState.value = false
                    imageLauncher.launch("image/*")
                },
                onDismiss = {
                    chooserBottomSheetState.value = false
                })

        }
    }


    registrationResult?.let { result ->
        when (result) {
            is Resource.Error -> {

            }

            is Resource.Loading -> TODO()
            is Resource.Success -> {
                Toast.makeText(
                    navController.context,
                    "Registration successful!",
                    Toast.LENGTH_SHORT
                ).show()
                showSuccessScreen = true
            }
        }

    }
    if (showSuccessScreen) {
        SuccessScreen(modifier = Modifier.fillMaxSize()) {
            navController.popBackStack()
            showSuccessScreen = false
        }
        viewModel.clearRegistrationResult()
    }
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = label,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            errorTextColor = Color.Black,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            errorContainerColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            focusedBorderColor = Color(0xFF00BDD3),
            errorBorderColor = Color.Red,
            unfocusedLabelColor = Color.Gray,
            focusedLabelColor = Color(0xFF00BDD3),
            errorLabelColor = Color.Red,
            unfocusedSupportingTextColor = Color.Gray,
            focusedSupportingTextColor = Color.Gray,
            errorSupportingTextColor = Color.Red,
            cursorColor = Color.Black,
            errorCursorColor = Color.Red,
            errorPlaceholderColor = Color.Red
        ),
        shape = RoundedCornerShape(4.dp),
        singleLine = true,
        supportingText = supportingText,
        suffix = suffix,
        isError = isError
    )
}

@Composable
fun PositionSelectionRadioGroup(
    selectedPosition: Int,
    onPositionSelected: (Int) -> Unit
) {
    val roles = listOf("Frontend developer", "Backend developer", "Designer", "QA")

    Column {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Select your position",
            style = Typography.heading1
        )
        roles.forEachIndexed { index, role ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPositionSelected(index) }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedPosition == index,
                    onClick = { onPositionSelected(index) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = role, style = Typography.body2)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentSelectionBottomSheet(
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose how you want to add a photo",
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Gray
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 80.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }
                            onCameraSelected()
                        }) {
                    Image(
                        painter = painterResource(id = R.drawable.camera_icon),
                        contentDescription = "Camera"
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "Camera", color = Color.Black)
                }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }
                            onGallerySelected()
                        }) {
                    Image(
                        painter = painterResource(id = R.drawable.gallery_icon),
                        contentDescription = "Gallery"
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "Gallery", color = Color.Black)
                }
            }
        }
    }
}
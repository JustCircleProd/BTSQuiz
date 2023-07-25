package com.justcircleprod.btsquiz.ui.settings.developersAndLicenses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.ActivityDevelopersAndLicensesBinding

class DevelopersAndLicensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDevelopersAndLicensesBinding

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDevelopersAndLicensesBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        SectionTitle(text = stringResource(id = R.string.copyright_disclaimer))
                    }
                    item {
                        Text(
                            text = stringResource(id = R.string.copyright_disclaimer_text),
                            color = colorResource(id = R.color.text_color),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        Line()
                    }

                    item {
                        SectionTitle(text = stringResource(id = R.string.developers))
                    }
                    item {
                        CreatorsImage()
                    }
                    item {
                        Line()
                    }

                    item {
                        SectionTitle(text = stringResource(id = R.string.licenses))
                    }

                    licenses.forEach { license ->
                        item {
                            LicenseName(
                                licenseName = license["licenseName"] as String,
                                licenseLink = license["licenseLink"] as String
                            )
                        }

                        for (packageInfo in (license["packages"] as List<Map<String, String>>)) {
                            item {
                                PackageInfoElement(
                                    packageName = packageInfo["projectNameVersion"]!!,
                                    licenseInfo = packageInfo["projectInfo"]!!
                                )
                            }
                        }
                    }

                    item {
                        Line()
                    }
                }
            }
        }

        setContentView(binding.root)
    }

    @Composable
    private fun SectionTitle(text: String) {
        Text(
            text = text,
            color = colorResource(id = R.color.text_color),
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 5.dp)
        )
    }

    @Composable
    fun CreatorsImage() {
        Image(
            painter = painterResource(id = R.drawable.developers_image),
            contentDescription = stringResource(id = R.string.developers_content_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
        )
    }

    @Composable
    fun Line() {
        Divider(
            color = colorResource(id = R.color.line_color),
            thickness = dimensionResource(id = R.dimen.line_height),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(bottom = dimensionResource(id = R.dimen.line_bottom_margin))
        )
    }

    @Composable
    private fun LicenseName(licenseName: String, licenseLink: String) {
        val uriHandler = LocalUriHandler.current

        Text(
            text = licenseName,
            fontSize = 18.sp,
            color = colorResource(id = R.color.license_title_color),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .clickable(
                    onClick = {
                        uriHandler.openUri(licenseLink)
                    }
                )
        )
    }

    @Composable
    private fun PackageInfoElement(
        packageName: String,
        licenseInfo: String,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(bottom = 6.dp)
        ) {
            Text(
                text = packageName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = licenseInfo,
                color = colorResource(id = R.color.text_color),
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
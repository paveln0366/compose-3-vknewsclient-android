import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.vkid.manifest.placeholders) apply true
}

val localProps = Properties()
file("local.properties").inputStream().use { localProps.load(it) }

val clientId: String = localProps.getProperty("VKIDClientID")
    ?: error("VKIDClientID is not set in local.properties")
val clientSecret: String = localProps.getProperty("VKIDClientSecret")
    ?: error("VKIDClientSecret is not set in local.properties")

vkidManifestPlaceholders {
    init(
        clientId = clientId,
        clientSecret = clientSecret,
    )
    vkidRedirectHost = "vk.ru"
    vkidRedirectScheme = "vk$clientId"
    vkidClientId = clientId
    vkidClientSecret = clientSecret
}
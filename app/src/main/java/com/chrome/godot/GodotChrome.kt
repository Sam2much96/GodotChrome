package com.chrome.godot

//import  android.util.Log
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
// Documentation 1 : https://developer.android.com/reference/android/webkit/WebView

import android.app.Activity
//import android.view.TextureView
import android.webkit.ConsoleMessage
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.collection.ArraySet
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

import java.util.HashMap

// Class Constants are mapped to Godot's GDScript Error Enumerations

class GodotChrome(godot : Godot) : GodotPlugin(godot) {
    private var myWebView : WebView? = null
    private var chromeDevTools : Dictionary = Dictionary() // for debugging the Web view class

    //You're using a companion object in Kotlin to define constants
    //or functions that belong to the class itself rather than its instances.
    companion object {
        private const val GodotChrome : String = "GodotChrome"
    }

    @NonNull
    override fun getPluginName(): String {
        return GodotChrome
    }


    // Example of a custom function that can be called from GDScript
    @UsedByGodot
    fun myCustomFunction(param: String): String {
        return "Hello from Kotlin: $param"
    }

    // Return the debug log for this Webview Kit session stored in a dict
    @UsedByGodot
    fun consoleLog(): Dictionary {
        return chromeDevTools
    }

    // Custom WebChromeClient to capture console messages
    // Needed for Java script alerts and console logging
    // WebChromeClient (Handles Browser Features)
    private inner class CustomWebChromeClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if (consoleMessage != null) {
                val logKey = "${consoleMessage.sourceId()}:${consoleMessage.lineNumber()}"
                val logValue = "[${consoleMessage.messageLevel()}] ${consoleMessage.message()}"

                chromeDevTools.put(logKey, logValue) // Store log messages
            }
            return true
        }
        // log loading page progress
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            chromeDevTools.put("Progress", "$newProgress%")
        }
    }


    // This function will dynamically add a WebView on top of the Godot view
    // Returns an Integer so can be checked with var err == OK
    @UsedByGodot
    fun helloWorld(
        url: String,
        headers: Dictionary = Dictionary(), // Empty Header file
        enableJavaScript: Boolean = false, // default is false
        order : Int = -99, // for setting webview layering
        debugger : Boolean = true // for storing Console.log messages
    ): Int {
        //Get the Godot Context
        val context = activity?.applicationContext ?: return 1  // Avoids null issues //= godot.context

        // Get the Godot activity
        val activity: Activity? = godot.activity



        // Run WebView Init On Main UI Thread
        activity?.runOnUiThread {


                if (myWebView == null) { //avoid recreating webview instances
                    myWebView = WebView(context)
                    myWebView!!.webViewClient = WebViewClient()
                    //myWebView!!.settings.javaScriptEnabled = enableJavaScript // false by default

                    // webview settings for speed, security and JS heavy apps
                    // javascript
                    myWebView!!.settings.javaScriptEnabled = enableJavaScript

                    // storing and cache
                    myWebView!!.settings.domStorageEnabled = true
                    myWebView!!.settings.databaseEnabled = true
                    myWebView!!.settings.cacheMode = WebSettings.LOAD_DEFAULT
                    myWebView!!.settings.loadsImagesAutomatically = true
                    myWebView!!.settings.useWideViewPort = true

                    // safe browsing and privacy
                    myWebView!!.settings.allowFileAccess = false
                    myWebView!!.settings.allowContentAccess = true

                    // performance
                    myWebView!!.settings.loadWithOverviewMode = true
                    myWebView!!.settings.builtInZoomControls = true
                    myWebView!!.settings.displayZoomControls = false
                    myWebView!!.settings.userAgentString = "MyCustomWebView/1.0"

                    // Auto play videos
                    myWebView!!.settings.mediaPlaybackRequiresUserGesture = false // Auto-play videos


                }




            // handle headers
                if (!headers.isEmpty()) {
                    // Do something if headers contain keys and values
                    myWebView!!.loadUrl(url, convertDictionaryToMap(headers))
                }

                //load without headers
                if (headers.isEmpty()) {
                    // Do something if headers contain keys and values
                    myWebView!!.loadUrl(url)
                }

                // Handle Console Log Caprture for debugging
                if (debugger){
                    myWebView!!.webChromeClient = CustomWebChromeClient() // Capture Console logs
                }


                // Get the root layout of the Godot activity
                val rootView = activity.findViewById<FrameLayout>(android.R.id.content)

                if (order == -99){
                    //Webview Layout
                    // Create default fullscreen layout parameters for the WebView
                    val layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    // default layer above all UI elements
                    // Add the WebView to the root view of the activity
                    rootView.addView(myWebView, layoutParams)
                }

                if (order != -99){
                    // Get the total number of views in FrameLayout
                    val totalViews = rootView.childCount

                    chromeDevTools.put("totalViews", totalViews) // store webview view count

                    // Ensure the index is within a valid range
                    val safeIndex = Math.max(0, Math.min(order, totalViews))

                    // Add the WebView to the root view of the activity
                    rootView.addView(myWebView, safeIndex)

                }


                emitSignal("connected") // emit connected signal
                chromeDevTools.put("status", 0) // store webview client state
        }
        return 0
    }


    //override fun onMainLoop() {
        // You can override this to hook into Godot's main loop if necessary
    //}
    // Used To Remove The WebView From The ViewPort
    @UsedByGodot
    fun goodByeWorld(): Int{
        val activity: Activity? = godot.activity
        val rootView = activity?.findViewById<FrameLayout>(android.R.id.content)
        rootView?.removeView(myWebView)
        myWebView?.destroy() // properly destroy webview object
        myWebView = null
        emitSignal("disconnected") // emit disconnected signal
        chromeDevTools.put("status", 1) //disconnected
        return 0
    }

    override fun getPluginSignals(): Set<SignalInfo> {
        // add and register signals
        val signals: MutableSet<SignalInfo> = ArraySet()
        signals.add(SignalInfo("connected"))
        signals.add(SignalInfo("disconnected"))
        signals.add(SignalInfo("Misc"))

        return signals
    }

    //Converts Dictionary to Map<String, String>
    private fun convertDictionaryToMap(dictionary: Dictionary): MutableMap<String, String> {
        val map: MutableMap<String, String> = HashMap()

        if (!dictionary.isEmpty()) { // ✅ Replace let with simple if-check
            for (key in dictionary.keys) {
                if (key is String) { // Ensure key is a String
                    val value = dictionary[key]
                    if (value is String) { // Ensure value is a String
                        map.put(key, value) // Using put() instead of direct assignment
                    }
                }
            }
        }
        return map
    }


    @UsedByGodot
    fun getContentHeight(): Int {
        //Gets the height of the HTML content.
        val height : Int = myWebView!!.getContentHeight()
        return height
    }

    @UsedByGodot
    fun getProgress(): Int {
        // Gets the Progress of the currrent page from 1 - 100
        val progress : Int = myWebView!!.getProgress()
        return progress
    }

    @UsedByGodot
    fun getTitle(): String {
        //Gets the title for the current page.
        // This is the title of the current page until WebViewClient.onReceivedTitle is called.
        val titleUnsafe : String? = myWebView!!.getTitle()
        val safeString: String = titleUnsafe ?: "Error Getting Page Status"
        return safeString
    }



}

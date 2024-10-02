package com.chrome.godot

//import  android.util.Log
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
import android.app.Activity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.view.ViewGroup

// Godot Initializer
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.UsedByGodot



class GodotChrome(godot : Godot) : GodotPlugin(godot) {
    private var myWebView : WebView? = null

    override fun getPluginName(): String {
        return "GodotChrome"
    }

    // Optionally register custom functions, signals, properties, etc.
    //override fun getPluginMethods(): MutableSet<String> {
    //    return mutableSetOf("myCustomFunction")
    //}

    // Example of a custom function that can be called from GDScript
    @UsedByGodot
    fun myCustomFunction(param: String): String {
        return "Hello from Kotlin: $param"
    }

    // This function will dynamically add a WebView on top of the Godot view
    // Returns an Integer so can be checked with var err == OK
    @UsedByGodot
    fun helloWorld(param: String): Int {
        //Get the Godot Context
        val _context = godot.context

        // Get the Godot activity
        val activity: Activity? = godot.activity

        //null check converts Context? to Context
        if (_context != null) {
            // Run WebView Init On Main UI Thread
            activity!!.runOnUiThread{
                myWebView = WebView(_context)
                myWebView!!.webViewClient = WebViewClient()
                myWebView!!.settings.javaScriptEnabled = true
                myWebView!!.loadUrl(param)

                // Create layout parameters for the WebView
                val layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // Get the root layout of the Godot activity
                val rootView = activity.findViewById<FrameLayout>(android.R.id.content)

                // Add the WebView to the root view of the activity
                rootView.addView(myWebView, layoutParams)
            }
            return 0
        } else {
            return 1
        }

    }
        //Log.v("Context Debug: ", _context.toString())


        //Log.v("Url Debug: ", param)

    //override fun onMainLoop() {
        // You can override this to hook into Godot's main loop if necessary
    //}
    // Used To Remove The WebView From The ViewPort
    @UsedByGodot
    fun goodByeWorld(): Int{
        val activity: Activity? = godot.activity
        val rootView = activity?.findViewById<FrameLayout>(android.R.id.content)
        rootView?.removeView(myWebView)
        myWebView = null
        return 0
    }

}

**Godot Chrome**

Godot Chrome is a plugin that exposes Android Webkit's API v1 to Godot Engine GDScript  via Android Plugins.
Users can create Embedded Chrome Webbrowser in godot engine

This Plugin Currently Works For The Godot 3.5 Branch and Newer. It Uses V1 Plugin Integration with .gdap files but uses godot's maven repository as a dependency in the projects's build.gradle files rather than the local aar files. 


**📖 Documentation**

**📌 Creating an Embedded Chrome Browser**

(1) Create an embedded Chrome browser in godot via gdscript with `helloWorld(url, headers, enableJavascript, order, debugger)` 

**Parameters:**
  
  `url: String` – The URL to load.

  `headers: Dictionary = Dictionary()` – Custom HTTP headers.

  `enableJavaScript: Boolean = false`– Enables JavaScript (default: `false` for security reasons)

  `order : Int = -99` – WebView layering order (-99 is the default overlay for UI and background renders).

  `debugger : Boolean = true` – Stores `console.log` messages that can be parsed in Godot.

**📌 Other Methods:**

(1) `goodByeWorld()`– Destroys the web view instance.

(2) `getCurrentHeight()` – Returns the height of the loaded HTML page as an integer.

(3) `getTitle()` – Returns the title of the loaded URL as a string.

(4) `getProgress()` – Returns the progress of the current page (1 - 100).

(5) `consoleLog()` – Prints JavaScript `console.log()` entries to Godot as a dictionary. Also debugs the embedded Chrome instance status. 


**📌 Signals:**

(1) `connected` – Emitted when an embedded browser instance is created.

(2) `disconnected` – Emitted when the embedded browser instance is destroyed.



**🚀 How To Use**


(1) **Download the Plugin:** 
    Download the `.gdap` and `.aar` precompiled release files from itchio at https://inhumanity-arts.itch.io/godotchrome

(2) **Place them in your:** 
    `res://project/android/plugin` 

(3) **Enable the Plugin:** 
    In **Export Settings** in Godot Engine, enable **Custom build** and check GodotChrome plugin

(4) **Initialize the Engine in Code:** 
  `var Chrome = Engine.get_singleton("GodotChrome")`

(5) **Call Supported Methods on Your Chrome Instance**
  Example : `Chrome.helloWorld("https://example.com", {}, true, -99, true)`


**🔥 Godot 4.4 Support**

A **Godot 4.4+** version would be made available for $1.99 to support maintenance and updates. This version will continue implementing **Android WebKit API v1 calls.**
The Main Branch Uses v1 plugins for use in Godot 3.5.* branches to be maintained via **CI/CD Devops pipelines** 

**Godot Chrome**
Godot Chrome is a plugin that exposes Android Webkit's API v1 to Godot Engine GDScript  via Android Plugins.
Users can create Embedded Chrome Webbrowser in godot engine

This Plugin Currently Works For The Godot 3.5 Branch and Newer. It Uses V1 Plugin Integration with .gdap files but uses godot's maven repository as a dependency in the projects's build.gradle files rather than the local aar files. 

**Docs**

(1) Create an embedded Chrome browser in godot via gdscript with `helloWorld(url, headers, enableJavascript, order, debugger)` 

  url: String

  headers: Dictionary = Dictionary() to Send A Custom Header file

  enableJavaScript: Boolean = false, where default is false for security

  order : Int = -99, is for setting webview layering order with -99 being a default for overlaying all UI and Background Renders

  debugger : Boolean = true is for storing Console.log messages which can be parsed in godot engine

(2) Destroy a webview instance with `goodByeWorld()`

(3) `getCurrentHeight()` returns the integer value of the height of the loaded html page 

(4) `getTitle()` returns the Title of the loaded url as a string

(5) getProgress() Gets the Progress of the currrent page from 1 - 100

(6) consoleLog() prints out the console.log() Javascript entries to Godot Engine as a dictionary. It also debugs the Chrome Embedded Instance Status & State

(7) connected signal which is emitted once an embedded browser instance is created

(8) disconnected signal which is emitted by the embedded browser instance is destroyed


**How To Use:**

(1) Download the .gdap and .aar release files from https://inhumanity-arts.itch.io/godotchrome

(2) Place them in your `res://project/android/plugin` 

(3) In Export Setting in Godot Engine, Enable Custom build and check GodotChrome plugin

(4) Call the Engine in code via `var Chrome = Engine.get_singleton("GodotChrome")`

(5) Call Supported Methods on your Chrome Embedded browser instance


**Godot 4.4 Support**

A Godot 4.4+ would be made available for $1.99 to support maintenance and long term updates for the plugin which currently only implements version 1 API calls from Android Webkit.
THe Main Branch Uses v1 plugins for use in Godot 3.5.* branches to be maintained via CI/CD Devops pipelines 

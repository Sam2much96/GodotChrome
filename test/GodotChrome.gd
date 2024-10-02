extends Node


# Tests The Plugin Integration

func _ready():
	if Engine.has_singleton("GodotChrome"):
		var singleton = Engine.get_singleton("GodotChrome")
		#var p =singleton.myCustomFunction("Hello World")
		#print_debug(p) #works
		
		var err = singleton.helloWorld("https://www.google.com")
		if err == OK:
			print(err)
		else:
			print(err)

Download: https://github.com/RailRanger/trcloser (Source is available as well)
Main page: http://demthruz.wordpress.com/2014/08/04/tr-closer-close-any-program-based-on-your-public-ip-address/


=== TR Closer ===

Requires: Java
Created in: Java and wrapped with Launch4j
Uses: Jsoup http://jsoup.org/ 1.7.3
Licence: http://jsoup.org/license

TR Closer closes any application(usually your torrent application such as transmission) if your public IPv4 address matches the one you select.
The program also supports wildcards, e.g putting 192.167.50.0 will block all 255 addresses.
Every 30 seconds your wan address is checked with the selected server.

A few notes about the sections above:

* It is normal to see the text: "ERROR: The process "trans*" not found." This simply means that your executable isn't running.
* 0.0.0.0 will close the selected program based on any ip address.
* The program will detect if your internet connection is offline, and will continue to work when your internet connection resumes.
== Installation ==

Run the executable.

== Optional ==

Create a shortcut if you wish and place it in your startup folder.
For windows 7 and below: search for the startup folder and place the shortcut there.
For Windows 8: Press win+r and type shell:startup and place the shortcut there.
== Changelog ==

= 1.0 =
*Initial Release



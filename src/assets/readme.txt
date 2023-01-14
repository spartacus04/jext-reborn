# How to setup the plugin

## Once you have downloaded this file put the discs.json file in the root of plugin directory

```
Server
└───...
└───plugins
    └───JukeboxExtendedReborn
    │   └───discs.json
	│	└───config.json
	│	└───...
	└───Jext.jar
	└───...
```

## As for the resource pack file you can set it up by editing the server config file

Example upload your file to dropbox.
Generate a download url and replace the dl=0 at the end of the url with a dl=1

Example:
original url: https://www.dropbox.com/s/msie7hucnpbkfy/test.PNG?dl=0
edited   url: https://www.dropbox.com/s/msie7hucnpbkfy/test.PNG?dl=1

then in the server.properties file set the server-resource-pack to the edited url

## Setting up integrations

You should have a integrations.txt file in downloaded files

In it you can find necessary configuration for other plugins

# Your plugin is now ready to go, restart the server to start using jext
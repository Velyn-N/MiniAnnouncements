<h1 align="center">MiniAnnouncements</h1>

<p align="center"><strong>Announcements for your Paper Minecraft Server</strong></p>
<p align="center">Versatile, Configurable and absolutely awesome!</p>

<h3 align="center"><a href="https://github.com/Velyn-N/MiniAnnouncements/releases">Download from GitHub</a></h3>

## How it works

After installing the plugin, you can define your Announcements in the `config.yml` File.
Refer to the [MiniMessage Documentation](https://docs.papermc.io/adventure/minimessage/) for instructions on how to format these messages.

You can take a look at default Announcements the Plugin provides.
They showcase some of the features this Plugin provides.

After creating your own Announcement you can use the `/miniannouncements` Command to reload the Configuration
and send Announcements to yourself or others.<br>
Upon reloading, all scheduled Announcements will be logged to the Servers' Console.

## Custom MiniMessage Tags

MiniAnnouncements supports custom MiniMessage Tags that should help with writing dynamic Messages.

| Tag Format                         | Examples                                                                                                       | Explanation                                                                |
|------------------------------------|----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| <player_name>                      |                                                                                                                | Prints the receiving Players Name                                          |
| <player_display_name>              |                                                                                                                | Prints the receiving Players Display Name                                  |
| <player_uuid>                      |                                                                                                                | Prints the receiving Players UUID                                          |
| <date_time:format:fixed-date-time> | <date_time:"def"><br><date_time:"dd.MM.yyyy HH:mm:ss"><br><date_time:"MM.dd.yyyy HH:mm":"1999-12-31 23:59:59"> | Printing Date-Time Values. Can specify Format and specific points in time. |
| \<date:format:fixed-date>          | \<date:"def"><br>\<date:"dd.MM.yyyy"><br>\<date:"MM.dd.yyyy":"1999-12-31">                                     | Printing Date Values. Can specify Format and specific points in time.      |

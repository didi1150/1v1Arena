# InformatikProject


Dies ist die Dokumentation für mein Informatik Projekt, (INSERT NAME).

## Grundidee
Die Grundidee war League of Legends, ein Free-To-Play-MOBA von Riot GAMES, in Minecraft zu implementieren. 

### Minecraft
#### Minecraft Server
Ein Minecraft Server ist eine Möglichkeit für mehrere Minecraft Spieler, zusammen innerhalb einer Welt zu spielen.
#### Spigot
Spigot ist eine Minecraft Server Software, die eine API anbietet, um das Spielerlebnis von Spielern auf dem Server zu verändern. 

[Hier gehts zum Ursprung von Spigot](https://www.spigotmc.org/wiki/history-of-spigot)
#### Spigot Plugin
Man kann dem Server Plugins hinzufügen, die die Spigot-API verwenden, um das Spielerlebnis zu verändern.

## Aufbau eines Spigot Plugins
Ein Spigot Plugin sollte diese notwendigen Bestandteile besitzen: 
### - Die Main Class
Die Main Class ist die Klasse des Projekts, in der das wichtigste steht, wie z.B was passieren soll, wenn das Plugin geladen wird, oder was passiert, wenn das Plugin sich vom Server abkoppelt (Server Stop).
### - Die Plugin.yml
Diese Datei ist sozusagen die Info Datei des Plugins. Darin ist für den Server beispielsweise der Pfad zur Main Class des Projects niedergeschrieben.

Weitere häufig verwendete Interfaces wären z.B das Listener Interface, oder die Command API.

-> [Hier gehts zu einer Erklärung der Listener](https://www.spigotmc.org/wiki/using-the-event-api)

-> [Hier gehts zu einer Erklärung der Command API](https://www.spigotmc.org/wiki/create-a-simple-command)

## Spielaufbau des Plugins
Die Idee ist einfach: Beim joinen haben die Spieler 20 Sekunden Zeit, ihren Champion auszuwählen. Nach dieser Phase bekommen sie Zeit, um 4 Items auszuwählen. Danach beginnt die Kampfphase. Falls ein Spieler in dieser Phase den Gegner besiegt, oder der Gegner den Server verlässt, gewinnt der Spieler.

## Strukur des Plugins

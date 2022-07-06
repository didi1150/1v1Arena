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

## Struktur des Plugins
### Die Main Class
In meiner Main Class werden alle Manager initialisiert.

![Didi](https://user-images.githubusercontent.com/61380021/177633428-21b18f8c-718e-4921-84aa-38cfd042c1d9.png)


Gehen wir mal die Manager durch:

### Der GameStateManager

Das Spiel hat verschiedene States: Der LobbyState, der ItemSelectState, der IngameState und der EndingState.

#### Aufbau eines GameStates
Ein GameState besitzt eine Start und Stop Methode. Die meisten der GameStates besitzen auch noch einen Countdown.
#### Aufbau eines Countdowns
Auch hier ist der Aufbau recht simpel:
- Es gibt eine Start/Stop Methode
- Es gibt einen BukkitTask, der im Prinzip eine Runnable ist, die mit dem Minecraft Server gesynced ist


Hier nochmal zum Veranschaulichen:
![gamesystem](https://user-images.githubusercontent.com/61380021/177636642-493c3d80-273c-4f50-bfe7-3e4d8286267a.png)
![Countdowns](https://user-images.githubusercontent.com/61380021/177635308-53c308e5-49c8-461b-a60f-631197fb9298.png)

### Der ChampionManager
Wie der Name schon sagt, ist der ChampionManager dafür zuständig, alle spielbaren Champions zu managen. In ihm sind alle spielbaren und ausgewählten Champions aufgelistet.

#### Aufbau eines Champions
Ein Champion hat einen Ability Array, einen String name, BaseStats (Der Name ist eigentlich selbsterklärend), ein Icon, und ein Standart Attacken Item.
#### Aufbau einer Ability
Eine Ability kann von einem Spieler ausgeführt werden. Insgesamt besitzt ein Spieler vier Abiltiies. Wenn die Ability ausgeführt wird, wird sie auf Cooldown gesetzt, und der Spieler kann sie währenddessen nicht benutzen. Der Cooldown wird vom AbilityStateManager gesteuert.

### Der AbilityStateManager
Dieser Manager ist dafür zuständig, Abilities in den normalen Cooldown, oder in einen RecastCountdown zu versetzen (RecastCountdown zählt die Zeit herunter, wo man ein zweites Mal die Abilities nach dem ersten Cast ausführen kann. Dies geht nur bei Recastable Abilities).

#### Recastable Abilities
Recastable Abilties sind Abilties, die man mehrmals hintereinander ausführen kann.

Hier nochmal der (GROßE) Aufbau: 
![Champion_and_Abilities](https://user-images.githubusercontent.com/61380021/177640028-7fdf4e89-a066-42ba-8b1f-60c9710c4cd8.png)



# iti0301-2024
## Gandalf Royale

Players start empty handed spread around the map. Players have three inventory slots and they can collect different magic weapons and they can also switch between them or drop them. Different weapons have different purposes, some deal damage and some have other uses. The play area shrinks over time until only one player is alive, at which point the game ends. The player can be in the red zone, but will take damage over time.
There are also AI goblins roaming around the map (randomized movement), they will follow the player when the player enters their proximity (players will take damage, when the goblin is close enough). Goblins drop gold coins when killed. These coins can be used to buy even better weapons and to restore health. <br> <br>
Each player has three resources: health, mana and gold. <br>
**Health** - Visible to all nearby. Every hit decreases health and potions can be bought and used to regenerate health. <br>
**Mana** - Visible to all nearby. Every spell has a different mana cost. Mana regenerates automatically. Used to limit spell casting. <br>
**Gold** - Visible to only the player. Dropped by goblins when killed. Can be used to buy items from the shop. 

### Installation and starting

Until we do not have server, client has to run both server and clinet side on the same machine.

**Server Side:** <br>
1. Clone this repositroy [iti0301-2024-server](https://gitlab.cs.taltech.ee/rkilks/iti0301-2024-server)
2. If you don't have Gradle plugin, download it
3. Make sure you have server file as module in project structure, if it is not you should make it a module
4. Open Project view and go to iti0301-2024-server -> server -> src -> main -> java -> ee.taltech.game.server -> datamanagement -> GameServer
5. Run this file and you shoud have working server side

**Client Side:** <br>
1. Clone this repositroy [iti0301-2024-game](https://gitlab.cs.taltech.ee/rkilks/iti0301-2024-game)
2. If you don't have Gradle plugin, download it
3. Open Gradle plugin on project and go to iti0301-2024-game -> Tasks -> Other -> run
4. Now if you run this iti0301-2024-game\[run\] file you should have working client side

If you have started sever first and after that client then you shoud have working game instance on your screen.
Have fun :P
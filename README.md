# Plugin Description: H1

H1 is a Minecraft server plugin that removes one heart from a player each time they die.
If a player loses all their hearts (lives), a command is executed, which you can configure. For example, you can ban the player. WARNING: by default, it only displays a message that the player has lost all their lives.

You can use this creatively: sell hearts for in-game currency or real money, or make a quest that rewards hearts. You can also go hardcore and ban players permanently, but hey, it's Minecraft — not many players are into masochism. Use your imagination.

# Features:

- Player data is stored in a local SQLite database.
- All messages are configurable.
- You can give, set, or restore player hearts via command.
- There’s a limit: players can’t be given more than 20 half-hearts (10 full hearts).
- Includes a placeholder that displays the number of hearts a player has.

# Commands:

**/h1 give <player> <amount>** – Adds hearts to a player (up to 10 max).  
**/h1 set <player> <amount>** – Sets the player’s hearts to a specific value (up to 10).  
**/h1 recover <player>** – Restores the player’s lives to the maximum. Ideal for auto-recovery.  
**/h1 remove <player> <amount>** – Removes hearts from the player. (Lives can't go below zero.)  
**/h1 reload** – Reloads the plugin.

# Permissions:

**h1.reload** – Allows plugin reload.  
**h1.set** – Grants access to the /h1 set command.  
**h1.give** – Grants access to the /h1 give command.  
**h1.recover** – Grants access to the /h1 recover command.  
**h1.remove** – Grants access to the /h1 remove command.

# Placeholders:

Requires PlaceholderAPI.  
**%h1_lives%** – Displays the number of lives the player has (number only).


# Configuration

```yml
language: "en"    # en, ru
lives:
  # Default number of lives (hearts) for a new player
  default: 10

  # Command to execute when a player runs out of lives.
  # You can use the %player% placeholder, which will be replaced with the player's name.
  command: "say Player %player% has lost all their lives!"

database:
  # SQLite database file name.
  # The file will be created in the plugin folder if it doesn't exist.
  file: "players.db"
```
# Messages file:

```yml
prefix: "§6[H1] §r"
command:
  usage: "Usage: /h1 <give|set|remove|recover|reload> <player> <amount>"
  unknown: "Unknown command!"
reload:
  success: "Plugin reloaded successfully!"
give:
  success: "You gave %player% %amount% hearts."
  received: "You received %amount% hearts."
  max: "The player already has the maximum number of hearts (%max% hearts)."
  limited: "You can only give the player %available% hearts to avoid exceeding the limit of %max% hearts."
remove:
  success: "You removed %amount% hearts from %player%."
player:
  not:
    found: "Player not found!"
no:
  permission: "You don't have permission to use this command!"
life:
  info: "You now have %lives%."
recover:
  success: "You restored %player%'s life to the maximum."
number:
  format:
    error: "Invalid number: %input%"

```

# Installation:

1. Download the plugin.  
2. Stop your server.  
3. Drop the `.jar` file into your `/plugins/` folder.  
4. Start the server.  
5. Done.  
To enable placeholders, install PlaceholderAPI.

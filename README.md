# LSD

![](./android/assets/images/icon.png)

### Game Information:
A casual physics based arcade game where you fire the character around, avoiding the obstacles, to hit and destroy all the black platforms in order to beat the level.  This is a port of the original game idea (which was written in Lua) but this one is written in Java using the awesome libGDX framework. 

### Running the current game:
It can compile to the following platforms:
- Desktop (requires Java 1.6+ and OpenGL 3+) *Tested current version*
- Mobile (iOS / Android) *Not tested*
- HTML5 (any modern browser, Internet Explorer requires Google Chrome Frame) *Tested*

A compiled (but not regularly updated) HTML5 version is at: [basimkhajwal.freeoda.com/games/LSD] (http://basimkhajwal.freeoda.com/games/LSD)

They should all work but only desktop and HTML5 builds have currently been tested. For the latest version, download this repository and run the gradle-based projects on your computer -- more [here](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline).

### Development:

#### Currently Working:
- [x] Platforms are loaded from a Tiled map (.tmx) format
- [x] Platforms destroy correctly when player launched off them
- [x] Platform count / destroyed
- [x] Player moves accurately and the aim is precise
- [x] Camera movement to follow the player
- [x] Add ability to restart the level
- [x] Implement player death when reaching map boundaries
- [x] Add moving down jump animation

#### TODO:
- [ ] Create solid blocks the player has to avoid
- [ ] Implement player death with solid blocks
- [ ] Create a player died menu
- [ ] Create a level finished menu
- [ ] Add ability to progress from level to level

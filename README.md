# PEGS Emotes

Static and animated Discord/Twitch style chat emotes for Minecraft.

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

- Setup Fabric for [VSCode](https://fabricmc.net/wiki/tutorial:vscode_setup)
- Building Jar `.\gradlew build`
- Built Jar can be found in `build/libs/*.jar`

## Installing

> Requires Fabric API

- Download the latest `.jar` file
- Drop into your mod folder
- Restart Minecraft

## Usage

Simply type the emote into chat using the format you would in a Twitch chat E.G. `HACKERMANS` and `modCheck`.

## Adding emotes

To add emotes, you'll probably first want to setup the mod to load emotes from your local environment.
The easiest way to do this is to change the emote repository URL in the `pegs-emotes.json` config file to a file url,
for example `file:///D:\Dev\Java\PegsEmotes\content\`. It should point to the `content` directory in this repository.

After this, add new lines to the `content/emotes.json` file to add new emotes. Most emotes are stored in either the
`static` or `animated` subdirectories in the `content` folder.

Adding animated emotes is a bit different:

1. Use [this site](https://sheeptester.github.io/words-go-here/misc/animated-painting-maker.html) to convert a gif into
   an image with all the frames.
2. Get the frame time from [this site](https://ezgif.com/maker) by multiplying the delay by 10
   (e.g. peepoLeave: delay = 2, frame_time = 20)
3. Add an entry to `emotes.json` pointing to the above image and including the calculated frame time.

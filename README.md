# What is it?
Bob is an All-In-One Replay editor, that aims to edit almost everything you see.

From [modifying chunk data](https://www.youtube.com/watch?v=2UeZoHjp0s4) to [adding players](https://www.youtube.com/watch?v=qZpBwZYVf8M) in the replay, Bob will be able to do almost anything.

Bob will not have any limits. If you want to put a ridiculously high value, you can (do it at your own risk, don't blame me if you break a replay).

There are currently no useful GUIs, as it's a pain to make, and I have other things to do before tackling that.

# WARNING:
```diff
- Bob is very experimental.
- Do not blame me for any broken Replays (that's why Bob makes backups), but follow next point instead.
- If you have any issues with Bob, report it in the issues tab ONLY IF IT HASN'T BEEN REPORTED ALREADY.
```

# What can it do?
As I said previously, you can:
* [Edit the chunk data](https://www.youtube.com/watch?v=2UeZoHjp0s4) (not chunks yet, I'm working on [Regions](https://github.com/RaderRMT/Regions) to edit chunk data)
* [Change the weather](https://www.youtube.com/watch?v=rSy1GLKjlyc)
* [Change the time](https://www.youtube.com/watch?v=jJNyomiZLfQ)
* [Add players](https://www.youtube.com/watch?v=qZpBwZYVf8M) (entities in general)
* [And more](https://www.youtube.com/watch?v=dQw4w9WgXcQ)...

# Planned features:
* - [ ] 3D Engine:
    * - [ ] Make a texture atlas from Minecraft's textures
    * - [ ] Show chunk data
    * - [ ] Optimizations:
        * - [ ] Meshing
        * - [ ] Greedy Meshing
        * - [ ] Editing chunk data
        * - [ ] Showing entities
* - [ ] Replays:
    * - [ ] Cleaning Replays (removing useless packets)
    * - [ ] Export Replays
    * - [ ] Merge Replays
* - [ ] Features:
    * - [ ] Adding/Editing entities
    * - [ ] Changing time/weather
    * - [ ] Recording user's movements for entities path
    * - [ ] Timeline Editor:
        * - [ ] Adding/Removing Keyframe
        * - [ ] Following an entity
        * - [ ] Making a complex path
        * - [ ] Record user's movements for path
    * - [ ] World Downloader:
        * - [ ] Export world on current timestamp
        * - [ ] Export the entire world up to current timestamp (every chunks loaded up to timestamp)
        * - [ ] Export selected chunks
        * - [ ] Overwrite chunks in an already existing save
    * - [ ] Work on Replay recorded from 1.8 to 1.17

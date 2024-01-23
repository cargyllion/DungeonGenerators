<h1>Brianna's Dungeon Generators</h1>
<p><i>They're fer making yer roguelikes cooler!</i></p>

<p>This project is an open source collection of Roguelike dungeon generators aggregated from various roguelikes, with the purpose of allowing other game designers quick and easy access to more eye-catching dungeons with which to torture your playerbase.</p>

<p>All dungeon generators may be used free of charge, so long as proper attribution is made to the individuals involved in their development.</p>

<p>As of January 23, 2024, this project includes 4 generators.</p>

<h2>Roguelike</h2>
<p>Your generic series of rooms interconnected with winding corridors.</p>

<b>Issues:</b> Dungeon generation will inevitably fail on non-standard maps that exceed 70 tiles in either axis. This should be fixed by scaling the length check from 100 tiles in findPath().

<b>Authors:</b> Jeff Lait, Brianna Stafford

<h2>Drunken Caves</h2>
<p>An implementation of Leo Grady's <a href="https://en.wikipedia.org/wiki/Random_walker_algorithm">Random Walker Algorithm</a>.</p>

<b>Authors:</b> Jeff Lait, Brianna Stafford

<h2>Maze Generator</h2>
<p>A simple maze generator.</p>

<b>Todo:</b> In POWDER, the narrow corridors these floors can generate with caused issues with navigation for larger NPCs like the Cretan Minotaur. Change the cutout step so that it punches out the entire wall of the cell, instead of individual tiles.

<b>Authors:</b> Jeff Lait, Brianna Stafford

<h2>Qix</h2>
<p>Generates a dungeon modeled after a Qix playfield.</p>

<b>Authors:</b> Jeff Lait, Brianna Stafford

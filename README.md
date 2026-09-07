<p align="center">
  <img src="docs/assets/bluespoof-logo.png" alt="BlueSpoof" width="760">
</p>

<p align="center">
  <strong>Public API for controlling BlueSpoof fake players from any Bukkit/Paper plugin.</strong>
</p>

<p align="center">
  <img alt="Version" src="https://img.shields.io/badge/version-3.9-blue">
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.4.10-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Java" src="https://img.shields.io/badge/Java-8+-ED8B00?logo=openjdk&logoColor=white">
  <img alt="Build" src="https://img.shields.io/badge/build-Gradle-02303A?logo=gradle&logoColor=white">
  <img alt="License" src="https://img.shields.io/badge/license-MIT-green">
</p>

## Overview

Fake players spawned by BlueSpoof are real
server-side players (physics, health, inventory, tab list) driven by the server instead of a
network client. This API lets your plugin play the client's role: hold movement keys, turn the
camera, jump, attack, chat, and walk to goals with A* pathfinding. Think of it as **Mineflayer,
but server-side and for Java plugins**.

The API version always matches the first two segments of the BlueSpoof release that ships it
(this release is `3.9`, shipped with BlueSpoof 3.9.0).

This library only compiles against the API surface; the BlueSpoof plugin itself must be
installed on the server for any of it to work. Get it at
[blueva.net/store/blue-spoof](https://blueva.net/store/blue-spoof).

## Installation

The API classes are shaded (unrelocated) into the BlueSpoof plugin jar, so your plugin only
needs the API at compile time, with `provided`/`compileOnly` scope. Declare BlueSpoof as a
dependency (or soft-dependency) in your `plugin.yml`:

```yaml
depend: [BlueSpoof]      # or softdepend: [BlueSpoof]
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://repo.blueva.net/releases")
}

dependencies {
    compileOnly("net.blueva.spoof:BlueSpoof-API:3.9")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>blueva</id>
        <url>https://repo.blueva.net/releases</url>
    </repository>
</repositories>

<dependency>
    <groupId>net.blueva.spoof</groupId>
    <artifactId>BlueSpoof-API</artifactId>
    <version>3.9</version>
    <scope>provided</scope>
</dependency>
```

repo.blueva.net is a public Maven repository, no authentication needed to depend on it.

## Quick start

```java
import net.blueva.spoof.api.BlueSpoofAPI;
import net.blueva.spoof.api.FakePlayer;
import net.blueva.spoof.api.pathfinding.PathStatus;
import net.blueva.spoof.api.pathfinding.goals.GoalBlock;

public void demo() {
    if (!BlueSpoofAPI.isAvailable()) {
        return; // BlueSpoof is not installed/enabled
    }

    BlueSpoofAPI.getFakePlayer("SteveBot").ifPresent(bot -> {
        bot.chat("Hello! I'm a bot.");
        bot.pathfinder().goTo(new GoalBlock(100, 64, -30))
                .thenAccept(result -> {
                    if (result.getStatus() == PathStatus.SUCCESS) {
                        bot.chat("I'm here!");
                    }
                });
    });
}
```

### Creating fake players

```java
FakePlayer bot = BlueSpoofAPI.createFakePlayer("AlexBot", spawnLocation);
bot.lookAtInstantly(targetLocation);
```

The fake player joins like a real player: join message, tab-list entry, skin,
`PlayerJoinEvent`, survival mode and full physics. Remove it with
`bot.disconnect()`.

## Movement: control states ("held keys")

Movement is **client-style input, not teleports**. Control states behave like
held keyboard keys consumed every tick by vanilla server physics: acceleration,
friction, jump arcs, water, ladders and knockback are all real. On modern server
versions the inputs are fed straight into the vanilla `travel()` path, producing
movement indistinguishable from a real client; older versions use a
physics-approximating fallback stepper.

```java
bot.setControlState(ControlState.FORWARD, true);   // start walking
bot.setControlState(ControlState.SPRINT, true);    // ... fast
bot.lookAt(destination);                            // steer with the camera
// later:
bot.clearControlStates();                           // release everything
```

Available controls: `FORWARD`, `BACKWARD`, `LEFT`, `RIGHT`, `JUMP`, `SPRINT`,
`SNEAK`. `JUMP` behaves like holding space (jumps again on landing, swims up in
liquids). For a single hop use `bot.jump()`.

## Looking

```java
bot.lookAt(targetLocation);      // smooth mouse-like turn, returns CompletableFuture
bot.look(90.0, 0.0);             // face west, horizon
bot.lookAtInstantly(target);     // snap instantly
```

`look`/`lookAt` complete their future once the fake player faces the target.

## Pathfinding

```java
import net.blueva.spoof.api.pathfinding.goals.*;

bot.pathfinder().goTo(new GoalNear(100, 64, -30, 2));      // get within 2 blocks
bot.pathfinder().goTo(new GoalXZ(100, -30));               // reach a column
bot.pathfinder().goTo(new GoalGetToBlock(chestLocation));  // stand next to a block
bot.pathfinder().goTo(new GoalFollow(player, 3.0));        // follow a player

// Fire and forget:
bot.pathfinder().setGoal(new GoalBlock(0, 64, 0));

// Stop:
bot.pathfinder().stop();     // graceful
bot.pathfinder().cancel();   // immediate, releases all controls
```

The pathfinder walks on land, steps up single blocks, drops down ledges (up to
`Movements.getMaxDropDown()`, default 4), sprints when allowed, **swims through
water** (rivers, lakes, diving; `Movements.setAllowSwimming(...)`, default on),
**climbs ladders, vines and scaffolding** (`Movements.setAllowClimbing(...)`,
default on), avoids lava and hazardous blocks, and recomputes the path when
stuck or when a dynamic goal moves. It also **leaps gaps** with a real
sprint-jump arc (minigame parkour; `Movements.setAllowParkour(...)`, default
on): cardinal gaps up to `setMaxParkourGap(3)` (a 4-block leap, the vanilla
limit; default gap is 1, i.e. 2-block jumps) and diagonal jumps when the gap
is 2+. Configure it per bot:

```java
Movements movements = bot.pathfinder().getMovements();
movements.setAllowSprinting(true);
movements.setMaxParkourGap(3);            // up to 4-block sprint jumps (since 3.7, default 1)
movements.setAllowSwimming(true);         // path through water (since 3.7, default on)
movements.setAllowClimbing(true);         // ladders, vines, scaffolding (since 3.7, default on)
movements.setAllowParkour(false);         // disable gap leaps if unwanted (since 3.7, default on)
movements.setMaxDropDown(2);
movements.setSearchRadius(20000);         // A* node budget
movements.setThinkTimeoutMillis(15000);
```

Custom goals: implement `net.blueva.spoof.api.pathfinding.Goal`
(`isEnd(PathNode)` + `heuristic(PathNode)`).

## Inventory and containers

Every fake player has a real inventory you can read and write on any server
version (call these on the bot's owning thread, e.g. inside an API future's
`thenAccept`):

```java
BotInventory inv = bot.inventory();          // slots 0-8 hotbar, 9-35 main,
                                             // 36-39 armor, 40 off-hand
ItemStack held = inv.getHeldItem();
int arrows = inv.count(Material.ARROW);
int slot = inv.first(Material.OAK_LOG);
inv.addItem(new ItemStack(Material.COBBLESTONE, 64));
inv.removeItem(Material.OAK_LOG, 4);
inv.setItem(BotInventory.SLOT_HELMET, helmet);
```

Opening chests and crafting go through the real vanilla menus on Minecraft
26.1+ (guard with `bot.supportsAdvancedActions()`; opening/reading works
everywhere, clicking requires the native hook):

```java
import net.blueva.spoof.api.inventory.*;

bot.openContainer(chestLocation).thenAccept(view -> {
    System.out.println(view.getTitle());             // "Chest"
    view.click(0, ContainerClick.SHIFT_LEFT)         // quick-move slot 0 to the bot
        .thenCompose(ok -> view.moveToInventory(1))  // same, returning moved amount
        .thenCompose(moved -> view.close());
});

// Crafting: real recipe matching, ingredient consumption, remaining items
// (buckets), stats and advancements. 2x2 recipes use the inventory grid;
// 3x3 recipes require a crafting table within reach.
bot.craft(new ItemStack(Material.OAK_PLANKS, 8), 8)
   .thenAccept(crafted -> bot.chat("crafted " + crafted + " planks"));
```

## Actions

Every action with an observable outcome returns a `CompletableFuture` that
completes on the server's main thread (or the owning region thread on Folia):

```java
bot.swingArm(EquipmentSlot.HAND);
bot.attack(targetEntity);
bot.jump();
bot.dropItem(false);                  // drop one item; true = whole stack
bot.setHeldItemSlot(3);
bot.equip(EquipmentSlot.HEAD, helmet);
bot.teleport(location);
bot.chat("/spawn");                   // through the real chat pipeline
bot.sendMessage("Only the bot sees this");
```

Because fake players are real players, you can also use the Bukkit API directly
on `bot.getBukkitPlayer()` for anything not covered here (scoreboards, effects,
inventories...). Prefer the API for locomotion so the movement simulation stays
coherent.

## Advanced world interaction (Minecraft 26.1+)

Minigame-grade interaction driven through the exact vanilla server code paths.
Check `bot.supportsAdvancedActions()` first; on older server versions these
methods throw `UnsupportedOperationException`:

```java
// Fishing: cast, wait for the bite, reel in. PlayerFishEvent fires for the bot.
bot.useItem(EquipmentSlot.HAND);                 // cast the rod
// ... listen for PlayerFishEvent (State.BITE) on the bot ...
bot.useItem(EquipmentSlot.HAND);                 // reel in

// Dig with vanilla survival timing (tool speed, enchants, crack animation):
bot.lookAt(oreLocation)
   .thenCompose(v -> bot.dig(oreLocation))
   .thenAccept(broken -> { if (broken) bot.chat("mined!"); });
bot.stopDigging();                               // abort (future completes false)

// Right-click a block: place blocks, open doors/chests, flip levers:
bot.useItemOn(doorLocation, BlockFace.NORTH, EquipmentSlot.HAND);

// Right-click an entity: mount, leash, trade, shear, feed:
bot.interact(villager, EquipmentSlot.HAND);

// Bow / shield / food: start with useItem, release manually:
bot.useItem(EquipmentSlot.HAND);                 // draw the bow
bot.releaseUseItem();                            // shoot
```

## Events

Register Bukkit listeners for the fake-player lifecycle:

| Event | Fired when |
|-------|-----------|
| `FakePlayerSpawnEvent` | a fake player has fully joined and is ready to control |
| `FakePlayerQuitEvent` | a fake player is about to disconnect |
| `FakePlayerTickEvent` | every tick while a fake player is under active API control |
| `FakePlayerGoalReachedEvent` | a pathfinder navigation succeeds |
| `FakePlayerPathFailedEvent` | a navigation fails (no path, timeout, stuck, cancelled) |

```java
@EventHandler
public void onGoal(FakePlayerGoalReachedEvent event) {
    event.getFakePlayer().chat("Arrived!");
}
```

All standard Bukkit events (`PlayerJoinEvent`, `PlayerMoveEvent`,
`EntityDamageEvent`, `AsyncPlayerChatEvent`, ...) also fire for fake players.
Detect them with `BlueSpoofAPI.isFakePlayer(player)`.

## Threading

- Getters and control-state queries can be called from any thread.
- Mutating actions are automatically bounced to the fake player's owning
  thread (main thread on Spigot/Paper, region thread on Folia).
- `CompletableFuture`s returned by the API complete on that owning thread, so
  plain `thenAccept`/`thenRun` continuations may safely touch the Bukkit API.

## Versioning

The API version matches the BlueSpoof release that ships it. Additions ship
as `default` methods with `@since` javadoc, so consumer plugins compiled
against older API jars keep working: methods added later simply throw
`UnsupportedOperationException` if the installed BlueSpoof is too old.

## Authors

- Blueva
- Whiron

Website: [blueva.net](https://blueva.net)

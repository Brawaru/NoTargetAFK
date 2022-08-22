# NoTargetAFK

> Prevents mobs from targeting AFK players.

# What is this?

Essentials AFK status does not protect you from mobs. You can of course enable
freeze function, but mobs will still try to attack you: skeletons will shoot
arrows, creepers come close and explode.

This plugin changes that. Now, whenever you go AFK, all mobs just lose
interest in you. They will still spawn, and wander around you, but won't
target or attack you like you don't exist or one of their own.

> **Note**
>
> In 1.19, Warden will still be able to sniff you and will get angry. It,
> however, won't attack you while you are AFK, instead, it will stay next to
> you and roar endlessly. This unfortunately cannot be prevented as there no
> APIs for that, so don't go AFK in places where Warden can spawn :)

# Usage

Install alongside the Essentials plugin. This plugin currently does not have
configuration or permissions, these will probably be added in the future.

## Configuration options

The plugin itself is currently not configurable.

<details>
<summary>Permissions available</summary>

- **`notargetafk.mobs-lose-track`** \
  Whether all entities should lose track of player when they go AFK.

  **Default**: true.
- **`notargetafk.mobs-dont-target`** \
  Whether the player should not be targeted when they are AFK.

  **Default**: true.

</details>

## Feedback and bug reports

All feedback and bug reports are welcome in the [GitHub issues][issues].

## Support policy

To cut cost of maintenance only the latest major Minecraft versions are
supported (targeted and tested). You may be able to download older versions of
the plugin through releases history, but do not expect any of the changes from
newer versions to be back-ported.

You are welcome to fork and back-port this plugin to work on older versions;
however, no bug reports will be accepted from such builds unless they are
reproducible in newer builds.

## Credits

This project would not exist without the support of [Fundy's][fundy] offline
chat community.

[issues]: https://github.com/Brawaru/NoTargetAFK/issues
[fundy]: https://www.twitch.tv/fundy
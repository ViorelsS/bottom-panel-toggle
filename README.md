# Bottom Panel Toggle

![Bottom Panel Toggle](src/main/resources/META-INF/pluginIcon.svg)

One shortcut to hide and restore the bottom panel in IntelliJ IDEA.

**macOS:** `Cmd+J` · **Windows / Linux:** `Ctrl+J`

## Why this plugin exists

Working in IntelliJ often means switching between the editor, Terminal, Run,
Debug, and Services. Each tool window has its own shortcut. Hiding the active
tool window depends on which window is active, while hiding all tool windows
also hides side panels such as Project.

I wanted one consistent command for the bottom panel:

- Hide the bottom tool windows that are currently visible and return to the editor.
- Press the same shortcut again to restore the last used bottom tool window.
- Keep side panels open.
- Make the decision from the visible layout, regardless of whether the editor,
  a bottom tool window, or Project has focus.

Bottom Panel Toggle adds that command using the IntelliJ Platform APIs.

## Behavior

| Current layout | Result |
| --- | --- |
| Terminal is visible at the bottom | Hide it and focus the editor. |
| Run, Debug, or Services is visible at the bottom | Hide it and focus the editor. |
| Multiple bottom tool windows are visible | Hide them all; the next toggle restores the last used one. |
| No bottom tool window is visible | Restore and focus the last used bottom tool window. |
| Project is open on the left | Toggle the bottom panel and keep Project open. |
| A tool window has been moved to a side | Leave it there; it no longer counts as a bottom panel. |
| A tool window is floating or in a separate window | Leave it alone. |
| No previous bottom tool window can be restored | Open Terminal if available at the bottom; otherwise open the bottom tool window whose visible button label comes first alphabetically. |

Hiding a tool window does not close its tabs or stop running processes.
The last used bottom tool window is remembered separately for each open project
and is not persisted when the project is closed.

## Installation

Download the plugin ZIP from the [latest GitHub release](https://github.com/ViorelsS/bottom-panel-toggle/releases/latest), then install it in IntelliJ IDEA:

1. Open **Settings → Plugins** in IntelliJ IDEA.
2. Open the gear menu and select **Install Plugin from Disk…**.
3. Select the downloaded `bottom-panel-toggle-<version>.zip` file.
4. Restart the IDE if prompted.
5. Configure the shortcut as described below.

### Shortcut conflicts

The standard IntelliJ keymaps already use `Cmd+J` / `Ctrl+J` for
**Insert Live Template**. Assign the combination to **Toggle Bottom Panel**
and remove that same combination from conflicting actions in **Settings → Keymap**.
The plugin does not remove shortcuts from other actions automatically.

Default bindings are supplied for the standard Windows/Linux and macOS keymaps.
If you use another keymap, assign the shortcut to **Toggle Bottom Panel** manually.

The command is also available from **Tools → Toggle Bottom Panel** and
**Find Action**.

### Terminal key handling

Since version 1.0.1, the Reworked Terminal explicitly allows **Toggle Bottom Panel**
even when **Settings → Tools → Terminal → Override IDE shortcuts** is enabled.
The shortcut must still be assigned to the action in your active keymap.

This integration applies to the Reworked Terminal. In the Classic Terminal,
if the shell receives the shortcut instead of the IDE, disable
**Settings → Tools → Terminal → Override IDE shortcuts**.

## Compatibility

The declared IntelliJ IDEA compatibility range is **2025.3.6.1 through 2026.2.x**
(builds `253.33813.55` through `262.*`).

Binary compatibility has been checked with Plugin Verifier against:

- IntelliJ IDEA **2025.3.6.1** — build `253.33813.55`.
- IntelliJ IDEA **2026.1.5** — build `261.27258.48`.
- IntelliJ IDEA **2026.2.3** — build `262.10968.63`.

All three checks report compatibility with two experimental API usages in the
Reworked Terminal integration and no internal API usages. The Terminal integration
uses an experimental JetBrains API, so compatibility must be checked before
extending the supported IDE range.

The Terminal plugin is optional. Bottom Panel Toggle can also run when Terminal
is absent or disabled.

## Development

The project uses Kotlin, JDK 21, and the Gradle Wrapper included in this repository.
Its compilation and default sandbox target is **IntelliJ IDEA 2025.3.6.1**.

From the repository directory, with JDK 21 configured:

```bash
# Start an isolated IntelliJ instance with the plugin loaded.
./gradlew runIde

# Build the installable ZIP in build/distributions/.
./gradlew buildPlugin

# Check the archive structure and binary compatibility with the configured IDEs.
./gradlew verifyPluginStructure verifyPlugin
```

On Windows, use `gradlew.bat` instead of `./gradlew`.
The first run downloads Gradle, the target IntelliJ distribution, and dependencies.

### Manual verification

In the sandbox, open a project and an editor file, then check:

1. Open Terminal at the bottom, focus the editor, and toggle twice.
   Terminal should hide, then reopen with focus.
2. Toggle while Terminal itself has focus. The editor should receive focus.
3. Keep Project open on the left and repeat the toggle.
4. Open another bottom tool window, hide it with the mouse, focus Project,
   and toggle. The most recently used bottom tool window should reopen.
5. Move a tool window to a side or make it floating. The bottom-panel command
   should leave it alone.
6. With the Reworked Terminal engine selected, enable **Override IDE shortcuts**
   and toggle while the terminal has focus.
7. Disable the Terminal plugin, restart the sandbox, and open a fresh project
   with at least two available bottom-bar buttons. With no previous bottom panel,
   the toggle should open the tool window whose button label comes first
   alphabetically, regardless of button order on the bar.

Manual checks have been performed on macOS in the IntelliJ IDEA 2025.3.6.1 sandbox.
The PowerShell toggle has also been verified on Windows with the Reworked Terminal
and **Override IDE shortcuts** enabled.
Linux has not yet been manually tested. Binary compatibility checks are listed
separately above.

## Contributing

When reporting a problem, include your IntelliJ version, operating system,
active keymap, tool window positions, and the steps needed to reproduce it.

For a change, explain the behavior it improves and how you verified it.

## License

[MIT](LICENCE.md) © 2026 Viorelss.

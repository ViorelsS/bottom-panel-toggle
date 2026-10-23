# Changelog

## [1.0.3] - 2026-10-23

### Changed

- Build the modern package for IntelliJ IDEA 2025.3.6.1 through 2026.2.x from the same source project as the legacy package.
- Keep automatic shortcut support in the Reworked Terminal when Override IDE shortcuts is enabled, using the native optional extension descriptor.
- Select this package with `-PideLine=modern`, which is also the default build.
- Separate build output and sandbox directories for the two packages.
- Verify binary compatibility against IntelliJ IDEA 2025.3.6.1, 2026.1.5, and 2026.2.3. Each report is Compatible with two experimental API usage findings for the terminal provider and no deprecated or internal API usage findings.

## [1.0.2] - 2026-10-23

### Added

- Provide the legacy compatibility package for IntelliJ IDEA 2024.1 through 2025.3.x, including early 2025.3 builds below the modern package's minimum build.

### Changed

- Compile against IntelliJ IDEA Community Edition 2024.1 using a JDK 17 toolchain.
- Share the toggle implementation with the modern package; select legacy with `-PideLine=legacy`.
- Exclude the Reworked Terminal integration from this package. Disable Override IDE shortcuts if the terminal captures the toggle shortcut.
- Verify binary compatibility against Community Edition 2024.1, 2024.2.6, 2024.3.7.1, 2025.1.7.2, and 2025.2.6.3, plus IntelliJ IDEA 2025.3 and 2025.3.6.1. All seven reports are Compatible with no API usage warnings.

## [1.0.1] - 2026-10-22

### Fixed

- Allow the toggle shortcut in the Reworked Terminal when Override IDE shortcuts is enabled.

### Changed

- Replace internal layout API usage with an alphabetical fallback when neither the previous bottom panel nor Terminal is available.

## [1.0.0] - 2026-10-22

### Added

- Toggle bottom tool windows with Cmd+J on macOS or Ctrl+J on Windows/Linux.
- Hide visible bottom tool windows and return focus to the editor.
- Restore and focus the last used bottom tool window when the bottom panel is hidden.
- Remember bottom tool window usage separately for each open project.
- Leave side, floating, and separate tool windows unchanged.
- Open Terminal, or the first available bottom-bar tool window, when no previous bottom tool window can be restored.

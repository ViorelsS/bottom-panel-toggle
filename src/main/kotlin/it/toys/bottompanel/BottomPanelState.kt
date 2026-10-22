package it.toys.bottompanel

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ToolWindowType
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.openapi.wm.ex.ToolWindowManagerListener.ToolWindowManagerEventType

@Service(Service.Level.PROJECT)
class BottomPanelState {
    var lastId: String? = null
}

class BottomPanelListener(
    private val project: Project
) : ToolWindowManagerListener {

    override fun toolWindowShown(toolWindow: ToolWindow) {
        remember(toolWindow)
    }

    override fun stateChanged(
        manager: ToolWindowManager,
        changeType: ToolWindowManagerEventType
    ) {
        if (changeType != ToolWindowManagerEventType.ActivateToolWindow) return

        val window = manager.getToolWindow(manager.lastActiveToolWindowId)
            ?: return

        if (window.isVisible) remember(window)
    }

    private fun remember(window: ToolWindow) {
        if (window.isBottomPanel()) {
            project.service<BottomPanelState>().lastId = window.id
        }
    }
}

internal fun ToolWindow.isBottomPanel(): Boolean =
    anchor == ToolWindowAnchor.BOTTOM &&
            (type == ToolWindowType.DOCKED ||
                    type == ToolWindowType.SLIDING)
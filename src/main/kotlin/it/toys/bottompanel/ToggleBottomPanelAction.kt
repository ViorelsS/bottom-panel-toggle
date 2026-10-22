package it.toys.bottompanel

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ex.ToolWindowManagerEx

class ToggleBottomPanelAction : DumbAwareAction() {

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val manager = ToolWindowManager.getInstance(project)
        val state = project.service<BottomPanelState>()

        val bottomWindows = manager.toolWindowIds
            .mapNotNull { manager.getToolWindow(it) }
            .filter { it.isVisible && it.isBottomPanel() }

        if (bottomWindows.isEmpty()) {
            val candidates = manager.toolWindowIds
                .mapNotNull { manager.getToolWindow(it) }
                .filter { it.isAvailable && it.isBottomPanel() }

            // ponytail: internal layout API; recheck when upgrading the IntelliJ SDK.
            val layout = ToolWindowManagerEx.getInstanceEx(project).getLayout()

            val target = candidates.firstOrNull { it.id == state.lastId }
                ?: candidates.firstOrNull { it.id == "Terminal" }
                ?: candidates
                    .filter {
                        it.isShowStripeButton && manager.isStripeButtonShow(it)
                    }
                    .minWithOrNull(
                        compareBy<ToolWindow> { it.isSplitMode }
                            .thenBy {
                                layout.getInfo(it.id)?.order
                                    ?.takeIf { order -> order >= 0 }
                                    ?: Int.MAX_VALUE
                            }
                    )

            target?.activate(null)
            return
        }

        val previous = bottomWindows.firstOrNull { it.id == state.lastId }
            ?: bottomWindows.first()

        state.lastId = previous.id

        bottomWindows.forEach { it.hide() }
        manager.activateEditorComponent()
    }
}
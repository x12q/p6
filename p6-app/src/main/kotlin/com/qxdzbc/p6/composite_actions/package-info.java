/**
 * This package contains composite actions.
 *
 * Composite actions are:
 * - actions that change multiple aspects of the app at once. Often these aspect are not quite related, so such actions are not suitable to be put in packages for any specific major group of UI.
 * - or, they are very old actions designed at the beginning of the app and are not very well-defined. All composite action exist as global singletons.
 *
 *
 * A composite actions should be considered to be put in one of the major group of UI before being put here. Only if it is not suitable to be put in any of the major group of UI, it should be put here.
 *
 * TODO move composite actions to suitable major groups of UI if possible.
 */
package com.qxdzbc.p6.composite_actions;
# The dependency injection container pattern of this app

This app contains two level of Dagger Component.

Level 1: App level Component. This level represent by P6Component. It houses objects that are available globally to every object at any level.
    - Scope = SingletonScope
Level 2:
    - The purpose of scoping object to these lv2 component is to provide automatic wiring within their respective state object.
    - All lv2 components have direct access to lv1 component.
    - Window level, see WindowComponent
        - Scope: WindowScope
        - wiring within: WindowState
    - Workbook level, see WbComponent
        - Scope: WbScope
        - wiring within: WorkbookState
    - Worksheet level, see WsComponent
        - Scope: WsScope
        - wiring within: WorksheetState
    - So, what would I need to do if I need access to object within a lv2 from the outside, such as, from lv1, or from other lv2 component?
        - answer: direct query either using the central state container, or use local container.
        - Example:
            How do I need access to an object within WorksheetState of WsComponent from a WorkbookState?
                answer: give my position is at a WorkbookState, I would face with multiple Worksheet at once, so I must have a worksheet name that I need to query. I can just invoke the worksheet container within my workbook to read access the worksheet state object, then go down further to reach my target object.
            How do I do that from WindowState?
                answer: given my position at a WindowState, I must have the id of the workbook and worksheet I need to query, so, I just need to use the state container objects in my window to access my target objects
            How do I do that from anywhere?
                answer: because anywhere can access the global objects at lv1, I can just get the root-level state containers, then go down the query path in order to get my target objects.
    - Why don't I put WsComponent under WbComponent, WbComponent under WindowComponent in the component hierarchy? 
        - answer: the reason is that a Worksheet can be moved to different Workbook by users, so it is unreasonable to give a worksheet direct access to some workbook that it no long be a part of. The same is for Workbook and Window.
            - For example: 
                - Ws1 is in Wb1. User move Ws1 to Wb2.
                - If I put WsComponent under WbComponent, Ws1 will still have access to Wb1 while it is in Wb2.
                - This is bad
            


```mermaid

classDiagram

    AthleteViewScene ..|> RaceListTab
    AthleteViewScene ..|> RaceGraphTab
    AthleteViewScene ..|> StatTab    

    MeetViewScene ..|> RaceListTab
    MeetViewScene ..|> StatTab

    class UIController {
        
    }

    class AthleteViewScene {

    }

    class MeetViewScene {

    }

    class RaceListTab {

    }

    class RaceGraphTab {
        
    }

    class StatTab {

    }

    class AthleteEntryScene {

    }

    class MeetEntryScene {

    }

    class DefaultScene {

    }

```
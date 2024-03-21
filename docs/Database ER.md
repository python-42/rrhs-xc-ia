```mermaid

erDiagram
    RACE ||--|{ MEET :"occurs at"
    RACE ||--|{ ATHLETE:"is ran by"

    RACE {
        int meetID
        int athleteID
        int level
        double time
        double mileOneSplit
        double mileTwoSplit
        int place
    }

    MEET {
        Date date
        String name
        int ID
    }

    ATHLETE {
        String fullName
        int gradYear
        int ID
    }

```
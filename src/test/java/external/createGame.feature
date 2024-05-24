Feature: Crear una partida nueva y acceder a ella.

Scenario: Crear una partida nueva después de logear
Given driver "htpps://localhost:80"
When submit().click(input[type=partidaPreview])
Then match html("title") contains "newGame"
And input('name') = "newGame"
And select('select[name=gamesystem]', 2)
And input('meeting') = "testplace"
And input('description') = "testdescription"
And input('date') = "2019-12-12"
When submit().click(input[type=submit])
Then screenshot()
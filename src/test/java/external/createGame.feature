Feature: Crear una partida nueva y acceder a ella.

Scenario: Crear una partida nueva después de logear
Given driver 'http://localhost:80'
When click('button[name=createGame]')
And input('#username', 'admin')
And input('#password', 'aa')
When submit().click(".form-signin button")
When click('button[name=createGame]')
And input('#name', 'newGame')
And select('select[name=gamesystem]', 2)
And input('#meeting', 'testplace')
And input('#description', 'testdescription')
And input('#date', '2019-12-12')
When submit().click('button[type=submit]')
# funciona hasta aquí'
Then match driver.url contains 'game'
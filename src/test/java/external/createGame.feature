Feature: Crear una partida nueva y acceder a ella.

Scenario: Crear una partida nueva después de logear
Given driver "localhost:80"
When submit().click(input[type=partidaPreview])
Then match html("title") contains "newGame"
And input('name') = "newGame"
And select('select[name=gamesystem]', 2)
And input('meeting') = "testplace"
And input('description') = "testdescription"
And input('date') = "2019-12-12"
When submit().click(input[type=submit])
Then screenshot()

Scenario: try to login to github
    and then do a google search
  
      Given driver 'https://github.com/login'
      And input('#login_field', 'dummy')
      And input('#password', 'world')
      When submit().click("input[name=commit]")
      Then match html('.flash-error') contains 'Incorrect username or password.'
  
      Given driver 'https://google.com'
      And input("textarea[name=q]", 'karate dsl')
      When submit().click("input[name=btnI]")
      Then waitForUrl('https://github.com/karatelabs/karate')
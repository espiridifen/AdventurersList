function fn() {
    var env = karate.env; // get system property 'karate.env'
    karate.log('karate.env system property was:', env);

    if (!env) {
        env = 'dev';
    }

    /**
     * Variables here are available in all tests
     */
    var config = {
        env: env,
        myVarName: 'someValue',
        baseUrl: 'http://localhost:80'
    }

    /**
     * Drivers for tests - currently configured value is good for Linux
     */
    karate.configure('driver', {
        type: 'chrome',
        // descomentar para chromium bajo linux
        // executable: '/usr/bin/chromium-browser',
        // executable: "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
        executable: "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
        // executable : "C:\\Users\\49leg\\Downloads\\chromedriver-win64(1)\\chromedriver-win64\\chromedriver.exe",
        showDriverLog: true,
        addOptions: ["--remote-allow-origins=*"]
    })

    if (env == 'dev') {
        // customize
        // e.g. config.foo = 'bar';
    } else if (env == 'e2e') {
        // customize
    }
    return config;
}